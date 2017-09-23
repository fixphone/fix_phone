package dhcc.cn.com.fix_phone.ui.activity;

import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.OnClick;
import dhcc.cn.com.fix_phone.R;
import dhcc.cn.com.fix_phone.base.BaseActivity;

/**
 * Created by Administrator on 2017/9/21 0021.
 *
 */

public class ResetPersonInfoActivity extends BaseActivity{

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
        }
    }

    @Override
    protected void initEvent() {
        super.initEvent();
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
