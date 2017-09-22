package dhcc.cn.com.fix_phone.ui.activity;

import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.OnClick;
import dhcc.cn.com.fix_phone.R;
import dhcc.cn.com.fix_phone.base.BaseActivity;

/**
 * Created by songyang on 2017\9\22 0022.
 */

public class ResetPassWordActivity extends BaseActivity{

    private static final String IMG_TAG_HIDE = "hide";
    private static final String IMG_TAG_SHOW = "show";

    @BindView(R.id.title_name)
    TextView title_name;
    @BindView(R.id.old_eye_state)
    ImageView old_eye_state;
    @BindView(R.id.eye_state)
    ImageView eye_state;
    @BindView(R.id.old_reset_pass_word)
    EditText old_reset_pass_word;
    @BindView(R.id.reset_pass_word)
    EditText reset_pass_word;


    @Override
    public int getLayoutId() {
        return R.layout.activity_reset_pass;
    }

    @Override
    protected void initEvent() {
        super.initEvent();
        title_name.setText("修改密码");
        old_eye_state.setTag(IMG_TAG_HIDE);
        eye_state.setTag(IMG_TAG_HIDE);
    }

    @OnClick({R.id.title_back, R.id.old_eye_state, R.id.eye_state, R.id.reset_confirm})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.title_back:
                finish();
                break;
            case R.id.old_eye_state:
                if (old_eye_state.getTag().equals(IMG_TAG_HIDE)) {
                    old_eye_state.setImageDrawable(getResources().getDrawable(R.drawable.login_eyeopen));
                    old_eye_state.setTag(IMG_TAG_SHOW);
                    old_reset_pass_word.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    old_eye_state.setImageDrawable(getResources().getDrawable(R.drawable.login_eyeclose));
                    old_eye_state.setTag(IMG_TAG_HIDE);
                    old_reset_pass_word.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
                old_reset_pass_word.setSelection(old_reset_pass_word.getText().toString().length());
                break;
            case R.id.eye_state:
                if (eye_state.getTag().equals(IMG_TAG_HIDE)) {
                    eye_state.setImageDrawable(getResources().getDrawable(R.drawable.login_eyeopen));
                    eye_state.setTag(IMG_TAG_SHOW);
                    reset_pass_word.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    eye_state.setImageDrawable(getResources().getDrawable(R.drawable.login_eyeclose));
                    eye_state.setTag(IMG_TAG_HIDE);
                    reset_pass_word.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
                reset_pass_word.setSelection(reset_pass_word.getText().toString().length());
                break;
            case R.id.reset_confirm:

                break;
        }
    }
}
