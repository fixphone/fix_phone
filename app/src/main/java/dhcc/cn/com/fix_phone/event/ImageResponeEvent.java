package dhcc.cn.com.fix_phone.event;

import dhcc.cn.com.fix_phone.bean.ImageResponse;

/**
 * 2017/10/9 21
 */
public class ImageResponeEvent extends BaseEvent {
    public ImageResponse mImageResponse;

    public ImageResponeEvent(ImageResponse imageResponse) {
        mImageResponse = imageResponse;
    }
}
