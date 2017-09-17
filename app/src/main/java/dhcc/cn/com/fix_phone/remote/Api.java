package dhcc.cn.com.fix_phone.remote;



import dhcc.cn.com.fix_phone.bean.TelCheckRequest;
import dhcc.cn.com.fix_phone.bean.TelCheckResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * 2016/11/2 11
 */
public interface Api {
    String BASE_LOGIN_URL = "http://120.77.202.151:8080";

    /*获取验证码的返回消息*/
    @POST("/Account/SendRegisterPhoneCode")
    Call<TelCheckResponse> getVerificationCodeResponse(@Body TelCheckRequest telCheck);

    /*注册*/
    /*@POST("/Account /RegisterByPhone")
    Call<UserResponse> register(@Body UserRegisterRequest userRegisterRequest);*/

}
