package dhcc.cn.com.fix_phone.ui.activity;

import android.content.Intent;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.mikhaellopez.circularprogressbar.CircularProgressBar;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import dhcc.cn.com.fix_phone.R;
import dhcc.cn.com.fix_phone.adapter.MineCircleAdapter;
import dhcc.cn.com.fix_phone.base.BaseActivity;
import dhcc.cn.com.fix_phone.bean.CircleBusiness;
import dhcc.cn.com.fix_phone.bean.CircleItem;
import dhcc.cn.com.fix_phone.bean.FavoResponse;
import dhcc.cn.com.fix_phone.bean.PhotoInfo;
import dhcc.cn.com.fix_phone.bean.User;
import dhcc.cn.com.fix_phone.bean.VideoInfo;
import dhcc.cn.com.fix_phone.event.CirCleBusinessEvent;
import dhcc.cn.com.fix_phone.event.FavoResponseEvent;
import dhcc.cn.com.fix_phone.remote.ApiManager;
import dhcc.cn.com.fix_phone.ui.fragment.DeleteDialogFragment;

/**
 * 2017/10/6 13
 */
public class MineCirCleActivity extends BaseActivity implements DeleteDialogFragment.SelectListener {

    protected static final String TAG        = "MineCirCleActivity";
    public static final    int    MAX_NUMBER = 20;
    public                 int    pageIndex  = 1;
    public                 int    pageSize   = 20;
    public boolean isLoadMore;

    @BindView(R.id.toolbar_title)
    TextView            mToolbarTitle;
    @BindView(R.id.toolbar)
    Toolbar             mToolbar;
    @BindView(R.id.recyclerview)
    RecyclerView        mRecyclerview;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout  mRefreshLayout;
    @BindView(R.id.video_progress)
    CircularProgressBar mVideoProgress;
    private String            mTitle;
    private MineCircleAdapter mAdapter;
    private int               mResourceType;
    private int               mCurrentPosition;
    private String            mFInterID;

    @Override
    public int getLayoutId() {
        return R.layout.activity_mine_circle;
    }

    @Override
    protected void init() {
        EventBus.getDefault().register(this);
        Intent intent = getIntent();
        mTitle = intent.getStringExtra("title");
        mResourceType = intent.getIntExtra("resource_type", 1);
    }

