package dhcc.cn.com.fix_phone.bean;

import com.raizlabs.android.dbflow.annotation.Database;

/**
 * Created by Administrator on 2017/6/7.
 */

@Database(name = AppDatabase.NAME, version = AppDatabase.VERSION)
public class AppDatabase {
    //数据库名称
    public static final String NAME = "fixPhoneDatabase";
    //数据库版本号

    public static final int VERSION = 1;
}
