package vieboo.http.okhttp.activitys;

import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.okhttp.Request;

import vieboo.http.okhttp.R;
import vieboo.http.okhttp.controller.MainController;
import vieboo.http.okhttp.http.DownloadDelegate;
import vieboo.http.okhttp.http.ImageDelegate;
import vieboo.http.okhttp.http.Param;
import vieboo.http.okhttp.http.io.ResultCallback;
import vieboo.http.okhttp.http.io.TaskResultCallback;


public class MainActivity extends BaseActivity implements TaskResultCallback
{

    ImageView img;
    TextView text;
    MainController mainController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        img = (ImageView) findViewById(R.id.img);
        text = (TextView) findViewById(R.id.text);

        mainController = new MainController();
        mainController.getData1(new Param[]{new Param("oneType", "2"), new Param("twoType", "2"),
                new Param("lastTime", "1447302020000")},
                this, this);

//        System.out.println("1------>" + Thread.currentThread().getName());

//        String url = "https://kyfw.12306.cn/otn/";
//        GetDelegate.getInstance().getAsyn(url, new ResultCallback<String>() {
//            @Override
//            public void onError(Request request, Exception e) {
//                System.out.println("2------>" + Thread.currentThread().getName());
//                System.out.println("2------>" + e.toString());
//            }
//
//            @Override
//            public void onResponse(String response) {
//                System.out.println("3------>" + Thread.currentThread().getName());
//                System.out.println("response--->" + response);
//            }
//        }, null);



//        ImageDelegate.getInstance().displayImage(img, "https://ss0.baidu.com/6ONWsjip0QIZ8tyhnq/it/u=872774238,2449305677&fm=58");
//
//        DownloadDelegate.getInstance().downloadAsyn("http://e.hiphotos.baidu.com/baike/c0%3Dbaike80%2C5%2C5%2C80%2C26/sign=410619fb3d01213fdb3e468e358e5db4/9f510fb30f2442a71525d087d543ad4bd11302ec.jpg",
//                Environment.getExternalStorageDirectory().getAbsolutePath(),
//                new ResultCallback<String>() {
//                    @Override
//                    public void onError(Request request, Exception e) {
//
//                    }
//
//                    @Override
//                    public void onResponse(String response) {
//                        Toast.makeText(getApplicationContext(), "OK~~~~", Toast.LENGTH_SHORT).show();
//                    }
//                });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBefore(Request request, int taskId) {

    }

    @Override
    public void onAfter(int taskId) {

    }

    @Override
    public void onError(Request request, Exception e, int taskId) {
        System.out.println("error------>" + e.getMessage());
    }

    @Override
    public void onResponse(Object response, int taskId) {
        System.out.println("response------>" + response.toString());
        text.setText(response.toString());
    }
}
