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

    public static String wxTitle = "送你人人聚财218元投资红包，来和我一起赚钱吧！";
    public static String wxImgPath = "http://mobile.rrjc.com:80/upload/fund/20160901/201609010934238567.jpg";
    public static String wxUrl = "http://mobile.rrjc.com/wapzhuce?userid=33795298";
    public static String wxContent = "【人人聚财】有抵押更可靠，车贷直营规模第1名！预计年化收益15%，218元红包投资就到手，赶快来领！";
}
