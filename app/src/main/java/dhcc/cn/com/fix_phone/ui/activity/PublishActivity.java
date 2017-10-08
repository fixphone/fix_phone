package dhcc.cn.com.fix_phone.ui.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Rect;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.github.rubensousa.bottomsheetbuilder.BottomSheetBuilder;
import com.github.rubensousa.bottomsheetbuilder.adapter.BottomSheetItemClickListener;
import com.google.gson.Gson;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;
import com.zhihu.matisse.internal.entity.CaptureStrategy;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CountDownLatch;

import butterknife.BindView;
import butterknife.OnClick;
import dhcc.cn.com.fix_phone.MyApplication;
import dhcc.cn.com.fix_phone.R;
import dhcc.cn.com.fix_phone.adapter.SelectImageAdapter;
import dhcc.cn.com.fix_phone.base.BaseActivity;
import dhcc.cn.com.fix_phone.bean.UploadResponse;
import dhcc.cn.com.fix_phone.conf.CircleDefaultData;
import dhcc.cn.com.fix_phone.event.PublishSuccessEvent;
import dhcc.cn.com.fix_phone.utils.FileUtils;
import dhcc.cn.com.fix_phone.utils.GsonUtils;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

import static dhcc.cn.com.fix_phone.ui.activity.FeedBackActivity.TAKEPHOTO_PATH;
import static dhcc.cn.com.fix_phone.ui.activity.FeedBackActivity.getCurrentTime;

/**
 * 2017/9/26 23
 */
public class PublishActivity extends BaseActivity implements SelectImageAdapter.OnAddClickListener, SelectImageAdapter.OnDeleteClickListener {

    private static final String TAG                       = "PublishActivity";
    private static final int    REQUEST_CODE_CHOOSE_PHOTO = 100;
    private static final int    REQUEST_CODE_CHOOSE_VIDEO = 200;
    private final static int    CAMERA_REQUEST_CODE       = 22;//拍摄视频请求code

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
    @BindView(R.id.LinearLayout_show)
    LinearLayout   mLinearLayout;

    private BottomSheetDialog    mDialog;
    private int                  mType;
    private Map<Integer, String> mMap;
    private String               mSelectType;
    private SelectImageAdapter   mAdapter;
    private int                          currentNumber = 9;
    private CopyOnWriteArrayList<String> mList         = new CopyOnWriteArrayList<>();
    private File mFileVideo;

    @Override
    protected void init() {
        Intent intent = getIntent();
        mType = intent.getIntExtra("type", 0);
        mMap = CircleDefaultData.getCirCleDefailtMap();
        mAdapter = new SelectImageAdapter(this, null);
    }

