package dhcc.cn.com.fix_phone.remote;

import android.util.Log;

import java.util.List;

import retrofit2.Response;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * 2016/10/25 11
 */
public class RxUtil {
    private static final String TAG = "RxUtil";

    /**
     * 判断返回的list是否为空
     */
    public static Func1<List<?>, Boolean> listResult = new Func1<List<?>, Boolean>() {

        @Override
        public Boolean call(List<?> list) {
            return list != null && list.size() > 0;
        }
    };

    /**
     * @param t
     * @param <T> 创建一个Observable
     */
    public static <T> Observable<T> createObservable(final T t) {
        return Observable.create(new Observable.OnSubscribe<T>() {
            @Override
            public void call(Subscriber<? super T> subscriber) {
                try {
                    subscriber.onNext(t);
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }

    /**
     * @param <T>
     * @return 线程的调度
     */
    public static <T> Observable.Transformer<T, T> rxSchedulerHelper() {
        return new Observable.Transformer<T, T>() {
            @Override
            public Observable<T> call(Observable<T> observable) {
                return observable.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());
            }
        };
    }

    /*====================================HTTP=========================================================*/

    /**
     * @param <T>
     * @return http 返回结果的处理
     */
    public static <T> Observable.Transformer<BaseHttpResult<T>, T> httpHandleResult() {
        return new Observable.Transformer<BaseHttpResult<T>, T>() {
            @Override
            public Observable<T> call(final Observable<BaseHttpResult<T>> Observable) {
                return Observable.flatMap(new Func1<BaseHttpResult<T>, Observable<T>>() {
                    @Override
                    public Observable<T> call(BaseHttpResult<T> result) {
                        if (result.getCode() == 0) {
                            return createObservable(result.getData());
                        } else {
                            return rx.Observable.error(new ApiException(result.getMsg()));
                        }
                    }
                });
            }
        };
    }

    /**
     * @param <T>
     * @return Response 结果的处理
     */
    public static <T> Observable.Transformer<Response<BaseHttpResult<T>>, BaseHttpResult<T>> httpHandleResponse() {
        return new Observable.Transformer<Response<BaseHttpResult<T>>, BaseHttpResult<T>>() {
            @Override
            public Observable<BaseHttpResult<T>> call(Observable<Response<BaseHttpResult<T>>> response) {
                return response.flatMap(new Func1<Response<BaseHttpResult<T>>, Observable<BaseHttpResult<T>>>() {
                    @Override
                    public Observable<BaseHttpResult<T>> call(Response<BaseHttpResult<T>> baseHttpResult) {
                        Log.d(TAG, "call: " + baseHttpResult.code());
                        if (baseHttpResult.code() == 200) {
                            return createObservable(baseHttpResult.body());
                        } else {
                            return Observable.error(new ApiException(baseHttpResult.message()));
                        }
                    }
                });
            }
        };
    }

    /**
     * @param <T>
     * @return http 结果的处理
     */
    public static <T> Observable.Transformer<Response<BaseHttpResult<T>>, T> httpHandle() {
        return new Observable.Transformer<Response<BaseHttpResult<T>>, T>() {
            @Override
            public Observable<T> call(Observable<Response<BaseHttpResult<T>>> response) {
                return response.compose(RxUtil.<T>httpHandleResponse()).compose(RxUtil.<T>httpHandleResult());
            }
        };
    }


}
