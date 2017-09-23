package dhcc.cn.com.fix_phone.ui.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.malinskiy.superrecyclerview.SuperRecyclerView;

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
import dhcc.cn.com.fix_phone.ui.widget.CommentListView;
import dhcc.cn.com.fix_phone.ui.widget.DivItemDecoration;
import dhcc.cn.com.fix_phone.ui.widget.dialog.UpLoadDialog;
import dhcc.cn.com.fix_phone.utils.CommonUtils;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * @author yiw
 * @ClassName: CircleActivity
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @date 2015-12-28 下午4:21:18
 */
public class CircleActivity extends YWActivity implements CircleContract.View, EasyPermissions.PermissionCallbacks {

    protected static final String TAG = "CircleActivity";
    private CircleAdapter circleAdapter;
    private LinearLayout  edittextbody;
    private EditText      editText;
    private ImageView     sendIv;

    private int screenHeight;
    private int editTextBodyHeight;
    private int currentKeyboardH;
    private int selectCircleItemH;
    private int selectCommentItemOffset;

    private CirclePresenter     presenter;
    private CommentConfig       commentConfig;
    private SuperRecyclerView   recyclerView;
    private RelativeLayout      bodyLayout;
    private LinearLayoutManager layoutManager;
    private TextView            titleBar;

    private final static int TYPE_PULLREFRESH   = 1;
    private final static int TYPE_UPLOADREFRESH = 2;
    private UpLoadDialog                         uploadDialog;
    private SwipeRefreshLayout.OnRefreshListener refreshListener;
    private String                               mTypeId;
    private String                               mContentName;
    public static final int MAX_NUMBER = 20;
    public              int pageIndex  = 1;
    public              int pageSize   = 20;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_circle);
        init();
        presenter = new CirclePresenter(this);
        initView();

        initPermission();

        initData();
    }

    @Override
    protected void onDestroy() {
        if (presenter != null) {
            presenter.recycle();
        }
        super.onDestroy();
    }

    private void initData() {
        ApiManager.Instance().getCircleDetailAds(mTypeId);
        ApiManager.Instance().getCircleBusinessList(MAX_NUMBER, pageIndex, pageSize, mTypeId, "");
    }

    private void init() {
        Intent intent = getIntent();
        mTypeId = intent.getStringExtra("data");
        mContentName = intent.getStringExtra("name");
    }


    private void initPermission() {
        String[] perms = { Manifest.permission.WRITE_EXTERNAL_STORAGE
                , Manifest.permission.READ_EXTERNAL_STORAGE};

        if (EasyPermissions.hasPermissions(this, perms)) {
            // Already have permission, do the thing
            // ...
        } else {
            // Do not have permissions, request them now
            EasyPermissions.requestPermissions(this, "因为功能需要，需要使用相关权限，请允许",
                    100, perms);
        }
    }

    @SuppressLint({"ClickableViewAccessibility", "InlinedApi"})
    private void initView() {

        initTitle();
        initUploadDialog();

        recyclerView = (SuperRecyclerView) findViewById(R.id.recyclerView);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new DivItemDecoration(2, true));
        recyclerView.getMoreProgressView().getLayoutParams().width = ViewGroup.LayoutParams.MATCH_PARENT;

//        recyclerView.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                if (edittextbody.getVisibility() == View.VISIBLE) {
//                    updateEditTextBodyVisible(View.GONE, null);
//                    return true;
//                }
//                return false;
//            }
//        });

