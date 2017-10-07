package dhcc.cn.com.fix_phone.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.rubensousa.bottomsheetbuilder.BottomSheetBuilder;
import com.github.rubensousa.bottomsheetbuilder.BottomSheetMenuDialog;
import com.github.rubensousa.bottomsheetbuilder.adapter.BottomSheetItemClickListener;
import com.miguelcatalan.materialsearchview.MaterialSearchView;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.List;

import dhcc.cn.com.fix_phone.R;
import dhcc.cn.com.fix_phone.adapter.CircleAdapter;
import dhcc.cn.com.fix_phone.bean.CircleDetailAd;
import dhcc.cn.com.fix_phone.bean.CircleItem;
import dhcc.cn.com.fix_phone.bean.CommentConfig;
import dhcc.cn.com.fix_phone.bean.CommentItem;
import dhcc.cn.com.fix_phone.bean.FavortItem;
import dhcc.cn.com.fix_phone.mvp.contract.CircleContract;
import dhcc.cn.com.fix_phone.mvp.presenter.CirclePresenter;
import dhcc.cn.com.fix_phone.remote.ApiManager;

/**
 * @author yiw
 * @ClassName: CircleActivity
 * @date 2015-12-28 下午4:21:18
 */
public class CircleActivity extends YWActivity implements CircleContract.View {

    public static final    int    MAX_NUMBER = 20;
    protected static final String TAG        = "CircleActivity";
    public                 int    pageIndex  = 1;
    public                 int    pageSize   = 20;
    public boolean isLoadMore;

    private CircleAdapter         circleAdapter;
    private CirclePresenter       presenter;
    private RecyclerView          recyclerView;
    private LinearLayoutManager   layoutManager;
    private TextView              titleBar;
    private SmartRefreshLayout    mRefreshLayout;
    private String                mTypeId;
    private String                mContentName;
    private Toolbar               mToolbar;
    private ImageView             mSearch;
    private LinearLayout          mPublish;
    private BottomSheetMenuDialog mDialog;
    private MaterialSearchView    mSearchView;
    private TextView              mTextView_number;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_circle);
        init();
        initView();
        initData();
        initListener();
    }

    @Override
    protected void onDestroy() {
        if (presenter != null) {
            presenter.recycle();
        }
        super.onDestroy();
    }

    private void initListener() {
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

        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        mSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSearchView.showSearch();
            }
        });

        mPublish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createBottomDialog();
            }
        });

        circleAdapter.setOnPopWindowClickListener(new CircleAdapter.OnPopWindowClickListener() {
            @Override
            public void onItemClick(int position, CircleItem circleItem) {
                switch (position) {
                    case 0: // 收藏
                        ApiManager.Instance().addFavo(circleItem.getUser().FInterID);
                        break;
                    case 1: // 投诉
                        startActivity(FeedBackActivity.class);
                        break;
                    case 2: // 分享

                        break;
                }
            }
        });

        mSearchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                ApiManager.Instance().getCircleBusinessList(MAX_NUMBER, pageIndex, pageSize, mTypeId, query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //Do some magic
                return false;
            }
        });

    }

    private void createBottomDialog() {
        if (mDialog != null && mDialog.isShowing()) {
            mDialog.dismiss();
        }
        mDialog = new BottomSheetBuilder(CircleActivity.this)
                .setMode(BottomSheetBuilder.MODE_LIST)
                .setMenu(R.menu.menu_bottom_simple_sheet)
                .setItemClickListener(new BottomSheetItemClickListener() {
                    @Override
                    public void onBottomSheetItemClick(MenuItem item) {
                        int id = item.getItemId();
                        switch (id) {
                            case R.id.menu_image:
                                startActivity(new Intent(CircleActivity.this, PublishActivity.class).putExtra("type", 1));
                                break;
                            case R.id.menu_video:
                                startActivity(new Intent(CircleActivity.this, PublishActivity.class).putExtra("type", 2));
                                break;
                            case R.id.menu_cancel:
                                mDialog.dismiss();
                                break;

                        }
                    }
                })
                .createDialog();

        mDialog.show();
    }

    private void initData() {
        ApiManager.Instance().getCircleDetailAds(mTypeId);
        ApiManager.Instance().getCircleBusinessList(MAX_NUMBER, pageIndex, pageSize, mTypeId, "");
    }

    private void loadMore() {
        ApiManager.Instance().getCircleBusinessList(MAX_NUMBER, pageIndex, pageSize, mTypeId, "");
    }

    private void init() {
        presenter = new CirclePresenter(this);
        Intent intent = getIntent();
        mTypeId = intent.getStringExtra("data");
        mContentName = intent.getStringExtra("name");
    }

    private void initView() {
        initTitle();
        initToolbar();
        mSearchView = (MaterialSearchView) findViewById(R.id.search_view);
        mRefreshLayout = (SmartRefreshLayout) findViewById(R.id.refreshLayout);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        circleAdapter = new CircleAdapter(this);
        circleAdapter.setCirclePresenter(presenter);
        recyclerView.setAdapter(circleAdapter);

        mSearchView.setVoiceSearch(false);
        mSearchView.setCursorDrawable(R.drawable.custom_cursor);
        mSearchView.setEllipsize(true);

        mTextView_number = (TextView) findViewById(R.id.textView_number);
    }

    private void initToolbar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mSearch = (ImageView) findViewById(R.id.imageView_search);
        mPublish = (LinearLayout) findViewById(R.id.linearLayout_publish);
    }

    private void initTitle() {
        titleBar = (TextView) findViewById(R.id.toolbar_title);
        titleBar.setText(mContentName);
    }

    @Override
    public void update2DeleteCircle(String circleId) {
    }

    @Override
    public void update2AddFavorite(int circlePosition, FavortItem addItem) {
    }

    @Override
    public void update2DeleteFavort(int circlePosition, String favortId) {
    }

    @Override
    public void update2AddComment(int circlePosition, CommentItem addItem) {
    }

    @Override
    public void update2DeleteComment(int circlePosition, String commentId) {
    }

    @Override
    public void updateEditTextBodyVisible(int visibility, CommentConfig commentConfig) {
    }

    @Override
    public void update2loadData(int loadType, List<CircleItem> datas) {

    }

    @Override
    public void updateAd(List<CircleDetailAd.FObjectBean.ListBean> lists) {
        circleAdapter.setFObjectBean(lists);
    }

    @Override
    public void updateCircleItem(List<CircleItem> circleItems) {
        if (isLoadMore) {
            mRefreshLayout.finishLoadmore(200);
            circleAdapter.addDatas(circleItems);
        } else {
            mRefreshLayout.finishRefresh(200);
            circleAdapter.setDatas(circleItems);
        }
        circleAdapter.notifyDataSetChanged();
    }

    @Override
    public void showTextViewNumber(boolean b) {
        if (b) {
            mTextView_number.setVisibility(View.VISIBLE);
        } else {
            mTextView_number.setVisibility(View.GONE);
        }
    }

    @Override
    public void showLoading(String msg) {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void showError(String errorMsg) {
        Toast.makeText(this, "" + errorMsg, Toast.LENGTH_SHORT).show();
    }

    private <T extends Activity> void startActivity(Class<T> clazz) {
        Intent intent = new Intent(this, clazz);
        startActivity(intent);
    }

    @Override
    public void onBackPressedSupport() {
        if (mSearchView.isSearchOpen()) {
            mSearchView.closeSearch();
        } else {
            super.onBackPressed();
        }
    }
}
