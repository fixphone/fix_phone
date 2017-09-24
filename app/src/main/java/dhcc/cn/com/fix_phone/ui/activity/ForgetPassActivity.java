package dhcc.cn.com.fix_phone.ui.activity;

import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.OnClick;
import dhcc.cn.com.fix_phone.R;
import dhcc.cn.com.fix_phone.base.BaseActivity;
import dhcc.cn.com.fix_phone.ui.widget.MyCountDownTimer;

/**
 * Created by Administrator on 2017/9/24 0024.
 *
 */

public class ForgetPassActivity extends BaseActivity{

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
        title_name.setText("密码找回");
        eye_state.setTag(IMG_TAG_HIDE);
        myCountDownTimer = new MyCountDownTimer(120 * 1000, 1000, get_code_tv, this);
        toast = Toast.makeText(this, "", Toast.LENGTH_SHORT);
    }

    @OnClick({R.id.get_code_tv, R.id.eye_state, R.id.confirm_btn})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.get_code_tv:
                if (get_code_tv.getText().toString().equals("获取验证码")) {
                    myCountDownTimer.start();
                    getCode();
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

                break;
        }
    }

    private void getCode(){

    }
}
