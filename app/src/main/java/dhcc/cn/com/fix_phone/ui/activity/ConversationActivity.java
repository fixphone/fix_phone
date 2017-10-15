package dhcc.cn.com.fix_phone.ui.activity;

import android.os.Bundle;

import dhcc.cn.com.fix_phone.R;
import dhcc.cn.com.fix_phone.base.BaseActivity;
import dhcc.cn.com.fix_phone.base.RongBaseActivity;

/**
 * Created by Administrator on 2017/10/9 0009.
 *
 */

public class ConversationActivity extends RongBaseActivity{


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversation);
        String title = getIntent().getData().getQueryParameter("title");
        setTitle(title);
    }
}
