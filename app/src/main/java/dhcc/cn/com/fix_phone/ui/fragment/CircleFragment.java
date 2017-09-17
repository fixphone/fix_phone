package dhcc.cn.com.fix_phone.ui.fragment;

import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.youth.banner.Banner;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import dhcc.cn.com.fix_phone.R;
import dhcc.cn.com.fix_phone.adapter.CircleAdapter;
import dhcc.cn.com.fix_phone.base.BaseFragment;
import dhcc.cn.com.fix_phone.base.GlideImageLoader;
import dhcc.cn.com.fix_phone.bean.CirCleADResponse;
import dhcc.cn.com.fix_phone.bean.CircleItem;
import dhcc.cn.com.fix_phone.conf.CircleDefaultData;
import dhcc.cn.com.fix_phone.event.CircleAdEvent;
import dhcc.cn.com.fix_phone.remote.ApiManager;

/**
 * 2017/9/16 23
 */
public class CircleFragment extends BaseFragment implements BaseQuickAdapter.OnItemClickListener {

    @BindView(R.id.refreshLayout)
    SmartRefreshLayout mRefreshLayout;

    @BindView(R.id.recyclerview)
    RecyclerView mRecyclerView;

    private List<CircleItem> mCircleItems;
    private CircleAdapter    mAdapter;
    private View             mHeaderView;
    private Banner           mBanner;

    public static CircleFragment newInstance() {
        return new CircleFragment();
    }

    @Override
    protected void init() {
        EventBus.getDefault().register(this);
        mCircleItems = CircleDefaultData.getCircleDefaultData();
        mAdapter = new CircleAdapter(_mActivity, mCircleItems);
    }

    @Override
    protected void destroy() {
        super.destroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_circle;
    }

    @Override
    protected void initView(View view) {
        super.initView(view);
        mHeaderView = getHeaderView();
        GridLayoutManager layoutManager = new GridLayoutManager(_mActivity, 3);
        mRecyclerView.setLayoutManager(layoutManager);
        mAdapter.setSpanSizeLookup(new BaseQuickAdapter.SpanSizeLookup() {
            @Override
            public int getSpanSize(GridLayoutManager gridLayoutManager, int position) {
                return mCircleItems.get(position).getSpanSize();
            }
        });
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mAdapter.addHeaderView(mHeaderView);
        mRecyclerView.setAdapter(mAdapter);
        mRefreshLayout.setEnableLoadmore(false);
    }

    @Override
    protected void initEvent() {
        mAdapter.setOnItemClickListener(this);
    }

    @Override
    protected void initData() {
        ApiManager.Instance().getCircleAds();
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

    }

    private View getHeaderView() {
        View view = LayoutInflater.from(_mActivity).inflate(R.layout.header_circle, (ViewGroup) mRecyclerView.getParent(), false);
        mBanner = view.findViewById(R.id.banner);
        return view;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onShowAd(CircleAdEvent event) {
        List<CirCleADResponse.FObjectBean.ListBean> lists = event.mListBeans;
        List<String> imageList = new ArrayList<>();
        for (CirCleADResponse.FObjectBean.ListBean list : lists) {
            imageList.add(list.FUrl);
        }
        mBanner.setImageLoader(new GlideImageLoader());
        mBanner.setImages(imageList);
        mBanner.start();
    }
}
