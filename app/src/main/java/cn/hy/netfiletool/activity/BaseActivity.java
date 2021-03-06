package cn.hy.netfiletool.activity;


import android.app.AlertDialog.Builder;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import cn.hy.netfiletool.application.AppBox;
import cn.hy.netfiletool.bean.HostInfo;
import cn.hy.netfiletool.net.Session;

public abstract class BaseActivity extends AppCompatActivity {

    protected Intent intent;//获取Activity传递过来的数据

    protected AppBox appBox;




    /**
     * 获取当前会话
     * @return
     */
    protected Session getSession(HostInfo hostInfo){
        return appBox.getSession(hostInfo);
    }

    /**
     * 将当前Activity添加到App类中
     * 该操作跟onRestart方法中不同
     * 仅当Activity创建时才需要添加
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        appBox = (AppBox) getApplication();
        intent = getIntent();
    }

    /**
     * 当前Activity销毁时
     * 将App中对该Activity的引用
     * 销毁
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    /**
     * 上层Activity移除
     * 该Activity返回栈顶时
     * 类App中设置该Activity为
     * 最前端Activity
     */
    @Override
    protected void onRestart() {
        super.onRestart();
    }

    /**
     * 移除手机App头部的标题栏及顶部的状态栏
     */
    public void removeTitle() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        getWindow().setStatusBarColor(Color.TRANSPARENT);
    }


    /**
     * 加载一个信息对话框,显示给定的标题及内容
     *
     * @param head
     * @param msg
     */
    protected void dialogShowMsg(String head, String msg) {
        Builder builder = new Builder(BaseActivity.this);
        builder.setMessage(msg);
        builder.setTitle(head);
        builder.setPositiveButton("确定", (dialog, which) -> dialog.cancel());
        builder.create().show();
    }

    /**
     * 加载一条信息提示
     *
     * @param msg
     */
    protected void toastMsg(String msg) {
        Toast.makeText(getBaseContext(), msg, Toast.LENGTH_SHORT)
                .show();
    }

}

