package dhcc.cn.com.fix_phone.base;

/**
 * Created by zjq on 2016/7/22.
 */
public interface BasePresenter<T extends BaseView> {

    void attachView(T view);

    void detachView();

}
