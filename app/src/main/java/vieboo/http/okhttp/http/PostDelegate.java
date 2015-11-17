package vieboo.http.okhttp.http;

import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Set;

import vieboo.http.okhttp.http.io.ResultCallback;

/**
 * Post请求
 * Created by weibo.kang on 2015/11/10.
 */
public class PostDelegate {

    private final MediaType MEDIA_TYPE_STREAM = MediaType.parse("application/octet-stream;charset=utf-8");
    private final MediaType MEDIA_TYPE_STRING = MediaType.parse("text/plain;charset=utf-8");
    private final MediaType MEDIA_TYPE_JSON = MediaType.parse("application/json;charset=utf-8");

    private static PostDelegate mInstance;

    private PostDelegate() {

    }

    public static PostDelegate getInstance() {
        if(null == mInstance) {
            synchronized (PostDelegate.class) {
                if(null == mInstance) {
                    mInstance = new PostDelegate();
                }
            }
        }
        return mInstance;
    }


    /** -------------------------------------异步Post请求------------------------------------- **/


    /**
     * post异步请求Json
     */
    public void postAsyn(String url, JSONObject json, final ResultCallback callback) {
        postAsynWithMediaType(url, json.toString(), MEDIA_TYPE_JSON, callback, null);
    }


    /**
     * 普通异步post请求
     */
    public void postAsyn(String url, Map<String, String> params, final ResultCallback callback)
    {
        postAsyn(url, params, callback, null);
    }

    public void postAsyn(String url, Map<String, String> params, final ResultCallback callback,
                         Object tag)
    {
        Param[] paramsArr = map2Params(params);
        postAsyn(url, paramsArr, callback, tag);
    }

    public void postAsyn(String url, Param[] params, final ResultCallback callback)
    {
        postAsyn(url, params, callback, null);
    }

    public void postAsyn(String url, Param[] params, final ResultCallback callback, Object tag)
    {
        Request request = buildPostFormRequest(url, params, tag);
        OkHttpClientHelper.getInstance().deliveryResult(callback, request);
    }


    /**
     * 直接将bodyStr以写入请求体
     */
    public void postAsyn(String url, String bodyStr, final ResultCallback callback)
    {
        postAsyn(url, bodyStr, callback, null);
    }

    public void postAsyn(String url, String bodyStr, final ResultCallback callback, Object tag)
    {
        postAsynWithMediaType(url, bodyStr, MEDIA_TYPE_STRING, callback, tag);
    }


    /**
     * 直接将bodyBytes以写入请求体
     */
    public void postAsyn(String url, byte[] bodyBytes, final ResultCallback callback)
    {
        postAsyn(url, bodyBytes, callback, null);
    }

    public void postAsyn(String url, byte[] bodyBytes, final ResultCallback callback, Object tag)
    {
        postAsynWithMediaType(url, bodyBytes, MEDIA_TYPE_STREAM, callback, tag);
    }


    /**
     * 直接将bodyFile以写入请求体
     */
    public void postAsyn(String url, File bodyFile, final ResultCallback callback)
    {
        postAsyn(url, bodyFile, callback, null);
    }

    public void postAsyn(String url, File bodyFile, final ResultCallback callback, Object tag)
    {
        postAsynWithMediaType(url, bodyFile, MEDIA_TYPE_STREAM, callback, tag);
    }


    /**
     * 直接将bodyFile以写入请求体
     */
    public void postAsynWithMediaType(String url, File bodyFile, MediaType type,
                                      final ResultCallback callback, Object tag)
    {
        RequestBody body = RequestBody.create(type, bodyFile);
        Request request = buildPostRequest(url, body, tag);
        OkHttpClientHelper.getInstance().deliveryResult(callback, request);
    }

    /**
     * 直接将bodyStr以写入请求体
     */
    public void postAsynWithMediaType(String url, String bodyStr, MediaType type,
                                      final ResultCallback callback, Object tag)
    {
        RequestBody body = RequestBody.create(type, bodyStr);
        Request request = buildPostRequest(url, body, tag);
        OkHttpClientHelper.getInstance().deliveryResult(callback, request);
    }

