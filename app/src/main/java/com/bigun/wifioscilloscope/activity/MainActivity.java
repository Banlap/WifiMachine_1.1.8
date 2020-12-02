package com.bigun.wifioscilloscope.activity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.bigun.wifioscilloscope.BigunApp;
import com.bigun.wifioscilloscope.R;
import com.bigun.wifioscilloscope.impl.XYImpl;
import com.bigun.wifioscilloscope.impl.XYImpl.CoordinateListener;
import com.bigun.wifioscilloscope.order.TcpOrder;
import com.bigun.wifioscilloscope.order.TcpOrder.RequestListener;
import com.bigun.wifioscilloscope.util.ByteUtil;
import com.bigun.wifioscilloscope.util.MainHelper;
import com.bigun.wifioscilloscope.util.NetUtil;
import com.bigun.wifioscilloscope.util.ViewUpdateUtils;
import com.bigun.wifioscilloscope.util.WidgetUtils;
import com.bigun.wifioscilloscope.view.CurveBgView;
import com.bigun.wifioscilloscope.view.TView;
import com.bigun.wifioscilloscope.view.XcursorView;
import com.bigun.wifioscilloscope.view.Y1View;
import com.bigun.wifioscilloscope.view.Y2View;
import com.bigun.wifioscilloscope.view.YcursorView;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.InputType;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
//��Ҫ�л���ˮƽ���ߺʹ�ֱ���߹��ܣ��α�������ܣ��������������ֱ���ߣ��Զ������������ֱ����֮��ĸ������Զ�����õ�ʱ���������������ˮƽ���ߣ�������Զ������������֮��ĵ�ѹ�
//�ڽ���˵���ѡ��ˮƽ��ֱ�α꣬�����һ���α꣨���ߣ�����Ļ�ϣ����һ�η����α�1���ٵ��һ�η����α�2�������ε����ر��αꡣͬһʱ�䲻��ͬʱ����ˮƽ�α�ʹ�ֱ�α꣬��������ˮƽ�α꣬��ֱ�α��Զ���ҽ�ֹ��
//�α��ͨ����ָ�����������»����ҵ���λ�ã�����ʾһ��11�Ű�͸�����ڵ����ε����Ͻ�������������ʾ�α�1��ֵ���α�2��ֵ���α�1-�α�2��ֵ������Ǵ�ֱ�α꣬��Ӧ����ʱ���Զ����Ƶ�ʲ���ʾ������
import android.graphics.Color;

/**
 * ��Ҫ�л���ˮƽ���ߺʹ�ֱ���߹��ܣ��α�������ܣ��������������ֱ���ߣ��Զ������������ֱ����֮��ĸ������Զ�����õ�ʱ���������������ˮƽ���ߣ�
 * ������Զ������������֮��ĵ�ѹ�
 * �ڽ���˵���ѡ��ˮƽ��ֱ�α꣬�����һ���α꣨���ߣ�����Ļ�ϣ����һ�η����α�1���ٵ��һ�η����α�2�������ε����ر��α�
 * ��ͬһʱ�䲻��ͬʱ����ˮƽ�α�ʹ�ֱ�α꣬��������ˮƽ�α꣬��ֱ�α��Զ���ҽ�ֹ��
 * �α��ͨ����ָ�����������»����ҵ���λ�ã�����ʾһ��11�Ű�͸�����ڵ����ε����Ͻ�����
 * ����������ʾ�α�1��ֵ���α�2��ֵ���α�1-�α�2��ֵ������Ǵ�ֱ�α꣬��Ӧ����ʱ���Զ����Ƶ�ʲ���ʾ������
 * 
 * @author Administrator �м� ���˳�ʱ��������x�ᱶ��Ϊ1
 */
