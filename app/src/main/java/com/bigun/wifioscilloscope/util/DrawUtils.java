package com.bigun.wifioscilloscope.util;

import java.util.List;

import com.bigun.wifioscilloscope.view.ViewManager;

public class DrawUtils {
	public static float[] getPtsByBytes(List<Byte> bytes) {
		float[] pts = new float[bytes.size() * 4];
		int startX = 0;
		int endX = ViewManager.SPACE;
		int startY = bytes.get(0);
		int endY = bytes.get(0);
		for (int i = 0; i < bytes.size(); i++) {
			pts[i] = startX;
			pts[i + 1] = startY;
			pts[i + 2] = endX;
			pts[i + 3] = endY;

			startX += ViewManager.SPACE;
			endX = startX + ViewManager.SPACE;
			// startY = endY;
			endY = bytes.get(i);
		}
		return pts;
	}

	public static float[] getPtsByList(List<Float> l) {
		float[] pts = new float[l.size()];
		for (int i = 0; i < l.size(); i++) {
			pts[i] = l.get(i);
		}
		return pts;
	}
}
