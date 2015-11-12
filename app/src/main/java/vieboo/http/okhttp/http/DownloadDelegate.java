package vieboo.http.okhttp.http;

import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * 下载相关的模块
 * Created by weibo.kang on 2015/11/11.
 */
public class DownloadDelegate {

    private static DownloadDelegate mInstance;

    private DownloadDelegate() {

    }

    public static DownloadDelegate getInstance() {
        if(null == mInstance) {
            synchronized (DownloadDelegate.class) {
                if(null == mInstance) {
                    mInstance = new DownloadDelegate();
                }
            }
        }
        return mInstance;
    }



    /**
     * 异步下载文件
     *
     * @param url
     * @param destFileDir 本地文件存储的文件夹
     * @param callback
     */
    public void downloadAsyn(final String url, final String destFileDir, final ResultCallback callback, Object tag)
    {
        final Request request = new Request.Builder()
                .url(url)
                .tag(tag)
                .build();
        final Call call = OkHttpClientHelper.getInstance().getOkHttpClient().newCall(request);
        call.enqueue(new Callback()
        {
            @Override
            public void onFailure(final Request request, final IOException e)
            {
                OkHttpClientHelper.getInstance().sendFailedStringCallback(request, e, callback);
            }

            @Override
            public void onResponse(Response response)
            {
                InputStream is = null;
                byte[] buf = new byte[2048];
                int len = 0;
                FileOutputStream fos = null;
                try
                {
                    is = response.body().byteStream();

                    File dir = new File(destFileDir);
                    if (!dir.exists())
                    {
                        dir.mkdirs();
                    }
                    File file = new File(dir, getFileName(url));
//                    if(!file.exists()) {
//                        file.createNewFile();
//                    }
                    fos = new FileOutputStream(file);
                    while ((len = is.read(buf)) != -1)
                    {
                        fos.write(buf, 0, len);
                    }
                    fos.flush();
                    //如果下载文件成功，第一个参数为文件的绝对路径
                    OkHttpClientHelper.getInstance().sendSuccessResultCallback(file.getAbsolutePath(), callback);
                } catch (IOException e)
                {
                    OkHttpClientHelper.getInstance().sendFailedStringCallback(response.request(), e, callback);
                } finally
                {
                    try
                    {
                        if (is != null) is.close();
                    } catch (IOException e)
                    {
                    }
                    try
                    {
                        if (fos != null) fos.close();
                    } catch (IOException e)
                    {
                    }
                }

            }
        });
    }


    public void downloadAsyn(final String url, final String destFileDir, final ResultCallback callback)
    {
        downloadAsyn(url, destFileDir, callback, null);
    }

    private String getFileName(String path)
    {
        int separatorIndex = path.lastIndexOf("/");
        return (separatorIndex < 0) ? path : path.substring(separatorIndex + 1, path.length());
    }

}
