package com.threebody.conference.ui.view.video;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.hardware.Camera.PreviewCallback;
import android.os.Build;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageView;

import com.threebody.conference.ui.util.LoggerUtil;
import com.threebody.sdk.domain.VideoBean;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

/*SurfaceView不需要通过线程来更新视图，使用时还需要对其进行创建，销毁情况改变时进行监听，这就是需要实现Callback接口
PreviewCallback回到接口，用于显示预览框
*/
public class LocalVideoView extends SurfaceView implements PreviewCallback, Callback, VideoView{
	Context activity;
	OnFramenLister listener;
	
	/**
	 * save device 
	 */
	public final static String DEVICES="devices";
	
	public final static String DEVICE_NAME="name";
	
	public final static String VIDEOFORMAT="videoFormat";
	
	public final static String ISFIRSTOPEN="isFirstOpen";
    public  static int WIDTH = 176;
    public  static int HEIGHT = 144;
	
	private int cameraFPS = 0, cameraWidth = 0, cameraHeight = 0;
	
	private Camera camera;
	private int currentCamera = CameraInfo.CAMERA_FACING_FRONT;//代表摄像头的方位，目前有定义值两个分别为CAMERA_FACING_FRONT前置和CAMERA_FACING_BACK后置 
	private int numOfCamera = 1;
	File file = new File("/sdcard/testView.yuv");
	private FileOutputStream fos;
//	private Handler handler;
	private SurfaceHolder holder;
	private static boolean initVideo = true;
	private static boolean isShareing = false;
	private boolean isPortrait = true;
	private int frameSize;
    private int[] rgba;
//	private VideoCommon videoCommon = CommonFactory.getInstance().getVideoCommon();
	private boolean isPreview = false;
	private boolean isOpen = false;
	
	public static boolean isDestroyed = false;
	
	public LocalVideoView(Context context) {
		super(context);
        activity = context;
		init();
		// TODO Auto-generated constructor stub
	}
	
	public LocalVideoView(Context context, AttributeSet attrs) {
		super(context, attrs);
		activity =  context;
		
		init();
	}	
	
