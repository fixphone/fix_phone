package dhcc.cn.com.fix_phone.event;

import java.util.List;

import dhcc.cn.com.fix_phone.bean.CircleDetailAd;

/**
 * 2017/9/20 22
 */
public class CircleDetailAdEvent extends BaseEvent{
    public List<CircleDetailAd.FObjectBean.ListBean> mListBeans;

    public CircleDetailAdEvent(List<CircleDetailAd.FObjectBean.ListBean> mListBeans) {
        this.mListBeans = mListBeans;
    }
}
