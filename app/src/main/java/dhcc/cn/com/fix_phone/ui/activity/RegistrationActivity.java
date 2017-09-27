package dhcc.cn.com.fix_phone.ui.activity;

import android.content.Intent;
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
import dhcc.cn.com.fix_phone.bean.RegisterRequest;
import dhcc.cn.com.fix_phone.event.RegisterEvent;
import dhcc.cn.com.fix_phone.event.TelCheckEvent;
import dhcc.cn.com.fix_phone.remote.ApiManager;
import dhcc.cn.com.fix_phone.ui.widget.MyCountDownTimer;
import dhcc.cn.com.fix_phone.utils.AMUtils;

/**
 * Created by Administrator on 2017/9/24 0024.
 *
 */

public class RegistrationActivity extends BaseActivity{

    private static final String TAG = "RegistrationActivity";

    public static final int REG_SUCCESS = 0x0003;
    private static final String IMG_TAG_HIDE = "hide";
    private static final String IMG_TAG_SHOW = "show";

    @BindView(R.id.title_name)
    TextView title_name;
    @BindView(R.id.phone_num_et)
    EditText phone_num_et;
    @BindView(R.id.code_et)
    EditText code_et;
    @BindView(R.id.get_code_tv)
    TextView get_code_tv;
    @BindView(R.id.pass_word_et)
    EditText pass_word_et;
    @BindView(R.id.eye_state)
    ImageView eye_state;

    private MyCountDownTimer myCountDownTimer;
    private Toast toast;

    @Override
    public int getLayoutId() {
        return R.layout.activity_registration;
    }

    @Override
    protected void initEvent() {
        super.initEvent();
        title_name.setText("注册");
        eye_state.setTag(IMG_TAG_HIDE);
        myCountDownTimer = new MyCountDownTimer(120 * 1000, 1000, get_code_tv, this);
        toast = Toast.makeText(this, "", Toast.LENGTH_SHORT);
    }

    @OnClick({R.id.title_back, R.id.get_code_tv, R.id.eye_state, R.id.confirm_btn})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.title_back:
                finish();
                break;
            case R.id.get_code_tv:
                if(!AMUtils.isMobile(phone_num_et.getText().toString())){
                    toast.setText("请填写正确的手机号码");
                    toast.show();
                    return;
                }
                if (get_code_tv.getText().toString().equals("获取验证码")) {
                    myCountDownTimer.start();
                    getCode(phone_num_et.getText().toString());
                } else {
                    toast.setText("验证码已发送，请注意查收");
                    toast.show();
                }
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
            case R.id.confirm_btn:
                registration(phone_num_et.getText().toString(), code_et.getText().toString(), pass_word_et.getText().toString());
                break;
        }
    }

    private void getCode(String phoneNum){
        ApiManager.Instance().getVerificationCodeResponse(phoneNum);
    }

    private void registration(String phoneNum, String code, String passWord){
        if(!AMUtils.isMobile(phoneNum)){
            toast.setText("请填写正确的手机号码");
            toast.show();
            return;
        }
        if(TextUtils.isEmpty(code)){
            toast.setText("请填写验证码");
            toast.show();
            return;
        }
        if(TextUtils.isEmpty(passWord)){
            toast.setText("密码不能为空");
            toast.show();
            return;
        }
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.phone = phoneNum;
        registerRequest.postCode = code;
        registerRequest.pwd = passWord;
//        ApiManager.Instance().register(phoneNum,passWord);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getCode(TelCheckEvent telCheckEvent){
        Log.d(TAG, "getCode: " + telCheckEvent.telCheckResponse.FMsg);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getCode(RegisterEvent registerEvent){
        Log.d(TAG, "getCode: " + registerEvent.registerResponse.FMsg);
        Intent intent = getIntent();
        intent.putExtra("phone", phone_num_et.getText().toString());
        intent.putExtra("passWord", pass_word_et.getText().toString());
        setResult(REG_SUCCESS, intent);
    }
}
