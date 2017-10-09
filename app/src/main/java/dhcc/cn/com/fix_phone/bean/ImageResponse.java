package dhcc.cn.com.fix_phone.bean;

import java.util.List;

import dhcc.cn.com.fix_phone.base.BaseResponse;

/**
 * 2017/10/9 21
 */
public class ImageResponse extends BaseResponse {

    /**
     * FIsNeedRelogin : false
     * FInterID : 0
     * FBillNo : null
     * rows : null
     * FObject : {"list":["http://120.77.202.151:8080/OSS/GetImage?FileName=Adver/Store/3/20170803/779a02d0-85e1-4fff-ac9d-f7f78de89367.jpg","http://120.77.202.151:8080/OSS/GetImage?FileName=Adver/Store/3/20170803/4052105b-c9f4-4dd2-a41f-53ed575ea224.jpg","http://120.77.202.151:8080/OSS/GetImage?FileName=Adver/Store/3/20170913/a2074081-898b-4e5b-bd36-0d56a6a4fb4a.jpg"]}
     * FIsFooterData : false
     */

    public boolean     FIsNeedRelogin;
    public int         FInterID;
    public Object      FBillNo;
    public Object      rows;
    public FObjectBean FObject;
    public boolean     FIsFooterData;

    public static class FObjectBean {
        public List<String> list;
    }
}
