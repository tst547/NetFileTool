package cn.hy.netfiletool.fragment;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import cn.hy.netfiletool.application.AppBox;

public class BaseListFragment extends ListFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    public AppBox getAppBox(){
        return (AppBox) getActivity().getApplication();
    }
}
