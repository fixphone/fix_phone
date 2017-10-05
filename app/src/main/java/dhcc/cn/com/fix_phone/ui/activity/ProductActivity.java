package dhcc.cn.com.fix_phone.ui.activity;

import android.content.Intent;
import android.graphics.Rect;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.OnClick;
import dhcc.cn.com.fix_phone.MyApplication;
import dhcc.cn.com.fix_phone.R;
import dhcc.cn.com.fix_phone.base.BaseActivity;
import dhcc.cn.com.fix_phone.event.BusinessEvent;
import dhcc.cn.com.fix_phone.remote.ApiManager;

/**
 * 2017/10/1 16
 */
public class ProductActivity extends BaseActivity {

    public static final    int    MAX_NUMBER         = 20;
    public                 int    pageIndex          = 1;
    public                 int    pageSize           = 20;

    @BindView(R.id.toolbar_title)
    TextView     mToolbarTitle;
    @BindView(R.id.imageView_communication)
    ImageView    mImageViewCommunication;
    @BindView(R.id.toolbar)
    Toolbar      mToolbar;
    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;

    private String mName;
    private String mHeadurl;
    private String mUserID;

    @Override
    public int getLayoutId() {
        return R.layout.activity_product;
    }

    @Override
    protected void init() {
        Intent intent = getIntent();
        mName = intent.getStringExtra("name");
        mHeadurl = intent.getStringExtra("headurl");
        mUserID = intent.getStringExtra("userID");
        EventBus.getDefault().register(this);
    }

    @Override
    protected void destroy() {
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void initView() {
        mToolbarTitle.setText(mName);
        GridLayoutManager layout = new GridLayoutManager(this, 3);
        mRecyclerView.setLayoutManager(layout);
        mRecyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                outRect.set(5, 5, 5, 5);
            }
        });
    }

    @Override
    protected void initData() {
        ApiManager.Instance().GetIconList(mUserID,
                MyApplication.getCurrentTypeId(),
                pageIndex,
                pageSize,
                MAX_NUMBER,"");
    }

    @Override
    protected void initEvent() {
        super.initEvent();
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        mImageViewCommunication.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    @OnClick({R.id.textView_circle, R.id.textView_home})
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.textView_circle:
                finish();
                break;
            case R.id.textView_home:
                startActivity(new Intent(ProductActivity.this, MainActivity.class));
                break;
        }
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onShowAd(BusinessEvent event) {

    }

}
