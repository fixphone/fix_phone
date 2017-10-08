package dhcc.cn.com.fix_phone.bean;

import com.google.gson.annotations.SerializedName;

import dhcc.cn.com.fix_phone.base.BaseResponse;

/**
 * Created by Administrator on 2017/10/8 0008.
 */

public class RongTokenResponse extends BaseResponse{

    @SerializedName("FObject")
    public TokenBody FObject;

    public class TokenBody{

        public String imToken;
    }
}
