package vieboo.http.okhttp.http;

import com.squareup.okhttp.Headers;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.MultipartBuilder;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.File;
import java.io.IOException;
import java.net.FileNameMap;
import java.net.URLConnection;

/**
 * 上传相关的模块
 * Created by weibo.kang on 2015/11/11.
 */
public class UploadDelegate {

    private static UploadDelegate mInstance;


    private UploadDelegate() {

    }

    public static UploadDelegate getInstance() {
        if(null == mInstance) {
            synchronized(UploadDelegate.class) {
                if(null == mInstance) {
                    mInstance = new UploadDelegate();
                }
            }
        }
        return mInstance;
    }


    /**
     * 同步基于post的文件上传:上传单个文件
     */
    public Response post(String url, String fileKey, File file, Object tag) throws IOException
    {
        return post(url, new String[]{fileKey}, new File[]{file}, null, tag);
    }

    /**
     * 同步基于post的文件上传:上传多个文件以及携带key-value对：主方法
     */
    public Response post(String url, String[] fileKeys, File[] files, Param[] params, Object tag) throws IOException
    {
        Request request = buildMultipartFormRequest(url, files, fileKeys, params, tag);
        return OkHttpClientHelper.getInstance().deliveryResult(request);
    }

    /**
     * 同步单文件上传
     */
    public Response post(String url, String fileKey, File file, Param[] params, Object tag) throws IOException
    {
        return post(url, new String[]{fileKey}, new File[]{file}, params, tag);
    }

    /**
     * 异步基于post的文件上传:主方法
     */
    public void postAsyn(String url, String[] fileKeys, File[] files, Param[] params, ResultCallback callback, Object tag)
    {
        Request request = buildMultipartFormRequest(url, files, fileKeys, params, tag);
        OkHttpClientHelper.getInstance().deliveryResult(callback, request);
    }

    /**
     * 异步基于post的文件上传:单文件不带参数上传
     */
    public void postAsyn(String url, String fileKey, File file, ResultCallback callback, Object tag) throws IOException
    {
        postAsyn(url, new String[]{fileKey}, new File[]{file}, null, callback, tag);
    }

    /**
     * 异步基于post的文件上传，单文件且携带其他form参数上传
     */
    public void postAsyn(String url, String fileKey, File file, Param[] params, ResultCallback callback, Object tag)
    {
        postAsyn(url, new String[]{fileKey}, new File[]{file}, params, callback, tag);
    }

    private Request buildMultipartFormRequest(String url, File[] files,
                                              String[] fileKeys, Param[] params, Object tag)
    {
        params = validateParam(params);

        MultipartBuilder builder = new MultipartBuilder()
                .type(MultipartBuilder.FORM);

        for (Param param : params)
        {
            builder.addPart(Headers.of("Content-Disposition", "form-data; name=\"" + param.key + "\""),
                    RequestBody.create(null, param.value));
        }
        if (files != null)
        {
            RequestBody fileBody = null;
            for (int i = 0; i < files.length; i++)
            {
                File file = files[i];
                String fileName = file.getName();
                fileBody = RequestBody.create(MediaType.parse(guessMimeType(fileName)), file);
                //TODO 根据文件名设置contentType
                builder.addPart(Headers.of("Content-Disposition",
                                "form-data; name=\"" + fileKeys[i] + "\"; filename=\"" + fileName + "\""),
                        fileBody);
            }
        }

        RequestBody requestBody = builder.build();
        return new Request.Builder()
                .url(url)
                .post(requestBody)
                .tag(tag)
                .build();
    }

    private Param[] validateParam(Param[] params)
    {
        if (params == null)
            return new Param[0];
        else return params;
    }

    private String guessMimeType(String path)
    {
        FileNameMap fileNameMap = URLConnection.getFileNameMap();
        String contentTypeFor = fileNameMap.getContentTypeFor(path);
        if (contentTypeFor == null)
        {
            contentTypeFor = "application/octet-stream";
        }
        return contentTypeFor;
    }

}
