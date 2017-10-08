package dhcc.cn.com.fix_phone.ui.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import dhcc.cn.com.fix_phone.R;
import dhcc.cn.com.fix_phone.adapter.AddPhotoAdapter;
import dhcc.cn.com.fix_phone.base.BaseActivity;
import dhcc.cn.com.fix_phone.utils.FileUtils;
import dhcc.cn.com.fix_phone.utils.UIUtils;

/**
 * Created by songyang on 2017\9\22 0022.
 */

public class FeedBackActivity extends BaseActivity{

    public static String TAKEPHOTO_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + "/DCIM/Camera/";
    public static final int COMPLETE_RESULT_CODE = 21; //上传完成返回
    private final static int CAMERA_REQUEST_CODE = 22;//拍照请求code
    private final static int IMAGE_REQUEST_CODE = 23;//图库
    private final static int CAMERA_DISPOSE_CODE = 25;//图片处理

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
    private String photo_path = "";

    @Override
    public int getLayoutId() {
        return R.layout.activity_feedback;
    }

    @OnClick(R.id.btn_confirm)
    public void onClick(){
//        if(mList != null && mList.size() > 1){
//            showProgressMessage(getString(com.magic.photoviewlib.R.string.uploading));
//            ImageRequestListener requestListener = new ImageRequest();
//            requestListener.requestQINIUConfig(userId, token, 1);
//        }else {
//            Toast.makeText(getContext(), getString(com.magic.photoviewlib.R.string.please_add_photo), Toast.LENGTH_SHORT).show();
//        }
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
                    addSelectDialog();
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

    private void addSelectDialog() {
        AlertDialog.Builder ab = new AlertDialog.Builder(this);
        ab.setItems(new String[]{"拍照", "从手机相册里选择"}, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0) {
                    photograph();
                } else {
                    getImageFromStorage();
                }
            }
        });
        ab.create().show();
    }

    /**
     * 拍照
     */
    private void photograph() {
        photo_path = TAKEPHOTO_PATH + getCurrentTime("yyyyMMddHHmmss") + ".jpg";
        File file = FileUtils.createFile(photo_path);
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
        intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
        startActivityForResult(intent, CAMERA_REQUEST_CODE);
    }

    /**
     * 图库
     */
    private void getImageFromStorage() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, IMAGE_REQUEST_CODE);
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
        String path = "";
        if (requestCode == CAMERA_REQUEST_CODE) {
            System.out.println("path: "+photo_path);
            if(resultCode != RESULT_OK) return;
            //拍照
//            imageDispose(photo_path);
        } else if (requestCode == IMAGE_REQUEST_CODE && data != null) {
            if(resultCode != RESULT_OK) return;
            //图库
            Uri selectedImage = data.getData();
            photo_path = FileUtils.getPathFromCursor(selectedImage, this);
//            imageDispose(photo_path);
        } else if (requestCode == CAMERA_DISPOSE_CODE) {
            //图片处理
            if (resultCode == Activity.RESULT_OK && data != null) {
//                path = data.getStringExtra(LPhotoEditActivity.LPHOTO_EDIT_RESULT_KEY);
            }
            if (TextUtils.isEmpty(path)) {
                path = photo_path;
            }
        } else {
            return;
        }
        //更新页面数据
        if (!TextUtils.isEmpty(photo_path)) {
            Uri localUri = Uri.parse("file://" + path);
            Intent localIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, localUri);
            sendBroadcast(localIntent);
            mList.add(mList.size() - 1, photo_path);
            if(mList.size() == 4){
                mList.remove(mList.size() - 1);
            }
            mPhotoAdapter.notifyDataSetChanged();
        }
    }

    public static String getCurrentTime(String pattern) {
        String time = "";
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        return format.format(date);
    }
}
