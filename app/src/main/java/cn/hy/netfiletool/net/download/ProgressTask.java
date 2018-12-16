package cn.hy.netfiletool.net.download;

import android.os.AsyncTask;

/**
 * 进度条刷新任务
 */
public class ProgressTask extends AsyncTask<Integer, Integer, Integer> {

    private volatile boolean run = true;

    private DownLoadMsg loadMsg;

    public ProgressTask(DownLoadMsg loadMsg){
        super();
        this.loadMsg = loadMsg;
    }

    @Override
    protected Integer doInBackground(Integer... integers) {
        return null;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {

    }

    public synchronized ProgressTask finish() {
        this.run = false;
        return this;
    }
}
