package dhcc.cn.com.fix_phone.bean;

import java.util.List;

/**
 * 2017/9/17 16
 */
public class CirCleADResponse {

    /**
     * FIsNeedRelogin : false
     * FIsSuccess : true
     * FMsg :
     * FInterID : 0
     * FBillNo : null
     * rows : null
     * FObject : {"list":[{"FEntryID":1,"FUrl":"http://120.77.202.151:8080/OSS/GetImage?FileName=Adver/Index/20170806-1-2.jpg","FType":0,"FTypeNumber":null,"FTypeValue":null,"FLinkType":1,"FLinkNumber":"Store","FLinkName":"店铺","FLinkID":5,"FIsFooterData":false},{"FEntryID":2,"FUrl":"http://120.77.202.151:8080/OSS/GetImage?FileName=Adver/Index/20170806-2.jpg","FType":0,"FTypeNumber":null,"FTypeValue":null,"FLinkType":1,"FLinkNumber":"Store","FLinkName":"店铺","FLinkID":-1,"FIsFooterData":false},{"FEntryID":3,"FUrl":"http://120.77.202.151:8080/OSS/GetImage?FileName=Adver/Index/20170806-3.jpg","FType":0,"FTypeNumber":null,"FTypeValue":null,"FLinkType":1,"FLinkNumber":"Store","FLinkName":"店铺","FLinkID":-1,"FIsFooterData":false},{"FEntryID":4,"FUrl":"http://120.77.202.151:8080/OSS/GetImage?FileName=Adver/Index/20170806-4.jpg","FType":0,"FTypeNumber":null,"FTypeValue":null,"FLinkType":1,"FLinkNumber":"Store","FLinkName":"店铺","FLinkID":-1,"FIsFooterData":false}]}
     * FIsFooterData : false
     */

    public boolean     FIsNeedRelogin;
    public boolean     FIsSuccess;
    public String      FMsg;
    public int         FInterID;
    public Object      FBillNo;
    public Object      rows;
    public FObjectBean FObject;
    public boolean     FIsFooterData;

    public static class FObjectBean {
        public List<ListBean> list;

        public static class ListBean {
            /**
             * FEntryID : 1
             * FUrl : http://120.77.202.151:8080/OSS/GetImage?FileName=Adver/Index/20170806-1-2.jpg
             * FType : 0
             * FTypeNumber : null
             * FTypeValue : null
             * FLinkType : 1
             * FLinkNumber : Store
             * FLinkName : 店铺
             * FLinkID : 5
             * FIsFooterData : false
             */

            public int     FEntryID;
            public String  FUrl;
            public int     FType;
            public Object  FTypeNumber;
            public Object  FTypeValue;
            public int     FLinkType;
            public String  FLinkNumber;
            public String  FLinkName;
            public String  FLinkID;
            public boolean FIsFooterData;
        }
    }
}
