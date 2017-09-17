package dhcc.cn.com.fix_phone.ui.fragment;

import android.os.Bundle;

import dhcc.cn.com.fix_phone.base.BaseFragment;

/**
 * 2017/9/16 23
 */
public class ImFragment extends BaseFragment {

    public static ImFragment newInstance() {
        
        Bundle args = new Bundle();
        
        ImFragment fragment = new ImFragment();
        fragment.setArguments(args);
        return fragment;
    }
    
    @Override
    protected int getLayoutId() {
        return 0;
    }
}
