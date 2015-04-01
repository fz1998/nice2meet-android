/*
 * libjingle
 * Copyright 2014, Google Inc.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *  1. Redistributions of source code must retain the above copyright notice,
 *     this list of conditions and the following disclaimer.
 *  2. Redistributions in binary form must reproduce the above copyright notice,
 *     this list of conditions and the following disclaimer in the documentation
 *     and/or other materials provided with the distribution.
 *  3. The name of the author may not be used to endorse or promote products
 *     derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE AUTHOR ``AS IS'' AND ANY EXPRESS OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
 * MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO
 * EVENT SHALL THE AUTHOR BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS;
 * OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR
 * OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF
 * ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package org.st;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.os.Build;
import android.util.Log;

import com.threebody.sdk.util.LoggerUtil;

import org.webrtc.VideoRenderer;
import org.webrtc.VideoRenderer.I420Frame;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingQueue;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Efficiently renders YUV frames using the GPU for CSC.
 * Clients will want first to call setView() to pass GLSurfaceView
 * and then for each video stream either create instance of VideoRenderer using
 * createGui() call or VideoRenderer.Callbacks interface using create() call.
 * Only one instance of the class can be created.
 */
public class VideoRendererView implements GLSurfaceView.Renderer {
  private final String TAG = "VideoRendererView";
  private GLSurfaceView surface;
  // Indicates if SurfaceView.Renderer.onSurfaceCreated was called.
  // If true then for every newly created yuv image renderer createTexture()
  // should be called. The variable is accessed on multiple threads and
  // all accesses are synchronized on yuvImageRenderers' object lock.
  private boolean onSurfaceCreatedCalled;
  // List of yuv renderers.
//  private ArrayList<YuvImageRenderer> yuvImageRenderers;
  private YuvImageRenderer yuvImageRenderer;//yuvImageRenderer;
  private int program;
  private boolean local;
  private int videoWidth = 0;
  private int videoHeight = 0;

  private final String VERTEX_SHADER_STRING =
      "varying vec2 interp_tc;\n" +
      "attribute vec4 in_pos;\n" +
      "attribute vec2 in_tc;\n" +
      "\n" +
      "void main() {\n" +
      "  gl_Position = in_pos;\n" +
      "  interp_tc = in_tc;\n" +
      "}\n";

  private final String FRAGMENT_SHADER_STRING =
      "precision mediump float;\n" +
      "varying vec2 interp_tc;\n" +
      "\n" +
      "uniform sampler2D y_tex;\n" +
      "uniform sampler2D u_tex;\n" +
      "uniform sampler2D v_tex;\n" +
      "\n" +
      "void main() {\n" +
      // CSC according to http://www.fourcc.org/fccyvrgb.php
      "  float y = texture2D(y_tex, interp_tc).r;\n" +
      "  float u = texture2D(u_tex, interp_tc).r - 0.5;\n" +
      "  float v = texture2D(v_tex, interp_tc).r - 0.5;\n" +
      "  gl_FragColor = vec4(y + 1.403 * v, " +
      "                      y - 0.344 * u - 0.714 * v, " +
      "                      y + 1.77 * u, 1);\n" +
      "}\n";

  public VideoRendererView(GLSurfaceView surface, boolean local) {
    this.surface = surface;
    this.local = local;
    // Create an OpenGL ES 2.0 context.
      if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.HONEYCOMB)
      {
          surface.setPreserveEGLContextOnPause(true);
      }

    surface.setEGLContextClientVersion(2);
    surface.setRenderer(this);
    surface.setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
    
    /**
     * Creates VideoRenderer.Callbacks with top left corner at (x, y) and
     * resolution (width, height). All parameters are in percentage of
     * screen resolution.Gu
     */
	yuvImageRenderer = new YuvImageRenderer(this.surface);
	if (this.onSurfaceCreatedCalled) {
		// onSurfaceCreated has already been called for VideoRendererGui -
		// need to create texture for new image and add image to the
		// rendering list.
		final CountDownLatch countDownLatch = new CountDownLatch(1);
		this.surface.queueEvent(new Runnable() {
			public void run() {
				yuvImageRenderer.createTextures(program);
				countDownLatch.countDown();
			}
		});
		// Wait for task completion.
		try {
			countDownLatch.await();
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
	}   
  }
  
