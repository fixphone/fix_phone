package dhcc.cn.com.fix_phone.conf;

import java.util.ArrayList;
import java.util.List;

import dhcc.cn.com.fix_phone.R;
import dhcc.cn.com.fix_phone.bean.CircleItem;

/**
 * 2017/9/17 15
 */
public class CircleDefaultData {

    public static List<CircleItem> getCircleDefaultData(){
        List<CircleItem> list = new ArrayList<>();

        list.add(new CircleItem(CircleItem.TEXT,3,"手机分类 v",0));
        list.add(new CircleItem(CircleItem.IMG_TEXT,1,"配件耗材", R.drawable.menu_0));
        list.add(new CircleItem(CircleItem.IMG_TEXT,1,"液晶加工",R.drawable.menu_1));
        list.add(new CircleItem(CircleItem.IMG_TEXT,1,"模组总成",R.drawable.menu_2));
        list.add(new CircleItem(CircleItem.IMG_TEXT,1,"手机维修",R.drawable.menu_1));
        list.add(new CircleItem(CircleItem.IMG_TEXT,1,"手机买卖",R.drawable.menu_4));
        list.add(new CircleItem(CircleItem.IMG_TEXT,1,"维修工具",R.drawable.menu_5));
        list.add(new CircleItem(CircleItem.IMG_TEXT,1,"设备",R.drawable.menu_6));
        list.add(new CircleItem(CircleItem.IMG_TEXT,1,"跑单曝光",R.drawable.menu_7));
        list.add(new CircleItem(CircleItem.TEXT,3,"电脑/笔记本/电视 v",0));
        list.add(new CircleItem(CircleItem.IMG_TEXT,1,"玻璃模组",R.drawable.menu_8));
        list.add(new CircleItem(CircleItem.IMG_TEXT,1,"液晶加工",R.drawable.menu_9));
        list.add(new CircleItem(CircleItem.IMG_TEXT,1,"TAB维修",R.drawable.menu_10));
        list.add(new CircleItem(CircleItem.IMG_TEXT,1,"屏线配件",R.drawable.menu_11));
        list.add(new CircleItem(CircleItem.IMG_TEXT,1,"机壳背光",R.drawable.menu_12));
        list.add(new CircleItem(CircleItem.IMG_TEXT,1,"整机",R.drawable.menu_13));
        list.add(new CircleItem(CircleItem.IMG_TEXT,1,"设备",R.drawable.menu_14));
        list.add(new CircleItem(CircleItem.IMG_TEXT,1,"跑单曝光",R.drawable.menu_15));


        return list;
    }
}
