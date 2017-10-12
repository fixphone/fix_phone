package dhcc.cn.com.fix_phone.ui.activity;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.io.IOException;

import butterknife.BindView;
import butterknife.OnClick;
import dhcc.cn.com.fix_phone.R;
import dhcc.cn.com.fix_phone.base.BaseActivity;
import dhcc.cn.com.fix_phone.bean.BusinessResponse;
import dhcc.cn.com.fix_phone.utils.ImageActions;

/**
 * Created by Administrator on 2017/9/21 0021.
 *
 */

public class SelectHeaderActivity extends BaseActivity{

    private static final int GALLERY_REQUEST_CODE = 0;
    private static final int CAMERA_REQUEST_CODE = 1;
    private static final int CROP_REQUEST_CODE = 2;

    @BindView(R.id.title_name)
    TextView title_name;
    @BindView(R.id.header_icon)
    ImageView header_icon;

    private Intent resultIntent;
    private BusinessResponse mResponse;

    @Override
    public int getLayoutId() {
        return R.layout.activity_select_header;
    }

    @Override
    protected void initEvent() {
        super.initEvent();
        title_name.setText("修改店铺头像");
    }

    @Override
    protected void initData() {
        super.initData();
        Intent intent = getIntent();
        if(intent != null && intent.hasExtra("Action")){
            mResponse = (BusinessResponse)getIntent().getSerializableExtra("Action");
            Glide.with(this).load(mResponse.FObject.headUrl).diskCacheStrategy(DiskCacheStrategy.ALL).into(header_icon);
        }
    }

    @OnClick({R.id.title_back, R.id.photo_take_btn, R.id.photo_album_btn})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.title_back:
                finish();
                break;
            case R.id.photo_take_btn:
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
                break;
            case R.id.photo_album_btn:
                Intent intent = ImageActions.actionPickImage();
                startActivityForResult(intent, GALLERY_REQUEST_CODE);
                break;
            default:
                break;
        }
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

    public void upLoadingHeadPic(String path){

    }
}
