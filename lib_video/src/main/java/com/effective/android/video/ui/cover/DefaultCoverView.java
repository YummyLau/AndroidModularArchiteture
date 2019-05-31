package com.effective.android.video.ui.cover;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.effective.android.video.R;
import com.effective.android.video.Utils;
import com.effective.android.video.bean.VideoInfo;
import com.effective.android.video.bean.VideoStatus;

import androidx.annotation.DrawableRes;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

/**
 * 默认提供的浮层view
 * Created by yummylau on 2019/04/20.
 */
public class DefaultCoverView extends RelativeLayout implements View.OnClickListener {

    private RelativeLayout controlView;
    private ImageView lbPlay;                               //底部控制栏：左下播放按钮
    private TextView lbPosition;                            //底部控制栏：左下播放时间
    private TextView rbDuration;                            //底部控制栏：视频总时长
    private ImageView rbFullScreen;                         //底部控制栏：右下全屏按钮
    private SeekBar bProgress;                              //底部控制栏：底部进度条

    private ImageView ltBack, rtVolume;                     //顶部栏：返回，音量

    private ImageView midStatusImg;                         //中间：播放暂停按钮，loading，重播，中部提示
    private TextView midStatusTip;

    private View coverLayout;                               //封面：封面图片，时间提示
    private ImageView cover;
    private TextView videoTimeTip;                          //右下角视频事件提示（悬浮）

    private Drawable playStatusIcon;
    private Drawable playIcon;
    private Drawable pauseStatusIcon;
    private Drawable pauseIcon;
    private Drawable loadingStatusIcon;
    private Drawable replayIcon;
    private Drawable backIcon;
    private Drawable volumeIcon;
    private Animation loadingAni;

    private boolean enableControl;
    private boolean enableVideoTime;
    private boolean enableBack;
    private boolean enableVolume;

    private boolean isTouchProgress = false;

    private VideoStatus videoStatus;
    private ICoverReceivingLayer controlListener;
    private ICoverControlLayer coverControlLayer;
    private VideoStatusListener statusListener;
    private VideoProcessListener processListener;

    private InnerHideControlRunnable innerHideControlRunnable;
    private InnerTrackerProgressRunnable innerTrackerProgressRunnable;

    private static final long QUERY_PROGRESS_MS = 200;
    private static final long CONTROL_AUTO_HIDDEN_MS = 5 * 1000;

    public DefaultCoverView(Context context) {
        this(context, null);
    }

