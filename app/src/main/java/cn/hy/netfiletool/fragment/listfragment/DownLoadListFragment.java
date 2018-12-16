package cn.hy.netfiletool.fragment.listfragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import cn.hy.netfiletool.R;
import cn.hy.netfiletool.fragment.BaseListFragment;

public class DownLoadListFragment extends BaseListFragment {

    public DownLoadListFragment(){
        super();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.down_load_list_fragment, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

}
