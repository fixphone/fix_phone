package dhcc.cn.com.fix_phone.bean;

import java.util.List;

import dhcc.cn.com.fix_phone.base.BaseResponse;

/**
 * 2017/10/6 12
 */
public class AddFavoResponse extends BaseResponse {

    /**
     * FIsNeedRelogin : false
     * FInterID : 0
     * FBillNo : null
     * rows : []
     * FObject : null
     * FIsFooterData : false
     */

    public boolean FIsNeedRelogin;
    public int     FInterID;
    public Object  FBillNo;
    public Object  FObject;
    public boolean FIsFooterData;
    public List<?> rows;
}
