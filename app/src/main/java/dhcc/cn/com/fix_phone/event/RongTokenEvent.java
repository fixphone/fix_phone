package dhcc.cn.com.fix_phone.event;

import dhcc.cn.com.fix_phone.bean.RongTokenResponse;

/**
 * Created by Administrator on 2017/10/8 0008.
 */

public class RongTokenEvent extends BaseEvent{

    public RongTokenResponse.TokenBody tokenBody;

    public RongTokenEvent(RongTokenResponse.TokenBody tokenBody){
        this.tokenBody = tokenBody;
    }
}
