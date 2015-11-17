package vieboo.http.okhttp.controller;

import org.json.JSONException;
import org.json.JSONObject;

import vieboo.http.okhttp.controller.io.IDataLoadingEventListener;
import vieboo.http.okhttp.http.Param;
import vieboo.http.okhttp.http.io.TaskResultCallback;

/**
 * Created by vieboo on 15/11/17.
 */
public class MainController extends BaseController {

    public void getData1(Param params[], TaskResultCallback resultCallback,
                         IDataLoadingEventListener mLoadingIndicator) {
        JSONObject jObj = new JSONObject();
        for (int i = 0; i < params.length; i++) {
            try {
                jObj.put(params[i].key, params[i].value);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        sendRequestJson("http://app.data.zhaogang.com/Kinterface", 1, jObj, resultCallback, mLoadingIndicator,
                "加载中", true);
    }
}
