package dhcc.cn.com.fix_phone.ui.activity;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.OnClick;
import dhcc.cn.com.fix_phone.R;
import dhcc.cn.com.fix_phone.base.BaseActivity;

/**
 * Created by songyang on 2017\9\18 0018.
 *
 */

public class PersonInfoActivity extends BaseActivity {

    public static final String ACTION_NAME = "公司名称";
    public static final String ACTION_CONTACT = "联系人";
    public static final String ACTION_POSTCODE = "邮编";
    public static final String ACTION_PHONE = "手机";
    public static final String ACTION_MOBILE_PHONE = "电话";
    public static final String ACTION_ADDRESS = "经营地址";
    public static final String ACTION_PROFILE = "公司简介";
    public static final int ACTION_NAME_CODE = 1;
    public static final int ACTION_CONTACT_CODE = 2;
    public static final int ACTION_POSTCODE_CODE = 3;
    public static final int ACTION_PHONE_CODE = 4;
    public static final int ACTION_MOBILE_PHONE_CODE = 5;
    public static final int ACTION_ADDRESS_CODE = 6;
    public static final int ACTION_PROFILE_CODE = 7;

    @BindView(R.id.title_name)
    TextView title_name;
    @BindView(R.id.info_company_name_tv)
    TextView info_company_name_tv;
    @BindView(R.id.info_contact_name_tv)
    TextView info_contact_name_tv;
    @BindView(R.id.info_postcode_tv)
    TextView info_postcode_tv;
    @BindView(R.id.info_mobile_phone_tv)
    TextView info_mobile_phone_tv;
    @BindView(R.id.info_phone_num_tv)
    TextView info_phone_num_tv;
    @BindView(R.id.info_address_tv)
    TextView info_address_tv;
    @BindView(R.id.info_company_profile_tv)
    TextView info_company_profile_tv;

    @Override
    public int getLayoutId() {
        return R.layout.activity_person_info;
    }

    @Override
    protected void initEvent() {
        super.initEvent();
        title_name.setText("店铺资料");
    }

    @OnClick({R.id.title_back, R.id.info_company_name_rl, R.id.info_contact_name_rl, R.id.info_postcode_rl,
            R.id.info_mobile_phone_rl, R.id.info_phone_num_rl, R.id.info_address_rl, R.id.info_company_profile_rl})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.title_back:
                finish();
                break;
            case R.id.info_company_name_rl:
                startActivity(ResetPersonInfoActivity.class, ACTION_NAME, ACTION_NAME_CODE, info_company_name_tv.getText().toString());
                break;
            case R.id.info_contact_name_rl:
                startActivity(ResetPersonInfoActivity.class, ACTION_CONTACT, ACTION_CONTACT_CODE, info_contact_name_tv.getText().toString());
                break;
            case R.id.info_postcode_rl:
                startActivity(ResetPersonInfoActivity.class, ACTION_POSTCODE, ACTION_POSTCODE_CODE, info_postcode_tv.getText().toString());
                break;
            case R.id.info_mobile_phone_rl:
                startActivity(ResetPersonInfoActivity.class, ACTION_PHONE, ACTION_PHONE_CODE, info_phone_num_tv.getText().toString());
                break;
            case R.id.info_phone_num_rl:
                startActivity(ResetPersonInfoActivity.class, ACTION_MOBILE_PHONE, ACTION_MOBILE_PHONE_CODE, info_mobile_phone_tv.getText().toString());
                break;
            case R.id.info_address_rl:
                startActivity(ResetPersonInfoActivity.class, ACTION_ADDRESS, ACTION_ADDRESS_CODE, info_address_tv.getText().toString());
                break;
            case R.id.info_company_profile_rl:
                startActivity(ResetPersonInfoActivity.class, ACTION_PROFILE, ACTION_PROFILE_CODE, info_company_profile_tv.getText().toString());
                break;
            default:
                break;
        }
    }

    private void startActivity(Class clazz, String action, int code, String content){
        Intent intent = new Intent(this, clazz);
        intent.putExtra("Action", action);
        intent.putExtra("Content", content);
        startActivityForResult(intent, code);
    }
}
