package dhcc.cn.com.fix_phone.ui.activity;

import android.Manifest;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.zhy.http.okhttp.callback.StringCallback;

import java.io.File;
import java.io.IOException;

import butterknife.BindView;
import butterknife.OnClick;
import dhcc.cn.com.fix_phone.Account;
import dhcc.cn.com.fix_phone.R;
import dhcc.cn.com.fix_phone.base.BaseActivity;
import dhcc.cn.com.fix_phone.bean.BusinessResponse;
import dhcc.cn.com.fix_phone.bean.UploadIconResponse;
import dhcc.cn.com.fix_phone.remote.ApiManager;
import dhcc.cn.com.fix_phone.ui.widget.LoadDialog;
import dhcc.cn.com.fix_phone.utils.GsonUtils;
import dhcc.cn.com.fix_phone.utils.ImageActions;
import dhcc.cn.com.fix_phone.utils.UploadFileUtil;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Created by Administrator on 2017/9/21 0021.
 */

public class SelectHeaderActivity extends BaseActivity {

    private static final String TAG                       = "SelectHeaderActivity";
    private static final int    REQUEST_CODE_CHOOSE_PHOTO = 0x0100;
    public static final  int    RESULT_CODE               = 0x0020;
    private static final int    GALLERY_REQUEST_CODE      = 0;
    private static final int    CAMERA_REQUEST_CODE       = 1;
    private static final int    CROP_REQUEST_CODE         = 2;

    @BindView(R.id.title_name)
    TextView  title_name;
    @BindView(R.id.header_icon)
    ImageView header_icon;

    private Intent           resultIntent;
    private BusinessResponse mResponse;
    private LoadDialog       loadDialog;

    @Override
    public int getLayoutId() {
        return R.layout.activity_select_header;
    }

    @Override
    protected void initEvent() {
        super.initEvent();
        title_name.setText("修改店铺头像");
        loadDialog = new LoadDialog(this, false, "");
    }

    @Override
    protected void initData() {
        super.initData();
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("Action")) {
            mResponse = (BusinessResponse) getIntent().getSerializableExtra("Action");
            Glide.with(this).load(mResponse.FObject.headUrl).diskCacheStrategy(DiskCacheStrategy.ALL).into(header_icon);
        }
    }

    @OnClick({R.id.title_back, R.id.photo_take_btn, R.id.photo_album_btn})
    public void onClick(View view) {
        RxPermissions rxPermissions = new RxPermissions(this);
        switch (view.getId()) {
            case R.id.title_back:
                finish();
                break;
            case R.id.photo_take_btn:
                rxPermissions.request(Manifest.permission.CAMERA).subscribe(new Observer<Boolean>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Boolean aBoolean) {
                        if (aBoolean) {
                            camera();
                        } else {
                            Toast.makeText(SelectHeaderActivity.this, R.string.permission_request_denied, Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });

                break;
            case R.id.photo_album_btn:
                rxPermissions.request(Manifest.permission.WRITE_EXTERNAL_STORAGE).subscribe(new Observer<Boolean>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Boolean aBoolean) {
                        if (aBoolean) {
                            getPhoto();
                        } else {
                            Toast.makeText(SelectHeaderActivity.this, R.string.permission_request_denied, Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
                break;
            default:
                break;
        }
    }

    private void getPhoto() {
        Intent intent = ImageActions.actionPickImage();
        startActivityForResult(intent, GALLERY_REQUEST_CODE);
    }

    private void camera() {
        if (!ImageActions.isImageCaptureAvailable(this)) {
            Toast.makeText(this, "您的手机不主持拍照功能", Toast.LENGTH_SHORT).show();
            return;
        }
        try {
            resultIntent = ImageActions.actionCapture(this);
        } catch (IOException e) {
            Toast.makeText(this, "存储空间不可用，无法使用拍照功能", Toast.LENGTH_SHORT).show();
            return;
        }
        startActivityForResult(resultIntent, CAMERA_REQUEST_CODE);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == GALLERY_REQUEST_CODE) {
                cropImage(data.getData());
            } else if (requestCode == CAMERA_REQUEST_CODE) {
                Uri image = ImageActions.capturedImage(this, resultIntent, resultCode, data);
                cropImage(image);
            } else if (requestCode == CROP_REQUEST_CODE) {
                Uri imgSaveUri = ImageActions.croppedImage(this, resultIntent, resultCode, data);
                String path = imgSaveUri.getPath();
                upLoadingHeadPic(path);
            }
        }
    }

    private boolean cropImage(Uri image) {
        try {
            resultIntent = ImageActions.actionCrop(this, image, 200);
            startActivityForResult(resultIntent, CROP_REQUEST_CODE);
            return true;
        } catch (ActivityNotFoundException e) {
            return false;
        }
    }

    public void upLoadingHeadPic(String path) {
        loadDialog.show();
        File file = new File(path);
        UploadFileUtil.uploadPhotoFile(file, "/Account/UploadIcon", new StringCallback() {
            @Override
            public void onError(okhttp3.Call call, Exception e, int id) {
                if (loadDialog != null)
                    loadDialog.dismiss();
                Log.d(TAG, "onError: " + e);
            }

            @Override
            public void onResponse(String response, int id) {
                if (loadDialog != null)
                    loadDialog.dismiss();
                try {
                    UploadIconResponse resp = GsonUtils.getSingleBean(response, UploadIconResponse.class);
                    Glide.with(SelectHeaderActivity.this).load(resp.FObject.fullUrl).diskCacheStrategy(DiskCacheStrategy.ALL).into(header_icon);
                    mResponse.FObject.headUrl = resp.FObject.fullUrl;
                    ApiManager.Instance().getSelfInfo(Account.getUserId());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void onBackPressedSupport() {
        setResult(RESULT_CODE, getIntent().putExtra("result", mResponse));
        super.onBackPressedSupport();
    }
}