    @Override
    protected void initEvent() {
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                isLoadMore = false;
                initData();
            }
        });


        mRefreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                isLoadMore = true;
                loadMore();
            }
        });

        mAdapter.setDeleteClickListener(new MineCircleAdapter.OnDeleteClickListener() {
            @Override
            public void onDelete(int position, CircleItem item) {
                mCurrentPosition = position;
                mFInterID = item.getUser().FInterID;
                DeleteDialogFragment deleteFragment = DeleteDialogFragment.newInstance();
                deleteFragment.show(getSupportFragmentManager(), "deleteFragment");
            }
        });

        mAdapter.setOnVideoClickListener(new MineCircleAdapter.OnVideoClickListener() {
            @Override
            public void onVideoClickListener(CircleItem circleItem) {
                startActivity(new Intent(MineCirCleActivity.this, ExtendsNormalActivity.class).
                        putExtra("path", circleItem.getVideoInfo().FFileName).
                        putExtra("imagePath", circleItem.getVideoInfo().FCaptureFileName));
            }
        });
    }

    @Override
    protected void initData() {
        if (mResourceType == 1) {
            pageIndex = 1;
            ApiManager.Instance().getMyList(MAX_NUMBER, pageIndex, pageSize, "");
        } else {
            ApiManager.Instance().getFavoList();
        }
    }

    private void loadMore() {
        if (mResourceType == 1) {
            ApiManager.Instance().getMyList(MAX_NUMBER, ++pageIndex, pageSize, "");
        } else {
            ApiManager.Instance().getFavoList();
        }
    }

    @Override
    protected void initView() {
        mToolbarTitle.setText(mTitle);
        mRecyclerview.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerview.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        mRecyclerview.setItemAnimator(new DefaultItemAnimator());
        mAdapter = new MineCircleAdapter(this);
        mRecyclerview.setAdapter(mAdapter);
    }

    @Override
    protected void destroy() {
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onShowBusiness(CirCleBusinessEvent event) {
        CircleBusiness.FObjectBean bean = event.mFObjectBean;
        List<CircleItem> circleItems = transformCircleItem(bean);
        updateCircleItem(circleItems);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onDelete(FavoResponseEvent event) {
        FavoResponse response = event.mResponse;
        if (response.FIsSuccess) {
            mAdapter.getData().remove(mCurrentPosition);
            mAdapter.notifyDataSetChanged();
        }
        Toast.makeText(this, "" + response.FMsg, Toast.LENGTH_SHORT).show();
    }

    public void updateCircleItem(List<CircleItem> circleItems) {
        if (isLoadMore) {
            mRefreshLayout.finishLoadmore(200);
            mAdapter.addData(circleItems);
        } else {
            mRefreshLayout.finishRefresh(200);
            mAdapter.setData(circleItems);
        }
        mAdapter.notifyDataSetChanged();
    }

    private List<CircleItem> transformCircleItem(CircleBusiness.FObjectBean bean) {
        List<CircleBusiness.FObjectBean.RowsBean> rows = bean.rows;
        List<CircleItem> circleItemList = new ArrayList<>();
        for (int i = 0; i < rows.size(); i++) {
            CircleBusiness.FObjectBean.RowsBean rowsBean = rows.get(i);
            CircleItem circleItem = new CircleItem();

            User user = new User();
            user.FCompanyName = rowsBean.FCompanyName;
            user.FContent = rowsBean.FContent;
            user.FCreateDate = rowsBean.FCreateDate;
            user.FCreatorID = rowsBean.FCreatorID;
            user.FHeadUrl = rowsBean.FHeadUrl;
            user.FInterID = rowsBean.FInterID;
            user.FIsFooterData = rowsBean.FIsFooterData;
            user.FPhone = rowsBean.FPhone;
            user.FShareUrl = rowsBean.FShareUrl;
            user.FTimeAgo = rowsBean.FTimeAgo;
            user.FTypeID = rowsBean.FTypeID;
            user.FTypeName = rowsBean.FTypeName;
            user.FTypeNumber = rowsBean.FTypeNumber;
            user.FUserName = rowsBean.FUserName;
            user.FUserType = rowsBean.FUserType;
            user.FUserTypeName = rowsBean.FUserTypeName;
            user.FUserTypeNumber = rowsBean.FUserTypeNumber;
            circleItem.setUser(user);
            circleItem.setType(CircleItem.TYPE_URL);

            VideoInfo videoInfo = new VideoInfo();
            CircleBusiness.FObjectBean.RowsBean.VideoBean video = rowsBean.Video;
            if (video != null) {
                videoInfo.FBitrate = video.FBitrate;
                videoInfo.FBitrateMode = video.FBitrateMode;
                videoInfo.FCaptureFileName = video.FCaptureFileName;
                videoInfo.FColorFormat = video.FColorFormat;
                videoInfo.FDisplayRatio = video.FDisplayRatio;
                videoInfo.FDisplayRatioHeight = video.FDisplayRatioHeight;
                videoInfo.FDisplayRatioWidth = video.FDisplayRatioWidth;
                videoInfo.FDuration = video.FDuration;
                videoInfo.FFileName = video.FFileName;
                videoInfo.FFrameHeight = video.FFrameHeight;
                videoInfo.FFrameRate = video.FFrameRate;
                videoInfo.FFrameWidth = video.FFrameWidth;
                videoInfo.FInterID = video.FInterID;
                videoInfo.FIsFooterData = video.FIsFooterData;
                videoInfo.FUUID = video.FUUID;
                circleItem.setType(CircleItem.TYPE_VIDEO);
            }
            circleItem.setVideoInfo(videoInfo);

            List<CircleBusiness.FObjectBean.RowsBean.ImageBean> imageList = rowsBean.ImageList;
            if (imageList != null && !imageList.isEmpty()) {
                List<PhotoInfo> list = new ArrayList<>(imageList.size());
                for (int j = 0; j < imageList.size(); j++) {
                    PhotoInfo photoInfo = new PhotoInfo();
                    photoInfo.FEntryID = imageList.get(j).FEntryID;
                    photoInfo.FInterID = imageList.get(j).FInterID;
                    photoInfo.FIsFooterData = imageList.get(j).FIsFooterData;
                    photoInfo.FOriginalUrl = imageList.get(j).FOriginalUrl;
                    photoInfo.FUUID = imageList.get(j).FUUID;

                    list.add(photoInfo);
                }
                circleItem.setPhotos(list);
                circleItem.setType(CircleItem.TYPE_IMG);
            }

            circleItemList.add(circleItem);
        }

        return circleItemList;
    }

    @Override
    public void onSelector() {
        if (mResourceType == 1) {
            ApiManager.Instance().DeleteBusi(mFInterID);
        } else {
            ApiManager.Instance().deleteFavo(mFInterID);
        }
    }
}
