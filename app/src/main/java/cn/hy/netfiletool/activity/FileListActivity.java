package cn.hy.netfiletool.activity;

import java.io.*;
import java.net.URLEncoder;
import java.util.List;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.*;
import android.widget.*;
import cn.hy.netfiletool.R;
import cn.hy.netfiletool.box.App;
import cn.hy.netfiletool.box.ConstStrings;
import cn.hy.netfiletool.box.Key;
import cn.hy.netfiletool.common.FileUtil;
import cn.hy.netfiletool.common.MyGson;
import cn.hy.netfiletool.net.HostInfo;
import cn.hy.netfiletool.net.IOStream;
import cn.hy.netfiletool.net.download.DownLoadMsg;
import cn.hy.netfiletool.net.download.Progress;
import cn.hy.netfiletool.pojo.BaseMsg;
import cn.hy.netfiletool.pojo.FileMsg;

public class FileListActivity extends BaseActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 获取上一个Activity传过来的文件列表(String类型的JSON)
        List<FileMsg> fileList = (List<FileMsg>) getIntent().getExtras().get(Key.FileListKey);
        // 加载该文件列表
        initList(fileList);
    }


    /**
     * 初始化列表
     *
     * @param fileList
     */
    protected void initList(List<FileMsg> fileList) {
        ListView hlv = findViewById(R.id.hostFileView);
        CustomAdapter customAdapter = new CustomAdapter(fileList);
        hlv.setAdapter(customAdapter);
        hlv.setOnItemClickListener((adapterView, view, pos, n) -> {
            FileMsg file = (FileMsg) hlv.getItemAtPosition(pos);
            if (file.isDir) {
                App.getSession().getFileList(file.path, ((call, response) -> {
                    BaseMsg<List<FileMsg>> baseMsg;
                    try {
                        baseMsg = (BaseMsg<List<FileMsg>>) MyGson.getObject(response.body().string()
                                , App.fileListType);
                        Intent intent = new Intent(FileListActivity.this, FileListActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable(Key.FileListKey, (Serializable) baseMsg.msg);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    } catch (IOException e) {
                        e.printStackTrace();
                        toastMsg(getBaseContext(), ConstStrings.FailedFileList);
                    }
                }));
            }
            if(FileUtil.isVideo(file.name)){
                String path = file.path;
                try {
                    path = URLEncoder.encode(path,"utf-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                String url = App.getSession().getUrlBase().concat("/video?filePath=").concat(path);
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                String type = "video/*";
                Uri uri = Uri.parse(url);
                intent.setDataAndType(uri,type);
                startActivity(intent);
            }
        });
        hlv.setOnItemLongClickListener((adapterView, view, pos, n) -> {
            FileMsg file = (FileMsg) hlv.getItemAtPosition(pos);
            AlertDialog.Builder builder = new AlertDialog.Builder(FileListActivity.this);
            String[] options = {ConstStrings.Detail, ConstStrings.DownLoad};
            builder
                    .setItems(options, (dialog, which) -> {
                        if (which == 0)
                            dialogShowMsg(ConstStrings.Detail, App.getFileMsg(file));
                        if (which == 1) {
                            Progress progress = new Progress();
                            progress.setMax(file.size);
                            DownLoadMsg downLoadMsg = new DownLoadMsg();
                            downLoadMsg.setBaseFile(file);
                            downLoadMsg.setRunFlag(true);
                            downLoadMsg.setId(System.currentTimeMillis());
                            downLoadMsg.setProgress(progress);
                            try {
                                downLoadMsg.setFile(FileUtil.createFileByBaseFile(file,App.SdCardPath + App.DownLoadPath));
                                IOStream.down(downLoadMsg);
                            } catch (IOException e) {
                                downLoadMsg.setRunFlag(false);
                            }
                            App.downloadMsgs.add(downLoadMsg);
                        }
                    })
                    .show();
            return true;
        });
    }

    class CustomAdapter extends BaseAdapter {

        private List<FileMsg> files;

        protected CustomAdapter(List<FileMsg> files) {
            this.files = files;
        }

        @Override
        public int getCount() {
            return files.size();
        }

        @Override
        public Object getItem(int position) {
            return files.get(position);
        }

        @Override
        public long getItemId(int position) {
            return files.get(0).hashCode();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            FileMsg file = files.get(position);
            LayoutInflater inflater = FileListActivity.this.getLayoutInflater();
            View view = inflater.inflate(R.layout.file_item, null);
            TextView text = view.findViewById(R.id.item_text);
            ImageView image = view.findViewById(R.id.icon);
            text.setText(file.name);
            if (file.isDir)
                image.setImageResource(R.drawable.icons_folder);
            else
                image.setImageResource(R.drawable.icons_file);
            return view;
        }
    }

}
