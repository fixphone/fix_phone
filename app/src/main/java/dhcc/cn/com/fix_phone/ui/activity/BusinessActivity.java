package dhcc.cn.com.fix_phone.ui.activity;

import android.content.Intent;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.youth.banner.Banner;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import dhcc.cn.com.fix_phone.R;
import dhcc.cn.com.fix_phone.base.BaseActivity;
import dhcc.cn.com.fix_phone.base.GlideImageLoader;
import dhcc.cn.com.fix_phone.bean.BusinessResponse;
import dhcc.cn.com.fix_phone.event.BusinessEvent;
import dhcc.cn.com.fix_phone.remote.ApiManager;

/**
 * 2017/9/26 21
 */
public class BusinessActivity extends BaseActivity {
    private static final String TAG = "BusinessActivity";
    @BindView(R.id.imageview_head)
    ImageView mImageviewHead;
    @BindView(R.id.textView_product)
    TextView  mTextViewProduct;
    @BindView(R.id.textView_phone)
    TextView  mTextViewPhone;
    @BindView(R.id.banner)
    Banner    mBanner;
    @BindView(R.id.textView_desc)
    TextView  mTextViewDesc;
    @BindView(R.id.textView_home)
    TextView  mTextViewHome;
    @BindView(R.id.textView_circle)
    TextView  mTextViewCircle;
    @BindView(R.id.toolbar_title)
    TextView  mToolbarTitle;
    @BindView(R.id.imageView_communication)
    ImageView mCommunication;
    @BindView(R.id.toolbar)
    Toolbar   mToolbar;
    private String mName;
    private String mHeadurl;
    private String mUserID;

    @Override
    public int getLayoutId() {
        return R.layout.activity_business;
    }

    @Override
    protected void init() {
        Intent intent = getIntent();
        mName = intent.getStringExtra("name");
        mHeadurl = intent.getStringExtra("headurl");
        mUserID = intent.getStringExtra("userID");
        Log.d(TAG, "init: " + mUserID);
        EventBus.getDefault().register(this);
    }

    @Override
    protected void initView() {
        mToolbarTitle.setText(mName);
        Glide.with(this).load(mHeadurl).into(mImageviewHead);
    }

    @Override
    protected void initData() {
        ApiManager.Instance().getUserInfo(mUserID);
    }

    @Override
    protected void initEvent() {
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    protected void destroy() {
        super.destroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onShowAd(BusinessEvent event) {
        BusinessResponse response = event.mResponse;
        String companyProfile = response.FObject.companyProfile;
        mTextViewDesc.setText(companyProfile);

        List<String> imageList = response.FObject.productList;
        mBanner.setImageLoader(new GlideImageLoader());
        mBanner.setImages(imageList);
        mBanner.start();
    }

    @OnClick({R.id.textView_circle, R.id.textView_home, R.id.textView_product, R.id.textView_phone})
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.textView_circle:
                finish();
                break;
            case R.id.textView_home:
                startActivity(new Intent(BusinessActivity.this, MainActivity.class));
                break;
            case R.id.textView_product:
                Intent intent = new Intent(BusinessActivity.this, ProductActivity.class);
                intent.putExtra("name",mName).
                        putExtra("headurl",mHeadurl).
                        putExtra("userID",mUserID);
                startActivity(intent);
                break;
            case R.id.textView_phone:

                break;
        }
    }
}
