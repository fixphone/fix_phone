package dhcc.cn.com.fix_phone.bean;

import java.io.Serializable;
import java.util.List;

import dhcc.cn.com.fix_phone.base.BaseResponse;

/**
 * 2017/10/1 13
 */
public class BusinessResponse extends BaseResponse {

    /**
     * FIsNeedRelogin : false
     * FInterID : 0
     * FBillNo : null
     * rows : []
     * FObject : {"userID":57,"name":"18923465093","phone":"18923465093","userType":0,"userTypeName":"普通用户","userTypeNumber":"Ordinary","weChatID":null,"companyName":"18923465093","companyProfile":null,"contact":null,"postCode":null,"contactMobile":null,"contactPhone":null,"address":null,"comments":null,"headUrl":"http://120.77.202.151:8080/OSS/GetImage?FileName=/UserIcon/Default/default.png","productList":[]}
     * FIsFooterData : false
     */

    public boolean     FIsNeedRelogin;
    public int         FInterID;
    public Object      FBillNo;
    public FObjectBean FObject;
    public boolean     FIsFooterData;

    public class FObjectBean implements Serializable {
        /**
         * userID : 57
         * name : 18923465093
         * phone : 18923465093
         * userType : 0
         * userTypeName : 普通用户
         * userTypeNumber : Ordinary
         * weChatID : null
         * companyName : 18923465093
         * companyProfile : null
         * contact : null
         * postCode : null
         * contactMobile : null
         * contactPhone : null
         * address : null
         * comments : null
         * headUrl : http://120.77.202.151:8080/OSS/GetImage?FileName=/UserIcon/Default/default.png
         * productList : []
         */

        public int          userID;
        public String       name;
        public String       phone;
        public int          userType;
        public String       userTypeName;
        public String       userTypeNumber;
        public String       weChatID;
        public String       companyName;
        public String       companyProfile;
        public String       contact;
        public String       postCode;
        public String       contactMobile;
        public String       contactPhone;
        public String       address;
        public String       comments;
        public String       headUrl;
        public List<String> productList;
    }
}
