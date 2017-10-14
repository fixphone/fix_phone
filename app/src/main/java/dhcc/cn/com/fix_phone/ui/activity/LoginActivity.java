package dhcc.cn.com.fix_phone.ui.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dhcc.cn.com.fix_phone.Account;
import dhcc.cn.com.fix_phone.R;
import dhcc.cn.com.fix_phone.base.RongBaseActivity;
import dhcc.cn.com.fix_phone.event.LoginEvent;
import dhcc.cn.com.fix_phone.event.RongTokenEvent;
import dhcc.cn.com.fix_phone.event.TokenEvent;
import dhcc.cn.com.fix_phone.remote.ApiManager;
import dhcc.cn.com.fix_phone.rong.SealConst;
import dhcc.cn.com.fix_phone.rong.SealUserInfoManager;
import dhcc.cn.com.fix_phone.rong.network.http.HttpException;
import dhcc.cn.com.fix_phone.rong.response.GetUserInfoByIdResponse;
import dhcc.cn.com.fix_phone.ui.widget.LoadDialog;
import dhcc.cn.com.fix_phone.utils.AMUtils;
import dhcc.cn.com.fix_phone.utils.CommonUtils;
import dhcc.cn.com.fix_phone.utils.MD5;
import dhcc.cn.com.fix_phone.utils.NLog;
import dhcc.cn.com.fix_phone.utils.NToast;
import dhcc.cn.com.fix_phone.utils.RongGenerate;
import dhcc.cn.com.fix_phone.utils.SHA1;
import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.UserInfo;

/**
 * Created by Administrator on 2017/9/24 0024.
 */

public class LoginActivity extends RongBaseActivity {

    private static final String TAG            = "LoginActivity";
    public static final  int    REG_CODE       = 0x0001;
    private static final String IMG_TAG_HIDE   = "hide";
    private static final String IMG_TAG_SHOW   = "show";
    private static final int    LOGIN          = 5;
    private static final int    GET_TOKEN      = 6;
    private static final int    SYNC_USER_INFO = 9;

    @BindView(R.id.title_name)
    TextView  title_name;
    @BindView(R.id.phone_num_et)
    EditText  phone_num_et;
    @BindView(R.id.pass_word_et)
    EditText  pass_word_et;
    @BindView(R.id.title_back_iv)
    ImageView title_back_iv;
    @BindView(R.id.eye_state)
    ImageView eye_state;

