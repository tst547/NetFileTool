package cn.hy.netfiletool.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.ListFragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import cn.hy.netfiletool.R;
import cn.hy.netfiletool.activity.MainActivity;
import cn.hy.netfiletool.box.App;
import cn.hy.netfiletool.common.WifiUtil;
import cn.hy.netfiletool.net.HostInfo;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class HostListFragment extends ListFragment {

    private CustomAdapter customAdapter;

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

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        HostInfo host = (HostInfo) customAdapter.getItem(position);

        MainActivity mainActivity = (MainActivity) getActivity();
        mainActivity.openHostFileList(host);

    }

    /**
     * 初始化列表
     *
     * @param fileMap
     */
    protected void initList(Map<String, HostInfo> fileMap) {
        //hlv.setEmptyView(getActivity().findViewById(R.id.empty_tv));
        customAdapter = new CustomAdapter(fileMap);
        setListAdapter(customAdapter);
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
            image.setImageResource(R.drawable.computer);
            return view;
        }
    }

}
