package dhcc.cn.com.fix_phone;

import dhcc.cn.com.fix_phone.bean.LoginInfo;
import dhcc.cn.com.fix_phone.bean.User;
import dhcc.cn.com.fix_phone.utils.AppSharedPreferences;


/**
 * Created by sy on 16/7/18.
 * <p>
 * app账户信息缓存
 */
public final class Account {

    private static final String USER_ID = "userId";
    private static final String LOGIN = "login";

    //获取用户id
    public static String getUserId() {
        return AppSharedPreferences.getInstance(MyApplication.getContext()).get(USER_ID);
    }

    //保存用户id
    public static void setUserId(String userId) {
        AppSharedPreferences.getInstance(MyApplication.getContext()).set(USER_ID, userId);
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
    public static void setLoginInfo(LoginInfo loginInfo) {
        loginInfo.save();
    }

    //保存用户信息
    public static LoginInfo getLoginInfo() {
        return LoginInfo.getLoginInfo(getUserId());
    }

    //保存登录令牌
    public static void setAccessToken(String userId, String accessToken) {
        LoginInfo loginInfo = LoginInfo.getLoginInfo(userId);
        loginInfo.accessToken = accessToken;
        loginInfo.save();
    }

    //获取登录令牌
    public static String getAccessToken(){
        LoginInfo loginInfo = LoginInfo.getLoginInfo(getUserId());
        return loginInfo.getAccessToken();
    }


}
