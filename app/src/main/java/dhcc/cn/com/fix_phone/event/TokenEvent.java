package dhcc.cn.com.fix_phone.event;

import dhcc.cn.com.fix_phone.bean.TokenResponse;

/**
 * Created by songyang on 2017\10\14 0014.
 */

public class TokenEvent extends BaseEvent{

    public TokenResponse tokenResponse;

    public TokenEvent(TokenResponse tokenResponse){
        this.tokenResponse = tokenResponse;
    }
}
