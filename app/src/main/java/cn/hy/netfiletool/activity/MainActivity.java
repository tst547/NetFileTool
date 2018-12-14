package cn.hy.netfiletool.activity;

import android.content.Context;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import cn.hy.netfiletool.R;
import cn.hy.netfiletool.box.App;
import cn.hy.netfiletool.fragment.DownLoadListFragment;
import cn.hy.netfiletool.fragment.HostListFragment;
import cn.hy.netfiletool.fragment.LocalFileListFragment;

public class MainActivity extends BaseActivity {

    private HostListFragment hostListFragment;

    private LocalFileListFragment localFileListFragment;

    private DownLoadListFragment downLoadListFragment;

    private Fragment currentFragment;

    /**
     * 根据底部菜单栏单击事件切换fragment
     */
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = item -> {
                switch (item.getItemId()) {
                    case R.id.navigation_cloud:
                        switchFragment(R.id.content,hostListFragment);
                        return true;
                    case R.id.navigation_dashboard:
                        switchFragment(R.id.content,localFileListFragment);
                        return true;
                    case R.id.navigation_notifications:
                        switchFragment(R.id.content,downLoadListFragment);
                        return true;
                }
                return false;
            };

    /**
     * 切换fragment
     * @param res
     * @param targetFragment
     */
    private void switchFragment(@IdRes int res,Fragment targetFragment) {
        FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction();

        //该切换方式仅仅是隐藏和显示fragment 并没有销毁及新建
        //但需要保持参数的fragment是同一个对象（没有再次构造新对象）
        if (!targetFragment.isAdded()) {
            if(null!=currentFragment)
                transaction.hide(currentFragment);
            transaction.add(res, targetFragment).commit();
        } else {
            transaction.hide(currentFragment).show(targetFragment).commit();
        }
        currentFragment = targetFragment;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //初始化底部菜单栏
        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    @Override
    protected void onStart() {
        super.onStart();
        hostListFragment = new HostListFragment();
        localFileListFragment = new LocalFileListFragment();
        downLoadListFragment = new DownLoadListFragment();
        WifiManager wm = (WifiManager) getApplicationContext()
                .getSystemService(Context.WIFI_SERVICE);//获取Android WIFI管理工具
        App.readWifiInfo(wm);//根据WIFI读取网络信息
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.add_btn_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()) {
            case R.id.action_cart://监听菜单按钮

                break;
        }
        return super.onOptionsItemSelected(item);
    }

}
