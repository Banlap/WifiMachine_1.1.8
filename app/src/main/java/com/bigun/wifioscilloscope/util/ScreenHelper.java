package com.bigun.wifioscilloscope.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.DisplayMetrics;
import android.view.WindowManager;

public class ScreenHelper {

	/**
	 * Get Window Point
	 * 
	 * @param context
	 * @return Point
	 */
	@SuppressLint("NewApi")
	public static Point screenSize(Context context) {
		WindowManager wm = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		Point point = new Point();
		wm.getDefaultDisplay().getSize(point);
		return point;
	}

	/**
	 * TODO��ȡӦ��������
	 * 
	 * @return Rect
	 */
	public static Point appSize(Context acty) {
		Point point = screenSize(acty);
		point.y -= statusBarHeight(acty);
		return point;
	}

	/**
	 * TODO ��ȡ״̬��
	 * 
	 * @param context
	 * @return int
	 */
	public static int statusBarHeight(Context context) {
		int resourceId = context.getResources().getIdentifier(
				"status_bar_height", "dimen", "android");
		if (resourceId > 0) {
			return context.getResources().getDimensionPixelSize(resourceId);
		}
		return 0;
	}

	/**
	 * ��ȡ��Ļ�ܶȣ���λpx/cm
	 * 
	 * @param context
	 * @return
	 */
	public static float getScreenXppiCM(Context context) {
		DisplayMetrics metrics = context.getResources().getDisplayMetrics();
		float mX = (float) (metrics.xdpi / 2.54);// px/inch
		return mX;
	}
}
