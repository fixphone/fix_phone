package dhcc.cn.com.fix_phone.ui.fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.OnClick;
import dhcc.cn.com.fix_phone.R;
import dhcc.cn.com.fix_phone.base.BaseFragment;
import dhcc.cn.com.fix_phone.bean.BusinessResponse;
import dhcc.cn.com.fix_phone.event.BusinessEvent;
import dhcc.cn.com.fix_phone.remote.ApiManager;
import dhcc.cn.com.fix_phone.rong.SealConst;
import dhcc.cn.com.fix_phone.ui.activity.AboutAppActivity;
import dhcc.cn.com.fix_phone.ui.activity.FeedBackActivity;
import dhcc.cn.com.fix_phone.ui.activity.MineCirCleActivity;
import dhcc.cn.com.fix_phone.ui.activity.MyActivity;
import dhcc.cn.com.fix_phone.ui.activity.MyProductActivity;
import dhcc.cn.com.fix_phone.ui.activity.PersonInfoActivity;
import dhcc.cn.com.fix_phone.ui.activity.VipActivity;
import dhcc.cn.com.fix_phone.ui.widget.RoundImageView;
import io.rong.imkit.RongIM;
import io.rong.imlib.model.UserInfo;

/**
 * 2017/9/16 23
 */
public class MeFragment extends BaseFragment {

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
    }

    @Override
    protected void initData() {
        ApiManager.Instance().getUserInfo("");
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onUserInfo(BusinessEvent event) {
        mResponse = event.mResponse;
        user_name.setText(mResponse.FObject.name);
        user_mobile.setText(mResponse.FObject.phone);
        Glide.with(getContext()).load(mResponse.FObject.headUrl).diskCacheStrategy(DiskCacheStrategy.ALL).into(user_icon);
        RongIM.getInstance().setCurrentUserInfo(new UserInfo(sp.getString(SealConst.SEALTALK_LOGIN_ID, ""), mResponse.FObject.name, Uri.parse(mResponse.FObject.headUrl)));
    }

    @OnClick({R.id.mine_info, R.id.mine_circle, R.id.mine_house, R.id.mine_suggest, R.id.mine_vip,
            R.id.mine_advert, R.id.mine_produce, R.id.mine_clear, R.id.mine_app, R.id.title_right})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.mine_info:
                Intent intent = new Intent(getContext(), PersonInfoActivity.class);
                intent.putExtra("BusinessResponse", mResponse);
                startActivity(intent);
                break;
            case R.id.mine_circle:
                startActivity(new Intent(getActivity(), MineCirCleActivity.class).putExtra("title", "我的圈子").
                        putExtra("resource_type", 1));
                break;
            case R.id.mine_house:
                startActivity(new Intent(getActivity(), MineCirCleActivity.class).putExtra("title", "我的收藏").
                        putExtra("resource_type", 2));
                break;
            case R.id.mine_suggest:
                startActivity(FeedBackActivity.class);
                break;
            case R.id.mine_vip:
                startActivity(VipActivity.class);
                break;
            case R.id.mine_advert:
                startActivity(new Intent(getActivity(), MyProductActivity.class).putExtra("type", 1));
                break;
            case R.id.mine_produce:
                startActivity(new Intent(getActivity(), MyProductActivity.class).putExtra("type", 2));
                break;
            case R.id.mine_clear:

                break;
            case R.id.mine_app:
                startActivity(AboutAppActivity.class);
                break;
            case R.id.title_right:
                startActivity(MyActivity.class);
                break;
            default:
                break;
        }
    }

    private void startActivity(Class clazz) {
        Intent intent = new Intent(getActivity(), clazz);
        startActivity(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
