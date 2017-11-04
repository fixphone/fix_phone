package dhcc.cn.com.fix_phone.adapter;

import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import dhcc.cn.com.fix_phone.R;
import dhcc.cn.com.fix_phone.bean.QueryUserResponse;
import dhcc.cn.com.fix_phone.utils.GlideCircleTransform;

/**
 * 2017/11/4 15
 */
public class AddFriendAdapter extends BaseQuickAdapter<QueryUserResponse.QueryList.QueryUser, BaseViewHolder> {

    public AddFriendAdapter(@LayoutRes int layoutResId, @Nullable List<QueryUserResponse.QueryList.QueryUser> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, QueryUserResponse.QueryList.QueryUser item) {
        ImageView view = helper.getView(R.id.search_header);
        Glide.with(mContext).load(item.FHeadUrl).diskCacheStrategy(DiskCacheStrategy.ALL).transform(new GlideCircleTransform(mContext)).into(view);
        helper.setText(R.id.search_name,item.FCompanyName);
    }
}
