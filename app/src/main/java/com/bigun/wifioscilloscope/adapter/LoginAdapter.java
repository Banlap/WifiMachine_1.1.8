package com.bigun.wifioscilloscope.adapter;

import java.util.List;

import com.bigun.wifioscilloscope.BigunApp;
import com.bigun.wifioscilloscope.R;
import com.bigun.wifioscilloscope.bean.Device;
import com.bigun.wifioscilloscope.util.WidgetUtils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class LoginAdapter extends BaseAdapter {
	private List<Device> devices;
	private Context context;

	public LoginAdapter(Context context, List<Device> devices) {
		this.devices = devices;
		this.context = context;
	}

	@Override
	public int getCount() {
		return devices.size() + 1;
	}

	@Override
	public Object getItem(int arg0) {
		return devices.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(int position, View view, ViewGroup arg2) {
		if (view == null) {
			view = LayoutInflater.from(context).inflate(R.layout.item_device,
					null);
		}
		TextView tvNo = (TextView) view.findViewById(R.id.tv_No);
		TextView tvName = (TextView) view.findViewById(R.id.tv_Name);
		TextView tvIp = (TextView) view.findViewById(R.id.tv_Ip);
		if (position == 0) {
			tvNo.setText("ÐòºÅ");
			tvName.setText("Éè±¸Ãû³Æ");
			tvIp.setText("ip");
		} else {
			tvNo.setText(position + "");
			tvName.setText(devices.get(position - 1).name);
			tvIp.setText(devices.get(position - 1).ip);
		}
//		if (position % 2 == 0 && position != 0) {
//			tvNo.setBackgroundResource(R.drawable.xuhao_light);
//			tvName.setBackgroundResource(R.drawable.device_bg_light);
//			tvIp.setBackgroundResource(R.drawable.ip_bg_light);
//		} else {
//			tvNo.setBackgroundResource(R.drawable.xuhao_deep);
//			tvName.setBackgroundResource(R.drawable.device_bg_deep);
//			tvIp.setBackgroundResource(R.drawable.ip_bg_deep);
//		}

//		WidgetUtils.setSize(tvNo, (int) (BigunApp.screenPoint.x * 0.05),
//				(int) (BigunApp.screenPoint.y * 0.1));
//		WidgetUtils.setSize(tvName, (int) (BigunApp.screenPoint.x * 0.05),
//				(int) (BigunApp.screenPoint.y * 0.1));
//		WidgetUtils.setSize(tvIp, (int) (BigunApp.screenPoint.x * 0.05),
//				(int) (BigunApp.screenPoint.y * 0.1));

		return view;
	}
}
