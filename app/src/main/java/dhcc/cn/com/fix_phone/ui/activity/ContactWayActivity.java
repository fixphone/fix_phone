package dhcc.cn.com.fix_phone.ui.activity;

import android.content.Intent;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.OnClick;
import dhcc.cn.com.fix_phone.R;
import dhcc.cn.com.fix_phone.base.BaseActivity;
import dhcc.cn.com.fix_phone.bean.BusinessResponse;
import io.rong.imkit.RongIM;

/**
 * 2017/10/6 11
 */
public class ContactWayActivity extends BaseActivity {

    @BindView(R.id.toolbar_title)
    TextView  mToolbarTitle;
    @BindView(R.id.imageView_communication)
    ImageView mImageViewCommunication;
    @BindView(R.id.toolbar)
    Toolbar   mToolbar;
    @BindView(R.id.info_company_name_tv)
    TextView  mInfoCompanyNameTv;
    @BindView(R.id.info_contact_name_tv)
    TextView  mInfoContactNameTv;
    @BindView(R.id.info_postcode_tv)
    TextView  mInfoPostcodeTv;
    @BindView(R.id.info_mobile_phone_tv)
    TextView  mInfoMobilePhoneTv;
    @BindView(R.id.info_phone_num_tv)
    TextView  mInfoPhoneNumTv;
    @BindView(R.id.info_company_profile_tv)
    TextView  mInfoCompanyProfileTv;

    private BusinessResponse mBusiness;
    private String           mweChatId;
    private String           mName;

    @Override
    protected void init() {
        Intent intent = getIntent();
        mBusiness = (BusinessResponse) intent.getSerializableExtra("mResponse");
        mName = intent.getStringExtra("name");
        mweChatId = intent.getStringExtra("weChatId");

    }

    @Override
    protected void initView() {
        if (mBusiness != null && mBusiness.FObject != null) {
            mToolbarTitle.setText(mBusiness.FObject.name);
            mInfoCompanyNameTv.setText(mBusiness.FObject.companyName);
            mInfoContactNameTv.setText(mBusiness.FObject.contact);
            mInfoPhoneNumTv.setText(mBusiness.FObject.contactPhone);
            mInfoMobilePhoneTv.setText(mBusiness.FObject.contactMobile);
            mInfoPostcodeTv.setText(mBusiness.FObject.postCode);
            mInfoCompanyProfileTv.setText(mBusiness.FObject.address);
        }
    }

    @Override
    protected void initEvent() {
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        mImageViewCommunication.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    try {
                        RongIM.getInstance().startPrivateChat(ContactWayActivity.this, mweChatId, mName);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_contact_way;
    }

    @OnClick({R.id.textView_circle, R.id.textView_home})
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.textView_circle:
                finish();
                break;
            case R.id.textView_home:
                startActivity(new Intent(ContactWayActivity.this, MainActivity.class));
                break;
        }
    }

}
