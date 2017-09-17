package dhcc.cn.com.fix_phone.base;

import rx.Observable;

/**
 * 2016/10/24 18
 */
public interface BaseModel {
    <T> Observable<T> getData();
}
