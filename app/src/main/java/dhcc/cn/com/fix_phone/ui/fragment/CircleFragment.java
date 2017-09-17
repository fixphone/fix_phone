package dhcc.cn.com.fix_phone.ui.fragment;

import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import butterknife.BindView;
import dhcc.cn.com.fix_phone.R;
import dhcc.cn.com.fix_phone.base.BaseFragment;

/**
 * 2017/9/16 23
 */
public class CircleFragment extends BaseFragment {

    @BindView(R.id.refreshLayout)
    SmartRefreshLayout mRefreshLayout;

    @BindView(R.id.recyclerview)
    RecyclerView mRecyclerView;

    public static CircleFragment newInstance() {
        return new CircleFragment();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_circle;
    }

    @Override
    protected void initView(View view) {
        super.initView(view);
        mRecyclerView.setLayoutManager(new GridLayoutManager(_mActivity, 3));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
    }
}
