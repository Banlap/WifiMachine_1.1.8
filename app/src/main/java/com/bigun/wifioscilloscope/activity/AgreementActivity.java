package com.bigun.wifioscilloscope.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bigun.wifioscilloscope.BigunApp;
import com.bigun.wifioscilloscope.R;
import com.bigun.wifioscilloscope.util.WidgetUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

@ContentView(R.layout.activity_agreement)
public class AgreementActivity extends Activity {

    @ViewInject(R.id.iv_bg_agreement)
    private ImageView mIvBgAgreement;

    @ViewInject(R.id.tv_agreement)
    private TextView mTvAgreement;

    @ViewInject(R.id.tv_return)
    private TextView mTvReturn;

    @ViewInject(R.id.tv_agreement_title)
    private TextView mTvAgreementTitle;

    @ViewInject(R.id.sv_agreement)
    private ScrollView mSvAgreement;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        ViewUtils.inject(this);
        initViewStyle();
        initHttpText();
    }

    private void initHttpText() {

       /* new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL aURL = new URL("http://new.heyingyun.com/xfc/WiFi示波器隐私协议.html");
                    HttpURLConnection conn = (HttpURLConnection) aURL.openConnection();
                    conn.setRequestMethod("GET");
                    conn.setConnectTimeout(5 * 1000);
                    conn.connect();
                    InputStream is = conn.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(is));
                    StringBuffer buffer = new StringBuffer();
                    String line = " ";
                    while ((line = reader.readLine()) != null){
                        buffer.append(line);
                    }
                    String text = buffer.toString();
                    //Toast.makeText(AgreementActivity.this, "内容："+ buffer.toString(),Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();*/

    }

    /**
     * 初始化视图样式
     */
    private void initViewStyle() {

        int bgWidth = (int) (BigunApp.screenPoint.x * 0.95);
        int bgHeight = (int) (BigunApp.screenPoint.y * 0.9);
        WidgetUtils.setSize(mIvBgAgreement, bgWidth, bgHeight);
        //Toast.makeText(LoginActivity.this, "w/h: "+ BigunApp.screenPoint.x + "/" + BigunApp.screenPoint.y, Toast.LENGTH_LONG ).show();
        mIvBgAgreement.setX((float) (BigunApp.screenPoint.x * 0.1) / 3);
        mIvBgAgreement.setY((float) (-bgHeight * 0.1));

        //返回键
        int rWidth = (int) (BigunApp.screenPoint.x * 0.1);
        int rHeight = (int) (BigunApp.screenPoint.y * 0.17);
        WidgetUtils.setSize(mTvReturn, rWidth, rHeight/2);
        mTvReturn.setX((float) (BigunApp.screenPoint.x * 0.1)/2);
        mTvReturn.setY((float) (BigunApp.screenPoint.y * 0.15)/2);

        //隐私协议 标题
        mTvAgreementTitle.setX((float) (BigunApp.screenPoint.x * 0.4));
        mTvAgreementTitle.setY((float) (BigunApp.screenPoint.y * 0.15)/2);

        //隐私协议内容
        int tvWidth = (int) (BigunApp.screenPoint.x * 0.93);
        int tvgHeight = (int) (BigunApp.screenPoint.y * 0.75);
        WidgetUtils.setSize(mSvAgreement, tvWidth, tvgHeight);
        mSvAgreement.setX((float) (BigunApp.screenPoint.x * 0.1)/2);
        mSvAgreement.setY((float) (BigunApp.screenPoint.y * 0.35)/2);


    }

    @OnClick({ R.id.tv_return })
    public void OnClick(View v) {
        switch (v.getId()) {
            case R.id.tv_return:
                finish();
                break;
        }
    }


}
