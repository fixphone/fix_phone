package dhcc.cn.com.fix_phone.adapter;

import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import dhcc.cn.com.fix_phone.R;

/**
 * 2017/10/1 17
 */
public class ImageProductAdapter extends BaseQuickAdapter<String, BaseViewHolder> {

    public ImageProductAdapter(@LayoutRes int layoutResId, @Nullable List<String> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {
        ImageView view = helper.getView(R.id.imageView_icon);
        Log.d(TAG, "convert: "+ item);
        Glide.with(mContext).load(item).diskCacheStrategy(DiskCacheStrategy.ALL).into(view);
        ImageView delete = helper.getView(R.id.imageView_delete);
        delete.setVisibility(View.VISIBLE);

        helper.addOnClickListener(R.id.imageView_delete);
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
    }
}
