package dhcc.cn.com.fix_phone.event;

import dhcc.cn.com.fix_phone.bean.ProductImage;

/**
 * 2017/10/6 10
 */
public class ProductImageEvent extends BaseEvent{
    public ProductImage mProductImage;

    public ProductImageEvent(ProductImage productImage) {
        mProductImage = productImage;
    }
}
