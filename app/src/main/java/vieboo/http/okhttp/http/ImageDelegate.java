package vieboo.http.okhttp.http;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageView;

import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.io.InputStream;

/**
 * 加载图片相关
 * Created by weibo.kang on 2015/11/11.
 */
public class ImageDelegate {

    private static ImageDelegate mInstance;

    private ImageDelegate() {

    }

    public static ImageDelegate getInstance() {
        if(null == mInstance) {
            synchronized(ImageDelegate.class) {
                if(null == mInstance) {
                    mInstance = new ImageDelegate();
                }
            }
        }
        return mInstance;
    }


    public void displayImage(final ImageView view, String url)
    {
        displayImage(view, url, -1, null);
    }

    public void displayImage(final ImageView view, String url, Object tag)
    {
        displayImage(view, url, -1, tag);
    }

    /**
     * 加载图片
     */
    public void displayImage(final ImageView view, final String url, final int errorResId, final Object tag)
    {
        final Request request = new Request.Builder()
                .url(url)
                .build();
        Call call = OkHttpClientHelper.getInstance().getOkHttpClient().newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                setErrorResId(view, errorResId);
            }

            @Override
            public void onResponse(Response response) {
                InputStream is = null;
                try {
                    is = response.body().byteStream();
                    ImageUtils.ImageSize actualImageSize = ImageUtils.getImageSize(is);
                    ImageUtils.ImageSize imageViewSize = ImageUtils.getImageViewSize(view);
                    int inSampleSize = ImageUtils.calculateInSampleSize(actualImageSize, imageViewSize);
                    try {
                        is.reset();
                    } catch (IOException e) {
                        response = GetDelegate.getInstance().get(url, tag);
                        is = response.body().byteStream();
                    }

                    BitmapFactory.Options ops = new BitmapFactory.Options();
                    ops.inJustDecodeBounds = false;
                    ops.inSampleSize = inSampleSize;
                    final Bitmap bm = BitmapFactory.decodeStream(is, null, ops);
                    OkHttpClientHelper.getInstance().getDelivery().post(new Runnable() {
                        @Override
                        public void run() {
                            view.setImageBitmap(bm);
                        }
                    });
                } catch (Exception e) {
                    setErrorResId(view, errorResId);

                } finally {
                    if (is != null) try {
                        is.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });


    }


    private void setErrorResId(final ImageView view, final int errorResId)
    {
        OkHttpClientHelper.getInstance().getDelivery().post(new Runnable() {
            @Override
            public void run() {
                view.setImageResource(errorResId);
            }
        });
    }

}
