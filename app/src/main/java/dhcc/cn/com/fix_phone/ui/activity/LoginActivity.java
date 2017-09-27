package dhcc.cn.com.fix_phone.ui.activity;

import android.content.Intent;
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

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.OnClick;
import dhcc.cn.com.fix_phone.R;
import dhcc.cn.com.fix_phone.base.BaseActivity;
import dhcc.cn.com.fix_phone.event.LoginEvent;
import dhcc.cn.com.fix_phone.event.RegisterEvent;
import dhcc.cn.com.fix_phone.remote.ApiManager;
import dhcc.cn.com.fix_phone.utils.AMUtils;

/**
 * Created by Administrator on 2017/9/24 0024.
 *
 */

public class LoginActivity extends BaseActivity{

    private static final String TAG = "LoginActivity";
    public static final int REG_CODE = 0x0001;
    private static final String IMG_TAG_HIDE = "hide";
    private static final String IMG_TAG_SHOW = "show";

    @BindView(R.id.title_name)
    TextView title_name;
    @BindView(R.id.phone_num_et)
    EditText phone_num_et;
    @BindView(R.id.pass_word_et)
    EditText pass_word_et;
    @BindView(R.id.title_back_iv)
    ImageView title_back_iv;
    @BindView(R.id.eye_state)
    ImageView eye_state;

    private Toast toast;

    @Override
    public int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    protected void initEvent() {
        super.initEvent();
        title_name.setText("");
        title_back_iv.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.button_cancel_icon));
        eye_state.setTag(IMG_TAG_HIDE);
        toast = Toast.makeText(this, "", Toast.LENGTH_SHORT);
    }

    @OnClick({R.id.title_back_iv, R.id.eye_state, R.id.login_confirm, R.id.registration_tv, R.id.forget_pass})
    public void onClick(View view){
        switch (view.getId()){
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

    private void startActivity(Class clazz){
        Intent intent = new Intent(this, clazz);
        startActivityForResult(intent, REG_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REG_CODE){
            if(resultCode == RegistrationActivity.REG_SUCCESS){
                String phone = data.getStringExtra("phone");
                String passWord = data.getStringExtra("passWord");
                phone_num_et.setText(phone);
                pass_word_et.setText(passWord);
            }
        }
    }

    public void login(){
        String phone = phone_num_et.getText().toString();
        String psw = pass_word_et.getText().toString();
        if(!AMUtils.isMobile(phone)){
            toast.setText("请填写正确的手机号码");
            toast.show();
            return;
        }
        if(TextUtils.isEmpty(psw)){
            toast.setText("密码不能为空");
            toast.show();
            return;
        }
        ApiManager.Instance().login(phone, psw);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void login(LoginEvent loginEvent){
        Log.d(TAG, "getCode: " + loginEvent.loginResponse.FMsg);
    }
}
