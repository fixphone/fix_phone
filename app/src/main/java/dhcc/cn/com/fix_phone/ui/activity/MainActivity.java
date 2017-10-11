package dhcc.cn.com.fix_phone.ui.activity;

import android.content.Intent;
import android.support.design.widget.TabLayout;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import dhcc.cn.com.fix_phone.Account;
import dhcc.cn.com.fix_phone.R;
import dhcc.cn.com.fix_phone.base.BaseActivity;
import dhcc.cn.com.fix_phone.event.LoginEvent;
import dhcc.cn.com.fix_phone.remote.ApiManager;
import dhcc.cn.com.fix_phone.ui.fragment.CircleFragment;
import dhcc.cn.com.fix_phone.ui.fragment.ImFragment;
import dhcc.cn.com.fix_phone.ui.fragment.MeFragment;
import me.yokeyword.fragmentation.SupportFragment;
import me.yokeyword.fragmentation.SwipeBackLayout;

public class MainActivity extends BaseActivity implements TabLayout.OnTabSelectedListener {

    @BindView(R.id.tabLayout)
    TabLayout mTabLayout;

    private SupportFragment[] mFragments = new SupportFragment[3];

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void init() {
        super.init();
        mFragments[0] = ImFragment.newInstance();
        mFragments[1] = CircleFragment.newInstance();
        mFragments[2] = MeFragment.newInstance();
        getSwipeBackLayout().setEdgeOrientation(SwipeBackLayout.STATE_IDLE);
    }

    @Override
    protected void initView() {
        loadMultipleRootFragment(R.id.frameLayout_main, 1, mFragments);
        mTabLayout.getTabAt(1).select();
    }

    @Override
    protected void initEvent() {
        super.initEvent();
        mTabLayout.addOnTabSelectedListener(this);
        if(!Account.isLogin()){
            startActivity(new Intent(this, LoginActivity.class));
            return;
        }
        String refreshToken = Account.getLoginInfo().getRefreshToken();
        ApiManager.Instance().RefreshToken(refreshToken);
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        int position = tab.getPosition();
        showHideFragment(mFragments[position]);
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refreshTokenResult(LoginEvent loginEvent){
        if(loginEvent.loginResponse != null && loginEvent.loginResponse.FObject != null){
            Account.setAccessToken(Account.getUserId(), loginEvent.loginResponse.FObject.getAccessToken());
            ApiManager.Instance().getRongToken(loginEvent.loginResponse.FObject.accessToken);
        }
    }
}
