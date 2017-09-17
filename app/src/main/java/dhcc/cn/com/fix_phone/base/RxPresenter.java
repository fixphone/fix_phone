package dhcc.cn.com.fix_phone.base;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * 2016/10/24 18
 */
public class RxPresenter<T extends BaseView, E extends BaseModel> implements BasePresenter<T> {
    protected T                     mView;
    protected E                     mModel;
    private   CompositeSubscription mCompositeSubscription;
    private static final String TAG = "RxPresenter";

    public void addSubscribe(Subscription subscription) {
        if (mCompositeSubscription == null) {
            mCompositeSubscription = new CompositeSubscription();
            mCompositeSubscription.add(subscription);
        }
    }

    public void unSubscriber() {
        if (mCompositeSubscription != null) {
            mCompositeSubscription.unsubscribe();
        }
    }

    @Override
    public void attachView(T view) {
        mView = view;
    }

    @Override
    public void detachView() {
        unSubscriber();
        mView = null;
    }
}
