package vieboo.http.okhttp.http;

import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;

import vieboo.http.okhttp.http.io.ResultCallback;

/**
 * Get请求
 * Created by weibo.kang on 2015/11/6.
 */
public class GetDelegate {

    private static GetDelegate mInstance;

    private GetDelegate() {

    }

    public static GetDelegate getInstance() {
        if(null == mInstance) {
            synchronized (GetDelegate.class) {
                if(null == mInstance) {
                    mInstance = new GetDelegate();
                }
            }
        }
        return mInstance;
    }

    private Request buildGetRequest(String url, Object tag) {

        Request.Builder builder = new Request.Builder()
                .url(url);

        if (tag != null) {
            builder.tag(tag);
        }

        return builder.build();
    }


    /** -------------------------------------异步Get请求------------------------------------- **/


    /**
     * 异步的get请求
     */
    public void getAsyn(String url, final ResultCallback callback)
    {
        getAsyn(url, callback, null);
    }

    public void getAsyn(String url, final ResultCallback callback, Object tag)
    {
        final Request request = buildGetRequest(url, tag);
        getAsyn(request, callback);
    }

    /**
     * 通用的方法
     */
    public void getAsyn(Request request, ResultCallback callback)
    {
        OkHttpClientHelper.getInstance().deliveryResult(callback, request);
    }



    /** -------------------------------------同步Get请求------------------------------------- **/


    /**
     * 通用的方法
     */
    public Response get(Request request) throws IOException
    {
        Response execute = OkHttpClientHelper.getInstance().deliveryResult(request);
        return execute;
    }

    /**
     * 同步的Get请求
     */
    public Response get(String url) throws IOException
    {
        return get(url,null);
    }

    public Response get(String url, Object tag) throws IOException
    {
        final Request request = buildGetRequest(url, tag);
        return get(request);
    }


    /**
     * 同步的Get请求
     */
    public String getAsString(String url) throws IOException
    {
        return getAsString(url, null);
    }

    public String getAsString(String url, Object tag) throws IOException
    {
        Response execute = get(url, tag);
        return execute.body().string();
    }

}
