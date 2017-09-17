package dhcc.cn.com.fix_phone.remote;

import org.greenrobot.eventbus.EventBus;

import dhcc.cn.com.fix_phone.bean.CirCleADResponse;
import dhcc.cn.com.fix_phone.event.CircleAdEvent;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 2017/9/17 16
 */
public class ApiManager {
    private Api mApi;

    private ApiManager() {
        mApi = ApiService.Instance().getService();
    }

    private static class Factory {
        private static final ApiManager instance = new ApiManager();
    }

    public static ApiManager Instance() {
        return Factory.instance;
    }

    public void getCircleAds() {
        mApi.getCircleAD().enqueue(new Callback<CirCleADResponse>() {
            @Override
            public void onResponse(Call<CirCleADResponse> call, Response<CirCleADResponse> response) {
                if (response.code() == 200) {
                    CirCleADResponse body = response.body();
                    if (body != null && body.FIsSuccess) {
                        if (body.FObject != null && !body.FObject.list.isEmpty()) {
                            EventBus.getDefault().post(new CircleAdEvent(body.FObject.list));
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<CirCleADResponse> call, Throwable t) {

            }
        });
    }
}
