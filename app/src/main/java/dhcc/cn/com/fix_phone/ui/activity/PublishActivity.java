package dhcc.cn.com.fix_phone.ui.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Rect;
import android.net.Uri;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.rubensousa.bottomsheetbuilder.BottomSheetBuilder;
import com.github.rubensousa.bottomsheetbuilder.adapter.BottomSheetItemClickListener;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;
import com.zhihu.matisse.internal.entity.CaptureStrategy;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import dhcc.cn.com.fix_phone.R;
import dhcc.cn.com.fix_phone.base.BaseActivity;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * 2017/9/26 23
 */
public class PublishActivity extends BaseActivity {

    private static final int REQUEST_CODE_CHOOSE_PHOTO = 100;
    private static final int REQUEST_CODE_CHOOSE_VIDEO = 200;

    @BindView(R.id.title_name)
    TextView       mTitleName;
    @BindView(R.id.container_rl)
    RelativeLayout mContainerRl;
    @BindView(R.id.content_et)
    EditText       mContentEt;
    @BindView(R.id.count_tv)
    TextView       mCountTv;
    @BindView(R.id.photo_select_recycle_view)
    RecyclerView   mRecyclerView;
    @BindView(R.id.btn_confirm)
    Button         mBtnConfirm;
    @BindView(R.id.textView_type)
    TextView       mTextViewType;
    @BindView(R.id.imageView_selector)
    ImageView      mSelector;
    @BindView(R.id.textView_video)
    TextView       mTextViewVideo;

    private BottomSheetDialog mDialog;
    private int               mType;
    private List<Uri>         mSelected;

    @Override
    protected void init() {
        Intent intent = getIntent();
        mType = intent.getIntExtra("type", 0);
    }

    @Override
    protected void initData() {
        mTitleName.setText("发布生意");
        if (mType == 1) {
            mTextViewVideo.setVisibility(View.GONE);
        } else {
            mTextViewVideo.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void initView() {
        GridLayoutManager manager = new GridLayoutManager(this, 3);
        mRecyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                outRect.set(3, 3, 3, 3);
            }
        });
        mRecyclerView.setLayoutManager(manager);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_publish;
    }

    @Override
    protected void initEvent() {

    }

    @OnClick({R.id.title_back, R.id.textView_type, R.id.btn_confirm, R.id.imageView_selector})
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.title_back:
                finish();
                break;
            case R.id.textView_type:
                createBottomDialog();
                break;
            case R.id.btn_confirm:
                break;
            case R.id.imageView_selector:
                applyPermission();
                break;
        }
    }

    private void applyPermission() {
        RxPermissions rxPermissions = new RxPermissions(this);
        rxPermissions.request(Manifest.permission.WRITE_EXTERNAL_STORAGE).subscribe(new Observer<Boolean>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(Boolean aBoolean) {
                if (aBoolean) {
                    handleSelector();
                } else {
                    Toast.makeText(PublishActivity.this, R.string.permission_request_denied, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });
    }

    private void handleSelector() {
        if (mType == 1) {
            selectPhoto();
        } else if (mType == 2) {
            createVideo();
        }
    }

    private void createVideo() {
        Matisse.from(PublishActivity.this)
                .choose(MimeType.ofVideo()) // 选择 mime 的类型
                .countable(true)
                .maxSelectable(1) // 图片选择的最多数量
                .gridExpectedSize(getResources().getDimensionPixelSize(R.dimen.grid_expected_size))
                .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
                .thumbnailScale(0.85f) // 缩略图的比例
                .imageEngine(new GlideEngine()) // 使用的图片加载引擎
                .forResult(REQUEST_CODE_CHOOSE_VIDEO); // 设置作为标记的请求码
    }

    private void selectPhoto() {
        Matisse.from(PublishActivity.this)
                .choose(MimeType.ofImage()) // 选择 mime 的类型
                .countable(true)
                .capture(true)
                .captureStrategy(new CaptureStrategy(true, "com.zhihu.matisse.sample.fileprovider"))
                .maxSelectable(9) // 图片选择的最多数量
                .gridExpectedSize(getResources().getDimensionPixelSize(R.dimen.grid_expected_size))
                .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
                .thumbnailScale(0.85f) // 缩略图的比例
                .imageEngine(new GlideEngine()) // 使用的图片加载引擎
                .forResult(REQUEST_CODE_CHOOSE_PHOTO); // 设置作为标记的请求码

    }

    private void createBottomDialog() {
        if (mDialog != null) {
            mDialog.dismiss();
        }
        mDialog = new BottomSheetBuilder(this, R.style.AppTheme_BottomSheetDialog_Custom)
                .setMode(BottomSheetBuilder.MODE_LIST)
                .setMenu(R.menu.menu_botton_type)
                .expandOnStart(true)
                .setItemClickListener(new BottomSheetItemClickListener() {
                    @Override
                    public void onBottomSheetItemClick(MenuItem item) {
                        int itemId = item.getItemId();
                        Log.d("Item click", item.getTitle() + "");
                        mDialog.dismiss();
                    }
                })
                .createDialog();

        mDialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_CHOOSE_PHOTO && resultCode == RESULT_OK) {
            mSelected = Matisse.obtainResult(data);
            Log.d("Matisse", "mSelected: " + mSelected);
        }
    }
}
