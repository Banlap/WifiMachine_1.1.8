package com.bigun.wifioscilloscope.util;

public class CRC16 {
	private short[] crcTable = new short[256];
	private int gPloy = 0x1021; // 生成多项式

	public CRC16() {
		computeCrcTable();
	}

	private short getCrcOfByte(int aByte) {
		int value = aByte << 8;

		for (int count = 7; count >= 0; count--) {
			if ((value & 0x8000) != 0) { // 高第16位为1，可以按位异或
				value = (value << 1) ^ gPloy;
			} else {
				value = value << 1; // 首位为0，左移
			}

		}
		value = value & 0xFFFF; // 取低16位的值
		return (short) value;
	}

	/*
	 * 生成0 - 255对应的CRC16校验码
	 */
	private void computeCrcTable() {
		for (int i = 0; i < 256; i++) {
			crcTable[i] = getCrcOfByte(i);
		}
	}

	// public short getCrc(byte[] data) {
	// int crc = 0;
	// int length = data.length;
	// for (int i = 0; i < length; i++) {
	// crc = ((crc & 0xFF) << 8) ^ crcTable[(((crc & 0xFF00) >> 8) ^ data[i]) &
	// 0xFF];
	// }
	// crc = crc & 0xFFFF;
	// return (short)crc;
	// }
	public char getCrc(byte[] data) {
		int crc = 0;
		int length = data.length;
		for (int i = 0; i < length; i++) {
			crc = ((crc & 0xFF) << 8)
					^ crcTable[(((crc & 0xFF00) >> 8) ^ data[i]) & 0xFF];
		}
		crc = crc & 0xFFFF;
		return (char) crc;
	}

	public static char decode(byte[] Buf) {
		int CRC;
		int i, Temp;
		CRC = 0xffff;
		for (i = 0; i < Buf.length; i++) {
			CRC = CRC ^ byteToInteger(Buf[i]);
			// System.out.println(byteToInteger(Buf[i]));
			for (Temp = 0; Temp < 8; Temp++) {
				if ((CRC & 0x01) == 1)
					CRC = (CRC >> 1) ^ 0xA001;
				else
					CRC = CRC >> 1;
			}
		}
		return (char) CRC;
	}

	public static int byteToInteger(byte b) {
		int value;
		value = b & 0xff;
		return value;
	}
}
