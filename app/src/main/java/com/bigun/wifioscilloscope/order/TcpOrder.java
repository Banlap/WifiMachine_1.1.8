package com.bigun.wifioscilloscope.order;

import java.util.HashMap;
import java.util.Map;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Message;

import com.bigun.wifioscilloscope.tcp.TcpConnection;
import com.bigun.wifioscilloscope.tcp.TcpConnection.OrderListener;
import com.bigun.wifioscilloscope.util.ByteUtil;
import com.bigun.wifioscilloscope.util.CRC16;

@SuppressLint("HandlerLeak")
public class TcpOrder implements OrderListener {
	private TcpConnection connection = TcpConnection.getInstance();
	private RequestListener listener;
	private Map<String, Object> map = new HashMap<String, Object>();

	public TcpOrder(RequestListener listener) {
		this.listener = listener;
	}

	private Map<String, Object> getParamsMap() {
		map.clear();
		return map;
	}

	@SuppressWarnings("unchecked")
	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0:
				listener.requestSuccess((Map<String, Object>) msg.obj, msg.arg1);
				break;
			case 1:
				listener.requestFail(msg.arg1);
				break;
			}
		};
	};

	/**
	 * 登录
	 */
	public void login() {
		String data = "Login admin 123456 0 500 ";
		String huiche = "\r\n";
		char d = CRC16.decode(data.getBytes());
		byte[] dB = ByteUtil.charToByte(d);
		String dStr = ByteUtil.bytesToHexString(dB);
		connection.order(data + dStr + huiche, this, TcpConnection.LOGIN_TYPE);
	}

	/**
	 * 登录
	 */
	public void login(int startPos, int endPos) {
		String data = "Login admin 123456 " + startPos + " " + endPos + " ";
		String huiche = "\r\n";
		char d = CRC16.decode(data.getBytes());
		byte[] dB = ByteUtil.charToByte(d);
		String dStr = ByteUtil.bytesToHexString(dB);
		connection.order(data + dStr + huiche, this, TcpConnection.LOGIN_TYPE);
	}

	/**
	 * 设置视图窗口
	 */
	public void setView(int startPos, int endPos) {
		// int startPos = endPos - 500;
		String data = "Set_View_Wnd " + startPos + " " + endPos + " ";
		String huiche = "\r\n";
		char d = CRC16.decode(data.getBytes());
		byte[] dB = ByteUtil.charToByte(d);
		String dStr = ByteUtil.bytesToHexString(dB);
		System.out.println("设置视图窗口:" + data + dStr + huiche);
		connection.order(data + dStr + huiche, this,
				TcpConnection.SET_VIEW_TYPE);
	}

	/**
	 * 设置采样参数
	 */
	public void setSampPara(String params) {
		// String data = "Set_Samp_Para " + params + " 16DD\r\n";
		String data = "Set_Samp_Para " + params + " ";
		String huiche = "\r\n";
		char d = CRC16.decode(data.getBytes());
		// 下面没问题
		byte[] dB = ByteUtil.charToByte(d);
		String dStr = ByteUtil.bytesToHexString(dB);
		connection.order(data + dStr + huiche, this,
				TcpConnection.SETSAMPPARA_TYPE);
		// System.out.println("501----:" + data);
	}

	/**
	 * 配置信号发生器
	 * 
	 * @param params
	 */
	public void setGenSetConf(String params) {
		String data = "Sig_Gen_Set_Conf " + params + " ";
		String huiche = "\r\n";
		char d = CRC16.decode(data.getBytes());
		// 下面没问题
		byte[] dB = ByteUtil.charToByte(d);
		String dStr = ByteUtil.bytesToHexString(dB);
		// System.out.println("108-------:" + data + dStr + huiche);
		connection.order(data + dStr + huiche, this,
				TcpConnection.GENSETCONF_TYPE);
		// System.out.println("501----:" + data);
	}

	/**
	 * 上传波形数据
	 */
	public void sigGenSetConf() {
		String data = "Sig_Gen_Set_Conf 1 5.0 5000 80 FAF4\r\n";
		connection.order(data, this, TcpConnection.GENSETCONF_TYPE);
	}

	/**
	 * 上位机下发的心跳数据包
	 */
	public void sendHeart() {
		String data = "Heart_Pack ";
		String huiche = "\r\n";
		char d = CRC16.decode(data.getBytes());
		byte[] dB = ByteUtil.charToByte(d);
		String dStr = ByteUtil.bytesToHexString(dB);

		connection.order(data + dStr + huiche, this, TcpConnection.HEART_TYPE);
	}

	/**
	 * 断开网络连接
	 */
	public void disconNet() {
		String data = "DisconnetMe ";
		String huiche = "\r\n";
		char d = CRC16.decode(data.getBytes());
		byte[] dB = ByteUtil.charToByte(d);
		String dStr = ByteUtil.bytesToHexString(dB);
		System.out.println("103--------------断开网络连接");
		connection.order(data + dStr + huiche, this,
				TcpConnection.DISCONNECT_TYPE);
	}

	@Override
	public void orderSuccess(byte[] data, int orderType) {
		if (orderType == TcpConnection.LOGIN_TYPE) {
			int isLoginSuccess = (int) (data[7] & 0xff);
			if (isLoginSuccess == 0) {
				// System.out.println("登录成功");
				map = getParamsMap();
				map.put("login", 0);
				Message msg = Message.obtain(handler);
				msg.what = 0;
				msg.obj = map;
				msg.arg1 = orderType;
				msg.sendToTarget();
			} else {
				map = getParamsMap();
				map.put("login", 1);
				Message msg = Message.obtain(handler);
				msg.what = 0;
				msg.obj = map;
				msg.arg1 = orderType;
				msg.sendToTarget();
			}
		}
	}

	@Override
	public void orderFail(Exception e, int orderType) {
		Message msg = Message.obtain(handler);
		msg.what = 1;
		msg.arg1 = orderType;
		msg.sendToTarget();
	}

	public interface RequestListener {
		void requestSuccess(Map<String, Object> map, int orderType);

		void requestFail(int orderType);
	}

}
