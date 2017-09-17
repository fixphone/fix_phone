package dhcc.cn.com.fix_phone.ui.fragment;

import android.os.Bundle;
import android.view.View;

import butterknife.BindView;
import butterknife.OnClick;
import dhcc.cn.com.fix_phone.R;
import dhcc.cn.com.fix_phone.base.BaseFragment;

/**
 * 2017/9/16 23
 */
public class MeFragment extends BaseFragment {

    public static MeFragment newInstance() {
        Bundle args = new Bundle();

        MeFragment fragment = new MeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_me;
    }

    @Override
    protected void init() {
        super.init();

    }

    @OnClick({R.id.mine_info, R.id.mine_circle, R.id.mine_house, R.id.mine_suggest, R.id.mine_vip,
            R.id.mine_advert, R.id.mine_produce, R.id.mine_clear, R.id.mine_app})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.mine_circle:

                break;
            default:
                break;
        }
    }
}
