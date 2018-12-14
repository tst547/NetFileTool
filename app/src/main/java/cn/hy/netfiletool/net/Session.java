package cn.hy.netfiletool.net;


import cn.hy.netfiletool.box.Key;
import cn.hy.netfiletool.common.FileUtil;
import cn.hy.netfiletool.common.WifiUtil;
import cn.hy.netfiletool.net.download.DownLoadMsg;
import okhttp3.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 跟主机相关交互的操作
 * Created by hanyu on 2017/11/16 0016.
 */
public class Session {

    private String urlBase;

    private static Session session;

    private Session(String urlBase) {
        this.urlBase = urlBase;
    }

    public String getUrlBase(){
        return this.urlBase;
    }

    /**
     * @return
     */
    public static Session create(HostInfo hostInfo) {
        if (null == session) {
            session = new Session("http://" + WifiUtil.long2ip(hostInfo.getHostIp())+ ":" + hostInfo.getHostPort());
        }
        return session;
    }

    /**
     * 文件列表
     *
     * @param filePath
     * @return
     */
    public void getFileList(String filePath, OKHttpCallback.Netable able) {
        Map<String, Object> temp = new HashMap<>();
        if (null != filePath)
            temp.put(Key.FilePathKey, filePath);
        get(Key.PathListKey, temp, able);
    }

    /**
     * 文件下载
     * @param downLoadMsg
     * @param able
     */
    public Call fileDownLoad(DownLoadMsg downLoadMsg, OKHttpCallback.Netable able) {
            long size;
            if ((size = FileUtil.getFileSize(downLoadMsg.getFile()))!=downLoadMsg.getProgress().getOffset())
                downLoadMsg.getProgress().setOffset(size);
            OkHttpClient okHttpClient = new OkHttpClient();
            RequestBody body = new FormBody.Builder()
                    .add(Key.FilePathKey, downLoadMsg.getBaseFile().path)
                    .build();
            Request request = new Request.Builder()
                    .url(urlBase + "/fileDL")
                    .addHeader(Key.Range
                            ,String.valueOf(downLoadMsg
                                    .getProgress()
                                    .getOffset() > 0 ? downLoadMsg
                                    .getProgress()
                                    .getOffset()+"-" : 0))
                    .post(body)
                    .build();
            Call call = okHttpClient.newCall(request);
            call.enqueue(new OKHttpCallback(able));
            return call;
    }

    /**
     * 简单http get请求拼装
     *
     * @param path
     * @param params
     * @return
     */
    private void get(String path, Map<String, Object> params, OKHttpCallback.Netable able) {
        if (null != params)
            for (Map.Entry<String, Object> entry : params.entrySet()) {
                if (!path.endsWith("&") && !path.contains("&"))
                    path = path.concat("?" + entry.getKey() + "=" + entry.getValue());
                else
                    path = path.concat("&" + entry.getKey() + "=" + entry.getValue());
            }
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder()
                .url(urlBase + path)
                .build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new OKHttpCallback(able));
    }


}