  public VideoRenderer.Callbacks getRendererCallback() {
      LoggerUtil.info(getClass().getName(), " id = "+yuvImageRenderer.toString());
	  return yuvImageRenderer;
  }
  
  private void layoutVideo() {
	  int viewWidth = this.surface.getWidth();
	  int viewHeight = this.surface.getHeight();
	  
	  if (videoWidth <= 0 || videoHeight <= 0|| viewWidth <= 0 || viewHeight <= 0) {
		  synchronized (yuvImageRenderer) {
			  yuvImageRenderer.setRenderRegion(0, 0, 100, 100);
		  }
		  return;
	  }

	  synchronized (yuvImageRenderer) {
		  if (videoWidth * viewHeight >= videoHeight * viewWidth) {
			  int xVideoCut = (100 * videoWidth * viewHeight) / (videoHeight * viewWidth) - 100;
			  yuvImageRenderer.setRenderRegion(-xVideoCut / 2, 0, 100 + xVideoCut, 100);
		  } else {
			  int yVideoCut = (100 * videoHeight * viewWidth) / (videoWidth * viewHeight) - 100;
			  yuvImageRenderer.setRenderRegion(0, -yVideoCut / 2, 100, 100 + yVideoCut);
		  }    
	  }
  }

  // Poor-man's assert(): die with |msg| unless |condition| is true.
  private void abortUnless(boolean condition, String msg) {
    if (!condition) {
    	Log.e(TAG, msg);
//      throw new RuntimeException(msg);
    }
  }

  // Assert that no OpenGL ES 2.0 error has been raised.
  private void checkNoGLES2Error() {
    int error = GLES20.glGetError();
    abortUnless(error == GLES20.GL_NO_ERROR, "GLES20 error: " + error);
  }

  // Wrap a float[] in a direct FloatBuffer using native byte order.
  private FloatBuffer directNativeFloatBuffer(float[] array) {
    FloatBuffer buffer = ByteBuffer.allocateDirect(array.length * 4).order(
        ByteOrder.nativeOrder()).asFloatBuffer();
    buffer.put(array);
    buffer.flip();
    return buffer;
  }

  // Compile & attach a |type| shader specified by |source| to |program|.
  private void addShaderTo(
      int type, String source, int program) {
    int[] result = new int[] {
        GLES20.GL_FALSE
    };
    int shader = GLES20.glCreateShader(type);
    GLES20.glShaderSource(shader, source);
    GLES20.glCompileShader(shader);
    GLES20.glGetShaderiv(shader, GLES20.GL_COMPILE_STATUS, result, 0);
    abortUnless(result[0] == GLES20.GL_TRUE,
        GLES20.glGetShaderInfoLog(shader) + ", source: " + source);
    GLES20.glAttachShader(program, shader);
    GLES20.glDeleteShader(shader);

    checkNoGLES2Error();
  }

  /**
   * Class used to display stream of YUV420 frames at particular location
   * on a screen. New video frames are sent to display using renderFrame()
   * call.
   */
  private class YuvImageRenderer implements VideoRenderer.Callbacks {
    private GLSurfaceView surface;
    private int program;
    private FloatBuffer textureVertices;
    private int[] yuvTextures = { -1, -1, -1 };

    // Render frame queue - accessed by two threads. renderFrame() call does
    // an offer (writing I420Frame to render) and early-returns (recording
    // a dropped frame) if that queue is full. draw() call does a peek(),
    // copies frame to texture and then removes it from a queue using poll().
    LinkedBlockingQueue<I420Frame> frameToRenderQueue;
    // Local copy of incoming video frame.
    private I420Frame frameToRender;
    // Flag if renderFrame() was ever called
    boolean seenFrame ;
    // Total number of video frames received in renderFrame() call.
    private int framesReceived;
    // Number of video frames dropped by renderFrame() because previous
    // frame has not been rendered yet.
    private int framesDropped;
    // Number of rendered video frames.
    private int framesRendered;
    // Time in ns when the first video frame was rendered.
    private long startTimeNs = -1;
    // Time in ns spent in draw() function.
    private long drawTimeNs;
    // Time in ns spent in renderFrame() function - including copying frame
    // data to rendering planes
    private long copyTimeNs;

    // Texture Coordinates mapping the entire texture.
    private final FloatBuffer textureCoords = directNativeFloatBuffer(
        new float[] {
            0, 0, 0, 1, 1, 0, 1, 1
        });

