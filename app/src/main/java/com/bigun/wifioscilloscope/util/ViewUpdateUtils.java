package com.bigun.wifioscilloscope.util;

import android.view.View;
import android.widget.ImageView;

import com.bigun.wifioscilloscope.BigunApp;
import com.bigun.wifioscilloscope.R;
import com.bigun.wifioscilloscope.view.TView;
import com.bigun.wifioscilloscope.view.Y1View;
import com.bigun.wifioscilloscope.view.Y2View;

import java.io.UnsupportedEncodingException;
import java.util.Base64;

public class ViewUpdateUtils {
	static int offset = 12;
	private static float hr;
	private static float hb;
	private static int lastRealPoint = 0;

	private static byte[] tempB = null;
	private static int tempBIndex = 0;
	private static Boolean isSort = false;

	public static void viewUpdate(int startX, int mainY, int W, int H,
			Y1View y1, Y2View y2, Object pts, ImageView vRedTriangle,
			ImageView vBlueTriangle, TView mTview, View vProgress,
			int currentTimes, int moveUpW, int moveUpH) {
		BigunApp.isFullPoint = true;
		byte[] b = (byte[]) pts;
		//
		// 这个总点数跟登录时发的采样点参数一致
		int totalPoint = 500 / currentTimes;
		int temp1 = b[11] & 0xff;
		int temp2 = b[12] << 8;
		int realPoint = temp1 + temp2;

		System.out.println("realPoint: " + realPoint + "  totalPoint: " + totalPoint + " vProgress: "  +  vProgress.getX());
		if (realPoint < totalPoint) {

			ViewUpdateWhenPiontLess500.viewUpate(startX, mainY, W, H, y1, y2,
					pts, vRedTriangle, vBlueTriangle, mTview, vProgress,
					currentTimes, realPoint, moveUpW, moveUpH);
			return;
		}

		float length = 0;
		int count = 0;
		int count2 = 0;
		for (int i = 23; i < b.length; i++) {
			if (count == totalPoint && count2 == totalPoint) {
				// 更新波形图
				y1.updateY(vProgress);
				y2.updateY(vProgress);

				// 更新左右三角形位置
				hr = (float) ((b[19] * 2.5) / 2.54f);
				hb = (float) ((b[20] * 2.5) / 2.54f);
				setImageStyle(vRedTriangle, vBlueTriangle);
				offset = vRedTriangle.getHeight() / 2;
				float hrS = (hr / 50f) * H / 2 - mainY + offset;
				float hbS = (hb / 50f) * H / 2 - mainY + offset;

				vRedTriangle.setY((int) (H / 2 - hrS));
				vBlueTriangle.setY((int) (H / 2 - hbS));

				// 更新触发电平虚线
				// System.out.println("305-----:" + (byte) b[21]);
				if (BigunApp.isUpdataT == true) {
					float ht = (byte) b[21];
					float htS = (ht / 50f) * H / 2;
					mTview.setY(BigunApp.screenPoint.y / 2 - htS);
				}

				length = 0;
				count = 0;
				count2 = 0;
				break;
			}
			if (i % 2 != 0) {
				// 横坐标每个点的间隔
				length += ((W / 500f) * currentTimes);
				count++;
				float h1 = (byte) b[i] / 2.54f;
				float h1S = (h1 / 50f) * H / 2;
				y1.lineY((int) (length), (int) (H / 2 - h1S));

			} else {
				// length += (((W) / 20f) / 5);
				count2++;
				float h2 = (byte) b[i] / 2.54f;
				float h2S = (h2 / 50f) * H / 2;
				y2.lineY((int) (length), (int) (H / 2 - h2S));
			}
		}

		// 分包处理
		// if (lastRealPoint + realPoint < 500) {
		// if (tempB == null) {
		// tempB = new byte[1024];
		// System.arraycopy(b, 0, tempB, 0, 23 + realPoint);
		// // System.arraycopy(tempB, 0, b, 0, 23 + realPoint);
		// tempBIndex += 23 + realPoint;
		// lastRealPoint += realPoint;
		// isSort = true;
		// return;
		// }
		// System.arraycopy(b, 23, tempB, tempBIndex, realPoint);
		// lastRealPoint += realPoint;
		// tempBIndex += realPoint;
		// isSort = true;
		// return;
		// }
		// if (isSort) {
		// // System.out.println("194==:"+b[24]);
		// // int t1 = b[24]&0xff;
		// // int t2 = b[25]&0xff;
		// // int t3 = b[26]&0xff;
		// // if (t1 + t2 + t3 == 0) {
		// // System.arraycopy(tempB, tempBIndex, b, 23, realPoint * 2);
		// // } else {
		// System.arraycopy(b, 23, tempB, tempBIndex, realPoint);
		// // }
		// lastRealPoint = 0;
		// tempBIndex = 0;
		// isSort = false;
		// b = tempB;
		// tempB = null;
		// }

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
