package com.threebody.conference.ui.view.video;



public interface VideoView {
	
	public void resetSize(int width, int height);
	public void setVideoBean(VideoBean videoBean);
	public void setLarge(boolean isLarge);
	public boolean isLarge();
	public void setStatus(boolean isMove);
}
