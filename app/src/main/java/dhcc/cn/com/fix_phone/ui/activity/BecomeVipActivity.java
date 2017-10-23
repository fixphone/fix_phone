package dhcc.cn.com.fix_phone.ui.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
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

public class BecomeVipActivity extends BaseActivity {

    @BindView(R.id.title_name)
    TextView title_name;

    @BindView(R.id.weixplay)
    Button play;

    @BindView(R.id.money)
    TextView money;

    @BindView(R.id.description)
    TextView description;

    private String mBillNo;
    private PlayInWeChat mPlayInWeChat;
    private IWXAPI mWXApi;

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int errCode = intent.getIntExtra("errCode", 0);
            Log.d(TAG, "payResult: ");
            String result = "";
            if (errCode == 0) {
                result = "支付成功";
                play.setVisibility(View.GONE);
            } else if (errCode == -2) {
                result = "取消支付";
                play.setText("重新支付");
            } else {
                result = "支付失败";
                play.setText("重新支付");
            }
            description.setText("已经"+result);

        }
    };

    @Override
    protected void init() {
        EventBus.getDefault().register(this);
        mWXApi = WXAPIFactory.createWXAPI(this, Constants.APP_ID, true);
        mWXApi.registerApp(Constants.APP_ID);
        LocalBroadcastManager.getInstance(this).registerReceiver(mReceiver,new IntentFilter("dhcc.cn.com.fix_phone"));
    }

    @Override
    protected void destroy() {
        EventBus.getDefault().unregister(this);
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mReceiver);
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

    private static final String TAG = "BecomeVipActivity";

    public void pay(PlayInWeChat payInfo) {
        PayReq request = new PayReq();
        request.appId = Constants.APP_ID;
        request.partnerId = payInfo.getPartnerId();
        request.prepayId = payInfo.getPrepayId();
        request.packageValue = "Sign=WXPay";
        request.nonceStr = payInfo.getNonceStr();
        request.timeStamp = payInfo.getTimeStamp();
        request.sign = payInfo.getSign();
        mWXApi.sendReq(request);
    }

}
