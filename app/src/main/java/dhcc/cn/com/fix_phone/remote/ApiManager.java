package dhcc.cn.com.fix_phone.remote;

import android.util.Log;

import org.greenrobot.eventbus.EventBus;

import dhcc.cn.com.fix_phone.Account;
import dhcc.cn.com.fix_phone.bean.AddFriendResponse;
import dhcc.cn.com.fix_phone.bean.BusinessResponse;
import dhcc.cn.com.fix_phone.bean.CirCleADResponse;
import dhcc.cn.com.fix_phone.bean.CircleBusiness;
import dhcc.cn.com.fix_phone.bean.CircleDetailAd;
import dhcc.cn.com.fix_phone.bean.FavoResponse;
import dhcc.cn.com.fix_phone.bean.GetFriendResponse;
import dhcc.cn.com.fix_phone.bean.ImageResponse;
import dhcc.cn.com.fix_phone.bean.LoginInfo;
import dhcc.cn.com.fix_phone.bean.LoginResponse;
import dhcc.cn.com.fix_phone.bean.CreateOrderInfo;
import dhcc.cn.com.fix_phone.bean.OrderInfo;
import dhcc.cn.com.fix_phone.bean.PlayInWeChat;
import dhcc.cn.com.fix_phone.bean.ProductImage;
import dhcc.cn.com.fix_phone.bean.QueryUserResponse;
import dhcc.cn.com.fix_phone.bean.RegisterResponse;
import dhcc.cn.com.fix_phone.bean.RongTokenResponse;
import dhcc.cn.com.fix_phone.bean.TelCheckResponse;
import dhcc.cn.com.fix_phone.bean.TokenResponse;
import dhcc.cn.com.fix_phone.bean.VipOrderInfo;
import dhcc.cn.com.fix_phone.event.AddFriendEvent;
import dhcc.cn.com.fix_phone.event.BusinessEvent;
import dhcc.cn.com.fix_phone.event.CirCleBusinessEvent;
import dhcc.cn.com.fix_phone.event.CircleAdEvent;
import dhcc.cn.com.fix_phone.event.CircleDetailAdEvent;
import dhcc.cn.com.fix_phone.event.CollectEvent;
import dhcc.cn.com.fix_phone.event.DeleteFriendEvent;
import dhcc.cn.com.fix_phone.event.FavoResponseEvent;
import dhcc.cn.com.fix_phone.event.FindPswEvent;
import dhcc.cn.com.fix_phone.event.GetFriendEvent;
import dhcc.cn.com.fix_phone.event.ImageResponeEvent;
import dhcc.cn.com.fix_phone.event.LoginEvent;
import dhcc.cn.com.fix_phone.event.ProductImageEvent;
import dhcc.cn.com.fix_phone.event.QueryUserEvent;
import dhcc.cn.com.fix_phone.event.RefreshTokenEvent;
import dhcc.cn.com.fix_phone.event.RegisterEvent;
import dhcc.cn.com.fix_phone.event.RongTokenEvent;
import dhcc.cn.com.fix_phone.event.SelfInfoEvent;
import dhcc.cn.com.fix_phone.event.TelCheckEvent;
import dhcc.cn.com.fix_phone.event.TokenEvent;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.zhy.http.okhttp.log.LoggerInterceptor.TAG;

/**
 * 2017/9/17 16
 */
public class ApiManager {
    private Api mApi;

