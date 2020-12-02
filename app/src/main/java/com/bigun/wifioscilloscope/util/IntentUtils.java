package com.bigun.wifioscilloscope.util;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.util.Log;

public class IntentUtils {
	public static void goToOtherApp(Activity mActivity, String packageName) {
		try {
			PackageManager packageManager = mActivity.getPackageManager();
			Intent intent = new Intent();
			intent = packageManager.getLaunchIntentForPackage(packageName);
			mActivity.startActivity(intent);
		} catch (Exception e) {
			Log.e("GVSAppView", "������ת����Ӧ��,�����Ƿ�װ�˸�Ӧ��");
		}
	}
}
