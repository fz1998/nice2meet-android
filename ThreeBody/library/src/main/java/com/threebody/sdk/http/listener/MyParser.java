package com.threebody.sdk.http.listener;

import com.threebody.sdk.http.entity.BaseResponse;

public interface MyParser {
	public void onFinish(BaseResponse result);
}
