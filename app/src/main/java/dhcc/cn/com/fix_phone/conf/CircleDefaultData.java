package dhcc.cn.com.fix_phone.conf;

import android.support.v4.util.ArrayMap;

import com.chad.library.adapter.base.entity.MultiItemEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import dhcc.cn.com.fix_phone.R;
import dhcc.cn.com.fix_phone.bean.CircleFragmentItem;
import dhcc.cn.com.fix_phone.bean.CircleFragmentTitle;

/**
 * 2017/9/17 15
 */
public class CircleDefaultData {

    public static Map<Integer,String> getCirCleDefailtMap(){
        Map<Integer,String> map = new ArrayMap<>();
        map.put(R.id.Accessories,"Accessories");
        map.put(R.id.LiquidCrystal,"LiquidCrystal");
        map.put(R.id.Module,"Module");
        map.put(R.id.Maintain,"Maintain");
        map.put(R.id.Business,"Business");
        map.put(R.id.Tool,"Tool");
        map.put(R.id.Device,"Device");
        map.put(R.id.Comp_Module,"Comp_Module");
        map.put(R.id.Comp_LiquidCrystal,"Comp_LiquidCrystal");
        map.put(R.id.Comp_TabMaintain,"Comp_TabMaintain");
        map.put(R.id.Comp_Accessories,"Comp_Accessories");
        map.put(R.id.Comp_Chassis,"Comp_Chassis");
        map.put(R.id.Comp_Whole,"Comp_Whole");
        map.put(R.id.Comp_Device,"Comp_Device");
        map.put(R.id.Comp_PolarizedLight,"Comp_PolarizedLight");
        return map;
    }

    public static List<MultiItemEntity> getCircleDefaultData() {
        List<MultiItemEntity> list = new ArrayList<>();

        CircleFragmentTitle phoneTitle = new CircleFragmentTitle("手机分类");
        list.add(phoneTitle);
        phoneTitle.addSubItem(new CircleFragmentItem("Accessories", "配件耗材", R.drawable.menu_0));
        phoneTitle.addSubItem(new CircleFragmentItem("LiquidCrystal", "液晶加工", R.drawable.menu_1));
        phoneTitle.addSubItem(new CircleFragmentItem("Module", "模组总成", R.drawable.menu_2));
        phoneTitle.addSubItem(new CircleFragmentItem("Maintain", "手机维修", R.drawable.menu_3));
        phoneTitle.addSubItem(new CircleFragmentItem("Business", "手机买卖", R.drawable.menu_4));
        phoneTitle.addSubItem(new CircleFragmentItem("Tool", "维修工具", R.drawable.menu_5));
        phoneTitle.addSubItem(new CircleFragmentItem("Device", "设备", R.drawable.menu_6));
        phoneTitle.addSubItem(new CircleFragmentItem("PolarizedLight", "跑单曝光", R.drawable.menu_7));

        CircleFragmentTitle computeTitle = new CircleFragmentTitle("电脑/笔记本/电视");
        list.add(computeTitle);
        computeTitle.addSubItem(new CircleFragmentItem("Comp_Module", "玻璃模组", R.drawable.menu_8));
        computeTitle.addSubItem(new CircleFragmentItem("Comp_LiquidCrystal", "液晶加工", R.drawable.menu_9));
        computeTitle.addSubItem(new CircleFragmentItem("Comp_TabMaintain", "TAB维修", R.drawable.menu_10));
        computeTitle.addSubItem(new CircleFragmentItem("Comp_Accessories", "屏线配件", R.drawable.menu_11));
        computeTitle.addSubItem(new CircleFragmentItem("Comp_Chassis", "机壳背光", R.drawable.menu_12));
        computeTitle.addSubItem(new CircleFragmentItem("Comp_Whole", "整机", R.drawable.menu_13));
        computeTitle.addSubItem(new CircleFragmentItem("Comp_Device", "设备", R.drawable.menu_14));
        computeTitle.addSubItem(new CircleFragmentItem("Comp_PolarizedLight", "跑单曝光", R.drawable.menu_15));

        return list;
    }
}
