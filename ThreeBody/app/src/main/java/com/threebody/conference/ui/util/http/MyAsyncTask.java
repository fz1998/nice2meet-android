package com.threebody.conference.ui.util.http;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.app.FragmentManager;

import com.threebody.conference.R;
import com.threebody.conference.ui.util.ToastUtil;
import com.threebody.conference.ui.util.http.entity.BaseResponse;
import com.threebody.conference.ui.util.http.listener.MyParser;
import com.threebody.conference.ui.view.HttpProgressDialog;
import com.threebody.sdk.util.LoggerUtil;


public class MyAsyncTask extends AsyncTask<Void, Void, BaseResponse>{
    public static int RELOGIN_TIME = 0;
	String json;
	Context context;
	@SuppressWarnings("rawtypes")
	Class cls;
	MyParser parser;
	HttpProgressDialog dialog;
	String errorStr;
	BaseResponse response;
	boolean isNeedLogin = true;
	long time;
	
	@SuppressWarnings("rawtypes")
	public MyAsyncTask(String json, Context context, Class cls,
                       String errorStr, MyParser parser, HttpProgressDialog dialog){
		super();
		this.json = json;
		this.context = context;
		this.cls = cls;
		if(errorStr != null){
            this.errorStr = errorStr;
        }else{
            this.errorStr = "";
        }
		this.parser = parser;
		this.dialog = dialog;
	}
	@SuppressWarnings("rawtypes")
	public MyAsyncTask(String json, Context context, Class cls,
                       String errorStr, MyParser parser, HttpProgressDialog dialog, boolean isNeedLogin){
		super();
		this.json = json;
		this.context = context;
		this.cls = cls;
		if(this.errorStr != null){
            this.errorStr = errorStr;
        }else{
            this.errorStr = "";
        }
		this.parser = parser;
		this.dialog = dialog;
		this.isNeedLogin = isNeedLogin;
	}
	@Override
	protected BaseResponse doInBackground(Void... params) {
		response = (BaseResponse) JsonUtil.getResponse(context, json, cls);
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
        LoggerUtil.info(getClass().getName(), result.getLongErrorCode() + " " + result.getCode());

		if(result != null){
            parser.onFinish(result);
		}else{
			ToastUtil.showToast(context, R.string.responseNull);
		}
//		dialogDismiss();
		parser.onFinish(null);
	}
	private void dialogDismiss() {
		FragmentManager manager = dialog.getFragmentManager();
		if(manager != null && manager.beginTransaction() != null){
			if(dialog != null ){
				dialog.dismissAllowingStateLoss();
			}
		}
	}

}
