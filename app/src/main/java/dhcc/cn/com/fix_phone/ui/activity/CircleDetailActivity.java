package dhcc.cn.com.fix_phone.ui.activity;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadmoreListener;
import com.youth.banner.Banner;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import dhcc.cn.com.fix_phone.R;
import dhcc.cn.com.fix_phone.base.BaseActivity;
import dhcc.cn.com.fix_phone.base.GlideImageLoader;
import dhcc.cn.com.fix_phone.bean.CircleBusiness;
import dhcc.cn.com.fix_phone.bean.CircleDetailAd;
import dhcc.cn.com.fix_phone.event.CirCleBusinessEvent;
import dhcc.cn.com.fix_phone.event.CircleDetailAdEvent;
import dhcc.cn.com.fix_phone.remote.ApiManager;

/**
 * 2017/9/20 21
 */
public class CircleDetailActivity extends BaseActivity implements OnRefreshLoadmoreListener {
    private static final String TAG        = "CircleDetailActivity";
    public static final  int    MAX_NUMBER = 20;
    public               int    pageIndex  = 1;
    public               int    pageSize   = 20;

    @BindView(R.id.refreshLayout)
    SmartRefreshLayout mRefreshLayout;

    @BindView(R.id.recyclerview)
    RecyclerView mRecyclerView;

    private String mTypeId;
    private View   mHeaderView;
    private Banner mBanner;

    @Override
    protected void init() {
        Intent intent = getIntent();
        mTypeId = intent.getStringExtra("data");
        EventBus.getDefault().register(this);
    }

    @Override
    protected void initView() {
        mHeaderView = getHeaderView();
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_circle_detail;
    }

    @Override
    protected void initData() {
        ApiManager.Instance().getCircleDetailAds(mTypeId);
        ApiManager.Instance().getCircleBusinessList(MAX_NUMBER, pageIndex, pageSize, mTypeId, "");
    }

    @Override
    protected void initEvent() {
        mRefreshLayout.setOnRefreshLoadmoreListener(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onShowAd(CircleDetailAdEvent event) {
        List<CircleDetailAd.FObjectBean.ListBean> lists = event.mListBeans;
        List<String> imageList = new ArrayList<>();
        for (CircleDetailAd.FObjectBean.ListBean list : lists) {
            imageList.add(list.FUrl);
        }
        mBanner.setImageLoader(new GlideImageLoader());
        mBanner.setImages(imageList);
        mBanner.start();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onShowBusiness(CirCleBusinessEvent event) {
        CircleBusiness.FObjectBean bean = event.mFObjectBean;

    }

    @Override
    protected void destroy() {
        EventBus.getDefault().unregister(this);
    }

    private View getHeaderView() {
        View view = LayoutInflater.from(this).inflate(R.layout.header_circle, (ViewGroup) mRecyclerView.getParent(), false);
        mBanner = view.findViewById(R.id.banner);
        return view;
    }

    @Override
    public void onLoadmore(RefreshLayout refreshlayout) {

    }

    @Override
    public void onRefresh(RefreshLayout refreshlayout) {

    }
}
