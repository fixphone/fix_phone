package dhcc.cn.com.fix_phone.ui.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.OnClick;
import dhcc.cn.com.fix_phone.R;
import dhcc.cn.com.fix_phone.base.BaseFragment;
import dhcc.cn.com.fix_phone.ui.activity.FeedBackActivity;
import dhcc.cn.com.fix_phone.ui.activity.MyActivity;
import dhcc.cn.com.fix_phone.ui.activity.PersonInfoActivity;

/**
 * 2017/9/16 23
 */
public class MeFragment extends BaseFragment {

    @BindView(R.id.container_rl)
    RelativeLayout mContainerRl;
    @BindView(R.id.title_back)
    RelativeLayout mTitleBackRl;
    @BindView(R.id.title_right)
    TextView mTitleRightTv;
    @BindView(R.id.bottom_line)
    View mBottomLine;
    @BindView(R.id.title_name)
    TextView mTitleNameTv;

    public static MeFragment newInstance() {
        Bundle args = new Bundle();

        MeFragment fragment = new MeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_me;
    }

    @Override
    protected void initEvent() {
        super.initEvent();
        mContainerRl.setBackgroundColor(Color.TRANSPARENT);
        mTitleBackRl.setVisibility(View.GONE);
        mBottomLine.setVisibility(View.GONE);
        mTitleNameTv.setVisibility(View.GONE);
        mTitleRightTv.setText("设置");
        mTitleRightTv.setTextColor(ContextCompat.getColor(getContext(), R.color.app_text_color_black));
    }

    @OnClick({R.id.mine_info, R.id.mine_circle, R.id.mine_house, R.id.mine_suggest, R.id.mine_vip,
            R.id.mine_advert, R.id.mine_produce, R.id.mine_clear, R.id.mine_app, R.id.title_right})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.mine_info:
                startActivity(PersonInfoActivity.class);
                break;
            case R.id.mine_circle:

                break;
            case R.id.mine_house:

                break;
            case R.id.mine_suggest:
                startActivity(FeedBackActivity.class);
                break;
            case R.id.title_right:
                startActivity(MyActivity.class);
                break;
            default:
                break;
        }
    }

    private void startActivity(Class clazz){
        Intent intent = new Intent(getActivity(), clazz);
        startActivity(intent);
    }
}
