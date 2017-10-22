package dhcc.cn.com.fix_phone.ui.activity;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.OnClick;
import dhcc.cn.com.fix_phone.R;
import dhcc.cn.com.fix_phone.base.BaseActivity;
import dhcc.cn.com.fix_phone.bean.CreateOrderInfo;
import dhcc.cn.com.fix_phone.bean.OrderInfo;
import dhcc.cn.com.fix_phone.bean.PlayInWeChat;
import dhcc.cn.com.fix_phone.bean.VipOrderInfo;
import dhcc.cn.com.fix_phone.conf.Constants;
import dhcc.cn.com.fix_phone.remote.ApiManager;


/**
 * @author songyang
 * @date 2017\9\23 0023
 */

public class BecomeVipActivity extends BaseActivity implements IWXAPIEventHandler {

    @BindView(R.id.title_name)
    TextView title_name;

    @BindView(R.id.weixplay)
    Button play;

    @BindView(R.id.money)
    TextView money;

    @BindView(R.id.description)
    TextView description;

    private String       mType;
    private String       mBillNo;
    private PlayInWeChat mPlayInWeChat;
    private IWXAPI       mWXApi;

    @Override
    protected void init() {
        EventBus.getDefault().register(this);
        mWXApi = WXAPIFactory.createWXAPI(this, Constants.APP_ID, true);
        mWXApi.registerApp(Constants.APP_ID);
    }

    @Override
    protected void destroy() {
        EventBus.getDefault().unregister(this);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_become_vip;
    }

    @Override
    protected void initView() {
        play.setVisibility(View.GONE);
    }

    @Override
    protected void initEvent() {
        super.initEvent();
        title_name.setText("会员信息");
    }

    @OnClick(R.id.title_back)
    public void onClick() {
        finish();
    }

    @Override
    protected void initData() {
        ApiManager.Instance().GetVIPOrderInfo();
    }

    @OnClick(R.id.weixplay)
    public void play() {
        if (mPlayInWeChat != null) {
            if (!mWXApi.isWXAppInstalled()) {
                Toast.makeText(this, "请安装微信客户端", Toast.LENGTH_SHORT).show();
            } else {
                pay(mPlayInWeChat);
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void createOrderInfo(CreateOrderInfo info) {
        if (info != null && info.FIsSuccess) {
            mBillNo = info.FObject.billNo;
            ApiManager.Instance().GetOrderInfo(mBillNo);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void GenVipInfo(VipOrderInfo info) {
        if (info.FObject != null) {
            description.setText(info.FObject.description);
            money.setText(info.FObject.price + "元");
            ApiManager.Instance().GenOrder(info.FObject.type + "");
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void orderInfo(OrderInfo info) {
        if (info != null && info.FObject != null) {
            OrderInfo.FObjectBean.BillBean billBean = info.FObject.bill;
            ApiManager.Instance().PayInWeChat(mBillNo);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void payInWeChat(PlayInWeChat info) {
        if (info != null && info.FIsSuccess) {
            play.setVisibility(View.VISIBLE);
            mPlayInWeChat = info;
        }
    }

    // 微信发送请求到第三方应用时，会回调到该方法
    @Override
    public void onReq(BaseReq req) {
        switch (req.getType()) {
            case ConstantsAPI.COMMAND_PAY_BY_WX:

                break;
            default:
                break;
        }
    }

    public void pay(PlayInWeChat payInfo) {
        PayReq request = new PayReq();
        request.appId = payInfo.getAppId();
        request.partnerId = payInfo.getPartnerId();
        request.prepayId = payInfo.getPartnerId();
        request.packageValue = "Sign=WXPay";
        request.nonceStr = payInfo.getNonceStr();
        request.timeStamp = payInfo.getTimeStamp();
        request.sign = payInfo.getSign();
        mWXApi.sendReq(request);
    }

    // 第三方应用发送到微信的请求处理后的响应结果，会回调到该方法
    @Override
    public void onResp(BaseResp resp) {
        int result = 0;
        switch (resp.errCode) {
            case BaseResp.ErrCode.ERR_OK:
                result = R.string.errcode_success;
                break;
            case BaseResp.ErrCode.ERR_USER_CANCEL:
                result = R.string.errcode_cancel;
                break;
            case BaseResp.ErrCode.ERR_AUTH_DENIED:
                result = R.string.errcode_deny;
                break;
            default:
                result = R.string.errcode_unknown;
                break;
        }

        Toast.makeText(this, result, Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        //如果分享的时候，该已经开启，那么微信开始这个activity时，会调用onNewIntent，所以这里要处理微信的返回结果
        setIntent(intent);
        mWXApi.handleIntent(intent, this);
    }
}
