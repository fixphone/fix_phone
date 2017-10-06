package dhcc.cn.com.fix_phone.ui.activity;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.BindView;
import dhcc.cn.com.fix_phone.R;
import dhcc.cn.com.fix_phone.base.BaseActivity;

/**
 * 2017/9/26 23
 */
public class PublishActivity extends BaseActivity {

    @BindView(R.id.title_back_iv)
    ImageView      mTitleBackIv;
    @BindView(R.id.title_back)
    RelativeLayout mTitleBack;
    @BindView(R.id.title_name)
    TextView       mTitleName;
    @BindView(R.id.title_right)
    TextView       mTitleRight;
    @BindView(R.id.bottom_line)
    View           mBottomLine;
    @BindView(R.id.container_rl)
    RelativeLayout mContainerRl;
    @BindView(R.id.content_et)
    EditText       mContentEt;
    @BindView(R.id.count_tv)
    TextView       mCountTv;
    @BindView(R.id.photo_select_recycle_view)
    RecyclerView   mPhotoSelectRecycleView;
    @BindView(R.id.btn_confirm)
    Button         mBtnConfirm;
    @BindView(R.id.textView_type)
    TextView       mTextViewType;

    @Override
    public int getLayoutId() {
        return R.layout.activity_publish;
    }

    @Override
    protected void initEvent() {
        mTextViewType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }
}
