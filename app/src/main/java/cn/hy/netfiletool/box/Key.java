package cn.hy.netfiletool.box;

/**
 * Created by hanyu on 2017/11/16 0016.
 */
public class Key {

    public static final String DownLoadPath = "JCdownload";// 文件到手机SD卡上的指定目录

    public static final String HostInfoKey = "hostInfo";
    public static final String FileListKey = "fileList";
    public static final String FilePathKey = "filePath";
    public static final String Range = "Range";
    public static final String DataBase = "net_file_db";


    public static final int FindKey = 0x01;
    public static final int ScanKey = 0x02;
    public static final int MB = 1048576;
    public static final int ProRefresh = 50;
    public final static int MSG_SUCCESS = 200;

    public static final int ServerScannerPort = 22555; //扫描消息接收端口(广播)



}
