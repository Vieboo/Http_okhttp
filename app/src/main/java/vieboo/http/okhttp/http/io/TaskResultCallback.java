package vieboo.http.okhttp.http.io;

import com.squareup.okhttp.Request;

/**
 * Created by vieboo on 15/11/16.
 */
public interface TaskResultCallback<T> {

    void onBefore(Request request, int taskId);

    void onAfter(int taskId);

    void onError(Request request, Exception e, int taskId);

    void onResponse(T response, int taskId);

}
