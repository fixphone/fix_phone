package dhcc.cn.com.fix_phone.ui.fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.OnClick;
import dhcc.cn.com.fix_phone.Account;
import dhcc.cn.com.fix_phone.R;
import dhcc.cn.com.fix_phone.base.BaseFragment;
import dhcc.cn.com.fix_phone.bean.BusinessResponse;
import dhcc.cn.com.fix_phone.event.BusinessEvent;
import dhcc.cn.com.fix_phone.remote.ApiManager;
import dhcc.cn.com.fix_phone.rong.SealConst;
import dhcc.cn.com.fix_phone.ui.activity.AboutAppActivity;
import dhcc.cn.com.fix_phone.ui.activity.FeedBackActivity;
import dhcc.cn.com.fix_phone.ui.activity.LoginActivity;
import dhcc.cn.com.fix_phone.ui.activity.MineCirCleActivity;
import dhcc.cn.com.fix_phone.ui.activity.MyActivity;
import dhcc.cn.com.fix_phone.ui.activity.MyProductActivity;
import dhcc.cn.com.fix_phone.ui.activity.PersonInfoActivity;
import dhcc.cn.com.fix_phone.ui.activity.VipActivity;
import dhcc.cn.com.fix_phone.ui.widget.LoadDialog;
import dhcc.cn.com.fix_phone.ui.widget.RoundImageView;
import io.rong.imkit.RongIM;
import io.rong.imlib.model.UserInfo;

/**
 * 2017/9/16 23
 */
public class MeFragment extends BaseFragment {
    private static final String TAG = "MeFragment";
    @BindView(R.id.container_rl)
    RelativeLayout mContainerRl;
    @BindView(R.id.title_back)
    RelativeLayout mTitleBackRl;
    @BindView(R.id.title_right)
    TextView       mTitleRightTv;
    @BindView(R.id.bottom_line)
    View           mBottomLine;
    @BindView(R.id.title_name)
    TextView       mTitleNameTv;
    @BindView(R.id.user_name)
    TextView       user_name;
    @BindView(R.id.user_mobile)
    TextView       user_mobile;
    @BindView(R.id.user_icon)
    RoundImageView user_icon;

    private BusinessResponse mResponse;
    private SharedPreferences sp;
    private SharedPreferences.Editor editor;
    private LoadDialog loadDialog;
    private Toast toast;

    public static MeFragment newInstance() {
        Bundle args = new Bundle();

        MeFragment fragment = new MeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_me;
    }

    @Override
    protected void init() {
        super.init();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void initEvent() {
        super.initEvent();
        mContainerRl.setBackgroundColor(Color.TRANSPARENT);
        mTitleBackRl.setVisibility(View.GONE);
        mBottomLine.setVisibility(View.GONE);
        mTitleNameTv.setVisibility(View.GONE);
        mTitleRightTv.setText("设置");
        mTitleRightTv.setTextColor(ContextCompat.getColor(getContext(), R.color.app_text_color_black));
        sp = getContext().getSharedPreferences("config", getContext().MODE_PRIVATE);
        editor = sp.edit();
        loadDialog = new LoadDialog(getContext(), false, "");
        toast = Toast.makeText(getContext(), "", Toast.LENGTH_SHORT);
    }

    @Override
    protected void initData() {
        if(Account.isLogin())
        ApiManager.Instance().getUserInfo(Account.getUserId());
    }

    @Override
    public void onResume() {
        super.onResume();
        if(Account.isLogin()){
            if(mResponse != null){
                setViewState(mResponse.FObject.name, mResponse.FObject.phone, mResponse.FObject.headUrl);
            }
        }else {
            setViewState("", "", "");
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onUserInfo(BusinessEvent event) {
        mResponse = event.mResponse;
        if(mResponse != null){
            setViewState(mResponse.FObject.name, mResponse.FObject.phone, mResponse.FObject.headUrl);
        }
    }

    private void setViewState(String name, String phone, String headUrl){
        user_name.setText(name);
        user_mobile.setText(phone);
        if(TextUtils.isEmpty(headUrl)){
            user_icon.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.user_default_icon));
        }else {
            Glide.with(getContext()).load(headUrl).diskCacheStrategy(DiskCacheStrategy.ALL).into(user_icon);
        }
        RongIM.getInstance().setCurrentUserInfo(new UserInfo(Account.getUserId(), name, Uri.parse(headUrl)));
        editor.putString(SealConst.SEALTALK_LOGIN_NAME, name);
        editor.apply();
    }

    @OnClick({R.id.mine_info, R.id.mine_circle, R.id.mine_house, R.id.mine_suggest, R.id.mine_vip,
            R.id.mine_advert, R.id.mine_produce, R.id.mine_clear, R.id.mine_app, R.id.title_right})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.mine_info:
                if(Account.isLogin()){
                    Intent intent = new Intent(getContext(), PersonInfoActivity.class);
                    intent.putExtra("BusinessResponse", mResponse);
                    startActivity(intent);
                }else {
                    startLoginActivity();
                }
                break;
            case R.id.mine_circle:
                if(Account.isLogin()){
                    startActivity(new Intent(getActivity(), MineCirCleActivity.class).putExtra("title", "我的圈子").
                            putExtra("resource_type", 1));
                }else {
                    startLoginActivity();
                }

                break;
            case R.id.mine_house:
                if(Account.isLogin()){
                    startActivity(new Intent(getActivity(), MineCirCleActivity.class).putExtra("title", "我的收藏").
                            putExtra("resource_type", 2));
                }else {
                    startLoginActivity();
                }
                break;
            case R.id.mine_suggest:
                if(Account.isLogin()){
                    startActivity(new Intent(getContext(),FeedBackActivity.class));
                }else {
                    startLoginActivity();
                }
                break;
            case R.id.mine_vip:
                if(Account.isLogin()){
                    startActivity(new Intent(getContext(), VipActivity.class));
                }else {
                    startLoginActivity();
                }
                break;
            case R.id.mine_advert:
                if(Account.isLogin()){
                    startActivity(new Intent(getActivity(), MyProductActivity.class).putExtra("type", 1));
                }else {
                    startLoginActivity();
                }
                break;
            case R.id.mine_produce:
                if(Account.isLogin()){
                    startActivity(new Intent(getActivity(), MyProductActivity.class).putExtra("type", 2));
                }else {
                    startLoginActivity();
                }
                break;
            case R.id.mine_clear:
                loadDialog.show();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        loadDialog.dismiss();
                        toast.setText("已清空");
                        toast.show();
                    }
                }, (int)((Math.random() * 10) % 3 + 1) * 1000);
                break;
            case R.id.mine_app:
                startActivity(new Intent(getContext(), AboutAppActivity.class));
                break;
            case R.id.title_right:
                if(Account.isLogin()){
                    startActivity(new Intent(getContext(), MyActivity.class).putExtra("BusinessResponse", mResponse));
                }else {
                    startLoginActivity();
                }
                break;
            default:
                break;
        }
    }

    private void startLoginActivity() {
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        startActivity(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
