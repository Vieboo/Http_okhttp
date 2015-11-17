package vieboo.http.okhttp.activitys;

import android.support.v4.app.FragmentActivity;

import vieboo.http.okhttp.controller.io.IDataLoadingEventListener;
import vieboo.http.okhttp.weight.LoadingDialog;

/**
 * Created by vieboo on 15/11/12.
 */
public class BaseActivity extends FragmentActivity implements IDataLoadingEventListener {

    private LoadingDialog loadingDialog;


    @Override
    public void loadDataStarted(String msg, boolean isLoadingDialogCancel, int taskId) {
        if(null == loadingDialog) {
            loadingDialog = LoadingDialog.createDialog(this);
        }
        if(!loadingDialog.isShowing()) {
            loadingDialog.setCancelable(isLoadingDialogCancel);
            loadingDialog.setMessage(msg);
            loadingDialog.show();
        }
    }

    @Override
    public void loadDataComplete(int taskId) {
        if(null != loadingDialog && loadingDialog.isShowing()) {
            loadingDialog.dismiss();
        }
    }
}
