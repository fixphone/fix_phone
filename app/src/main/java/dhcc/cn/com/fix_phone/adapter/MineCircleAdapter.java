package dhcc.cn.com.fix_phone.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import dhcc.cn.com.fix_phone.R;
import dhcc.cn.com.fix_phone.adapter.viewholder.CircleViewHolder;
import dhcc.cn.com.fix_phone.adapter.viewholder.ImageViewHolder;
import dhcc.cn.com.fix_phone.adapter.viewholder.URLViewHolder;
import dhcc.cn.com.fix_phone.adapter.viewholder.VideoViewHolder;
import dhcc.cn.com.fix_phone.bean.CircleItem;
import dhcc.cn.com.fix_phone.bean.PhotoInfo;
import dhcc.cn.com.fix_phone.ui.activity.ImagePagerActivity;
import dhcc.cn.com.fix_phone.ui.widget.MultiImageView;

import static com.zhy.http.okhttp.log.LoggerInterceptor.TAG;

/**
 * Created by yiwei on 16/5/17.
 */
public class MineCircleAdapter extends BaseRecycleViewAdapter {
    private Context context;
    private int     type;

    public MineCircleAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getItemViewType(int position) {
        CircleItem item = (CircleItem) datas.get(position);
        if (CircleItem.TYPE_URL.equals(item.getType())) {
            return CircleViewHolder.TYPE_URL;
        } else if (CircleItem.TYPE_IMG.equals(item.getType())) {
            return CircleViewHolder.TYPE_IMAGE;
        } else if (CircleItem.TYPE_VIDEO.equals(item.getType())) {
            return CircleViewHolder.TYPE_VIDEO;
        }
        return super.getItemViewType(position);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_mine_circle_item, parent, false);
        if (viewType == CircleViewHolder.TYPE_URL) {
            viewHolder = new URLViewHolder(view);
        } else if (viewType == CircleViewHolder.TYPE_IMAGE) {
            viewHolder = new ImageViewHolder(view);
        } else if (viewType == CircleViewHolder.TYPE_VIDEO) {
            viewHolder = new VideoViewHolder(view);
        }
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, final int position) {

        final CircleViewHolder holder = (CircleViewHolder) viewHolder;
        if (type == 1) {
            holder.mCommunication.setVisibility(View.GONE);
        } else {
            holder.mCommunication.setVisibility(View.VISIBLE);
        }
        holder.setData((CircleItem) datas.get(position));

        switch (holder.viewType) {
            case CircleViewHolder.TYPE_URL:// 处理链接动态的链接内容和和图片


                break;
            case CircleViewHolder.TYPE_IMAGE:// 处理图片
                if (holder instanceof ImageViewHolder) {
                    imageViewClick((ImageViewHolder) holder, (CircleItem) datas.get(position));
                }

                break;
            case CircleViewHolder.TYPE_VIDEO: // 视频处理
                if (holder instanceof VideoViewHolder) {

                    ((VideoViewHolder) holder).getFrameLayout().setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Log.d(TAG, "onClick: ");
                            if (mOnVideoClickListener != null) {
                                mOnVideoClickListener.onVideoClickListener((CircleItem) datas.get(position));
                            }
                        }
                    });
                }

                break;
            default:
                break;
        }

        holder.snsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mDeleteClickListener != null) {
                    mDeleteClickListener.onDelete(position, (CircleItem) datas.get(position));
                }
            }
        });

    }


    private void imageViewClick(ImageViewHolder holder, final CircleItem circleItem) {
        holder.multiImageView.setOnItemClickListener(new MultiImageView.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                //imagesize是作为loading时的图片size
                ImagePagerActivity.ImageSize imageSize = new ImagePagerActivity.ImageSize(view.getMeasuredWidth(), view.getMeasuredHeight());
                List<String> photoUrls = new ArrayList<>();
                for (PhotoInfo photoInfo : circleItem.getPhotos()) {
                    photoUrls.add(photoInfo.FOriginalUrl);
                }
                ImagePagerActivity.startImagePagerActivity(context, photoUrls, position, imageSize);
            }
        });
    }

    @Override
    public int getItemCount() {
        return datas.size();//有head需要加1
    }

    public void setResourceType(int resourceType) {
        type = resourceType;
    }

    public interface OnDeleteClickListener {
        void onDelete(int position, CircleItem item);
    }

    private OnDeleteClickListener mDeleteClickListener;

    public void setDeleteClickListener(OnDeleteClickListener deleteClickListener) {
        mDeleteClickListener = deleteClickListener;
    }

    public interface OnVideoClickListener {
        void onVideoClickListener(CircleItem circleItem);
    }

    private OnVideoClickListener mOnVideoClickListener;

    public void setOnVideoClickListener(OnVideoClickListener onVideoClickListener) {
        mOnVideoClickListener = onVideoClickListener;
    }
}
