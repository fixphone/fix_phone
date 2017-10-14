package dhcc.cn.com.fix_phone.event;

import dhcc.cn.com.fix_phone.bean.GetFriendResponse;

/**
 * Created by songyang on 2017\10\14 0014.
 */

public class GetFriendEvent extends BaseEvent{

    public GetFriendResponse getFriendResponse;

    public GetFriendEvent(GetFriendResponse getFriendResponse){
        this.getFriendResponse = getFriendResponse;
    }
}