    private Toast                    toast;
    private SharedPreferences        sp;
    private SharedPreferences.Editor editor;
    private String                   loginToken;
    private String                   phoneString;
    private String                   passwordString;
    private String                   connectResultId;
    private LoadDialog               loadDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setHeadVisibility(View.GONE);
        ButterKnife.bind(this);
        sp = getSharedPreferences("config", MODE_PRIVATE);
        editor = sp.edit();
        EventBus.getDefault().register(this);
        title_name.setText("");
        title_back_iv.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.button_cancel_icon));
        eye_state.setTag(IMG_TAG_HIDE);
        toast = Toast.makeText(this, "", Toast.LENGTH_SHORT);
        String phone = Account.getLoginInfo() == null ? "" : Account.getLoginInfo().getPhone();
        phone_num_et.setText(phone);
        loadDialog = new LoadDialog(this, false, "");
    }

    @OnClick({R.id.title_back_iv, R.id.eye_state, R.id.login_confirm, R.id.registration_tv, R.id.forget_pass})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.title_back_iv:
                finish();
                break;
            case R.id.eye_state:
                if (eye_state.getTag().equals(IMG_TAG_HIDE)) {
                    eye_state.setImageDrawable(getResources().getDrawable(R.drawable.login_eyeopen));
                    eye_state.setTag(IMG_TAG_SHOW);
                    pass_word_et.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    eye_state.setImageDrawable(getResources().getDrawable(R.drawable.login_eyeclose));
                    eye_state.setTag(IMG_TAG_HIDE);
                    pass_word_et.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
                pass_word_et.setSelection(pass_word_et.getText().toString().length());
                break;
            case R.id.login_confirm:
                login();
                break;
            case R.id.registration_tv:
                startActivity(RegistrationActivity.class);
                break;
            case R.id.forget_pass:
                startActivity(ForgetPassActivity.class);
                break;
        }
    }

    private void startActivity(Class clazz) {
        Intent intent = new Intent(this, clazz);
        startActivityForResult(intent, REG_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REG_CODE) {
            if (resultCode == RegistrationActivity.REG_SUCCESS) {
                String phone = data.getStringExtra("phone");
                String passWord = data.getStringExtra("passWord");
                phone_num_et.setText(phone);
                pass_word_et.setText(passWord);
            }
        }
    }

    public void login() {
        phoneString = phone_num_et.getText().toString();
        passwordString = pass_word_et.getText().toString();
        if (!AMUtils.isMobile(phoneString)) {
            toast.setText("请填写正确的手机号码");
            toast.show();
            return;
        }
        if (TextUtils.isEmpty(passwordString)) {
            toast.setText("密码不能为空");
            toast.show();
            return;
        }
        ApiManager.Instance().login(phoneString, MD5.encrypt(passwordString));
        loadDialog.show();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void loginResult(LoginEvent loginEvent) {
        loadDialog.dismiss();
        if (loginEvent.loginResponse != null && loginEvent.loginResponse.FObject != null) {
            Account.setUserId(loginEvent.loginResponse.FObject.userID);
            Account.setLoginInfo(loginEvent.loginResponse.FObject);
            Account.setLogin(true);
//            ApiManager.Instance().getRongToken(loginEvent.loginResponse.FObject.accessToken);
            String appKey = Account.APP_KEY;
            String appSecret = Account.APP_SECRET;
            String nonce = Math.random() + "";
            String timestamp =  System.currentTimeMillis() + "";
            ApiManager.Instance().getToken(appKey, nonce, timestamp, SHA1.shaEncrypt(appSecret + nonce + timestamp), loginEvent.loginResponse.FObject.userID,
                    "MagicRing", "http://120.77.202.151:8080/OSS/GetImage?FileName=UserIcon/20171014/4be20c3f-e7b6-48da-9c05-22a0423bbfc9.jpg");
        } else {
            toast.setText(loginEvent.errorMessage);
            toast.show();
        }
    }

//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void getToken(RongTokenEvent tokenEvent) {
//        if (tokenEvent.tokenBody != null) {
//            Log.d(TAG, "getToken: " + tokenEvent.tokenBody.imToken);
//            loginToken = tokenEvent.tokenBody.imToken;
//            if (!TextUtils.isEmpty(loginToken)) {
//                RongIM.connect(loginToken, new RongIMClient.ConnectCallback() {
//                    @Override
//                    public void onTokenIncorrect() {
//                        NLog.e("connect", "onTokenIncorrect");
//                    }
//
//                    @Override
//                    public void onSuccess(String s) {
//                        connectResultId = s;
//                        NLog.e("connect", "onSuccess userid:" + s);
//                        editor.putString(SealConst.SEALTALK_LOGIN_ID, s);
//                        editor.apply();
//                        SealUserInfoManager.getInstance().openDB();
//                        request(SYNC_USER_INFO, true);
//                    }
//
//                    @Override
//                    public void onError(RongIMClient.ErrorCode errorCode) {
//                        NLog.e("connect", "onError errorcode:" + errorCode.getValue());
//                    }
//                });
//            }
//            goToMain();
//        } else {
//            Toast.makeText(mContext, "" + tokenEvent.errorMessage, Toast.LENGTH_SHORT).show();
//        }
//    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getToken(TokenEvent tokenEvent) {
        if (tokenEvent.tokenResponse!= null) {
            if(tokenEvent.tokenResponse.code == 200){
                Log.d(TAG, "getToken: " + tokenEvent.tokenResponse.code);
                loginToken = tokenEvent.tokenResponse.token;
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
                            editor.apply();
                            SealUserInfoManager.getInstance().openDB();
                            request(SYNC_USER_INFO, true);
                        }

                        @Override
                        public void onError(RongIMClient.ErrorCode errorCode) {
                            NLog.e("connect", "onError errorcode:" + errorCode.getValue());
                        }
                    });
                }
                goToMain();
            }else {
                Log.d(TAG, "getToken: code = " + tokenEvent.tokenResponse.code);
            }
        } else {
            Toast.makeText(mContext, "" + tokenEvent.errorMessage, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public Object doInBackground(int requestCode, String id) throws HttpException {
        switch (requestCode) {
            case SYNC_USER_INFO:
                return action.getUserInfoById(connectResultId);
        }
        return null;
    }

    @Override
    public void onSuccess(int requestCode, Object result) {
        if (result != null) {
            switch (requestCode) {
                case SYNC_USER_INFO:
                    GetUserInfoByIdResponse userInfoByIdResponse = (GetUserInfoByIdResponse) result;
                    if (userInfoByIdResponse.getCode() == 200) {
                        if (TextUtils.isEmpty(userInfoByIdResponse.getResult().getPortraitUri())) {
                            userInfoByIdResponse.getResult().setPortraitUri(RongGenerate.generateDefaultAvatar(userInfoByIdResponse.getResult().getNickname(), userInfoByIdResponse.getResult().getId()));
                        }
                        String nickName = userInfoByIdResponse.getResult().getNickname();
                        String portraitUri = userInfoByIdResponse.getResult().getPortraitUri();
                        editor.putString(SealConst.SEALTALK_LOGIN_NAME, nickName);
                        editor.putString(SealConst.SEALTALK_LOGING_PORTRAIT, portraitUri);
                        editor.apply();
                        RongIM.getInstance().refreshUserInfoCache(new UserInfo(connectResultId, nickName, Uri.parse(portraitUri)));
                    }
                    //不继续在login界面同步好友,群组,群组成员信息
                    SealUserInfoManager.getInstance().getAllUserInfo();
                    goToMain();
                    break;
            }
        }
    }

    @Override
    public void onFailure(int requestCode, int state, Object result) {
        if (!CommonUtils.isNetworkConnected(mContext)) {
            LoadDialog.dismiss(mContext);
            NToast.shortToast(mContext, "网络不可用");
            return;
        }
        switch (requestCode) {
            case SYNC_USER_INFO:
                LoadDialog.dismiss(mContext);
                NToast.shortToast(mContext, "同步用户信息接口请求失败");
                break;
        }
    }

    private void goToMain() {
        editor.putString("loginToken", loginToken);
        editor.putString(SealConst.SEALTALK_LOGING_PHONE, phoneString);
        editor.putString(SealConst.SEALTALK_LOGING_PASSWORD, passwordString);
        editor.apply();
        LoadDialog.dismiss(mContext);
        startActivity(new Intent(LoginActivity.this, MainActivity.class));
        finish();
    }
}
