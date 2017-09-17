package dhcc.cn.com.fix_phone.bean;

import com.chad.library.adapter.base.entity.AbstractExpandableItem;
import com.chad.library.adapter.base.entity.MultiItemEntity;

import dhcc.cn.com.fix_phone.adapter.CircleAdapter;

/**
 * 2017/9/17 14
 */
public class CircleTitle extends AbstractExpandableItem<CircleItem> implements MultiItemEntity {

    private String content;

    public CircleTitle(String content) {
        this.content = content;
    }

    @Override
    public int getLevel() {
        return 0;
    }

    @Override
    public int getItemType() {
        return CircleAdapter.TYPE_LEVEL_0;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
