package dhcc.cn.com.fix_phone.event;

import dhcc.cn.com.fix_phone.bean.QueryUserResponse;

/**
 * Created by Administrator on 2017/10/15 0015.
 */

public class QueryUserEvent extends BaseEvent{

    public QueryUserResponse queryUserResponse;

    public QueryUserEvent(QueryUserResponse queryUserResponse){
        this.queryUserResponse = queryUserResponse;
    }
}
