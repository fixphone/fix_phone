package dhcc.cn.com.fix_phone.event;

import java.util.List;

import dhcc.cn.com.fix_phone.bean.CirCleADResponse;

/**
 * 2017/9/17 16
 */
public class CircleAdEvent {
    public List<CirCleADResponse.FObjectBean.ListBean> mListBeans;

    public CircleAdEvent(List<CirCleADResponse.FObjectBean.ListBean> mListBeans) {
        this.mListBeans = mListBeans;
    }
}