    public DefaultCoverView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DefaultCoverView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs, defStyleAttr);
    }

    private void initView(Context context, AttributeSet attrs, int defStyleAttr) {
        //顶部
        View root = LayoutInflater.from(context).inflate(R.layout.video_default_cover_layout, this, true);
        ltBack = root.findViewById(R.id.control_lt_back);
        rtVolume = root.findViewById(R.id.control_rt_volume);

        //中部
        midStatusImg = root.findViewById(R.id.control_mid_video_status_img);
        midStatusTip = root.findViewById(R.id.control_mid_video_status_tip);

        //底部控制栏
        controlView = root.findViewById(R.id.control_bottom_layout);
        rbFullScreen = root.findViewById(R.id.control_rb_full_screen);
        lbPlay = root.findViewById(R.id.control_lb_play);
        lbPosition = root.findViewById(R.id.control_lb_position);
        rbDuration = root.findViewById(R.id.control_rb_duration);
        bProgress = root.findViewById(R.id.control_b_progress);

        //封面
        coverLayout = root.findViewById(R.id.cover_layout);
        cover = root.findViewById(R.id.cover);
        videoTimeTip = root.findViewById(R.id.control_rb_video_time);

        final TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.DefaultCoverView, defStyleAttr, 0);
        if (typedArray != null) {
            backIcon = typedArray.getDrawable(R.styleable.DefaultCoverView_ic_back);
            volumeIcon = typedArray.getDrawable(R.styleable.DefaultCoverView_ic_volume);
            playStatusIcon = typedArray.getDrawable(R.styleable.DefaultCoverView_ic_play_state);
            pauseStatusIcon = typedArray.getDrawable(R.styleable.DefaultCoverView_ic_pause_state);
            replayIcon = typedArray.getDrawable(R.styleable.DefaultCoverView_ic_replay_state);
            loadingStatusIcon = typedArray.getDrawable(R.styleable.DefaultCoverView_ic_loading_state);
            enableVideoTime = typedArray.getBoolean(R.styleable.DefaultCoverView_open_time, false);
            enableBack = typedArray.getBoolean(R.styleable.DefaultCoverView_open_back, false);
            enableVolume = typedArray.getBoolean(R.styleable.DefaultCoverView_open_volume, false);
            enableControl = typedArray.getBoolean(R.styleable.DefaultCoverView_open_control, false);
            typedArray.recycle();
        }
        playIcon = ContextCompat.getDrawable(getContext(), R.drawable.video_ic_video_play);
        pauseIcon = ContextCompat.getDrawable(getContext(), R.drawable.video_ic_video_pause);

        if (playStatusIcon == null) {
            playStatusIcon = ContextCompat.getDrawable(getContext(), R.drawable.video_ic_video_play_state);
        }
        if (pauseStatusIcon == null) {
            pauseStatusIcon = ContextCompat.getDrawable(getContext(), R.drawable.video_ic_video_pause_state);
        }
        if (replayIcon == null) {
            replayIcon = ContextCompat.getDrawable(getContext(), R.drawable.video_ic_video_replay_state);
        }
        if (loadingStatusIcon == null) {
            loadingStatusIcon = ContextCompat.getDrawable(getContext(), R.drawable.video_ic_video_loading_state);
        }
        if (backIcon == null) {
            backIcon = ContextCompat.getDrawable(getContext(), R.drawable.video_ic_top_left_back);
        }
        if (volumeIcon == null) {
            volumeIcon = ContextCompat.getDrawable(getContext(), R.drawable.video_ic_top_right_volume);
        }

        midStatusImg.setImageDrawable(loadingStatusIcon);
        ltBack.setImageDrawable(backIcon);
        rtVolume.setImageDrawable(volumeIcon);

        rtVolume.setVisibility(enableVolume ? VISIBLE : GONE);
        ltBack.setVisibility(enableBack ? VISIBLE : GONE);

        coverLayout.setVisibility(GONE);

        ltBack.setOnClickListener(this);
        rtVolume.setOnClickListener(this);
        rbFullScreen.setOnClickListener(this);
        lbPlay.setOnClickListener(this);
        midStatusTip.setOnClickListener(this);
        midStatusImg.setOnClickListener(this);

        if (innerHideControlRunnable == null) {
            innerHideControlRunnable = new InnerHideControlRunnable();
        } else {
            removeCallbacks(innerHideControlRunnable);
        }
        if (innerTrackerProgressRunnable == null) {
            innerTrackerProgressRunnable = new InnerTrackerProgressRunnable();
        } else {
            removeCallbacks(innerTrackerProgressRunnable);
        }
    }

    public void setEnableVideoTime(boolean enableTime) {
        this.enableVideoTime = enableTime;
    }

    public void setLoadingStatusIcon(@DrawableRes int id) {
        loadingStatusIcon = ContextCompat.getDrawable(getContext(), id);
        midStatusImg.setImageDrawable(loadingStatusIcon);
    }

    public void setPauseStatusIcon(@DrawableRes int id) {
        pauseStatusIcon = ContextCompat.getDrawable(getContext(), id);
    }

    public void setPlayStatusIcon(@DrawableRes int id) {
        playStatusIcon = ContextCompat.getDrawable(getContext(), id);
    }

    public void setCoverListener(ICoverReceivingLayer controlListener) {
        this.controlListener = controlListener;
    }

    public void initPlayerCover(ICoverControlLayer coverControlLayer) {
        this.coverControlLayer = coverControlLayer;
    }

    public void setStatusListener(VideoStatusListener statusListener) {
        this.statusListener = statusListener;
    }

    public void setProcessListener(VideoProcessListener processListener) {
        this.processListener = processListener;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.control_lt_back: {
                if (controlListener != null) {
                    controlListener.onBack();
                }
                break;
            }
            case R.id.control_lb_play: {
                if (coverControlLayer != null && coverControlLayer.isPlaying()) {
                    setControlVisible(true);
                    coverControlLayer.pause();
                    return;
                }
                if (coverControlLayer != null && coverControlLayer.isPause()) {
                    setControlVisible(true);
                    coverControlLayer.play();
                    return;
                }
                break;
            }

            case R.id.control_mid_video_status_img: {
                if (coverVisible()) {
                    if (controlListener != null) {
                        controlListener.onCoverClick();
                    }
                    return;
                }

                if (coverControlLayer != null && coverControlLayer.isPlaying()) {
                    setControlVisible(true);
                    coverControlLayer.pause();
                    return;
                }
                if (coverControlLayer != null && coverControlLayer.isPause()) {
                    setControlVisible(true);
                    coverControlLayer.play();
                    return;
                }
                break;
            }
            case R.id.control_rb_full_screen: {
                if (coverControlLayer != null && coverControlLayer.isFullScreen()) {
                    exitFullScreen();
                } else {
                    enterFullScreen();
                }
                break;
            }
        }
    }

    public void enterFullScreen() {
        if (controlListener != null && controlListener.onFullScreen(true)) {
            if (coverControlLayer != null) {
                coverControlLayer.enterFullScreen();
                rbFullScreen.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.video_ic_collapse));
            }
        }
    }

    public void exitFullScreen() {
        if (controlListener != null && controlListener.onFullScreen(false)) {
            if (coverControlLayer != null) {
                coverControlLayer.exitFullScreen();
                rbFullScreen.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.video_ic_unfold));
            }
        }
    }

