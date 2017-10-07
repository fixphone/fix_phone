package dhcc.cn.com.fix_phone.mvp.contract;


import java.util.List;

import dhcc.cn.com.fix_phone.bean.CircleDetailAd;
import dhcc.cn.com.fix_phone.bean.CircleItem;
import dhcc.cn.com.fix_phone.bean.CommentConfig;
import dhcc.cn.com.fix_phone.bean.CommentItem;
import dhcc.cn.com.fix_phone.bean.FavortItem;

/**
 * Created by suneee on 2016/7/15.
 */
public interface CircleContract {

    interface View extends BaseView {
        void update2DeleteCircle(String circleId);

        void update2AddFavorite(int circlePosition, FavortItem addItem);

        void update2DeleteFavort(int circlePosition, String favortId);

        void update2AddComment(int circlePosition, CommentItem addItem);

        void update2DeleteComment(int circlePosition, String commentId);

        void updateEditTextBodyVisible(int visibility, CommentConfig commentConfig);

        void update2loadData(int loadType, List<CircleItem> datas);

        void updateAd(List<CircleDetailAd.FObjectBean.ListBean> lists);

        void updateCircleItem(List<CircleItem> circleItem);

        void showTextViewNumber(boolean b);
    }

    interface Presenter extends BasePresenter {
        void loadData(int loadType);

        void deleteCircle(final String circleId);

        void addFavort(final int circlePosition);

        void deleteFavort(final int circlePosition, final String favortId);

        void deleteComment(final int circlePosition, final String commentId);
    }
}
