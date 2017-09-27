package dhcc.cn.com.fix_phone;

import dhcc.cn.com.fix_phone.bean.User;
import dhcc.cn.com.fix_phone.utils.AppSharedPreferences;


/**
 * Created by sy on 16/7/18.
 * <p>
 * app账户信息缓存
 */
public final class Account {

    private static final String PHONE = "phone";
    private static final String LOGIN = "login";
    private static final String MESSAGE = "message";
    private static final String LOGIN_TOKEN = "login_token";
    private static final String QN_SERVER_URL = "qn_server_url";

    //获取用户id
    public static String getUserPhone() {
        return AppSharedPreferences.getInstance(MyApplication.getContext()).get(PHONE);
    }

    //保存用户id
    public static void setUserPhone(String userId) {
        AppSharedPreferences.getInstance(MyApplication.getContext()).set(PHONE, userId);
    }

    //获取用户登录token
    public static String getLoginToken() {
        return AppSharedPreferences.getInstance(MyApplication.getContext()).get(LOGIN_TOKEN);
    }

    //保存用户登录token
    public static void setLoginToken(String token) {
        AppSharedPreferences.getInstance(MyApplication.getContext()).set(LOGIN_TOKEN, token);
    }

    //获取登录状态
    public static boolean isLogin() {
        return AppSharedPreferences.getInstance(MyApplication.getContext()).getBoolean(LOGIN);
    }

    //保存登录状态
    public static void setLogin(boolean login) {
        AppSharedPreferences.getInstance(MyApplication.getContext()).setBoolean(LOGIN, login);
    }

    //保存用户信息
    public static void setUser(User user) {
        user.save();
    }

    //保存用户信息
    public static void setUserHeadUrl(String FPhone, String FHeadUrl) {
        User user = User.getUser(FPhone);
        user.FHeadUrl = FHeadUrl;
        user.save();
    }




}
