package dhcc.cn.com.fix_phone.bean;

import java.util.List;

import dhcc.cn.com.fix_phone.base.BaseResponse;

/**
 * 2017/10/22 20
 */
public class PublishResponse extends BaseResponse {

    /**
     * FInterID : 0
     * FIsFooterData : false
     * FIsNeedRelogin : false
     * FObject : {"needMembership":true}
     * rows : []
     */

    public int FInterID;
    public boolean     FIsFooterData;
    public boolean     FIsNeedRelogin;
    public FObjectBean FObject;
    public List<?>     rows;

    public static class FObjectBean {
        /**
         * needMembership : true
         */

        public boolean needMembership;
    }
}
