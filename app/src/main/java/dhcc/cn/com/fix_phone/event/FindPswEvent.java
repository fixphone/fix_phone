package dhcc.cn.com.fix_phone.event;

import dhcc.cn.com.fix_phone.bean.TelCheckResponse;

/**
 * Created by songyang on 2017\10\10 0010.
 */

public class FindPswEvent extends BaseEvent{

    public TelCheckResponse telCheckResponse;

    public FindPswEvent(TelCheckResponse telCheckResponse){
        this.telCheckResponse = telCheckResponse;
    }
}