//    public void clearGlide() {
//        GLImageLoader.clearRequest(cover);
//    }
//
//    public void clearCover() {
//        if (cover != null) {
//            cover.setImageDrawable(null);
//        }
//    }

    public void setControlVisible(boolean visible) {
        controlView.setVisibility(visible ? VISIBLE : GONE);
        if (coverControlLayer != null && coverControlLayer.isPlaying()) {
            setMidStatusImg(VideoStatus.PLAYING, visible);
        } else if (coverControlLayer != null && coverControlLayer.isPause()) {
            setMidStatusImg(VideoStatus.PAUSE, visible);
        }
        removeCallbacks(innerHideControlRunnable);
        if (visible) {
            postHideControlRunnable();
        }
        if (controlListener != null) {
            controlListener.onControlVisible(controlView.getVisibility() == VISIBLE);
        }
    }

    private boolean coverVisible() {
        return coverLayout.getVisibility() == VISIBLE;
    }

    public void setCoverVisible(boolean visible) {
        coverLayout.setVisibility(visible ? VISIBLE : GONE);
        if (visible) {
            if (coverControlLayer != null && coverControlLayer.isVideoLoaded()) {
                setVideoTimeTip(coverControlLayer.getVideoDuration(), 0);
            } else if (coverControlLayer != null && coverControlLayer.isMediaInfoLoaded()) {
                setVideoTimeTip(coverControlLayer.getMediaDuration(), 0);
            } else if (videoInfo != null) {
                setVideoTimeTip(videoInfo.duration, 0);
            } else {
                videoTimeTip.setVisibility(GONE);
            }
        }
    }

    public void setCoverColor(@DrawableRes int resId) {
        if (cover != null) {
            cover.setImageResource(resId);
        }
    }

    public void setVideoTimeTip(long duration, long position) {
        if (enableVideoTime) {
            if (duration > position) {
                videoTimeTip.setVisibility(VISIBLE);
                videoTimeTip.setText(Utils.getRemainingVideoTimeStr(duration, position));
            } else {
                videoTimeTip.setVisibility(GONE);
            }
        } else {
            videoTimeTip.setVisibility(GONE);
        }
    }

    private void setMidStatusImg(VideoStatus videoStatus, boolean isVisible) {
        switch (videoStatus) {
            case PREPARE: {
                if (isVisible) {
                    midStatusImg.clearAnimation();
                    midStatusImg.setVisibility(VISIBLE);
                    midStatusImg.setImageDrawable(loadingStatusIcon);
                    midStatusImg.startAnimation(getLoadingAnimation());
                } else {
                    midStatusImg.clearAnimation();
                    midStatusImg.setVisibility(GONE);
                }
                break;
            }
            case BUFFERING: {
                if (isVisible) {
                    midStatusImg.clearAnimation();
                    midStatusImg.setVisibility(VISIBLE);
                    midStatusImg.setImageDrawable(loadingStatusIcon);
                    midStatusImg.startAnimation(getLoadingAnimation());
                } else {
                    midStatusImg.clearAnimation();
                    midStatusImg.setVisibility(GONE);
                }
                break;
            }

            case PLAYING: {
                midStatusImg.clearAnimation();
                midStatusImg.setImageDrawable(pauseStatusIcon);
                lbPlay.setImageDrawable(pauseIcon);
                midStatusImg.setVisibility(isVisible ? VISIBLE : GONE);
                break;
            }

            case PAUSE: {
                midStatusImg.clearAnimation();
                midStatusImg.setImageDrawable(playStatusIcon);
                lbPlay.setImageDrawable(playIcon);
                midStatusImg.setVisibility(isVisible ? VISIBLE : GONE);
                break;
            }

            default: {
                midStatusImg.clearAnimation();
                midStatusImg.setVisibility(GONE);
            }
        }
    }

    public void onVideoStatus(VideoStatus status, String msg) {
        switch (status) {
            case PREPARE: {
                setMidStatusImg(VideoStatus.PREPARE, true);                                                  //视频信息加载完成之前，loading显示，界面点击无效
                setMidStatusTip(false);
                setControlVisible(false);
                break;
            }
            case BUFFERING: {
                if (videoStatus != VideoStatus.BUFFERING) {                    //如果当前mid正在显示，则需要更改状态
                    setMidStatusImg(VideoStatus.BUFFERING, true);
                }
                setMidStatusTip(false);
                break;
            }
            case PLAYING: {
                if (isMidStatusImgVisible() && isControlVisible() && videoStatus != VideoStatus.PLAYING) {           //如果当前mid正在显示，则需要更改状态
                    setMidStatusImg(status, true);
                } else {
                    setMidStatusImg(status, false);
                }
                setMidStatusTip(false);
                break;
            }
            case PAUSE: {
                if (isMidStatusImgVisible() && isControlVisible() && videoStatus != VideoStatus.PAUSE) {               //如果当前mid正在显示，则需要更改状态
                    setMidStatusImg(status, true);
                } else {
                    setMidStatusImg(status, false);
                }
                setMidStatusTip(false);
                break;
            }
            case FINISH: {
                setMidStatusImg(status, false);
                setMidStatusTip(false);
                break;
            }
            case ERROR: {
                setMidStatusImg(status, false);
                setMidStatusTip(false);
//                setMidStatusTip(true);
//                midStatusTip.setText(R.string.video_load_error);
                break;
            }
        }
        if (statusListener != null) {
            statusListener.onStatus(status, msg);
        }
        videoStatus = status;
    }


    public void setMidStatusTip(boolean visible) {
        midStatusTip.setVisibility(visible ? VISIBLE : GONE);
    }

    public boolean isMidStatusImgVisible() {
        return midStatusImg.getVisibility() == VISIBLE;
    }

    public boolean isControlVisible() {
        return controlView.getVisibility() == VISIBLE;
    }

    public void hideControlView() {
        setControlVisible(false);
    }

    private Animation getLoadingAnimation() {
        if (loadingAni == null) {
            loadingAni = AnimationUtils.loadAnimation(getContext(), R.anim.video_loading_anim);
        }
        return loadingAni;
    }

    private ImageView.ScaleType scaleType;
    private boolean isFullScreenCover;
    private VideoInfo videoInfo;

    public void initCover(VideoInfo videoInfo, @Nullable ImageView.ScaleType scaleType, boolean fullScreen) {
        if (videoInfo == null) {
            setCoverVisible(false);
            midStatusImg.setVisibility(GONE);
            return;
        }
        this.videoInfo = videoInfo;
        setCoverVisible(true);
        midStatusImg.clearAnimation();
        midStatusImg.setVisibility(VISIBLE);
        midStatusImg.setImageDrawable(playStatusIcon);
        this.scaleType = scaleType;
        if (this.scaleType == null) {
            cover.setScaleType(ImageView.ScaleType.CENTER_CROP);
        } else {
            cover.setScaleType(ImageView.ScaleType.FIT_CENTER);
        }
        isFullScreenCover = fullScreen;
//        GLImageLoader.disMediaCover(cover, videoInfo.cover, isFullScreenCover);
    }

    public void resetCover() {
        initCover(videoInfo, scaleType, isFullScreenCover);
    }

    public void syncTouchProgress(boolean touchProgress) {
        isTouchProgress = touchProgress;
    }

    public boolean isTouchProgress() {
        return isTouchProgress;
    }

    public void initControlView() {
        if (coverControlLayer == null) return;
        rbDuration.setText(Utils.getVideoTimeStr(coverControlLayer.getVideoDuration()));
        //单位精确到 0.1 秒
        bProgress.setMax(timeToProgress(coverControlLayer.getVideoDuration()));

        if (processListener != null) {
            processListener.onDuration(coverControlLayer.getVideoDuration());
        }

        bProgress.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            private long videoPosition = 0;

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                videoPosition = progressToTime(progress);
                if (processListener != null) {
                    processListener.onCurrentPosition(videoPosition);
                }
                lbPosition.setText(Utils.getVideoTimeStr(videoPosition));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                isTouchProgress = true;
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                videoPosition = progressToTime(bProgress.getProgress());
                coverControlLayer.makeSurePlay();
                coverControlLayer.seekTo(videoPosition);
                if (processListener != null) {
                    processListener.onCurrentPosition(videoPosition);
                }
                lbPosition.setText(Utils.getVideoTimeStr(videoPosition));
                isTouchProgress = false;
            }
        });
        postTrackerProgressRunnable();
    }

    public void setCoverImage(Drawable drawable) {
        cover.setImageDrawable(drawable);
    }

    public void hideDisplayCover(boolean hide) {
        cover.setVisibility(hide ? GONE : VISIBLE);
    }

    public void setPlayImage(Drawable drawable) {
        lbPlay.setImageDrawable(drawable);
    }


    public int timeToProgress(long videoTime) {
        return (int) (videoTime / 100);
    }

    private long progressToTime(int progress) {
        return progress * 100;
    }

    public void setProgress(int progress) {
        bProgress.setProgress(progress);
    }

    public void notifyBufferProcess(long bufferedPosition) {
        if (processListener != null) {
            processListener.onBufferPosition(bufferedPosition);
        }
    }

    public void setBufferedProgress(int bufferedProgress) {
        bProgress.setSecondaryProgress(bufferedProgress);
    }

    public void postHideControlRunnable() {
        removeCallbacks(innerHideControlRunnable);
        postDelayed(innerHideControlRunnable, CONTROL_AUTO_HIDDEN_MS);
    }

    public void removeControlRunnable() {
        removeCallbacks(innerHideControlRunnable);
    }

    public void postTrackerProgressRunnable() {
        removeCallbacks(innerTrackerProgressRunnable);
        post(innerTrackerProgressRunnable);
    }

    public void removeTrackerProgressRunnable() {
        removeCallbacks(innerTrackerProgressRunnable);
    }


    private class InnerHideControlRunnable implements Runnable {

        @Override
        public void run() {
            setControlVisible(false);
        }
    }

    public class InnerTrackerProgressRunnable implements Runnable {
        @Override
        public void run() {
            if (coverControlLayer != null) {
                long position = coverControlLayer.getVideoCurrentPosition();
                long duration = coverControlLayer.getVideoDuration();
                if (!isTouchProgress()) {
                    setProgress(timeToProgress(position));
                }
                DefaultCoverView.this.postDelayed(this, QUERY_PROGRESS_MS);
            }
        }
    }
}
