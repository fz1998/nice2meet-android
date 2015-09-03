package com.threebody.sdk.http;

import android.content.Context;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.threebody.sdk.http.listener.DoubleParser;
import com.threebody.sdk.http.listener.MyParser;
import com.threebody.sdk.util.LoggerUtil;
import com.threebody.sdk.util.StringUtil;

import org.apache.http.Header;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URLDecoder;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class HttpUtil {

    public static final String SERVICE_HOST_PRIVATE = "http://api.7nb.com.cn/api/m/1.0/";
    //	public static String SERVICE_HOST_ONLINE = "http://api.7nb.com.cn/";
    public static final String SERVICE_HOST_ONLINE = "http://api.welinjia.com/api/m/1.0/";
    public static String SERVICE_HOST = SERVICE_HOST_PRIVATE;

    public static final String LENGTH = "l";
    public static final int METHOD_POST = 0;
    public static final int METHOD_GET = 1;
    /**
     * 把javaBean类转化成param
     * @param object
     * @param strings 需要加密的属性
     * @return params
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws NoSuchMethodException
     * @throws java.lang.reflect.InvocationTargetException
     */
    public static RequestParams getParams(Object object,String[] strings) throws IllegalAccessException, IllegalArgumentException, NoSuchMethodException, InvocationTargetException {
            return getParams(object, strings, true);
    }
    public static RequestParams getParams(Object object,String[] strings, boolean isCheck) throws IllegalAccessException, IllegalArgumentException, NoSuchMethodException, InvocationTargetException {
//		RequestParams params = new RequestParams
        Map<String, String> paramMap = new TreeMap<String, String>(new Comparator<String>() {

            @Override
            public int compare(String lhs, String rhs) {
                return lhs.compareTo(rhs);
            }

        });
        if(object == null){
//            String
        }else{
            Field[] fields = object.getClass().getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true);
//                if(findString(strings, field.getName())){
//                    String encodePwd = encodePwd((String)field.get(object), context);
//                    paramMap.put(field.getName(), encodePwd);
//                    paramMap.put(LENGTH, ""+encodePwd.length());
//                }else{
//                    if(field.getType() == Object.class){
//                        Gson gs = new Gson();
//                        List<Object> value = getList(field,object);
//
//                        String items = gs.toJson(value);
//                        paramMap.put(field.getName(), items);
//                        LoggerUtil.info(HttpUtil.class.getName(), "items = "+items);
//                    }else{
                        Object o = field.get(object);
                        if(o != null){
                            paramMap.put(StringUtil.changToWaveType(field.getName()), ""+ field.get(object));
//                        }

                    }
//                }
            }
        }
//       if(isCheck){
//           long sarah = URLUtil.getSarah(paramMap);
//           paramMap.put("sarah", ""+sarah);
//       }
        return new RequestParams(paramMap);
    }

    @SuppressWarnings({ "unchecked" })
    private static List<Object> getList(Field field,Object object) throws NoSuchMethodException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {

        Method method = object.getClass().getMethod(StringUtil.getMethodGet(field.getName()));

        return (List<Object>) method.invoke(object);
    }

    //字符串加密
//    private static String encodePwd(String pswd, Context context) {
//        if(LemonApplication.publicKey == null){
//            ConnectUtil.initPublicKey(context);
//        }
//        return RSAutil.encode16(pswd, LemonApplication.publicKey);
//    }
    private static boolean findString(String[] strings, String str){
        if(strings != null){
            for (String string : strings) {
                if(str.equals(string)){
                    return true;
                }
            }
        }
        return false;
    }

//    //返回文件
//    public static void httpAtionFile(Context context, AsyncHttpClient client, String url, Object object,final MyParser parser){
//        httpAtionFile(true, context, client, url, object, parser);
//    }
//    public static void httpAtionFile(boolean isShow,Context context, AsyncHttpClient client, String url, Object object,final MyParser parser){
////        LoggerUtil.dubug(SplashActivity.class.getName(), "url = "+url);
//        client.post(url, new FileAsyncHttpResponseHandler(context) {
//
//            @Override
//            public void onSuccess(int arg0, Header[] arg1, File file) {
//                LoggerUtil.dubug(HttpUtil.class.getName(), "getPublicKey onSuccess");
//                parser.onFinish(file);
//
//            }
//
//            @Override
//            public void onFailure(int arg0, Header[] arg1, Throwable arg2, File arg3) {
//                LoggerUtil.dubug(HttpUtil.class.getName(), "getPublicKey onFailure ");
//
//            }
//        });
//    }


    public static String getUrl(String text){
        StringBuffer sb = new StringBuffer(SERVICE_HOST);
        sb.append(text);
        return sb.toString();
    }

    /**
     *
     * @param client    httpclient
     * @param url		请求的url
     * @param object	请求参数封装成的对象
     * @param parser	网络请求结果返回接口
     * @param strings	需要加密的字段
     */
    @SuppressWarnings({ "deprecation", "rawtypes" })
    public static void httpActionText(AsyncHttpClient client, final String url,Object object,final MyParser parser,String[] strings, final Class cls, int type){


        try{

            final RequestParams params = getParams(object, strings);
//			if(object instanceof Map){
//				params = HttpUtil.getParams((Map<String, String>) object);
//			}else{
//				params = 
//			}
            LoggerUtil.info(HttpUtil.class.getName(), "full url ="+URLDecoder.decode(AsyncHttpClient.getUrlWithQueryString(true, url, params)));

            switch (type){
                case METHOD_GET:
                    TextHttpResponseHandler handler = new TextHttpResponseHandler() {
                        @Override
                        public void onSuccess(int arg0, Header[] arg1, String text) {

                            MyAsyncTask task = new MyAsyncTask(text, cls, parser);
                            task.execute();
                        }

                        @Override
                        public void onFailure(int arg0, Header[] arg1, String arg2, Throwable arg3) {
                            parser.onFinish(null);
                            if(parser instanceof DoubleParser){
                                ((DoubleParser)parser).onFailed(arg2);
                            }
                        }
                    };
                    client.get(url, params, handler);
                    break;
//                case METHOD_GET:
//                    break;
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    public static void httpActionTextWX(String url, Object object, final Context context, final MyParser parser, AsyncHttpClient client, final Class cls){
        try {
//            RequestParams params = getParams(object, null, context, false);
            RequestParams params = new RequestParams();
            Field[] fields = object.getClass().getDeclaredFields();
            if(fields != null){
                for(Field field : fields){
                    field.setAccessible(true);
                    Object o = field.get(object);
                    if(o != null){
                        params.put(field.getName(), o);
                    }
                }
            }
            LoggerUtil.info(HttpUtil.class.getName(), "full url ="+URLDecoder.decode(AsyncHttpClient.getUrlWithQueryString(true, url, params)));
            client.get(url, params, new TextHttpResponseHandler(){
                @Override
                public void onSuccess(int statusCode, Header[] headers, String responseBody) {
                    super.onSuccess(statusCode, headers, responseBody);
//                    WXAsyncTask task = new WXAsyncTask(responseBody, cls, parser);
//                    task.execute();
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable error, String content) {
                    super.onFailure(statusCode, headers, error, content);
                    parser.onFinish(null);
                    if(parser instanceof DoubleParser){
                        ((DoubleParser)parser).onFailed(content);
                    }

                }
            });

        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
