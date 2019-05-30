package com.effective.android.video.ui.cover;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.effective.android.video.R;

/**
 *  默认提供的浮层view
 *  Created by g8931 on 2019/04/20.
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
            backIcon = typedArray.getDrawable(R.styleable.DefaultCoverView_back);
            volumeIcon = typedArray.getDrawable(R.styleable.DefaultCoverView_volume);
            playStatusIcon = typedArray.getDrawable(R.styleable.DefaultCoverView_play);
            pauseStatusIcon = typedArray.getDrawable(R.styleable.DefaultCoverView_pause);
            replayIcon = typedArray.getDrawable(R.styleable.DefaultCoverView_replay);
            loadingStatusIcon = typedArray.getDrawable(R.styleable.DefaultCoverView_loading);
            enableVideoTime = typedArray.getBoolean(R.styleable.DefaultCoverView_open_time_tip, false);
            enableBack = typedArray.getBoolean(R.styleable.DefaultCoverView_open_back, false);
            enableVolume = typedArray.getBoolean(R.styleable.DefaultCoverView_open_volume, false);
            enableControl = typedArray.getBoolean(R.styleable.DefaultCoverView_open_control, false);
            typedArray.recycle();
        }
        if (playStatusIcon == null) {
            playStatusIcon = ContextCompat.getDrawable(getContext(), R.drawable.icon_middle_play);
        }
        playIcon = ContextCompat.getDrawable(getContext(), R.drawable.ic_video_play);
        if (pauseStatusIcon == null) {
            pauseStatusIcon = ContextCompat.getDrawable(getContext(), R.drawable.icon_middle_pause);
        }
        pauseIcon = ContextCompat.getDrawable(getContext(), R.drawable.ic_video_pause);
        if (replayIcon == null) {
            replayIcon = ContextCompat.getDrawable(getContext(), R.drawable.icon_middle_replay);
        }
        if (loadingStatusIcon == null) {
            loadingStatusIcon = ContextCompat.getDrawable(getContext(), R.drawable.ic_common_loading_large);
        }
        if (backIcon == null) {
            backIcon = ContextCompat.getDrawable(getContext(), R.drawable.icon_top_left_back);
        }
        if (volumeIcon == null) {
            volumeIcon = ContextCompat.getDrawable(getContext(), R.drawable.icon_top_right_volume);
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

    @Override
    public void onClick(View v) {

    }
}
