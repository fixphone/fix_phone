package dhcc.cn.com.fix_phone.mvp.presenter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import dhcc.cn.com.fix_phone.MyApplication;
import dhcc.cn.com.fix_phone.bean.CircleBusiness;
import dhcc.cn.com.fix_phone.bean.CircleDetailAd;
import dhcc.cn.com.fix_phone.bean.CircleItem;
import dhcc.cn.com.fix_phone.bean.CommentConfig;
import dhcc.cn.com.fix_phone.bean.FavoResponse;
import dhcc.cn.com.fix_phone.bean.PhotoInfo;
import dhcc.cn.com.fix_phone.bean.User;
import dhcc.cn.com.fix_phone.bean.VideoInfo;
import dhcc.cn.com.fix_phone.event.CirCleBusinessEvent;
import dhcc.cn.com.fix_phone.event.CircleDetailAdEvent;
import dhcc.cn.com.fix_phone.event.CollectEvent;
import dhcc.cn.com.fix_phone.event.FavoResponseEvent;
import dhcc.cn.com.fix_phone.event.PublishSuccessEvent;
import dhcc.cn.com.fix_phone.mvp.contract.CircleContract;
import dhcc.cn.com.fix_phone.mvp.modle.CircleModel;


/**
 * @author yiw
 * @ClassName: CirclePresenter
 * @Description: 通知model请求服务器和通知view更新
 * @date 2015-12-28 下午4:06:03
 */
public class CirclePresenter implements CircleContract.Presenter {
    private CircleModel         circleModel;
    private CircleContract.View view;

    public CirclePresenter(CircleContract.View view) {
        circleModel = new CircleModel();
        this.view = view;
        EventBus.getDefault().register(this);
    }


    @Override
    public void loadData(int loadType) {

    }

    /**
     * @param circleId
     * @return void    返回类型
     * @throws
     * @Title: deleteCircle
     * @Description: 删除动态
     */
    public void deleteCircle(final String circleId) {
    }

    /**
     * @param circlePosition
     * @return void    返回类型
     * @throws
     * @Title: addFavort
     * @Description: 点赞
     */
    public void addFavort(final int circlePosition) {
    }

    /**
     * @param @param circlePosition
     * @param @param favortId
     * @return void    返回类型
     * @throws
     * @Title: deleteFavort
     * @Description: 取消点赞
     */
    public void deleteFavort(final int circlePosition, final String favortId) {
    }

    /**
     * @param content
     * @param config  CommentConfig
     * @return void    返回类型
     * @throws
     * @Title: addComment
     * @Description: 增加评论
     */
    public void addComment(final String content, final CommentConfig config) {

    }

    /**
     * @param @param circlePosition
     * @param @param commentId
     * @return void    返回类型
     * @throws
     * @Title: deleteComment
     * @Description: 删除评论
     */
    public void deleteComment(final int circlePosition, final String commentId) {

    }

    /**
     * @param commentConfig
     */
    public void showEditTextBody(CommentConfig commentConfig) {

    }


