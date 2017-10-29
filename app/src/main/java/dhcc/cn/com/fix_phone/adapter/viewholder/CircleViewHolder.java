package dhcc.cn.com.fix_phone.adapter.viewholder;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewStub;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.List;

import dhcc.cn.com.fix_phone.Account;
import dhcc.cn.com.fix_phone.R;
import dhcc.cn.com.fix_phone.bean.CircleItem;
import dhcc.cn.com.fix_phone.bean.CommentItem;
import dhcc.cn.com.fix_phone.bean.FavortItem;
import dhcc.cn.com.fix_phone.ui.activity.BusinessActivity;
import dhcc.cn.com.fix_phone.ui.activity.LoginActivity;
import dhcc.cn.com.fix_phone.ui.widget.CommentListView;
import dhcc.cn.com.fix_phone.ui.widget.ExpandTextView;
import dhcc.cn.com.fix_phone.ui.widget.PraiseListView;
import dhcc.cn.com.fix_phone.ui.widget.SnsPopupWindow;
import dhcc.cn.com.fix_phone.ui.widget.videolist.model.VideoLoadMvpView;
import dhcc.cn.com.fix_phone.ui.widget.videolist.widget.TextureVideoView;
import dhcc.cn.com.fix_phone.utils.CommunicationUtil;
import dhcc.cn.com.fix_phone.utils.GlideCircleTransform;
import dhcc.cn.com.fix_phone.utils.UIUtils;
import dhcc.cn.com.fix_phone.utils.UrlUtils;

/**
 * Created by yiw on 2016/8/16.
 */
public abstract class CircleViewHolder extends RecyclerView.ViewHolder implements VideoLoadMvpView {

    public final static int TYPE_URL   = 1;
    public final static int TYPE_IMAGE = 2;
    public final static int TYPE_VIDEO = 3;

    protected Context context = UIUtils.getContext();

    protected Activity mActivity;

    public void setActivity(Activity activity) {
        mActivity = activity;
    }

    public int viewType;

    public ImageView      headIv;
    public TextView       nameTv;
    public TextView       urlTipTv;
    /**
     * 动态的内容
     */
    public ExpandTextView contentTv;
    public TextView       timeTv;
    public TextView       deleteBtn;
    public View           snsBtn;
    /**
     * 点赞列表
     */
    public PraiseListView praiseListView;

    public LinearLayout digCommentBody;
    public View         digLine;

    public LinearLayout mCommunication; //沟通

    /**
     * 评论列表
     */
    public CommentListView commentList;
    public SnsPopupWindow  snsPopupWindow;


    public CircleViewHolder(View itemView, int viewType) {
        super(itemView);
        this.viewType = viewType;

        ViewStub viewStub = itemView.findViewById(R.id.viewStub);

        initSubView(viewType, viewStub);

        headIv = itemView.findViewById(R.id.headIv);
        nameTv = itemView.findViewById(R.id.nameTv);
        digLine = itemView.findViewById(R.id.lin_dig);

        contentTv = itemView.findViewById(R.id.contentTv);
        urlTipTv = itemView.findViewById(R.id.urlTipTv);
        timeTv = itemView.findViewById(R.id.timeTv);
        deleteBtn = itemView.findViewById(R.id.deleteBtn);
        snsBtn = itemView.findViewById(R.id.snsBtn);
        praiseListView = itemView.findViewById(R.id.praiseListView);

        digCommentBody = itemView.findViewById(R.id.digCommentBody);
        commentList = itemView.findViewById(R.id.commentList);

        mCommunication = itemView.findViewById(R.id.communication);

        snsPopupWindow = new SnsPopupWindow(itemView.getContext());

    }

    public abstract void initSubView(int viewType, ViewStub viewStub);

    @Override
    public TextureVideoView getVideoView() {
        return null;
    }

    @Override
    public void videoBeginning() {

    }

    @Override
    public void videoStopped() {

    }

    @Override
    public void videoPrepared(MediaPlayer player) {

    }

    @Override
    public void videoResourceReady(String videoPath) {

    }


