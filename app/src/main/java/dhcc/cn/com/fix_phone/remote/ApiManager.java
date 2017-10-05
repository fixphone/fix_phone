package dhcc.cn.com.fix_phone.remote;

import org.greenrobot.eventbus.EventBus;

import dhcc.cn.com.fix_phone.MyApplication;
import dhcc.cn.com.fix_phone.bean.BusinessResponse;
import dhcc.cn.com.fix_phone.bean.CirCleADResponse;
import dhcc.cn.com.fix_phone.bean.CircleBusiness;
import dhcc.cn.com.fix_phone.bean.CircleDetailAd;
import dhcc.cn.com.fix_phone.bean.LoginResponse;
import dhcc.cn.com.fix_phone.bean.RegisterResponse;
import dhcc.cn.com.fix_phone.bean.TelCheckResponse;
import dhcc.cn.com.fix_phone.event.BusinessEvent;
import dhcc.cn.com.fix_phone.event.CirCleBusinessEvent;
import dhcc.cn.com.fix_phone.event.CircleAdEvent;
import dhcc.cn.com.fix_phone.event.CircleDetailAdEvent;
import dhcc.cn.com.fix_phone.event.LoginEvent;
import dhcc.cn.com.fix_phone.event.RegisterEvent;
import dhcc.cn.com.fix_phone.event.TelCheckEvent;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 2017/9/17 16
 */
public class ApiManager {
    private Api mApi;

    private static LoginResponse.LoginBody getLoginInfo() {
        return MyApplication.getLoginResponse().FObject;
    }

    private ApiManager() {
        mApi = ApiService.Instance().getService();
    }

    private static class Factory {
        private static final ApiManager instance = new ApiManager();
    }

    public static ApiManager Instance() {
        return Factory.instance;
    }

    public void getCircleAds() {
        mApi.getCircleAD().enqueue(new Callback<CirCleADResponse>() {
            @Override
            public void onResponse(Call<CirCleADResponse> call, Response<CirCleADResponse> response) {
                if (response.code() == 200) {
                    CirCleADResponse body = response.body();
                    if (body != null && body.FIsSuccess) {
                        if (body.FObject != null && !body.FObject.list.isEmpty()) {
                            EventBus.getDefault().post(new CircleAdEvent(body.FObject.list));
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<CirCleADResponse> call, Throwable t) {

            }
        });
    }

    public void getCircleDetailAds(String typeId) {
        mApi.getCircleList(typeId).enqueue(new Callback<CircleDetailAd>() {
            @Override
            public void onResponse(Call<CircleDetailAd> call, Response<CircleDetailAd> response) {
                if (response.code() == 200) {
                    CircleDetailAd body = response.body();
                    if (body != null && body.FIsSuccess) {
                        if (body.FObject != null && !body.FObject.list.isEmpty()) {
                            EventBus.getDefault().post(new CircleDetailAdEvent(body.FObject.list));
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<CircleDetailAd> call, Throwable t) {

            }
        });
    }

    public void getCircleBusinessList(int number, int pageIndex, int pageSize, String type, String where) {
        mApi.getBusinessList(number, pageIndex, pageSize, type, where).enqueue(new Callback<CircleBusiness>() {
            @Override
            public void onResponse(Call<CircleBusiness> call, Response<CircleBusiness> response) {
                if (response.code() == 200) {
                    CircleBusiness business = response.body();
                    if (business != null && business.FIsSuccess) {
                        EventBus.getDefault().post(new CirCleBusinessEvent(business.FObject));
                    }
                }
            }

            @Override
            public void onFailure(Call<CircleBusiness> call, Throwable t) {

            }
        });
    }

    public void getUserInfo(String useId) {
        mApi.getUserInfo(getLoginInfo().accessToken, useId).enqueue(new Callback<BusinessResponse>() {
            @Override
            public void onResponse(Call<BusinessResponse> call, Response<BusinessResponse> response) {
                if (response.code() == 200) {
                    BusinessResponse businessResponse = response.body();
                    if (businessResponse != null && businessResponse.FIsSuccess) {
                        EventBus.getDefault().post(new BusinessEvent(response.body()));
                    }
                }
            }

            @Override
            public void onFailure(Call<BusinessResponse> call, Throwable t) {

            }
        });
    }

    //用户中心
    public void getVerificationCodeResponse(String telCheckRequest) {
        mApi.getVerificationCodeResponse(telCheckRequest).enqueue(new Callback<TelCheckResponse>() {
            @Override
            public void onResponse(Call<TelCheckResponse> call, Response<TelCheckResponse> response) {
                if (response.code() == 200) {
                    TelCheckResponse telCheckResponse = response.body();
                    if (telCheckResponse != null && telCheckResponse.FIsSuccess) {
                        EventBus.getDefault().post(new TelCheckEvent(telCheckResponse));
                    }
                }
            }

            @Override
            public void onFailure(Call<TelCheckResponse> call, Throwable t) {

            }
        });
    }

    public void register(String phone, String code, String pwd) {
        mApi.register(phone, code, pwd).enqueue(new Callback<RegisterResponse>() {
            @Override
            public void onResponse(Call<RegisterResponse> call, Response<RegisterResponse> response) {
                if (response.code() == 200) {
                    RegisterResponse registerResponse = response.body();
                    if (registerResponse != null) {
                        EventBus.getDefault().post(new RegisterEvent(registerResponse));
                    }
                }
            }

            @Override
            public void onFailure(Call<RegisterResponse> call, Throwable t) {

            }
        });
    }

    public void login(String phone, String psw) {
        mApi.login(phone, psw).enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.code() == 200 && response.body().FIsSuccess) {
                    LoginResponse loginResponse = response.body();
                    if (loginResponse != null) {
                        EventBus.getDefault().post(new LoginEvent(loginResponse));
                    }
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {

            }
        });
    }

    public void GetStoreList(String useId) {
        mApi.GetStoreList(useId).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
    }

    //19.获取产品图片
    public void GetIconList(String userID,
                            String type,
                            int pageIndex,
                            int pageSize,
                            int getCount,
                            String where) {
        mApi.GetIconList(getLoginInfo().accessToken, userID, type, pageIndex, pageSize, getCount, where).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
    }

}
