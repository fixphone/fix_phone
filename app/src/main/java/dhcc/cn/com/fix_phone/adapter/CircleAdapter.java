package dhcc.cn.com.fix_phone.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.youth.banner.Banner;

import java.util.ArrayList;
import java.util.List;

import dhcc.cn.com.fix_phone.R;
import dhcc.cn.com.fix_phone.adapter.viewholder.CircleViewHolder;
import dhcc.cn.com.fix_phone.adapter.viewholder.ImageViewHolder;
import dhcc.cn.com.fix_phone.adapter.viewholder.URLViewHolder;
import dhcc.cn.com.fix_phone.adapter.viewholder.VideoViewHolder;
import dhcc.cn.com.fix_phone.base.GlideImageLoader;
import dhcc.cn.com.fix_phone.bean.CircleDetailAd;
import dhcc.cn.com.fix_phone.bean.CircleItem;
import dhcc.cn.com.fix_phone.bean.PhotoInfo;
import dhcc.cn.com.fix_phone.mvp.presenter.CirclePresenter;
import dhcc.cn.com.fix_phone.ui.activity.ImagePagerActivity;
import dhcc.cn.com.fix_phone.ui.widget.MultiImageView;
import dhcc.cn.com.fix_phone.ui.widget.SnsPopupWindow;

/**
 * Created by yiwei on 16/5/17.
 */
public class CircleAdapter extends BaseRecycleViewAdapter {
    public final static int TYPE_HEAD     = 0;
    public static final int HEADVIEW_SIZE = 1;
    private static final String TAG = "CircleAdapter";

    private CirclePresenter                           presenter;
    private Activity                                  context;
    private List<CircleDetailAd.FObjectBean.ListBean> mList; // 广告原始数据
    private List<String> imageList = new ArrayList<>(); // 广告

    public void setCirclePresenter(CirclePresenter presenter) {
        this.presenter = presenter;
    }

    public CircleAdapter(Activity context) {
        this.context = context;
    }

    public void setFObjectBean(List<CircleDetailAd.FObjectBean.ListBean> list) {
        mList = list;
        imageList.clear();
        for (int i = 0; i < list.size(); i++) {
            imageList.add(list.get(i).FUrl);
        }
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return TYPE_HEAD;
        }
        CircleItem item = (CircleItem) datas.get(position - 1);
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
        if (viewType == TYPE_HEAD) {
            View headView = LayoutInflater.from(parent.getContext()).inflate(R.layout.bannar, parent, false);
            viewHolder = new HeaderViewHolder(headView);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_circle_item, parent, false);
            if (viewType == CircleViewHolder.TYPE_URL) {
                viewHolder = new URLViewHolder(view);
            } else if (viewType == CircleViewHolder.TYPE_IMAGE) {
                viewHolder = new ImageViewHolder(view);
            } else if (viewType == CircleViewHolder.TYPE_VIDEO) {
                viewHolder = new VideoViewHolder(view);
            }
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, final int position) {
        if (getItemViewType(position) == TYPE_HEAD) {
            HeaderViewHolder headerViewHolder = (HeaderViewHolder) viewHolder;
            headerViewHolder.setBannerData();
        } else {
            final int realPosition = position - HEADVIEW_SIZE;
            final CircleViewHolder holder = (CircleViewHolder) viewHolder;
            holder.setActivity(context);
            holder.setData((CircleItem) datas.get(realPosition));

            switch (holder.viewType) {
                case CircleViewHolder.TYPE_URL:// 处理链接动态的链接内容和和图片


                    break;
                case CircleViewHolder.TYPE_IMAGE:// 处理图片
                    if (holder instanceof ImageViewHolder) {
                        imageViewClick((ImageViewHolder) holder, (CircleItem) datas.get(realPosition));
                    }

                    break;
                case CircleViewHolder.TYPE_VIDEO: // 视频处理
                    if (holder instanceof VideoViewHolder) {
                        ((VideoViewHolder) holder).videoView.videoPlayer.setOnLongClickListener(new View.OnLongClickListener() {
                            @Override
                            public boolean onLongClick(View view) {
                                Log.d(TAG, "onLongClick: ");
                                if (mOnVideoLongClickListener != null) {
                                    mOnVideoLongClickListener.onVideoLongClickListener((CircleItem) datas.get(realPosition));
                                }
                                return true;
                            }
                        });
                    }

                    break;
                default:
                    break;
            }

            popupWindow(holder, (CircleItem) datas.get(realPosition));
        }
    }

    private void popupWindow(CircleViewHolder holder, final CircleItem circleItem) {
        final SnsPopupWindow snsPopupWindow = holder.snsPopupWindow;
        snsPopupWindow.update();
        snsPopupWindow.setmItemClickListener(new SnsPopupWindow.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                if (mOnPopWindowClickListener != null) {
                    mOnPopWindowClickListener.onItemClick(position, circleItem);
                }
            }
        });

        holder.snsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //弹出popupwindow
                snsPopupWindow.showPopupWindow(view);
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
        return datas.size() + 1;//有head需要加1
    }

    public class HeaderViewHolder extends RecyclerView.ViewHolder {
        public Banner mBanner;

        public HeaderViewHolder(View itemView) {
            super(itemView);
            mBanner = itemView.findViewById(R.id.banner);
        }

        public void setBannerData() {
            mBanner.setImageLoader(new GlideImageLoader());
            mBanner.setImages(imageList);
            mBanner.start();
        }
    }

    /**
     * 功能描述：弹窗子类项按钮监听事件
     */
    public interface OnPopWindowClickListener {
        void onItemClick(int position, CircleItem circleItem);
    }

    public void setOnPopWindowClickListener(OnPopWindowClickListener mItemClickListener) {
        this.mOnPopWindowClickListener = mItemClickListener;
    }

    private OnPopWindowClickListener mOnPopWindowClickListener;


    public interface OnVideoLongClickListener{
        void onVideoLongClickListener(CircleItem circleItem);
    }

    private OnVideoLongClickListener mOnVideoLongClickListener;

    public void setOnVideoLongClickListener(OnVideoLongClickListener onVideoLongClickListener) {
        mOnVideoLongClickListener = onVideoLongClickListener;
    }
}
