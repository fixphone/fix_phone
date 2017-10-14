package dhcc.cn.com.fix_phone.bean;

import java.util.List;

import dhcc.cn.com.fix_phone.base.BaseResponse;

/**
 * Created by songyang on 2017\10\14 0014.
 */

public class GetFriendResponse extends BaseResponse{

    public FriendList FObject;

    public class FriendList{

        public List<Friend> list;

        public class Friend{
            public String FFriendID;
            public String FPhone;
            public String FCompanyName;
            public String FHeadUrl;
            public String FInBlackList;
        }
    }
}
