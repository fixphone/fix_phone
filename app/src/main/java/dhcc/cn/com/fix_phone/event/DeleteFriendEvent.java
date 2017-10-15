package dhcc.cn.com.fix_phone.event;

import dhcc.cn.com.fix_phone.bean.TelCheckResponse;

/**
 * Created by Administrator on 2017/10/15 0015.
 */

public class DeleteFriendEvent extends BaseEvent{

    public TelCheckResponse telCheckResponse;

    public DeleteFriendEvent(TelCheckResponse telCheckResponse){
        this.telCheckResponse = telCheckResponse;
    }
}
