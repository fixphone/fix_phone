package dhcc.cn.com.fix_phone.base;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/9/27 0027.
 */

public class BaseResponse implements Serializable{

    @SerializedName("FIsSuccess")
    public boolean FIsSuccess;

    @SerializedName("FMsg")
    public String  FMsg;

}
