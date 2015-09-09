package cn.tee3.n2m.biz.http;

import android.os.AsyncTask;

import cn.tee3.n2m.biz.http.entity.BaseResponse;
import cn.tee3.n2m.biz.http.listener.MyParser;


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
		if(result != null){
            parser.onFinish(result);
		}
		parser.onFinish(null);
	}


}
