package dhcc.cn.com.fix_phone.bean;

import com.google.gson.annotations.SerializedName;

import dhcc.cn.com.fix_phone.base.BaseResponse;

/**
 * Created by Administrator on 2017/9/27 0027.
 */

public class LoginResponse extends BaseResponse {

    @SerializedName("FObject")
    public LoginInfo FObject;

}
