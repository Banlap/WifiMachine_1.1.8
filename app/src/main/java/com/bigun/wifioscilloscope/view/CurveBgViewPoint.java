package com.bigun.wifioscilloscope.view;

import java.util.ArrayList;
import java.util.List;

import com.bigun.wifioscilloscope.R;
import com.bigun.wifioscilloscope.util.ByteUtil;
import com.bigun.wifioscilloscope.util.DrawUtils;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.util.AttributeSet;
import android.view.View;

/**
 * @author Administrator һ��10�� 1��=25mv
 */
public class CurveBgViewPoint extends View {
	private Paint mPaint;
	private int W;
	private int H;

	public CurveBgViewPoint(Context context, int mainW, int mainH) {
		super(context);
		W = mainW;
		H = mainH;
	}

	public CurveBgViewPoint(Context context, AttributeSet attrs) {
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
		// Path path = new Path();
		// PathEffect effectX = new DashPathEffect(new float[] { 1f,
		// (float) (((W) / 20f) / 5) - 1 }, 0);
		mPaint.setAntiAlias(true);
		// mPaint.setPathEffect(effectX);

		// ������ÿ����ļ��
		float itX = (float) (((W) / 20f) / 5);
		// ������������
		int countX = (int) (W / itX);
		// ��������������֮��ļ��
		float differX = H / 10f;
		// ������ÿ����ļ��
		float itY = (float) (((H) / 10f) / 5);
		// ������������
		int countY = (int) (H / itY);
		// ��������������֮��ļ��
		float differY = W / 20f;
		// �������������ϵĵ������
		// float[] t = new float[] { 0, 0, 0, itY, 0, itY*2, 0, itY*3, };
		List<Float> psX = new ArrayList<Float>();
		for (int j = 0; j < 10; j++) {
			for (int i = 1; i <= countX; i++) {
				psX.add((float) i * itX);
				psX.add(differX * (j + 1));
			}
		}

		List<Float> psY = new ArrayList<Float>();
		for (int j = 0; j < 20; j++) {
			for (int i = 1; i <= countY; i++) {
				psY.add(differY * (j + 1));
				psY.add((float) i * itY);
			}
		}

		float pts[] = ByteUtil.floatMerger(DrawUtils.getPtsByList(psX),
				DrawUtils.getPtsByList(psY));

		canvas.drawPoints(pts, mPaint);
	}
}
