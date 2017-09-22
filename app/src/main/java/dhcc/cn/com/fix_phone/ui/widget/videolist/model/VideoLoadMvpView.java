package dhcc.cn.com.fix_phone.ui.widget.videolist.model;

import android.media.MediaPlayer;

import dhcc.cn.com.fix_phone.ui.widget.videolist.widget.TextureVideoView;


/**
 * @author Wayne
 */
public interface VideoLoadMvpView {

    TextureVideoView getVideoView();

    void videoBeginning();

    void videoStopped();

    void videoPrepared(MediaPlayer player);

    void videoResourceReady(String videoPath);
}
