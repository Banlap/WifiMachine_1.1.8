package com.bigun.ndk;

public class Crc16 {
	static {
		System.loadLibrary("WifiCRC16");
	}

	public native char checkCode(byte[] b, int len);
}
