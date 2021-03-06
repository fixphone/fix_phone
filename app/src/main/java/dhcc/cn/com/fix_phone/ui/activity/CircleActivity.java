package dhcc.cn.com.fix_phone.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MenuItem;
import android.view.MotionEvent;
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
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelmsg.ShowMessageFromWX;
import com.tencent.mm.opensdk.modelmsg.WXAppExtendObject;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.FileCallBack;

import java.io.File;
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
import dhcc.cn.com.fix_phone.ui.fragment.AlterDialogFragment;
import dhcc.cn.com.fix_phone.ui.fragment.CommonDeleteFragment;
import okhttp3.Call;

import static dhcc.cn.com.fix_phone.ui.activity.FeedBackActivity.getCurrentTime;

/**
 * @author yiw
 * @ClassName: CircleActivity
 * @date 2015-12-28 下午4:21:18
 */
public class CircleActivity extends YWActivity implements CircleContract.View, IWXAPIEventHandler ,CommonDeleteFragment.OnConfirmClickListener {

    public static final    int    MAX_NUMBER = 20;
    protected static final String TAG        = "CircleActivity";
    public                 int    pageIndex  = 1;
    public                 int    pageSize   = 20;
    public boolean isLoadMore;
    private static final String storePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/DCIM/Camera/";

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
    private AlterDialogFragment   mFragment;
    private GestureDetectorCompat mGestureDetectorCompat;
    private CommonDeleteFragment  vipDialog;


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
        Log.d(TAG, "onDestroy: circleAdapter");
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
                        showShareDialog(circleItem.getUser().FShareUrl);
                        break;
                    default:
                        break;
                }
            }
        });

        circleAdapter.setOnVideoLongClickListener(new CircleAdapter.OnVideoLongClickListener() {
            @Override
            public void onVideoLongClickListener(CircleItem circleItem) {
                Log.d(TAG, "onVideoLongClickListener: ");
                createBottomVideoDialog(circleItem);
            }
        });

        circleAdapter.setOnVideoClickListener(new CircleAdapter.OnVideoClickListener() {
            @Override
            public void onVideoClickListener(CircleItem circleItem) {
                startActivity(new Intent(CircleActivity.this, ExtendsNormalActivity.class).
                        putExtra("path", circleItem.getVideoInfo().FFileName).
                        putExtra("imagePath", circleItem.getVideoInfo().FCaptureFileName));
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

        mGestureDetectorCompat = new GestureDetectorCompat(this, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onDown(MotionEvent e) {
                return true;
            }

            @Override
            public boolean onDoubleTap(MotionEvent e) {
                recyclerView.scrollToPosition(0);
                return true;
            }
        });

        titleBar.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return mGestureDetectorCompat.onTouchEvent(event);
            }
        });
    }

    private void showShareDialog(String FShareUrl) {
        if (mFragment != null) {
            mFragment.dismiss();
        }
        mFragment = AlterDialogFragment.newInstance(FShareUrl);
        mFragment.show(getSupportFragmentManager(), "AlterDialogFragment");
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
                            default:
                                break;
                        }
                    }
                })
                .createDialog();

        mDialog.show();
    }

    private void initData() {
        pageIndex = 1;
        ApiManager.Instance().getCircleDetailAds(mTypeId);
        ApiManager.Instance().getCircleBusinessList(MAX_NUMBER, pageIndex, pageSize, mTypeId, "");
    }

    private void loadMore() {
        ApiManager.Instance().getCircleBusinessList(MAX_NUMBER, ++pageIndex, pageSize, mTypeId, "");
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
    @SuppressWarnings("unchecked")
    public void updateCircleItem(List<CircleItem> circleItems) {
        if (isLoadMore) {
            mRefreshLayout.finishLoadmore(200);
            circleAdapter.addData(circleItems);
        } else {
            mRefreshLayout.finishRefresh(200);
            circleAdapter.setData(circleItems);
        }
        showTextViewNumber(circleAdapter.getData().isEmpty());
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
    public void refreshData() {
        initData();
    }

    @Override
    public void showVipDialog() {
        if (vipDialog != null) {
            vipDialog.dismiss();
        }
        createDialog("提示", "您发布的数量已达到限时10条，开通会员可终身免费发布。");
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
            finish();
        }
    }

    private void createBottomVideoDialog(final CircleItem circleItem) {
        if (mDialog != null && mDialog.isShowing()) {
            mDialog.dismiss();
        }
        mDialog = new BottomSheetBuilder(this)
                .setMode(BottomSheetBuilder.MODE_LIST)
                .setMenu(R.menu.menu_bottom_video)
                .setItemClickListener(new BottomSheetItemClickListener() {
                    @Override
                    public void onBottomSheetItemClick(MenuItem item) {
                        int id = item.getItemId();
                        switch (id) {
                            case R.id.menu_save:
                                String fFileName = circleItem.getVideoInfo().FFileName;
                                OkHttpUtils.get().url(fFileName).build().
                                        execute(new FileCallBack(storePath, getCurrentTime("yyyyMMddHHmmss") + ".mp4") {
                                            @Override
                                            public void onError(Call call, Exception e, int id) {

                                            }

                                            @Override
                                            public void onResponse(File response, int id) {
                                                Toast.makeText(CircleActivity.this, "保存成功", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                break;
                            case R.id.menu_collect:
                                ApiManager.Instance().AddVideoFavo(circleItem.getVideoInfo().FUUID);
                                break;
                            case R.id.menu_cancel:
                                mDialog.dismiss();
                                break;
                            default:
                                break;
                        }
                    }
                })
                .createDialog();

        mDialog.show();
    }

    // 微信发送请求到第三方应用时，会回调到该方法
    @Override
    public void onReq(BaseReq req) {
        switch (req.getType()) {
            case ConstantsAPI.COMMAND_GETMESSAGE_FROM_WX:
                goToGetMsg();
                break;
            case ConstantsAPI.COMMAND_SHOWMESSAGE_FROM_WX:
                goToShowMsg((ShowMessageFromWX.Req) req);
                break;
            default:
                break;
        }
    }

    // 第三方应用发送到微信的请求处理后的响应结果，会回调到该方法
    @Override
    public void onResp(BaseResp resp) {
        int result = 0;
        switch (resp.errCode) {
            case BaseResp.ErrCode.ERR_OK:
                result = R.string.errcode_success;
                break;
            case BaseResp.ErrCode.ERR_USER_CANCEL:
                result = R.string.errcode_cancel;
                break;
            case BaseResp.ErrCode.ERR_AUTH_DENIED:
                result = R.string.errcode_deny;
                break;
            default:
                result = R.string.errcode_unknown;
                break;
        }

        Toast.makeText(this, result, Toast.LENGTH_LONG).show();
    }

    private void goToGetMsg() {
    }

    private void goToShowMsg(ShowMessageFromWX.Req showReq) {
        WXMediaMessage wxMsg = showReq.message;
        WXAppExtendObject obj = (WXAppExtendObject) wxMsg.mediaObject;

        StringBuffer msg = new StringBuffer();
        msg.append("description: ");
        msg.append(wxMsg.description);
        msg.append("\n");
        msg.append("extInfo: ");
        msg.append(obj.extInfo);
        msg.append("\n");
        msg.append("filePath: ");
        msg.append(obj.filePath);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        //如果分享的时候，该已经开启，那么微信开始这个activity时，会调用onNewIntent，所以这里要处理微信的返回结果
        setIntent(intent);
        AlterDialogFragment.mWXApi.handleIntent(intent, this);

        if (mFragment != null) {
            mFragment.dismiss();
        }
    }

    private void createDialog(String title, String desc) {
        vipDialog = CommonDeleteFragment.newInstance(title, desc);
        vipDialog.show(getSupportFragmentManager(), "CommonDeleteFragment");
    }

    @Override
    public void onConfirm() {
        startActivity(new Intent(this,BecomeVipActivity.class));
    }
}
