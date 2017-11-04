package dhcc.cn.com.fix_phone.conf;


import dhcc.cn.com.fix_phone.utils.LogUtils;

/**
 * @创建者 Administrator
 * @创时间 2015-10-20 上午10:21:37
 * @描述 常量类
 * @版本 $Rev: 8 $
 * @更新者 $Author: admin $
 * @更新时间 $Date: 2015-10-20 15:28:16 +0800 (星期二, 20 十月 2015) $
 * @更新描述 TODO
 */
public class Constants {
    /**
     * 日志级别
     * LogUtils.LEVEL_ALL:显示所有日志
     * LogUtils.LEVEL_OFF:不显示所有日志(关闭日志)
     */
    public static final int DEBUGLEVEL = LogUtils.LEVEL_ALL;

    public static final String APP_ID = "wxaba609e92f61abdf";

    public static class ShowMsgActivity {
        public static final String STitle = "showmsg_title";
        public static final String SMessage = "showmsg_message";
        public static final String BAThumbData = "showmsg_thumb_data";
    }

    public final static int SHARE_IMAGE_WIDTH = 200;
    public final static int SHARE_IMAGE_HEIGHT = 200;

    // 微信好友/微信朋友圈分享
    public static final String WX_TITLE = "wxTitle";
    public static final String WX_IMGPATH = "wxImgPath";
    public static final String WX_URL = "wxUrl";
    public static final String WX_CONTENT = "wxContent";

    //微信包名
    public static final String WECHAT_PACKAGE_NAME = "com.tencent.mm";

    public static String wxTitle = "速通交易";
    public static String wxImgPath = "http://mobile.rrjc.com:80/upload/fund/20160901/201609010934238567.jpg";
    public static String wxUrl = "http://mobile.rrjc.com/wapzhuce?userid=33795298";
    public static String wxContent = "【速通交易】速通交易APP是一款电子行业沟通信息平台，专注服务于液晶显示屏、整机、内置配件耗材及维修设备工厂、生产商、贸易商、华强北数万家档口以及全国电子行业商户需求，解决QQ、微信群人数的限制。";

    //融云
    public static final String MI_SERVICE  = "KEFU150358816985468";

    //本地广播
    public static final String ADD_FRIEND_SUCCESS = "cn.com.dhcc.addFriend.success";
}
