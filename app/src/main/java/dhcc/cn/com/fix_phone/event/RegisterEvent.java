package dhcc.cn.com.fix_phone.event;

import dhcc.cn.com.fix_phone.bean.RegisterResponse;

/**
 * Created by Administrator on 2017/9/25 0025.
 */

public class RegisterEvent {

    public RegisterResponse registerResponse;

    public RegisterEvent(RegisterResponse registerResponse){
        this.registerResponse = registerResponse;
    }
}
