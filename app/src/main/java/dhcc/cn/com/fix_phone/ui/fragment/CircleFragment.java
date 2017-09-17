package dhcc.cn.com.fix_phone.ui.fragment;

import android.os.Bundle;

import dhcc.cn.com.fix_phone.base.BaseFragment;

/**
 * 2017/9/16 23
 */
public class CircleFragment extends BaseFragment {

    public static CircleFragment newInstance() {
        Bundle args = new Bundle();
        CircleFragment fragment = new CircleFragment();
        fragment.setArguments(args);
        return fragment;
    }
    
    @Override
    protected int getLayoutId() {
        return 0;
    }
}