    public void setData(final CircleItem circleItem) {
        final String circleId = circleItem.getUser().getFInterID();

        handleHeadIcon(circleItem);

        handleContent(circleItem);

        handleFavort(circleItem);

        handleComment(circleItem);

        handleDigCommentBody(circleItem);

        handleUrlTipTv(circleItem);

        handledigLine(circleItem);

        handleChildData(circleItem);

        handleListener(circleItem);

    }

    private void handleListener(final CircleItem circleItem) {
        mCommunication.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!Account.isLogin()) {
                    context.startActivity(new Intent(context, LoginActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                    return;
                }
                if (Account.getLoginInfo() == null) {
                    context.startActivity(new Intent(context, LoginActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                    return;
                }
                if (TextUtils.equals(circleItem.getUser().FPhone, Account.getLoginInfo().getPhone())) {
                    Toast.makeText(context, "不能与自己沟通", Toast.LENGTH_SHORT).show();
                    return;
                }
                CommunicationUtil.communication(mActivity, circleItem.getUser().FCreatorID + "", circleItem.getUser().FCompanyName);
            }
        });

        headIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, BusinessActivity.class);
                intent.putExtra("name", circleItem.getUser().FUserName).
                        putExtra("headurl", circleItem.getUser().FHeadUrl).
                        putExtra("weChatId", circleItem.getUser().FCreatorID).
                        putExtra("userID", circleItem.getUser().FCreatorID + "");
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });


    }

    protected void handleChildData(CircleItem circleItem) {

    }

    private void handledigLine(CircleItem circleItem) {
        boolean hasComment = circleItem.hasComment();
        boolean hasFavort = circleItem.hasFavort();
        digLine.setVisibility(hasFavort && hasComment ? View.VISIBLE : View.GONE);
    }

    private void handleUrlTipTv(CircleItem circleItem) {
        urlTipTv.setVisibility(View.INVISIBLE);
    }

    private void handleDigCommentBody(CircleItem circleItem) {
        boolean hasComment = circleItem.hasComment();
        boolean hasFavort = circleItem.hasFavort();

        if (hasComment || hasFavort) {
            digCommentBody.setVisibility(View.VISIBLE);
        } else {
            digCommentBody.setVisibility(View.GONE);
        }
    }

    private void handleComment(CircleItem circleItem) {
        final List<CommentItem> commentsDatas = circleItem.getComments();
        boolean hasComment = circleItem.hasComment();
        if (hasComment) {//处理评论列表
            commentList.setDatas(commentsDatas);
            commentList.setVisibility(View.VISIBLE);
        } else {
            commentList.setVisibility(View.GONE);
        }
    }

    private void handleFavort(CircleItem circleItem) {
        final List<FavortItem> favortDatas = circleItem.getFavorters();
        boolean hasFavort = circleItem.hasFavort();
        if (hasFavort) {//处理点赞列表
            praiseListView.setDatas(favortDatas);
            praiseListView.setVisibility(View.VISIBLE);
        } else {
            praiseListView.setVisibility(View.GONE);
        }
    }

    private void handleHeadIcon(CircleItem circleItem) {
        String name = circleItem.getUser().getFUserName();
        String headImg = circleItem.getUser().getFHeadUrl();
        String timeAgo = circleItem.getUser().getFTimeAgo();
        Glide.with(context).load(headImg).diskCacheStrategy(DiskCacheStrategy.ALL).placeholder(R.color.bg_no_photo).transform(new GlideCircleTransform(context)).into(headIv);
        nameTv.setText(name);
        timeTv.setText(timeAgo);
    }

    private void handleContent(final CircleItem circleItem) {
        final String content = circleItem.getUser().getFContent();
        if (!TextUtils.isEmpty(content)) {
            contentTv.setExpand(circleItem.getUser().isExpand());
            contentTv.setExpandStatusListener(new ExpandTextView.ExpandStatusListener() {
                @Override
                public void statusChange(boolean isExpand) {
                    circleItem.getUser().setExpand(isExpand);
                }
            });
            contentTv.setText(UrlUtils.formatUrlString(content));
            contentTv.setVisibility(View.VISIBLE);
        } else {
            contentTv.setVisibility(View.GONE);
        }
    }
}

