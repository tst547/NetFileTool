package cn.hy.netfiletool.fragment.listfragment;

import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import cn.hy.netfiletool.R;
import cn.hy.netfiletool.fragment.BaseListFragment;
import cn.hy.netfiletool.key.ConstStrings;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class LocalFileListFragment extends BaseListFragment {

    private File externalRootFile;

    private CustomAdapter customAdapter;

    private List<MobileFile> fileList = new ArrayList<>();

    public LocalFileListFragment(){
        super();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        externalRootFile = Environment.getExternalStorageDirectory();
        if (null!=externalRootFile)
            for (File temp : externalRootFile.listFiles()){
                MobileFile mobileFile = new MobileFile();
                mobileFile.file = temp;
                mobileFile.fileName = temp.getName();
                fileList.add(mobileFile);
            }
        return inflater.inflate(R.layout.local_file_list_fragment, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        initList(fileList);
    }

    /**
     * 初始化列表
     *
     * @param files
     */
    protected void initList(List<MobileFile> files) {
        customAdapter = new CustomAdapter(files);
        setListAdapter(customAdapter);
    }


    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        MobileFile clickFile = (MobileFile) customAdapter.getItem(position);
        if (clickFile.file.isDirectory()){
            fileList.clear();
            if(!externalRootFile.getPath()
                    .equals(clickFile.file.getPath())){
                //拿当前点击文件的父级作为返回文件，实现点击该文件时返回上级
                MobileFile backFile = new MobileFile();
                backFile.fileName =ConstStrings.Back;
                backFile.file = clickFile.file.getParentFile();
                fileList.add(backFile);
            }
            if (null!=clickFile.file.listFiles())
                for (File temp : clickFile.file.listFiles()){
                    MobileFile mobileFile = new MobileFile();
                    mobileFile.file = temp;
                    mobileFile.fileName = temp.getName();
                    fileList.add(mobileFile);
                }
        }
        new Handler().postDelayed(()-> initList(fileList),200);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
    }

    class CustomAdapter extends BaseAdapter {

        private List<MobileFile> files;

        protected CustomAdapter(List<MobileFile> files) {
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
            MobileFile mobileFile = files.get(position);
            LayoutInflater inflater = getActivity().getLayoutInflater();
            View view = inflater.inflate(R.layout.file_item, null);
            TextView text = view.findViewById(R.id.item_text);
            ImageView image = view.findViewById(R.id.icon);
            text.setText(mobileFile.fileName);
            if (mobileFile.file.isDirectory())
                image.setImageResource(R.drawable.mobile_folder);
            else
                image.setImageResource(R.drawable.mobile_file);
            return view;
        }
    }

    /**
     * 列表展示用 (实现返回上级)
     */
    class MobileFile{
        String fileName;
        File file;
    }
}
