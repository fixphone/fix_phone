package dhcc.cn.com.fix_phone.bean;

import dhcc.cn.com.fix_phone.base.BaseResponse;

/**
 * Created by Administrator on 2017/10/12 0012.
 */

public class UploadIconResponse extends BaseResponse{

    public UploadIconBody FObject;

    public class UploadIconBody{
        public String url;
        public String fullUrl;

    }
}
