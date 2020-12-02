package com.bigun.wifioscilloscope.tcp;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.widget.Toast;

import com.bigun.wifioscilloscope.activity.LoginActivity;
import com.bigun.wifioscilloscope.impl.XYImpl;
import com.bigun.wifioscilloscope.util.ByteUtil;

public class TcpConnection {
	public static final int LOGIN_TYPE = 101;
	public static final int SET_VIEW_TYPE = 102;
	public static final int DISCONNECT_TYPE = 103;
	public static final int SETSAMPPARA_TYPE = 104;
	public static final int GENSETCONF_TYPE = 105;
	public static final int HEART_TYPE = 106;

	public static final int Y1 = 0;
	public static final int Y2 = 1;

	private Socket mSocket;
	private Boolean isConnet = true;

	private XYListener xyListener;

	private Context mContext;

	private TcpConnection() {
	}

	private static TcpConnection tcpConnection;

	public static TcpConnection getInstance() {
		if (tcpConnection == null) {
			tcpConnection = new TcpConnection();
		}
		return tcpConnection;
	}

	public void init(Context context) {
		mContext = context;
	}
	public void connectToDevice(final String ip,
			final ConnectListener connectListener) {
		xyListener = XYImpl.getInstance();
		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					mSocket = new Socket(ip, 31818);
					isConnet = true;
					connectListener.connSuccess();
				} catch (UnknownHostException e) {
					connectListener.connFail();
					e.printStackTrace();
					// Toast 显示需要出现在一个线程的消息队列中
					Looper.prepare();
					Toast.makeText(mContext,"IP: " + ip + " 识别错误:", android.widget.Toast.LENGTH_LONG).show();
					Looper.loop();
				} catch (IOException e) {
					connectListener.connFail();
					e.printStackTrace();
					// Toast 显示需要出现在一个线程的消息队列中
					Looper.prepare();
					Toast.makeText(mContext,"IO异常错误", android.widget.Toast.LENGTH_LONG).show();
					Looper.loop();
				}
			}
		}).start();
	}

	@SuppressLint("HandlerLeak")
	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case Y1:
				xyListener.updateXY(msg.obj, Y1);
				break;
			case Y2:
				xyListener.updateXY(msg.obj, Y2);
				break;
			}
		};
	};

	/**
	 * 发指令
	 * 
	 * @param order
	 * @param listener
	 * @param orderType
	 */
	public void order(final String order, final OrderListener listener,
			final int orderType) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				if (mSocket == null || mSocket.isClosed()) {
					return;
				}
				DataOutputStream writer;
				try {
					writer = new DataOutputStream(mSocket.getOutputStream());
					String data = order;
					byte[] buf = ByteUtil.stringToAsc(data);
					writer.write(buf); // 写一个UTF-8的信息
					writer.flush();
					byte[] buffer = new byte[10];
					InputStream is = mSocket.getInputStream();
					is.read(buffer);
					listener.orderSuccess(buffer, orderType);
					// System.out.println("接受成功了哈哈：" +
					// ByteUtil.bytesToHexString(buffer));
					// 登录成功后采集信息
					if (orderType == LOGIN_TYPE) {
						//实时更新波形图像
						while (isConnet) {
							byte[] b = new byte[1024 * 2];
							is.read(b);
							Message msgY1 = Message.obtain(handler);
							msgY1.what = Y1;
							msgY1.obj = b;
							msgY1.sendToTarget();
						}
						is.close();
						writer.close();
					}
					if (orderType == DISCONNECT_TYPE) {
//						System.out.println("断开连接了");
						isConnet = false;
					}
				} catch (IOException e) {
					listener.orderFail(e, orderType);
					e.printStackTrace();
				}
			}
		}).start();

	}

	public interface OrderListener {
		void orderSuccess(byte[] data, int orderType);

		void orderFail(Exception e, int orderType);
	}

	public interface ConnectListener {
		void connSuccess();

		void connFail();
	}

	public interface XYListener {
		void updateXY(Object obj, int type);
	}
}
