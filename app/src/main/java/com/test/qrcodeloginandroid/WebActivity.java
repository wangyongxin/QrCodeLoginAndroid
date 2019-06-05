package com.test.qrcodeloginandroid;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.Toast;

import com.github.lzyzsd.jsbridge.BridgeHandler;
import com.github.lzyzsd.jsbridge.BridgeWebView;
import com.github.lzyzsd.jsbridge.CallBackFunction;
import com.github.lzyzsd.jsbridge.DefaultHandler;
import com.google.gson.Gson;
import com.google.zxing.common.StringUtils;

import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WebActivity extends AppCompatActivity {

    private final String TAG = "MainActivity";
    int RESULT_CODE = 0;

    @BindView(R.id.webView)
    BridgeWebView webView;

    String userId = null;

    ValueCallback<Uri> mUploadMessage;

    ValueCallback<Uri[]> mUploadMessageArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        ButterKnife.bind(this);


        webView.setDefaultHandler(new DefaultHandler());

        webView.setWebChromeClient(new WebChromeClient() {

            @SuppressWarnings("unused")
            public void openFileChooser(ValueCallback<Uri> uploadMsg, String AcceptType, String capture) {
                this.openFileChooser(uploadMsg);
            }

            @SuppressWarnings("unused")
            public void openFileChooser(ValueCallback<Uri> uploadMsg, String AcceptType) {
                this.openFileChooser(uploadMsg);
            }

            public void openFileChooser(ValueCallback<Uri> uploadMsg) {
                mUploadMessage = uploadMsg;
                pickFile();
            }

            @Override
            public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, FileChooserParams fileChooserParams) {
                mUploadMessageArray = filePathCallback;
                pickFile();
                return true;
            }
        });

        Intent i = getIntent();
        String url = i.getStringExtra("url");
        if(url == null || "".equals(url)){
            url = "http://www.baidu.com";
        }
        userId = i.getStringExtra("userId");
        if(userId == null || "".equals(userId)){
            userId = "49";
        }
        webView.loadUrl(url);
//        webView.loadUrl("file:///android_asset/demo.html");

        webView.registerHandler("qr_login", new BridgeHandler() {

            @Override
            public void handler(String data, CallBackFunction function) {
                Log.i(TAG, "handler = submitFromWeb, data from web = " + data);
//                function.onCallBack("submitFromWeb exe, response data 中文 from Java");
                Gson gson = new Gson();
                Map<String, Object> mapData = null;
                String auth_code = null;
                try {
                    mapData = gson.fromJson(data, Map.class);
                    auth_code = (String)mapData.get("auth_code");
                }catch (Exception e){

                }

                if(auth_code != null){
                    String jsonData = "{\"userId\":"+userId+", \"auth_code\": \""+auth_code+"\"}";
                    String encode1 = Base64.encodeToString(jsonData.getBytes(), Base64.NO_WRAP);
                    String s = encode1 + "ttplus";
                    String encode2 = Base64.encodeToString(s.getBytes(), Base64.NO_WRAP);
                    System.out.println("secret_code: " + encode2);



                    HttpService service = HttpUtil.getRetrofit().create(HttpService.class);
                    Call<CodeMsg<Map<String, Object>, String>> call = service.comfirmLogin(encode2);
                    call.enqueue(new Callback<CodeMsg<Map<String, Object>, String>>() {
                        @Override
                        public void onResponse(Call<CodeMsg<Map<String, Object>, String>> call, Response<CodeMsg<Map<String, Object>, String>> response) {
//                        System.out.println("-------------post------------" + response.body());
                            CodeMsg<Map<String, Object>, String> data = response.body();
                            if (data.isSuccess()) {
                                Toast.makeText(WebActivity.this, data.content.get("msg").toString(), Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(WebActivity.this, data.errMsg, Toast.LENGTH_SHORT).show();
                            }

                            finish();
                        }

                        @Override
                        public void onFailure(Call<CodeMsg<Map<String, Object>, String>> call, Throwable t) {
                            Toast.makeText(WebActivity.this, "网络超时", Toast.LENGTH_SHORT).show();
                        }
                    });
                }

            }

        });

//        User user = new User();
//        Location location = new Location();
//        location.address = "SDU";
//        user.location = location;
//        user.name = "大头鬼";

//        webView.callHandler("functionInJs", new Gson().toJson(user), new CallBackFunction() {
//            @Override
//            public void onCallBack(String data) {
//
//            }
//        });
//
//        webView.send("hello");
    }

    public void pickFile() {
        Intent chooserIntent = new Intent(Intent.ACTION_GET_CONTENT);
        chooserIntent.setType("image/*");
        startActivityForResult(chooserIntent, RESULT_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == RESULT_CODE) {
            if (null == mUploadMessage && null == mUploadMessageArray){
                return;
            }
            if(null!= mUploadMessage && null == mUploadMessageArray){
                Uri result = intent == null || resultCode != RESULT_OK ? null : intent.getData();
                mUploadMessage.onReceiveValue(result);
                mUploadMessage = null;
            }

            if(null == mUploadMessage && null != mUploadMessageArray){
                Uri result = intent == null || resultCode != RESULT_OK ? null : intent.getData();
                mUploadMessageArray.onReceiveValue(new Uri[]{result});
                mUploadMessageArray = null;
            }

        }
    }
}
