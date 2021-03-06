package dhcc.cn.com.fix_phone.ui.activity;

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
import butterknife.OnClick;
import dhcc.cn.com.fix_phone.R;
import dhcc.cn.com.fix_phone.base.BaseActivity;
import dhcc.cn.com.fix_phone.event.FindPswEvent;
import dhcc.cn.com.fix_phone.event.TelCheckEvent;
import dhcc.cn.com.fix_phone.remote.ApiManager;
import dhcc.cn.com.fix_phone.ui.widget.LoadDialog;
import dhcc.cn.com.fix_phone.ui.widget.MyCountDownTimer;
import dhcc.cn.com.fix_phone.utils.AMUtils;
import dhcc.cn.com.fix_phone.utils.MD5;

/**
 * Created by Administrator on 2017/9/24 0024.
 *
 */

public class ForgetPassActivity extends BaseActivity{

    private static final String TAG = "ForgetPassActivity";
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
    private LoadDialog loadDialog;

    @Override
    public int getLayoutId() {
        Log.d(TAG, "getLayoutId: ");
        return R.layout.activity_registration;
    }

    @Override
    protected void initEvent() {
        super.initEvent();
        EventBus.getDefault().register(this);
        title_name.setText("密码找回");
        eye_state.setTag(IMG_TAG_HIDE);
        myCountDownTimer = new MyCountDownTimer(120 * 1000, 1000, get_code_tv, this);
        toast = Toast.makeText(this, "", Toast.LENGTH_SHORT);
        loadDialog = new LoadDialog(this, false, "");
    }

    @OnClick({R.id.get_code_tv, R.id.eye_state, R.id.confirm_btn})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.get_code_tv:
                if (!AMUtils.isMobile(phone_num_et.getText().toString())) {
                    toast.setText("请填写正确的手机号码");
                    toast.show();
                    return;
                }
                if (get_code_tv.getText().toString().equals("获取验证码")) {
                    myCountDownTimer.start();
                    ApiManager.Instance().sendChangePwdPhoneCode(phone_num_et.getText().toString());
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
                findPsw(phone_num_et.getText().toString(), code_et.getText().toString(), pass_word_et.getText().toString());
                break;
        }
    }

    private void findPsw(String phoneNum, String code, String passWord) {
        if (!AMUtils.isMobile(phoneNum)) {
            toast.setText("请填写正确的手机号码");
            toast.show();
            return;
        }
        if (TextUtils.isEmpty(code)) {
            toast.setText("请填写验证码");
            toast.show();
            return;
        }
        if (TextUtils.isEmpty(passWord)) {
            toast.setText("密码不能为空");
            toast.show();
            return;
        }
        loadDialog.show();
        ApiManager.Instance().ChangePwdByCode(phoneNum, code, MD5.encrypt(passWord));
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getCode(TelCheckEvent telCheckEvent) {
        Log.d(TAG, "getCode: " + telCheckEvent.telCheckResponse.FMsg);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void findPsw(FindPswEvent findPswEvent) {
        loadDialog.dismiss();
        if(findPswEvent.telCheckResponse != null){
            if(findPswEvent.telCheckResponse.FIsSuccess){
                toast.setText("密码已找回");
                toast.show();
                finish();
            }else {
                toast.setText(findPswEvent.telCheckResponse.FMsg);
                toast.show();
            }
        }else {
            toast.setText(findPswEvent.errorMessage);
            toast.show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