    @Override
    protected void initData() {
        mTitleName.setText("发布生意");
        if (mType == 1) {
            mLinearLayout.setVisibility(View.GONE);
            mRecyclerView.setVisibility(View.VISIBLE);
        } else {
            mLinearLayout.setVisibility(View.VISIBLE);
            mRecyclerView.setVisibility(View.GONE);
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
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_publish;
    }

    @Override
    protected void initEvent() {
        mAdapter.setOnAddClickListener(this);
        mAdapter.setOnDeleteClickListener(this);
    }

    @OnClick({R.id.title_back, R.id.textView_type, R.id.btn_confirm, R.id.imageView_selector})
    public void onHandleClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.title_back:
                finish();
                break;
            case R.id.textView_type:
                createBottomDialog();
                break;
            case R.id.btn_confirm:
                if (TextUtils.isEmpty(mSelectType)) {
                    Toast.makeText(this, "请选择要发布生意的类别", Toast.LENGTH_SHORT).show();
                } else {
                    confirmCircle();
                }
                break;
            case R.id.imageView_selector:
                applyPermission();
                break;
        }
    }

    private void confirmCircle() {
        if (mType == 1) {
            uploadPhoto();
        } else if (mType == 2) {
            uploadVideo();
        }
    }

    private void uploadVideo() {
        if (mFileVideo == null || !mFileVideo.exists()) {
            postUploadMessage();
        } else {
            mList = new CopyOnWriteArrayList<>();
            Log.d(TAG, "uploadVideo: "+ mFileVideo.length());
            final CountDownLatch startSignal = new CountDownLatch(1);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        uploadVideoFile(mList, mFileVideo, startSignal);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start();

            // 成功以后
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        startSignal.await();
                        postUploadMessage();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }

    private void uploadPhoto() {
        if (mAdapter.getData().isEmpty()) {
            postUploadMessage();
        } else {
            mList = new CopyOnWriteArrayList<>();
            final CountDownLatch startSignal = new CountDownLatch(mAdapter.getData().size());
            for (int i = 0; i < mAdapter.getData().size(); i++) {
                final File file = new File(mAdapter.getData().get(i));
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            uploadPhotoFile(mList, file, startSignal);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
            // 成功以后
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        startSignal.await();
                        postUploadMessage();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }

    private void postUploadMessage() {
        OkHttpUtils
                .post()
                .url("http://120.77.202.151:8080/Busi/Publish")
                .addParams("type", mSelectType)
                .addParams("content", getEditText())
                .addParams("images", mList.isEmpty() ? "" : GsonUtils.toJson(mList))
                .addParams("videoId", mList.isEmpty() ? "" : mList.get(0))
                .addHeader("accessKey", "JHD2017")
                .addHeader("accessToken", MyApplication.getLoginResponse().FObject.accessToken)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Request request, Exception e) {
                        Log.d(TAG, "onError: " + e.toString());
                    }

                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, "onResponse: " + response);
                        EventBus.getDefault().post(new PublishSuccessEvent(true));
                        finish();
                    }
                });
    }

    private void uploadPhotoFile(final CopyOnWriteArrayList<String> list, File file, final CountDownLatch startSignal) throws IOException {
        Response response = OkHttpUtils
                .post()
                .url("http://120.77.202.151:8080/Busi/UploadPicture")
                .addFile("mFile", file.getName(), file)
                .addHeader("accessKey", "JHD2017")
                .addHeader("accessToken", MyApplication.getLoginResponse().FObject.accessToken)
                .build()
                .execute();
        Log.d(TAG, "uploadVideoFile: " + response.body().string());
        Gson gson = new Gson();
        UploadResponse uploadResponse = gson.fromJson(response.body().string(), UploadResponse.class);
        list.add(uploadResponse.FObject.uuid);
        startSignal.countDown();
    }

    private void uploadVideoFile(final CopyOnWriteArrayList<String> list, File file, final CountDownLatch startSignal) throws IOException {
        Response response = OkHttpUtils
                .post()
                .url("http://120.77.202.151:8080/Busi/UploadVideo")
                .addFile("mFile", file.getName(), file)
                .addHeader("accessKey", "JHD2017")
                .addHeader("accessToken", MyApplication.getLoginResponse().FObject.accessToken)
                .build()
                .execute();
        Log.d(TAG, "uploadVideoFile: " + response.body().string());
        Gson gson = new Gson();
        UploadResponse uploadResponse = gson.fromJson(response.body().string(), UploadResponse.class);
        list.add(uploadResponse.FObject.uuid);
        startSignal.countDown();
    }

    private void applyPermission() {
        RxPermissions rxPermissions = new RxPermissions(this);
        rxPermissions.request(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA).subscribe(new Observer<Boolean>() {
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
            selectPhoto(currentNumber - mAdapter.getData().size());
        } else if (mType == 2) {
            createVideo();
        }
    }

    private void createVideo() {
        getVideo();
    }

    private String photo_path = "";

    /**
     * 拍摄视频
     */
    private void getVideo() {
        photo_path = TAKEPHOTO_PATH + getCurrentTime("yyyyMMddHHmmss") + ".mp4";
        File file = FileUtils.createFile(photo_path);
        Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
        intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
        startActivityForResult(intent, CAMERA_REQUEST_CODE);
    }

    private void selectPhoto(int number) {
        Matisse.from(PublishActivity.this)
                .choose(MimeType.ofImage()) // 选择 mime 的类型
                .countable(true)
                .capture(true)
                .captureStrategy(new CaptureStrategy(true, "dhcc.cn.com.fix_phone.FileProvider"))
                .maxSelectable(number) // 图片选择的最多数量
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
                .expandOnStart(false)
                .setItemClickListener(new BottomSheetItemClickListener() {
                    @Override
                    public void onBottomSheetItemClick(MenuItem item) {
                        int itemId = item.getItemId();
                        mSelectType = mMap.get(itemId);
                        mTextViewType.setText("当前类别为：" + item.getTitle());
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
            List<String> strings = Matisse.obtainPathResult(data);
            if (!strings.isEmpty()) {
                mAdapter.addData(strings);
            }
        }

        if (requestCode == CAMERA_REQUEST_CODE && resultCode == RESULT_OK) {
            mFileVideo = new File(photo_path);
            if (mFileVideo.exists() && mFileVideo.getName().endsWith("mp4")) {
                Log.d(TAG, "onActivityResult: " + photo_path);
                Glide.with(this).load(Uri.fromFile(mFileVideo)).placeholder(R.drawable.menu_0).into(mSelector);
                mTextViewVideo.setText("已拍好视频，发布您的生意圈");
            }
        }
    }

    @Override
    public void onDelete(int position) {
        mAdapter.remove(position);
    }

    @Override
    public void onAdd() {
        applyPermission();
    }

    public String getEditText() {
        String trim = mContentEt.getText().toString().trim();
        Log.d(TAG, "getEditText: " + trim);
        return trim;
    }
}
