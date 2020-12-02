package com.bigun.wifioscilloscope.util;

import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.RelativeLayout;

/*
 * ��ȡ�����ÿؼ���Ϣ
 */
public class WidgetUtils {
	/*
	 * ��ȡ�ؼ���
	 */
	public static int getWidth(View view) {
		int w = View.MeasureSpec.makeMeasureSpec(0,
				View.MeasureSpec.UNSPECIFIED);
		int h = View.MeasureSpec.makeMeasureSpec(0,
				View.MeasureSpec.UNSPECIFIED);
		view.measure(w, h);
		return (view.getMeasuredWidth());
	}
	
	public static void initSize(View view,int width,int height){
		LayoutParams params = new LayoutParams(width,height);
		view.setLayoutParams(params);
	}
	/*
	 * ��ȡ�ؼ���
	 */
	public static int getHeight(View view) {
		int w = View.MeasureSpec.makeMeasureSpec(0,
				View.MeasureSpec.UNSPECIFIED);
		int h = View.MeasureSpec.makeMeasureSpec(0,
				View.MeasureSpec.UNSPECIFIED);
		view.measure(w, h);
		return (view.getMeasuredHeight());
	}

	/*
	 * ���ÿؼ����ڵ�λ��X�����Ҳ��ı��ߣ� XΪ����λ�ã���ʱY���ܹ�0
	 */
	public static void setPointX(View view, int x) {
		MarginLayoutParams margin = new MarginLayoutParams(
				view.getLayoutParams());
		margin.setMargins(x, margin.topMargin, 0, 0);
		RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
				margin);
		view.setLayoutParams(layoutParams);
	}

	/*
	 * ���ÿؼ����ڵ�λ��Y�����Ҳ��ı��ߣ� YΪ����λ�ã���ʱX���ܹ�0
	 */
	public static void setPointY(View view, int y) {
		MarginLayoutParams margin = new MarginLayoutParams(
				view.getLayoutParams());
		margin.setMargins(margin.leftMargin, y, margin.rightMargin, y
				+ margin.height);
		RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
				margin);
		view.setLayoutParams(layoutParams);
	}

	public static int getPointY(View view) {
		MarginLayoutParams margin = new MarginLayoutParams(
				view.getLayoutParams());
		return margin.topMargin;
	}

	public static int getPointX(View view) {
		MarginLayoutParams margin = new MarginLayoutParams(
				view.getLayoutParams());
		return margin.leftMargin;
	}

	/*
	 * ���ÿؼ����ڵ�λ��YY�����Ҳ��ı��ߣ� XYΪ����λ��
	 */
	public static void setPoint(View view, int x, int y) {
		MarginLayoutParams margin = new MarginLayoutParams(
				view.getLayoutParams());
		margin.setMargins(x, y, 0, 0);
		RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
				margin);
		view.setLayoutParams(layoutParams);
	}

	public static void setWidth(View view, int width) {
		LayoutParams layoutParams = view.getLayoutParams();
		layoutParams.width = width;
		view.setLayoutParams(layoutParams);
	}

	public static void setHeight(View view, int height) {
		LayoutParams layoutParams = view.getLayoutParams();
		layoutParams.height = height;
		view.setLayoutParams(layoutParams);
	}

	public static void setSize(View view, int width, int height) {
		LayoutParams layoutParams = view.getLayoutParams();
		layoutParams.width = width;
		layoutParams.height = height;
		view.setLayoutParams(layoutParams);
	}

	/**
	 * TODO���ÿؼ�������ʹ�С
	 * 
	 * @param view
	 * @param width
	 * @param height
	 * @param x
	 * @param y
	 *            void
	 */
	public static void Rect(View view, int width, int height, int x, int y) {
		setSize(view, width, height);
		setPoint(view, x, y);
	}
}