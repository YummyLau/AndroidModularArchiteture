package com.effective.android.video

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.effective.android.video.bean.VideoCache
import com.effective.android.video.ui.cover.CoverReceivingLayer
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout
import kotlinx.android.synthetic.main.activity_video_list_layout.*
import kotlinx.android.synthetic.main.item_video_layout.view.*
import kotlinx.android.synthetic.main.video_player_view_layout.view.*


class VideoListActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video_list_layout)
        video_list.adapter = Adapter(DataFactory.getListDatas(), this)
    }

    class TextHolder : RecyclerView.ViewHolder {

        constructor(itemView: View) : super(itemView)

        fun bindData(data: TestData) {
            itemView.info.text = data.itemName
        }
    }

    class VideoHolder : RecyclerView.ViewHolder, MediaItem {

        constructor(itemView: View) : super(itemView)

        var data: TestData? = null
        var cache: VideoCache? = null;

        fun bindData(data: TestData) {
            this.data = data
            cache = null
            itemView.info.text = data.itemName
            if (data.videoInfo != null) {
                itemView.video_detail.visibility = View.VISIBLE
                itemView.video_detail.gesture_view.visibility = View.GONE
                itemView.video_detail.cover_view.setEnableVideoTime(true)
                itemView.video_detail.cover_view.setCoverColor(R.drawable.shape_player_cover_bg)
                itemView.video_detail.cover_view.setLoadingStatusIcon(R.drawable.ic_common_loading_small)
                itemView.video_detail.cover_view.setPauseStatusIcon(R.drawable.video_pause)
                itemView.video_detail.cover_view.setPlayStatusIcon(R.drawable.video_play)

                //设置播放层各种监听器
                itemView.video_detail.cover_view.setCoverListener(object : CoverReceivingLayer {

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

                itemView.video_detail.setOnClickListener(View.OnClickListener { })
                asyncVideoAndCoverLayout(data.videoInfo!!.width, data.videoInfo!!.height)
                itemView.video_detail.initVideoInfo(data.videoInfo!!)
            } else {
                itemView.video_detail.visibility = View.GONE
            }
            itemView.setOnClickListener {
                startDetailActivity(data)
            }
        }

        private fun startStreamActivity(testData: TestData) {
            val intent = Intent(itemView.context, VideoStreamActivity::class.java)
            intent.putExtra("data", testData)
            itemView.context.startActivity(intent)
        }

        private fun startDetailActivity(testData: TestData) {
            val intent = Intent(itemView.context, VideoDetailActivity::class.java)
            intent.putExtra("data", testData)
            itemView.context.startActivity(intent)
        }

        private fun asyncVideoAndCoverLayout(width: Int, height: Int) {
            if (width == 0 || height == 0) {
                itemView.video_detail.getLayoutParams().width = ViewGroup.LayoutParams.MATCH_PARENT
                itemView.video_detail.getLayoutParams().height = Utils.dip2px(itemView.context, 176f)
            } else {
                if (width >= height) {
                    itemView.video_detail.getLayoutParams().width = ViewGroup.LayoutParams.MATCH_PARENT
                    itemView.video_detail.getLayoutParams().height = Utils.dip2px(itemView.context, 176f)
                } else {
                    itemView.video_detail.getLayoutParams().height = Utils.dip2px(itemView.context, 222.5f)
                    itemView.video_detail.getLayoutParams().width = Utils.dip2px(itemView.context, 167.5f)
                }
            }
            itemView.video_detail.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_ZOOM)
        }

        override fun onSelect() {
            if (NetUtil.isWifi(itemView.context) && data != null && data!!.videoInfo != null) {
                //复用需要释放
                itemView.video_detail.release()
                if (cache == null) {
                    cache = VideoCache.Builder().build(data!!.videoInfo!!, itemView.video_detail.videoReceivingLayer)
                    itemView.video_detail.setVideoCache(cache!!)
                }
                VideoPlayerManager.instance.start(cache!!)
            }
        }

        override fun onUnselected() {
            itemView.video_detail.pause()
            itemView.video_detail.cover_view.resetCover()
        }

        override fun onPause() {
            itemView.video_detail.pause()
        }

        override fun onResume() {
            itemView.video_detail.restore()
        }

        override fun onDestroy() {
            itemView.video_detail.release()
        }

        override fun itemKey(): String = hashCode().toString() + "_" + adapterPosition

        override fun itemPosition(): Int = adapterPosition

        override fun itemType(): MediaType = MediaType.VIDEO

        override fun mediaView(): View = itemView.video_detail
    }

    class Adapter(list: MutableList<TestData>, context: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

        private val data: MutableList<TestData> = list
        private val context: Context = context

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            return if (viewType == 0) {
                TextHolder(LayoutInflater.from(context).inflate(R.layout.item_text_layout, parent, false))
            } else {
                VideoHolder(LayoutInflater.from(context).inflate(R.layout.item_video_layout, parent, false))
            }
        }

        override fun getItemViewType(position: Int): Int {
            val data = data[position]
            return if (data.videoInfo == null) {
                0
            } else {
                1
            }
        }

        override fun getItemCount(): Int = data.size

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            val data = data[position]
            if (holder is TextHolder) {
                holder.bindData(data)
            } else {
                (holder as VideoHolder).bindData(data)
            }
        }
    }
}
