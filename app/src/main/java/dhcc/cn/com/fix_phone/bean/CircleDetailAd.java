package dhcc.cn.com.fix_phone.bean;

import java.util.List;

/**
 * 2017/9/20 22
 */
public class CircleDetailAd {

    /**
     * FInterID : 0
     * FIsFooterData : false
     * FIsNeedRelogin : false
     * FIsSuccess : true
     * FMsg :
     * FObject : {"list":[{"FEntryID":1,"FIsFooterData":false,"FLinkID":5,"FLinkName":"店铺","FLinkNumber":"Store","FLinkType":1,"FType":0,"FUrl":"http://120.77.202.151:8080/OSS/GetImage?FileName=Adver/2-LiquidCrystal/20170806-2.jpg"},{"FEntryID":2,"FIsFooterData":false,"FLinkID":-1,"FLinkName":"店铺","FLinkNumber":"Store","FLinkType":1,"FType":0,"FUrl":"http://120.77.202.151:8080/OSS/GetImage?FileName=Adver/2-LiquidCrystal/20170806-3.jpg"},{"FEntryID":3,"FIsFooterData":false,"FLinkID":-1,"FLinkName":"店铺","FLinkNumber":"Store","FLinkType":1,"FType":0,"FUrl":"http://120.77.202.151:8080/OSS/GetImage?FileName=Adver/2-LiquidCrystal/20170806-4.jpg"}]}
     */

    public int FInterID;
    public boolean     FIsFooterData;
    public boolean     FIsNeedRelogin;
    public boolean     FIsSuccess;
    public String      FMsg;
    public FObjectBean FObject;

    public static class FObjectBean {
        public List<ListBean> list;

        public static class ListBean {
            /**
             * FEntryID : 1
             * FIsFooterData : false
             * FLinkID : 5
             * FLinkName : 店铺
             * FLinkNumber : Store
             * FLinkType : 1
             * FType : 0
             * FUrl : http://120.77.202.151:8080/OSS/GetImage?FileName=Adver/2-LiquidCrystal/20170806-2.jpg
             */

            public int FEntryID;
            public boolean FIsFooterData;
            public String     FLinkID;
            public String  FLinkName;
            public String  FLinkNumber;
            public int     FLinkType;
            public int     FType;
            public String  FUrl;
        }
    }
}
