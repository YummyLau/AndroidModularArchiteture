package com.effective.android.component.square.view.adapter.holder

import android.text.TextUtils
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.asksira.loopingviewpager.LoopingPagerAdapter
import com.asksira.loopingviewpager.LoopingViewPager
import com.effective.android.base.view.list.MediaHolder
import com.effective.android.component.square.Sdks
import com.effective.android.component.square.bean.Banner
import com.effective.android.component.square.bean.BannerList
import kotlinx.android.synthetic.main.square_holder_banner_layout.view.*

class BannerHolder(parent: ViewGroup, layoutId: Int) : MediaHolder<BannerList>(parent, layoutId) {

    override fun bindData(data: BannerList, position: Int, payloads: List<Any>) {

        itemView.bannerPager.adapter = object : LoopingPagerAdapter<Banner>(context, data, true) {

            override fun inflateView(viewType: Int, container: ViewGroup?, listPosition: Int): View {
                val view = ImageView(context)
                view.scaleType = ImageView.ScaleType.FIT_XY
                view.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
                return view
            }

            override fun bindView(convertView: View?, listPosition: Int, viewType: Int) {
                if (convertView is ImageView) {
                    if (!TextUtils.isEmpty(data[listPosition].imagePath)) {
                        Sdks.serviceImageloder.load(convertView, data[listPosition].imagePath!!, null)
                    }
                    convertView.setOnClickListener {
                        Sdks.getSdk().gotoDetailActivity(context, data[listPosition].url!!)
                    }
                }
            }
        }
        itemView.bannerPager.setIndicatorPageChangeListener(object : LoopingViewPager.IndicatorPageChangeListener {

            override fun onIndicatorProgress(selectingPosition: Int, progress: Float) {
                itemView.pageIndicatorView.setProgress(selectingPosition, progress);
            }

            override fun onIndicatorPageChange(newIndicatorPosition: Int) {
            }
        })
        itemView.pageIndicatorView.count = itemView.bannerPager.indicatorCount
    }
}