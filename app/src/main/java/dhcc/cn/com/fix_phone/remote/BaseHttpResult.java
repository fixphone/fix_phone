package dhcc.cn.com.fix_phone.remote;

import com.google.gson.annotations.SerializedName;

/**
 * 2016/10/25 11
 */
public class BaseHttpResult<T> {
    @SerializedName(value = "code", alternate = {"errNum", "errno", "retcode"})
    private int    code;
    @SerializedName(value = "msg", alternate = {"message", "errMsg", "err"})
    private String msg;
    @SerializedName(value = "data", alternate = {"retData", "itemList", "list", "audioinfos"})
    private T      data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