	public LocalVideoView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
		// TODO Auto-generated constructor stub
	}

	public void init(){
		
		if(holder ==null){
			holder = this.getHolder();
		}
        ImageView view ;
        SurfaceView s;
        LoggerUtil.info(getClass().getName(), "holder = " + holder.toString());
		
		holder.addCallback(this);
		holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
//		try {
//			fos = new FileOutputStream(file);
//		} catch (FileNotFoundException e) {
//			e.printStackTrace();
//		}
	}

	public void openCamera(){
		if(Build.VERSION.SDK_INT > 8){
			numOfCamera = Camera.getNumberOfCameras();//返回数量的物理相机，在这个设备上
			if(numOfCamera == 1){
                LoggerUtil.info(getClass().getName(), "1111111111111111");
				currentCamera = CameraInfo.CAMERA_FACING_BACK;
				camera = Camera.open();//底层默认打开的是后置摄像头
			}else{
				
				camera = Camera.open(currentCamera);
			}
		}else{
			camera = Camera.open();
		}
		isOpen = true;
	}
	
	public void startCamera() {	
//		handler = ((MediaCommonImpl)CommonFactory.getInstance().getMediaCommon()).getHandler();
		
		if(camera == null && !isOpen ){
			openCamera();
		}
		
		camera.setDisplayOrientation(degrees);
		
		setCameraParameters();
		camera.setPreviewCallback(this);
		try {
			camera.setPreviewDisplay(holder);
			
			
		} catch (IOException e) {
			e.printStackTrace();
		}
//		changePreview(true);
        camera.startPreview();
//		setBackgroundColor(0);
        LoggerUtil.info(getClass().getName(), "start camera");
	}
	
	/*
	 * 设置相机属性
	 */
	private void setCameraParameters() {
		Camera.Parameters parameters = camera.getParameters();
	    List<Camera.Size> previewSizes = parameters.getSupportedPreviewSizes();

	    cameraFPS = parameters.getPreviewFrameRate();
	    for(Camera.Size size : previewSizes){
	    	if(cameraWidth==0){
	    		cameraWidth = size.width;
	    		cameraHeight = size.height;
	    	}
	    	if(size.width <= cameraWidth){
	    		cameraWidth = size.width;
	    		cameraHeight = size.height;
	    	}
//	    	log.info("Camera.Size = " + size.width + ", " + size.height + ", " + fps);
	    }
	    WIDTH = cameraWidth;
        HEIGHT = cameraHeight;
	    frameSize = cameraWidth * cameraHeight;
	    rgba = new int[frameSize+1];
	    
//	    log.info("camera width="+cameraWidth+" height="+cameraHeight+" fps="+cameraFPS+" handle+"+AndroidCallback.getInstance().getHandle());
	    //调用Jni初始化本地视频接口
	    if(initVideo){
            LoggerUtil.info(getClass().getName(), "initVideo");
//	    	videoCommon.initializeMyVideo(cameraHeight, cameraWidth, cameraFPS);
	    	initVideo = false;
	    }
//	    parameters.setRotation(90);
	    
//	    parameters.set("rotation", 90);
//	    parameters.set("orietation", "portrait");
	    parameters.setPreviewSize(cameraWidth, cameraHeight);
//		note:有些设备在设置它的帧率时会报错！！！
//		params.setPreviewFrameRate(10);
		camera.setParameters(parameters);
	}

	/**
	 * 相应地，在surfaceDestroyed中也需要释放该Camera对象。
	 * 我们将首先调用stopPreview，以确保应该释放的资源都被清理。
	 */
	
	public void destroyCamera() {
        LoggerUtil.info(getClass().getName(), "surfaceDestroyed !!!!!!!!!!!");
		if(camera != null){

            LoggerUtil.info(getClass().getName(), "camera != null !!!!!!!!!!!");
			camera.setPreviewCallback(null);
			changePreview(false);//停止更新预览
			camera.release();//释放资源
			camera = null;
//			isShareing = false;
			
//			LocalCommonFactory.getInstance().getContactDataCommon().initLocalCamera();
			isOpen = false;
			invalidate();
//			videoPreview = null;
		}
	}
	private byte[] rotateYUV420SPFrontfacing(byte[] src, int width, int height) {
		byte[] des = new byte[src.length];
		int wh = width * height;
		int uv = wh/4;
		//旋转Y
		int k = 0;
		for(int i = width - 1; i >= 0; i--){
			for(int j = 0; j < height; j++){
				des[k++] = src[width * j + i];
			}
		}
		for(int i = width - 2; i >= 0; i -= 2){
			for(int j = 0; j < height/2; j++){
				des[k] = src[wh + width * j + i + 1];
				des[k+uv] = src[wh + width * j + i];
				k++;
			}
		}
		return des;
	}
	public static byte[] rotateYUV420SPBackfacing(byte[] src,int width,int height)
	 {
		byte[] des = new byte[src.length];
		int wh = width * height;
		int uv = wh/4;
		  //旋转Y
		  int k = 0;
		  for(int i=0;i<width;i++) {
		   for(int j = height - 1; j >= 0; j--) 
		   {
		         des[k] = src[width*j + i];   
		         k++;
		   }
		  }
		  
		  for(int i=0;i<width;i+=2) {
		   for(int j = height/2 - 1; j >= 0;j--) 
		   { 
			   des[k] = src[wh + width*j + i+1]; 
			   des[k+uv] = src[wh + width*j + i];
			   k++;
		   }
		  }
		  
	  return des;
	  
	 }
	private byte[] rotateYUV420( byte[] src, int width, int height){
		byte[] des = new byte[src.length];
		int i=0,j=0,n=0; 
	    int hw=width/2,hh=height/2; 
	    int wh = width * height;
//	    for(j=width;j>0;j--) {
//	        for(i=0;i<height;i++) { 
//	           des[n++] = src[width*i+j]; 
//	        } 
//	    }
	    for(i = 0; i < width; i++){
	    	for(j = height - 1; j >= 0; j--){
	    		des[n++] = src[width * j + i];
	    	}
	    }
//	    log.info("n= "+n +" ynum = "+wh);
	    for( i =0; i < width; i+=2){
	    	for(j = height/2 - 1; j >= 0;j--){
	    		des[n++] = src[wh + width * j + i];
	    		des[n++] = src[wh + width * j + i+1];
	    	}
	    }
//	    unsigned char *ptmp = src+width*height; 
//	    for(j=hw;j>0;j--) 
//	        for(i=0;i<hh;i++) 
//	        { 
//	            des[n++] = ptmp[hw*i+j]; 
//	        } 

//	    ptmp = src+width*height*5/4; 
//	    for(j=hw;j>0;j--) 
//	    for(i=0;i<hh;i++) 
//	    { 
//	        des[n++] = ptmp[hw*i+j]; 
//	    }
	    return des;
	}
	//前后摄像头切换有bug
	@Override
	public synchronized void onPreviewFrame(byte[] data, Camera camera) {
		
		LoggerUtil.info(getClass().getName(),"onPreviewFrameonPreviewFrameonPreviewFrame");
		
		if (data == null) {
			return;
		}
		
		//System.out.println(Conference4PhoneActivity.isShareImageOpen);
//		if(Conference4PhoneActivity.isShareImageOpen){
			rgba = decodeYUV420SP(rgba, data, cameraWidth, cameraHeight);
//			LocalCommonFactory.getInstance().getContactDataCommon().sendLocalCameraImage(rgba, cameraWidth, cameraHeight);
//		}
		
		if(true){
			// 关闭摄像头预览回调
			camera.setPreviewCallback(null);
			
			//转换后可以显示颜色，否则显示黑白
			
//			if(isPortrait){
				if(currentCamera == CameraInfo.CAMERA_FACING_BACK){
					yv12buf = rotateYUV420SPBackfacing(data, cameraWidth, cameraHeight);
				}else {
					yv12buf = rotateYUV420SPFrontfacing(data, cameraWidth, cameraHeight);
				}
//				videoCommon.sendMyVideoData(yv12buf, yv12buf.length, cameraHeight, cameraWidth);
//				log.info("portrait data length = "+data.length);
//			}else{
//				yv12buf = changeYUV420SP2P(data, data.length);
////				videoCommon.sendMyVideoData(yv12buf, yv12buf.length, cameraWidth, cameraHeight);
////				log.info("landscape data length = "+data.length);
//			}
			
//			log.info("yuv length = "+yv12buf.length);
//			yv12buf = rotateYUV420(yv12buf, cameraWidth, cameraHeight);
			
//			try {
//				fos.write(yv12buf);
//				fos.flush();
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
			
//			if(CommonFactory.getInstance().getMediaCommon().isLocal_video_channel_flag()){
				LoggerUtil.info(getClass().getName(), "cameraWidth = "+cameraWidth +" cameraHeight = "+cameraHeight+" fps ="+cameraFPS+"video data length="+yv12buf.length);
				
				
//			}

		}
		if(listener != null){
            VideoBean videoBean = new VideoBean();
            videoBean.setVideoDataByInt(rgba);
//            videoBean.setVideoData(yv12buf);
            videoBean.setWidth(cameraWidth);
            videoBean.setHeight(cameraHeight);
            listener.OnPreviewFrame(videoBean);
        }

		// 打开摄像头预览回调
		camera.setPreviewCallback(this);
	}
	
	int[] rgbIntBuf;
	byte[] yv12buf;

	private int degrees = 90;
	
	/**
	 * http://eyehere.net/2011/android-camera-2/
	 * 经测试，可行
	 * @param rgb
	 * @param yuv420sp
	 * @param width
	 * @param height
	 */
	private int[] decodeYUV420SP(int[] rgb, byte[] yuv420sp, int width, int height) {
	    final int frameSize = width * height;

	    for (int j = 0, yp = 0; j < height; j++) {
	        int uvp = frameSize + (j >> 1) * width, u = 0, v = 0;
	        for (int i = 0; i < width; i++, yp++) {
	            int y = (0xff & ((int) yuv420sp[yp])) - 16;
	            if (y < 0) y = 0;
	            if ((i & 1) == 0) {
	                v = (0xff & yuv420sp[uvp++]) - 128;
	                u = (0xff & yuv420sp[uvp++]) - 128;
	            }

	            int y1192 = 1192 * y;
	            int r = (y1192 + 1634 * v);
	            int g = (y1192 - 833 * v - 400 * u);
	            int b = (y1192 + 2066 * u);

	            if (r < 0) r = 0; else if (r > 262143) r = 262143;
	            if (g < 0) g = 0; else if (g > 262143) g = 262143;
	            if (b < 0) b = 0; else if (b > 262143) b = 262143;

	            rgb[yp] = 0xff000000 | ((r << 6) & 0xff0000) | ((g >> 2) & 0xff00) | ((b >> 10) & 0xff);
	        }
	    }
	    return rgb;
	}
	
	/**
	 * enming http://www.360doc.com/content/12/0606/17/10144181_216430261.shtml
	 *	已测试，不行
	 * @param rgbBuf
	 * @param yuv420sp
	 * @param width
	 * @param height
	 */
    private void decodeYUV420SP(byte[] rgbBuf, byte[] yuv420sp, int width, int height) {
    	final int frameSize = width * height;
		if (rgbBuf == null)
			throw new NullPointerException("buffer 'rgbBuf' is null");
		if (rgbBuf.length < frameSize * 3)
			throw new IllegalArgumentException("buffer 'rgbBuf' size "
					+ rgbBuf.length + " < minimum " + frameSize * 3);

		if (yuv420sp == null)
			throw new NullPointerException("buffer 'yuv420sp' is null");

		if (yuv420sp.length < frameSize * 3 / 2)
			throw new IllegalArgumentException("buffer 'yuv420sp' size " + yuv420sp.length
					+ " < minimum " + frameSize * 3 / 2);
    	
    	int i = 0, y = 0;
    	int uvp = 0, u = 0, v = 0;
    	int y1192 = 0, r = 0, g = 0, b = 0;
    	
    	for (int j = 0, yp = 0; j < height; j++) {
    		uvp = frameSize + (j >> 1) * width;
    		u = 0;
    		v = 0;
    		for (i = 0; i < width; i++, yp++) {
    			y = (0xff & ((int) yuv420sp[yp])) - 16;
    			if (y < 0) y = 0;
    			if ((i & 1) == 0) {
    				v = (0xff & yuv420sp[uvp++]) - 128;
    				u = (0xff & yuv420sp[uvp++]) - 128;
    			}
    			
    			y1192 = 1192 * y;
    			r = (y1192 + 1634 * v);
    			g = (y1192 - 833 * v - 400 * u);
    			b = (y1192 + 2066 * u);
    			
    			if (r < 0) r = 0; else if (r > 262143) r = 262143;
    			if (g < 0) g = 0; else if (g > 262143) g = 262143;
    			if (b < 0) b = 0; else if (b > 262143) b = 262143;
    			
    			rgbBuf[yp * 3] = (byte)(r >> 10);
    			rgbBuf[yp * 3 + 1] = (byte)(g >> 10);
    			rgbBuf[yp * 3 + 2] = (byte)(b >> 10);
    		}
    	}
    }
	
	/**
	 * 发转图片，摄像头采集过来的bitmap需要反转一下发到服务器
	 * @return
	 */
	private byte[] changeBitmap(byte[] data){
		Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
		Matrix matrix = new Matrix();
		matrix.postScale(1, -1);
		Bitmap bitmap2 = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
		bitmap.recycle();
		byte[] buffer = bitmap2.getNinePatchChunk().clone();
		bitmap2.recycle();
		return buffer;
	}

	private byte[] changeYUV420SP2P(byte[] data, int length) {
		int pixNum = length*2/3;
		byte[] yv12buf = new byte[length];
		System.arraycopy(data, 0, yv12buf, 0, pixNum);
		int index = pixNum;
		for (int i = pixNum + 1; i < length; i += 2) {
			yv12buf[index++] = data[i];
		}
		for (int i = pixNum; i < length; i += 2) {
			yv12buf[index++] = data[i];
		}
		
//		if(CommonFactory.getInstance().getMediaCommon().isLocal_video_channel_flag()){
////			byte[] buffer = changeBitmap(yv12buf);
//			ConferenceJni.sendVideoData(AndroidCallback.getInstance().getHandle(), yv12buf.length, yv12buf);
//		}
		
		return yv12buf;
	}
	
	public byte[] decodeYUV420SP2RGB(byte[] yuv420sp, int width, int height) {
	    final int frameSize = width * height;   
	    
	    byte[] rgbBuf = new byte[frameSize * 3];
	    
	   // if (rgbBuf == null) throw new NullPointerException("buffer 'rgbBuf' is null");   
	    if (rgbBuf.length < frameSize * 3) throw new IllegalArgumentException("buffer 'rgbBuf' size "  + rgbBuf.length + " < minimum " + frameSize * 3);   
	  
	    if (yuv420sp == null) throw new NullPointerException("buffer 'yuv420sp' is null");   
	  
	    if (yuv420sp.length < frameSize * 3 / 2) throw new IllegalArgumentException("buffer 'yuv420sp' size " + yuv420sp.length + " < minimum " + frameSize * 3 / 2);   
	       
	    int i = 0, y = 0;   
	    int uvp = 0, u = 0, v = 0;   
	    int y1192 = 0, r = 0, g = 0, b = 0;   
	       
	    for (int j = 0, yp = 0; j < height; j++) {   
	         uvp = frameSize + (j >> 1) * width;   
	         u = 0;   
	         v = 0;   
	         for (i = 0; i < width; i++, yp++) {   
	             y = (0xff & ((int) yuv420sp[yp])) - 16;   
	             if (y < 0) y = 0;   
	             if ((i & 1) == 0) {   
	                 v = (0xff & yuv420sp[uvp++]) - 128;   
	                 u = (0xff & yuv420sp[uvp++]) - 128;   
	             }   
	               
	             y1192 = 1192 * y;   
	             r = (y1192 + 1634 * v);   
	             g = (y1192 - 833 * v - 400 * u);   
	             b = (y1192 + 2066 * u);   
	               
	             if (r < 0) r = 0; else if (r > 262143) r = 262143;   
	             if (g < 0) g = 0; else if (g > 262143) g = 262143;   
	             if (b < 0) b = 0; else if (b > 262143) b = 262143;   
	               
	             rgbBuf[yp * 3] = (byte)(r >> 10);   
	             rgbBuf[yp * 3 + 1] = (byte)(g >> 10);   
	             rgbBuf[yp * 3 + 2] = (byte)(b >> 10);
	         }   
	     }//for
	    return rgbBuf;
	 }// decodeYUV420Sp2RGB  
	
	/**
	 * 此方法是专门为星网锐捷定制的YUV422(UYVY)格式转换
	 * @param data 原来的YUV422格式
	 * @param length 数据长度
	 * @return 返回YVU420格式
	 */
	private byte[] changeYUV4222YUV420(byte[] data, int length){ 
		  
		int width = 176;
		int height = 144;
		int ynum=width*height;  
		int i,j,k=0;  
		       
		byte[] yuv420 = new byte[length];
		//得到Y分量  
		for(i=0;i<ynum;i++){  
			yuv420[i]=data[i*2 + 1];  
		}  
		//得到U分量  
		for(i=0;i<height;i++){  
			if((i%2)!=0)continue;  
			
			for(j=0;j<(width/2);j++){  
				if((4*j+1)>(2*width))break;  
				
				yuv420[ynum+k*2*width/4+j]=data[i*2*width+4*j];  
			}  
			
			k++;  
		}  
		
		k=0;  
		//得到V分量  
		for(i=0;i<height;i++){  
			if((i%2)==0)continue;  
			
			for(j=0;j<(width/2);j++){  
				if((4*j+3)>(2*width))break;  
				yuv420[ynum+ynum/4+k*2*width/4+j]=data[i*2*width+4*j+2];  
		                
			}  
			
			k++;  
		}  
		       
		return yuv420;
	}
	
	public void changeCameraFacing(){
		destroyCamera();
//		camera = Camera.open((currentCamera + 1) % numOfCamera);
		currentCamera = (currentCamera + 1) % numOfCamera;
		startCamera();
//		try {
////			camera.setPreviewDisplay(this.getHolder());
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		camera.setPreviewCallback(this);
//		camera.startPreview();
//		invalidate();
        LoggerUtil.info(getClass().getName(), "CameraInfo.CAMERA_FACING_FRONT ================");
		
	}
	
	public void setCurrentCamera(int cameraId){
		currentCamera = cameraId;
	}
	
	public void releaseCamera(){
		if(camera != null){
			camera.setPreviewCallback(null);
			changePreview(false);
			camera.release();
			camera = null;
			isPreview = false;
		}
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
        LoggerUtil.info(getClass().getName(), "surfaceChangedsurfaceChangedsurfaceChanged");
		
	}
	
	@Override
	public void surfaceCreated(SurfaceHolder holder) {
        LoggerUtil.info(getClass().getName(), "surfaceCreatedsurfaceCreatedsurfaceCreated");
        startCamera();
//		changeVedioState(true);
//		((Conference4PhoneActivity)activity).checkSyncVideo();
		isDestroyed = false;
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
        LoggerUtil.info(getClass().getName(), "surfaceDestroyedsurfaceDestroyedsurfaceDestroyed");
		//changeVedioState(false);
		if(getCamera()){
			isDestroyed = true;
		}
		destroyCamera();
	}
	