@SuppressLint("HandlerLeak")
@ContentView(R.layout.activity_main)
public class MainActivity extends Activity implements CoordinateListener,
		OnTouchListener, RequestListener, OnCheckedChangeListener,
		OnClickListener, OnItemSelectedListener {
	@ViewInject(R.id.rootView)
	private FrameLayout mRootView;
	@ViewInject(R.id.tv_Triged)
	private TextView tvTriged;
	@ViewInject(R.id.tv_tr)
	private TextView tvTr1;
	@ViewInject(R.id.tv_tr2)
	private TextView tvTr2;
	@ViewInject(R.id.tv_bl1)
	private TextView tvBl1;
	@ViewInject(R.id.tv_bl2)
	private TextView tvBl2;
	@ViewInject(R.id.tv_bl3)
	private TextView tvBl3;
	@ViewInject(R.id.tv_bl4)
	private TextView tvBl4;
	@ViewInject(R.id.tv_bl5)
	private TextView tvBl5;
	@ViewInject(R.id.tv_bl6)
	private TextView tvBl6;
	@ViewInject(R.id.ll_main)
	private LinearLayout llMain;
	@ViewInject(R.id.fl_progress)
	private FrameLayout flProgress;
	@ViewInject(R.id.v_progress)
	private View vProgress;
	@ViewInject(R.id.view_cover)
	private View vCover;
	@ViewInject(R.id.view_cover_right)
	private View vCoverRight;
	@ViewInject(R.id.v_yLine_default)
	private View vYLine_default;
	@ViewInject(R.id.v_yLine_red)
	private View vYLine_red;
	@ViewInject(R.id.v_yLine_blue)
	private View vYLine_blue;
	@ViewInject(R.id.red_triangle)
	private ImageView vRedTriangle;
	@ViewInject(R.id.blue_triangle)
	private ImageView vBlueTriangle;


	// customView
	private CurveBgView mBgView;
	private Y1View mY1View;
	private Y2View mY2View;
	private TView mTview;

	private XcursorView mXcursorOne;
	private XcursorView mXcursorTwo;
	private YcursorView mYcursorOne;
	private YcursorView mYcursorTwo;
	// dialog
	private ImageView mIvReduce;
	private ImageView mIvAdd;
	private TextView mTvTimes;
	private TextView mTvDisConnet;
	private TextView mTvSignSet;
	private TextView mTvOut;
	private TextView mTvXcursor;
	private TextView mTvYcursor;

	//banlap: x���б���ʾ
	private ListView lvXspeed;
	private String data[] = new String[25];
	// ��͸�����ô�
	private View mViewSetting;
	// ���»�����
	private View mViewSlide;
	// ���Ͻǵ�����ͼ
	private View mViewDialog;
	// ����_Y1Y2
	private RadioGroup mGg;
	// ����_X_speed
	private View mViewXSpeed;
	private TextView mTvSpeed;
	private TextView mTvRate;
	// ����_T_������ƽ
	private View mViewT;
	private RadioGroup mViewBianyan;
	private RadioGroup mViewTongdao;
	private RadioGroup mViewMoshi;
	// ����_S_λ�Ƶ�ѹ
	private View mViewS;
	private TextView mTvS;
	// ����_V_�źŷ���������
	private View mViewV;
	private Spinner mSpWave;
	private EditText mEdRate;
	private RadioGroup mRgV;
	private Button mBtnSure;
	private Button mBtnCancel;
	private TextView mTvTips;
	private int waveType = 0;

	private TextView mTvUndetermined;
	private EditText mEdUndeterminedValue;
	private View mParentViewRate;
	private View mParentUndetermined;

	private boolean isYV = false;

	int moveW = 0;
	int moveH = 500;
	// view params
	int mainX;
	int mainY;
	int mainW;
	int mainH;
	// tcp
	TcpOrder order;
	// control params
	int YV = 0;// ������
	int YSP = -100;// ɨ���ٶ�
	int YT = -100;// ������ƽ
	int Y1S = -1;// Y1λ��
	int Y2S = -1;// Y2λ��
	int YE = -1;// 0Ϊֱ����1Ϊ����
	int T_type_yan = -1;// Ĭ��8Ϊ����ģʽ��6Ϊ���أ�7Ϊ����ͨ��
	int T_type_tongdao = -1;
	int T_type_mode = -1;
	int T_value_yan = -1;// ������ֵ
	int T_value_tongdao = -1;// ������ֵ
	int T_value_mode = -1;// ������ֵ
	int curClickViewId = 0;// ��ǰ�����view

	int[] XTimes = new int[] { 1, 5, 10, 25, 50 };
	int currentTimesIndex = 0;
	int currentTimes = 1;

	float mV = 4.0f;
	float mLine_red = 0;//Y1λ�Ƴ�ʼ��λ��
	float mLine_blue = 0;//Y2λ�Ƴ�ʼ��λ��

	int mainHalf=0;

	//private StringBuilder stringBuilder = null;

	private View[] settingViews;
	// ���Ƶ����н�ֱ��
	private boolean isShowDialog = false;
	private byte[] mMoshi;

	private int moveX =0;
	// ���һ�����ͼ����
	private float mBgLastX;
	// ��ǰ��ͼ����ʾ���һ��ĵ�������,���500 ���1024
	private float mLastPointIndex = 500;
	// ������
	private float progressLastIndex = 0;
	// �Ϸ����ٶ�
	private List<String> legalNs;
	// ��������
	private float progressS = 0;
	// y1 y2�б�
	private String[] mY1mVList = {"10mV","20mV", "50mV", "100mV", "200mV","500mV","1V","2V","5V"};

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		ViewUtils.inject(this);
		initParams();
		initViewStyle();
		initCustomView();
		initSettingWin();
		initEspecialView();
		// mRootView.setOnTouchListener(this);
		initListViewListener();
		order = new TcpOrder(this);
		XYImpl.getInstance().setListener(this);
		settingViews = new View[] { mGg, mViewXSpeed, mViewT, mViewS, mViewV };

	}

	private void initListViewListener() {
		lvXspeed.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				YSP = position*10;
				setPara();
				//Toast.makeText(MainActivity.this, "����ˣ�"+data[position], Toast.LENGTH_SHORT).show();
			}
		});
		lvXspeed.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
					case MotionEvent.ACTION_DOWN:
						break;
					case MotionEvent.ACTION_MOVE:
						//Toast.makeText(MainActivity.this, "������...", Toast.LENGTH_SHORT).show();
						resetGoneTime();
						break;
					case MotionEvent.ACTION_UP:
						break;
				}
				return false;
			}
		});
	}

	private void initParams() {
		String[] legalNsStrs = getResources().getStringArray(R.array.legal_ns);
		legalNs = new ArrayList<String>();
		for (int i = 0; i < legalNsStrs.length; i++) {
			legalNs.add(legalNsStrs[i]);
		}
	}

	private void initEspecialView() {
		// y1��y2������ͼ
		mGg = (RadioGroup) mViewSetting.findViewById(R.id.rg_jiaozhiliu);
		mGg.setOnCheckedChangeListener(this);
		// x��ɨ���ٶȵ�����ͼ
		mViewXSpeed = mViewSetting.findViewById(R.id.view_x_speed);
		mTvRate = (TextView) mViewSetting.findViewById(R.id.tv_rate);
		mTvSpeed = (TextView) mViewSetting.findViewById(R.id.tv_speed);


		lvXspeed = (ListView) mViewSlide.findViewById(R.id.lv_Xspeed);

		//banlap:�Ż�x��ɨ���ٶȵ�λ�б�
		for (int i=0; i<25; i++) {
			int nsId_Xspeed = getResources().getIdentifier("ns_" + i, "string", "com.bigun.wifioscilloscope");
			data[i] = getResources().getString(nsId_Xspeed).substring(2);
		}
		ArrayAdapter<String> XspeedAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,data);//�½�������ArrayAapeter
		lvXspeed.setAdapter(XspeedAdapter);


		// ������ƽ������ͼ
		mViewT = mViewSetting.findViewById(R.id.ll_T);
		mViewBianyan = (RadioGroup) findViewById(R.id.rg_chubianyan);
		mViewTongdao = (RadioGroup) findViewById(R.id.rg_chutongdao);
		mViewMoshi = (RadioGroup) findViewById(R.id.rg_chumoshi);

		mViewBianyan.setOnCheckedChangeListener(this);
		mViewTongdao.setOnCheckedChangeListener(this);
		mViewMoshi.setOnCheckedChangeListener(this);
		// λ�Ƶ�ѹ������ͼ
		mViewS = mViewSetting.findViewById(R.id.view_s);
		mTvS = (TextView) mViewSetting.findViewById(R.id.tv_y1);
		// �źŷ�����
		mViewV = mViewSetting.findViewById(R.id.view_signer);
		mEdRate = (EditText) mViewSetting.findViewById(R.id.et_rate);
		mSpWave = (Spinner) mViewSetting.findViewById(R.id.sp_wave_type);
		mSpWave.setOnItemSelectedListener(this);

		mBtnSure = (Button) mViewSetting.findViewById(R.id.btn_sure);
		mBtnCancel = (Button) mViewSetting.findViewById(R.id.btn_cancel);
		mRgV = (RadioGroup) mViewSetting.findViewById(R.id.rg_v);
		mRgV.getChildAt(0).setSelected(true);
		// 0 ���Ҳ� �޶���
		// 1 ���β� ռ�ձȣ�������0~100
		// 2 ���ǲ� ռ�ձȣ�������0~100
		// 3 ���ؽ��ݲ� ����������1~10
		// 4 ˫�ؽ��ݲ� ����������1~10
		// 5 ������ �޶���
		// 6 �����Ծ��β� ռ�ձȣ�������0~100
		mParentViewRate = mViewSetting.findViewById(R.id.ll_rate_parent);
		mParentUndetermined = mViewSetting
				.findViewById(R.id.ll_undetermined_parent);
		mTvUndetermined = (TextView) mViewSetting
				.findViewById(R.id.tv_undetermined);
		mEdUndeterminedValue = (EditText) mViewSetting
				.findViewById(R.id.et_undetermined);

		mRgV.setOnCheckedChangeListener(this);
		mBtnSure.setOnClickListener(this);
		mBtnCancel.setOnClickListener(this);
		mTvTips = (TextView) mViewSetting.findViewById(R.id.tv_slide_value);

		mEdUndeterminedValue.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {
				if ("".equals(s.toString()) || s.toString().endsWith("."))
					return;
				if (waveType == 1 || waveType == 2 || waveType == 6) {
					if (Float.valueOf(s.toString()) > 100) {
						s.clear();
						s.append("100");
					}
				}
				if (waveType == 3 || waveType == 4) {
					if (Integer.valueOf(s.toString()) > 10) {
						s.clear();
						s.append("10");
					} else if (Integer.valueOf(s.toString()) == 0) {
						s.clear();
						s.append("1");
					}
				}
			}
		});
	}

	@OnClick({ R.id.tv_bl1, R.id.tv_bl2, R.id.tv_bl3, R.id.tv_tr, R.id.tv_tr2,
			R.id.iv_reduce, R.id.iv_add, R.id.tv_disconnet, R.id.tv_out,
			R.id.tv_sign_set, R.id.view_signer, R.id.btn_sure, R.id.btn_cancel,
			R.id.red_triangle, R.id.blue_triangle, R.id.tv_xcursor,
			R.id.tv_ycursor })
	// @Override
	public void onClick(View v) {
		switch (v.getId()) {
		// Y1��ֱ������
		case R.id.tv_bl1:
			if (mViewSetting.isShown()) {
				mViewSetting.setVisibility(View.GONE);
				mTvTips.setText("");
				mViewSlide.setVisibility(View.GONE);
				/*ȷ��x���б�ر�*/
				lvXspeed.setVisibility(View.GONE);
				return;
			} else {
				mViewSetting.setVisibility(View.VISIBLE);
				mViewSlide.setVisibility(View.VISIBLE);
				settingViewShowController(mGg);
				curClickViewId = R.id.tv_bl1;
				/*ȷ��x���б�ر�*/
				lvXspeed.setVisibility(View.GONE);

				String y1mV = tvBl1.getText().toString().substring(4);

				for (int i=0; i< mY1mVList.length; i++) {
					if(mY1mVList[i].equals(y1mV)) {
						if(i==0){
							YV=0;
						} else {
							YV=i*10;
						}
					}
				}
				//��ʾ��ǰ��ֵ
				setPara();
				// Ĭ����ʾ�Ľ�ֱ����ʾ������һ��
				/*if (mMoshi[2] == 0) {
					((RadioButton) mGg.getChildAt(0)).setChecked(false);
					((RadioButton) mGg.getChildAt(1)).setChecked(true);
				} else {
					((RadioButton) mGg.getChildAt(0)).setChecked(true);
					((RadioButton) mGg.getChildAt(1)).setChecked(false);
				}*/

			}
			handler.sendEmptyMessageDelayed(0, 2000);
			break;
		// Y2��ֱ������
		case R.id.tv_bl2:
			if (mViewSetting.isShown()) {
				mViewSetting.setVisibility(View.GONE);
				mTvTips.setText("");
				mViewSlide.setVisibility(View.GONE);
				/*ȷ��x���б�ر�*/
				lvXspeed.setVisibility(View.GONE);
				return;
			} else {
				mViewSetting.setVisibility(View.VISIBLE);
				mViewSlide.setVisibility(View.VISIBLE);
				settingViewShowController(mGg);
				curClickViewId = R.id.tv_bl2;
				/*ȷ��x���б�ر�*/
				lvXspeed.setVisibility(View.GONE);

				String y2mV = tvBl2.getText().toString().substring(4);

				for (int i=0; i< mY1mVList.length; i++) {
					if(mY1mVList[i].equals(y2mV)) {
						if(i==0){
							YV=0;
						} else {
							YV=i*10;
						}
					}
				}
				//��ʾ��ǰ��ֵ
				setPara();
				// Ĭ����ʾ�Ľ�ֱ����ʾ������һ��
				/*if (mMoshi[0] == 0) {
					((RadioButton) mGg.getChildAt(0)).setChecked(false);
					((RadioButton) mGg.getChildAt(1)).setChecked(true);
				} else {
					((RadioButton) mGg.getChildAt(0)).setChecked(true);
					((RadioButton) mGg.getChildAt(1)).setChecked(false);
				}*/

			}
			handler.sendEmptyMessageDelayed(1, 2000);
			break;
		// Xɨ���ٶ�
		case R.id.tv_bl3:
			if (mViewSetting.isShown()) {
				mViewSetting.setVisibility(View.GONE);
				mTvTips.setText("");
				mViewSlide.setVisibility(View.GONE);
				lvXspeed.setVisibility(View.GONE);
				return;
			} else {
				mViewSetting.setVisibility(View.VISIBLE);
				mViewSlide.setVisibility(View.VISIBLE);
				lvXspeed.setVisibility(View.VISIBLE);
				settingViewShowController(mViewXSpeed);
				mTvSpeed.setText("ɨ���ٶ� " + tvBl3.getText());
				curClickViewId = R.id.tv_bl3;
				//YSP = 100/10;
				//��ʾ��ǰ��ֵ
				setPara();
			}
			handler.sendEmptyMessageDelayed(2, 2000);
			break;
		// ������ƽ
		case R.id.tv_tr:
			if (mViewSetting.isShown()) {
				mViewSetting.setVisibility(View.GONE);
				mTvTips.setText("");
				mViewSlide.setVisibility(View.GONE);
				/*ȷ��x���б�ر�*/
				lvXspeed.setVisibility(View.GONE);
				return;
			} else {
				mViewSetting.setVisibility(View.VISIBLE);
				mViewSlide.setVisibility(View.VISIBLE);
				settingViewShowController(mViewT);
				curClickViewId = R.id.tv_tr;
				/*ȷ��x���б�ر�*/
				lvXspeed.setVisibility(View.GONE);
				//��ʾ��ǰ��ֵ
				setPara();
			}
			handler.sendEmptyMessageDelayed(3, 2000);
			break;
		// ��������ǣ�����Y1λ��
		case R.id.red_triangle:
			if (mViewSetting.isShown()) {
				mViewSetting.setVisibility(View.GONE);
				mTvTips.setText("");
				mViewSlide.setVisibility(View.GONE);
				/* ���� Y1λ�� ��*/
				vYLine_red.setVisibility(View.INVISIBLE);
				vYLine_blue.setVisibility(View.INVISIBLE);
				/*ȷ��x���б�ر�*/
				lvXspeed.setVisibility(View.GONE);
				return;
			} else {
				mViewSetting.setVisibility(View.VISIBLE);
				mViewSlide.setVisibility(View.VISIBLE);
				settingViewShowController(mViewS);
				curClickViewId = R.id.red_triangle;
				mTvS.setText("Y1λ�Ƶ�ѹ");

				/* banlap: �洢Y1 Y2λ�Ƴ�ʼ��λ�ã�����ʾY1λ����*/
				/*if(mLine_red==0) {
					mLine_red = vYLine_red.getY();
				}*/
				System.out.println("getY:" + vYLine_red.getY() + "getYY:" + mainY);

				//���õ�ǰY1����������ƥ��
				//Toast.makeText(MainActivity.this, "�����θ߶ȣ�"+vRedTriangle.getHeight() + "mainY: " + mainY , Toast.LENGTH_LONG).show();
				vYLine_red.setY(vRedTriangle.getY()+(int)(vRedTriangle.getHeight()/2));
				vYLine_red.setVisibility(View.VISIBLE);
				/*ȷ��x���б�ر�*/
				lvXspeed.setVisibility(View.GONE);
				//��ʾ��ǰ��ֵ
				setPara();

			}
			handler.sendEmptyMessageDelayed(4, 2000);
			break;
		// ��������ǣ�����Y2λ��
		case R.id.blue_triangle:
			if (mViewSetting.isShown()) {
				mViewSetting.setVisibility(View.GONE);
				mViewSlide.setVisibility(View.GONE);
				mTvTips.setText("");

				/* ���� Y2λ�� ��*/
				vYLine_red.setVisibility(View.INVISIBLE);
				vYLine_blue.setVisibility(View.INVISIBLE);
				/*ȷ��x���б�ر�*/
				lvXspeed.setVisibility(View.GONE);
				return;
			} else {
				mViewSetting.setVisibility(View.VISIBLE);
				mViewSlide.setVisibility(View.VISIBLE);
				settingViewShowController(mViewS);
				curClickViewId = R.id.blue_triangle;
				mTvS.setText("Y2λ�Ƶ�ѹ");

				/* banlap: �洢Y1 Y2λ�Ƴ�ʼ��λ��*/
				/*if(mLine_blue==0) {
					mLine_blue = vYLine_blue.getY();
				}*/

				//���õ�ǰY2����������ƥ��
				vYLine_blue.setY(vBlueTriangle.getY()+(int)(vBlueTriangle.getHeight()/2));
				vYLine_blue.setVisibility(View.VISIBLE);
				/*ȷ��x���б�ر�*/
				lvXspeed.setVisibility(View.GONE);
				//��ʾ��ǰ��ֵ
				setPara();

			}
			handler.sendEmptyMessageDelayed(5, 2000);
			break;
		// ������Ͻǰ�ť����������
		case R.id.tv_tr2:
			if (mViewDialog.isShown())
				mViewDialog.setVisibility(View.GONE);
			else {
				mViewDialog.setVisibility(View.VISIBLE);
				mRootView.bringChildToFront(mViewDialog);
				/*ȷ��x���б�ر�*/
				lvXspeed.setVisibility(View.GONE);
			}
			break;
		// ����źŷ�����
		case R.id.tv_sign_set:
			if (mViewSetting.isShown()) {
				mTvTips.setText("");
				mViewSetting.setVisibility(View.GONE);
				mViewSlide.setVisibility(View.GONE);
			} else {
				mViewSetting.setVisibility(View.VISIBLE);
				//mViewSlide.setVisibility(View.VISIBLE);
				mViewSlide.setVisibility(View.GONE);
				settingViewShowController(mViewV);
				curClickViewId = R.id.tv_sign_set;

				mViewDialog.setVisibility(View.GONE);
			}
			break;
		case R.id.iv_reduce:
			if (currentTimesIndex != 0) {
				currentTimes = XTimes[currentTimesIndex - 1];
				onTimesChange();
				mTvTimes.setText(currentTimes + "");
				BigunApp.XTimes = currentTimes;
				currentTimesIndex--;
			}
			break;
		case R.id.iv_add:
			if (currentTimesIndex != 4) {
				currentTimes = XTimes[currentTimesIndex + 1];
				onTimesChange();
				mTvTimes.setText(currentTimes + "");
				BigunApp.XTimes = currentTimes;
				currentTimesIndex++;
			}
			break;
		case R.id.tv_disconnet:
			if ("�Ͽ�����".equals(mTvDisConnet.getText())) {
				mTvDisConnet.setText("���ص�¼");
				order.disconNet();
			} else {
				Intent intent = new Intent(this, LoginActivity.class);
				startActivity(intent);
				finish();
			}
			break;
		case R.id.tv_out:
			Toast.makeText(this, "����", Toast.LENGTH_LONG).show();
			finish();
			android.os.Process.killProcess(android.os.Process.myPid());
			break;
		case R.id.btn_sure:
			Float rate = Float.valueOf(mEdRate.getText().toString());// Ƶ��
			int wave = mSpWave.getFirstVisiblePosition();
			int first = wave;
			float second = mV;
			Float third = rate;
			String fourth = "";
			// float fourth = Float.valueOf(mEdUndeterminedValue.getText()
			// .toString());

			if (first == 0 || first == 5) {
				fourth = "0";
			} else if (first == 1 || first == 2 || first == 6) {
				fourth = Float.valueOf(mEdUndeterminedValue.getText()
						.toString()) + "";
			} else if (first == 3 || first == 4) {
				fourth = Integer.valueOf(mEdUndeterminedValue.getText()
						.toString()) + "";
			}

			order.setGenSetConf(first + " " + second + " " + third + " "
					+ fourth);
			mViewSetting.setVisibility(View.GONE);
			mViewSlide.setVisibility(View.GONE);
			mTvTips.setText("");
			break;
		case R.id.btn_cancel:
			mViewSetting.setVisibility(View.GONE);
			mViewSlide.setVisibility(View.GONE);
			mTvTips.setText("");
			break;
		case R.id.tv_xcursor:

			mViewYOne.setVisibility(View.GONE);
			mViewYTwo.setVisibility(View.GONE);

			if (mViewXOne.isShown() && mViewXTwo.isShown()) {
				mViewXOne.setVisibility(View.GONE);
				mViewXTwo.setVisibility(View.GONE);
				mViewCursor.setVisibility(View.GONE);
			} else if (mViewXOne.isShown()) {
				mViewXTwo.setVisibility(View.VISIBLE);
				mViewCursor.setVisibility(View.VISIBLE);
				differMv(mViewXOne.getY(), mViewXTwo.getY());
			} else {
				mViewXOne.setVisibility(View.VISIBLE);
			}
			break;
		case R.id.tv_ycursor:

			mViewXOne.setVisibility(View.GONE);
			mViewXTwo.setVisibility(View.GONE);

			if (mViewYOne.isShown() && mViewYTwo.isShown()) {
				mViewYOne.setVisibility(View.GONE);
				mViewYTwo.setVisibility(View.GONE);
				mViewCursor.setVisibility(View.GONE);
			} else if (mViewYOne.isShown()) {
				mViewYTwo.setVisibility(View.VISIBLE);
				mViewCursor.setVisibility(View.VISIBLE);
				differNs(mViewYOne.getX(), mViewYTwo.getX());
			} else {
				mViewYOne.setVisibility(View.VISIBLE);
			}
			break;
		// case R.id.rb_zhiliu:
		// break;
		}
	}

	private void onTimesChange() {
		mLastPointIndex = 500 / currentTimes;
		WidgetUtils.setWidth(vProgress, (BigunApp.ProgressW * 500 / 1024)
				/ currentTimes);
	}

	Handler handler = new Handler() {

		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0:
				mViewSetting.setVisibility(View.GONE);
				mViewSlide.setVisibility(View.GONE);
				mTvTips.setText("");
				if (YE != -1) {
					order.setSampPara("9=" + YE);
					resetParams();
				}
				order.setSampPara("1=" + YV / 10);
				break;
			case 1:
				mViewSetting.setVisibility(View.GONE);
				mViewSlide.setVisibility(View.GONE);
				mTvTips.setText("");
				if (YE != -1) {
					order.setSampPara("10=" + YE);
					resetParams();
				}
				order.setSampPara("2=" + YV / 10);
				break;
			case 2:
				mViewSetting.setVisibility(View.GONE);
				mViewSlide.setVisibility(View.GONE);
				lvXspeed.setVisibility(View.GONE);
				mTvTips.setText("");
				// order.setSampPara("10=" + YE);
				order.setSampPara("0=" + YSP / 10);
				// ÿ������ɨ���ٶȶ���ʼ����ͼ����
				order.setView(0, 500);
				mLastPointIndex = 500;
				progressLastIndex = 0;
				vProgress.setX(0);
				break;
			case 3:
				mViewSetting.setVisibility(View.GONE);
				mViewSlide.setVisibility(View.GONE);
				mTvTips.setText("");
				String params = "";
				if (T_type_yan != -1 && T_value_yan != -1) {
					params += T_type_yan + "=" + T_value_yan;
				}
				if (T_type_tongdao != -1 && T_value_tongdao != -1) {
					if (params.length() > 0) {
						params += " " + T_type_tongdao + "=" + T_value_tongdao;
					} else {
						params += T_type_tongdao + "=" + T_value_tongdao;
					}
				}
				if (T_type_mode != -1 && T_value_mode != -1) {
					if (params.length() > 0) {
						params += " " + T_type_mode + "=" + T_value_mode;
					} else {
						params += T_type_mode + "=" + T_value_mode;
					}
				}
				if (params.length() > 0) {
					order.setSampPara(params);
					resetParams();
				}
				order.setSampPara("5=" + YT);
				break;
			case 4:
				mViewSetting.setVisibility(View.GONE);
				mViewSlide.setVisibility(View.GONE);
				mTvTips.setText("");
				order.setSampPara("3=" + Y1S / 10);
				/* ���� Y1λ�� ��*/
				vYLine_red.setVisibility(View.GONE);
				break;
			case 5:
				mViewSetting.setVisibility(View.GONE);
				mViewSlide.setVisibility(View.GONE);
				mTvTips.setText("");
				order.setSampPara("4=" + Y2S / 10);
				/* ���� Y2λ�� ��*/
				vYLine_blue.setVisibility(View.GONE);
				break;
			case 10:
				tvBl5.setText((String) msg.obj);
				break;
			case 8:
				mY1View.setX(mainX);
				mY2View.setX(mainX);
				//mY1View.setX(mY1View.getX() - moveX);
				//mY2View.setX(mY2View.getX() - moveX);
				vProgress.setX((progressS / 1024f) * flProgress.getWidth());
				//moveX = 0;
				break;
			}

		};
	};

	private void resetParams() {
		YE = -1;
		T_type_yan = -1;
		T_type_tongdao = -1;
		T_type_mode = -1;
		T_value_yan = -1;
		T_value_tongdao = -1;
		T_value_mode = -1;
	}

	private float y1;
	private float y2;
	private String sensitivity;
	private String nsStr;
	private LinearLayout mViewXOne;
	private LinearLayout mViewXTwo;
	private LinearLayout mViewYOne;
	private LinearLayout mViewYTwo;
	private int lastX = 0;
	private int lastY = 0;
	private View mViewCursor;
	private TextView mTvCursorValueY1;
	private TextView mTvCursorValueY2;

	/**
	 * ��ʼ����͸����������
	 */
	private void initSettingWin() {
		// ��͸����������
		mViewSetting = LayoutInflater.from(this).inflate(R.layout.view_setting,
				null);
		mViewSetting.setVisibility(View.GONE);
		mRootView.addView(mViewSetting);
		int sW = (int) (mainW * 0.5);
		int sH = (int) (mainH * 0.5);
		WidgetUtils.setSize(mViewSetting, sW, sH);
		mViewSetting.setX((int) (mainX + BigunApp.screenPoint.x * 0.1));
		mViewSetting.setY((BigunApp.screenPoint.y - sH) / 2);
		// ���»�������
		mViewSlide = LayoutInflater.from(this).inflate(R.layout.view_slide,
				null);
		mViewSlide.setVisibility(View.GONE);
		mRootView.addView(mViewSlide);
		int sildeW = (int) (BigunApp.screenPoint.x * 0.2);
		int sildeH = (int) (mainH * 0.8);
		WidgetUtils.setSize(mViewSlide, sildeW, sildeH);
		mViewSlide.setX((int) (mainX + mainW - BigunApp.screenPoint.x * 0.3));
		mViewSlide.setY((BigunApp.screenPoint.y - sildeH) / 2);
		mViewSlide.setOnTouchListener(this);
	}

	// c
	/**
	 * ��ʼ����ͼ��ʽ
	 */
	private void initViewStyle() {
		// public
		mainW = (int) (BigunApp.screenPoint.x * 0.85);
		mainH = (int) (BigunApp.screenPoint.y * 0.85);
		mainX = (BigunApp.screenPoint.x - mainW) / 2;
		mainY = (BigunApp.screenPoint.y - mainH) / 2;
		BigunApp.MAIN_X = mainX;
		BigunApp.MAIN_H = mainH;
		int tvTY = mainY - tvTriged.getHeight();
		int magrin = mainW * 1 / 20;
		// llMain
		llMain.setX(mainX);
		llMain.setY(mainY);
		WidgetUtils.setSize(llMain, mainW, mainH);
		// tvTriged
		int tvTrigedX = mainX + magrin - 20;
		tvTriged.setX(tvTrigedX);
		tvTriged.setY(mainY / 2 - WidgetUtils.getHeight(tvTr1) / 2);
		// tvTr1
		int tvTr1X = mainX + mainW - tvTr1.getWidth() - magrin;
		tvTr1.setX(tvTr1X);
		tvTr1.setY(mainY / 2 - WidgetUtils.getHeight(tvTr1) / 2);
		// tvTr2
		tvTr2.setX(mainX + mainW + magrin);
		tvTr2.setY(mainY / 2 - WidgetUtils.getHeight(tvTr1) / 2);
		// flProgress
		int flW = mainW - (BigunApp.screenPoint.x - tvTr1X) - magrin * 2;
		WidgetUtils.setSize(flProgress, flW, mainY);
		// vProgress
		WidgetUtils.setWidth(vProgress, flW * 500 / 1024);

		//banlap�� ���� ���δ��ڵķ�Χ ����
		vProgress.setX((int)(flW/4));

		BigunApp.ProgressW = flW;
		// tvBl1
		int tvBY = mainY * 3 / 2 + mainH - WidgetUtils.getHeight(tvBl1) / 2;
		int tvBW = WidgetUtils.getHeight(tvBl1);
		int tvBMagrin = (mainW - tvBW * 6) / 6; // ���
		tvBl1.setX(tvTrigedX);
		tvBl1.setY(tvBY);
		// tvBl2
		tvBl2.setX(tvTrigedX + tvBW + tvBMagrin);
		tvBl2.setY(tvBY);
		// tvBl3
		tvBl3.setX(tvTrigedX + tvBW * 2 + tvBMagrin * 2);
		tvBl3.setY(tvBY);
		// tvBl4
		tvBl4.setX(tvTrigedX + tvBW * 3 + tvBMagrin * 3);
		tvBl4.setY(tvBY);
		// tvBl5
		tvBl5.setX(tvTrigedX + tvBW * 4 + tvBMagrin * 4);
		tvBl5.setY(tvBY);
		// tvBl6
		tvBl6.setX(tvTrigedX + tvBW * 5 + tvBMagrin * 5);
		tvBl6.setY(tvBY);
		// vCover
		WidgetUtils.setWidth(vCover, mainX);
		// vCoverRight
		WidgetUtils.setWidth(vCoverRight, mainX);
		WidgetUtils.setHeight(vCoverRight, mainH);
		vCoverRight.setX(mainW + mainX);
		vCoverRight.setY(mainY);
		// vRedTriangle
		WidgetUtils.setSize(vRedTriangle,
				(int) (BigunApp.screenPoint.x * 0.05),
				(int) (BigunApp.screenPoint.x * 0.05));
		vRedTriangle.setX((float) (mainX - BigunApp.screenPoint.x * 0.05));
		//banlap:���ú�ɫ������y��λ��
		//vRedTriangle.setY((BigunApp.screenPoint.y / 2));
		vRedTriangle.setY((int)(BigunApp.screenPoint.y * 0.5) + (int)(((mainH/2) - (int)(BigunApp.screenPoint.y * 0.5))*0.6));
		// vBlueTriangle
		WidgetUtils.setSize(vBlueTriangle,
				(int) (BigunApp.screenPoint.x * 0.05),
				(int) (BigunApp.screenPoint.x * 0.05));
		vBlueTriangle.setX((float) (mainX + mainW));
		//banlap:������ɫ������y��λ��
		//vBlueTriangle.setY(BigunApp.screenPoint.y / 2);
		vBlueTriangle.setY((int)(BigunApp.screenPoint.y * 0.5) + (int)(((mainH/2) - (int)(BigunApp.screenPoint.y * 0.5))*0.6));
		// �����ӳ�
		NetUtil.updateNetDelay(this, handler);

		//���Y1��Y2 ��ʾ��
		WidgetUtils.setSize(vYLine_red,mainW,5);
		WidgetUtils.setSize(vYLine_blue,mainW,5);

		vYLine_red.setVisibility(View.INVISIBLE);
		vYLine_blue.setVisibility(View.INVISIBLE);

		vYLine_default.setVisibility(View.INVISIBLE);

		mainHalf = (int) mainH / 2;

	}

	/**
	 * ��ʼ���Զ���view
	 */
	private void initCustomView() {
		// ����ͼ
		mBgView = new CurveBgView(this, mainW, mainH);
		// mBgView.setOnClickListener(this);
		mRootView.addView(mBgView);
		mBgView.setX(mainX);
		mBgView.setY(mainY);
		mBgView.setOnTouchListener(this);
		// Y1����
		mY1View = new Y1View(this, mainW, mainH);
		mY1View.setX(mainX - 10);
		mY1View.setY(mainY);
		mRootView.addView(mY1View);
		// Y2����
		mY2View = new Y2View(this, mainW, mainH);
		mY2View.setX(mainX - 10);
		mY2View.setY(mainY);
		mRootView.addView(mY2View);
		// �α�
		mViewXOne = new LinearLayout(this);
		mViewXTwo = new LinearLayout(this);
		mViewYOne = new LinearLayout(this);
		mViewYTwo = new LinearLayout(this);
		// mViewYOne.setBackgroundColor(Color.BLUE);
		// mViewYTwo.setBackgroundColor(Color.BLUE);

		LinearLayout.LayoutParams paramX = new LinearLayout.LayoutParams(mainW,
				10);
		LinearLayout.LayoutParams paramY = new LinearLayout.LayoutParams(10,
				mainH);
		mViewXOne.setLayoutParams(paramX);
		mViewXTwo.setLayoutParams(paramX);
		mViewYOne.setLayoutParams(paramY);
		mViewYTwo.setLayoutParams(paramY);

		mViewXOne.setX(mainX);
		mViewXTwo.setX(mainX);
		mViewXOne.setY((float) (mainY * 1.2));
		mViewXTwo.setY((float) (mainY * 1.6));
		mViewYOne.setX((float) (mainX * 1.2));
		mViewYTwo.setX((float) (mainX * 1.6));
		mViewYOne.setY(mainY);
		mViewYTwo.setY(mainY);

		mViewXOne.setGravity(Gravity.CENTER);
		mViewXTwo.setGravity(Gravity.CENTER);
		mViewYOne.setGravity(Gravity.CENTER);
		mViewYTwo.setGravity(Gravity.CENTER);

		mXcursorOne = new XcursorView(this, mainW, 5);
		mXcursorTwo = new XcursorView(this, mainW, 5);
		mYcursorOne = new YcursorView(this, 5, mainH);
		mYcursorTwo = new YcursorView(this, 5, mainH);

		mViewXOne.addView(mXcursorOne);
		mViewXTwo.addView(mXcursorTwo);
		mViewYOne.addView(mYcursorOne);
		mViewYTwo.addView(mYcursorTwo);

		mViewXOne.setVisibility(View.GONE);
		mViewXTwo.setVisibility(View.GONE);
		mViewYOne.setVisibility(View.GONE);
		mViewYTwo.setVisibility(View.GONE);

		mViewXOne.setOnTouchListener(this);
		mViewXTwo.setOnTouchListener(this);
		mViewYOne.setOnTouchListener(this);
		mViewYTwo.setOnTouchListener(this);

		mRootView.addView(mViewXOne);
		mRootView.addView(mViewXTwo);
		mRootView.addView(mViewYOne);
		mRootView.addView(mViewYTwo);
		// �α굯��
		mViewCursor = LayoutInflater.from(this).inflate(
				R.layout.view_corsor_value, null);
		mRootView.addView(mViewCursor);
		int cW = (int) (mainW * 0.5);
		int cH = (int) (mainH * 0.5);
		WidgetUtils.setSize(mViewCursor, cW, cH);
		mViewCursor.setX((int) (mainX + mainW - cW));
		mViewCursor.setY(mainY);
		mTvCursorValueY1 = (TextView) mViewCursor
				.findViewById(R.id.tv_corsor_value_Y1);
		mTvCursorValueY2 = (TextView) mViewCursor
				.findViewById(R.id.tv_corsor_value_Y2);
		mViewCursor.setVisibility(View.GONE);
		// ��ƽ
		mTview = new TView(this, mainW, mainH);
		mTview.setX(mainX);
		mTview.setY(mainY);
		mRootView.addView(mTview);
		// ����
		mViewDialog = LayoutInflater.from(this).inflate(R.layout.view_dialog,
				null);
		mViewDialog.setOnTouchListener(this);
		mRootView.addView(mViewDialog);
		mViewDialog.setVisibility(View.GONE);
		int dW = (int) (BigunApp.screenPoint.x * 0.3);
		int dH = (int) (BigunApp.screenPoint.y * 0.6);
		WidgetUtils.setSize(mViewDialog, dW, dH);
		mViewDialog.setX(tvTr2.getX() - dW);
		mViewDialog.setY(tvTr2.getY() + tvTr2.getHeight());
		mIvReduce = (ImageView) mViewDialog.findViewById(R.id.iv_reduce);
		mIvAdd = (ImageView) mViewDialog.findViewById(R.id.iv_add);
		mTvTimes = (TextView) mViewDialog.findViewById(R.id.tv_times);
		mTvDisConnet = (TextView) mViewDialog.findViewById(R.id.tv_disconnet);
		mTvSignSet = (TextView) mViewDialog.findViewById(R.id.tv_sign_set);
		mTvOut = (TextView) mViewDialog.findViewById(R.id.tv_out);
		mBtnSure = (Button) mViewDialog.findViewById(R.id.btn_sure);
		mBtnCancel = (Button) mViewDialog.findViewById(R.id.btn_cancel);
		mTvXcursor = (TextView) mViewDialog.findViewById(R.id.tv_xcursor);
		mTvYcursor = (TextView) mViewDialog.findViewById(R.id.tv_ycursor);

		mIvReduce.setOnClickListener(this);
		mIvAdd.setOnClickListener(this);
		mTvTimes.setOnClickListener(this);
		mTvDisConnet.setOnClickListener(this);
		mTvOut.setOnClickListener(this);
		mTvSignSet.setOnClickListener(this);
		mTvXcursor.setOnClickListener(this);
		mTvYcursor.setOnClickListener(this);

		WidgetUtils.setSize(mIvAdd, (int) (dW * 0.1), (int) (dW * 0.1));
		WidgetUtils.setSize(mIvReduce, (int) (dW * 0.1), (int) (dW * 0.1));

		mRootView.bringChildToFront(vCover);
		mRootView.bringChildToFront(vRedTriangle);
		mRootView.bringChildToFront(vCoverRight);
		mRootView.bringChildToFront(vBlueTriangle);
		mRootView.bringChildToFront(tvBl1);
		mRootView.bringChildToFront(tvBl2);
		mRootView.bringChildToFront(tvBl3);
	}

	@Override
	public void updateXY(Object pts, int type) {
		// x
		try {
			if (pts != null) {
				// Y1,Y2����
				byte[] b = (byte[]) pts;
				if (0x03 == b[6]) {
					tvTriged.setText("Triged");
					// ���²���ͼ
					ViewUpdateUtils.viewUpdate(mainX, mainY, mainW, mainH,
							mY1View, mY2View, pts, vRedTriangle, vBlueTriangle,
							mTview, vProgress, currentTimes, moveW, moveH);
					// Y1 Y2������
					sensitivity = Integer.toHexString((int) b[18] & 0xff);

					MainHelper.setSenSitivity(sensitivity, tvBl1, tvBl2, b[22]);
					// ��ֱ��
					mMoshi = ByteUtil.getBooleanArray(b[22]);
					// ������
					int b2 = b[12] << 8;
					int b1 = b[11];
					int rate = b1 + b2;
					// ɨ�ٵ�λ�Ͳ�����
					int ns = b[17] & 0xff;
					int nsId = getResources().getIdentifier("ns_" + ns,
							"string", "com.bigun.wifioscilloscope");
					nsStr = getResources().getString(nsId);
					if (nsStr == null) {
						return;
					}
					tvBl3.setText(nsStr);
					if (mViewSetting.isShown()) {
						int rateId = getResources().getIdentifier("rate_" + ns,
								"string", "com.bigun.wifioscilloscope");
						String rateStr = getResources().getString(rateId);
						mTvRate.setText("������:" + rateStr);
					}
					if (YSP == -100) {
						YSP = ns * 10;
					}
					// ����
					MainHelper.setElectric(b, tvBl6);
					// ������ƽ
					if (YT == -100) {
						YT = (int) b[21];
					}
					// λ��
					if (Y1S == -1) {
						Y1S = b[19] * 10;
					}

					if (Y2S == -1) {
						Y2S = b[20] * 10;
					}
					// ������
					// int p1 = b[9] & 0xff;
					// int p2 = b[10] << 8;
					// float progressCurrent = p1 + p2;
					// if (progressLastIndex != progressCurrent) {
					// vProgress.setX((progressCurrent / 1024f)
					// * flProgress.getWidth());
					// if (BigunApp.isFullPoint) {
					// mY1View.setX(mainX);
					// mY2View.setX(mainX);
					// }
					// }
				} else if (0x0d == b[6]) {
					tvTriged.setText("wait");
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.view.View.OnTouchListener#onTouch(android.view.View,
	 * android.view.MotionEvent)
	 */
	@Override
	public boolean onTouch(View view, MotionEvent event) {
		if (view == mViewSlide) {
			mViewDialog.setVisibility(View.GONE);
			resetGoneTime();
			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				y1 = event.getY();
				isYV = false;
				break;
			case MotionEvent.ACTION_MOVE:
				// y1y2��ֱ������
				y2 = event.getY();
				if (mViewSetting.isShown() && mGg.isShown()) {

					if (y1 - y2 > 0) {
						int differY = (int) (y1 - y2);
						YV = (int) (YV + differY / 100f);
						if (YV / 10 > 8) {
							YV = 80;
						}
					} else if (y2 - y1 > 0) {
						int differY = (int) (y1 - y2);
						YV = (int) (YV + differY / 100f);
						if (YV / 10 < 0) {
							YV = 0;
						}

					}
					setPara();
					System.out.println("y1: " + y1 + "   y2: " + y2 + "   and (y1-y2): " + (float)(y1-y2)/50f + " and YV: " + YV);

				}
				// x��ɨ���ٶ�
				if (mViewSetting.isShown() && mViewXSpeed.isShown()) {
					if (y1 - y2 > 0) {
						int differY = (int) (y1 - y2);
						YSP = YSP + differY / 50;
						if (YSP / 10 >= 24) {
							YSP = 240;
						}
					} else if (y2 - y1 > 0) {
						int differY = (int) (y1 - y2);
						YSP = YSP + differY / 50;
						if (YSP / 10 <= 0) {
							YSP = 0;
						}
					}
					System.out.println("y1: " + y1 + "   y2: " + y2 + "   and y1-y2: " + (y1-y2) + "    and YSP: " + YSP/10);
					setPara();
				}
				// ������ƽ
				if (mViewSetting.isShown() && mViewT.isShown()) {
					System.out.println("y1: " + y1 + "   y2: " + y2 + "   and y1-y2: " + (y1-y2) + "    and YT: " + YT);

					int differY = (int) (y1 - y2);
					YT += differY / 20;
					if (YT > 500) {
						YT = 500;
					}
					if (YT < -500) {
						YT = -500;
					}
					y1 = y2;
					setPara();
				}
				// ����λ��
				if (mViewSetting.isShown() && mViewS.isShown()) {
					//banlap�� ���·�ΧΪ -500 �� 500֮��
					if (curClickViewId == R.id.red_triangle) {
						if (y1 - y2 > 0) {
							int differY = (int) (y1 - y2);
							Y1S = Y1S + differY / 100;
							if (Y1S > 500) {
								Y1S = 500;
							}
						} else if (y2 - y1 > 0) {
							int differY = (int) (y1 - y2);
							Y1S = Y1S + differY / 100;
							if (Y1S < -500) {
								Y1S = -500;
							}
						}

						/** banlap: Y1����λ��ʱ����ʾˮƽ��*/
						//vYLine_red.setY((mainHalf - ((float)(mainHalf*0.002)*Y1S)) + mainY);
						vYLine_red.setY(BigunApp.screenPoint.y / 2 - ( (float) (Y1S/ 500f) * mainH /2));
						System.out.println("y1: " + y1 + " y2: "+ y2 + " y1-y2: "+ (y1 - y2) + " Y1S: " + Y1S + " bigunApp: " + (BigunApp.screenPoint.y / 2) + " Y1S/500f: " + ((float) (Y1S/ 500f) * mainH /2));
						//vYLine_red.setY(mLine_red - ((mLine_red - mainY)/800)*Y1S );
						//System.out.println("Y1S: " + Y1S + "���㣺" + ((mLine_red - mainY)/800)*Y1S);
						WidgetUtils.setSize(vYLine_red,mainW,5);
					}
					if (curClickViewId == R.id.blue_triangle) {
						//banlap�� ���·�ΧΪ -500 �� 500֮��
						if (y1 - y2 > 0) {
							int differY = (int) (y1 - y2);
							Y2S = Y2S + differY / 100;
							if (Y2S > 500) {
								Y2S = 500;
							}
						} else if (y2 - y1 > 0) {
							int differY = (int) (y1 - y2);
							Y2S = Y2S + differY / 100;
							if (Y2S < -500) {
								Y2S = -500;
							}
						}
						/** banlap: Y2����λ��ʱ����ʾˮƽ��*/
						//vYLine_blue.setY((mainHalf - ((float)(mainHalf*0.002)*Y2S)) + mainY);
						vYLine_blue.setY(BigunApp.screenPoint.y / 2 - ( (float) (Y2S/ 500f) * mainH /2));
						//vYLine_blue.setY(mLine_blue - ((mLine_blue - mainY)/800)*Y2S );
						//System.out.println("Y2S: " + Y2S + "mLine_blue: " + mLine_blue + "mianY: " + mainY + "���㣺" + ((mLine_blue - mainY)/800)*Y2S);
						WidgetUtils.setSize(vYLine_blue,mainW,5);
					}
					setPara();


				}
				break;
			case MotionEvent.ACTION_UP:

				if (mViewSetting.isShown() && mGg.isShown()) {
					//
					System.out.println("111");
					isYV = false;
				}
				break;
			}
			// y1 = y2;
		}
		if (view != mViewDialog) {
			if (event.getAction() == MotionEvent.ACTION_DOWN) {
				if (mViewDialog.isShown())
					mViewDialog.setVisibility(View.GONE);
			}
		}
		// ˮƽ�α�
		if (view == mViewXOne || view == mViewXTwo) {
			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				if (lastY == 0) {
					lastY = (int) event.getY();
				}
				break;
			case MotionEvent.ACTION_MOVE:
				float site = event.getRawY() - lastY;
				if (site < mainY) {
					site = mainY;
				}
				if (site > mainY + mainH - 3) {
					site = mainY + mainH - 3;
				}
				view.setY(site);
				if (mViewXOne.isShown() && mViewXTwo.isShown()) {
					if (!mViewCursor.isShown())
						mViewCursor.setVisibility(View.VISIBLE);
					differMv(mViewXOne.getY(), mViewXTwo.getY());
				}
				break;
			case MotionEvent.ACTION_UP:
				lastY = 0;
				break;
			}
		}
		// ��ֱ�α�
		if (view == mViewYOne || view == mViewYTwo) {
			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				if (lastX == 0) {
					lastX = (int) event.getX();
				}
				break;
			case MotionEvent.ACTION_MOVE:
				float site = event.getRawX() - lastX;
				if (site < mainX) {
					site = mainX;
				}
				if (site > mainX + mainW - 3) {
					site = mainX + mainW - 3;
				}
				view.setX(site);
				if (mViewYOne.isShown() && mViewYTwo.isShown()) {
					if (!mViewCursor.isShown())
						mViewCursor.setVisibility(View.VISIBLE);
					differNs(mViewYOne.getX(), mViewYTwo.getX());
				}
				break;
			case MotionEvent.ACTION_UP:
				lastX = 0;
				break;
			}
		}
		// ���һ�����ͼ
		if (view == mBgView) {
			float scale = (mainW / 500f) * currentTimes;
			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:

				mBgLastX = event.getX();
				Log.d("down: ", "" + event.getX());
				break;
			case MotionEvent.ACTION_MOVE:
				//banlap�� ��(int) (event.getX() - mBgLastX) ����Ϊ������ʹ��Ļ�����������߶�����������
				int differX = -((int) (event.getX() - mBgLastX));
				//moveX = -((int) (event.getX() - mBgLastX));
				Log.d("Move: ", "" + event.getX() + " and differX: " + differX + " and scale: " + scale + " and Last: " + (differX / scale)
						+ " and currentT: " + (500/currentTimes) );

				mLastPointIndex += differX / scale;
				if (mLastPointIndex < 500 / currentTimes) {
					mLastPointIndex = 500 / currentTimes;
				}
				if (mLastPointIndex > 1024) {
					mLastPointIndex = 1024;
				}
				mY1View.setX(mY1View.getX() - differX);
				mY2View.setX(mY2View.getX() - differX);

				mBgLastX = event.getX();
				Log.d("Move2: ", "" + mY1View.getX() + " and setX: " + (mY1View.getX() - differX));
				break;
			case MotionEvent.ACTION_UP:
				try {
					//System.out.println("205---:ACTION_UP");
					System.out.println("up: start: " + (int) (mLastPointIndex - 500 / currentTimes) + "  end: "+ mLastPointIndex);
					String ns = tvBl3.getText().toString();
					//�ж�x���Ƿ�������ʱ��
					if (legalNs.contains(ns)) {
						order.setView(
								(int) (mLastPointIndex - 500 / currentTimes),
								(int) mLastPointIndex);

						BigunApp.isRestart = true;
						BigunApp.startIndex = (int) (mLastPointIndex - 500 / currentTimes);

						moveW = (int) (mLastPointIndex - 500 / currentTimes);
						moveH = (int) mLastPointIndex;
					} else {
						// order.setView(0, 500);
					}
					progressS = mLastPointIndex - 500 / currentTimes;
					Thread t1 = new Thread() {
						@Override
						public void run() {
							super.run();
							try {
								sleep(800);
								handler.sendEmptyMessage(8);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
						}
					};
					t1.start();
					// new Handler().postDelayed(new Runnable() {
					//
					// @Override
					// public void run() {
					// if (BigunApp.isFullPoint) {
					// System.out.println("205---:Handler");
					// mY1View.setX(mainX);
					// mY2View.setX(mainX);
					// // vProgress
					// // .setX(((mLastPointIndex - 500 / currentTimes)
					// // / 1024f)
					// // * flProgress.getWidth());
					// }
					// }
					// }, 800);
				} catch (Exception e) {
					System.out.println("205---�쳣:" + e.getMessage());
				}
				break;
			}
		}
		return true;
	}

	// private boolean isRightLack = false;
	// private boolean isLeftLack = false;

	/**
	 * ������ʾ����
	 */
	private void setPara() {
		switch (curClickViewId) {
		case R.id.tv_bl1:
			MainHelper.setSenSitivity(mTvTips, YV / 10);
			// order.setSampPara("1=" + YV / 10);
			break;
		case R.id.tv_bl2:
			MainHelper.setSenSitivity(mTvTips, YV / 10);
			// order.setSampPara("2=" + YV / 10);
			break;
		case R.id.tv_bl3:
			int nsId = getResources().getIdentifier("ns_" + YSP / 10, "string",
					"com.bigun.wifioscilloscope");
			if (nsId != 0) {

				nsStr = getResources().getString(nsId);
				if (nsStr != null)
					mTvTips.setText(nsStr);
				// order.setSampPara("0=" + YSP / 10);
			}

			break;
		case R.id.tv_tr:
			mTvTips.setText("��ƽ:" + YT/10);
			BigunApp.isUpdataT = false;
			float htS = ((YT) / 500f) * mainH / 2;
			mTview.setY(BigunApp.screenPoint.y / 2 - htS);
			// order.setSampPara("5=" + YT / 10);
			break;
		case R.id.red_triangle:
			mTvTips.setText("Y1λ��:" + Y1S / 10);
			// order.setSampPara("3=" + Y1S / 10);
			break;
		case R.id.blue_triangle:
			mTvTips.setText("Y2λ��:" + Y2S / 10);
			// order.setSampPara("4=" + Y2S / 10);
			break;

		}
	}

	/**
	 * �������ص���ʱ
	 */
	public void resetGoneTime() {
		switch (curClickViewId) {
		case R.id.tv_bl1:
			handler.removeMessages(0);
			handler.sendEmptyMessageDelayed(0, 2000);
			break;
		case R.id.tv_bl2:
			handler.removeMessages(1);
			handler.sendEmptyMessageDelayed(1, 2000);
			break;
		case R.id.tv_bl3:
			handler.removeMessages(2);
			handler.sendEmptyMessageDelayed(2, 2000);
			break;
		case R.id.tv_tr:
			handler.removeMessages(3);
			handler.sendEmptyMessageDelayed(3, 2000);
			break;
		case R.id.red_triangle:
			handler.removeMessages(4);
			handler.sendEmptyMessageDelayed(4, 2000);
			break;
		case R.id.blue_triangle:
			handler.removeMessages(5);
			handler.sendEmptyMessageDelayed(5, 2000);
			break;
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.bigun.wifioscilloscope.order.TcpOrder.RequestListener#requestSuccess
	 * (java.util.Map, int)
	 */
	@Override
	public void requestSuccess(Map<String, Object> map, int orderType) {
	}

	@Override
	public void requestFail(int orderType) {

	}

	@Override
	public void onCheckedChanged(RadioGroup rg, int arg1) {
		resetGoneTime();
		switch (rg.getCheckedRadioButtonId()) {
		case R.id.rb_zhiliu:
			YE = 0;
			break;
		case R.id.rb_jiaoliu:
			YE = 1;
			break;
		case R.id.rb_up:
			T_type_yan = 6;
			T_value_yan = 0;
			break;
		case R.id.rb_down:
			T_type_yan = 6;
			T_value_yan = 1;
			break;
		case R.id.rb_ch1:
			T_type_tongdao = 7;
			T_value_tongdao = 0;
			break;
		case R.id.rb_ch2:
			T_type_tongdao = 7;
			T_value_tongdao = 1;
			break;
		case R.id.rb_aut:
			T_type_mode = 8;
			T_value_mode = 0;
			break;
		case R.id.rb_nor:
			T_type_mode = 8;
			T_value_mode = 1;
			break;
		case R.id.rb_sig:
			T_type_mode = 8;
			T_value_mode = 2;
			break;
		case R.id.rb_4v:
			mV = 4.0f;
			break;
		case R.id.rb_5v:
			mV = 5.0f;
			break;
		}
	}

	/**
	 * ��͸��������ʾ����
	 * 
	 * @param v
	 */
	private void settingViewShowController(View v) {
		for (int i = 0; i < settingViews.length; i++) {
			if (v == settingViews[i])
				v.setVisibility(View.VISIBLE);
			else
				settingViews[i].setVisibility(View.GONE);
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			// do something...
			finish();
			android.os.Process.killProcess(android.os.Process.myPid());
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	/**
	 * �����ѹ��
	 * 
	 * @param y1
	 * @param y2
	 */
	public void differMv(float y1, float y2) {
		// ����Y1��ѹ��
		String senY1 = tvBl1.getText().toString().substring(2);
		int senY1I = 0;
		String regex = "\\d*";
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(senY1);
		while (m.find()) {
			if (!"".equals(m.group()))
				senY1I = Integer.valueOf(m.group());
		}

		double differY1 = (Math.abs(y1 - y2)) * 10d / mainH;
		int differMvY1 = (int) (differY1 * senY1I);
		if (differMvY1 > 1000) {
			float fMv = differMvY1 / 1000f;
			mTvCursorValueY1.setText("Y1��ѹ�" + fMv + "V");
		} else {
			mTvCursorValueY1.setText("Y1��ѹ�" + differMvY1 + "mV");
		}
		// ����Y2��ѹ��
		String senY2 = tvBl2.getText().toString().substring(2);
		int senY2I = 0;
		String regexS = "\\d*";
		Pattern pS = Pattern.compile(regexS);
		Matcher mS = pS.matcher(senY2);
		while (mS.find()) {
			if (!"".equals(mS.group()))
				senY2I = Integer.valueOf(mS.group());
		}

		double differY2 = (Math.abs(y1 - y2)) * 10d / mainH;
		int differMvY2 = (int) (differY2 * senY2I);
		if (differMvY2 > 1000) {
			float fMv = differMvY2 / 1000f;
			mTvCursorValueY2.setText("Y2��ѹ�" + fMv + "V");
		} else {
			mTvCursorValueY2.setText("Y2��ѹ�" + differMvY2 + "mV");
		}

	}

	/**
	 * ����ʱ���
	 * 
	 * @param x1
	 * @param x2
	 */
	public void differNs(float x1, float x2) {
		String tmp = tvBl3.getText().toString().substring(2, 5);
		int base = Integer.valueOf(tmp);
		double differY = Math.abs(x1 - x2);
		int differNs = (int) ((differY / mainW) * (base * 20));
		mTvCursorValueY1.setText("ʱ��" + differNs + "nS");
	}

	@Override
	public void onItemSelected(AdapterView<?> adapterView, View view,
			int position, long arg3) {
		String selected = adapterView.getItemAtPosition(position).toString();
		if ("���Ҳ�".equals(selected)) {
			waveType = 0;
			mParentViewRate.setVisibility(View.VISIBLE);
			mParentUndetermined.setVisibility(View.GONE);
		} else if ("��������".equals(selected)) {
			waveType = 5;
			mParentUndetermined.setVisibility(View.GONE);
			mParentViewRate.setVisibility(View.GONE);
		} else {
			mParentUndetermined.setVisibility(View.VISIBLE);
			mParentViewRate.setVisibility(View.VISIBLE);
			if ("���β�".equals(selected)) {
				waveType = 1;
				mTvUndetermined.setText("ռ�ձ�(%)");
				mEdUndeterminedValue.setText("50");
			} else if ("���ǲ�".equals(selected)) {
				waveType = 2;
				mTvUndetermined.setText("ռ�ձ�(%)");
				mEdUndeterminedValue.setText("50");
			} else if ("�������β�".equals(selected)) {
				waveType = 6;
				mTvUndetermined.setText("ռ�ձ�(%)");
				mEdUndeterminedValue.setText("50");
			} else if ("���ؽ��ݲ�".equals(selected)) {
				waveType = 3;
				mTvUndetermined.setText("������");
				mEdUndeterminedValue.setText("6");
			} else if ("˫�ؽ��ݲ�".equals(selected)) {
				waveType = 4;
				mTvUndetermined.setText("������");
				mEdUndeterminedValue.setText("3");
			}
		}
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {

	}

}
