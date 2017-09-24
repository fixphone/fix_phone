package dhcc.cn.com.fix_phone.ui.activity;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.OnClick;
import dhcc.cn.com.fix_phone.R;
import dhcc.cn.com.fix_phone.base.BaseActivity;

/**
 * Created by Administrator on 2017/9/21 0021.
 *
 */

public class MyActivity extends BaseActivity{

    @BindView(R.id.my_icon)
    ImageView my_icon;
    @BindView(R.id.my_phone_num_tv)
    TextView my_phone_num_tv;
    @BindView(R.id.title_name)
    TextView title_name;

    @Override
    public int getLayoutId() {
        return R.layout.activity_my;
    }

    @Override
    protected void initEvent() {
        super.initEvent();
        title_name.setText("我的");
    }

    @OnClick({R.id.title_back, R.id.my_header_icon, R.id.my_phone_num, R.id.my_reset_pass, R.id.my_exit})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.title_back:
                finish();
                break;
            case R.id.my_header_icon:
                startActivity(SelectHeaderActivity.class);
                break;
            case R.id.my_phone_num:
                break;
            case R.id.my_reset_pass:
                startActivity(ResetPassWordActivity.class);
                break;
            case R.id.my_exit:
                startActivity(LoginActivity.class);
                break;
        }
    }

    private void startActivity(Class clazz){
        Intent intent = new Intent(this, clazz);
        startActivity(intent);
    }
}
