package vieboo.http.okhttp.http;

import android.os.Build;
import android.os.Handler;
import android.os.Looper;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.util.concurrent.TimeUnit;

/**
 * Created by weibo.kang on 2015/11/6.
 */
public class OkHttpClientHelper {

    private static OkHttpClientHelper mInstance;

    private OkHttpClient mOkHttpClient;     //OkHttpClient实例
    private final int CONNECTION_TIME_OUT = 1;    //超时时间
    private Handler mDelivery;

    private OkHttpClientHelper() {
        mOkHttpClient = new OkHttpClient();
        //cookie enabled
        if(Build.VERSION.SDK_INT >= 9)
            mOkHttpClient.setCookieHandler(new CookieManager(null, CookiePolicy.ACCEPT_ORIGINAL_SERVER));
        //connection time out
        mOkHttpClient.setConnectTimeout(CONNECTION_TIME_OUT, TimeUnit.SECONDS);

        mDelivery = new Handler(Looper.getMainLooper());
    }

    public static OkHttpClientHelper getInstance() {
        if(null == mInstance) {
            synchronized (OkHttpClientHelper.class) {
                if (null == mInstance) {
                    mInstance = new OkHttpClientHelper();
                }
            }
        }
        return mInstance;
    }

    public OkHttpClient getOkHttpClient() {
        return mOkHttpClient;
    }

    public Handler getDelivery() {
        return mDelivery;
    }


    /**
     * 取消请求
     * @param tag
     */
    public static void cancelTag(Object tag)
    {
        getInstance()._cancelTag(tag);
    }

    private void _cancelTag(Object tag)
    {
        mOkHttpClient.cancel(tag);
    }

    /**
     * 默认回调
     */
    private final ResultCallback<String> DEFAULT_RESULT_CALLBACK = new ResultCallback<String>() {
        @Override
        public void onError(Request request, Exception e) {

        }

        @Override
        public void onResponse(String response) {

        }
    };


    /**
     * 异步请求
     * @param callback
     * @param request
     */
    public void deliveryResult(ResultCallback callback, Request request) {

        if (callback == null) callback = DEFAULT_RESULT_CALLBACK;
        final ResultCallback resCallBack = callback;
        //UI thread
        callback.onBefore(request);
        mOkHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(final Request request, final IOException e) {
                sendFailedStringCallback(request, e, resCallBack);
            }

            @Override
            public void onResponse(final Response response) {
                System.out.println("COde----------->" + response.code());
                try {
                    final String string = response.body().string();
                    if (resCallBack.mType == String.class) {
                        sendSuccessResultCallback(string, resCallBack);
                    } else {
//                        Object o = mGson.fromJson(string, resCallBack.mType);
//                        sendSuccessResultCallback(o, resCallBack);
                    }

                } catch (IOException e) {
                    sendFailedStringCallback(response.request(), e, resCallBack);
                } catch (com.google.gson.JsonParseException e) {//Json解析的错误
                    sendFailedStringCallback(response.request(), e, resCallBack);
                }

            }
        });
    }


    /**
     * 同步请求
     */
    public Response deliveryResult(Request request) throws IOException {
        return mOkHttpClient.newCall(request).execute();
    }


    public void sendFailedStringCallback(final Request request, final Exception e, final ResultCallback callback) {

        mDelivery.post(new Runnable() {
            @Override
            public void run() {
                callback.onError(request, e);
                callback.onAfter();
            }
        });
    }

    public void sendSuccessResultCallback(final Object object, final ResultCallback callback) {

        mDelivery.post(new Runnable() {
            @Override
            public void run()
            {
                callback.onResponse(object);
                callback.onAfter();
            }
        });
    }

}
