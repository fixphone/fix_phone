package dhcc.cn.com.fix_phone.event;

import dhcc.cn.com.fix_phone.bean.AddFriendResponse;

/**
 * Created by Administrator on 2017/10/15 0015.
 */

public class AddFriendEvent extends BaseEvent{

    public AddFriendResponse addFriendResponse;
    public AddFriendEvent(AddFriendResponse addFriendResponse){
        this.addFriendResponse = addFriendResponse;
    }
}
