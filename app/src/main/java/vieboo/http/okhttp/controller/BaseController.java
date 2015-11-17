package vieboo.http.okhttp.controller;


import android.os.Handler;
import android.os.Looper;

import com.squareup.okhttp.Request;

import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.Objects;

import vieboo.http.okhttp.controller.io.IDataLoadingEventListener;
import vieboo.http.okhttp.http.GetDelegate;
import vieboo.http.okhttp.http.HttpConstant;
import vieboo.http.okhttp.http.Param;
import vieboo.http.okhttp.http.PostDelegate;
import vieboo.http.okhttp.http.io.ResultCallback;
import vieboo.http.okhttp.http.io.TaskResultCallback;

/**
 * Created by weibo.kang on 2015/11/6.
 */
public class BaseController {

    private Handler uiHandler;

    public BaseController() {
        uiHandler = new Handler(Looper.getMainLooper());
    }


    /**
     * 请求回调
     */
    class MResultCallback extends ResultCallback<String> {

        private int taskId;
        TaskResultCallback<Object> callback;
        private IDataLoadingEventListener mLoadingIndicator = null;
        private String loadingMessage;  //加载动画提示语
        private boolean isLoadingDialogCancel = true;  //加载动画框是否可以取消


        MResultCallback(int taskId, TaskResultCallback<Object> callback, IDataLoadingEventListener mLoadingIndicator,
                        String loadingMessage, boolean isLoadingDialogCancel) {
            this.taskId = taskId;
            this.callback = callback;
            this.mLoadingIndicator = mLoadingIndicator;
            this.loadingMessage = loadingMessage;
            this.isLoadingDialogCancel = isLoadingDialogCancel;
        }

        @Override
        public void onError(final Request request, final Exception e) {
            uiHandler.post(new Runnable() {
                @Override
                public void run() {
                    callback.onError(request, e, taskId);
                }
            });
        }

        @Override
        public void onResponse(final String response) {
            uiHandler.post(new Runnable() {
                @Override
                public void run() {
                    callback.onResponse(response, taskId);
                }
            });
        }

        @Override
        public void onBefore(final Request request) {
            super.onBefore(request);
            uiHandler.post(new Runnable() {
                @Override
                public void run() {
                    callback.onBefore(request, taskId);
                    if(null != mLoadingIndicator) {
                        mLoadingIndicator.loadDataStarted(loadingMessage, isLoadingDialogCancel, taskId);
                    }
                }
            });
        }

        @Override
        public void onAfter() {
            super.onAfter();
            uiHandler.post(new Runnable() {
                @Override
                public void run() {
                    callback.onAfter(taskId);
                    if (null != mLoadingIndicator) {
                        mLoadingIndicator.loadDataComplete(taskId);
                    }
                }
            });
        }
    }

    /**
     *
     * @param method    请求方式 post get
     * @param url       请求地址
     * @param taskId    taskId
     * @param params    参数
     * @param resultCallback    数据回调
     * @param mLoadingIndicator loading动画回调
     * @param loadingMessage    loading提示语
     * @param isLoadingDialogCancel loading动画是否可取消
     */
    protected void sendRequest(
            int method, String url, int taskId, Param params[], TaskResultCallback<Object> resultCallback,
            IDataLoadingEventListener mLoadingIndicator, String loadingMessage,
            boolean isLoadingDialogCancel) {
        switch (method) {
            //get
            case HttpConstant.GET:
                GetDelegate.getInstance().getAsyn(url, new MResultCallback(
                        taskId, resultCallback, mLoadingIndicator, loadingMessage, isLoadingDialogCancel));
                break;
            //post
            case HttpConstant.POST:
                PostDelegate.getInstance().postAsyn(url, params, new MResultCallback(
                    taskId, resultCallback, mLoadingIndicator, loadingMessage, isLoadingDialogCancel));
                break;
        }
    }

    /**
     *
     * @param url       请求地址
     * @param taskId    taskId
     * @param json    参数
     * @param resultCallback    数据回调
     * @param mLoadingIndicator loading动画回调
     * @param loadingMessage    loading提示语
     * @param isLoadingDialogCancel loading动画是否可取消
     */
    protected void sendRequestJson(
            String url, int taskId, JSONObject json, TaskResultCallback resultCallback,
            IDataLoadingEventListener mLoadingIndicator, String loadingMessage,
            boolean isLoadingDialogCancel) {
        PostDelegate.getInstance().postAsyn(url, json, new MResultCallback(
                taskId, resultCallback, mLoadingIndicator, loadingMessage, isLoadingDialogCancel));
    }

}
