package dhcc.cn.com.fix_phone.event;

import dhcc.cn.com.fix_phone.bean.AddFavoResponse;

/**
 * 2017/10/6 12
 */
public class AddFavoResponseEvent {
    public AddFavoResponse mResponse;

    public AddFavoResponseEvent(AddFavoResponse response) {
        mResponse = response;
    }
}
