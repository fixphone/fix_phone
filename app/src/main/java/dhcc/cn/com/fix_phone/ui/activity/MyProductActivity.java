package dhcc.cn.com.fix_phone.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.BindView;
import dhcc.cn.com.fix_phone.Account;
import dhcc.cn.com.fix_phone.MyApplication;
import dhcc.cn.com.fix_phone.R;
import dhcc.cn.com.fix_phone.adapter.ImageProductAdapter;
import dhcc.cn.com.fix_phone.base.BaseActivity;
import dhcc.cn.com.fix_phone.event.FavoResponseEvent;
import dhcc.cn.com.fix_phone.event.ImageResponeEvent;
import dhcc.cn.com.fix_phone.event.ProductImageEvent;
import dhcc.cn.com.fix_phone.remote.ApiManager;
import dhcc.cn.com.fix_phone.ui.fragment.CommonDeleteFragment;

/**
 * 2017/10/1 16
 */
public class MyProductActivity extends BaseActivity implements CommonDeleteFragment.OnConfirmClickListener {

    @BindView(R.id.toolbar_title)
    TextView       mToolbarTitle;
    @BindView(R.id.toolbar)
    Toolbar        mToolbar;
    @BindView(R.id.recyclerView)
    RecyclerView   mRecyclerView;
    @BindView(R.id.imageView_communication)
    ImageView      mCommIcon;
    @BindView(R.id.textView_title)
    TextView       mTextViewDesc;
    @BindView(R.id.relativeLayout)
    RelativeLayout mBottomLayout;

    private String              mUserID;
    private int                 mType;
    private ImageProductAdapter mImageAdapter;
    private List<String>        mStrings;
    private int                 deletePosition;

    @Override
    public int getLayoutId() {
        return R.layout.activity_product;
    }

    @Override
    protected void init() {
        Intent intent = getIntent();
        mUserID = Account.getUserId();
        mType = intent.getIntExtra("type", 0);
        EventBus.getDefault().register(this);
    }

    @Override
    protected void destroy() {
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void initView() {
        mBottomLayout.setVisibility(View.GONE);
        mCommIcon.setVisibility(View.GONE);
        if (mType == 1) {
            mToolbarTitle.setText("店铺广告");
            mTextViewDesc.setText("上传广告图片，为你的店铺打个免费的广告吧！");
        } else {
            mToolbarTitle.setText("店铺产品");
            mTextViewDesc.setText(getSpannableStringBuilder());
        }
        GridLayoutManager layout = new GridLayoutManager(this, 3);
        mRecyclerView.setLayoutManager(layout);
        mRecyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                outRect.set(5, 5, 5, 5);
            }
        });
        mImageAdapter = new ImageProductAdapter(R.layout.item_image_check_myproduct, null);
        mRecyclerView.setAdapter(mImageAdapter);
    }

    @NonNull
    private SpannableStringBuilder getSpannableStringBuilder() {
        String beforeText = "上传广告图片，让更多的小伙伴了解你的产品！\n";
        String afterText = "不建议一次上传超过5张图片";
        String beforeColor = "#000000";
        String afterColor = "#ff0000";
        int beforeSize = 16;
        int afterSize = 14;
        SpannableStringBuilder builder = new SpannableStringBuilder(beforeText);
        builder.setSpan(new ForegroundColorSpan(Color.parseColor(beforeColor)), 0, beforeText.length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE); //设置前面的字体颜色
        builder.setSpan(new AbsoluteSizeSpan(beforeSize, true), 0, beforeText.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE); //设置前面的字体大小
        builder.append(afterText);
        builder.setSpan(new ForegroundColorSpan(Color.parseColor(afterColor)), beforeText.length(), builder.length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE); //设置后面的字体颜色
        builder.setSpan(new AbsoluteSizeSpan(afterSize, true), beforeText.length(), builder.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);//设置后面的字体大小
        return builder;
    }

    @Override
    protected void initData() {
        if (mType == 1) {
            ApiManager.Instance().GetStoreList(mUserID);
        } else {
            ApiManager.Instance().getProductList(mUserID);
        }
    }

    @Override
    protected void initEvent() {
        super.initEvent();
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        mImageAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                ImagePagerActivity.ImageSize imageSize = new ImagePagerActivity.ImageSize(view.getMeasuredWidth(), view.getMeasuredHeight());
                ImagePagerActivity.startImagePagerActivity(MyProductActivity.this, mStrings, position, imageSize);
            }
        });

        mImageAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                deletePosition = position;
                if (mType == 1) {
                    createDialog("提示", "上传广告图片，为你的店铺打个免费的广告吧！");
                } else {
                    createDialog("注意", "删除的产品图片不会显示在店铺主页了哦！");
                }
            }
        });
    }

    private void createDialog(String title, String desc) {
        CommonDeleteFragment fragment = CommonDeleteFragment.newInstance(title, desc);
        fragment.show(getSupportFragmentManager(), "CommonDeleteFragment");
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onshowImage(ImageResponeEvent event) {
        if (event.mImageResponse.FIsSuccess) {
            mStrings = event.mImageResponse.FObject.list;
            mImageAdapter.setNewData(mStrings);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onshowImage(ProductImageEvent event) {
        if (event.mProductImage.FIsSuccess) {
            mStrings = event.mProductImage.FObject.list;
            mImageAdapter.setNewData(mStrings);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onshowImage(FavoResponseEvent event) {
        Toast.makeText(this, "" + event.mResponse.FMsg, Toast.LENGTH_SHORT).show();
        if (event.mResponse.FIsSuccess) {
            mImageAdapter.remove(deletePosition);
            Toast.makeText(this, "" + event.mResponse.FMsg, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onConfirm() {
        if (mType == 1) {
            ApiManager.Instance().DeleteStoreAdver(mImageAdapter.getItem(deletePosition));
        } else {
            ApiManager.Instance().DeleteProductIcon(mImageAdapter.getItem(deletePosition));
        }
    }
}
