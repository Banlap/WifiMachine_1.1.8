package com.bigun.wifioscilloscope.view;

import com.bigun.wifioscilloscope.BigunApp;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.PathEffect;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

/**
 * 垂直游标
 * 
 * @author Administrator
 * 
 */
public class YcursorView extends View {
	private Paint mPaint;

	private Path mPath;

	private int W;
	private int H;

	public YcursorView(Context context, int W, int H) {
		super(context);
		this.W = W;
		this.H = H;
		mPaint = new Paint();
		mPaint.setColor(Color.BLUE);
		mPaint.setStyle(Style.STROKE);
		mPaint.setStrokeWidth(3.5f);
		PathEffect effectX = new DashPathEffect(new float[] { 5f,
				(float) (H / 10 / 6) }, 0);
		mPaint.setAntiAlias(true);
		mPaint.setPathEffect(effectX);
		mPath = new Path();
	}

	// 自定义view的宽高
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int width = W;
		int height = H;
		setMeasuredDimension(width, height);
		// 自定义view的宽高时，不实用下面函数
		// super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}

	public YcursorView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		mPath.moveTo(0, 0);
		mPath.lineTo(0, H);
		canvas.drawPath(mPath, mPaint);
	}

	public void updateY(int startY) {

		invalidate();
	}

	public void setAble() {
		mPaint.setColor(Color.BLUE);
		invalidate();
		setEnabled(true);
	}

	public void setDisAble() {
		mPaint.setColor(Color.GRAY);
		invalidate();
		setEnabled(false);
	}
}
