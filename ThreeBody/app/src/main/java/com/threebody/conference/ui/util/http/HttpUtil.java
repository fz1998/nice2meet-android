package com.threebody.conference.ui.util.http;

import android.content.Context;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.threebody.conference.R;
import com.threebody.conference.ui.util.ToastUtil;
import com.threebody.conference.ui.util.http.listener.DoubleParser;
import com.threebody.conference.ui.util.http.listener.MyParser;
import com.threebody.conference.ui.view.HttpProgressDialog;
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
    public static RequestParams getParams(Object object,String[] strings, Context context) throws IllegalAccessException, IllegalArgumentException, NoSuchMethodException, InvocationTargetException {
            return getParams(object, strings, context, true);
    }
    public static RequestParams getParams(Object object,String[] strings, Context context, boolean isCheck) throws IllegalAccessException, IllegalArgumentException, NoSuchMethodException, InvocationTargetException {
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
    @SuppressWarnings("rawtypes")
    public static void httpActionText( Context context, AsyncHttpClient client,  String url, Object object, MyParser parser, String[] strings, Class cls, int resId){
        if(resId > 0){
            httpActionText(true, null, context, client, url, object, parser, strings, cls, context.getString(resId), 0);
        }else{
            httpActionText(true, null, context, client, url, object, parser, strings, cls, "", 0);
        }
    }
    @SuppressWarnings("rawtypes")
    public static void httpActionText( Context context, AsyncHttpClient client,  String url, Object object, MyParser parser, String[] strings, Class cls, String errorStr){
        httpActionText(true, null, context, client, url, object, parser, strings, cls, errorStr, 0);
    }
    @SuppressWarnings("rawtypes")
    public static void httpActionText( boolean isShow,  String message,  Context context, AsyncHttpClient client,  String url, Object object, MyParser parser, String[] strings, Class cls, int resId){
        httpActionText(isShow, message, context, client, url, object, parser, strings, cls, context.getString(resId), 0);
    }
    public static void httpActionText( boolean isShow,  String message,  Context context, AsyncHttpClient client,  String url, Object object, MyParser parser, String[] strings, Class cls, String errroMsg){
        httpActionText(isShow, message, context, client, url, object, parser, strings, cls, errroMsg, 0);
    }
    /**
     *
     * @param isShow    是否显示dialog
     * @param message	dialog中显示的文字（为null则没有文字）
     * @param context	context对象
     * @param client    httpclient
     * @param url		请求的url
     * @param object	请求参数封装成的对象
     * @param parser	网络请求结果返回接口
     * @param strings	需要加密的字段
     * @param cls		返回的json解析出的javabean对象
     * @param errorStr	网络请求错误时的提示信息(不包括error msg)
     */
    @SuppressWarnings({ "deprecation", "rawtypes" })
    public static void httpActionText(final boolean isShow, final String message, final Context context, AsyncHttpClient client, final String url,Object object,final MyParser parser,String[] strings, final Class cls, final String errorStr, int type){


        try{

            final RequestParams params = getParams(object, strings, context);
//			if(object instanceof Map){
//				params = HttpUtil.getParams((Map<String, String>) object);
//			}else{
//				params = 
//			}
            LoggerUtil.info(HttpUtil.class.getName(), "full url ="+URLDecoder.decode(AsyncHttpClient.getUrlWithQueryString(true, url, params)));

            switch (type){
                case METHOD_GET:
                    TextHttpResponseHandler handler = new TextHttpResponseHandler() {
                        HttpProgressDialog dialog = null;
                        @Override
                        public void onSuccess(int arg0, Header[] arg1, String text) {

                            MyAsyncTask task = new MyAsyncTask(text, context, cls, errorStr, parser, dialog);
                            task.execute();
                        }

                        @Override
                        public void onFailure(int arg0, Header[] arg1, String arg2, Throwable arg3) {
                            LoggerUtil.error(getClass().getName(), " onFailure response = "+arg2);
                            parser.onFinish(null);
                            if(context != null){
                                ToastUtil.showToast(context, R.string.connectFailure);
                            }
                            if(parser instanceof DoubleParser){
                                ((DoubleParser)parser).onFailed(arg2);
                            }
                            if(isShow && dialog != null){
                                dialog.dismissAllowingStateLoss();
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
                    LoggerUtil.info(getClass().getName(), "onSuccess response = "+responseBody);
//                    WXAsyncTask task = new WXAsyncTask(responseBody, cls, parser);
//                    task.execute();
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable error, String content) {
                    super.onFailure(statusCode, headers, error, content);
                    LoggerUtil.error(getClass().getName(), " onFailure response = "+content);
                    parser.onFinish(null);
                    if(context != null){
                        ToastUtil.showToast(context, R.string.connectFailure);
                    }
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
