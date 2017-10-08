package dhcc.cn.com.fix_phone.ui.activity;

import android.content.Intent;
import android.graphics.Rect;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.BindView;
import dhcc.cn.com.fix_phone.MyApplication;
import dhcc.cn.com.fix_phone.R;
import dhcc.cn.com.fix_phone.adapter.ImageProductAdapter;
import dhcc.cn.com.fix_phone.base.BaseActivity;
import dhcc.cn.com.fix_phone.event.ProductImageEvent;
import dhcc.cn.com.fix_phone.remote.ApiManager;
import dhcc.cn.com.fix_phone.ui.fragment.CommonDeleteFragment;

/**
 * 2017/10/1 16
 */
public class MyProductActivity extends BaseActivity {

    @BindView(R.id.toolbar_title)
    TextView     mToolbarTitle;
    @BindView(R.id.toolbar)
    Toolbar      mToolbar;
    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;

    private String              mUserID;
    private int                 mType;
    private ImageProductAdapter mImageAdapter;
    private List<String>        mStrings;

    @Override
    public int getLayoutId() {
        return R.layout.activity_product;
    }

    @Override
    protected void init() {
        Intent intent = getIntent();
        mUserID = MyApplication.getLoginResponse().FObject.userID;
        mType = intent.getIntExtra("type", 0);
        EventBus.getDefault().register(this);
    }

    @Override
    protected void destroy() {
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void initView() {
        if (mType == 1) {
            mToolbarTitle.setText("店铺广告");
        } else {
            mToolbarTitle.setText("店铺产品");
        }
        GridLayoutManager layout = new GridLayoutManager(this, 3);
        mRecyclerView.setLayoutManager(layout);
        mRecyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                outRect.set(5, 5, 5, 5);
            }
        });
        mImageAdapter = new ImageProductAdapter(R.layout.item_image_check, null);
        mRecyclerView.setAdapter(mImageAdapter);
    }

    @Override
    protected void initData() {
        if (mType == 1) {
            ApiManager.Instance().GetStoreList(mUserID);
        } else {
            ApiManager.Instance().getProductList(mUserID);
        }
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

        mImageAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                ImagePagerActivity.ImageSize imageSize = new ImagePagerActivity.ImageSize(view.getMeasuredWidth(), view.getMeasuredHeight());
                ImagePagerActivity.startImagePagerActivity(MyProductActivity.this, mStrings, position, imageSize);
            }
        });

        mImageAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                if (mType == 1) {
                    createDialog("提示", "上传广告图片，为你的店铺打个免费的广告吧！");
                } else {
                    createDialog("注意", "删除的产品图片不会显示在店铺主页了哦！");
                }
            }
        });
    }

    private void createDialog(String title, String desc) {
        CommonDeleteFragment fragment = CommonDeleteFragment.newInstance(title, desc);
        fragment.show(getSupportFragmentManager(), "CommonDeleteFragment");
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onshowImage(ProductImageEvent event) {
        mStrings = event.mProductImage.FObject.list;
        mImageAdapter.setNewData(mStrings);
    }

}
