package dhcc.cn.com.fix_phone.bean;

import java.util.List;

import dhcc.cn.com.fix_phone.base.BaseResponse;

/**
 * 2017/10/22 21
 */
public class OrderInfo extends BaseResponse {

    /**
     * FIsNeedRelogin : false
     * FInterID : 0
     * FBillNo : null
     * rows : []
     * FObject : {"bill":{"FUserID":57,"FUserName":"18923465093","FBillNo":"20171022210906768925","FDate":"2017-10-22 21:09:06","FPayDate":null,"FHandleDate":null,"FAmount":0.1,"FAuxAmount":0.1,"FPayableAmount":0,"FAuxDiscount":0,"FPayMethod":null,"FStatus":0,"FStatusName":"待付款","FIsCancellation":false,"FDescription":"开通会员可解除圈子限制发布条数","FTypeName":"服务","FEntryID":0,"FName":null,"FModel":null,"FPrice":0,"FDiscountPrice":0,"FStartDate":null,"FExpireDate":null,"FAddYear":0,"FType":0,"FIsFooterData":false}}
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
         * bill : {"FUserID":57,"FUserName":"18923465093","FBillNo":"20171022210906768925","FDate":"2017-10-22 21:09:06","FPayDate":null,"FHandleDate":null,"FAmount":0.1,"FAuxAmount":0.1,"FPayableAmount":0,"FAuxDiscount":0,"FPayMethod":null,"FStatus":0,"FStatusName":"待付款","FIsCancellation":false,"FDescription":"开通会员可解除圈子限制发布条数","FTypeName":"服务","FEntryID":0,"FName":null,"FModel":null,"FPrice":0,"FDiscountPrice":0,"FStartDate":null,"FExpireDate":null,"FAddYear":0,"FType":0,"FIsFooterData":false}
         */

        public BillBean bill;

        public static class BillBean {
            /**
             * FUserID : 57
             * FUserName : 18923465093
             * FBillNo : 20171022210906768925
             * FDate : 2017-10-22 21:09:06
             * FPayDate : null
             * FHandleDate : null
             * FAmount : 0.1
             * FAuxAmount : 0.1
             * FPayableAmount : 0.0
             * FAuxDiscount : 0.0
             * FPayMethod : null
             * FStatus : 0
             * FStatusName : 待付款
             * FIsCancellation : false
             * FDescription : 开通会员可解除圈子限制发布条数
             * FTypeName : 服务
             * FEntryID : 0
             * FName : null
             * FModel : null
             * FPrice : 0.0
             * FDiscountPrice : 0.0
             * FStartDate : null
             * FExpireDate : null
             * FAddYear : 0
             * FType : 0
             * FIsFooterData : false
             */

            public int FUserID;
            public String  FUserName;
            public String  FBillNo;
            public String  FDate;
            public Object  FPayDate;
            public Object  FHandleDate;
            public double  FAmount;
            public double  FAuxAmount;
            public double  FPayableAmount;
            public double  FAuxDiscount;
            public Object  FPayMethod;
            public int     FStatus;
            public String  FStatusName;
            public boolean FIsCancellation;
            public String  FDescription;
            public String  FTypeName;
            public int     FEntryID;
            public Object  FName;
            public Object  FModel;
            public double  FPrice;
            public double  FDiscountPrice;
            public Object  FStartDate;
            public Object  FExpireDate;
            public int     FAddYear;
            public int     FType;
            public boolean FIsFooterData;
        }
    }
}
