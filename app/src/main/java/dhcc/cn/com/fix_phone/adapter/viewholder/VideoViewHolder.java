package dhcc.cn.com.fix_phone.adapter.viewholder;

import android.view.View;
import android.view.ViewStub;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import dhcc.cn.com.fix_phone.R;
import dhcc.cn.com.fix_phone.bean.CircleItem;

/**
 * Created by suneee on 2016/8/16.
 */
public class VideoViewHolder extends CircleViewHolder {

    private ImageView mVideoplayer;
    private FrameLayout mFrameLayout;

    public VideoViewHolder(View itemView) {
        super(itemView, TYPE_VIDEO);
    }

    @Override
    public void initSubView(int viewType, ViewStub viewStub) {
        if (viewStub == null) {
            throw new IllegalArgumentException("viewStub is null...");
        }
        viewStub.setLayoutResource(R.layout.item_videoview_imageview);
        View subView = viewStub.inflate();
        mVideoplayer = subView.findViewById(R.id.imageView_video);
        mFrameLayout = subView.findViewById(R.id.frameLayout_videoView);
    }

    @Override
    protected void handleChildData(CircleItem circleItem) {
        Glide.with(mVideoplayer.getContext()).load(circleItem.getVideoInfo().FCaptureFileName).into(mVideoplayer);
    }

    public FrameLayout getFrameLayout() {
        return mFrameLayout;
    }
}
