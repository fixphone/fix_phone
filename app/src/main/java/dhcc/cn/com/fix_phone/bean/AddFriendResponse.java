package dhcc.cn.com.fix_phone.bean;

import dhcc.cn.com.fix_phone.base.BaseResponse;

/**
 * Created by Administrator on 2017/10/15 0015.
 */

public class AddFriendResponse extends BaseResponse{

    public Friend FObject;

    public class Friend{
        public String FFriendID;
        public String FName;
        public String FPhone;
        public String FCompanyName;
        public String FHeadUrl;
        public String FUserType;
        public String FUserTypeName;
        public String FUserTypeNumber;
    }
}
