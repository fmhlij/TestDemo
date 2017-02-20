package com.ljb.okhttpdemo;

import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    OkHttpClient okHttpClient = new OkHttpClient();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button button1 = (Button) findViewById(R.id.doGet);
        Button button2 = (Button) findViewById(R.id.doPost);
        Button button3 = (Button) findViewById(R.id.downPost);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doGet();
            }
        });
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doPost();
            }
        });
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                downPost();
            }
        });
    }

    private void doGet(){
        Log.d("ivs","doGet");

        Request.Builder builder = new Request.Builder();
        Request request = builder.get().url("http://www.imooc.com").build();
        execu(request);
    }

    private void doPost(){
        FormBody.Builder formBody = new FormBody.Builder();
        RequestBody requestBody = formBody
                .add("moId","10086")
                .add("platform","1")
                .add("type","6").build();

        Request.Builder builder = new Request.Builder();
        Request request = builder.url("http://192.168.1.200:8080/wamei/mobileController/share.htm").post(requestBody).build();
        execu(request);
    }

    private void downPost(){
        String url = "http://192.168.1.189:8080/wamei/upload/image/63ed0af1ae424870a85fb5980be4d83c.png";
        Request.Builder builder = new Request.Builder();
        Request request = builder.url(url).build();
        down(request);
    }

    private void down(Request request){
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("ivs","onFailure>>>"+call);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                InputStream inputStream = response.body().byteStream();
                FileOutputStream fos = null;
                fos = new FileOutputStream(new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/DCIM/Camera/wangshu.jpg"));
                byte[] buffer = new byte[2048];
                int len = 0;
                while ((len = inputStream.read(buffer)) != -1){
                    fos.write(buffer, 0 , len);
                }
                fos.flush();
                fos.close();
                inputStream.close();
                Log.d("ivs","下载成功");
            }
        });
    }

    private void execu(Request request) {
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("ivs","onFailure>>>"+call);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String string = response.body().string();
                Log.d("ivs",string);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),"请求成功",Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }


}
