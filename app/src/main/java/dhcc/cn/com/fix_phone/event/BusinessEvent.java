package dhcc.cn.com.fix_phone.event;

import dhcc.cn.com.fix_phone.bean.BusinessResponse;

/**
 * 2017/10/1 13
 */
public class BusinessEvent {
    public BusinessResponse mResponse;

    public BusinessEvent(BusinessResponse response) {
        mResponse = response;
    }
}