//	private void changeVedioState(boolean isOpen){
//		CommonFactory.getInstance().getUserCommon().onChangeVideoState(
//				CommonFactory.getInstance().getUserCommon().getOwnID(), isOpen);
//	}

	@Override
	public void resetSize(int width, int height) {
		// TODO Auto-generated method stub
	}

	@Override
	public void setVideoBean(VideoBean videoBean) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setLarge(boolean isLarge) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isLarge() {
		return false;
	}
	public boolean getCamera(){		
		return camera != null;
	}
	
	public  void setInitVideo(boolean initVideo){
		LocalVideoView.initVideo = initVideo;
	}

	@Override
	public void setStatus(boolean isMove) {
		if(isMove){
			camera.setPreviewCallback(null);
			changePreview(false);
			isPreview = false;
		}else{
			camera.setPreviewCallback(this);
			changePreview(true);
		}
	}

	public void changeStatus(boolean isOpenCamera) {
		if(isOpenCamera){
			if(camera == null){
				invalidate();
				init();
				startCamera();
			}
		}else{
			if(camera != null){
				destroyCamera();
			}
		}
	}

	public boolean isShareing() {
		return isShareing;
	}

	public  void setShareing(boolean isShareing) {
		LocalVideoView.isShareing = isShareing;
	}
	public void setCameraOrientation(int degrees){
//		CameraInfo info = new Camera.CameraInfo();
//		Camera.getCameraInfo(currentCamera, info);
//		int degrees = 0;
//		int rotation = activity.getWindowManager().getDefaultDisplay().getRotation();
//		switch (rotation) {
//		case Surface.ROTATION_0:
//			
//			break;
//		case Surface.ROTATION_90:
//			degrees = 90;
//			break;
//		case Surface.ROTATION_180:
//			degrees = 180;
//			break;
//		case Surface.ROTATION_270:
//			degrees = 270;
//			break;
//		default:
//			break;
//		}
//		if()
		this.degrees  = degrees;
		if(camera != null){
			try {
				camera.setDisplayOrientation(degrees);
			} catch (Exception e) {
				e.printStackTrace(); 
			}
		}
		
	}

	public void setPortrait(boolean isPortrait) {
		this.isPortrait = isPortrait;
		if(isPortrait){
//			videoCommon.exChange(cameraHeight, cameraWidth);
		}else{
//			videoCommon.exChange(cameraWidth, cameraHeight);
		}
	}

	public void setCameraLandscape() {
		if(camera != null){
			camera.setPreviewCallback(null);
			changePreview(false);
			setCameraOrientation(0);
			camera.setPreviewCallback(this);
			changePreview(true);
		}
	}

	public void setCameraPortrait() {
		if(camera != null){
			camera.setPreviewCallback(null);
			changePreview(false);
			setCameraOrientation(90);
			camera.setPreviewCallback(this);
//			changePreview(true);
            camera.startPreview();
		}
	}
	public void setParams(int width, int height){
		LayoutParams params = (LayoutParams)getLayoutParams();
		params.width = width;
		params.height = height;
		setLayoutParams(params);
	}
	public void reStartLocalView(){
//		if(camera != null){
        LoggerUtil.info(getClass().getName(), "reStartLocalView111");
		if(camera == null){
            LoggerUtil.info(getClass().getName(), "camera is null background!!!!");
//			init();
//			startCamera();
//			changeStatus(true);
		}else {
			if(isDestroyed){
				destroyCamera();
				currentCamera = (currentCamera + 2) % numOfCamera;
				startCamera();
			}
		}
			
//			}
//		}
	}
	private void changePreview(boolean state){
		try {
			if(state){
				if(!isPreview){
					camera.startPreview();
					isPreview = true;
				}
			}else {
				if(isPreview){
					camera.stopPreview();
					isPreview = false;
				}
			}
		} catch (Exception e) {
            LoggerUtil.error(getClass().getName(), e.getMessage());
		}
	}
	public boolean isPreview(){
		return isPreview;
	}

    public OnFramenLister getListener() {
        return listener;
    }

    public void setListener(OnFramenLister listener) {
        this.listener = listener;
    }
}
