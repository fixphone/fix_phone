package dhcc.cn.com.fix_phone.ui.fragment;

import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.youth.banner.Banner;
import com.youth.banner.listener.OnBannerListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import dhcc.cn.com.fix_phone.MyApplication;
import dhcc.cn.com.fix_phone.R;
import dhcc.cn.com.fix_phone.adapter.CircleFragmentAdapter;
import dhcc.cn.com.fix_phone.base.BaseFragment;
import dhcc.cn.com.fix_phone.base.GlideImageLoader;
import dhcc.cn.com.fix_phone.bean.CirCleADResponse;
import dhcc.cn.com.fix_phone.conf.CircleDefaultData;
import dhcc.cn.com.fix_phone.event.CircleAdEvent;
import dhcc.cn.com.fix_phone.remote.ApiManager;
import dhcc.cn.com.fix_phone.ui.activity.BusinessActivity;
import dhcc.cn.com.fix_phone.ui.activity.CircleActivity;

/**
 * 2017/9/16 23
 */
public class CircleFragment extends BaseFragment implements CircleFragmentAdapter.OnCircleItemClickListener {
    private static final String TAG = "CircleFragment";

    @BindView(R.id.refreshLayout)
    SmartRefreshLayout mRefreshLayout;

    @BindView(R.id.recyclerview)
    RecyclerView mRecyclerView;

    private List<MultiItemEntity> mCircleItems;
    private CircleFragmentAdapter mAdapter;
    private Banner                mBanner;
    private List<CirCleADResponse.FObjectBean.ListBean> mListBeans;

    public static CircleFragment newInstance() {
        return new CircleFragment();
    }

    @Override
    protected void init() {
        EventBus.getDefault().register(this);
        mCircleItems = CircleDefaultData.getCircleDefaultData();
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
        mRefreshLayout.setEnableLoadmore(false);
        mAdapter = new CircleFragmentAdapter(_mActivity, mCircleItems);
        mAdapter.addHeaderView(getHeaderView());
        final GridLayoutManager layoutManager = new GridLayoutManager(_mActivity, 3);
        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return mAdapter.getItemViewType(position) == CircleFragmentAdapter.TYPE_LEVEL_1 ? 1 : layoutManager.getSpanCount();
            }
        });
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(layoutManager);
        mAdapter.expand(1,false,false);
        mAdapter.expand(10,false,false);
    }

    @Override
    protected void initEvent() {
        mAdapter.setOnCircleItemClickListener(this);
        mBanner.setOnBannerListener(new OnBannerListener() {
            @Override
            public void OnBannerClick(int position) {
                startActivity(new Intent(_mActivity, BusinessActivity.class).
                        putExtra("headurl",mListBeans.get(position).FUrl).
                        putExtra("name",mListBeans.get(position).FLinkName).
                        putExtra("userID",mListBeans.get(position).FLinkID));
            }
        });
    }

    @Override
    protected void initData() {
        ApiManager.Instance().getCircleAds();
    }

    private View getHeaderView() {
        View view = LayoutInflater.from(_mActivity).inflate(R.layout.header_circle, (ViewGroup) mRecyclerView.getParent(), false);
        mBanner = view.findViewById(R.id.banner);
        return view;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onShowAd(CircleAdEvent event) {
        mListBeans = event.mListBeans;
        List<String> imageList = new ArrayList<>();
        for (CirCleADResponse.FObjectBean.ListBean list : mListBeans) {
            imageList.add(list.FUrl);
        }
        mBanner.setImageLoader(new GlideImageLoader());
        mBanner.setImages(imageList);
        mBanner.start();
    }

    @Override
    public void onCircleItemClick(BaseQuickAdapter adapter, View view, String typeId, String content) {
        MyApplication.setCurrentTypeId(typeId);
        startActivity(new Intent(_mActivity, CircleActivity.class).putExtra("data", typeId).putExtra("name", content));
    }
}
