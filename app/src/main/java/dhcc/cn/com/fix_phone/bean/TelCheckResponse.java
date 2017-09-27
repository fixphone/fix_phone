package dhcc.cn.com.fix_phone.bean;

import java.util.List;

import dhcc.cn.com.fix_phone.base.BaseResponse;

/**
 * 2017/9/17 12
 */
public class TelCheckResponse extends BaseResponse {

    /**
     * FInterID : 0
     * FIsFooterData : false
     * FIsNeedRelogin : false
     * FIsSuccess : true
     * FMsg : 已发送，请注意查收短信!
     * FObject : 2017/09/17 12:07:40
     * rows : []
     */

    public int FInterID;
    public boolean FIsFooterData;
    public boolean FIsNeedRelogin;
    public List<?> rows;
    public Object FObject;
}
