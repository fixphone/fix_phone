package dhcc.cn.com.fix_phone.bean;

import com.chad.library.adapter.base.entity.MultiItemEntity;

/**
 * 2017/9/17 14
 */
public class CircleItem implements MultiItemEntity {

    public static final int TEXT     = 1;
    public static final int IMG_TEXT = 2;

    public int    itemType;
    public int    spanSize;
    public String content;
    public int    sourceId;

    public CircleItem(int itemType, int spanSize, String content, int sourceId) {
        this.itemType = itemType;
        this.spanSize = spanSize;
        this.content = content;
        this.sourceId = sourceId;
    }

    public int getSpanSize() {
        return spanSize;
    }

    public void setSpanSize(int spanSize) {
        this.spanSize = spanSize;
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

    @Override
    public int getItemType() {
        return itemType;
    }
}
