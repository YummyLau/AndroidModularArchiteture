package com.effective.android.video

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import com.effective.android.video.bean.VideoCache
import com.effective.android.video.ui.cover.CoverReceivingLayer
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout
import kotlinx.android.synthetic.main.activity_video_detail_layout.*
import kotlinx.android.synthetic.main.item_video_layout.view.*
import kotlinx.android.synthetic.main.video_player_view_layout.view.*


class VideoDetailActivity : Activity() {

    var cache: VideoCache? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video_detail_layout)
        initView()

    }

    private fun initView() {
        val data = intent.getParcelableExtra<TestData>("data")
        info.text = data.itemName
        video_detail.visibility = View.VISIBLE
        video_detail.gesture_view.visibility = View.GONE
        video_detail.cover_view.enableVideoTime = true
        video_detail.cover_view.setCoverImage(R.drawable.shape_player_cover_bg)
        video_detail.cover_view.setLoadingStatusIcon(R.drawable.ic_common_loading_small)
        video_detail.cover_view.setPauseStatusIcon(R.drawable.video_pause)
        video_detail.cover_view.setPlayStatusIcon(R.drawable.video_play)
        //设置播放层各种监听器
        video_detail.cover_view.setCoverListener(object : CoverReceivingLayer {

            override fun onCoverClick() {
                startStreamActivity(data)
            }

            override fun onControlVisible(visible: Boolean) {
            }

            override fun onBack(): Boolean {
                return false
            }

            override fun onFullScreen(unfold: Boolean): Boolean {
                return false
            }
        })
        video_detail.setOnClickListener(View.OnClickListener {
            startStreamActivity(data)
        })
        asyncVideoAndCoverLayout(data.videoInfo!!.width, data.videoInfo!!.height)
        video_detail.initVideoInfo(data.videoInfo!!)
        if (cache == null) {
            cache = VideoCache.Builder().build(data!!.videoInfo!!, video_detail.videoReceivingLayer)
            video_detail.setVideoCache(cache!!)
        }
        if (NetUtil.isWifi(this)) {
            VideoPlayerManager.instance.start(cache!!)
        }
    }

    private fun startStreamActivity(testData: TestData) {
        val intent = Intent(this, VideoStreamActivity::class.java)
        intent.putExtra("data", testData)
        startActivity(intent)
    }

    private fun asyncVideoAndCoverLayout(width: Int, height: Int) {
        if (width == 0 || height == 0) {
            video_detail.getLayoutParams().width = ViewGroup.LayoutParams.MATCH_PARENT
            video_detail.getLayoutParams().height = Utils.dip2px(this, 176f)
        } else {
            if (width >= height) {
                video_detail.getLayoutParams().width = ViewGroup.LayoutParams.MATCH_PARENT
                video_detail.getLayoutParams().height = Utils.dip2px(this, 176f)
            } else {
                video_detail.getLayoutParams().height = Utils.dip2px(this, 222.5f)
                video_detail.getLayoutParams().width = Utils.dip2px(this, 167.5f)
            }
        }
        video_detail.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_ZOOM)
    }
}
