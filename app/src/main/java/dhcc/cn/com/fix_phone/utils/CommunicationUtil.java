package dhcc.cn.com.fix_phone.utils;

import android.content.Context;
import android.content.Intent;

import dhcc.cn.com.fix_phone.Account;
import dhcc.cn.com.fix_phone.ui.activity.LoginActivity;
import io.rong.imkit.RongIM;

/**
 * 2017/10/29 11
 */
public class CommunicationUtil {

    public static void communication(Context context, String creatorID, String companyName) {
        if (!Account.isLogin()) {
            context.startActivity(new Intent(context, LoginActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
        } else {
            try {
                RongIM.getInstance().startPrivateChat(context, creatorID, companyName);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
