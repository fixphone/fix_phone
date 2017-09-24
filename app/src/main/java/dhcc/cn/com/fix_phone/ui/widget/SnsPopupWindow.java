package dhcc.cn.com.fix_phone.ui.widget;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.PopupWindow;
import android.widget.TextView;

import dhcc.cn.com.fix_phone.R;
import dhcc.cn.com.fix_phone.utils.DensityUtil;

/**
 * 朋友圈点赞评论的popupwindow
 *
 * @author wei.yi
 */
public class SnsPopupWindow extends PopupWindow implements OnClickListener {

    private TextView collect;  //收藏
    private TextView complain; //投诉
    private TextView share;    //分享

    // 实例化一个矩形
    private       Rect  mRect     = new Rect();
    // 坐标的位置（x、y）
    private final int[] mLocation = new int[2];
    // 弹窗子类项选中时的监听
    private OnItemClickListener mItemClickListener;
    // 定义弹窗子类项列表

    public void setmItemClickListener(OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }

    public SnsPopupWindow(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.social_sns_popupwindow, null);
        collect = (TextView) view.findViewById(R.id.collect);
        complain = (TextView) view.findViewById(R.id.complain);
        share = (TextView) view.findViewById(R.id.share);
        collect.setOnClickListener(this);
        complain.setOnClickListener(this);
        share.setOnClickListener(this);

        this.setContentView(view);
        this.setWidth(DensityUtil.dip2px(context, 200));
        this.setHeight(DensityUtil.dip2px(context, 30));
        this.setFocusable(true);
        this.setOutsideTouchable(true);
        this.update();
        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0000000000);
        // 点back键和其他地方使其消失,设置了这个才能触发OnDismisslistener ，设置其他控件变化等操作
        this.setBackgroundDrawable(dw);
        this.setAnimationStyle(R.style.social_pop_anim);

    }

    public void showPopupWindow(View parent) {
        parent.getLocationOnScreen(mLocation);
        // 设置矩形的大小
        mRect.set(mLocation[0], mLocation[1], mLocation[0] + parent.getWidth(), mLocation[1] + parent.getHeight());
        if (!this.isShowing()) {
            showAtLocation(parent, Gravity.NO_GRAVITY, mLocation[0] - this.getWidth(), mLocation[1] - ((this.getHeight() - parent.getHeight()) / 2));
        } else {
            dismiss();
        }
    }

    @Override
    public void onClick(View view) {
        int position = 0;
        dismiss();
        switch (view.getId()) {
            case R.id.collect:
                position = 0;
                break;
            case R.id.complain:
                position = 1;
                break;
            case R.id.share:
                position = 2;
                break;
        }
        if (mItemClickListener != null) {
            mItemClickListener.onItemClick(position);
        }
    }

    /**
     * 功能描述：弹窗子类项按钮监听事件
     */
    public static interface OnItemClickListener {
        public void onItemClick(int position);
    }
}
