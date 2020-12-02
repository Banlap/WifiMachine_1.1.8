package com.bigun.wifioscilloscope.impl;

import java.util.List;

import com.bigun.wifioscilloscope.tcp.TcpConnection.XYListener;

public class XYImpl implements XYListener {
	public static XYImpl xyImpl;
	private CoordinateListener listener;

	private XYImpl() {
	}

	public static XYImpl getInstance() {
		if (xyImpl == null) {
			xyImpl = new XYImpl();
		}
		return xyImpl;
	}

	public void setListener(CoordinateListener listener) {
		this.listener = listener;
	}

	@Override
	public void updateXY(Object bytes, int type) {
		if (listener != null) {
			listener.updateXY(bytes, type);
		}
	}

	public interface CoordinateListener {
		void updateXY(Object m, int type);
	}
}
