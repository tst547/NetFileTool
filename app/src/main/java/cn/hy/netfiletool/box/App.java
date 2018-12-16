package cn.hy.netfiletool.box;

import android.net.DhcpInfo;
import android.net.wifi.WifiManager;
import android.os.Environment;
import cn.hy.netfiletool.common.MyMath;
import cn.hy.netfiletool.common.WifiUtil;
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
import java.util.List;
import java.util.Map;

/**
 * 全局类 类似j2ee中的 Application的概念 作为类与类之间的交互中心，存放 共用数据
 *
 * @author hanyu
 */
public class App {

    public static HostDao hostDao;

    public static List<DownLoadMsg> downloadMsgs = new ArrayList<>();//下载信息列表

    public static List<ProgressTask> taskList = new ArrayList<>();//进度条刷新任务列表

    public static Type fileListType = new TypeToken<BaseMsg<List<FileMsg>>>() {}.getType();

    private static Map<String,HostInfo> hostMap;

    public static NetWorkInfo netWorkInfo;//网路配置信息

    private static Scanner scanner;

    static {
        netWorkInfo = new NetWorkInfo();
        File external = Environment.getExternalStorageDirectory();
        File downloadFolder = new File(external.getPath() + Key.DownLoadPath);
        if (!downloadFolder.exists()) {
            downloadFolder.mkdirs();
        }
    }

    public static Map<String,HostInfo> getHostMap(){
        if (null==hostMap){
            hostMap = hostDao.selectAll();
        }
        return hostMap;
    }

    /**
     * 新增地址
     * @param key
     * @param hostInfo
     */
    public static void addAddress(String key,HostInfo hostInfo){
        if(!getHostMap().containsKey(key)){
            getHostMap().put(key,hostInfo);
            hostDao.add(hostInfo);
        }
    }

    /**
     * 用Android带有WifiManager 来获取 当前网络信息
     * @param wm
     */
    public static void readWifiInfo(WifiManager wm) {
        DhcpInfo di = wm.getDhcpInfo();
        //如果获取子网掩码失败 设置默认子网掩码为255.255.255.0
        netWorkInfo.setNetmask(di.netmask==0?4294967040l:di.netmask);
        netWorkInfo.setIp(WifiUtil.ip2long(WifiUtil.androidLong2ip(di.ipAddress)));
        netWorkInfo.setBroadcastAddr(netWorkInfo.getNetmask(),netWorkInfo.getIp());
        netWorkInfo.setGateWay(WifiUtil.ip2long(WifiUtil.androidLong2ip(di.gateway)));
        netWorkInfo.setScanPort(Key.ServerScannerPort);
        scanner = new Scanner(netWorkInfo);
        scanner.start();
    }

    public static Session getSession(HostInfo hostInfo) {
        return Session.create(hostInfo);
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