    /**
     * 清除对外部对象的引用，反正内存泄露。
     */
    public void recycle() {
        this.view = null;
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onShowAd(CircleDetailAdEvent event) {
        List<CircleDetailAd.FObjectBean.ListBean> lists = event.mListBeans;
        view.updateAd(lists);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onShowBusiness(CirCleBusinessEvent event) {
        CircleBusiness.FObjectBean bean = event.mFObjectBean;
        List<CircleItem> circleItems = transformCircleItem(bean);
        view.updateCircleItem(circleItems);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void addFavo(FavoResponseEvent event) {
        FavoResponse response = event.mResponse;
        view.showError(response.FMsg);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void addSuccess(PublishSuccessEvent event) {
        if (event.isSuccess) {
            view.refreshData();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void addSuccess(CollectEvent event) {
        if (event.isSuccess) {
            view.showError("收藏成功");
        }
    }


    private List<CircleItem> transformCircleItem(CircleBusiness.FObjectBean bean) {
        List<CircleBusiness.FObjectBean.RowsBean> rows = bean.rows;
        List<CircleItem> circleItemList = new ArrayList<>();
        for (int i = 0; i < rows.size(); i++) {
            CircleBusiness.FObjectBean.RowsBean rowsBean = rows.get(i);
            CircleItem circleItem = new CircleItem();

            User user = new User();
            user.FCompanyName = rowsBean.FCompanyName;
            user.FContent = rowsBean.FContent;
            user.FCreateDate = rowsBean.FCreateDate;
            user.FCreatorID = rowsBean.FCreatorID;
            user.FHeadUrl = rowsBean.FHeadUrl;
            user.FInterID = rowsBean.FInterID;
            user.FIsFooterData = rowsBean.FIsFooterData;
            user.FPhone = rowsBean.FPhone;
            user.FShareUrl = rowsBean.FShareUrl;
            user.FTimeAgo = rowsBean.FTimeAgo;
            user.FTypeID = rowsBean.FTypeID;
            user.FTypeName = rowsBean.FTypeName;
            user.FTypeNumber = rowsBean.FTypeNumber;
            user.FUserName = rowsBean.FUserName;
            user.FUserType = rowsBean.FUserType;
            user.FUserTypeName = rowsBean.FUserTypeName;
            user.FUserTypeNumber = rowsBean.FUserTypeNumber;
            circleItem.setUser(user);
            circleItem.setType(CircleItem.TYPE_URL);

            VideoInfo videoInfo = new VideoInfo();
            CircleBusiness.FObjectBean.RowsBean.VideoBean video = rowsBean.Video;
            if (video != null) {
                videoInfo.FBitrate = video.FBitrate;
                videoInfo.FBitrateMode = video.FBitrateMode;
                videoInfo.FCaptureFileName = video.FCaptureFileName;
                videoInfo.FColorFormat = video.FColorFormat;
                videoInfo.FDisplayRatio = video.FDisplayRatio;
                videoInfo.FDisplayRatioHeight = video.FDisplayRatioHeight;
                videoInfo.FDisplayRatioWidth = video.FDisplayRatioWidth;
                videoInfo.FDuration = video.FDuration;
                videoInfo.FFileName = video.FFileName;
                videoInfo.FFrameHeight = video.FFrameHeight;
                videoInfo.FFrameRate = video.FFrameRate;
                videoInfo.FFrameWidth = video.FFrameWidth;
                videoInfo.FInterID = video.FInterID;
                videoInfo.FIsFooterData = video.FIsFooterData;
                videoInfo.FUUID = video.FUUID;
                circleItem.setType(CircleItem.TYPE_VIDEO);
                MyApplication.putMessage(video.FFileName,video.FUUID);
            }
            circleItem.setVideoInfo(videoInfo);

            List<CircleBusiness.FObjectBean.RowsBean.ImageBean> imageList = rowsBean.ImageList;
            if (imageList != null && !imageList.isEmpty()) {
                List<PhotoInfo> list = new ArrayList<>(imageList.size());
                for (int j = 0; j < imageList.size(); j++) {
                    PhotoInfo photoInfo = new PhotoInfo();
                    photoInfo.FEntryID = imageList.get(j).FEntryID;
                    photoInfo.FInterID = imageList.get(j).FInterID;
                    photoInfo.FIsFooterData = imageList.get(j).FIsFooterData;
                    photoInfo.FOriginalUrl = imageList.get(j).FOriginalUrl;
                    photoInfo.FUUID = imageList.get(j).FUUID;
                    MyApplication.putMessage(imageList.get(j).FOriginalUrl,imageList.get(j).FUUID);
                    list.add(photoInfo);
                }
                circleItem.setPhotos(list);
                circleItem.setType(CircleItem.TYPE_IMG);
            }

            circleItemList.add(circleItem);
        }

        return circleItemList;
    }
}
