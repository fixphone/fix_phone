package dhcc.cn.com.fix_phone.ui.activity;

import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.youth.banner.Banner;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import dhcc.cn.com.fix_phone.Account;
import dhcc.cn.com.fix_phone.R;
import dhcc.cn.com.fix_phone.base.BaseActivity;
import dhcc.cn.com.fix_phone.base.GlideImageLoader;
import dhcc.cn.com.fix_phone.bean.BusinessResponse;
import dhcc.cn.com.fix_phone.conf.Constants;
import dhcc.cn.com.fix_phone.event.AddFriendEvent;
import dhcc.cn.com.fix_phone.event.BusinessEvent;
import dhcc.cn.com.fix_phone.remote.ApiManager;
import dhcc.cn.com.fix_phone.utils.CommunicationUtil;

/**
 * 2017/9/26 21
 */
public class BusinessActivity extends BaseActivity {
    private static final String TAG = "BusinessActivity";
    @BindView(R.id.imageview_head)
    ImageView      mImageviewHead;
    @BindView(R.id.textView_product)
    TextView       mTextViewProduct;
    @BindView(R.id.textView_phone)
    TextView       mTextViewPhone;
    @BindView(R.id.banner)
    Banner         mBanner;
    @BindView(R.id.textView_desc)
    TextView       mTextViewDesc;
    @BindView(R.id.textView_home)
    TextView       mTextViewHome;
    @BindView(R.id.textView_circle)
    TextView       mTextViewCircle;
    @BindView(R.id.toolbar_title)
    TextView       mToolbarTitle;
    @BindView(R.id.imageView_communication)
    ImageView      mCommunication;
    @BindView(R.id.toolbar)
    Toolbar        mToolbar;
    @BindView(R.id.relativeLayout)
    RelativeLayout mRelativeLayout;
    @BindView(R.id.button_call)
    Button         mButtonCall;
    @BindView(R.id.button_addFriend)
    Button         mButtonAddFriend;

    private String           mName;
    private String           mHeadurl;
    private String           mUserID;
    private String           mweChatId;
    private String           mFFriendID;
    private BusinessResponse mResponse;
    private int              type;
    private boolean          mIsIMCall;
    private boolean          mIsIMAddFriend;

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
        mweChatId = intent.getStringExtra("weChatId");
        mFFriendID = intent.getStringExtra("FFriendID");
        type = intent.getIntExtra("type", 0);
        mIsIMCall = intent.getBooleanExtra("isIMCall", false);
        mIsIMAddFriend = intent.getBooleanExtra("isIMAddFriend", false);
        EventBus.getDefault().register(this);
        Log.d(TAG, "init: mUserID：" + mUserID);
        Log.d(TAG, "init: mName：" + mName);
        Log.d(TAG, "init: mweChatId：" + mweChatId);
        Log.d(TAG, "init: type：" + type);
        Log.d(TAG, "init: headurl：" + mHeadurl);
        Log.d(TAG, "init: FFriendID：" + mFFriendID);
    }

    @Override
    protected void initView() {
        mToolbarTitle.setText(mName);
        Glide.with(this).load(mHeadurl).into(mImageviewHead);
        if (type == 1) {
            mRelativeLayout.setVisibility(View.GONE);
            mCommunication.setVisibility(View.GONE);
        }
        if (mIsIMCall) {
            type = 1;
            mButtonCall.setVisibility(View.VISIBLE);
            mRelativeLayout.setVisibility(View.GONE);
            mCommunication.setVisibility(View.GONE);
        }
        if (mIsIMAddFriend) {
            type = 1;
            mButtonCall.setVisibility(View.GONE);
            mRelativeLayout.setVisibility(View.GONE);
            mCommunication.setVisibility(View.GONE);
            mButtonAddFriend.setVisibility(View.VISIBLE);
        }
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

        mCommunication.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CommunicationUtil.communication(BusinessActivity.this, mweChatId, mName);
            }
        });
    }

    @OnClick(R.id.button_call)
    public void onCall(View view) {
        CommunicationUtil.communication(BusinessActivity.this, mweChatId, mName);
    }

    @OnClick(R.id.button_addFriend)
    public void addFriend(View view) {
        ApiManager.Instance().AddFriend(Account.getAccessToken(), mFFriendID);
    }

    @Override
    protected void destroy() {
        super.destroy();
        EventBus.getDefault().unregister(this);
    }

    private void setViewData() {
        mTextViewDesc.setText(mResponse.FObject.companyProfile);
        Glide.with(this).load(mHeadurl = TextUtils.isEmpty(mResponse.FObject.headUrl) ? mHeadurl : mResponse.FObject.headUrl).into(mImageviewHead);
        mToolbarTitle.setText(mName = mResponse.FObject.companyName);
        mweChatId = mResponse.FObject.phone;
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
                intent.putExtra("name", mName).
                        putExtra("headurl", mHeadurl).
                        putExtra("weChatId", mweChatId).
                        putExtra("userID", mUserID).
                        putExtra("type", type);
                startActivity(intent);
                break;

            case R.id.textView_phone:
                Intent intent2 = new Intent(BusinessActivity.this, ContactWayActivity.class);
                intent2.putExtra("mResponse", mResponse).
                        putExtra("name", mName).
                        putExtra("headurl", mHeadurl).
                        putExtra("weChatId", mweChatId).
                        putExtra("userID", mUserID).
                        putExtra("type", type);
                startActivity(intent2);
                break;
            default:
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void addFriend(AddFriendEvent event) {
        if (event.addFriendResponse != null) {
            Log.d(TAG, "queryUser: " + event.addFriendResponse.FObject);
            if (event.addFriendResponse.FObject != null) {
                LocalBroadcastManager.getInstance(this).sendBroadcast(new Intent(Constants.ADD_FRIEND_SUCCESS));
                Toast.makeText(this, "" + event.addFriendResponse.FMsg, Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onUserInfo(BusinessEvent event) {
        mResponse = event.mResponse;
        if (mResponse != null) {
            setViewData();
            List<String> imageList = mResponse.FObject.productList;
            mBanner.setImageLoader(new GlideImageLoader());
            mBanner.setImages(imageList);
            mBanner.start();
        }
    }
}
