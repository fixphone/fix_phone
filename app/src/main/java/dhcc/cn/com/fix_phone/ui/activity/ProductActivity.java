package dhcc.cn.com.fix_phone.ui.activity;

import android.content.Intent;
import android.graphics.Rect;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import dhcc.cn.com.fix_phone.R;
import dhcc.cn.com.fix_phone.adapter.ImageAdapter;
import dhcc.cn.com.fix_phone.base.BaseActivity;
import dhcc.cn.com.fix_phone.event.ProductImageEvent;
import dhcc.cn.com.fix_phone.remote.ApiManager;
import io.rong.imkit.RongIM;

/**
 * 2017/10/1 16
 */
public class ProductActivity extends BaseActivity {

    @BindView(R.id.toolbar_title)
    TextView       mToolbarTitle;
    @BindView(R.id.imageView_communication)
    ImageView      mImageViewCommunication;
    @BindView(R.id.toolbar)
    Toolbar        mToolbar;
    @BindView(R.id.recyclerView)
    RecyclerView   mRecyclerView;
    @BindView(R.id.relativeLayout)
    RelativeLayout mRelativeLayout;

    private String       mName;
    private String       mHeadurl;
    private String       mUserID;
    private String       mweChatId;
    private ImageAdapter mImageAdapter;
    private List<String> mStrings;
    private int          type;

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
        mweChatId = intent.getStringExtra("weChatId");
        type = intent.getIntExtra("type", 0);
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
        mImageAdapter = new ImageAdapter(R.layout.item_image, null);
        mRecyclerView.setAdapter(mImageAdapter);
        if (type == 1) {
            mImageViewCommunication.setVisibility(View.GONE);
            mRelativeLayout.setVisibility(View.GONE);
        }
    }

    @Override
    protected void initData() {
        ApiManager.Instance().getProductList(mUserID);
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
                try {
                    RongIM.getInstance().startPrivateChat(ProductActivity.this, mweChatId, mName);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        mImageAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                ImagePagerActivity.ImageSize imageSize = new ImagePagerActivity.ImageSize(view.getMeasuredWidth(), view.getMeasuredHeight());
                ImagePagerActivity.startImagePagerActivity(ProductActivity.this, mStrings, position, imageSize);
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
    public void onshowImage(ProductImageEvent event) {
        mStrings = event.mProductImage.FObject.list;
        mImageAdapter.setNewData(mStrings);
    }

}
