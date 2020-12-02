package com.bigun.wifioscilloscope.util;

import java.text.DecimalFormat;
import java.util.Arrays;

import android.widget.TextView;

public class MainHelper {
	/**
	 * 设置Y1Y2灵敏度
	 * 
	 * @param sensitivity
	 */
	public static void setSenSitivity(String sensitivity, TextView tvBl1,
			TextView tvBl2, byte b) {
		try {
			byte[] moshi = ByteUtil.getBooleanArray(b);
			String jiaozhi1;
			String jiaozhi2;
			if (moshi[2] == 0)
				jiaozhi1 = "==";
			else
				jiaozhi1 = "≈≈";

			if (moshi[0] == 0)
				jiaozhi2 = "==";
			else
				jiaozhi2 = "≈≈";

			if (sensitivity.length() < 2) {
				sensitivity = "0" + sensitivity;
			}
			if ("00".equals(sensitivity)) {
				tvBl1.setText("Y1" + jiaozhi1 + "10mV");
				tvBl2.setText("Y2" + jiaozhi2 + "10mV");
			} else {
				int y2 = Integer.valueOf(sensitivity.charAt(0) + "");
				int y1 = Integer.valueOf(sensitivity.charAt(1) + "");
				String y1S = "";
				String y2S = "";
				switch (y1) {
				case 0:
					y1S = "Y1" + jiaozhi1 + "10mV";
					break;
				case 1:
					y1S = "Y1" + jiaozhi1 + "20mV";
					break;
				case 2:
					y1S = "Y1" + jiaozhi1 + "50mV";
					break;
				case 3:
					y1S = "Y1" + jiaozhi1 + "100mV";
					break;
				case 4:
					y1S = "Y1" + jiaozhi1 + "200mV";
					break;
				case 5:
					y1S = "Y1" + jiaozhi1 + "500mV";
					break;
				case 6:
					y1S = "Y1" + jiaozhi1 + "1V";
					break;
				case 7:
					y1S = "Y1" + jiaozhi1 + "2V";
					break;
				case 8:
					y1S = "Y1" + jiaozhi1 + "5V";
					break;
				}
				switch (y2) {
				case 0:
					y2S = "Y2" + jiaozhi2 + "10mV";
					break;
				case 1:
					y2S = "Y2" + jiaozhi2 + "20mV";
					break;
				case 2:
					y2S = "Y2" + jiaozhi2 + "50mV";
					break;
				case 3:
					y2S = "Y2" + jiaozhi2 + "100mV";
					break;
				case 4:
					y2S = "Y2" + jiaozhi2 + "200mV";
					break;
				case 5:
					y2S = "Y2" + jiaozhi2 + "500mV";
					break;
				case 6:
					y2S = "Y2" + jiaozhi2 + "1V";
					break;
				case 7:
					y2S = "Y2" + jiaozhi2 + "2V";
					break;
				case 8:
					y2S = "Y2" + jiaozhi2 + "5V";
					break;
				}
				tvBl1.setText(y1S);
				tvBl2.setText(y2S);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * 实时设置灵敏度
	 * 
	 * @param tv
	 * @param sensitivity
	 */
	public static void setSenSitivity(TextView tv, int sensitivity) {
		String tvStr = "";
		switch (sensitivity) {
		case 0:
			tvStr = "10mV";
			break;
		case 1:
			tvStr = "20mV";
			break;
		case 2:
			tvStr = "50mV";
			break;
		case 3:
			tvStr = "100mV";
			break;
		case 4:
			tvStr = "200mV";
			break;
		case 5:
			tvStr = "500mV";
			break;
		case 6:
			tvStr = "1V";
			break;
		case 7:
			tvStr = "2V";
			break;
		case 8:
			tvStr = "5V";
			break;
		}
		tv.setText(tvStr);
	}

	/**
	 * 设置电量
	 * 
	 * @param b
	 * @param tvBl6
	 */
	public static void setElectric(byte[] b, TextView tvBl6) {
		int e1 = b[8] << 8;
		int e2 = b[7]&0xff;
		int e = e1 + e2;
		float ex = (e - 3500);
		float d = ex / 7;
		if (d > 100)
			d = 100;
		if (d < 0)
			d = 0;
		DecimalFormat df = new DecimalFormat("0.00");// 格式化小数
		String s = df.format(d);// 返回的是String类型
		tvBl6.setText("电量:" + s + "%");
	}
}
