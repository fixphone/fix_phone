package dhcc.cn.com.fix_phone;

import android.app.Application;
import android.content.Context;
import android.os.Handler;
import android.support.annotation.NonNull;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.DefaultRefreshFooterCreater;
import com.scwang.smartrefresh.layout.api.DefaultRefreshHeaderCreater;
import com.scwang.smartrefresh.layout.api.RefreshFooter;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;

import dhcc.cn.com.fix_phone.remote.ApiService;

/**
 * 2017/9/16 22
 */
public class App extends Application {

    private static Context mContext;
    private static Handler mHandler;
    private static long    mMainThreadId;

    public static Context getContext() {
        return mContext;
    }

    public static Handler getHandler() {
        return mHandler;
    }

    public static long getMainThreadId() {
        return mMainThreadId;
    }


    @Override
    public void onCreate() {//程序的入口方法

        //1.上下文
        mContext = getApplicationContext();

        //2.放置一个主线程的Handler
        mHandler = new Handler();

        //3.得到主线程的Id
        mMainThreadId = android.os.Process.myTid();

        //Tid Thread
        //Pid Process
        //Uid User
        super.onCreate();

        ApiService.Instance().getService();

        setGlobalRefreshStyle();
    }

    private void setGlobalRefreshStyle() {
        SmartRefreshLayout.setDefaultRefreshHeaderCreater(new DefaultRefreshHeaderCreater() {
            @NonNull
            @Override
            public RefreshHeader createRefreshHeader(Context context, RefreshLayout layout) {
                return new ClassicsHeader(mContext);
            }
        });

        SmartRefreshLayout.setDefaultRefreshFooterCreater(new DefaultRefreshFooterCreater() {
            @NonNull
            @Override
            public RefreshFooter createRefreshFooter(Context context, RefreshLayout layout) {
                return new ClassicsFooter(mContext);
            }
        });
    }
}
