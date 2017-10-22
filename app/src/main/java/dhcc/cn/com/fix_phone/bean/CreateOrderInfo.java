package dhcc.cn.com.fix_phone.bean;

import java.util.List;

import dhcc.cn.com.fix_phone.base.BaseResponse;

/**
 * 2017/10/22 20
 */
public class CreateOrderInfo extends BaseResponse {


    /**
     * FIsNeedRelogin : false
     * FInterID : 0
     * FBillNo : null
     * rows : []
     * FObject : {"billNo":"20171022210058868814"}
     * FIsFooterData : false
     */

    public boolean FIsNeedRelogin;
    public int         FInterID;
    public String      FBillNo;
    public FObjectBean FObject;
    public boolean     FIsFooterData;
    public List<?>     rows;

    public static class FObjectBean {
        /**
         * billNo : 20171022210058868814
         */

        public String billNo;
    }
}
