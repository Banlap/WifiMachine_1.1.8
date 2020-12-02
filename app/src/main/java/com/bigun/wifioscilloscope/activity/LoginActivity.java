package com.bigun.wifioscilloscope.activity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import com.bigun.wifioscilloscope.BigunApp;
import com.bigun.wifioscilloscope.R;
import com.bigun.wifioscilloscope.adapter.IPHistoryAdapter;
import com.bigun.wifioscilloscope.adapter.LoginAdapter;
import com.bigun.wifioscilloscope.bean.Device;
import com.bigun.wifioscilloscope.bean.Ip;
import com.bigun.wifioscilloscope.listener.SearchListener;
import com.bigun.wifioscilloscope.order.SearchDevice;
import com.bigun.wifioscilloscope.order.TcpOrder;
import com.bigun.wifioscilloscope.order.TcpOrder.RequestListener;
import com.bigun.wifioscilloscope.tcp.TcpConnection;
import com.bigun.wifioscilloscope.tcp.TcpConnection.ConnectListener;
import com.bigun.wifioscilloscope.util.WidgetUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.app.Activity;
import android.content.Intent;

@ContentView(R.layout.activity_login)
public class LoginActivity extends Activity implements SearchListener,
		OnItemClickListener, RequestListener, ConnectListener {
	// view
	@ViewInject(R.id.iv_bg_history)
	private ImageView mIvBgHistory;

	@ViewInject(R.id.iv_bg_list)
	private ImageView mIvBgList;

	@ViewInject(R.id.iv_bg_bottom)
	private ImageView mIvBgBottom;

	@ViewInject(R.id.listView)
	private ListView mListView;

	@ViewInject(R.id.btn_Search)
	private Button mBtnSearch;

	@ViewInject(R.id.btn_Login)
	private Button mBtnLogin;

	@ViewInject(R.id.iv_plane)
	private ImageView mIvPlane;


	@ViewInject(R.id.input_ip)
	private EditText mInputIp;

	@ViewInject(R.id.lv_ip_history)
	private ListView mIPListView;

	@ViewInject(R.id.tv_edit_ip_title)
	private TextView mEditIPTitle;

	@ViewInject(R.id.tv_ip_title)
	private TextView mIPTitle;

	@ViewInject(R.id.tv_user_agreement)
	private TextView mUserAgreement;

	private TextView tvIpDelete;

	// params
	private List<Device> devices = new ArrayList<Device>();
	private LoginAdapter mAdapter;
	private List<Ip> ipHistory = new ArrayList<Ip>();
	private IPHistoryAdapter mIPAdapter;
	private String loginIp;
	private TcpOrder order;

	private Timer mTimer;
	private Timer mTimerSearch;

	private Boolean isSearchIng = false;
	private int zero = 30;

	private SearchDevice searchDevice = null;

	//初始化 SharedPreferences数据存储方式
	SharedPreferences mIPRecord;
	private ArrayList<String> arr=null;
	private boolean mIsRecordIP = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		ViewUtils.inject(this);
		//banlap: 获取历史IP存储记录
		mIPRecord = this.getSharedPreferences("ipRecords", Context.MODE_PRIVATE);

		initViewStyle();
		dealWithView();
		isInputIP();
		searchDevice = new SearchDevice(this, this, zero);

	}


	/**
	 * 初始化视图样式
	 */
	private void initViewStyle() {

		//banlap: 优化登录界面显示
		// ivbgHistory
		int bgWidth = (int) (BigunApp.screenPoint.x * 0.45);
		int bgHeight = (int) (BigunApp.screenPoint.y * 0.9);
		WidgetUtils.setSize(mIvBgHistory, bgWidth, bgHeight);
		//Toast.makeText(LoginActivity.this, "w/h: "+ BigunApp.screenPoint.x + "/" + BigunApp.screenPoint.y, Toast.LENGTH_LONG ).show();
		mIvBgHistory.setX((float) (BigunApp.screenPoint.x * 0.1) / 3);
		mIvBgHistory.setY((float) (-bgHeight*0.1));

		// ivbgList
		WidgetUtils.setSize(mIvBgList, bgWidth, bgHeight);
		mIvBgList.setX((float) (BigunApp.screenPoint.x * 0.52));
		mIvBgList.setY((float) (-bgHeight*0.1));

		//历史IP记录框
		mIPTitle.setX((float) (BigunApp.screenPoint.x * 0.1) / 2);
		mIPTitle.setY((float) (BigunApp.screenPoint.y * 0.08));
		int historyWidth = (int) (BigunApp.screenPoint.x * 0.45);
		int historyHeight = (int) (BigunApp.screenPoint.y * 0.8);
		WidgetUtils.setSize(mIPListView, historyWidth, historyHeight);
		mIPListView.setX((float) (BigunApp.screenPoint.x * 0.1) / 3);
		mIPListView.setY((float) (BigunApp.screenPoint.y * 0.15)) ;

		// listview
		int listWidth = (int) (BigunApp.screenPoint.x * 0.42);
		int listHeight = (int) (BigunApp.screenPoint.y * 0.37);
		WidgetUtils.setSize(mListView, listWidth, listHeight);
		mListView.setX((float) (BigunApp.screenPoint.x * 0.53));
		mListView.setY((float) (BigunApp.screenPoint.y * 0.08));

		//输入框标题栏
		mEditIPTitle.setX((float) (BigunApp.screenPoint.x * 0.55));
		mEditIPTitle.setY((float) (BigunApp.screenPoint.y * 0.45));
		// editIp
		int editWidth = (int) (BigunApp.screenPoint.x * 0.4);
		int editHeight = (int) (BigunApp.screenPoint.y * 0.11);
		WidgetUtils.setSize(mInputIp, editWidth, editHeight);
		mInputIp.setX((float) (BigunApp.screenPoint.x * 0.53));
		mInputIp.setY((float) (BigunApp.screenPoint.y * 0.5));

		// btnsearch
		int btnWidth = (int) (BigunApp.screenPoint.x * 0.18);
		int btnHeight = (int) (BigunApp.screenPoint.y * 0.1);
		WidgetUtils.setSize(mBtnSearch, btnWidth, btnHeight);
		mBtnSearch.setX((float) (BigunApp.screenPoint.x * 0.54) +10);
		mBtnSearch.setY((float) (BigunApp.screenPoint.y * 0.65));

		// btnlogin
		WidgetUtils.setSize(mBtnLogin, btnWidth, btnHeight);
		mBtnLogin.setX((float) (BigunApp.screenPoint.x * 0.76));
		mBtnLogin.setY((float) (BigunApp.screenPoint.y * 0.65));

		mUserAgreement.setX((float) (BigunApp.screenPoint.x * 0.8));
		mUserAgreement.setY((float) (BigunApp.screenPoint.y * 0.85));

		//bg bottom
		int bottomWidth = (int) (BigunApp.screenPoint.x * 0.45);
		int bottomHeight = (int) (BigunApp.screenPoint.y * 0.2);
		WidgetUtils.setSize(mIvBgBottom, bottomWidth, bottomHeight);
		mIvBgBottom.setX((float) (BigunApp.screenPoint.x * 0.52));
		mIvBgBottom.setY((float) (-bgHeight*0.1));

	}

	private void dealWithView() {
		order = new TcpOrder(this);
		setBtnLoginAble(false);
		mAdapter = new LoginAdapter(this, devices);
		mListView.setAdapter(mAdapter);
		mListView.setOnItemClickListener(this);

		//banlap: 历史记录IP地址框监听
		mIPAdapter = new IPHistoryAdapter(this, ipHistory);
		mIPListView.setAdapter(mIPAdapter);
		mIPAdapter.setOnIpItemClickListener(new IPHistoryAdapter.IpItemClickListener() {

			//点击使用某个IP地址
			@Override
			public void OnIpItemClickListener(View v, String ip, int position) {
				//将ip地址传到输入框里
				mInputIp.setText(ip);
				//再次判断输入框
				String inputInfo = mInputIp.getText().toString();
				loginIp = inputInfo;
				if("".equals(inputInfo)) {
					setBtnLoginAble(false);
				} else {
					setBtnLoginAble(true);
				}
			}

			//长按删除某个IP地址
			@Override
			public void OnLongItemClickListener(View v, String ip, int position) {
				System.out.println("已经长按删除: " + position + "的ip地址信息");
				mInputIp.setText("");
				ipHistory.remove(position);
				arr.remove(position);
				//使用轻量级的数据存储方式
				updateIpList();
				mIPAdapter.notifyDataSetChanged();
			}

			//点击‘删除’字 删除某个IP地址
			@Override
			public void OnDeleteItemClickListener(View v, String ip, int position) {
				System.out.println("已经删除: " + position + "的ip地址信息");
				mInputIp.setText("");
				ipHistory.remove(position);
				arr.remove(position);
				//使用轻量级的数据存储方式
				updateIpList();
				mIPAdapter.notifyDataSetChanged();
			}

		});


		//临时存储IP地址记录
		String ipListStr = mIPRecord.getString("ipList","");
		if (ipListStr.equals("")) {
			arr = new ArrayList<String>();
		} else {
			arr = new ArrayList<String>();
			String[] split = ipListStr.split(",");
			for (int i = 0; i < split.length; i++) {
				Ip ip1 = new Ip(split[i]);
				arr.add(split[i]);
				ipHistory.add(ip1);
			}
		}


	}

	/**
	 * banlap: 各项按钮点击事件
	 */
	@OnClick({ R.id.btn_Login, R.id.btn_Search })
	public void LoginActivityOnClick(View v) {
		switch (v.getId()) {

		//查找
		case R.id.btn_Search:
			if (isSearchIng) {
				Log.d("Test by ab:", "no search");
				mBtnSearch.setBackgroundResource(R.drawable.login_btn_purple);
				mBtnSearch.setText("搜索");
				isSearchIng = false;
				mTimerSearch.cancel();
				zero = 30;
				searchDevice.stopSearch(isSearchIng);

			} else {
				Log.d("Test by ab:", "searching...");
				mBtnSearch.setBackgroundResource(R.drawable.login_btn_disable2);
				mBtnSearch.setText("停止  " + zero + "S");
				isSearchIng = true;

				searchDevice.getDeviceByUDP();
				mTimerSearch = new Timer();
				mTimerSearch.schedule(new SearchTask(), 0, 1000);
			}

			break;
		//登录
		case R.id.btn_Login:
			//判断是否重复IP地址，是则不保存到历史记录中
			for (int i=0; i<arr.size(); i++) {
				if(arr.get(i).toString().equals(loginIp)) {
					mIsRecordIP = true;
				}
			}
			if(!mIsRecordIP || arr.size()==0 ){
				arr.add(loginIp);
				updateIpList();
			}

			TcpConnection tcpConnection = TcpConnection.getInstance();
			tcpConnection.init(this);
			tcpConnection.connectToDevice(loginIp, this);
			break;

		//隐私协议
		case R.id.tv_user_agreement:

			Intent intent = new Intent(LoginActivity.this, AgreementActivity.class);
			startActivity(intent);
			break;

		}
	}

	/**
	 * banlap: SharedPreferences存储方式 存储历史IP记录
	 */
	public void updateIpList(){
		SharedPreferences.Editor editor = mIPRecord.edit();
		int arrSize = arr.size();
		String[] str = new String[arrSize];
		for(int i=0;i<arr.size();i++){
			str[i]=(String)arr.get(i);
		}
		StringBuffer strb = new StringBuffer();
		for (String s : str) {
			strb.append(s).append(",");
		}
		editor.putString("ipList", strb.toString());
		editor.apply();
	}

	/**
	 * banlap: 判断是否手动输入IP
	 */
	private void isInputIP() {
		mInputIp.setOnEditorActionListener(new TextView.OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				String inputInfo = mInputIp.getText().toString();
				loginIp = inputInfo;
				if("".equals(inputInfo)) {
					setBtnLoginAble(false);
				} else {
					setBtnLoginAble(true);
				}
				return false;
			}
		});

	}

	/**
	 * 是否允许点击登录
	 */
	private void setBtnLoginAble(Boolean b) {
		mBtnLogin.setClickable(b);
		if (b)
			mBtnLogin.setBackgroundResource(R.drawable.login_btn_yellow);
		else
			mBtnLogin.setBackgroundResource(R.drawable.login_btn_disable2);
	}

	@Override
	public void searchSuccess(Device device) {
		devices.add(device);
		mAdapter.notifyDataSetChanged();
	}

	@Override
	public void searchFail(Exception e) {

	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View view, int position,
			long arg3) {
		int realPos = position - 1;
		if (realPos < 0) {
			mBtnLogin.setClickable(false);
			return;
		}
		setBtnLoginAble(true);
		loginIp = devices.get(realPos).ip;
	}

	@Override
	public void requestSuccess(Map<String, Object> map, int orderType) {
		if (orderType == TcpConnection.LOGIN_TYPE) {
			Toast.makeText(this, "登录成功", Toast.LENGTH_LONG).show();
			// 每15S发送一次心跳包
			if (mTimer == null) {
				mTimer = new Timer();
				mTimer.schedule(task, 0, 15000);
			}
			Intent intent = new Intent(this, MainActivity.class);
			startActivity(intent);
			finish();
		}

	}

	@Override
	public void requestFail(int orderType) {
		if (orderType == TcpConnection.LOGIN_TYPE)
			Toast.makeText(this, "登录失败", Toast.LENGTH_LONG).show();

	}

	@Override
	public void connSuccess() {
		order.login();
	}

	@Override
	public void connFail() {

	}

	TimerTask task = new TimerTask() {

		@Override
		public void run() {
			order.sendHeart();
		}
	};

	public class SearchTask extends TimerTask {

		@Override
		public void run() {
			zero--;
			if (zero == 0) {
				handler.sendEmptyMessage(0);
			} else {
				handler.sendEmptyMessage(1);
			}
		}

	}

	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0:
				mBtnSearch.setBackgroundResource(R.drawable.login_btn_purple);
				mBtnSearch.setText("搜索");
				isSearchIng = false;
				mTimerSearch.cancel();
				zero = 30;
				break;
			case 1:
				mBtnSearch.setBackgroundResource(R.drawable.login_btn_disable2);
				mBtnSearch.setText("停止  " + zero + "S");
				isSearchIng = true;
				break;
			}
		};
	};


}
