package dhcc.cn.com.fix_phone.adapter.viewholder;

import android.view.View;
import android.view.ViewStub;

import dhcc.cn.com.fix_phone.R;
import dhcc.cn.com.fix_phone.bean.CircleItem;
import dhcc.cn.com.fix_phone.ui.widget.CircleVideoView;

/**
 * Created by suneee on 2016/8/16.
 */
public class VideoViewHolder extends CircleViewHolder {

    public CircleVideoView videoView;

    public VideoViewHolder(View itemView) {
        super(itemView, TYPE_VIDEO);
    }

    @Override
    public void initSubView(int viewType, ViewStub viewStub) {
        if (viewStub == null) {
            throw new IllegalArgumentException("viewStub is null...");
        }

        viewStub.setLayoutResource(R.layout.viewstub_videobody);
        View subView = viewStub.inflate();
        CircleVideoView videoBody = subView.findViewById(R.id.videoView);
        if (videoBody != null) {
            this.videoView = videoBody;
        }
    }

    @Override
    protected void handleChildData(CircleItem circleItem) {
        videoView.setVideoUrl(circleItem.getVideoInfo().FFileName);
        videoView.setVideoImgUrl(circleItem.getVideoInfo().FCaptureFileName);//视频封面图片
        videoView.setPostion(getAdapterPosition());
    }
}
