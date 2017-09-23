package dhcc.cn.com.fix_phone.ui.activity;

import android.widget.TextView;

import butterknife.BindView;
import butterknife.OnClick;
import dhcc.cn.com.fix_phone.R;
import dhcc.cn.com.fix_phone.base.BaseActivity;

/**
 * Created by songyang on 2017\9\23 0023.
 *
 */

public class AboutAppActivity extends BaseActivity{

    @BindView(R.id.title_name)
    TextView title_name;

    @Override
    public int getLayoutId() {
        return R.layout.activity_about_app;
    }

    @Override
    protected void initEvent() {
        super.initEvent();
        title_name.setText("简介");
    }

    @OnClick(R.id.title_back)
    public void onClick(){
        finish();
    }
}
