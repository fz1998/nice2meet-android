package com.threebody.sdk.http;


import com.alibaba.fastjson.JSON;
import com.threebody.sdk.util.LoggerUtil;


public class JsonUtil {
	
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static Object getResponse(Object object, Class cls){
		
		try {
			String json = (String)object;
			LoggerUtil.info(JsonUtil.class.getName(), "response = " + json);
			Object response = cls.newInstance();
			response = JSON.parseObject(json, cls);
			return response;
		} catch (Exception e) {
			e.printStackTrace();
//			ToastUtil.showShortToast(context, "数据错误");
			return null;
		}
	}
//	@SuppressWarnings({ "rawtypes", "unchecked" })
//	public static Object getResponse(Context context, Object object, Class resCls, String mapField, Class itemCls, String key, String jsonItems){
//		Map<Object, Object> map = new HashMap<Object, Object>();
//		String json = (String)object;
//		JSONObject jo = null;
//		try {
//			jo = new JSONObject(json);
//			Object response = getResponse(context, object, resCls);
//			JSONArray list = jo.getJSONArray(jsonItems);
//			for (int i = 0; i < list.length(); i++) {
//				Object item = itemCls.newInstance();
//				Method method = itemCls.getMethod(StringUtil.getMethodGet(key));
//				item = JSON.parseObject(list.getString(i), itemCls);
//				map.put(method.invoke(item), item);
//			}
//			if(map != null && !map.isEmpty()){
//				Method method = resCls.getMethod(StringUtil.getMethodSet(mapField),Map.class);
//				method.invoke(response, map);
//			}
//
//			return response;
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//
//		return null;
//	}
}
