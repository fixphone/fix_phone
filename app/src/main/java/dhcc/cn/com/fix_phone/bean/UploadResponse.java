package dhcc.cn.com.fix_phone.bean;

import com.google.gson.annotations.SerializedName;

import dhcc.cn.com.fix_phone.base.BaseResponse;

/**
 * 2017/10/7 14
 */
public class UploadResponse extends BaseResponse {

    /**
     * FIsSuccess : 操作是否成功
     * FIsNeedRelogin : 是否需要重新登录
     * FObject : {"uuid":"图片的uuid（Guid类型，全局唯一ID，该ID用于发布生意圈时回传）"}
     */

    @SerializedName("FIsSuccess")
    public String      FIsSuccessX;
    public String      FIsNeedRelogin;
    public FObjectBean FObject;

    public static class FObjectBean {
        /**
         * uuid : 图片的uuid（Guid类型，全局唯一ID，该ID用于发布生意圈时回传）
         */

        public String uuid;
    }
}
