package cn.hy.netfiletool.activity;

import java.io.*;
import java.net.URLEncoder;
import java.util.List;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.*;
import android.widget.*;
import cn.hy.netfiletool.R;
import cn.hy.netfiletool.common.MyMath;
import cn.hy.netfiletool.key.ConstStrings;
import cn.hy.netfiletool.key.Key;
import cn.hy.netfiletool.common.FileUtil;
import cn.hy.netfiletool.common.MyGson;
import cn.hy.netfiletool.bean.HostInfo;
import cn.hy.netfiletool.net.IOStream;
import cn.hy.netfiletool.net.download.DownLoadMsg;
import cn.hy.netfiletool.net.download.Progress;
import cn.hy.netfiletool.net.pojo.BaseMsg;
import cn.hy.netfiletool.net.pojo.FileMsg;
import com.google.gson.reflect.TypeToken;

public class FileListActivity extends BaseActivity{

    private File externalRootFile;

    private HostInfo currentHost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.file_layout);
        // 获取上一个Activity传过来的文件列表(String类型的JSON)
        List<FileMsg> fileList = (List<FileMsg>) getIntent().getExtras().get(Key.FileListKey);
        currentHost = (HostInfo) getIntent().getExtras().get(Key.HostInfoKey);
        // 加载该文件列表
        initList(fileList);
        externalRootFile = Environment.getExternalStorageDirectory();
    }


    /**
     * 初始化列表
     *
     * @param fileList
     */
    protected void initList(List<FileMsg> fileList) {
        ListView hlv = findViewById(R.id.FileListView);
        CustomAdapter customAdapter = new CustomAdapter(fileList);
        hlv.setAdapter(customAdapter);
        hlv.setOnItemClickListener((adapterView, view, pos, n) -> {
            FileMsg file = (FileMsg) hlv.getItemAtPosition(pos);
            if (file.isDir) {
                getSession(currentHost).getFileList(file.path, ((call, response) -> {
                    BaseMsg<List<FileMsg>> baseMsg;
                    try {
                        baseMsg = (BaseMsg<List<FileMsg>>) MyGson.getObject(response.body().string()
                                , new TypeToken<BaseMsg<List<FileMsg>>>() {}.getType());
                        Intent intent = new Intent(FileListActivity.this
                                , FileListActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable(Key.FileListKey, (Serializable) baseMsg.msg);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    } catch (IOException e) {
                        e.printStackTrace();
                        toastMsg(ConstStrings.FailedFileList);
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
                StringBuilder url = new StringBuilder();
                url.append(getSession(currentHost)
                        .getUrlBase())
                        .append(ConstStrings.FileIOURL)
                        .append(ConstStrings.Question)
                        .append(Key.FilePathKey)
                        .append(ConstStrings.Equal)
                        .append(path);
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                String type = ConstStrings.VideoIntentType;
                Uri uri = Uri.parse(url.toString());
                intent.setDataAndType(uri,type);
                startActivity(intent);
            }
        });
        hlv.setOnItemLongClickListener((adapterView, view, pos, n) -> {
            FileMsg file = (FileMsg) hlv.getItemAtPosition(pos);
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            String[] options = {ConstStrings.Detail, ConstStrings.DownLoad};
            builder
                    .setItems(options, (dialog, which) -> {
                        if (which == 0)
                            dialogShowMsg(ConstStrings.Detail, getFileMsg(file));
                        if (which == 1) {
                            Progress progress = new Progress();
                            progress.setMax(file.size);
                            DownLoadMsg downLoadMsg = new DownLoadMsg();
                            downLoadMsg.setBaseFile(file);
                            downLoadMsg.setRunFlag(true);
                            downLoadMsg.setId(System.currentTimeMillis());
                            downLoadMsg.setProgress(progress);
                            try {
                                downLoadMsg.setFile(FileUtil.createFileByBaseFile(file.name
                                        ,externalRootFile.getPath() + Key.DownLoadPath));
                                IOStream.down(getSession(currentHost),downLoadMsg);
                            } catch (IOException e) {
                                downLoadMsg.setRunFlag(false);
                            }
                            appBox.getDownloadMsgs().add(downLoadMsg);
                        }
                    })
                    .show();
            return true;
        });
    }

    /**
     * 文件信息拼装
     * 返回的字符串每个属性段都会换行显示
     * TODO 该方法属于妥协 正确情况应建立view 填充数据
     * @param file
     * @return
     */
    public String getFileMsg(FileMsg file) {
        return new StringBuffer(ConstStrings.FileName)
                .append(ConstStrings.Colon)
                .append(file.name)
                .append(ConstStrings.LineSeparator)//换行符
                .append(ConstStrings.FilePath)
                .append(ConstStrings.Colon)
                .append(file.path)
                .append(ConstStrings.LineSeparator)//换行符
                .append(ConstStrings.FileType)
                .append(ConstStrings.Colon)
                .append(file.isDir ? ConstStrings.Folder : ConstStrings.File)
                .append(ConstStrings.LineSeparator)//换行符
                .append(ConstStrings.FileSize)
                .append(ConstStrings.Colon)
                .append((file.size > 1048576 ? MyMath.divide(file.size, 1048576, ConstStrings.DivideFormat).concat(ConstStrings.FileUnits)
                        : (file.size > 1024 ? MyMath.divide(file.size, 1024, ConstStrings.DivideFormat).concat(ConstStrings.FileUnitsKb)
                        : String.valueOf(file.size).concat(ConstStrings.FileUnitsByte))))
                .toString();

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