//        recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
//                super.onScrolled(recyclerView, dx, dy);
//            }
//
//            @Override
//            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
//                super.onScrollStateChanged(recyclerView, newState);
//                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
//                    Glide.with(CircleActivity.this).resumeRequests();
//                } else {
//                    Glide.with(CircleActivity.this).pauseRequests();
//                }
//
//            }
//        });

        circleAdapter = new CircleAdapter(this);
        circleAdapter.setCirclePresenter(presenter);
        recyclerView.setAdapter(circleAdapter);

        edittextbody = (LinearLayout) findViewById(R.id.editTextBodyLl);
        editText = (EditText) findViewById(R.id.circleEt);
        sendIv = (ImageView) findViewById(R.id.sendIv);
        sendIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (presenter != null) {
                    //发布评论
                    String content = editText.getText().toString().trim();
                    if (TextUtils.isEmpty(content)) {
                        Toast.makeText(CircleActivity.this, "评论内容不能为空...", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    presenter.addComment(content, commentConfig);
                }
                updateEditTextBodyVisible(View.GONE, null);
            }
        });

        //setViewTreeObserver();
    }

    private void initUploadDialog() {
        uploadDialog = new UpLoadDialog(this);
    }

    private void initTitle() {
        titleBar = (TextView) findViewById(R.id.toolbar_title);
        titleBar.setText(mContentName);
    }

    private void setViewTreeObserver() {
        bodyLayout = (RelativeLayout) findViewById(R.id.bodyLayout);
        final ViewTreeObserver swipeRefreshLayoutVTO = bodyLayout.getViewTreeObserver();
        swipeRefreshLayoutVTO.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {

                Rect r = new Rect();
                bodyLayout.getWindowVisibleDisplayFrame(r);
                int statusBarH = getStatusBarHeight();//状态栏高度
                int screenH = bodyLayout.getRootView().getHeight();
                if (r.top != statusBarH) {
                    //在这个demo中r.top代表的是状态栏高度，在沉浸式状态栏时r.top＝0，通过getStatusBarHeight获取状态栏高度
                    r.top = statusBarH;
                }
                int keyboardH = screenH - (r.bottom - r.top);
                Log.d(TAG, "screenH＝ " + screenH + " &keyboardH = " + keyboardH + " &r.bottom=" + r.bottom + " &top=" + r.top + " &statusBarH=" + statusBarH);

                if (keyboardH == currentKeyboardH) {//有变化时才处理，否则会陷入死循环
                    return;
                }

                currentKeyboardH = keyboardH;
                screenHeight = screenH;//应用屏幕的高度
                editTextBodyHeight = edittextbody.getHeight();

                if (keyboardH < 150) {//说明是隐藏键盘的情况
                    updateEditTextBodyVisible(View.GONE, null);
                    return;
                }
                //偏移listview
                if (layoutManager != null && commentConfig != null) {
                    layoutManager.scrollToPositionWithOffset(commentConfig.circlePosition + CircleAdapter.HEADVIEW_SIZE, getListviewOffset(commentConfig));
                }
            }
        });
    }

    /**
     * 获取状态栏高度
     *
     * @return
     */
    private int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            if (edittextbody != null && edittextbody.getVisibility() == View.VISIBLE) {
                updateEditTextBodyVisible(View.GONE, null);
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void update2DeleteCircle(String circleId) {
/*        List<CircleItem> circleItems = circleAdapter.getDatas();
        for (int i = 0; i < circleItems.size(); i++) {
            if (circleId.equals(circleItems.get(i).getId())) {
                circleItems.remove(i);
                circleAdapter.notifyDataSetChanged();
                return;
            }
        }*/
    }

    @Override
    public void update2AddFavorite(int circlePosition, FavortItem addItem) {
        if (addItem != null) {
            CircleItem item = (CircleItem) circleAdapter.getDatas().get(circlePosition);
            item.getFavorters().add(addItem);
            circleAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void update2DeleteFavort(int circlePosition, String favortId) {
        CircleItem item = (CircleItem) circleAdapter.getDatas().get(circlePosition);
        List<FavortItem> items = item.getFavorters();
        for (int i = 0; i < items.size(); i++) {
            if (favortId.equals(items.get(i).getId())) {
                items.remove(i);
                circleAdapter.notifyDataSetChanged();
                return;
            }
        }
    }

    @Override
    public void update2AddComment(int circlePosition, CommentItem addItem) {
        if (addItem != null) {
            CircleItem item = (CircleItem) circleAdapter.getDatas().get(circlePosition);
            item.getComments().add(addItem);
            circleAdapter.notifyDataSetChanged();
        }
        //清空评论文本
        editText.setText("");
    }

    @Override
    public void update2DeleteComment(int circlePosition, String commentId) {
        CircleItem item = (CircleItem) circleAdapter.getDatas().get(circlePosition);
        List<CommentItem> items = item.getComments();
        for (int i = 0; i < items.size(); i++) {
            if (commentId.equals(items.get(i).getId())) {
                items.remove(i);
                circleAdapter.notifyDataSetChanged();
                return;
            }
        }
    }

    @Override
    public void updateEditTextBodyVisible(int visibility, CommentConfig commentConfig) {
        this.commentConfig = commentConfig;
        edittextbody.setVisibility(visibility);

        measureCircleItemHighAndCommentItemOffset(commentConfig);

        if (View.VISIBLE == visibility) {
            editText.requestFocus();
            //弹出键盘
            CommonUtils.showSoftInput(editText.getContext(), editText);

        } else if (View.GONE == visibility) {
            //隐藏键盘
            CommonUtils.hideSoftInput(editText.getContext(), editText);
        }
    }

    @Override
    public void update2loadData(int loadType, List<CircleItem> datas) {
        if (loadType == TYPE_PULLREFRESH) {
            recyclerView.setRefreshing(false);
            circleAdapter.setDatas(datas);
        } else if (loadType == TYPE_UPLOADREFRESH) {
            circleAdapter.getDatas().addAll(datas);
        }
        circleAdapter.notifyDataSetChanged();

    }

    @Override
    public void updateAd(List<CircleDetailAd.FObjectBean.ListBean> lists) {
        circleAdapter.setFObjectBean(lists);
    }

    @Override
    public void updateCircleItem(List<CircleItem> circleItems) {
        circleAdapter.setDatas(circleItems);
        circleAdapter.notifyDataSetChanged();
    }

    /**
     * 测量偏移量
     *
     * @param commentConfig
     * @return
     */
    private int getListviewOffset(CommentConfig commentConfig) {
        if (commentConfig == null)
            return 0;
        //这里如果你的listview上面还有其它占高度的控件，则需要减去该控件高度，listview的headview除外。
        //int listviewOffset = mScreenHeight - mSelectCircleItemH - mCurrentKeyboardH - mEditTextBodyHeight;
        int listviewOffset = screenHeight - selectCircleItemH - currentKeyboardH - editTextBodyHeight - titleBar.getHeight();
        if (commentConfig.commentType == CommentConfig.Type.REPLY) {
            //回复评论的情况
            listviewOffset = listviewOffset + selectCommentItemOffset;
        }
        return listviewOffset;
    }

    private void measureCircleItemHighAndCommentItemOffset(CommentConfig commentConfig) {
        if (commentConfig == null)
            return;

        int firstPosition = layoutManager.findFirstVisibleItemPosition();
        //只能返回当前可见区域（列表可滚动）的子项
        View selectCircleItem = layoutManager.getChildAt(commentConfig.circlePosition + CircleAdapter.HEADVIEW_SIZE - firstPosition);

        if (selectCircleItem != null) {
            selectCircleItemH = selectCircleItem.getHeight();
        }

        if (commentConfig.commentType == CommentConfig.Type.REPLY) {
            //回复评论的情况
            CommentListView commentLv = (CommentListView) selectCircleItem.findViewById(R.id.commentList);
            if (commentLv != null) {
                //找到要回复的评论view,计算出该view距离所属动态底部的距离
                View selectCommentItem = commentLv.getChildAt(commentConfig.commentPosition);
                if (selectCommentItem != null) {
                    //选择的commentItem距选择的CircleItem底部的距离
                    selectCommentItemOffset = 0;
                    View parentView = selectCommentItem;
                    do {
                        int subItemBottom = parentView.getBottom();
                        parentView = (View) parentView.getParent();
                        if (parentView != null) {
                            selectCommentItemOffset += (parentView.getHeight() - subItemBottom);
                        }
                    } while (parentView != null && parentView != selectCircleItem);
                }
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

    }

    @Override
    public void showLoading(String msg) {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void showError(String errorMsg) {

    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {

    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        Toast.makeText(this, "您拒绝了相关权限，可能会导致相关功能不可用", Toast.LENGTH_LONG).show();
        /*if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            new AppSettingsDialog.Builder(this, getString(R.string.rationale_ask_again))
                    .setTitle(getString(R.string.title_settings_dialog))
                    .setPositiveButton(getString(R.string.setting))
                    .setNegativeButton(getString(R.string.cancel), null *//* click listener *//*)
                    .setRequestCode(RC_SETTINGS_SCREEN)
                    .build()
                    .show();
        }*/
    }

}
