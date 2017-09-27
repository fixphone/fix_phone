package dhcc.cn.com.fix_phone.event;

import dhcc.cn.com.fix_phone.bean.LoginResponse;

/**
 * Created by Administrator on 2017/9/25 0025.
 */

public class LoginEvent {

    public LoginResponse loginResponse;

    public LoginEvent(LoginResponse loginResponse){
        this.loginResponse = loginResponse;
    }
}
