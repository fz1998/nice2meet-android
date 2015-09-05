package com.threebody.conference.ui.view;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.threebody.conference.R;


public class MyProgressDialog extends ProgressDialog{
	String message = "";
	public MyProgressDialog(Context context, int theme, String message) {
		super(context, theme);
		if(message != null){
			this.message = message;
		}
	}

	public MyProgressDialog(Context context, String message) {
		super(context);
		if(message != null){
			this.message = message;
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_progress);
		if(!message.equals("")){
			TextView tv = (TextView)findViewById(R.id.tvMessage);
			tv.setVisibility(View.VISIBLE);
			tv.setText(message);
		}
	}
	
}
