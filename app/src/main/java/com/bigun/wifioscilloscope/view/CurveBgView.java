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
public class CurveBgView extends View {
	private Paint mPaint;
	private Paint mPaint_cross;
	private int W;
	private int H;

	public CurveBgView(Context context, int mainW, int mainH) {
		super(context);
		W = mainW;
		H = mainH;
	}

	public CurveBgView(Context context, AttributeSet attrs) {
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
		mPaint.setAntiAlias(true);

		//banlap: ������������ɫ
		mPaint_cross = new Paint();
		mPaint_cross.setColor(getResources().getColor(R.color.deep_orange));
		mPaint_cross.setStyle(Style.STROKE);
		mPaint_cross.setStrokeWidth(5);
		mPaint_cross.setAntiAlias(true);
		mPaint_cross.setDither(true);
		mPaint_cross.setAntiAlias(true);

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
		List<Float> psX = new ArrayList<Float>();
		for (int j = 0; j < 10; j++) {
			for (int i = 1; i <= countX; i++) {
				psX.add((float) i * itX);
				psX.add(differX * (j + 1));
			}
		}
		// �������������ϵĵ������
		List<Float> psY = new ArrayList<Float>();
		for (int j = 0; j < 20; j++) {
			for (int i = 1; i <= countY; i++) {
				psY.add(differY * (j + 1));
				psY.add((float) i * itY);
			}
		}

		// ���������ر����
		List<Float> centerX = new ArrayList<Float>();
		for (int i = 1; i < countX; i++) {
			centerX.add((float) i * itX);
			centerX.add(differX * 5);
		}

		List<Float> centerY = new ArrayList<Float>();
		for (int i = 1; i <= countY; i++) {
			centerY.add(differY * 10);
			centerY.add((float) i * itY);
		}

		float pts[] = ByteUtil.floatMerger(DrawUtils.getPtsByList(psX),
				DrawUtils.getPtsByList(psY));
		float ptsCX[] = DrawUtils.getPtsByList(centerX);
		float ptsCY[] = DrawUtils.getPtsByList(centerY);

		canvas.drawPoints(pts, mPaint);
		mPaint.setStrokeWidth(4);
		//banlap�� ʹ������ɫ����������x��
		canvas.drawPoints(ptsCX, mPaint_cross);
		canvas.drawPoints(ptsCY, mPaint);
	}
}
