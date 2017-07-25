package com.chikai.renterapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.chikai.renterapp.net.CommonResponseListener;
import com.chikai.renterapp.net.NetEngine;
import com.chikai.renterapp.net.OnResponseListener;
import com.chikai.renterapp.net.response.TestResponseBean;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        NetEngine.sendTestRequest(MainActivity.this, new CommonResponseListener<TestResponseBean>() {
            @Override
            public void onSucceed(TestResponseBean testResponseBean) {

            }

        },MainActivity.class.getSimpleName());
    }
}
