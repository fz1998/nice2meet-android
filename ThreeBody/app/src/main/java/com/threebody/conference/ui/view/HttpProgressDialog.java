package com.threebody.conference.ui.view;


import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;

import com.threebody.sdk.http.HttpHelper;


public class HttpProgressDialog extends DialogFragment {
	String message = "";
	
	public HttpProgressDialog(){super();}
    @SuppressLint("ValidFragment")
	public HttpProgressDialog(String message) {
		super();
		this.message = message;
	}
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ProgressDialog dialog = null;
//		int current = android.os.Build.VERSION.SDK_INT;
//        if (current >= Build.VERSION_CODES.HONEYCOMB){
//            dialog = new ProgressDialog(getActivity(),ProgressDialog.THEME_HOLO_LIGHT);
        	dialog=new MyProgressDialog(getActivity(), message);
//        }else{
//            dialog = new ProgressDialog(getActivity());
//            dialog.setMessage(message);
//        }
		
		return dialog;
	}
	@Override
	public void show(FragmentManager manager, String tag) {
		super.show(manager, tag);
	}
	@Override
	public void onCancel(DialogInterface dialog) {
		super.onCancel(dialog);
		HttpHelper.getInstance().cancel(getActivity());
	}
}
