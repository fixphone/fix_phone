package dhcc.cn.com.fix_phone.wxapi;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.TextView;

import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import dhcc.cn.com.fix_phone.R;
import dhcc.cn.com.fix_phone.conf.Constants;


public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {

    private static final String TAG = "MicroMsg.SDKSample.WXPayEntryActivity";

    private IWXAPI api;
    private TextView mTextView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pay_result);
        mTextView = findViewById(R.id.show_message);
        api = WXAPIFactory.createWXAPI(this, Constants.APP_ID);
        api.handleIntent(getIntent(), this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
    }

    // 微信发送请求到第三方应用时，会回调到该方法
    @Override
    public void onReq(BaseReq req) {

    }

    // 第三方应用发送到微信的请求处理后的响应结果，会回调到该方法
    @Override
    public void onResp(BaseResp resp) {
        Log.d(TAG, "onResp: " + resp.toString());
        String result = "";
        //有时候支付结果还需要发送给服务器确认支付状态
        if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
            if (resp.errCode == 0) {
                result = "支付成功";
            } else if (resp.errCode == -2) {
                result = "取消支付";
            } else {
                result = "支付失败";
            }
            mTextView.setText(result);
            LocalBroadcastManager.getInstance(this).sendBroadcast(new Intent("dhcc.cn.com.fix_phone").putExtra("errCode",resp.errCode ));
            finish();
        }
    }

}