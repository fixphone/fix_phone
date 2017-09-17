package dhcc.cn.com.fix_phone.adapter;

import android.content.Context;
import android.view.View;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.entity.MultiItemEntity;

import java.util.List;

import dhcc.cn.com.fix_phone.R;
import dhcc.cn.com.fix_phone.bean.CircleItem;
import dhcc.cn.com.fix_phone.bean.CircleTitle;

/**
 * 2017/9/17 15
 */
public class CircleAdapter extends BaseMultiItemQuickAdapter<MultiItemEntity, BaseViewHolder> {

    public static final int TYPE_LEVEL_0 = 0;
    public static final int TYPE_LEVEL_1 = 1;

    public CircleAdapter(Context context, List data) {
        super(data);
        addItemType(TYPE_LEVEL_0, R.layout.item_circle_title);
        addItemType(TYPE_LEVEL_1, R.layout.item_circle_content);
    }

    @Override
    protected void convert(final BaseViewHolder helper, MultiItemEntity item) {
        switch (helper.getItemViewType()) {
            case TYPE_LEVEL_0:
                final CircleTitle title = (CircleTitle) item;
                helper.setText(R.id.textView_title, title.getContent());
                helper.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int pos = helper.getAdapterPosition();
                        if (title.isExpanded()) {
                            collapse(pos);
                        } else {
                            expand(pos);
                        }
                    }
                });
                break;
            case TYPE_LEVEL_1:
                final CircleItem circleItem = (CircleItem) item;
                helper.setText(R.id.textView_desc, circleItem.getContent())
                        .setImageResource(R.id.imageView_icon, circleItem.getSourceId());

                helper.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (mOnCircleItemClickListener != null) {
                            mOnCircleItemClickListener.onCircleItemClick(CircleAdapter.this, helper.itemView, circleItem.getTypeId());
                        }
                    }
                });
                break;
        }
    }


    public interface OnCircleItemClickListener {
        void onCircleItemClick(BaseQuickAdapter adapter, View view, String typeId);
    }

    private OnCircleItemClickListener mOnCircleItemClickListener;

    public void setOnCircleItemClickListener(OnCircleItemClickListener onCircleItemClickListener) {
        mOnCircleItemClickListener = onCircleItemClickListener;
    }
}

