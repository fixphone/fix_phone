package dhcc.cn.com.fix_phone.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import cn.jzvd.JZVideoPlayer;
import cn.jzvd.JZVideoPlayerStandard;
import dhcc.cn.com.fix_phone.R;

/**
 * Created by Nathen on 2017/9/19.
 */

public class ExtendsNormalActivity extends Activity {
    private static final String TAG = "ExtendsNormalActivity";

    private JZVideoPlayerStandard mJzVideoPlayerStandard;
    private String                mPath;
    private String                mImagePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_extends_normal);
        init();
        initView();
        initData();
    }

    public void init() {
        Intent intent = getIntent();
        mPath = intent.getStringExtra("path");
        mImagePath = intent.getStringExtra("imagePath");
        Log.d(TAG, "init: " + mPath);
        Log.d(TAG, "init: " + mImagePath);
    }

    public void initView() {
        mJzVideoPlayerStandard = findViewById(R.id.videoplayer);
    }

    public void initData() {
        mJzVideoPlayerStandard.setUp(mPath, JZVideoPlayerStandard.SCREEN_LAYOUT_NORMAL, "");
        Glide.with(this).load(mImagePath).diskCacheStrategy(DiskCacheStrategy.ALL).into(mJzVideoPlayerStandard.thumbImageView);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mJzVideoPlayerStandard.startVideo();
    }

    @Override
    public void onBackPressed() {
        if (JZVideoPlayer.backPress()) {
            return;
        }
        super.onBackPressed();
    }

    @Override
    protected void onPause() {
        super.onPause();
        JZVideoPlayer.releaseAllVideos();
    }
}
