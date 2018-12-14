package cn.hy.netfiletool.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import cn.hy.netfiletool.R;
import cn.hy.netfiletool.activity.FileListActivity;
import cn.hy.netfiletool.box.App;
import cn.hy.netfiletool.box.ConstStrings;
import cn.hy.netfiletool.box.Key;
import cn.hy.netfiletool.common.FileUtil;
import cn.hy.netfiletool.common.MyGson;
import cn.hy.netfiletool.common.RefreshableView;
import cn.hy.netfiletool.common.WifiUtil;
import cn.hy.netfiletool.net.HostInfo;
import cn.hy.netfiletool.pojo.BaseMsg;
import cn.hy.netfiletool.pojo.FileMsg;

import java.io.IOException;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class HostListFragment extends Fragment {

    private RefreshableView refreshableView;

    public HostListFragment(){
        super();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.file_list_fragment, container, false);

        ViewGroup parent = (ViewGroup) rootView.getParent();
        if (parent != null) {
            parent.removeView(rootView);
        }
        //下拉刷新事件注册 将在下拉刷新时触发该事件
        refreshableView = rootView.findViewById(R.id.refreshable_view);
        refreshableView.setOnRefreshListener(() -> {
            try {
                new Thread(()->{
                    mHandler.obtainMessage(Key.MSG_SUCCESS, App.hostMap).sendToTarget();//发送刷新UI的消息
                }).start();
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            refreshableView.finishRefreshing();
        }, 0);

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        initList(App.hostMap);
    }

    /**
     * 初始化列表
     *
     * @param fileList
     */
    protected void initList(Map<String, HostInfo> fileList) {
        ListView hlv = getActivity().findViewById(R.id.hostFileView);
        CustomAdapter customAdapter = new CustomAdapter(fileList);
        hlv.setAdapter(customAdapter);
        hlv.setOnItemClickListener((adapterView, view, pos, n) -> {
            HostInfo file = (HostInfo) hlv.getItemAtPosition(pos);

        });
    }

    final Handler mHandler = new Handler() {
        @SuppressWarnings("unchecked")
        public void handleMessage(Message msg) {// 此方法在ui线程运行
            switch (msg.what) {
                case Key.MSG_SUCCESS:
                    initList((Map<String, HostInfo>) msg.obj);//重新加载一次列表
                    break;
                default:
                    break;
            }
        }
    };

    class CustomAdapter extends BaseAdapter {

        private List<HostInfo> files;

        protected CustomAdapter(Map<String,HostInfo> hosts) {
            this.files = new ArrayList<>();
            for (HostInfo hostInfo:hosts.values()){
                files.add(hostInfo);
            }
        }

        @Override
        public int getCount() {
            return files.size();
        }

        @Override
        public Object getItem(int position) {
            return files.get(position);
        }

        @Override
        public long getItemId(int position) {
            return files.get(0).hashCode();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            HostInfo host = files.get(position);
            LayoutInflater inflater = getActivity().getLayoutInflater();
            View view = inflater.inflate(R.layout.file_item, null);
            TextView text = view.findViewById(R.id.item_text);
            ImageView image = view.findViewById(R.id.icon);
            text.setText(WifiUtil.long2ip(host.getHostIp()));
            image.setImageResource(R.drawable.ic_computer_black_24dp);
            return view;
        }
    }

}
