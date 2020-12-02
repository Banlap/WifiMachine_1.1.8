package com.bigun.wifioscilloscope.view;

import com.bigun.wifioscilloscope.BigunApp;
import com.bigun.wifioscilloscope.util.WidgetUtils;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

/**
 * Y1视图
 * 
 * @author Administrator
 * 
 */
public class Y1View extends View {
	private Paint mPaint;

	private Path mPath;

	private View view;
	private boolean isUpdate = false;
	private boolean isUpdate2 = false;
	private int W;
	private int H;

	private float totalL = 0;
	private float startY = 0;

	private float mGridX;

	public Y1View(Context context, int W, int H) {
		super(context);
		this.W = W;
		this.H = H;
		mPaint = new Paint();
		mPaint.setColor(Color.RED);
		mPaint.setStyle(Style.STROKE);
		mPaint.setStrokeWidth(1.5f);
		mPath = new Path();

		// 横坐标每个格的间隔
		mGridX = (float) ((W) / 20f);
	}

	// 自定义view的宽高
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// int width = W;
		// int height = H;
		setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);
		// 自定义view的宽高时，不实用下面函数
		// super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}

	public Y1View(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		canvas.drawPath(mPath, mPaint);
		if (BigunApp.isFullPoint) {
			mPath.reset();
		}

	}

	public void clearView() {
		BigunApp.isFullPoint = true;
		mPath.reset();
		invalidate();
	}

	public void updateY(View vProgress) {
		//System.out.println("updateY:" + W + "  /  " + H);
		int width = (int) (W - BigunApp.SLIDE_X);
		if (width < 0) {
			width = 0;
		}
		//onMeasure(width, H);
		onMeasure(width, H);
		//重新刷新view 并进行onDraw()方法
		invalidate();
		// totalL = 0;
		if (BigunApp.isFullPoint)
			startY = 0;
	}

	public void isStatus(){
		isUpdate = true;
	}

	public void lineY(float length, int i) {
		// System.out.println("103-----------:" + i);
		//System.out.println("length: " + length + "  i: " +  i);
		if (startY == 0) {
			startY = i;
			mPath.moveTo(0, startY);
		}
		// System.out.println("106-----length:" + length);
		mPath.lineTo(length * 1, i);
		// totalL += (mGridX / 25) * BigunApp.XTimes;
	}

}
