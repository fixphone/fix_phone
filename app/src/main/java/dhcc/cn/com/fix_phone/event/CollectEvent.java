package dhcc.cn.com.fix_phone.event;

/**
 * 2017/10/8 13
 */
public class CollectEvent extends BaseEvent{
    public boolean isSuccess;

    public CollectEvent(boolean isSuccess) {
        this.isSuccess = isSuccess;
    }
}
