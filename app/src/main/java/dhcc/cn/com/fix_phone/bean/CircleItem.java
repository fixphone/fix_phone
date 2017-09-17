package dhcc.cn.com.fix_phone.bean;

import com.chad.library.adapter.base.entity.MultiItemEntity;

import dhcc.cn.com.fix_phone.adapter.CircleAdapter;

/**
 * 2017/9/17 14
 */
public class CircleItem implements MultiItemEntity {

    public String content;
    public int    sourceId;
    public String typeId;

    public CircleItem(String typeId, String content, int sourceId) {
        this.content = content;
        this.typeId = typeId;
        this.sourceId = sourceId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getSourceId() {
        return sourceId;
    }

    public void setSourceId(int sourceId) {
        this.sourceId = sourceId;
    }

    public String getTypeId() {
        return typeId;
    }

    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }

    @Override
    public int getItemType() {
        return CircleAdapter.TYPE_LEVEL_1;
    }
}
