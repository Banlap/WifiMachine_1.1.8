package com.bigun.wifioscilloscope.util;

public class portUtils {
	/**
	 * TODOªÒ»°…˘“Ù∑÷±¥
	 * 
	 * @param buffer
	 * @return float
	 */
	public static float getSoundDBVal(byte[] buffer, int size) {
		byte checkSum = (byte) 0xFF;
		float valResult = 0;
		if (size != 5)
			return -1;
		if (size < 5)
			return -1;
		checkSum = (byte) (buffer[1] + buffer[2]);


		if (checkSum == buffer[3] && "aa".endsWith(Integer.toHexString(buffer[0]&0xff))
				&& "ff".endsWith(Integer.toHexString(buffer[4]&0xff))) {
			valResult = buffer[1] * 256 + buffer[2];
			valResult = valResult / 10;
			return valResult;
		}
		return -2;
	}

	public static String bytesToHexString(byte[] src) {
		StringBuilder stringBuilder = new StringBuilder("");
		if (src == null || src.length <= 0) {
			return null;
		}
		for (int i = 0; i < src.length; i++) {
			int v = src[i] & 0xFF;
			String hv = Integer.toHexString(v);
			if (hv.length() < 2) {
				stringBuilder.append(0);
			}
			stringBuilder.append(hv);
		}
		return stringBuilder.toString();
	}
}
