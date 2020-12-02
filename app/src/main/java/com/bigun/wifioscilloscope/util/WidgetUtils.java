package com.bigun.wifioscilloscope.util;

import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.RelativeLayout;

/*
 * 获取、设置控件信息
 */
public class WidgetUtils {
	/*
	 * 获取控件宽
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
	 * 获取控件高
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
	 * 设置控件所在的位置X，并且不改变宽高， X为绝对位置，此时Y可能归0
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
	 * 设置控件所在的位置Y，并且不改变宽高， Y为绝对位置，此时X可能归0
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
	 * 设置控件所在的位置YY，并且不改变宽高， XY为绝对位置
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
	 * TODO设置控件的坐标和大小
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