package dhcc.cn.com.fix_phone;

import android.app.Application;
import android.content.Context;
import android.os.Handler;

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
    }
}
