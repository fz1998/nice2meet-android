package com.threebody.sdk.util;


import java.lang.reflect.Field;
import java.text.DecimalFormat;

public class StringUtil {
	public static final String CATE_END = "!300x240p75";
	
	//输出JavaBean类的toString
	public static String getToString(Object object) {
		StringBuilder sb = new StringBuilder();
		sb.append(object.getClass().getName() +" info[");
		setFields(sb, object);
		sb.append("];");
		return sb.toString();
	}
	
	public static void setFields(StringBuilder sb,Object object) {
		Field[] fields = object.getClass().getDeclaredFields();
		for (Field field : fields) {
			field.setAccessible(true);
			sb.append(" ")
			  .append(field.getName()+"=");
				try {
					sb.append(field.get(object)+" ");
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				}
			
		}
	}
	
	
	public static String changToWaveType(String key){
		StringBuilder sb = new StringBuilder();
		if(!key.contains("_"))return key;
		int index = key.indexOf("_");
		sb.append(key.substring(0, index))
		  .append(key.substring(index + 1, index + 2).toUpperCase())
		  .append(key.substring(index + 2));
		return sb.toString();
	}



	public static String getDecimal(double d){
		DecimalFormat decimalFormat = new DecimalFormat("#0.00");//构造方法的字符格式这里如果小数不足2位,会以0补足.
		String p = decimalFormat.format(d);
		return p;
	}
	
	public static Double stringToDouble(String d){
		if(d != null && !d.equals("")){
			return Double.parseDouble(d);
		}else{
			return 0.00;
		}
	}

	public static int setIntValue(int intValue, String str) {
		if(str == null || str.equals("")){
			return 0;
		}else{
			return Integer.parseInt(str);
		}
	}

	public static boolean stringToBoolean(String hasMustPmf) {
		if(hasMustPmf == null || hasMustPmf.equals("")){
			return  false;
		}else{
			int value = Integer.parseInt(hasMustPmf);
			if(value == 0){
				return false;
			}else{
				return true;
			}
		}
	}

	public static String getMethodGet(String name) {
		StringBuilder sb = new StringBuilder("get");
		sb.append(name.substring(0, 1).toUpperCase()).append(name.substring(1));
		return sb.toString();
	}

	public static String getMethodSet(String name) {
		StringBuilder sb = new StringBuilder("set");
		sb.append(name.substring(0, 1).toUpperCase()).append(name.substring(1));
		return sb.toString();
	}
	

	public static boolean isNull(String text){
		if(text == null || text.equals("")){
			return false;
		}else{
			return true;
		}
	}
	




	public static String getFileName(String path) {
		if(isNull(path)){
			StringBuffer sb = new StringBuffer(path);
			int index = sb.lastIndexOf("/");
			if(index != -1){
				return sb.substring(index + 1);
			}
		}
		return "";
	}

	public static String getFileType(String fileName) {
		if(isNull(fileName)){
			StringBuffer sb = new StringBuffer(fileName);
			int index = sb.lastIndexOf(".");
			if(index != -1){
				return sb.substring(index + 1);
			}
		}
		return "";
	}


}
