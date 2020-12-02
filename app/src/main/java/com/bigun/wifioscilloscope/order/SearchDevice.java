package com.bigun.wifioscilloscope.order;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Logger;

import android.app.Application;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.format.Time;
import android.util.Log;

import com.bigun.wifioscilloscope.bean.Device;
import com.bigun.wifioscilloscope.listener.SearchListener;
import com.bigun.wifioscilloscope.util.ByteUtil;
import com.bigun.wifioscilloscope.util.NetUtil;
import com.bigun.wifioscilloscope.util.portUtils;

public class SearchDevice {
	private Context mContext;
	private Timer timer;
	private DatagramSocket mSocket;
	private DatagramPacket mPacket;
	//private DatagramSocket recvSocket;
	//private DatagramPacket recvPacket;

	private Boolean isSend = true;
	private Boolean isRec = true;
	private Boolean isStop = true;

	private SearchListener mListener;
	private int mZero;

	private byte[] buffer = new byte[25];

	public SearchDevice(Context context, SearchListener listener, int zero) {
		mContext = context;
		mListener = listener;
		mZero = zero;
	}

	// 每3秒发送一次数据包
	TimerTask mTask = new TimerTask() {

		@Override
		public void run() {
			try {
				if (isSend) {
					mZero = mZero-3;
					if (mZero == 0) {
						handler.sendEmptyMessage(1);
						isSend = false;
					} else {
						handler.sendEmptyMessage(mZero);
						mSocket.send(mPacket);
					}
				} else {
					mTask.cancel();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	};

	public void getDeviceByUDP() {
		new Thread(new Runnable() {

			@SuppressWarnings("resource")
			@Override
			public void run() {
				try {
					// 发送
					/*InetAddress serverAddr = InetAddress
							.getByName("10.82.41.63");*/
					InetAddress serverAddr = InetAddress
							.getByName("255.255.255.255");
					mSocket = new DatagramSocket(4321);
					String order = "IS_HYDSO_There? FAF4\r\n";
					byte[] buf = ByteUtil.stringToAsc(order);
					mPacket = new DatagramPacket(buf, buf.length, serverAddr, 37689);

					// 接受
					//recvSocket = new DatagramSocket(37689);
					//recvPacket = new DatagramPacket(buffer, buffer.length);

					// 刷新
					timer = new Timer();
					timer.schedule(mTask, 0, 3000);
					while (isRec) {

						DatagramPacket recvPacket = new DatagramPacket(buffer,
								buffer.length);
						mSocket.receive(recvPacket);

						// 获得ip
						// 用途码为0x06时
						if (buffer[6] == 0x06) {
							StringBuffer sb = new StringBuffer("");
							for (int i = 0; i < 4; i++) {
								int temp = ((int) buffer[7 + i] & 0xff);
								sb.append(temp);
								if (i != 3) {
									sb.append(".");
								}
							}
							// 获得设备名
							StringBuffer sbName = new StringBuffer("");
							for (int x = 0; x < 10; x++) {
								// 13--22
								int temp = (int) (buffer[13 + x] & 0xff);
								if (temp != 0) {
									char a = (char) buffer[13 + x];
									sbName.append(a);
								}
							}
							Device device = new Device();
							device.name = sbName.toString();
							device.ip = sb.toString();
							// System.out.println("101----搜索WIFI设备：" + sb +
							// "---:"
							// + sbName);
							Message msg = Message.obtain(handler);
							msg.what = 0;
							msg.obj = device;
							msg.sendToTarget();
							isSend = false;
							isRec = false;
						}
					}
				} catch (Exception e) {
					// System.out.println("101---------:" + e.getMessage());
					requestFail(e);
				} finally {
					if(mSocket != null) mSocket.close();
				}
			}
		}).start();
	}

	public void stopSearch(boolean stop){
		if (mTask != null) {
			mTask.cancel();
			mTask = null;
			Log.d("Test by ab:", "mTask is cancel");


		}
	}

	private void requestFail(Exception e) {
		mListener.searchFail(e);
		isSend = true;
		isRec = true;

	}

	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {

			switch (msg.what) {
				case 0:
					mListener.searchSuccess((Device) msg.obj);
					break;
				case 1:
					Log.e("Test by ab:", "false");
					break;
			}
		};
	};


}
