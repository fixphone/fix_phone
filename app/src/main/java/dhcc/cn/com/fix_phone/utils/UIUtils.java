package dhcc.cn.com.fix_phone.utils;

import android.content.Context;
import android.content.res.Resources;
import android.os.Handler;

import dhcc.cn.com.fix_phone.MyApplication;

public class UIUtils {
    /**
     * 得到上下文
     */
    public static Context getContext() {
        return MyApplication.getContext();
    }

    /**
     * 得到Resource对象
     */
    public static Resources getResources() {
        return getContext().getResources();
    }

    /**
     * 得到string.xml中的字符
     */
    public static String getString(int resId) {
        return getResources().getString(resId);
    }

    /**
     * 得到string.xml中的字符数组
     */
    public static String[] getStringArr(int resId) {
        return getResources().getStringArray(resId);
    }

    /**
     * 得到color.xml中的颜色值
     */
    public static int getColor(int resId) {
        return getResources().getColor(resId);
    }

    /**
     * 得到应用程序的包名
     */
    public static String getPackageName() {
        return getContext().getPackageName();
    }

    /**
     * 得到主线程的Id
     */
    public static long getMainThreadId() {
        return MyApplication.getMainThreadId();
    }

    /**
     * 得到主线程的hanlder
     */
    public static Handler getMainThreadHandler() {
        return MyApplication.getHandler();
    }

    /**
     * 安全的执行一个task
     *
     * @return
     */
    public static void postTaskSafely(Runnable task) {
        // 当前线程==子线程,通过消息机制,把任务交给主线程的Handler去执行
        // 当前线程==主线程,直接执行任务
        int curThreadId = android.os.Process.myTid();
        long mainThreadId = getMainThreadId();
        if (curThreadId == mainThreadId) {
            task.run();
        } else {
            getMainThreadHandler().post(task);
        }
    }

    public static int dip2px(float dp) {
        float density = getContext().getResources().getDisplayMetrics().density;
        return (int) (density * dp + 0.5);
    }
}
