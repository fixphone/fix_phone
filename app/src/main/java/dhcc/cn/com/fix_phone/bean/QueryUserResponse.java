package dhcc.cn.com.fix_phone.bean;

import java.util.List;

import dhcc.cn.com.fix_phone.base.BaseResponse;

/**
 * Created by Administrator on 2017/10/15 0015.
 */

public class QueryUserResponse extends BaseResponse {

    public QueryList FObject;

    public class QueryList {

        public List<QueryUser> list;

        public class QueryUser {
            public String  FFriendID;
            public String  FPhone;
            public String  FCompanyName;
            public String  FHeadUrl;
            public boolean FInBlackList;
            public boolean FIsFooterData;
            public String  FName;
            public String  FUserID;
            public int     FUserType;
            public String  FUserTypeName;
            public String  FUserTypeNumber;
        }
    }
}
