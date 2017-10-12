package dhcc.cn.com.fix_phone.remote;


import dhcc.cn.com.fix_phone.bean.BusinessResponse;
import dhcc.cn.com.fix_phone.bean.CirCleADResponse;
import dhcc.cn.com.fix_phone.bean.CircleBusiness;
import dhcc.cn.com.fix_phone.bean.CircleDetailAd;
import dhcc.cn.com.fix_phone.bean.CollectResponse;
import dhcc.cn.com.fix_phone.bean.FavoResponse;
import dhcc.cn.com.fix_phone.bean.LoginResponse;
import dhcc.cn.com.fix_phone.bean.ProductImage;
import dhcc.cn.com.fix_phone.bean.RegisterResponse;
import dhcc.cn.com.fix_phone.bean.RongTokenResponse;
import dhcc.cn.com.fix_phone.bean.TelCheckResponse;
import dhcc.cn.com.fix_phone.bean.UploadResponse;
import dhcc.cn.com.fix_phone.bean.ImageResponse;
import io.reactivex.Observable;
import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

/**
 * 2016/11/2 11
 */
public interface Api {
    String BASE_LOGIN_URL = "http://120.77.202.151:8080";

    //1.发送注册验证码
    @POST("/Account/SendRegisterPhoneCode")
    Call<TelCheckResponse> getVerificationCodeResponse(@Query("phone") String telCheck);

    //2.手机号码注册
    @POST("/Account/RegisterByPhone")
    Call<RegisterResponse> register(@Query("phone") String phone,
                                    @Query("code") String code,
                                    @Query("pwd") String pwd);

    //3.账号密码登录
    @POST("/Account/Login")
    Call<LoginResponse> login(@Query("phone") String phone,
                              @Query("pwd") String pwd);

    //4.手机号码随机密码登录
    @POST("/Account/LoginByCode")
    Call<LoginResponse> LoginByCode(@Query("phone") String phone,
                                    @Query("code") String code);

    //5.修改密码
    @POST("/Account/ChangePwd")
    Call<TelCheckResponse> ChangePwd(@Query("phone") String phone,
                                  @Query("oldPwd") String oldPwd,
                                  @Query("pwd") String pwd);

    //6.发送修改密码验证码  --使用注册的手机号码发送验证码的方式登录。
    @POST("/Account/SendChangePwdPhoneCode")
    Call<TelCheckResponse> SendChangePwdPhoneCode(@Query("phone") String phone);

    //7.修改密码-手机验证
    @POST("/Account/ChangePwdByCode")
    Call<TelCheckResponse> ChangePwdByCode(@Query("phone") String phone,
                                        @Query("code") String code,
                                        @Query("pwd") String pwd);

    //8.刷新调用令牌
    @GET("/Account/RefreshToken")
    Call<LoginResponse> RefreshToken(@Query("refreshToken") String refreshToken);

    //9.获取融云Token
    @GET("/Account/LoginByCode")
    Call<LoginResponse> LoginByCode(@Header("accessToken") String accessToken);


    //10.生意圈获取数据
    @GET("/Busi/GetList")
    Call<CircleBusiness> getBusinessList(@Query("getCount") int number,
                                         @Query("pageIndex") int pageIndex,
                                         @Query("pageSize") int pageSize,
                                         @Query("type") String type,
                                         @Query("where") String where);

    //11.我的帖子
    @GET("/Busi/GetMyList")
    Call<CircleBusiness> getMyList(@Header("accessToken") String accessToken,
                                   @Query("getCount") int number,
                                   @Query("pageIndex") int pageIndex,
                                   @Query("pageSize") int pageSize,
                                   @Query("type") String type,
                                   @Query("where") String where);

    //12.上传生意圈图片 POST /Busi/ UploadPicture
    @Multipart
    @POST("/Busi/UploadPicture")
    @Headers("accessKey:JHD2017")
    Observable<UploadResponse> UploadPictureBusi(@Header("accessToken") String accessToken, @Part MultipartBody.Part file);

    //13.上传生意圈视频
    @Multipart
    @POST("/Busi/UploadVideo")
    Observable<UploadResponse> UploadVideoBusi(@Part MultipartBody.Part file);

    //14.发布生意圈
    @POST("/Busi/Publish")
    Call<String> PublishBusi(@Part MultipartBody.Part file);

    //15.删除生意圈
    @POST("/Busi/Delete")
    Call<String> DeleteBusi(@Header("accessToken") String accessToken,
                            @Query("FInterID") String FInterID);

    //16.上传店铺头像
    @POST("/Account/UploadIcon")
    Call<UploadResponse> UploadIconAccount(@Part MultipartBody.Part file);

    //17.获取店铺资料
    @GET("/Account/GetUserInfo")
    Call<BusinessResponse> getUserInfo(@Header("accessToken") String accessToken,
                                       @Query("userID") String userID);

    //18.修改店铺资料
    @POST("/Account/ChangeUserInfo")
    Call<BusinessResponse> ChangeUserInfo(@Header("accessToken") String accessToken,
                                @Query("name") String name,
                                @Query("companyName") String companyName,
                                @Query("companyProfile") String companyProfile,
                                @Query("contact") String contact,
                                @Query("postCode") String postCode,
                                @Query("contactMobile") String contactMobile,
                                @Query("contactPhone") String contactPhone,
                                @Query("address") String address);

    //19.获取产品图片
    @GET("/Product/GetList")
    Call<ProductImage> GetProductList(@Header("accessToken") String accessToken,
                                      @Query("userID") String userID);

