package cn.hy.netfiletool.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.*;
import android.support.v4.view.ViewPager;
import android.view.*;
import android.widget.EditText;
import cn.hy.netfiletool.R;
import cn.hy.netfiletool.box.App;
import cn.hy.netfiletool.box.ConstStrings;
import cn.hy.netfiletool.box.Key;
import cn.hy.netfiletool.common.MyGson;
import cn.hy.netfiletool.common.WifiUtil;
import cn.hy.netfiletool.dao.HostDao;
import cn.hy.netfiletool.fragment.DownLoadListFragment;
import cn.hy.netfiletool.fragment.HostListFragment;
import cn.hy.netfiletool.fragment.LocalFileListFragment;
import cn.hy.netfiletool.net.HostInfo;
import cn.hy.netfiletool.pojo.BaseMsg;
import cn.hy.netfiletool.pojo.FileMsg;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity {

    private HostDao hostDao;

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

        hostDao = new HostDao(this,Key.DataBase,1);
        App.hostDao = hostDao;
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

    @Override
    protected void onStart() {
        super.onStart();
        WifiManager wm = (WifiManager) getApplicationContext()
                .getSystemService(Context.WIFI_SERVICE);//获取Android WIFI管理工具
        App.readWifiInfo(wm);//根据WIFI读取网络信息
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
                    App.addAddress(address,new HostInfo(WifiUtil.ip2long(address),Integer.valueOf(port)));
                    dialog.cancel();
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
                    //创建文件夹
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
                                    , App.fileListType);
                            Bundle bundle = new Bundle();
                            Intent intent = new Intent(this, FileListActivity.class);
                            bundle.putSerializable(Key.FileListKey, (Serializable) baseMsg.msg);
                            bundle.putSerializable(Key.HostInfoKey, host);
                            intent.putExtras(bundle);
                            startActivity(intent);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                        toastMsg(ConstStrings.FailedFileList);
                    }
                }));
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {

        private final List<Fragment> mFragmentList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
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
