package cn.hy.netfiletool.net;

import cn.hy.netfiletool.box.App;
import cn.hy.netfiletool.box.ConstStrings;
import cn.hy.netfiletool.common.MyMath;
import cn.hy.netfiletool.net.download.DownLoadMsg;
import cn.hy.netfiletool.net.download.Progress;
import okhttp3.Call;

import java.io.*;

public class IOStream {

    public static Call down(HostInfo hostInfo, DownLoadMsg downLoadMsg){
            return App.getSession(hostInfo).fileIO(downLoadMsg, ((call, response) -> {
                Progress progress = downLoadMsg.getProgress();
                File fl = downLoadMsg.getFile();
                FileOutputStream bos = null;
                try {
                    bos = new FileOutputStream(fl);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    downLoadMsg.setRunFlag(false);
                    downLoadMsg.setMsg(ConstStrings.DownLoadFileOutPutFailed);
                }
                try {
                    long offset = downLoadMsg.getProgress().getOffset();
                    long max = downLoadMsg.getProgress().getMax();
                    InputStream is = response.body().byteStream();
                    int read;
                    byte[] b = new byte[40960];
                    while ((read = is.read(b)) != -1 && downLoadMsg.isRunFlag() && offset < max) {
                        bos.write(b, 0, read);
                        offset += Integer.valueOf(read).longValue();
                        progress.setOffset(progress.getOffset() + Integer.valueOf(read).longValue());
                        progress.setCurrent(MyMath.divideMax100(progress.getOffset(), progress.getMax()));
                        b = new byte[40960];
                    }
                    is.close();
                } catch (Exception e) {
                    e.printStackTrace();
                    downLoadMsg.setMsg(ConstStrings.DownLoadStreamFailed);
                } finally {
                    downLoadMsg.setRunFlag(false);
                    try {
                        bos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }));

    }

    public static void upload(){
        //todo 文件推送到主机
    }
}