    //20.上传产品图片 --头像的二进制数据
    @POST("/Product/UploadIcon")
    Call<String> UploadIcon();

    //21.删除产品图片
    @POST("/Product/DeleteIcon")
    Call<FavoResponse> DeleteProductIcon(@Header("accessToken") String accessToken,
                                         @Query("url") String url);

    //22获取首页广告
    @GET("/Adver/GetIndexList")
    Call<CirCleADResponse> getCircleAD();

    //23.获取生意圈广告
    @GET("/Adver/GetCircleList")
    Call<CircleDetailAd> getCircleList(@Query("type") String typeId);


    //24.获取店铺广告
    @GET("/Adver/GetStoreList")
    Call<ImageResponse> GetStoreList(@Query("userId") String userId);

    //25.添加店铺广告
    @POST("/Adver/AddStoreAdver")
    Call<String> AddStoreAdver();

    //26.删除店铺广告
    @POST("/Adver/DeleteStoreAdver")
    Call<FavoResponse> DeleteStoreAdver(@Header("accessToken") String accessToken,
                                        @Query("url") String url);

    //27.收藏列表
    @POST("/Favo/GetList")
    Call<CircleBusiness> getFavoList(@Header("accessToken") String accessToken);

    //28.添加收藏
    @POST("/Favo/Add")
    Call<FavoResponse> AddFavo(@Header("accessToken") String accessToken,
                               @Query("interId") String interId);

    //29.删除收藏
    @POST("/Favo/Delete")
    Call<FavoResponse> DeleteFavo(@Header("accessToken") String accessToken,
                                  @Query("interId") String interId);

    //30.添加图片收藏
    @POST("/Favo/AddPicture")
    Call<CollectResponse> AddPictureFavo(@Header("accessToken") String accessToken,
                                         @Query("uuid") String uuid);

    //31.删除图片收藏
    @POST("/Favo/DeletePicture")
    Call<CollectResponse> DeletePictureFavo(@Header("accessToken") String accessToken,
                                            @Query("uuid") String uuid);

    //32.添加视频收藏
    @POST("/Favo/AddVideo")
    Call<CollectResponse> AddVideoFavo(@Header("accessToken") String accessToken,
                                       @Query("uuid") String uuid);

    //33.删除视频收藏
    @POST("/Favo/DeleteVideo")
    Call<CollectResponse> DeleteVideoFavo(@Header("accessToken") String accessToken,
                                          @Query("uuid") String uuid);

    //34.上传投诉建议图片
    @POST("Suggestion/UploadPicture")
    Call<String> UploadPictureSuggestion();

    //35.提交投诉建议
    @POST("/Suggestion/Add")
    Call<String> AddSuggestion(@Header("accessToken") String accessToken, @Header("uuid") String... uuid);

    //36.获取好友列表
    @POST("/Friend/GetList")
    Call<String> GetListFriend(@Header("accessToken") String accessToken,
                               @Query("companyName") String companyName);

    //37.查找用户
    @POST("/Friend/QueryUser")
    Call<String> QueryUserFriend(@Header("accessToken") String accessToken,
                                 @Query("queryField") String queryField);

    //38.添加好友
    @POST("/Friend/Add")
    Call<String> AddFriend(@Header("accessToken") String accessToken,
                           @Query("friendId") String friendId);

    //39.删除好友
    @POST("/Friend/Delete")
    Call<String> DeleteFriend(@Header("accessToken") String accessToken,
                              @Query("friendId") String friendId);

    //40.判断是否好友
    @GET("/Friend/IsFriend")
    Call<String> IsFriend(@Header("accessToken") String accessToken,
                          @Query("friendId") String friendId);

    //41.会员-获取升级会员信息
    @GET("/Order/GetVIPOrderInfo")
    Call<String> GetVIPOrderInfo();

    //42.会员-生成订单
    @POST("/Order/GenOrder")
    Call<String> GenOrder(@Header("accessToken") String accessToken,
                          @Query("type") String type);

    //43.会员-获取订单信息
    @POST("/Order/Get")
    Call<String> GetOrder(@Header("accessToken") String accessToken,
                          @Query("billNo") String billNo);

    //44.会员-微信支付
    @GET("/Order/PayInWeChat")
    Call<String> PayInWeChat(@Header("accessToken") String accessToken,
                             @Query("billNo") String billNo);

    //45.黑名单-添加
    @GET("/BlackList/Add")
    Call<String> AddBlackList(@Header("accessToken") String accessToken,
                              @Query("blackUserId") String blackUserId);

    //46.黑名单-移除
    @POST("/BlackList/Delete")
    Call<String> DeleteBlackList(@Header("accessToken") String accessToken,
                                 @Query("blackUserId") String blackUserId);

    //47.黑名单-获取黑名单列表
    @POST("/BlackList/GetList")
    Call<String> GetBlackList(@Header("accessToken") String accessToken);

    //48用户-判断当前用户是否会员
    @POST("/Account/IsVIP")
    Call<String> IsVIP(@Header("accessToken") String accessToken);

    //49.用户-获取APP简介
    @GET("/Account/GetIntroduction")
    Call<String> GetIntroduction();

    //50.会员-获取当前用户已完成订单信息
    @GET("/Order/GetSuccessOrder")
    Call<String> GetSuccessOrder(@Header("accessToken") String accessToken);

    //51.获取融云token
    @GET("/Account/GetRongToken")
    Call<RongTokenResponse> getRongToken(@Header("accessToken") String accessToken);
}
