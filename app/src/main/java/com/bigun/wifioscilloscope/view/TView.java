package com.bigun.wifioscilloscope.view;

import com.bigun.wifioscilloscope.R;

import android.annotation.SuppressLint;
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
 * Y1��ͼ
 * 
 * @author Administrator
 * 
 */
public class TView extends View {
	private Paint mPaint;

	private Path mPath;

	private int W;
	private int H;

	@SuppressLint("ResourceAsColor")
	public TView(Context context, int W, int H) {
		super(context);
		this.W = W;
		this.H = H;
		mPaint = new Paint();
		//banlap: ���ĵ�ƽ��ʾ��ɫ�����
		mPaint.setColor(getResources().getColor(R.color.deep_green));
		mPaint.setStyle(Style.STROKE);
		mPaint.setStrokeWidth(10);
		PathEffect effectX = new DashPathEffect(new float[] { 10f, W / 20 / 7 },
				0);
		mPaint.setAntiAlias(true);
		mPaint.setPathEffect(effectX);
		mPath = new Path();
	}

	// �Զ���view�Ŀ��
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int width = W;
		int height = H;
		setMeasuredDimension(width, height);
		// �Զ���view�Ŀ��ʱ����ʵ�����溯��
		// super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}

	public TView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		mPath.moveTo(0, 0);
		mPath.lineTo(W, 0);
		canvas.drawPath(mPath, mPaint);
	}

	public void updateY(int startY) {

		invalidate();
	}

}
