package dhcc.cn.com.fix_phone.bean;

import java.util.List;

import dhcc.cn.com.fix_phone.base.BaseResponse;

/**
 * 2017/10/7 14
 */
public class UploadResponse extends BaseResponse {

    /**
     * FIsNeedRelogin : false
     * FInterID : 0
     * FBillNo : null
     * rows : []
     * FObject : {"uuid":"6F42A5DC-5C74-44BC-8C7C-E49BDC1B69A8"}
     * FIsFooterData : false
     */

    public boolean FIsNeedRelogin;
    public int         FInterID;
    public Object      FBillNo;
    public FObjectBean FObject;
    public boolean     FIsFooterData;
    public List<?>     rows;

    public static class FObjectBean {
        /**
         * uuid : 6F42A5DC-5C74-44BC-8C7C-E49BDC1B69A8
         */

        public String uuid;
    }

    @Override
    public String toString() {
        return "UploadResponse{" +
                "FIsNeedRelogin=" + FIsNeedRelogin +
                ", FInterID=" + FInterID +
                ", FBillNo=" + FBillNo +
                ", FObject=" + FObject +
                ", FIsFooterData=" + FIsFooterData +
                ", rows=" + rows +
                '}';
    }
}
