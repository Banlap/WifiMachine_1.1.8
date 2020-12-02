package com.bigun.wifioscilloscope.util;

import android.os.SystemClock;
import android.view.View;
import android.widget.ImageView;

import com.bigun.wifioscilloscope.BigunApp;
import com.bigun.wifioscilloscope.R;
import com.bigun.wifioscilloscope.view.TView;
import com.bigun.wifioscilloscope.view.Y1View;
import com.bigun.wifioscilloscope.view.Y2View;

public class ViewUpdateWhenPiontLess500 {
	static int offset = 12;
	private static float hr;
	private static float hb;
	public static int lastPoint = 0;
	public static float length = 0;
	public static int countS = 0;
	private static boolean isMove = false;  //����� ���ƶ�����ͼ���ᷴ���������ݣ��򷵻صڶ������ݺ��ˢ��view

	public static void viewUpate(int startX, int mainY, int W, int H,
			Y1View y1, Y2View y2, Object pts, ImageView vRedTriangle,
			ImageView vBlueTriangle, TView mTview, View vProgress,
			int currentTimes, int realPoint, int moveUpW, int moveUpH) {
		byte[] b = (byte[]) pts;
		int totalPoint = 500 / currentTimes;
		int count = 0;
		int count2 = 0;
		BigunApp.isFullPoint = false;
		lastPoint += realPoint;

		isMove = false;
		//System.out.println("lastPoint: " + lastPoint );
		if (lastPoint >= totalPoint) {
			BigunApp.isFullPoint = true;
			isMove = true;
			lastPoint = 0;
		}

		// ��ʼλ��
		int p1 = b[9] & 0xff;
		int p2 = b[10] << 8;
		float progressCurrent = p1 + p2;
		// if (BigunApp.isRestart) {
		// length = 0;
		// y1.clearView();
		// if (progressCurrent == BigunApp.startIndex) {
		// BigunApp.isRestart = false;
		// }
		// return;
		// }

		//System.out.println("105-----��ʼλ��:" + progressCurrent);
		for (int i = 23; i < b.length; i++) {
			if (count >= realPoint && count2 >= realPoint) {

				// �����صڶ������ݺ�Ÿ��²���ͼ
				if(isMove){
					y1.updateY(vProgress);
					y2.updateY(vProgress);
				}

				// ��������������λ��
				hr = (float) ((b[19] * 2.5) / 2.54f);
				hb = (float) ((b[20] * 2.5) / 2.54f);
				setImageStyle(vRedTriangle, vBlueTriangle);
				offset = vRedTriangle.getHeight() / 2;
				float hrS = (hr / 50f) * H / 2 - mainY + offset;
				float hbS = (hb / 50f) * H / 2 - mainY + offset;

				vRedTriangle.setY((int) (H / 2 - hrS));
				vBlueTriangle.setY((int) (H / 2 - hbS));
				// ���´�����ƽ����
				// System.out.println("305-----:" + (byte) b[21]);
				if (BigunApp.isUpdataT == true) {
					float ht = (byte) b[21];
					float htS = (ht / 50f) * H / 2;
					mTview.setY(BigunApp.screenPoint.y / 2 - htS);
				}
				// System.out.println("106-------:" + lastPoint + "----"
				// + totalPoint);
				//System.out.println("107-------:" + length + "-count---:"
				//		+ count);
				if (length - 1 < W && W < length + 1) {
					length = 0;
					//System.out.println("107-------:length����");
				} else if (length + 1 > W) {
					length = 0;
				}

				// System.out.println("109-------:" + countS);
				// if (countS >= 500) {
				// countS = 0;
				// System.out.println("109-------:countS��0");
				// }
				count = 0;
				count2 = 0;
				break;
			}
			if (i % 2 != 0) {
				// ������ÿ����ļ��
				length += (W / 500f) * currentTimes;
				count++;
				countS++;
				float h1 = (byte) b[i] / 2.54f;
				float h1S = (h1 / 50f) * H / 2;
				y1.lineY(length, (int) (H / 2 - h1S));
			} else {
				// length += (((W) / 20f) / 5);
				count2++;
				float h2 = (byte) b[i] / 2.54f;
				float h2S = (h2 / 50f) * H / 2;
				y2.lineY(length, (int) (H / 2 - h2S));
			}
		}


	}

	private static void setImageStyle(ImageView vRedTriangle,
			ImageView vBlueTriangle) {
		if (hr >= 50) {
			hr = 50;
			vRedTriangle.setImageResource(R.drawable.red_triangle_up);
		} else if (hr <= -50) {
			hr = -50;
			vRedTriangle.setImageResource(R.drawable.red_triangle_down);
		} else
			vRedTriangle.setImageResource(R.drawable.red_triangle);

		if (hb >= 50) {
			hb = 50;
			vBlueTriangle.setImageResource(R.drawable.blue_triangle_up);
		} else if (hb <= -50) {
			hb = -50;
			vBlueTriangle.setImageResource(R.drawable.blue_triangle_down);
		} else
			vBlueTriangle.setImageResource(R.drawable.blue_triangle);
	}

}
