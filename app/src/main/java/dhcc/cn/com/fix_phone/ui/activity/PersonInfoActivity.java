package dhcc.cn.com.fix_phone.ui.activity;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.OnClick;
import dhcc.cn.com.fix_phone.R;
import dhcc.cn.com.fix_phone.base.BaseActivity;
import dhcc.cn.com.fix_phone.bean.BusinessResponse;

/**
 * Created by songyang on 2017\9\18 0018.
 *
 */

public class PersonInfoActivity extends BaseActivity {

    public static final String ACTION_NAME = "公司名称";
    public static final String ACTION_CONTACT = "联系人";
    public static final String ACTION_POSTCODE = "邮编";
    public static final String ACTION_PHONE = "电话";
    public static final String ACTION_MOBILE_PHONE = "手机";
    public static final String ACTION_ADDRESS = "经营地址";
    public static final String ACTION_PROFILE = "企业简介";
    public static final int ACTION_REQUEST_CODE = 0x0010;

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

    private BusinessResponse mResponse;

    @Override
    public int getLayoutId() {
        return R.layout.activity_person_info;
    }

    @Override
    protected void initEvent() {
        super.initEvent();
        title_name.setText("店铺资料");
    }

    @Override
    protected void initData() {
        super.initData();
        if(getIntent() != null && getIntent().hasExtra("BusinessResponse")){
            mResponse = (BusinessResponse)getIntent().getSerializableExtra("BusinessResponse");
            if(mResponse != null)
            setViewState(mResponse);
        }
    }

    private void setViewState(BusinessResponse mResponse){
        info_company_name_tv.setText(setText(mResponse.FObject.companyName));
        info_contact_name_tv.setText(setText(mResponse.FObject.contact));
        info_postcode_tv.setText(setText(mResponse.FObject.postCode));
        info_mobile_phone_tv.setText(setText(mResponse.FObject.contactMobile));
        info_phone_num_tv.setText(setText(mResponse.FObject.contactPhone));
        info_address_tv.setText(setText(mResponse.FObject.address));
        info_company_profile_tv.setText(setText(mResponse.FObject.companyProfile));
    }

    private String setText(String s){
        if(TextUtils.isEmpty(s)){
            return "";
        }
        return s;
    }

    @OnClick({R.id.title_back, R.id.info_company_name_rl, R.id.info_contact_name_rl, R.id.info_postcode_rl,
            R.id.info_mobile_phone_rl, R.id.info_phone_num_rl, R.id.info_address_rl, R.id.info_company_profile_rl})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.title_back:
                finish();
                break;
            case R.id.info_company_name_rl:
                startActivity(ResetPersonInfoActivity.class, ACTION_NAME, info_company_name_tv.getText().toString());
                break;
            case R.id.info_contact_name_rl:
                startActivity(ResetPersonInfoActivity.class, ACTION_CONTACT, info_contact_name_tv.getText().toString());
                break;
            case R.id.info_postcode_rl:
                startActivity(ResetPersonInfoActivity.class, ACTION_POSTCODE, info_postcode_tv.getText().toString());
                break;
            case R.id.info_mobile_phone_rl:
                startActivity(ResetPersonInfoActivity.class, ACTION_MOBILE_PHONE, info_mobile_phone_tv.getText().toString());
                break;
            case R.id.info_phone_num_rl:
                startActivity(ResetPersonInfoActivity.class, ACTION_PHONE, info_phone_num_tv.getText().toString());
                break;
            case R.id.info_address_rl:
                startActivity(ResetPersonInfoActivity.class, ACTION_ADDRESS, info_address_tv.getText().toString());
                break;
            case R.id.info_company_profile_rl:
                startActivity(ResetPersonInfoActivity.class, ACTION_PROFILE, info_company_profile_tv.getText().toString());
                break;
            default:
                break;
        }
    }

    private void startActivity(Class clazz, String action, String content){
        Intent intent = new Intent(this, clazz);
        intent.putExtra("Action", action);
        intent.putExtra("Content", content);
        intent.putExtra("Bean", mResponse);
        startActivityForResult(intent, ACTION_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == ACTION_REQUEST_CODE){
            if(resultCode == ResetPersonInfoActivity.RESULT_CODE){
                mResponse = (BusinessResponse)data.getSerializableExtra("resp");
                setViewState(mResponse);
            }
        }
    }
}
