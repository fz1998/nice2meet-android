package com.threebody.conference.ui.util;

import android.util.Log;

public class LoggerUtil {
 
	public static void info(String tag,String msg){
		Log.i(tag, msg);
	}
	public static void dubug(String tag,String msg){
		Log.d(tag, msg);
	}
	public static void error(String tag,String msg){
		Log.i(tag, msg);
	}
}
