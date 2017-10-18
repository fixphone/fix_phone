package dhcc.cn.com.fix_phone.ui.activity;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;
import com.zhihu.matisse.internal.entity.CaptureStrategy;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CountDownLatch;

import butterknife.BindView;
import butterknife.OnClick;
import dhcc.cn.com.fix_phone.Account;
import dhcc.cn.com.fix_phone.R;
import dhcc.cn.com.fix_phone.adapter.AddPhotoAdapter;
import dhcc.cn.com.fix_phone.base.BaseActivity;
import dhcc.cn.com.fix_phone.bean.UploadResponse;
import dhcc.cn.com.fix_phone.ui.widget.LoadDialog;
import dhcc.cn.com.fix_phone.utils.GsonUtils;
import dhcc.cn.com.fix_phone.utils.UIUtils;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import okhttp3.Call;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by songyang on 2017\9\22 0022.
 *
 */

public class FeedBackActivity extends BaseActivity{

    private static final String TAG = "FeedBackActivity";
    public static String TAKEPHOTO_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + "/DCIM/Camera/";
    private static final int REQUEST_CODE_CHOOSE_PHOTO = 100;

    @BindView(R.id.title_name)
    TextView title_name;
    @BindView(R.id.photo_select_recycle_view)
    RecyclerView mRecycleView;
    @BindView(R.id.content_et)
    EditText content_et;
    @BindView(R.id.count_tv)
    TextView count_tv;

    private AddPhotoAdapter mPhotoAdapter;
    private List<String> mList;
    private CopyOnWriteArrayList<String> writeList = new CopyOnWriteArrayList<>();
    private String photo_path = "";

    @Override
    public int getLayoutId() {
        return R.layout.activity_feedback;
    }

    @OnClick(R.id.btn_confirm)
    public void onClick(){
        LoadDialog.show(this);
        uploadPhoto();
    }

    @OnClick(R.id.title_back)
    public void backClick(){
        finish();
    }

    @Override
    protected void initEvent() {
        super.initEvent();
        title_name.setText("投诉建议");
        content_et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                count_tv.setText(s.length() + "/300");
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        initRecyclerView();
    }

    private void initRecyclerView() {
        GridLayoutManager manager = new GridLayoutManager(this, 3);
        mRecycleView.setLayoutManager(manager);
        mRecycleView.setItemAnimator(new DefaultItemAnimator());
        mRecycleView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                outRect.bottom = UIUtils.dip2px(15);
            }
        });
        mPhotoAdapter = new AddPhotoAdapter(this);
        mRecycleView.setAdapter(mPhotoAdapter);
        mPhotoAdapter.setOnItemClickListener(new AddPhotoAdapter.OnLocationClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (position == mList.size() - 1 && TextUtils.isEmpty(mList.get(position))) {
                    applyPermission(4 - mPhotoAdapter.getItemCount());
                } else {
                    //点击此处应该是删除还是编辑？如果是编辑，删除在哪里？该如何设计？
                    deleteDialog(position);
                }
            }
        });
        if (mList == null) {
            mList = new ArrayList<>();
        }
        mList.add("");
        mPhotoAdapter.setList(mList);
    }

    private void deleteDialog(final int position) {
        new AlertDialog.Builder(this).setTitle("删除").setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                if (mList != null && mList.size() > 1) {
                    mList.remove(position);
                    if(!TextUtils.isEmpty(mList.get(mList.size() - 1))){
                        mList.add("");
                    }
                    mPhotoAdapter.notifyDataSetChanged();
                }
            }
        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        }).show();
    }

    @SuppressWarnings("AccessStaticViaInstance")
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_CHOOSE_PHOTO && resultCode == RESULT_OK) {
            List<String> strings = Matisse.obtainPathResult(data);
            if (!strings.isEmpty()) {
                mList.addAll(mList.size() - 1, strings);
                if(mList.size() == 4){
                    mList.remove(mList.size() - 1);
                }
                mPhotoAdapter.notifyDataSetChanged();
            }
        }
    }

    public static String getCurrentTime(String pattern) {
        String time = "";
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        return format.format(date);
    }

    private void applyPermission(final int number) {
        RxPermissions rxPermissions = new RxPermissions(this);
        rxPermissions.request(Manifest.permission.CAMERA).subscribe(new Observer<Boolean>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(Boolean aBoolean) {
                if (aBoolean) {
                    selectPhoto(number);
                } else {
                    Toast.makeText(FeedBackActivity.this, R.string.permission_request_denied, Toast.LENGTH_LONG).show();
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

    private void selectPhoto(int number) {
        Matisse.from(this)
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

    private void uploadPhoto() {
        List<String> photoList = removeNull(mList);
        if (photoList.isEmpty()) {
            postUploadMessage();
        } else {
            writeList = new CopyOnWriteArrayList<>();
            final CountDownLatch startSignal = new CountDownLatch(photoList.size());
            for (int i = 0; i < photoList.size(); i++) {
                final File file = new File(photoList.get(i));
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            uploadPhotoFile(writeList, file, startSignal);
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

    private void uploadPhotoFile(final CopyOnWriteArrayList<String> list, File file, final CountDownLatch startSignal) throws IOException {
        Response response = OkHttpUtils
                .post()
                .url("http://120.77.202.151:8080/Busi/UploadPicture")
                .addFile("mFile", file.getName(), file)
                .addHeader("accessKey", "JHD2017")
                .addHeader("accessToken", Account.getAccessToken())
                .build()
                .execute();
        Log.d(TAG, "uploadPhotoFile: " + response.body().string());
        Gson gson = new Gson();
        UploadResponse uploadResponse = gson.fromJson(response.body().string(), UploadResponse.class);
        list.add(uploadResponse.FObject.uuid);
        startSignal.countDown();
    }

    private void postUploadMessage() {
        OkHttpUtils
                .post()
                .url("http://120.77.202.151:8080/Suggestion/Add")
                .addParams("content", content_et.getText().toString())
                .addParams("images", writeList.isEmpty() ? "" : GsonUtils.toJson(writeList))
                .addHeader("accessKey", "JHD2017")
                .addHeader("accessToken", Account.getAccessToken())
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        LoadDialog.dismiss(FeedBackActivity.this);
                        Log.d(TAG, "onError: " + e.toString());
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        LoadDialog.dismiss(FeedBackActivity.this);
                        Log.d(TAG, "onResponse: " + response);
                    }
                });
    }

    private List<String> removeNull(List<String> list){
        List<String> temp = new ArrayList<>();
        for(String s : list){
            if(!TextUtils.isEmpty(s)){
                temp.add(s);
            }
        }
        return temp;
    }
}
