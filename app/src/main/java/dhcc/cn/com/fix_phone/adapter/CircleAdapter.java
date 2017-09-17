package dhcc.cn.com.fix_phone.adapter;

import android.content.Context;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import dhcc.cn.com.fix_phone.R;
import dhcc.cn.com.fix_phone.bean.CircleItem;

/**
 * 2017/9/17 15
 */
public class CircleAdapter extends BaseMultiItemQuickAdapter<CircleItem, BaseViewHolder> {

    public CircleAdapter(Context context, List data) {
        super(data);
        addItemType(CircleItem.TEXT, R.layout.item_circle_title);
        addItemType(CircleItem.IMG_TEXT, R.layout.item_circle_content);
    }

    @Override
    protected void convert(BaseViewHolder helper, CircleItem item) {
        switch (helper.getItemViewType()) {
            case CircleItem.TEXT:
                helper.setText(R.id.textView_title, item.getContent());
                break;
            case CircleItem.IMG_TEXT:
                helper.setText(R.id.textView_desc, item.getContent()).
                        setImageResource(R.id.imageView_icon, item.getSourceId());
                break;
        }
    }
}

