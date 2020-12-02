package com.bigun.wifioscilloscope.view;

import com.bigun.wifioscilloscope.R;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.util.AttributeSet;
import android.view.View;

/**
 * @author Administrator һ��10�� 1��=25mv
 */
public class CurveBgViewS extends View {
	private Paint mPaint;
	private int U = ViewManager.BG_SIZE;
	private int W;
	private int H;

	public CurveBgViewS(Context context, int mainW, int mainH) {
		super(context);
		W = mainW;
		H = mainH;
	}

	public CurveBgViewS(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		printLayout(canvas);
	}

	/**
	 * ���Ʊ���
	 * 
	 * @param canvas
	 */
	private void printLayout(Canvas canvas) {
		mPaint = new Paint();
		mPaint.setColor(getResources().getColor(R.color.purple));
		// // �����ʽ
		// mPaint.setStrokeJoin(Join.MITER);
		// // �����ο�
		mPaint.setStyle(Style.STROKE);
		// ���ʿ��
		mPaint.setStrokeWidth(2);
		// �������
		mPaint.setAntiAlias(true);
		mPaint.setDither(true);
		Path path = new Path();
		PathEffect effectX = new DashPathEffect(new float[] { 2f,
				(float) ((W / 20f) / 5) - 2.1f }, 0);
		mPaint.setAntiAlias(true);
		mPaint.setPathEffect(effectX);
		// X������
		for (int i = 0; i < 10; i++) {
			if (i == 5) {
				mPaint.setStrokeWidth(7f);
			} else {
				mPaint.setStrokeWidth(2f);
			}
			if (i != 0) {
				path.reset();
				path.moveTo(0, H / 10 * i);
				path.lineTo(W, H / 10 * i);
				canvas.drawPath(path, mPaint);
			}
		}
		// Y�����ߣ�Y����������ڿ�/20
		PathEffect effectY = new DashPathEffect(new float[] { 2f,
				(float) (H / 10 / 6) }, 0);// ÿ����ļ����
		mPaint.setAntiAlias(true);
		mPaint.setPathEffect(effectY);
		for (int x = 0; x < 20; x++) {
			if (x == 10) {
				mPaint.setStrokeWidth(7f);
			} else {
				mPaint.setStrokeWidth(2f);
			}
			if (x != 0) {
				path.reset();
				path.moveTo((float) (W / 20f * x + 2.1f), 0);
				path.lineTo((float) (W / 20f * x + 2.1f), H);
				canvas.drawPath(path, mPaint);
			}
		}
	}

}
