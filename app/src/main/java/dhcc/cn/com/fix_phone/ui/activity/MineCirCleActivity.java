package dhcc.cn.com.fix_phone.ui.activity;

import android.content.Intent;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

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
import dhcc.cn.com.fix_phone.bean.PhotoInfo;
import dhcc.cn.com.fix_phone.bean.User;
import dhcc.cn.com.fix_phone.bean.VideoInfo;
import dhcc.cn.com.fix_phone.event.CirCleBusinessEvent;
import dhcc.cn.com.fix_phone.remote.ApiManager;

/**
 * 2017/10/6 13
 */
public class MineCirCleActivity extends BaseActivity {

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
    }

    @Override
    protected void initData() {
        pageIndex = 1;
        ApiManager.Instance().getMyList(MAX_NUMBER, pageIndex, pageSize, "");
    }

    private void loadMore() {
        ApiManager.Instance().getMyList(MAX_NUMBER, ++pageIndex, pageSize, "");
    }

    @Override
    protected void initView() {
        mToolbarTitle.setText(mTitle);
        mRecyclerview.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerview.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
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

    public void updateCircleItem(List<CircleItem> circleItems) {
        if (isLoadMore) {
            mRefreshLayout.finishLoadmore(200);
            mAdapter.addDatas(circleItems);
        } else {
            mRefreshLayout.finishRefresh(200);
            mAdapter.setDatas(circleItems);
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
}
