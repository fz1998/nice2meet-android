package com.threebody.conference.ui.util.http.listener;

import com.threebody.conference.ui.util.http.entity.BaseResponse;

public interface MyParser {
	public void onFinish(BaseResponse result);
}
