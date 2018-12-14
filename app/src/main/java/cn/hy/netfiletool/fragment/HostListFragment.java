package cn.hy.netfiletool.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import cn.hy.netfiletool.R;
import cn.hy.netfiletool.activity.FileListActivity;
import cn.hy.netfiletool.box.App;
import cn.hy.netfiletool.box.ConstStrings;
import cn.hy.netfiletool.box.Key;
import cn.hy.netfiletool.common.MyGson;
import cn.hy.netfiletool.common.WifiUtil;
import cn.hy.netfiletool.net.HostInfo;
import cn.hy.netfiletool.pojo.BaseMsg;
import cn.hy.netfiletool.pojo.FileMsg;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class HostListFragment extends Fragment {

    private View rootView;

    public HostListFragment(){
        super();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.file_list_fragment, container, false);
        //使用安卓自带刷新Layout并注册下拉刷新时处理事件
        SwipeRefreshLayout swipeRefreshLayout = rootView.findViewById(R.id.swiperereshlayout);
        swipeRefreshLayout.setOnRefreshListener(() ->
                new Handler().postDelayed(()->{
                    initList(App.getHostMap());
                    swipeRefreshLayout.setRefreshing(false);
                },1200));
        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        initList(App.getHostMap());
    }

    @Override
    public void onResume() {
        super.onResume();
        initList(App.getHostMap());
    }

    /**
     * 初始化列表
     *
     * @param fileMap
     */
    protected void initList(Map<String, HostInfo> fileMap) {
        ListView hlv = getActivity().findViewById(R.id.hostFileView);
        hlv.setEmptyView(getActivity().findViewById(R.id.empty_tv));
        CustomAdapter customAdapter = new CustomAdapter(fileMap);
        hlv.setAdapter(customAdapter);
        hlv.setOnItemClickListener((adapterView, view, pos, n) -> {
            //单击主机后以打开浏览该主机硬盘内的文件
            HostInfo host = (HostInfo) hlv.getItemAtPosition(pos);
            Intent intent = new Intent(getActivity(), FileListActivity.class);
            App.getSession().setHostInfo(host).getFileList(null
                    , ((call, response) -> {
                        String res ;
                        try {
                            res = response.body().string();
                            if (null != response) {
                                BaseMsg<List<FileMsg>> baseMsg = (BaseMsg<List<FileMsg>>) MyGson.getObject(res
                                        , App.fileListType);
                                Bundle bundle = new Bundle();
                                bundle.putSerializable(Key.FileListKey, (Serializable) baseMsg.msg);
                                intent.putExtras(bundle);
                                startActivity(intent);
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                            Toast.makeText(getContext(), ConstStrings.FailedFileList, Toast.LENGTH_SHORT)
                                    .show();
                        }
                    }));
        });
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
    }

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
