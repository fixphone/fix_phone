package dhcc.cn.com.fix_phone.ui.activity;

import android.content.Intent;
import android.view.View;

import butterknife.OnClick;
import dhcc.cn.com.fix_phone.R;
import dhcc.cn.com.fix_phone.base.BaseActivity;

/**
 * Created by songyang on 2017\9\18 0018.
 */

public class PersonInfoActivity extends BaseActivity {

    @Override
    public int getLayoutId() {
        return R.layout.activity_person_info;
    }

    @OnClick({R.id.info_company_name_rl})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.info_company_name_rl:
                startActivity(ResetPersonInfoActivity.class);
                break;
        }
    }

    private void startActivity(Class clazz){
        Intent intent = new Intent(this, clazz);
        startActivity(intent);
    }
}
