package dhcc.cn.com.fix_phone.ui.activity;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import butterknife.BindView;
import butterknife.OnClick;
import dhcc.cn.com.fix_phone.Account;
import dhcc.cn.com.fix_phone.R;
import dhcc.cn.com.fix_phone.base.BaseActivity;
import dhcc.cn.com.fix_phone.bean.BusinessResponse;

/**
 * Created by Administrator on 2017/9/21 0021.
 */

public class MyActivity extends BaseActivity {

    @BindView(R.id.my_icon)
    ImageView my_icon;
    @BindView(R.id.my_phone_num_tv)
    TextView  my_phone_num_tv;
    @BindView(R.id.title_name)
    TextView  title_name;

    private static final int SELECT_ICON_CODE = 0x0015;
    private BusinessResponse mResponse;

    @Override
    public int getLayoutId() {
        return R.layout.activity_my;
    }

    @Override
    protected void initEvent() {
        super.initEvent();
        title_name.setText("我的");
    }

    @Override
    protected void initData() {
        super.initData();
        if (getIntent() != null && getIntent().hasExtra("BusinessResponse")) {
            mResponse = (BusinessResponse) getIntent().getSerializableExtra("BusinessResponse");
            setViewState(mResponse);
        }
    }

    @OnClick({R.id.title_back, R.id.my_header_icon, R.id.my_phone_num, R.id.my_reset_pass, R.id.my_exit})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.title_back:
                finish();
                break;
            case R.id.my_header_icon:
                startActivityForResult(new Intent(this, SelectHeaderActivity.class).putExtra("Action", mResponse),SELECT_ICON_CODE);
                break;
            case R.id.my_phone_num:
                break;
            case R.id.my_reset_pass:
                startActivity(new Intent(this, ResetPassWordActivity.class));
                break;
            case R.id.my_exit:
                exit();
                break;
        }
    }

    private void setViewState(BusinessResponse mResponse) {
        if (mResponse != null && mResponse.FObject != null) {
            Glide.with(this).load(mResponse.FObject.headUrl).diskCacheStrategy(DiskCacheStrategy.ALL).into(my_icon);
            my_phone_num_tv.setText(mResponse.FObject.phone);
        }
    }

    private void exit() {
        Account.setLogin(false);
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == SELECT_ICON_CODE){
            if(resultCode == SelectHeaderActivity.RESULT_CODE){
                mResponse = (BusinessResponse)data.getSerializableExtra("result");
                Glide.with(this).load(mResponse.FObject.headUrl).diskCacheStrategy(DiskCacheStrategy.ALL).into(my_icon);
            }
        }
    }
}
