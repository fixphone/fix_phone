package dhcc.cn.com.fix_phone.event;

import dhcc.cn.com.fix_phone.bean.CircleBusiness;

/**
 * 2017/9/20 23
 */
public class CirCleBusinessEvent extends BaseEvent{
    public CircleBusiness.FObjectBean mFObjectBean;

    public CirCleBusinessEvent(CircleBusiness.FObjectBean FObjectBean) {
        mFObjectBean = FObjectBean;
    }
}
