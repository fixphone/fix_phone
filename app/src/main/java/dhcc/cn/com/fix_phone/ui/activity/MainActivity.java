package dhcc.cn.com.fix_phone.ui.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.design.widget.TabLayout;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import dhcc.cn.com.fix_phone.Account;
import dhcc.cn.com.fix_phone.R;
import dhcc.cn.com.fix_phone.base.BaseActivity;
import dhcc.cn.com.fix_phone.event.LoginEvent;
import dhcc.cn.com.fix_phone.event.RongTokenEvent;
import dhcc.cn.com.fix_phone.remote.Api;
import dhcc.cn.com.fix_phone.remote.ApiManager;
import dhcc.cn.com.fix_phone.rong.SealConst;
import dhcc.cn.com.fix_phone.rong.SealUserInfoManager;
import dhcc.cn.com.fix_phone.ui.fragment.CircleFragment;
import dhcc.cn.com.fix_phone.ui.fragment.ImFragment;
import dhcc.cn.com.fix_phone.ui.fragment.MeFragment;
import dhcc.cn.com.fix_phone.ui.widget.LoadDialog;
import dhcc.cn.com.fix_phone.utils.NLog;
import io.rong.imkit.RongIM;
import io.rong.imkit.manager.IUnReadMessageObserver;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.message.ContactNotificationMessage;
import me.yokeyword.fragmentation.SupportFragment;
import me.yokeyword.fragmentation.SwipeBackLayout;

public class MainActivity extends BaseActivity implements TabLayout.OnTabSelectedListener {

    @BindView(R.id.tabLayout)
    TabLayout mTabLayout;

    private SharedPreferences        sp;
    private SharedPreferences.Editor editor;
    private String                   connectResultId;

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
        sp = getSharedPreferences("config", MODE_PRIVATE);
        editor = sp.edit();
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
        EventBus.getDefault().register(this);
        getConversationPush();
        getPushMessage();

        if(!Account.isLogin()){
            startActivity(new Intent(this, LoginActivity.class));
            return;
        }
        String refreshToken = Account.getLoginInfo().getRefreshToken();
        ApiManager.Instance().RefreshToken(refreshToken);
        ApiManager.Instance().getRongToken(Account.getAccessToken());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        String refreshToken = Account.getLoginInfo().getRefreshToken();
        ApiManager.Instance().RefreshToken(refreshToken);
        ApiManager.Instance().getRongToken(Account.getAccessToken());
        ApiManager.Instance().getUserInfo(Account.getUserId());
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    private void getConversationPush() {
        if (getIntent() != null && getIntent().hasExtra("PUSH_CONVERSATIONTYPE") && getIntent().hasExtra("PUSH_TARGETID")) {
            final String conversationType = getIntent().getStringExtra("PUSH_CONVERSATIONTYPE");
            final String targetId = getIntent().getStringExtra("PUSH_TARGETID");
            RongIM.getInstance().getConversation(Conversation.ConversationType.valueOf(conversationType), targetId, new RongIMClient.ResultCallback<Conversation>() {
                @Override
                public void onSuccess(Conversation conversation) {
                    if (conversation != null) {
                        if (conversation.getLatestMessage() instanceof ContactNotificationMessage) { //好友消息的push
                            startActivity(new Intent(MainActivity.this, NewFriendListActivity.class));
                        } else {
                            Uri uri = Uri.parse("rong://" + getApplicationInfo().packageName).buildUpon().appendPath("conversation")
                                    .appendPath(conversationType).appendQueryParameter("targetId", targetId).build();
                            Intent intent = new Intent(Intent.ACTION_VIEW);
                            intent.setData(uri);
                            startActivity(intent);
                        }
                    }
                }

                @Override
                public void onError(RongIMClient.ErrorCode e) {

                }
            });
        }
    }

    private void getPushMessage() {
        Intent intent = getIntent();
        if (intent != null && intent.getData() != null && intent.getData().getScheme().equals("rong")) {
            String path = intent.getData().getPath();
            if (path.contains("push_message")) {
                SharedPreferences sharedPreferences = getSharedPreferences("config", MODE_PRIVATE);
                String cacheToken = sharedPreferences.getString("loginToken", "");
                if (TextUtils.isEmpty(cacheToken)) {
                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                } else {
                    if (!RongIM.getInstance().getCurrentConnectionStatus().equals(RongIMClient.ConnectionStatusListener.ConnectionStatus.CONNECTED)) {
                        LoadDialog.show(MainActivity.this);
                        RongIM.connect(cacheToken, new RongIMClient.ConnectCallback() {
                            @Override
                            public void onTokenIncorrect() {
                                LoadDialog.dismiss(MainActivity.this);
                            }

                            @Override
                            public void onSuccess(String s) {
                                LoadDialog.dismiss(MainActivity.this);
                            }

                            @Override
                            public void onError(RongIMClient.ErrorCode e) {
                                LoadDialog.dismiss(MainActivity.this);
                            }
                        });
                    }
                }
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refreshTokenResult(LoginEvent loginEvent){
        if(loginEvent.loginResponse != null && loginEvent.loginResponse.FObject != null){
            Account.setAccessToken(Account.getUserId(), loginEvent.loginResponse.FObject.getAccessToken());
            ApiManager.Instance().getRongToken(loginEvent.loginResponse.FObject.accessToken);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getToken(RongTokenEvent tokenEvent) {
        if (tokenEvent.tokenBody != null) {
            final String loginToken = tokenEvent.tokenBody.imToken;
            if (!TextUtils.isEmpty(loginToken)) {
                RongIM.connect(loginToken, new RongIMClient.ConnectCallback() {
                    @Override
                    public void onTokenIncorrect() {
                        NLog.e("connect", "onTokenIncorrect");
                    }

                    @Override
                    public void onSuccess(String s) {
                        connectResultId = s;
                        NLog.e("connect", "onSuccess userid:" + s);
                        editor.putString(SealConst.SEALTALK_LOGIN_ID, s);
                        editor.putString("loginToken", loginToken);
                        editor.apply();
                        SealUserInfoManager.getInstance().openDB();
                    }

                    @Override
                    public void onError(RongIMClient.ErrorCode errorCode) {
                        NLog.e("connect", "onError errorcode:" + errorCode.getValue());
                    }
                });
            }
        } else {
            Toast.makeText(this, "" + tokenEvent.errorMessage, Toast.LENGTH_SHORT).show();
        }
    }
}
