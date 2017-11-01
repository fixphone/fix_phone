package dhcc.cn.com.fix_phone.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
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
import dhcc.cn.com.fix_phone.remote.ApiManager;
import dhcc.cn.com.fix_phone.ui.widget.LoadDialog;
import dhcc.cn.com.fix_phone.utils.AMUtils;
import dhcc.cn.com.fix_phone.utils.MD5;

/**
 * Created by Administrator on 2017/9/24 0024.
 */

public class LoginActivity extends RongBaseActivity {
    private static final String TAG = "LoginActivity";
    public static final  int    LOGIN_BACK_CODE = 0x0010;
    public static final  int    REG_CODE        = 0x0001;
    private static final String IMG_TAG_HIDE    = "hide";
    private static final String IMG_TAG_SHOW    = "show";

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

    private Toast      toast;
    private String     phoneString;
    private String     passwordString;
    private LoadDialog loadDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setHeadVisibility(View.GONE);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        title_name.setText("登录");
        title_back_iv.setVisibility(View.GONE);
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
                onBackPressedSupport();
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
            default:
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
        if (loginEvent.loginResponse != null && loginEvent.loginResponse.FObject != null && loginEvent.loginResponse.FIsSuccess) {
            Account.setUserId(loginEvent.loginResponse.FObject.userID);
            Account.setLoginInfo(loginEvent.loginResponse.FObject);
            Account.setLogin(true);
            goToMain();
        } else {
            toast.setText(loginEvent.errorMessage);
            toast.show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onBackPressedSupport() {
        setResult(LOGIN_BACK_CODE, getIntent());
        super.onBackPressedSupport();
    }

    private void goToMain() {
        startActivity(new Intent(LoginActivity.this, MainActivity.class));
        finish();
    }
}
