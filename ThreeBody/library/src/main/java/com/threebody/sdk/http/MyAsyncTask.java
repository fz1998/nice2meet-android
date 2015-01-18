package com.threebody.sdk.http;

import android.os.AsyncTask;

import com.threebody.sdk.http.entity.BaseResponse;
import com.threebody.sdk.http.listener.MyParser;


public class MyAsyncTask extends AsyncTask<Void, Void, BaseResponse>{
    public static int RELOGIN_TIME = 0;
	String json;
	@SuppressWarnings("rawtypes")
	Class cls;
	MyParser parser;
	BaseResponse response;
	
	@SuppressWarnings("rawtypes")
	public MyAsyncTask(String json, Class cls, MyParser parser){
		super();
		this.json = json;
		this.parser = parser;
        this.cls = cls;
	}

	@Override
	protected BaseResponse doInBackground(Void... params) {
		response = (BaseResponse) JsonUtil.getResponse(json, cls);
		try {
			Thread.sleep(500);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(response != null){
			return response;
		}
		return null;
	}
	
	@Override
	protected void onPostExecute(BaseResponse result) {
		super.onPostExecute(result);
//        LoggerUtil.info(getClass().getName(), result.getLongErrorCode() + " " + result.getCode());

		if(result != null){
            parser.onFinish(result);
		}
//		dialogDismiss();
		parser.onFinish(null);
	}


}
