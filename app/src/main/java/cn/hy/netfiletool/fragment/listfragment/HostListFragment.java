package cn.hy.netfiletool.fragment.listfragment;

import android.app.AlertDialog;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.*;
import android.widget.*;
import cn.hy.netfiletool.R;
import cn.hy.netfiletool.activity.MainActivity;
import cn.hy.netfiletool.common.WifiUtil;
import cn.hy.netfiletool.fragment.BaseListFragment;
import cn.hy.netfiletool.bean.HostInfo;
import cn.hy.netfiletool.key.ConstStrings;

import java.util.List;


public class HostListFragment extends BaseListFragment {

    private CustomAdapter customAdapter;

    private View rootView;

    public HostListFragment(){
        super();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater,container,savedInstanceState);
        rootView = inflater.inflate(R.layout.file_list_fragment, container, false);

        //使用安卓自带刷新Layout并注册下拉刷新时处理事件
        SwipeRefreshLayout swipeRefreshLayout = rootView.findViewById(R.id.swiperereshlayout);
        swipeRefreshLayout.setOnRefreshListener(() ->
                new Handler().postDelayed(()->{
                    initList(getAppBox().getHostData().values());
                    swipeRefreshLayout.setRefreshing(false);
                },1200));
        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        initList(getAppBox().getHostData().values());
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
     * @param hosts
     */
    protected void initList(List<HostInfo> hosts) {
        //hlv.setEmptyView(getActivity().findViewById(R.id.empty_tv));
        customAdapter = new CustomAdapter(hosts);
        setListAdapter(customAdapter);
        getListView().setOnItemLongClickListener((adapterView, view, pos, n)->{
            HostInfo hostInfo = (HostInfo) getListView().getItemAtPosition(pos);
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            String[] options = {ConstStrings.Delete, ConstStrings.HostDetail};
            builder
                    .setItems(options, (dialog, which) -> {
                        if (which == 0)
                            getAppBox().getHostData().deleteHost(
                                    String.valueOf(hostInfo.getHostIp())
                                            .concat(ConstStrings.Colon)
                                            .concat(String.valueOf(hostInfo.getHostPort())));
                        if (which == 1) {

                        }
                    });
            final AlertDialog dialog = builder.create();
            Window window = dialog.getWindow();
            WindowManager.LayoutParams lp = window.getAttributes();
            lp.alpha = 0.9f;
            window.setAttributes(lp);
            dialog.show();
            return true;
        });
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
    }

        class CustomAdapter extends BaseAdapter {

        private List<HostInfo> hosts;

        protected CustomAdapter(List<HostInfo> hosts) {
            this.hosts = hosts;
        }

        @Override
        public int getCount() {
            return hosts.size();
        }

        @Override
        public Object getItem(int position) {
            return hosts.get(position);
        }

        @Override
        public long getItemId(int position) {
            return hosts.get(0).hashCode();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            HostInfo host = hosts.get(position);
            LayoutInflater inflater = getActivity().getLayoutInflater();
            View view = inflater.inflate(R.layout.file_item, null);
            TextView text = view.findViewById(R.id.item_text);
            ImageView image = view.findViewById(R.id.icon);
            text.setText(WifiUtil.long2ip(host.getHostIp())
                    .concat(ConstStrings.Colon)
                    .concat(String.valueOf(host.getHostPort())));
            image.setImageResource(R.drawable.computer);
            return view;
        }
    }

}
