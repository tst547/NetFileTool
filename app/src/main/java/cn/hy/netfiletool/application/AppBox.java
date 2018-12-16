package cn.hy.netfiletool.application;

import android.app.Application;
import android.net.DhcpInfo;
import android.net.wifi.WifiManager;
import android.os.Environment;
import cn.hy.netfiletool.application.collection.HostData;
import cn.hy.netfiletool.key.Key;
import cn.hy.netfiletool.common.WifiUtil;
import cn.hy.netfiletool.bean.HostInfo;
import cn.hy.netfiletool.net.Scanner;
import cn.hy.netfiletool.net.Session;
import cn.hy.netfiletool.net.download.DownLoadMsg;
import cn.hy.netfiletool.net.NetWorkInfo;
import cn.hy.netfiletool.net.download.ProgressTask;
import cn.hy.netfiletool.net.pojo.BaseMsg;
import cn.hy.netfiletool.net.pojo.FileMsg;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * 全局类 类似j2ee中的 Application的概念
 *
 * @author temp547
 */
public class AppBox extends Application {

    private List<ProgressTask> taskList = new ArrayList<>();//进度条刷新任务列表

    private List<DownLoadMsg> downloadMsgs = new ArrayList<>();//下载信息列表

    private Scanner scanner;

    private NetWorkInfo netWorkInfo;//网路配置信息

    private HostData hostData;

    public List<DownLoadMsg> getDownloadMsgs() {
        return downloadMsgs;
    }

    public Session getSession(HostInfo hostInfo) {
        return Session.create(hostInfo);
    }

    public void setHostData(HostData hostData) {
        this.hostData = hostData;
    }

    public HostData getHostData() {
        return hostData;
    }

    public Scanner getScanner() {
        return scanner;
    }

    public void setScanner(Scanner scanner) {
        this.scanner = scanner;
    }

    public NetWorkInfo getNetWorkInfo() {
        return netWorkInfo;
    }

    public void setNetWorkInfo(NetWorkInfo netWorkInfo) {
        this.netWorkInfo = netWorkInfo;
    }

    public List<ProgressTask> getTaskList() {
        return taskList;
    }

    public void setTaskList(List<ProgressTask> taskList) {
        this.taskList = taskList;
    }
}
