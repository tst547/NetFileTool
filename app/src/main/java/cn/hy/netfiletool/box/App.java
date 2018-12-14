package cn.hy.netfiletool.box;

import android.net.DhcpInfo;
import android.net.wifi.WifiManager;
import android.os.Environment;
import cn.hy.netfiletool.common.MyMath;
import cn.hy.netfiletool.common.WifiUtil;
import cn.hy.netfiletool.dao.DaoSupport;
import cn.hy.netfiletool.dao.HostDao;
import cn.hy.netfiletool.net.HostInfo;
import cn.hy.netfiletool.net.Scanner;
import cn.hy.netfiletool.net.Session;
import cn.hy.netfiletool.net.download.DownLoadMsg;
import cn.hy.netfiletool.net.NetWorkInfo;
import cn.hy.netfiletool.net.download.ProgressTask;
import cn.hy.netfiletool.pojo.BaseMsg;
import cn.hy.netfiletool.pojo.FileMsg;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 全局类 类似j2ee中的 Application的概念 作为类与类之间的交互中心，存放 共用数据
 *
 * @author hanyu
 */
public class App {

    public static boolean isHostListStarted;

    public static HostDao hostDao;

    public static List<DownLoadMsg> downloadMsgs = new ArrayList<>();//下载信息列表

    public static List<ProgressTask> taskList = new ArrayList<>();//进度条刷新任务列表

    public static String SdCardPath = Environment.getExternalStorageDirectory()
            + "/";
    public static File path;

    public static Type fileListType = new TypeToken<BaseMsg<List<FileMsg>>>() {}.getType();

    private static int ServerScannerPort = 22555; //扫描消息接收端口(广播)

    private static Map<String,HostInfo> hostMap;

    public static String DownLoadPath = "JCdownload";// 文件到手机SD卡上的指定目录

    public static NetWorkInfo netWorkInfo;//网路配置信息

    private static Scanner scanner;

    static {
        netWorkInfo = new NetWorkInfo();
        path = new File(App.SdCardPath + App.DownLoadPath);
        if (!path.exists()) {
            path.mkdirs();
        }
    }

    public static Map<String,HostInfo> getHostMap(){
        if (null==hostMap){
            hostMap = hostDao.selectAll();
        }
        return hostMap;
    }

    public static void addAddress(String key,HostInfo hostInfo){
        getHostMap().put(key,hostInfo);
        hostDao.add(hostInfo);
    }

    /**
     * 用Android带有WifiManager 来获取 当前网络信息
     * @param wm
     */
    public static void readWifiInfo(WifiManager wm) {
        // WifiInfo wifiInfo = wm.getConnectionInfo();
        DhcpInfo di = wm.getDhcpInfo();
        netWorkInfo.setNetmask(di.netmask==0?4294967040l:di.netmask);
        netWorkInfo.setIp(WifiUtil.ip2long(WifiUtil.androidLong2ip(di.ipAddress)));
        netWorkInfo.setBroadcastAddr(netWorkInfo.getNetmask(),netWorkInfo.getIp());
        netWorkInfo.setGateWay(WifiUtil.ip2long(WifiUtil.androidLong2ip(di.gateway)));
        netWorkInfo.setScanPort(App.ServerScannerPort);
        scanner = new Scanner(netWorkInfo);
        scanner.start();
    }

    public static Session getSession() {
        return Session.create();
    }

    public static String getFileMsg(FileMsg file) {
        return new StringBuffer(ConstStrings.FileName)
                .append(ConstStrings.Colon)
                .append(file.name)
                .append(ConstStrings.LineSeparator)
                .append(ConstStrings.FilePath)
                .append(ConstStrings.Colon)
                .append(file.path)
                .append(ConstStrings.LineSeparator)
                .append(ConstStrings.FileType)
                .append(ConstStrings.Colon)
                .append(file.isDir ? ConstStrings.Folder : ConstStrings.File)
                .append(ConstStrings.LineSeparator)
                .append(ConstStrings.FileSize)
                .append(ConstStrings.Colon)
                .append((file.size > 1048576 ? MyMath.divide(file.size, 1048576, ConstStrings.DivideFormat).concat(ConstStrings.FileUnits)
                        : (file.size > 1024 ? MyMath.divide(file.size, 1024, ConstStrings.DivideFormat).concat(ConstStrings.FileUnitsKb)
                        : String.valueOf(file.size).concat(ConstStrings.FileUnitsByte))))
                .toString();

    }
}
