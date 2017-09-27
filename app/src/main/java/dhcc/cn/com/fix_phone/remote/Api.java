package dhcc.cn.com.fix_phone.remote;


import dhcc.cn.com.fix_phone.bean.CirCleADResponse;
import dhcc.cn.com.fix_phone.bean.CircleBusiness;
import dhcc.cn.com.fix_phone.bean.CircleDetailAd;
import dhcc.cn.com.fix_phone.bean.RegisterResponse;
import dhcc.cn.com.fix_phone.bean.TelCheckResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * 2016/11/2 11
 */
public interface Api {
    String BASE_LOGIN_URL = "http://120.77.202.151:8080";

    /*获取验证码的返回消息*/
    @POST("/Account/SendRegisterPhoneCode")
    Call<TelCheckResponse> getVerificationCodeResponse(@Query("phone") String telCheck);

    /*注册*/
    @POST("/Account /RegisterByPhone")
    Call<RegisterResponse> register(@Query("phone") String phone,
                                    @Query("pwd") String pwd,
                                    @Query("companyName") String companyName,
                                    @Query("companyProfile") String companyProfile,
                                    @Query("contact") String contact,
                                    @Query("contactMobile") String contactMobile,
                                    @Query("contactPhone") String contactPhone,
                                    @Query("postCode") String postCode,
                                    @Query("address") String address);

    //获取首页广告
    @GET("/Adver/GetIndexList")
    Call<CirCleADResponse> getCircleAD();

    //23.获取生意圈广告
    @GET("/Adver/GetCircleList")
    Call<CircleDetailAd> getCircleList(@Query("type") String typeId);

    //10.生意圈获取数据
    @GET("/Busi/GetList")
    Call<CircleBusiness> getBusinessList(@Query("getCount") int number,
                                         @Query("pageIndex") int pageIndex,
                                         @Query("pageSize") int pageSize,
                                         @Query("type") String type,
                                         @Query("where") String where);

    //17.获取店铺资料
    @GET("/Account/GetUserInfo")
    Call<String> getUserInfo(@Query("userID") String userID);
}