    /**
     * 直接将bodyBytes以写入请求体
     */
    public void postAsynWithMediaType(String url, byte[] bodyBytes, MediaType type,
                                      final ResultCallback callback, Object tag)
    {
        RequestBody body = RequestBody.create(type, bodyBytes);
        Request request = buildPostRequest(url, body, tag);
        OkHttpClientHelper.getInstance().deliveryResult(callback, request);
    }





    /** -------------------------------------同步Post请求------------------------------------- **/

    public Response post(String url, Param[] params) throws IOException
    {
        return post(url, params, null);
    }

    /**
     * 同步的Post请求
     */
    public Response post(String url, Param[] params, Object tag) throws IOException
    {
        Request request = buildPostFormRequest(url, params, tag);
        Response response = OkHttpClientHelper.getInstance().deliveryResult(request);
        return response;
    }

    public String postAsString(String url, Param[] params) throws IOException
    {
        return postAsString(url, params, null);
    }

    /**
     * 同步的Post请求
     */
    public String postAsString(String url, Param[] params, Object tag) throws IOException
    {
        Response response = post(url, params, tag);
        return response.body().string();
    }


    /**
     * 同步的Post请求:直接将bodyStr以写入请求体
     */
    public Response post(String url, String bodyStr) throws IOException
    {
        return post(url, bodyStr,null);
    }

    public Response post(String url, String bodyStr, Object tag) throws IOException
    {
        RequestBody body = RequestBody.create(MEDIA_TYPE_STRING, bodyStr);
        Request request = buildPostRequest(url, body, tag);
        Response response = OkHttpClientHelper.getInstance().deliveryResult(request);
        return response;
    }

    /**
     * 同步的Post请求:直接将bodyFile以写入请求体
     */
    public Response post(String url, File bodyFile) throws IOException
    {
        return post(url, bodyFile,null);
    }

    public Response post(String url, File bodyFile, Object tag) throws IOException
    {
        RequestBody body = RequestBody.create(MEDIA_TYPE_STREAM, bodyFile);
        Request request = buildPostRequest(url, body, tag);
        Response response = OkHttpClientHelper.getInstance().deliveryResult(request);
        return response;
    }

    /**
     * 同步的Post请求
     */
    public Response post(String url, byte[] bodyBytes) throws IOException
    {
        return post(url, bodyBytes,null);
    }

    public Response post(String url, byte[] bodyBytes, Object tag) throws IOException
    {
        RequestBody body = RequestBody.create(MEDIA_TYPE_STREAM, bodyBytes);
        Request request = buildPostRequest(url, body, tag);
        Response response = OkHttpClientHelper.getInstance().deliveryResult(request);
        return response;
    }





    /**
     * post构造Request的方法
     *
     * @param url
     * @param body
     * @return
     */
    private Request buildPostRequest(String url, RequestBody body, Object tag)
    {
        Request.Builder builder = new Request.Builder()
                .url(url)
                .post(body);
        if (tag != null)
        {
            builder.tag(tag);
        }
        Request request = builder.build();
        return request;
    }


    private Request buildPostFormRequest(String url, Param[] params, Object tag) {
        if (params == null)
        {
            params = new Param[0];
        }
        FormEncodingBuilder builder = new FormEncodingBuilder();
        for (Param param : params)
        {
            builder.add(param.key, param.value);
        }
        RequestBody requestBody = builder.build();

        Request.Builder reqBuilder = new Request.Builder();
        reqBuilder.url(url)
                .post(requestBody);

        if (tag != null)
        {
            reqBuilder.tag(tag);
        }
        return reqBuilder.build();

    }

    private Param[] map2Params(Map<String, String> params)
    {
        if (params == null) return new Param[0];
        int size = params.size();
        Param[] res = new Param[size];
        Set<Map.Entry<String, String>> entries = params.entrySet();
        int i = 0;
        for (Map.Entry<String, String> entry : entries)
        {
            res[i++] = new Param(entry.getKey(), entry.getValue());
        }
        return res;
    }
}
