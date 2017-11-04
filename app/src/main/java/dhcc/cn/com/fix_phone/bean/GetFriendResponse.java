package dhcc.cn.com.fix_phone.bean;

import java.util.List;

import dhcc.cn.com.fix_phone.base.BaseResponse;

/**
 * Created by songyang on 2017\10\14 0014.
 *
 */

public class GetFriendResponse extends BaseResponse {

    public FriendList FObject;

    public class FriendList {

        public List<Friend> list;

        public class Friend {

            /**
             * FFriendID : 70
             * FInBlackList : false
             * FIsFooterData : false
             * FName : 15999660312
             * FUserID : 69
             * FUserType : 0
             * FUserTypeName : 普通用户
             * FUserTypeNumber : Ordinary
             */

            public int     FFriendID;
            public String  FPhone;
            public String  FCompanyName;
            public String  FHeadUrl;
            public boolean FInBlackList;
            public boolean FIsFooterData;
            public String  FName;
            public int     FUserID;
            public int     FUserType;
            public String  FUserTypeName;
            public String  FUserTypeNumber;

            @Override
            public String toString() {
                return "Friend{" +
                        "FFriendID='" + FFriendID + '\'' +
                        ", FPhone='" + FPhone + '\'' +
                        ", FCompanyName='" + FCompanyName + '\'' +
                        ", FHeadUrl='" + FHeadUrl + '\'' +
                        ", FInBlackList='" + FInBlackList + '\'' +
                        ", FIsFooterData=" + FIsFooterData +
                        ", FName='" + FName + '\'' +
                        ", FUserID=" + FUserID +
                        ", FUserType=" + FUserType +
                        ", FUserTypeName='" + FUserTypeName + '\'' +
                        ", FUserTypeNumber='" + FUserTypeNumber + '\'' +
                        '}';
            }
        }
    }
}
