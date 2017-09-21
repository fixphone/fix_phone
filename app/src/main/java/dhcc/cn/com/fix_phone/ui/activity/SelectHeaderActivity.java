package dhcc.cn.com.fix_phone.ui.activity;

import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.OnClick;
import dhcc.cn.com.fix_phone.R;
import dhcc.cn.com.fix_phone.base.BaseActivity;

/**
 * Created by Administrator on 2017/9/21 0021.
 */

public class SelectHeaderActivity extends BaseActivity{

    @BindView(R.id.title_name)
    TextView title_name;

    @Override
    public int getLayoutId() {
        return R.layout.activity_select_header;
    }

    @Override
    protected void initEvent() {
        super.initEvent();
        title_name.setText("修改店铺头像");
    }

    @OnClick({R.id.title_back})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.title_back:
                finish();
                break;
        }
    }
}
