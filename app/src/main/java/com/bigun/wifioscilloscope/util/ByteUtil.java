package com.bigun.wifioscilloscope.util;

public class ByteUtil {
	/**
	 * �ַ���תasc
	 * 
	 * @param s
	 */
	public static byte[] stringToAsc(String s) {
		char[] chars = s.toCharArray(); // ���ַ���ת��Ϊ�ַ�����
		byte[] result = new byte[s.length()];
		for (int i = 0; i < chars.length; i++) {// ������
			result[i] = (byte) chars[i];
		}
		return result;
	}

	/**
	 * byteת16����
	 * 
	 * @param src
	 * @return
	 */
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

	/**
	 * TODO�ַ�ת�ֽ�
	 * 
	 * @param c
	 * @return byte[]
	 */
	public static byte[] charToByte(char c) {
		byte[] b = new byte[2];
		b[0] = (byte) ((c & 0xFF00) >> 8);
		b[1] = (byte) (c & 0xFF);
		return b;
	}

	/**
	 * ����У����
	 * 
	 * @return
	 */
	public static char generateCheckCode(String pdat, int len) {
		char crc = 0xffff;
		int index = 0;
		while (len-- >= 0) {
			crc = pdat.charAt(index++);
			for (int i = 0; i < 8; i++) {
				if ((crc & 0x0001) != 0x00) {
					crc >>= 1;
					crc ^= 0xA001;
				} else {
					crc >>= 1;
				}
			}
		}
		return crc;
	}

	/**
	 * ��byteת��Ϊһ������Ϊ8��byte���飬����ÿ��ֵ����bit
	 */
	public static byte[] getBooleanArray(byte b) {
		byte[] array = new byte[8];
		for (int i = 7; i >= 0; i--) {
			array[i] = (byte) (b & 1);
			b = (byte) (b >> 1);
		}
		return array;
	}

	// uint16_t Crc16(u_int8_t* pdat, u_int32_t len) {
	// uint16_t crc = 0xffff;
	// while (len--) {
	// u_int8_t i;
	// crc = (uint16_t) *pdat++;
	// for (i = 0; i < 8; ++i) {
	// if (crc & 0x0001) {
	// crc >> 1;
	// crc ^= 0xA001;
	// } else
	// crc >>= 1;
	// }
	// }
	// return crc;
	// }
	/**
	 * �ϲ�����byte����
	 * 
	 * @param byte_1
	 * @param byte_2
	 * @return
	 */
	public static byte[] byteMerger(byte[] byte_1, byte[] byte_2) {
		byte[] byte_3 = new byte[byte_1.length + byte_2.length];
		System.arraycopy(byte_1, 0, byte_3, 0, byte_1.length);
		System.arraycopy(byte_2, 0, byte_3, byte_1.length, byte_2.length);
		return byte_3;
	}

	/**
	 * �ϲ�����float����
	 * 
	 * @param byte_1
	 * @param byte_2
	 * @return
	 */
	public static float[] floatMerger(float[] float_1, float[] float_2) {
		float[] float_3 = new float[float_1.length + float_2.length];
		System.arraycopy(float_1, 0, float_3, 0, float_1.length);
		System.arraycopy(float_2, 0, float_3, float_1.length, float_2.length);
		return float_3;
	}

}
