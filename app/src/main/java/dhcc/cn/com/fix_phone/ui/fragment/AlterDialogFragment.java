package dhcc.cn.com.fix_phone.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXWebpageObject;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import dhcc.cn.com.fix_phone.R;
import dhcc.cn.com.fix_phone.conf.Constants;
import dhcc.cn.com.fix_phone.utils.AMUtils;
import java.util.HashMap;

/**
 * 2017/10/8 19
 */
public class AlterDialogFragment extends DialogFragment implements View.OnClickListener {

    private TextView mFriend;
    private TextView mCircle;

    public static IWXAPI mWXApi;
    private HashMap<String, String> wechatMap = new HashMap<String, String>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);

        mWXApi = WXAPIFactory.createWXAPI(getActivity(), Constants.APP_ID, true);
        mWXApi.registerApp(Constants.APP_ID);
        initWechatShareData();

        View rootView = inflater.inflate(R.layout.fragment_alter_dialog, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mFriend = view.findViewById(R.id.textView_friend);
        mFriend.setOnClickListener(this);
        mCircle = view.findViewById(R.id.textView_circle);
        mCircle.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.textView_friend:
                shareWechat();
                break;
            case R.id.textView_circle:
                shareCircle();
                break;
        }
    }

    private void initWechatShareData() {
        wechatMap.put(Constants.WX_TITLE, Constants.wxTitle);
        wechatMap.put(Constants.WX_IMGPATH, Constants.wxImgPath);
        wechatMap.put(Constants.WX_URL, Constants.wxUrl);
        wechatMap.put(Constants.WX_CONTENT, Constants.wxContent);
    }

    private void shareWechat() {
        if (!AMUtils.checkInstallation(getActivity(), Constants.WECHAT_PACKAGE_NAME)) {
            Toast.makeText(getActivity(), "请安装微信", Toast.LENGTH_SHORT).show();
        }
        if (wechatMap == null) {
            Toast.makeText(getActivity(), "分享错误", Toast.LENGTH_SHORT).show();
        }

        WXWebpageObject webpage = new WXWebpageObject();
        webpage.webpageUrl = wechatMap.get(Constants.WX_URL);
        final WXMediaMessage msg = new WXMediaMessage(webpage);
        msg.title = wechatMap.get(Constants.WX_TITLE);
        msg.description = wechatMap.get(Constants.WX_CONTENT);
        //runOnUiThread(new Runnable() {
        //    @Override
        //    public void run() {
        //        Glide.with(this).load(wechatMap.get(Constants.WX_IMGPATH))
        //            .asBitmap().into(new SimpleTarget<Bitmap>(Constants.SHARE_IMAGE_WIDTH, Constants.SHARE_IMAGE_HEIGHT) {
        //            @Override
        //            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
        //                msg.setThumbImage(resource);
        //            }
        //        });
        //    }
        //});

        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("webpage");
        req.message = msg;
        req.scene = SendMessageToWX.Req.WXSceneSession;
        mWXApi.sendReq(req);
    }

    private void shareCircle() {
        WXWebpageObject webpage = new WXWebpageObject();
        webpage.webpageUrl = wechatMap.get(Constants.WX_URL);
        final WXMediaMessage msg = new WXMediaMessage(webpage);
        msg.title = wechatMap.get(Constants.WX_TITLE);
        msg.description = wechatMap.get(Constants.WX_CONTENT);

        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("webpage");
        req.message = msg;
        req.scene = SendMessageToWX.Req.WXSceneTimeline;
        mWXApi.sendReq(req);
    }

    private String buildTransaction(final String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
    }
}
