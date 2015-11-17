package vieboo.http.okhttp.controller.io;

/**
 * 加载数据时加载动画的工具接口
 *
 * Created by vieboo on 15/11/13.
 */
public interface IDataLoadingEventListener {

    void loadDataStarted(String msg, boolean isLoadingDialogCancel, int taskId);

    void loadDataComplete(int taskId);

}
