package dhcc.cn.com.fix_phone.bean;

import java.util.List;

import dhcc.cn.com.fix_phone.base.BaseResponse;

/**
 * 2017/10/22 20
 */
public class VipOrderInfo extends BaseResponse {


    /**
     * FIsNeedRelogin : false
     * FInterID : 0
     * FBillNo : null
     * rows : []
     * FObject : {"type":1,"name":"升级会员","description":"开通会员可解除圈子限制发布条数","price":0.1}
     * FIsFooterData : false
     */

    public boolean     FIsNeedRelogin;
    public int         FInterID;
    public String      FBillNo;
    public FObjectBean FObject;
    public boolean     FIsFooterData;
    public List<?>     rows;

    public static class FObjectBean {
        /**
         * type : 1
         * name : 升级会员
         * description : 开通会员可解除圈子限制发布条数
         * price : 0.1
         */

        public int    type;
        public String name;
        public String description;
        public double price;
    }
}
