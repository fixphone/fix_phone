package dhcc.cn.com.fix_phone.remote;


import dhcc.cn.com.fix_phone.MyApplication;

/**
 * 2016/11/2 11
 */
public class ApiService {

    private Api mService;

    private ApiService() {
        mService = RetrofitHelper.getRetrofit(Api.BASE_LOGIN_URL, MyApplication.getContext()).create(Api.class);
    }

    private static class Factory {
        private static final ApiService Instance = new ApiService();
    }

    public static ApiService Instance() {
        return Factory.Instance;
    }

    public Api getService() {
        return mService;
    }
}
