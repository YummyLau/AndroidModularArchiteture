package com.effective.android.base.view;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Email yummyl.lau@gmail.com
 * Created by yummylau on 2019/06/19.
 */
public class BannerPageAdapter<R> extends PagerAdapter {

    protected List<R> mData;
    protected ViewHolder<R> viewHolder;
    private boolean once;


    public int getCount() {
        return mData.size();
    }


    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View view = viewHolder.getView(container.getContext(), toRealPosition(position), mData.get(position));
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        View view = (View) object;
        container.removeView(view);
    }


    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    public BannerPageAdapter(ViewHolder<R> holder) {
        viewHolder = holder;
        mData = new ArrayList<>();
    }

    public void setData(List<R> data) {
        mData.clear();
        mData.addAll(data);
        once = data.size() == 1;
        if (!once) {
            R first = data.get(0);
            R last = data.get(data.size() - 1);
            mData.add(first);
            mData.add(0, last);
        }
        notifyDataSetChanged();
    }


    /**
     * switch from the position in ViewPager to
     * the position in user's eyes
     */
    public int toRealPosition(int position) {
        if (once || mData.size() == 0)
            return 0;
        if (position == getCount() - 1) {
            return 0;
        } else if (position == 0) {
            return getCount() - 3;
        } else {
            return position - 1;
        }
    }

    /**
     * switch from the position in user's eyes to
     * the position in ViewPager
     */
    public int toPosition(int realPosition) {
        //only one or never set data
        if (once || mData.size() == 0) {
            return 0;
        }
        return realPosition + 1;
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        return POSITION_NONE;
    }


    public interface ViewHolder<T>{
        View getView(Context context, int position, T data);
    }


}
