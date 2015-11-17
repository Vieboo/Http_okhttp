package vieboo.http.okhttp.weight;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.widget.TextView;

import vieboo.http.okhttp.R;

/**
 * Created by vieboo on 15/11/17.
 */
public class LoadingDialog extends Dialog {

    public LoadingDialog(Context context) {
        super(context);
    }

    public LoadingDialog(Context context, int theme) {
        super(context, theme);
    }

    protected LoadingDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    public static LoadingDialog createDialog(Context context) {
        LoadingDialog instance = null;
        instance = new LoadingDialog(context, R.style.style_loading_dialog);
        instance.setContentView(R.layout.layout_loading_dialog);
        instance.getWindow().getAttributes().gravity = Gravity.CENTER;
        instance.setCancelable(true);
        return instance;
    }

    public void setMessage(String msg) {
        TextView message = (TextView) findViewById(R.id.message);
        message.setText(msg);
    }
}
