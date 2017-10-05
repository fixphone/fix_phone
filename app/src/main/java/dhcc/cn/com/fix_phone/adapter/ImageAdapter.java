package dhcc.cn.com.fix_phone.adapter;

import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import dhcc.cn.com.fix_phone.R;

/**
 * 2017/10/1 17
 */
public class ImageAdapter extends BaseQuickAdapter<String, BaseViewHolder> {

    public ImageAdapter(@LayoutRes int layoutResId, @Nullable List<String> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {
        ImageView view = helper.getView(R.id.imageView_icon);
        Glide.with(mContext).load(item).into(view);
    }
}