    private static LoginInfo getLoginInfo() {
        LoginInfo loginInfo = Account.getLoginInfo();
        if (loginInfo == null) {
            loginInfo = new LoginInfo();
        }
        return loginInfo;
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
                    if (body != null) {
                        if (body.FObject != null && !body.FObject.list.isEmpty()) {
                            EventBus.getDefault().post(new CircleAdEvent(body.FObject.list));
                        }
                    }
                } else {
                    CircleAdEvent event = new CircleAdEvent(null);
                    event.errorMessage = "服务器返回错误";
                    event.isOk = false;
                    EventBus.getDefault().post(event);
                }
            }

            @Override
            public void onFailure(Call<CirCleADResponse> call, Throwable t) {
                CircleAdEvent event = new CircleAdEvent(null);
                event.errorMessage = t.getMessage();
                event.isOk = false;
                EventBus.getDefault().post(event);
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
                } else {
                    CircleDetailAdEvent event = new CircleDetailAdEvent(null);
                    event.errorMessage = "服务器返回错误";
                    event.isOk = false;
                    EventBus.getDefault().post(event);
                }
            }

            @Override
            public void onFailure(Call<CircleDetailAd> call, Throwable t) {
                CircleDetailAdEvent event = new CircleDetailAdEvent(null);
                event.errorMessage = t.getMessage();
                event.isOk = false;
                EventBus.getDefault().post(event);
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
                } else {
                    CirCleBusinessEvent event = new CirCleBusinessEvent(null);
                    event.errorMessage = "服务器返回错误";
                    event.isOk = false;
                    EventBus.getDefault().post(event);
                }
            }

            @Override
            public void onFailure(Call<CircleBusiness> call, Throwable t) {
                CirCleBusinessEvent event = new CirCleBusinessEvent(null);
                event.errorMessage = t.getMessage();
                event.isOk = false;
                EventBus.getDefault().post(event);
            }
        });
    }

    //11.我的帖子
    public void getMyList(int number,
                          int pageIndex,
                          int pageSize,
                          String where) {
        mApi.getMyList(getLoginInfo().accessToken, number, pageIndex, pageSize, "", where).enqueue(new Callback<CircleBusiness>() {
            @Override
            public void onResponse(Call<CircleBusiness> call, Response<CircleBusiness> response) {
                if (response.code() == 200) {
                    CircleBusiness business = response.body();
                    if (business != null && business.FIsSuccess) {
                        EventBus.getDefault().post(new CirCleBusinessEvent(business.FObject));
                    }
                } else {
                    CirCleBusinessEvent event = new CirCleBusinessEvent(null);
                    event.errorMessage = "服务器返回错误";
                    event.isOk = false;
                    EventBus.getDefault().post(event);
                }
            }

            @Override
            public void onFailure(Call<CircleBusiness> call, Throwable t) {
                CirCleBusinessEvent event = new CirCleBusinessEvent(null);
                event.errorMessage = t.getMessage();
                event.isOk = false;
                EventBus.getDefault().post(event);
            }
        });

    }

    //17.获取店铺资料
    public void getUserInfo(String useId) {
        mApi.getUserInfo(getLoginInfo().accessToken, useId).enqueue(new Callback<BusinessResponse>() {
            @Override
            public void onResponse(Call<BusinessResponse> call, Response<BusinessResponse> response) {
                if (response.code() == 200) {
                    BusinessResponse businessResponse = response.body();
                    if (businessResponse != null && businessResponse.FIsSuccess) {
                        EventBus.getDefault().post(new BusinessEvent(response.body()));
                    }
                } else {
                    BusinessEvent event = new BusinessEvent(null);
                    event.errorMessage = "服务器返回错误";
                    event.isOk = false;
                    EventBus.getDefault().post(event);
                }
            }

            @Override
            public void onFailure(Call<BusinessResponse> call, Throwable t) {
                BusinessEvent event = new BusinessEvent(null);
                event.errorMessage = t.getMessage();
                event.isOk = false;
                EventBus.getDefault().post(event);
            }
        });
    }

    //17copy.获取用户信息
    public void getSelfInfo(String useId) {
        mApi.getUserInfo(getLoginInfo().accessToken, useId).enqueue(new Callback<BusinessResponse>() {
            @Override
            public void onResponse(Call<BusinessResponse> call, Response<BusinessResponse> response) {
                if (response.code() == 200) {
                    BusinessResponse businessResponse = response.body();
                    if (businessResponse != null && businessResponse.FIsSuccess) {
                        EventBus.getDefault().post(new SelfInfoEvent(response.body()));
                    }
                } else {
                    SelfInfoEvent event = new SelfInfoEvent(null);
                    event.errorMessage = "服务器返回错误";
                    event.isOk = false;
                    EventBus.getDefault().post(event);
                }
            }

            @Override
            public void onFailure(Call<BusinessResponse> call, Throwable t) {
                SelfInfoEvent event = new SelfInfoEvent(null);
                event.errorMessage = t.getMessage();
                event.isOk = false;
                EventBus.getDefault().post(event);
            }
        });
    }

    //18.修改店铺资料
    public void ChangeUserInfo(String accessToken, String name, String companyName, String companyProfile,
                               String contact, String postCode, String contactMobile, String contactPhone,
                               String address) {
        mApi.ChangeUserInfo(accessToken, name, companyName, companyProfile, contact, postCode, contactMobile,
                contactPhone, address).enqueue(new Callback<BusinessResponse>() {
            @Override
            public void onResponse(Call<BusinessResponse> call, Response<BusinessResponse> response) {
                if (response.code() == 200) {
                    BusinessResponse businessResponse = response.body();
                    if (businessResponse != null && businessResponse.FIsSuccess) {
                        EventBus.getDefault().post(new BusinessEvent(response.body()));
                    }
                } else {
                    BusinessEvent event = new BusinessEvent(null);
                    event.errorMessage = "服务器返回错误";
                    event.isOk = false;
                    EventBus.getDefault().post(event);
                }
            }

            @Override
            public void onFailure(Call<BusinessResponse> call, Throwable t) {
                BusinessEvent event = new BusinessEvent(null);
                event.errorMessage = t.getMessage();
                event.isOk = false;
                EventBus.getDefault().post(event);
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
                } else {
                    TelCheckEvent event = new TelCheckEvent(null);
                    event.errorMessage = "服务器返回错误";
                    event.isOk = false;
                    EventBus.getDefault().post(event);
                }
            }

            @Override
            public void onFailure(Call<TelCheckResponse> call, Throwable t) {
                TelCheckEvent event = new TelCheckEvent(null);
                event.errorMessage = t.getMessage();
                event.isOk = false;
                EventBus.getDefault().post(event);
            }
        });
    }

    //2.手机号码注册
    public void register(String phone, String code, String pwd) {
        mApi.register(phone, code, pwd).enqueue(new Callback<RegisterResponse>() {
            @Override
            public void onResponse(Call<RegisterResponse> call, Response<RegisterResponse> response) {
                if (response.code() == 200) {
                    RegisterResponse registerResponse = response.body();
                    if (registerResponse != null) {
                        EventBus.getDefault().post(new RegisterEvent(registerResponse));
                    }
                } else {
                    RegisterEvent event = new RegisterEvent(null);
                    event.errorMessage = "服务器返回错误";
                    event.isOk = false;
                    EventBus.getDefault().post(event);
                }
            }

            @Override
            public void onFailure(Call<RegisterResponse> call, Throwable t) {
                RegisterEvent event = new RegisterEvent(null);
                event.errorMessage = t.getMessage();
                event.isOk = false;
                EventBus.getDefault().post(event);
            }
        });
    }

    //3.账号密码登录
    public void login(String phone, String psw) {
        mApi.login(phone, psw).enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.code() == 200) {
                    LoginResponse loginResponse = response.body();
                    if (loginResponse != null) {
                        LoginEvent event = new LoginEvent(loginResponse);
                        event.errorMessage = loginResponse.FMsg;
                        event.isOk = true;
                        EventBus.getDefault().post(event);
                    }
                } else {
                    LoginEvent event = new LoginEvent(null);
                    event.errorMessage = "服务器返回错误";
                    event.isOk = false;
                    EventBus.getDefault().post(event);
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                LoginEvent event = new LoginEvent(null);
                event.errorMessage = t.getMessage();
                event.isOk = false;
                EventBus.getDefault().post(event);
            }
        });
    }

    //5.修改密码
    public void ChangePwd(String phone, String oldPwd, String pwd) {
        mApi.ChangePwd(phone, oldPwd, pwd).enqueue(new Callback<TelCheckResponse>() {
            @Override
            public void onResponse(Call<TelCheckResponse> call, Response<TelCheckResponse> response) {
                if (response.code() == 200 && response.body() != null) {
                    TelCheckResponse telCheckResponse = response.body();
                    EventBus.getDefault().post(new TelCheckEvent(telCheckResponse));
                } else {
                    TelCheckEvent event = new TelCheckEvent(null);
                    event.errorMessage = "服务器返回错误";
                    event.isOk = false;
                    EventBus.getDefault().post(event);
                }
            }

            @Override
            public void onFailure(Call<TelCheckResponse> call, Throwable t) {
                TelCheckEvent event = new TelCheckEvent(null);
                event.errorMessage = t.getMessage();
                event.isOk = false;
                EventBus.getDefault().post(event);
            }
        });
    }

    //6.发送修改密码验证码
    public void sendChangePwdPhoneCode(String phone) {
        mApi.SendChangePwdPhoneCode(phone).enqueue(new Callback<TelCheckResponse>() {
            @Override
            public void onResponse(Call<TelCheckResponse> call, Response<TelCheckResponse> response) {
                if (response.code() == 200) {
                    TelCheckResponse telCheckResponse = response.body();
                    if (telCheckResponse != null && telCheckResponse.FIsSuccess) {
                        EventBus.getDefault().post(new TelCheckEvent(telCheckResponse));
                    }
                } else {
                    TelCheckEvent event = new TelCheckEvent(null);
                    event.errorMessage = "服务器返回错误";
                    event.isOk = false;
                    EventBus.getDefault().post(event);
                }
            }

            @Override
            public void onFailure(Call<TelCheckResponse> call, Throwable t) {
                TelCheckEvent event = new TelCheckEvent(null);
                event.errorMessage = t.getMessage();
                event.isOk = false;
                EventBus.getDefault().post(event);
            }
        });
    }

    //7.修改密码-手机验证
    public void ChangePwdByCode(String phone, String code, String psw) {
        mApi.ChangePwdByCode(phone, code, psw).enqueue(new Callback<TelCheckResponse>() {
            @Override
            public void onResponse(Call<TelCheckResponse> call, Response<TelCheckResponse> response) {
                if (response.code() == 200) {
                    TelCheckResponse telCheckResponse = response.body();
                    if (telCheckResponse != null && telCheckResponse.FIsSuccess) {
                        EventBus.getDefault().post(new FindPswEvent(telCheckResponse));
                    }
                } else {
                    FindPswEvent event = new FindPswEvent(null);
                    event.errorMessage = "服务器返回错误";
                    event.isOk = false;
                    EventBus.getDefault().post(event);
                }
            }

            @Override
            public void onFailure(Call<TelCheckResponse> call, Throwable t) {
                FindPswEvent event = new FindPswEvent(null);
                event.errorMessage = t.getMessage();
                event.isOk = false;
                EventBus.getDefault().post(event);
            }
        });
    }

    //8.刷新调用令牌
    public void RefreshToken(String refreshToken) {
        mApi.RefreshToken(refreshToken).enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.code() == 200 && response.body() != null) {
                    LoginResponse loginResponse = response.body();
                    RefreshTokenEvent event = new RefreshTokenEvent(loginResponse);
                    event.errorMessage = loginResponse.FMsg;
                    event.isOk = true;
                    EventBus.getDefault().post(event);
                } else {
                    RefreshTokenEvent event = new RefreshTokenEvent(null);
                    event.errorMessage = "服务器返回错误";
                    event.isOk = false;
                    EventBus.getDefault().post(event);
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                RefreshTokenEvent event = new RefreshTokenEvent(null);
                event.errorMessage = t.getMessage();
                event.isOk = false;
                EventBus.getDefault().post(event);
            }
        });
    }

    public void getRongToken(String accessToken) {
        mApi.getRongToken(accessToken).enqueue(new Callback<RongTokenResponse>() {
            @Override
            public void onResponse(Call<RongTokenResponse> call, Response<RongTokenResponse> response) {
                if (response.code() == 200) {
                    RongTokenResponse tokenResponse = response.body();
                    if (tokenResponse != null) {
                        EventBus.getDefault().post(new RongTokenEvent(tokenResponse.FObject));
                    }
                } else {
                    RongTokenEvent event = new RongTokenEvent(null);
                    event.errorMessage = "服务器返回错误";
                    event.isOk = false;
                    EventBus.getDefault().post(event);
                }
            }

            @Override
            public void onFailure(Call<RongTokenResponse> call, Throwable t) {
                RongTokenEvent event = new RongTokenEvent(null);
                event.errorMessage = t.getMessage();
                event.isOk = false;
                EventBus.getDefault().post(event);
            }
        });
    }

    public void getToken(String appKey, String nonce, String timestamp, String signature, String userId,
                         String name, String portraitUri) {
        mApi.getToken(appKey, nonce, timestamp, signature, userId, name, portraitUri).enqueue(new Callback<TokenResponse>() {
            @Override
            public void onResponse(Call<TokenResponse> call, Response<TokenResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    TokenResponse tokenResponse = response.body();
                    TokenEvent event = new TokenEvent(tokenResponse);
                    EventBus.getDefault().post(event);
                } else {
                    TokenEvent event = new TokenEvent(null);
                    event.errorMessage = "服务器返回错误";
                    event.isOk = false;
                    EventBus.getDefault().post(event);
                }
            }

            @Override
            public void onFailure(Call<TokenResponse> call, Throwable t) {
                TokenEvent event = new TokenEvent(null);
                event.errorMessage = t.getMessage();
                event.isOk = false;
                EventBus.getDefault().post(event);
            }
        });
    }

    public void GetStoreList(String useId) {
        mApi.GetStoreList(useId).enqueue(new Callback<ImageResponse>() {

            @Override
            public void onResponse(Call<ImageResponse> call, Response<ImageResponse> response) {
                if (response.code() == 200) {
                    ImageResponse body = response.body();
                    if (body != null) {
                        EventBus.getDefault().post(new ImageResponeEvent(body));
                    }
                } else {
                    ImageResponeEvent event = new ImageResponeEvent(null);
                    event.errorMessage = "服务器返回错误";
                    event.isOk = false;
                    EventBus.getDefault().post(event);
                }
            }

            @Override
            public void onFailure(Call<ImageResponse> call, Throwable t) {
                ImageResponeEvent event = new ImageResponeEvent(null);
                event.errorMessage = t.getMessage();
                event.isOk = false;
                EventBus.getDefault().post(event);
            }
        });
    }

    //26.删除店铺广告
    public void DeleteStoreAdver(String url) {
        mApi.DeleteStoreAdver(getLoginInfo().accessToken, url).enqueue(new Callback<FavoResponse>() {
            @Override
            public void onResponse(Call<FavoResponse> call, Response<FavoResponse> response) {
                FavoResponse body = response.body();
                if (response.code() == 200 && body != null) {
                    EventBus.getDefault().post(new FavoResponseEvent(body));
                }
            }

            @Override
            public void onFailure(Call<FavoResponse> call, Throwable t) {

            }
        });
    }

    //21.删除产品图片
    public void DeleteProductIcon(String url) {
        mApi.DeleteProductIcon(getLoginInfo().accessToken, url).enqueue(new Callback<FavoResponse>() {
            @Override
            public void onResponse(Call<FavoResponse> call, Response<FavoResponse> response) {
                FavoResponse body = response.body();
                if (response.code() == 200 && body != null) {
                    EventBus.getDefault().post(new FavoResponseEvent(body));
                }
            }

            @Override
            public void onFailure(Call<FavoResponse> call, Throwable t) {

            }
        });
    }

    //19.获取产品图片
    public void getProductList(String userID) {
        mApi.GetProductList(getLoginInfo().accessToken, userID).enqueue(new Callback<ProductImage>() {
            @Override
            public void onResponse(Call<ProductImage> call, Response<ProductImage> response) {
                ProductImage body = response.body();
                if (response.code() == 200 && body != null && body.FIsSuccess) {
                    EventBus.getDefault().post(new ProductImageEvent(body));
                }
            }

            @Override
            public void onFailure(Call<ProductImage> call, Throwable t) {

            }
        });
    }

    //27.收藏列表
    public void getFavoList() {
        mApi.getFavoList(getLoginInfo().accessToken).enqueue(new Callback<CircleBusiness>() {
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

    //28.添加收藏
    public void addFavo(String interId) {
        mApi.AddFavo(getLoginInfo().accessToken, interId).enqueue(new Callback<FavoResponse>() {
            @Override
            public void onResponse(Call<FavoResponse> call, Response<FavoResponse> response) {
                FavoResponse body = response.body();
                if (response.code() == 200 && body != null) {
                    EventBus.getDefault().post(new FavoResponseEvent(body));
                }
            }

            @Override
            public void onFailure(Call<FavoResponse> call, Throwable t) {

            }
        });
    }

    //29.删除收藏
    public void deleteFavo(String interId) {
        mApi.DeleteFavo(getLoginInfo().accessToken, interId).enqueue(new Callback<FavoResponse>() {
            @Override
            public void onResponse(Call<FavoResponse> call, Response<FavoResponse> response) {
                FavoResponse body = response.body();
                if (response.code() == 200 && body != null) {
                    Log.d(TAG, "onResponse: " + body);
                    EventBus.getDefault().post(new FavoResponseEvent(body));
                }
            }

            @Override
            public void onFailure(Call<FavoResponse> call, Throwable t) {

            }
        });
    }

    //29.删除收藏
    public void DeleteItemFavo(String uuid) {
        mApi.DeleteItemFavo(getLoginInfo().accessToken, uuid).enqueue(new Callback<FavoResponse>() {
            @Override
            public void onResponse(Call<FavoResponse> call, Response<FavoResponse> response) {
                FavoResponse body = response.body();
                if (response.code() == 200 && body != null) {
                    Log.d(TAG, "onResponse: " + body);
                    EventBus.getDefault().post(new FavoResponseEvent(body));
                }
            }

            @Override
            public void onFailure(Call<FavoResponse> call, Throwable t) {

            }
        });
    }

    //30.添加图片收藏
    public void AddPictureFavo(String uuid) {
        mApi.AddPictureFavo(getLoginInfo().accessToken, uuid).enqueue(new Callback<FavoResponse>() {
            @Override
            public void onResponse(Call<FavoResponse> call, Response<FavoResponse> response) {
                FavoResponse body = response.body();
                if (response.code() == 200 && body != null) {
                    EventBus.getDefault().post(new CollectEvent(true));
                }
            }

            @Override
            public void onFailure(Call<FavoResponse> call, Throwable t) {

            }
        });
    }

    //31.删除图片收藏
    public void DeletePictureFavo(String uuid) {
        mApi.DeletePictureFavo(getLoginInfo().accessToken, uuid).enqueue(new Callback<FavoResponse>() {
            @Override
            public void onResponse(Call<FavoResponse> call, Response<FavoResponse> response) {
                FavoResponse body = response.body();
                if (response.code() == 200 && body != null) {
                    EventBus.getDefault().post(new CollectEvent(true));
                }
            }

            @Override
            public void onFailure(Call<FavoResponse> call, Throwable t) {

            }
        });
    }

    //32.添加视频收藏
    public void AddVideoFavo(String uuid) {
        mApi.AddVideoFavo(getLoginInfo().accessToken, uuid).enqueue(new Callback<FavoResponse>() {
            @Override
            public void onResponse(Call<FavoResponse> call, Response<FavoResponse> response) {
                FavoResponse body = response.body();
                if (response.code() == 200 && body != null) {
                    EventBus.getDefault().post(new CollectEvent(true));
                }
            }

            @Override
            public void onFailure(Call<FavoResponse> call, Throwable t) {

            }
        });
    }

    //33.删除视频收藏
    public void DeleteVideoFavo(String uuid) {
        mApi.DeleteVideoFavo(getLoginInfo().accessToken, uuid).enqueue(new Callback<FavoResponse>() {
            @Override
            public void onResponse(Call<FavoResponse> call, Response<FavoResponse> response) {
                FavoResponse body = response.body();
                if (response.code() == 200 && body != null) {
                    EventBus.getDefault().post(new CollectEvent(true));
                }
            }

            @Override
            public void onFailure(Call<FavoResponse> call, Throwable t) {

            }
        });
    }

    //36.获取好友列表
    public void GetListFriend(String accessToken, String companyName) {
        mApi.GetListFriend(accessToken, companyName).enqueue(new Callback<GetFriendResponse>() {
            @Override
            public void onResponse(Call<GetFriendResponse> call, Response<GetFriendResponse> response) {
                GetFriendResponse getFriendResponse = response.body();
                if (response.code() == 200 && getFriendResponse != null) {
                    EventBus.getDefault().post(new GetFriendEvent(getFriendResponse));
                } else {
                    GetFriendEvent event = new GetFriendEvent(null);
                    event.errorMessage = "服务器返回错误";
                    event.isOk = false;
                    EventBus.getDefault().post(event);
                }
            }

            @Override
            public void onFailure(Call<GetFriendResponse> call, Throwable t) {
                GetFriendEvent event = new GetFriendEvent(null);
                event.errorMessage = t.getMessage();
                event.isOk = false;
                EventBus.getDefault().post(event);
            }
        });
    }

    //15.删除生意圈
    public void DeleteBusi(String FInterID) {
        mApi.DeleteBusi(getLoginInfo().accessToken, FInterID).enqueue(new Callback<FavoResponse>() {
            @Override
            public void onResponse(Call<FavoResponse> call, Response<FavoResponse> response) {
                FavoResponse body = response.body();
                if (response.code() == 200 && body != null) {
                    EventBus.getDefault().post(new FavoResponseEvent(body));
                }
            }

            @Override
            public void onFailure(Call<FavoResponse> call, Throwable t) {
            }
        });
    }

    //37.查找用户
    public void QueryUserFriend(String accessToken, String queryField) {
        mApi.QueryUserFriend(accessToken, queryField).enqueue(new Callback<QueryUserResponse>() {
            @Override
            public void onResponse(Call<QueryUserResponse> call, Response<QueryUserResponse> response) {
                QueryUserResponse queryUserResponse = response.body();
                if (response.code() == 200 && queryUserResponse != null) {
                    EventBus.getDefault().post(new QueryUserEvent(queryUserResponse));
                } else {
                    QueryUserEvent event = new QueryUserEvent(null);
                    event.errorMessage = "服务器返回错误";
                    event.isOk = false;
                    EventBus.getDefault().post(event);
                }
            }

            @Override
            public void onFailure(Call<QueryUserResponse> call, Throwable t) {
                QueryUserEvent event = new QueryUserEvent(null);
                event.errorMessage = t.getMessage();
                event.isOk = false;
                EventBus.getDefault().post(event);
            }
        });
    }

    //38.添加好友
    public void AddFriend(String accessToken, String friendId) {
        mApi.AddFriend(accessToken, friendId).enqueue(new Callback<AddFriendResponse>() {
            @Override
            public void onResponse(Call<AddFriendResponse> call, Response<AddFriendResponse> response) {
                AddFriendResponse addFriendResponse = response.body();
                if (response.code() == 200 && addFriendResponse != null) {
                    EventBus.getDefault().post(new AddFriendEvent(addFriendResponse));
                } else {
                    AddFriendEvent event = new AddFriendEvent(null);
                    event.errorMessage = "服务器返回错误";
                    event.isOk = false;
                    EventBus.getDefault().post(event);
                }
            }

            @Override
            public void onFailure(Call<AddFriendResponse> call, Throwable t) {
                AddFriendEvent event = new AddFriendEvent(null);
                event.errorMessage = t.getMessage();
                event.isOk = false;
                EventBus.getDefault().post(event);
            }
        });
    }

    //39.删除好友
    public void DeleteFriend(String accessToken, String friendId) {
        mApi.DeleteFriend(accessToken, friendId).enqueue(new Callback<TelCheckResponse>() {
            @Override
            public void onResponse(Call<TelCheckResponse> call, Response<TelCheckResponse> response) {
                TelCheckResponse telCheckResponse = response.body();
                if (response.code() == 200 && telCheckResponse != null) {
                    EventBus.getDefault().post(new DeleteFriendEvent(telCheckResponse));
                } else {
                    DeleteFriendEvent event = new DeleteFriendEvent(null);
                    event.errorMessage = "服务器返回错误";
                    event.isOk = false;
                    EventBus.getDefault().post(event);
                }
            }

            @Override
            public void onFailure(Call<TelCheckResponse> call, Throwable t) {
                DeleteFriendEvent event = new DeleteFriendEvent(null);
                event.errorMessage = t.getMessage();
                event.isOk = false;
                EventBus.getDefault().post(event);
            }
        });
    }

    //41.会员-获取升级会员信息
    public void GetVIPOrderInfo() {
        mApi.GetVIPOrderInfo().enqueue(new Callback<VipOrderInfo>() {
            @Override
            public void onResponse(Call<VipOrderInfo> call, Response<VipOrderInfo> response) {
                if (response.isSuccessful()) {
                    EventBus.getDefault().post(response.body());
                }
            }

            @Override
            public void onFailure(Call<VipOrderInfo> call, Throwable t) {

            }
        });
    }

    //42.会员-生成订单
    public void GenOrder(String type) {
        mApi.GenOrder(getLoginInfo().getAccessToken(), type).enqueue(new Callback<CreateOrderInfo>() {
            @Override
            public void onResponse(Call<CreateOrderInfo> call, Response<CreateOrderInfo> response) {
                if (response.isSuccessful()) {
                    EventBus.getDefault().post(response.body());
                }
            }

            @Override
            public void onFailure(Call<CreateOrderInfo> call, Throwable t) {

            }
        });

    }

    //43.会员-获取订单信息
    public void GetOrderInfo(String billNo) {
        mApi.GetOrderInfo(getLoginInfo().getAccessToken(), billNo).enqueue(new Callback<OrderInfo>() {
            @Override
            public void onResponse(Call<OrderInfo> call, Response<OrderInfo> response) {
                if (response.isSuccessful()) {
                    EventBus.getDefault().post(response.body());
                }
            }

            @Override
            public void onFailure(Call<OrderInfo> call, Throwable t) {

            }
        });
    }

    //44.会员-微信支付
    public void PayInWeChat(String billNo) {
        mApi.PayInWeChat(getLoginInfo().getAccessToken(), billNo).enqueue(new Callback<PlayInWeChat>() {
            @Override
            public void onResponse(Call<PlayInWeChat> call, Response<PlayInWeChat> response) {
                if (response.isSuccessful()) {
                    EventBus.getDefault().post(response.body());
                }
            }

            @Override
            public void onFailure(Call<PlayInWeChat> call, Throwable t) {

            }
        });
    }

    //50.会员-获取当前用户已完成订单信息
    public void GetSuccessOrder() {
        mApi.GetSuccessOrder(getLoginInfo().getAccessToken()).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
    }

}
