package dhcc.cn.com.fix_phone.bean;

import com.chad.library.adapter.base.entity.AbstractExpandableItem;
import com.chad.library.adapter.base.entity.MultiItemEntity;

import dhcc.cn.com.fix_phone.adapter.CircleFragmentAdapter;

/**
 * 2017/9/17 14
 */
public class CircleFragmentTitle extends AbstractExpandableItem<CircleFragmentItem> implements MultiItemEntity {

    private String content;

    public CircleFragmentTitle(String content) {
        this.content = content;
    }

    @Override
    public int getLevel() {
        return 0;
    }

    @Override
    public int getItemType() {
        return CircleFragmentAdapter.TYPE_LEVEL_0;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