    private YuvImageRenderer(GLSurfaceView surface) {
      Log.v(TAG, "YuvImageRenderer.Create");
      this.surface = surface;
      frameToRenderQueue = new LinkedBlockingQueue<I420Frame>(1);      
      setRenderRegion(0, 0, 100, 100);
    }
    
    private void setRenderRegion(int x, int y, int width, int height) {
        // Create texture vertices.
        float xLeft = local ? -(x - 50) / 50.0f : (x - 50) / 50.0f;
        float yTop = (50 - y) / 50.0f;
        float xRight = local ? -(x + width - 50) / 50.0f : (x + width - 50) / 50.0f;
        float yBottom = (50 - y - height) / 50.0f;
        float textureVeticesFloat[] = new float[] {
            xLeft, yTop,
            xLeft, yBottom,
            xRight, yTop,
            xRight, yBottom
        };
        textureVertices = directNativeFloatBuffer(textureVeticesFloat);   	
    }

    private void createTextures(int program) {
      Log.v(TAG, "  YuvImageRenderer.createTextures");
      this.program = program;

      // Generate 3 texture ids for Y/U/V and place them into |textures|.
      GLES20.glGenTextures(3, yuvTextures, 0);
      for (int i = 0; i < 3; i++)  {
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0 + i);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, yuvTextures[i]);
        GLES20.glTexImage2D(GLES20.GL_TEXTURE_2D, 0, GLES20.GL_LUMINANCE,
            128, 128, 0, GLES20.GL_LUMINANCE, GLES20.GL_UNSIGNED_BYTE, null);
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D,
            GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR);
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D,
            GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D,
            GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE);
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D,
            GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE);
      }
      checkNoGLES2Error();
    }

    private void draw() {
        LoggerUtil.info(getClass().getName(), " draw = "+VideoRendererView.this.toString()+" seenFrame = "+seenFrame);
      long now = System.nanoTime();
      if (!seenFrame) {
        // No frame received yet - nothing to render.
        return;
      }
      I420Frame frameFromQueue;
      synchronized (frameToRenderQueue) {
        frameFromQueue = frameToRenderQueue.peek();
        if (frameFromQueue != null && startTimeNs == -1) {
          startTimeNs = now;
        }
        for (int i = 0; i < 3; ++i) {
          int w = (i == 0) ? frameToRender.width : frameToRender.width / 2;
          int h = (i == 0) ? frameToRender.height : frameToRender.height / 2;
          GLES20.glActiveTexture(GLES20.GL_TEXTURE0 + i);
          GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, yuvTextures[i]);
          if (frameFromQueue != null) {
            GLES20.glTexImage2D(GLES20.GL_TEXTURE_2D, 0, GLES20.GL_LUMINANCE,
                w, h, 0, GLES20.GL_LUMINANCE, GLES20.GL_UNSIGNED_BYTE,
                frameFromQueue.yuvPlanes[i]);
          }
        }
        if (frameFromQueue != null) {
          frameToRenderQueue.poll();
        }
      }
      int posLocation = GLES20.glGetAttribLocation(program, "in_pos");
      GLES20.glEnableVertexAttribArray(posLocation);
      GLES20.glVertexAttribPointer(
          posLocation, 2, GLES20.GL_FLOAT, false, 0, textureVertices);

      int texLocation = GLES20.glGetAttribLocation(program, "in_tc");
      GLES20.glEnableVertexAttribArray(texLocation);
      GLES20.glVertexAttribPointer(
          texLocation, 2, GLES20.GL_FLOAT, false, 0, textureCoords);

      GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4);

      GLES20.glDisableVertexAttribArray(posLocation);
      GLES20.glDisableVertexAttribArray(texLocation);

      checkNoGLES2Error();

      if (frameFromQueue != null) {
        framesRendered++;
        drawTimeNs += (System.nanoTime() - now);
        if ((framesRendered % 150) == 0) {
          logStatistics();
        }
      }
    }

    private void logStatistics() {
      long timeSinceFirstFrameNs = System.nanoTime() - startTimeNs;
      Log.v(TAG, "Frames received: " + framesReceived + ". Dropped: " +
          framesDropped + ". Rendered: " + framesRendered);
      if (framesReceived > 0 && framesRendered > 0) {
        Log.v(TAG, "Duration: " + (int)(timeSinceFirstFrameNs / 1e6) +
            " ms. FPS: " + (float)framesRendered * 1e9 / timeSinceFirstFrameNs);
        Log.v(TAG, "Draw time: " +
            (int) (drawTimeNs / (1000 * framesRendered)) + " us. Copy time: " +
            (int) (copyTimeNs / (1000 * framesReceived)) + " us");
      }
    }

    @Override
    public void setSize(final int width, final int height) {
      Log.v(TAG, "YuvImageRenderer.setSize: " + width + " x " + height + (local ? ", local" : " remote"));
//      if (width > 320 || height > 320) {
//    	  return;
//      }
      int[] strides = { width, width / 2, width / 2  };
      // Frame re-allocation need to be synchronized with copying
      // frame to textures in draw() function to avoid re-allocating
      // the frame while it is being copied.
      synchronized (frameToRenderQueue) {
        // Clear rendering queue
        frameToRenderQueue.poll();
        // Re-allocate / allocate the frame
        frameToRender = new I420Frame(width, height, strides, null);
      }
      videoWidth = width;
      videoHeight = height;
      layoutVideo();
    }

    @Override
    public synchronized void renderFrame(I420Frame frame) {
        LoggerUtil.info(getClass().getName(), " renderFrame = "+VideoRendererView.this.toString());
      long now = System.nanoTime();
      framesReceived++;
      // Check input frame parameters.
      if (!(frame.yuvStrides[0] == frame.width &&
          frame.yuvStrides[1] == frame.width / 2 &&
          frame.yuvStrides[2] == frame.width / 2)) {
        Log.e(TAG, "Incorrect strides " + frame.yuvStrides[0] + ", " +
            frame.yuvStrides[1] + ", " + frame.yuvStrides[2]);
        return;
      }
      // Skip rendering of this frame if setSize() was not called.
      if (frameToRender == null) {
        framesDropped++;
        return;
      }
      // Check incoming frame dimensions
      if (frame.width != frameToRender.width ||
          frame.height != frameToRender.height) {
        throw new RuntimeException("Wrong frame size " +
            frame.width + " x " + frame.height);
      }

      if (frameToRenderQueue.size() > 0) {
        // Skip rendering of this frame if previous frame was not rendered yet.
        framesDropped++;
        return;
      }
      frameToRender.copyFrom(frame);
      copyTimeNs += (System.nanoTime() - now);
      frameToRenderQueue.offer(frameToRender);
      seenFrame = true;
      surface.requestRender();
    }
  }
  
  @Override
  public void onSurfaceCreated(GL10 unused, EGLConfig config) {
    Log.v(TAG, "VideoRendererGui.onSurfaceCreated");

    // Create program.
    program = GLES20.glCreateProgram();
    addShaderTo(GLES20.GL_VERTEX_SHADER, VERTEX_SHADER_STRING, program);
    addShaderTo(GLES20.GL_FRAGMENT_SHADER, FRAGMENT_SHADER_STRING, program);

    GLES20.glLinkProgram(program);
    int[] result = new int[] {
        GLES20.GL_FALSE
    };
    result[0] = GLES20.GL_FALSE;
    GLES20.glGetProgramiv(program, GLES20.GL_LINK_STATUS, result, 0);
    abortUnless(result[0] == GLES20.GL_TRUE,
        GLES20.glGetProgramInfoLog(program));
    GLES20.glUseProgram(program);

    GLES20.glUniform1i(GLES20.glGetUniformLocation(program, "y_tex"), 0);
    GLES20.glUniform1i(GLES20.glGetUniformLocation(program, "u_tex"), 1);
    GLES20.glUniform1i(GLES20.glGetUniformLocation(program, "v_tex"), 2);

    synchronized (yuvImageRenderer) {
    	// Create textures for all images.
    	yuvImageRenderer.createTextures(program);
    	onSurfaceCreatedCalled = true;
    }
    checkNoGLES2Error();
    
    GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
  }

  @Override
  public void onSurfaceChanged(GL10 unused, int width, int height) {
    Log.v(TAG, "VideoRendererGui.onSurfaceChanged: " +
        width + " x " + height + "  ");
    GLES20.glViewport(0, 0, width, height);
    layoutVideo();
  }

  @Override
  public void onDrawFrame(GL10 unused) {

    GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
    synchronized (yuvImageRenderer) {
    	yuvImageRenderer.draw();
    }
  }

}
