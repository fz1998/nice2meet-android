package cn.tee3.n2m.ui.view;


import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;

import cn.tee3.n2m.biz.http.HttpHelper;

//// TODO: 2015/9/5 What's the purpose of this class ?
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
		return new MyProgressDialog(getActivity(), message);
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
