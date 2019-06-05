package com.test.qrcodeloginandroid;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


//import com.vondear.rxfeature.activity.ActivityScanerCode;
//import com.vondear.rxfeature.module.scaner.OnRxScanerListener;
//import com.vondear.rxtool.RxActivityTool;


import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.github.xudaojie.qrcodelib.CaptureActivity;

public class MainActivity extends AppCompatActivity {

    private static int REQUEST_QR_CODE = 1;
    @BindView(R.id.userIdEt)
    EditText userIdEt;
//    @BindView(R.id.testBaiduBtn)
//    Button testBaiduBtn;
//    @BindView(R.id.scanBtn)
//    Button scanBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

    }

    @OnClick(R.id.testBaiduBtn)
    public void testBaiduClick(View v) {
        System.out.println("测试打开百度");

    }
    @OnClick(R.id.scanBtn)
    public void testScanClick(View v) {
        System.out.println("测试扫描二维码");

        Intent i = new Intent(MainActivity.this, CaptureActivity.class);
        startActivityForResult(i, REQUEST_QR_CODE);

//        Intent intent = new Intent(MainActivity.this, QrcodeScanActivity.class);
//        startActivity(intent);
        /*OnRxScanerListener listener = new OnRxScanerListener() {

            @Override
            public void onSuccess(String type, com.google.zxing.Result result) {
                System.out.println("二维码扫描结果： --> " + result.getText());
            }

            @Override
            public void onFail(String type, String message) {

            }
        };
        ActivityScanerCode.setScanerListener(listener);
        RxActivityTool.skipActivity(this, ActivityScanerCode.class);*/
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK
                && requestCode == REQUEST_QR_CODE
                && data != null) {
            String result = data.getStringExtra("result");
            Toast.makeText(MainActivity.this, result, Toast.LENGTH_SHORT).show();

            Intent i = new Intent(MainActivity.this, WebActivity.class);
            i.putExtra("url", result);
            i.putExtra("userId", userIdEt.getText().toString());
            startActivity(i);

        }
    }
}
