package dhcc.cn.com.fix_phone.event;

import dhcc.cn.com.fix_phone.bean.TelCheckResponse;

/**
 * Created by Administrator on 2017/9/25 0025.
 */

public class TelCheckEvent {

    public TelCheckResponse telCheckResponse;

    public TelCheckEvent(TelCheckResponse telCheckResponse){
        this.telCheckResponse = telCheckResponse;
    }
}
