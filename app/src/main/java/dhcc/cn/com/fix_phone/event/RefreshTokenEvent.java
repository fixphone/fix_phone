package dhcc.cn.com.fix_phone.event;

import dhcc.cn.com.fix_phone.bean.LoginResponse;

/**
 * Created by Administrator on 2017/10/29 0029.
 */

public class RefreshTokenEvent extends BaseEvent{

    public LoginResponse loginResponse;

    public RefreshTokenEvent(LoginResponse loginResponse){
        this.loginResponse = loginResponse;
    }
}
