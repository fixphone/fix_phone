package dhcc.cn.com.fix_phone.event;

/**
 * 2017/10/8 12
 */
public class PublishSuccessEvent extends BaseEvent{
    public boolean isSuccess;

    public PublishSuccessEvent(boolean b) {
        isSuccess = b;
    }
}
