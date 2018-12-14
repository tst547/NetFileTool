package cn.hy.netfiletool.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import cn.hy.netfiletool.R;

public class LocalFileListFragment extends Fragment {

    public LocalFileListFragment(){
        super();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.local_file_list_fragment, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
    }
}
