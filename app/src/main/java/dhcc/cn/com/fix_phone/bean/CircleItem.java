package dhcc.cn.com.fix_phone.bean;

import java.util.List;


public class CircleItem extends BaseBean {

    public final static String TYPE_URL   = "1";
    public final static String TYPE_IMG   = "2";
    public final static String TYPE_VIDEO = "3";

    private static final long serialVersionUID = 1L;

    private String            type;//1:链接  2:图片 3:视频
    private List<PhotoInfo>   photos;
    private List<FavortItem>  favorters;
    private List<CommentItem> comments;
    private VideoInfo         mVideoInfo;
    private User              user;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<FavortItem> getFavorters() {
        return favorters;
    }

    public void setFavorters(List<FavortItem> favorters) {
        this.favorters = favorters;
    }

    public List<CommentItem> getComments() {
        return comments;
    }

    public void setComments(List<CommentItem> comments) {
        this.comments = comments;
    }

    public List<PhotoInfo> getPhotos() {
        return photos;
    }

    public void setPhotos(List<PhotoInfo> photos) {
        this.photos = photos;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public boolean hasFavort() {
        if (favorters != null && favorters.size() > 0) {
            return true;
        }
        return false;
    }

    public boolean hasComment() {
        if (comments != null && comments.size() > 0) {
            return true;
        }
        return false;
    }

    public VideoInfo getVideoInfo() {
        return mVideoInfo;
    }

    public void setVideoInfo(VideoInfo videoInfo) {
        mVideoInfo = videoInfo;
    }
}
