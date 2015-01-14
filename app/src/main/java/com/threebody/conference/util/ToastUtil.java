package com.threebody.conference.util;

import android.content.Context;
import android.widget.Toast;



public class ToastUtil {
	public static final int LENGTH_NORMOL = 3000;
	
	protected static boolean result;
	
	public static void showToast(Context context, String text){
		Toast.makeText(context, text, LENGTH_NORMOL).show();
	}
//	public static void showLongToast(Context context, String text){
//		Toast.makeText(context, text, Toast.LENGTH_LONG).show();
//	}
//	public static void showShortToast(Context context, String text){
//		Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
//	}
	
	public static void showToast(Context context, int stringId){
		Toast.makeText(context, context.getString(stringId), LENGTH_NORMOL).show();
	}
	
	

}
