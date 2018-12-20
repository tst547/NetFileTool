package cn.hy.netfiletool.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.DhcpInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.*;
import android.support.v4.view.ViewPager;
import android.view.*;
import android.widget.EditText;
import cn.hy.netfiletool.R;
import cn.hy.netfiletool.application.collection.HostData;
import cn.hy.netfiletool.key.ConstStrings;
import cn.hy.netfiletool.key.Key;
import cn.hy.netfiletool.common.MyGson;
import cn.hy.netfiletool.common.WifiUtil;
import cn.hy.netfiletool.database.dao.HostDao;
import cn.hy.netfiletool.fragment.listfragment.DownLoadListFragment;
import cn.hy.netfiletool.fragment.listfragment.HostListFragment;
import cn.hy.netfiletool.fragment.listfragment.LocalFileListFragment;
import cn.hy.netfiletool.bean.HostInfo;
import cn.hy.netfiletool.net.NetWorkInfo;
import cn.hy.netfiletool.net.Scanner;
import cn.hy.netfiletool.net.pojo.BaseMsg;
import cn.hy.netfiletool.net.pojo.FileMsg;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity {

    private ViewPager viewPager;

    private MenuItem menuItem;

    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //请求创建文件权限
        ActivityCompat.requestPermissions(MainActivity.this, new String[]{android
                .Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);

        //初始化底部菜单栏点击事件(切换fragment)
        bottomNavigationView = findViewById(R.id.navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener((item)->{
            switch (item.getItemId()) {
                case R.id.navigation_cloud:
                    viewPager.setCurrentItem(0);
                    break;
                case R.id.navigation_dashboard:
                    viewPager.setCurrentItem(1);
                    break;
                case R.id.navigation_notifications:
                    viewPager.setCurrentItem(2);
                    break;
            }
            return false;
        });

        //使用ViewPager管理fragment
        viewPager = findViewById(R.id.viewpager);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }
            @Override
            public void onPageSelected(int position) {
                if (menuItem != null) {
                    menuItem.setChecked(false);
                } else {
                    bottomNavigationView.getMenu().getItem(0).setChecked(false);
                }
                menuItem = bottomNavigationView.getMenu().getItem(position);
                menuItem.setChecked(true);
            }
            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        setupViewPager(viewPager);

        WifiManager wm = (WifiManager) getApplicationContext()
                .getSystemService(Context.WIFI_SERVICE);//获取Android WIFI管理工具
        DhcpInfo di = wm.getDhcpInfo();
        //如果获取子网掩码失败 设置默认子网掩码为255.255.255.0
        NetWorkInfo netWorkInfo = new NetWorkInfo();
        netWorkInfo.setNetmask(di.netmask==0?4294967040l:di.netmask);
        netWorkInfo.setIp(WifiUtil.ip2long(WifiUtil.androidLong2ip(di.ipAddress)));
        netWorkInfo.setBroadcastAddr(netWorkInfo.getNetmask(),netWorkInfo.getIp());
        netWorkInfo.setGateWay(WifiUtil.ip2long(WifiUtil.androidLong2ip(di.gateway)));
        netWorkInfo.setScanPort(Key.ServerScannerPort);
        HostData hostData = new HostData(
                new HostDao(this,Key.DataBase,1));
        Scanner scanner = new Scanner();
        scanner.startListener(hostData,Key.ScannerRecvPort);
        appBox.setHostData(hostData);
        appBox.setScanner(scanner);
        appBox.setNetWorkInfo(netWorkInfo);
        scanner.conn(netWorkInfo);
    }

    /**
     *　ViewPagerAdapter初始化
     * @param viewPager
     */
    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new HostListFragment());
        adapter.addFragment(new LocalFileListFragment());
        adapter.addFragment(new DownLoadListFragment());
        viewPager.setAdapter(adapter);
    }

    /**
     * 打开右上角菜单
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.add_btn_menu, menu);
        return true;
    }

    /**
     * 右上角菜单 选择事件
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()) {
            case R.id.app_add://监听菜单按钮
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("输入一个地址");
                //    通过LayoutInflater来加载一个xml的布局文件作为一个View对象
                View view = LayoutInflater.from(MainActivity.this).inflate(R.layout.dialog, null);
                //    设置我们自己定义的布局文件作为弹出框的Content
                builder.setView(view);

                final EditText ipAddress = view.findViewById(R.id.ip_address);
                final EditText ipPort = view.findViewById(R.id.ip_port);

                builder.setPositiveButton(ConstStrings.Confirm, (dialog, which) -> {
                    String address = ipAddress.getText().toString().trim();
                    String port = ipPort.getText().toString().trim();
                    appBox.getHostData().putHost(address
                            .concat(ConstStrings.Colon)
                            .concat(port),new HostInfo(WifiUtil.ip2long(address),Integer.valueOf(port)));
                    dialog.cancel();
                    viewPager.setCurrentItem(0);
                    Fragment fragment = ((ViewPagerAdapter)viewPager.getAdapter()).getCurrentFragment();
                    if (fragment instanceof HostListFragment){
                        ((HostListFragment) fragment).loadViewList();
                    }
                });
                builder.setNegativeButton(ConstStrings.Cancel, (dialog, which) -> {
                    dialog.cancel();
                });
                builder.show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        //super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    File external = Environment.getExternalStorageDirectory();
                    File downloadFolder = new File(external.getPath() + Key.DownLoadPath);
                    if (!downloadFolder.exists()) {
                        downloadFolder.mkdirs();
                    }
                    break;
                }
        }
    }

    /**
     * 打开主机文件列表Activity
     * @param host
     */
    public void openHostFileList(HostInfo host) {
        getSession(host).getFileList(null
                , ((call, response) -> {
                    String res ;
                    try {
                        res = response.body().string();
                        if (null != response) {
                            BaseMsg<List<FileMsg>> baseMsg = (BaseMsg<List<FileMsg>>) MyGson.getObject(res
                                    , new TypeToken<BaseMsg<List<FileMsg>>>() {}.getType());
                            Bundle bundle = new Bundle();
                            Intent intent = new Intent(this, FileListActivity.class);
                            bundle.putSerializable(Key.FileListKey, (Serializable) baseMsg.msg);
                            bundle.putSerializable(Key.HostInfoKey, host);
                            intent.putExtras(bundle);
                            startActivity(intent);
                        }else {
                            toastMsg(ConstStrings.ConnFailed);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                        toastMsg(ConstStrings.FailedFileList);
                    }
                }));
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {

        private final List<Fragment> mFragmentList = new ArrayList<>();

        private Fragment currentFragment;

        public Fragment getCurrentFragment(){
            return this.currentFragment;
        }

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public void setPrimaryItem(ViewGroup container, int position, Object object) {
            super.setPrimaryItem(container, position, object);
            this.currentFragment = (Fragment) object;
        }


        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment) {
            mFragmentList.add(fragment);
        }

    }
}
