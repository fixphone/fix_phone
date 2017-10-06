package dhcc.cn.com.fix_phone.event;

import dhcc.cn.com.fix_phone.bean.FavoResponse;

/**
 * 2017/10/6 12
 */
public class FavoResponseEvent {
    public FavoResponse mResponse;

    public FavoResponseEvent(FavoResponse response) {
        mResponse = response;
    }
}
