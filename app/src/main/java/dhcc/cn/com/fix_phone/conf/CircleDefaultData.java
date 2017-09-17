package dhcc.cn.com.fix_phone.conf;

import com.chad.library.adapter.base.entity.MultiItemEntity;

import java.util.ArrayList;
import java.util.List;

import dhcc.cn.com.fix_phone.R;
import dhcc.cn.com.fix_phone.bean.CircleItem;
import dhcc.cn.com.fix_phone.bean.CircleTitle;

/**
 * 2017/9/17 15
 */
public class CircleDefaultData {

    public static List<MultiItemEntity> getCircleDefaultData() {
        List<MultiItemEntity> list = new ArrayList<>();
        CircleTitle phoneTitle = new CircleTitle("手机分类");
        list.add(phoneTitle);
        phoneTitle.addSubItem(new CircleItem("Accessories", "配件耗材", R.drawable.menu_0));
        phoneTitle.addSubItem(new CircleItem("LiquidCrystal", "液晶加工", R.drawable.menu_1));
        phoneTitle.addSubItem(new CircleItem("Module", "模组总成", R.drawable.menu_2));
        phoneTitle.addSubItem(new CircleItem("Maintain", "手机维修", R.drawable.menu_1));
        phoneTitle.addSubItem(new CircleItem("Business", "手机买卖", R.drawable.menu_4));
        phoneTitle.addSubItem(new CircleItem("Tool", "维修工具", R.drawable.menu_5));
        phoneTitle.addSubItem(new CircleItem("Device", "设备", R.drawable.menu_6));
        phoneTitle.addSubItem(new CircleItem("PolarizedLight", "跑单曝光", R.drawable.menu_7));
        CircleTitle computeTitle = new CircleTitle("电脑/笔记本/电视");
        list.add(computeTitle);
        computeTitle.addSubItem(new CircleItem("Comp_Module", "玻璃模组", R.drawable.menu_8));
        computeTitle.addSubItem(new CircleItem("Comp_LiquidCrystal", "液晶加工", R.drawable.menu_9));
        computeTitle.addSubItem(new CircleItem("Comp_TabMaintain", "TAB维修", R.drawable.menu_10));
        computeTitle.addSubItem(new CircleItem("Comp_Accessories", "屏线配件", R.drawable.menu_11));
        computeTitle.addSubItem(new CircleItem("Comp_Chassis", "机壳背光", R.drawable.menu_12));
        computeTitle.addSubItem(new CircleItem("Comp_Whole", "整机", R.drawable.menu_13));
        computeTitle.addSubItem(new CircleItem("Comp_Device", "设备", R.drawable.menu_14));
        computeTitle.addSubItem(new CircleItem("", "跑单曝光", R.drawable.menu_15));
        return list;
    }
}
