package dhcc.cn.com.fix_phone.base;

import android.os.Bundle;
import android.support.annotation.Nullable;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import me.yokeyword.fragmentation.anim.DefaultHorizontalAnimator;
import me.yokeyword.fragmentation.anim.FragmentAnimator;
import me.yokeyword.fragmentation_swipeback.SwipeBackActivity;

/**
 * 2016/10/27 13
 */
public abstract class BaseActivity<T extends BasePresenter> extends SwipeBackActivity implements BaseView {
    private T        mPresenter;
    private Unbinder mUnbinder;

    @Override
    @SuppressWarnings("unchecked")
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        mUnbinder = ButterKnife.bind(this);
        mPresenter = initPresenter();
        if (mPresenter != null) {
            mPresenter.attachView(this);
        }
        init();
        initView();
        initData();
        initEvent();
    }

    protected void init() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPresenter != null) {
            mPresenter.detachView();
        }
        mUnbinder.unbind();
    }

    protected void initView() {

    }

    protected void initEvent() {

    }

    protected void initData() {

    }

    protected T initPresenter() {
        return null;
    }

    public abstract int getLayoutId();

    public T getPresenter() {
        return mPresenter;
    }

    @Override
    public FragmentAnimator onCreateFragmentAnimator() {
        return new DefaultHorizontalAnimator();
    }


}
