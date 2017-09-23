package dhcc.cn.com.fix_phone.adapter.viewholder;

import android.view.View;
import android.view.ViewStub;

import java.util.List;

import dhcc.cn.com.fix_phone.R;
import dhcc.cn.com.fix_phone.bean.CircleItem;
import dhcc.cn.com.fix_phone.bean.PhotoInfo;
import dhcc.cn.com.fix_phone.ui.widget.MultiImageView;

/**
 * Created by suneee on 2016/8/16.
 */
public class ImageViewHolder extends CircleViewHolder {
    /**
     * 图片
     */
    public MultiImageView multiImageView;

    public ImageViewHolder(View itemView) {
        super(itemView, TYPE_IMAGE);
    }

    @Override
    public void initSubView(int viewType, ViewStub viewStub) {
        if (viewStub == null) {
            throw new IllegalArgumentException("viewStub is null...");
        }
        viewStub.setLayoutResource(R.layout.viewstub_imgbody);
        View subView = viewStub.inflate();
        MultiImageView multiImageView = subView.findViewById(R.id.multiImagView);
        if (multiImageView != null) {
            this.multiImageView = multiImageView;
        }
    }

    @Override
    protected void handleChildData(CircleItem circleItem) {
        final List<PhotoInfo> photos = circleItem.getPhotos();
        if (photos != null && photos.size() > 0) {
            multiImageView.setVisibility(View.VISIBLE);
            multiImageView.setList(photos);
        } else {
            multiImageView.setVisibility(View.GONE);
        }
    }
}
