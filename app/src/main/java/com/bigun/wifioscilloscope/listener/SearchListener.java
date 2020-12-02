package com.bigun.wifioscilloscope.listener;

import com.bigun.wifioscilloscope.bean.Device;

public interface SearchListener {
	void searchSuccess(Device device);

	void searchFail(Exception e);
}
