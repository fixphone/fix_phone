package dhcc.cn.com.fix_phone.ui.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.OnClick;
import dhcc.cn.com.fix_phone.Account;
import dhcc.cn.com.fix_phone.R;
import dhcc.cn.com.fix_phone.base.BaseActivity;
import dhcc.cn.com.fix_phone.bean.BusinessResponse;
import dhcc.cn.com.fix_phone.event.BusinessEvent;
import dhcc.cn.com.fix_phone.remote.ApiManager;
import dhcc.cn.com.fix_phone.rong.SealConst;
import dhcc.cn.com.fix_phone.ui.widget.LoadDialog;
import io.rong.imkit.RongIM;
import io.rong.imlib.model.UserInfo;

/**
 * Created by Administrator on 2017/9/21 0021.
 *
 */

public class ResetPersonInfoActivity extends BaseActivity{

    public static final int RESULT_CODE = 0x0006;

    @BindView(R.id.title_name)
    TextView title_name;
    @BindView(R.id.title_right)
    TextView title_right;
    @BindView(R.id.content_tv)
    TextView content_tv;
    @BindView(R.id.content_et)
    EditText content_et;

    private String titleName = "修改";
    private String content = "";
    private Intent intent;
    private BusinessResponse mResponse;
    private SharedPreferences sp;
    private Toast toast;
    private LoadDialog loadDialog;

    @Override
    public int getLayoutId() {
        return R.layout.activity_reset_person_info;
    }

    @Override
    protected void initData() {
        super.initData();
        intent = getIntent();
        if(intent != null && intent.hasExtra("Action")){
            titleName = intent.getStringExtra("Action");
            content = intent.getStringExtra("Content");
            mResponse = (BusinessResponse)intent.getSerializableExtra("Bean");
        }
    }

    @Override
    protected void initEvent() {
        super.initEvent();
        EventBus.getDefault().register(this);
        title_name.setText(titleName);
        title_right.setText("提交");
        content_et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                content_tv.setText(s.length() + "/30");
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        content_et.setText(content);
        content_et.setSelection(content_et.getText().length());
        toast = Toast.makeText(this, "", Toast.LENGTH_SHORT);
        loadDialog = new LoadDialog(this, false, "");
    }

    @OnClick({R.id.title_back, R.id.title_right})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.title_back:
                onBackPressedSupport();
                break;
            case R.id.title_right:
                if(TextUtils.isEmpty(content_et.getText().toString())){
                    toast.setText("内容不能为空!");
                    toast.show();
                    return;
                }
                assignment(mResponse, content_et.getText().toString());
                ApiManager.Instance().ChangeUserInfo(Account.getAccessToken(),mResponse.FObject.name,
                        mResponse.FObject.companyName, mResponse.FObject.companyProfile, mResponse.FObject.contact,
                        mResponse.FObject.postCode, mResponse.FObject.contactMobile, mResponse.FObject.contactPhone,
                        mResponse.FObject.address);
                loadDialog.show();
                break;
        }
        sp = getSharedPreferences("config", MODE_PRIVATE);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onUserInfo(BusinessEvent event) {
        loadDialog.dismiss();
        if(event.mResponse != null && event.mResponse.FIsSuccess){
            mResponse = event.mResponse;
            RongIM.getInstance().setCurrentUserInfo(new UserInfo(sp.getString(SealConst.SEALTALK_LOGIN_ID, ""), mResponse.FObject.name, Uri.parse(mResponse.FObject.headUrl)));
            toast.setText("成功");
            toast.show();
        }else {
            toast.setText(event.errorMessage);
            toast.show();
        }
    }

    @Override
    public void onBackPressedSupport() {
        if(mResponse != null && intent != null){
            intent.putExtra("resp", mResponse);
            setResult(RESULT_CODE, intent);
        }
        super.onBackPressedSupport();
    }

    private void assignment(BusinessResponse businessResponse, String text){
        if(titleName.equals(PersonInfoActivity.ACTION_NAME)){
            businessResponse.FObject.companyName = text;
        }else if(titleName.equals(PersonInfoActivity.ACTION_CONTACT)){
            businessResponse.FObject.contact = text;
        }else if(titleName.equals(PersonInfoActivity.ACTION_POSTCODE)){
            businessResponse.FObject.postCode = text;
        }else if(titleName.equals(PersonInfoActivity.ACTION_PHONE)){
            businessResponse.FObject.contactPhone = text;
        }else if(titleName.equals(PersonInfoActivity.ACTION_MOBILE_PHONE)){
            businessResponse.FObject.contactMobile = text;
        }else if(titleName.equals(PersonInfoActivity.ACTION_ADDRESS)){
            businessResponse.FObject.address = text;
        }else if(titleName.equals(PersonInfoActivity.ACTION_PROFILE)){
            businessResponse.FObject.companyProfile = text;
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
