package dhcc.cn.com.fix_phone.remote;

import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.EventBus;

import dhcc.cn.com.fix_phone.bean.CirCleADResponse;
import dhcc.cn.com.fix_phone.bean.CircleBusiness;
import dhcc.cn.com.fix_phone.bean.CircleDetailAd;
import dhcc.cn.com.fix_phone.bean.RegisterResponse;
import dhcc.cn.com.fix_phone.bean.TelCheckResponse;
import dhcc.cn.com.fix_phone.event.CirCleBusinessEvent;
import dhcc.cn.com.fix_phone.event.CircleAdEvent;
import dhcc.cn.com.fix_phone.event.CircleDetailAdEvent;
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
        mApi.getUserInfo(useId).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Logger.json(response.body());
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

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

    public void register(String phone,
                         String pwd,
                         String companyName,
                         String companyProfile,
                         String contact,
                         String contactMobile,
                         String contactPhone,
                         String postCode,
                         String address) {
        mApi.register(phone, pwd, companyName, companyProfile, contact, contactMobile, contactPhone, postCode, address).enqueue(new Callback<RegisterResponse>() {
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


}
