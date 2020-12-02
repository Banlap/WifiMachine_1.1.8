package com.bigun.wifioscilloscope.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bigun.wifioscilloscope.R;
import com.bigun.wifioscilloscope.bean.Device;
import com.bigun.wifioscilloscope.bean.Ip;

import java.util.List;

public class IPHistoryAdapter extends BaseAdapter {

    private int newResourceId;
    private List<Ip> mIp;
    private Context context;
    private IpItemClickListener mIpItemClickListener =null;

    public IPHistoryAdapter(Context context, List<Ip> ip) {
        this.mIp = ip;
        this.context = context;
    }

    @Override
    public int getCount() {
        return mIp.size();
    }

    @Override
    public Object getItem(int arg0) {
        return mIp.get(arg0);
    }

    @Override
    public long getItemId(int arg0) {
        return arg0;
    }


    public interface IpItemClickListener {
        //点击传递IP
        void OnIpItemClickListener(View v, String ip, int position);
        //长按删除历史IP记录
        void OnLongItemClickListener(View v, String ip, int position);

        void OnDeleteItemClickListener(View v, String ip, int position);
    }

    public void setOnIpItemClickListener(IpItemClickListener ipItemClickListener) {
        mIpItemClickListener = ipItemClickListener;
    }

    public void setnDeleteItemClickListener(IpItemClickListener ipItemClickListener) {
        mIpItemClickListener = ipItemClickListener;
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        final View view = convertView;
        convertView = LayoutInflater.from(context).inflate(R.layout.item_ip, null);
        LinearLayout lvIP = (LinearLayout) convertView.findViewById(R.id.ll_ip_item);
        TextView tvIP = (TextView) convertView.findViewById(R.id.tv_ip_history);
        TextView tvDelete = (TextView) convertView.findViewById(R.id.tv_ip_delete);

        tvIP.setText(mIp.get(position).ip);

        lvIP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(context,"你点击了第"+position+"项"+"你选择"+mIp.get(position).ip,Toast.LENGTH_SHORT).show();
                mIpItemClickListener.OnIpItemClickListener(view, mIp.get(position).ip, position);
            }
        });
        lvIP.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                //Toast.makeText(context,"你删除了第"+ position +"项记录: "+mIp.get(position).ip,Toast.LENGTH_LONG).show();
                mIpItemClickListener.OnLongItemClickListener(view, mIp.get(position).ip, position);
                return false;
            }
        });

        tvDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(context,"你删除了第"+ position +"项记录: "+mIp.get(position).ip,Toast.LENGTH_LONG).show();
                mIpItemClickListener.OnDeleteItemClickListener(view, mIp.get(position).ip, position);
            }
        });


        return convertView;
    }
}
