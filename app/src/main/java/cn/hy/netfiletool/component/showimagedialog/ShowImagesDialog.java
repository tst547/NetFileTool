package cn.hy.netfiletool.component.showimagedialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.bumptech.glide.request.target.SimpleTarget;

import java.util.ArrayList;
import java.util.List;

import cn.hy.netfiletool.R;
import cn.hy.netfiletool.component.showimagedialog.adapter.ShowImagesAdapter;

import com.github.chrisbanes.photoview.PhotoView;

public class ShowImagesDialog extends Dialog {

    private View mView;
    private Context mContext;
    private ShowImagesViewPager mViewPager;
    private TextView mIndexText;
    private List<String> mImgUrls;
    private List<String> mTitles;
    private List<View> mViews;
    private ShowImagesAdapter mAdapter;
    private String defaultItem;

    public ShowImagesDialog(@NonNull Context context, List<String> imgUrls, String defaultItem) {
        super(context, R.style.transparentBgDialog);
        this.mContext = context;
        this.mImgUrls = imgUrls;
        this.defaultItem = defaultItem;
        initView();
        initData();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.setContentView(mView);
        Window window = getWindow();
        WindowManager.LayoutParams wl = window.getAttributes();
        wl.x = 0;
        wl.y = 0;
        DisplayMetrics metrics = getContext().getResources().getDisplayMetrics();

        wl.height = metrics.heightPixels;
        wl.width = metrics.widthPixels; //屏幕宽高的属性由DisplayMetrics类获得
        wl.gravity = Gravity.CENTER;
        window.setAttributes(wl);
    }

    private void initView() {
        mView = View.inflate(mContext, R.layout.show_image_dialog, null);
        mViewPager = mView.findViewById(R.id.vp_images);
        mIndexText = mView.findViewById(R.id.tv_image_index);
        mTitles = new ArrayList<>();
        mViews = new ArrayList<>();
    }


    private void initData() {
        for (String imgUrl : mImgUrls) {
            final PhotoView photoView = new PhotoView(mContext);
            ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            photoView.setLayoutParams(layoutParams);
            photoView.setOnPhotoTapListener((view, x, y) -> dismiss());
            if (imgUrl.endsWith(".gif"))
                Glide.with(mContext)
                        .load(imgUrl)
                        .error(R.drawable.loading_error)
                        .into(new GlideDrawableImageViewTarget(photoView,7));
            else
                Glide.with(mContext)
                        .load(imgUrl)
                        .placeholder(R.drawable.loading_image)
                        .error(R.drawable.loading_error)
                        .into(new SimpleTarget<GlideDrawable>() {
                            @Override
                            public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                                photoView.setImageDrawable(resource);
                            }
                        });

            mViews.add(photoView);
            mTitles.add(mImgUrls.indexOf(imgUrl) + "");
        }

        mAdapter = new ShowImagesAdapter(mViews, mTitles);
        mViewPager.setAdapter(mAdapter);
        mIndexText.setText(1 + "/" + mImgUrls.size());
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mIndexText.setText(position + 1 + "/" + mImgUrls.size());
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        mViewPager.setCurrentItem(mImgUrls.indexOf(defaultItem));//设置当前应显示第几张图片 根据参数而定
    }
}
