package vieboo.http.okhttp;

import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.okhttp.Request;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import vieboo.http.okhttp.http.DownloadDelegate;
import vieboo.http.okhttp.http.GetDelegate;
import vieboo.http.okhttp.http.ImageDelegate;
import vieboo.http.okhttp.http.PostDelegate;
import vieboo.http.okhttp.http.ResultCallback;


public class MainActivity extends AppCompatActivity
{

    ImageView img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        img = (ImageView) findViewById(R.id.img);

        System.out.println("1------>" + Thread.currentThread().getName());

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


//        String url = "http://app.data.zhaogang.com/Kinterface";
//        JSONObject json = new JSONObject();
//        try{json.put("oneType", "1");
//        json.put("twoType", "1");}catch (Exception e){};
//        PostDelegate.getInstance().postAsyn(url, json, new ResultCallback<String>() {
//            @Override
//            public void onError(Request request, Exception e) {
//                System.out.println("2------>" + e.toString());
//            }
//
//            @Override
//            public void onResponse(String response) {
//                System.out.println("3------->" + response);
//            }
//        });


        ImageDelegate.getInstance().displayImage(img, "https://ss0.baidu.com/6ONWsjip0QIZ8tyhnq/it/u=872774238,2449305677&fm=58");

        DownloadDelegate.getInstance().downloadAsyn("http://e.hiphotos.baidu.com/baike/c0%3Dbaike80%2C5%2C5%2C80%2C26/sign=410619fb3d01213fdb3e468e358e5db4/9f510fb30f2442a71525d087d543ad4bd11302ec.jpg",
                Environment.getExternalStorageDirectory().getAbsolutePath(),
                new ResultCallback<String>() {
                    @Override
                    public void onError(Request request, Exception e) {

                    }

                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(getApplicationContext(), "OK~~~~", Toast.LENGTH_SHORT).show();
                    }
                });

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
}
