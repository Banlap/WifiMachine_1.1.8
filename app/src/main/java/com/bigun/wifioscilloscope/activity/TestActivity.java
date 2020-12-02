package com.bigun.wifioscilloscope.activity;

import com.bigun.wifioscilloscope.R;
import com.bigun.wifioscilloscope.R.layout;
import com.bigun.wifioscilloscope.view.CurveBgView;

import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

public class TestActivity extends Activity {
	private ViewGroup mRootView;
	@SuppressLint("NewApi") @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_test);
//		mRootView = (ViewGroup) findViewById(R.id.rootView);
////		CurveBgView curveView = new CurveBgView(this);
//		PathEffectView view = new PathEffectView(this);
//		mRootView.addView(curveView);
//		curveView.setX(30f);
//		curveView.setY(30f);
	}

}
