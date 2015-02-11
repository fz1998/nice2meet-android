package com.threebody.conference.ui.view.video;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;

import com.threebody.sdk.domain.VideoBean;
import com.threebody.sdk.util.LoggerUtil;

import java.nio.ByteBuffer;

public class RemoteVideoView extends View implements VideoView{
	private Bitmap videoBit = null;
	private Context context;
//	private VideoCommonImpl videoCommon = (VideoCommonImpl) CommonFactory.getInstance().getVideoCommon();
	private ByteBuffer buffer;
	
	//视频数据类
	private VideoBean videoBean;

	private float width = 300f;
	
	//是否是最大化
	private boolean isLarge = false;
	
	private boolean isMove = false;
	private boolean isGetVideoData = false;
	
	private boolean isDrawing = true;
	
	int mWidth,mHeight;
	
	int bitWidth, bitHeight;
	
	//用来显示的view
//	private View layoutView = LayoutInflater.from(context).inflate(R.layout.video_cell_item, null);
	
	private String userName;

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public RemoteVideoView(Context context) {
		super(context);
		this.context = context;
	}

	public RemoteVideoView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
	}

	public RemoteVideoView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.context = context;
	}

//	private void init() {
//		userName = (TextView) layoutView.findViewById(R.id.video_cell_item_name);
//		videoView = (RemoteVideoView) layoutView.findViewById(R.id.video_cell_item_videoView);
//	}
	
	/**
	 * 设置视频数据
	 * 
	 * @param videoBean
	 */
	public void setVideoBean(VideoBean videoBean) {
		try{
//			log.info("videoBean.getWidth() === " + videoBean.getWidth());
            checkSize(videoBean);
			if(videoBit != null){
				videoBean.setWidth(videoBit.getWidth());
				videoBean.setHeight(videoBit.getHeight());
				this.videoBean = videoBean;
//				log.info("video length = "+videoBean.getVideoData().length);
				if(!isMove){
					post(new Runnable() {
                        @Override
                        public void run() {
                            invalidate();
                        }
                    });
				}
			}
		}catch(ClassCastException e){
//			log.info("11111");
			e.printStackTrace();
		}
	}
    private boolean checkSize(VideoBean videoBean){
        if(videoBean.getWidth() != bitWidth || videoBean.getHeight() != bitHeight){
            resetSize(videoBean.getWidth(), videoBean.getHeight());
            return true;
        }
        return false;
    }
	/**
	 * 重置视频分辨率
	 * 
	 * @param width
	 * @param height
	 */
	public void resetSize(int width, int height) {
		LoggerUtil.info(getClass().getName(), "reSetSize width = " + width + " height = " + height);
		bitWidth = width;
		bitHeight = height;
		if(videoBit != null){
			videoBit.recycle();
		}
		
//		if( width * height > 352 * 288 && width > 0){
////			setBackgroundResource(R.drawable.video_nosupport);
//			videoBit = null;
//			isDrawing = false;
//			invalidate();
//		}else{
//			setBackgroundColor(0);
			videoBit = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
			isDrawing = true;
//		}
				
	}


    @Override
    protected void onDraw(Canvas canvas) {
//        super.onDraw(canvas);
        Log.e(VIEW_LOG_TAG, "onDraw 111111111111111");
//		log.info("222222");
        if(isDrawing){
            if(videoBit != null){
                drawBitmap(canvas);
            }else{
                canvas.drawColor(Color.WHITE);
            }
        }
    }

    //	private PaintFlagsDrawFilter pfdf = new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG|Paint.FILTER_BITMAP_FLAG);
	private void drawBitmap(Canvas canvas){
		try {
//			canvas.setDrawFilter(pfdf);
			LoggerUtil.info(getClass().getName(), "3333333");
			if(!isLarge){
//				width = ConferenceApplication.SCREEN_HEIGHT < 480 ? 180f : 300f;				
			}else{
//				width = ConferenceApplication.SCREEN_WIDTH;
			}

			if (videoBean != null && videoBean.getVideoData() != null
					&& videoBean.getVideoData().length > 0) {
				LoggerUtil.info(getClass().getName(), "444444");
				buffer = ByteBuffer.wrap(videoBean.getVideoData());
                LoggerUtil.info(getClass().getName(), "buffer length = "+buffer.remaining());
//				log.info("555555");
//				if(buffer != null && videoBit != null){
					videoBit.copyPixelsFromBuffer(buffer);
//					log.info("666666");
					Matrix m = new Matrix();
					float scaleX , scaleY;
					int xOff = 0;
					scaleX = (float)mWidth/videoBit.getWidth();
					scaleY = (float)mHeight/videoBit.getHeight();

					m.postScale(scaleX, scaleY);
					m.postTranslate(xOff, 0);
					canvas.drawBitmap(videoBit, m, null);
//				}

			}
// else if (videoBean != null && videoBean.getVideoDataByInt() != null
//					&& videoBean.getVideoDataByInt().length > 0) {
//				if(videoBit != null){
//					videoBit.recycle();
//				}
//				LoggerUtil.info(getClass().getName(), "local");
//				videoBit = Bitmap.createBitmap(videoBean.getVideoDataByInt(), 176, 144, Bitmap.Config.RGB_565);
////                buffer = ByteBuffer.wrap(videoBean.getVideoData());
////                videoBit.copyPixelsFromBuffer(buffer);
//				Matrix m = new Matrix();
//				float scale = 1;
//				int xOff = 0;
//				if (isLarge) {
////					scale = (float) ConferenceApplication.SCREEN_WIDTH / videoBit.getWidth();
//					xOff = (int) ((width - videoBit.getWidth() * scale) / 2);
//				} else {
////					scale = (float) findViewById(R.id.video_cell_item_videoView).getWidth() / videoBit.getWidth();
//				}
//
//                m.setRotate(270);
//                videoBit = Bitmap.createBitmap(videoBit, 0,0, videoBit.getWidth(), videoBit.getHeight(), m, false);
////				m.postScale(scale, scale);
////				m.postTranslate(xOff, 0);
////				canvas.drawBitmap(videoBit, m, null);
//                canvas.drawBitmap(videoBit, (float)0, (float)0.0, null);
//			}
            else {
//				log.info("视频数据为空");
				// 关闭视频
//				Matrix m = new Matrix();
//				float scale = 180.0f / videoBit.getWidth();
//				m.postScale(scale, scale);
//				Canvas canvasBit = new Canvas(videoBit);
//				canvasBit.drawColor(Color.WHITE);
//				canvasBit.save();
//				canvas.drawBitmap(videoBit, m, null);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 关闭视图
	 */
	public void closeVideo() {
		videoBean = null;
		invalidate();
	}

	public void setLarge(boolean isLarge) {
		this.isLarge = isLarge;
//		if(isLarge)
//			this.setBackgroundResource(R.color.black);
//		else
//			this.setBackgroundResource(R.color.white);
	}

	public boolean isLarge() {
		return isLarge;
	}

	

	@Override
	public void setStatus(boolean isMove) {
		// TODO Auto-generated method stub
		this.isMove = isMove;
	}

	public void changeStatus(int channelID,boolean isOpen) {
        LoggerUtil.info(getClass().getName(), "changeStatus channelID = "+channelID+" isopen = "+isOpen);
//		if(videoCommon.getDeviceMap().get(channelID) != null){
//			log.info("channelID is exist!!");
//			if(isOpen){) != null){
//                log.info("channelID is exist!!");
//				if(!isGetVideoData){
//					videoCommon.openVideo(channelID);
//					isGetVideoData = true;
//					if(bitWidth != 0){
//						videoBit = Bitmap.createBitmap(bitWidth, bitHeight, Config.RGB_565);
//					}
//				}
//			}else{
//				if(isGetVideoData){
//					videoCommon.closeVideo(channelID);
//					videoBit = null;
//					isGetVideoData = false;
//				}
//			}
//		}
	}

	public boolean isGetVideoData() {
		return isGetVideoData;
	}

	public void setGetVideoData(boolean isGetVideoData) {
		this.isGetVideoData = isGetVideoData;
	}

	public void setParams(int width, int height) {
//		setLayoutParam(width, height);
		mWidth = width;
		mHeight = height;
	}

	public void setLayoutParam(int width, int height) {
        mWidth = width;
        mHeight = height;
		FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) getLayoutParams();
		params.width = FrameLayout.LayoutParams.MATCH_PARENT;
		params.height = FrameLayout.LayoutParams.MATCH_PARENT;
		params.setMargins(0, 0, 0, 0);
		setLayoutParams(params);
	}
}