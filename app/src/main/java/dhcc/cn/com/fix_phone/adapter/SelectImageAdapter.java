package dhcc.cn.com.fix_phone.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;
import java.util.List;

import dhcc.cn.com.fix_phone.R;

/**
 * 2017/10/1 17
 */
public class SelectImageAdapter extends RecyclerView.Adapter<SelectImageAdapter.SelectViewHolder> {
    private List<String> mData = new ArrayList<>();
    private Context mContext;

    public List<String> getData() {
        return mData;
    }

    public void setData(List<String> data) {
        mData = data;
    }

    public SelectImageAdapter(Context context, List<String> data) {
        mContext = context;
        if (data != null) {
            mData = data;
        }
    }

    @Override
    public SelectImageAdapter.SelectViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_image_check, parent, false);
        return new SelectViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(SelectImageAdapter.SelectViewHolder helper, final int position) {
        if (position == mData.size()) {
            helper.mIcon.setImageResource(R.drawable.add_camera);
            helper.mDelete.setVisibility(View.GONE);
            helper.mIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mOnAddClickListener != null) {
                        mOnAddClickListener.onAdd();
                    }
                }
            });

        } else {
            Glide.with(mContext).load(mData.get(position)).diskCacheStrategy(DiskCacheStrategy.ALL).into(helper.mIcon);
            helper.mDelete.setVisibility(View.VISIBLE);
            helper.mDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mOnDeleteClickListener != null) {
                        mOnDeleteClickListener.onDelete(position);
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mData.size() < 9 ? mData.size() + 1 : mData.size();
    }

    public void addData(List<String> strings) {
        mData.addAll(strings);
        notifyDataSetChanged();
    }

    public void addData(String s) {
        mData.add(s);
    }

    public void remove(int position) {
        mData.remove(position);
        notifyDataSetChanged();
    }

    public static class SelectViewHolder extends RecyclerView.ViewHolder {

        public final ImageView mIcon;
        public final ImageView mDelete;

        public SelectViewHolder(View itemView) {
            super(itemView);
            mIcon = itemView.findViewById(R.id.imageView_icon);
            mDelete = itemView.findViewById(R.id.imageView_delete);
        }
    }

    public interface OnDeleteClickListener {
        void onDelete(int position);
    }

    public interface OnAddClickListener {
        void onAdd();
    }

    private OnDeleteClickListener mOnDeleteClickListener;

    private OnAddClickListener mOnAddClickListener;

    public void setOnDeleteClickListener(OnDeleteClickListener onDeleteClickListener) {
        mOnDeleteClickListener = onDeleteClickListener;
    }

    public void setOnAddClickListener(OnAddClickListener onAddClickListener) {
        mOnAddClickListener = onAddClickListener;
    }
}
