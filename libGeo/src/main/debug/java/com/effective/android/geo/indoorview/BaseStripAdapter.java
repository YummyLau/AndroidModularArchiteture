/*
 * Copyright (C) 2015 Baidu, Inc. All Rights Reserved.
 */
package com.effective.android.geo.indoorview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * 楼层条数据适配器
 */
public class BaseStripAdapter extends BaseAdapter {

	private LayoutInflater mInflater;
    private List<String> mFloorList = new ArrayList<String>();
    private int selectedPos;
    private Context mContext;

    private class NoteViewHolder {

        private TextView mFloorTextTV;
    }

    public BaseStripAdapter(Context ctx) {
        mInflater = (LayoutInflater) ctx
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mContext = ctx;
    }

    public void setmFloorList(List<String> mFloorList) {
        this.mFloorList = mFloorList;
    }
    
    public int getPosition(String floor) {
        int re = 0;
        for(int i = 0; i < mFloorList.size(); i++) {
            if (floor.equals(mFloorList.get(i))) {
                re = i;
                break;
            }
        }
        
        return re;
    }

    public void setNoteList(List<String> floorList) {
        mFloorList = floorList;
    }

    public int getCount() {
        return mFloorList.size();
    }

    public Object getItem(int position) {
        return mFloorList.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public void setSelectedPostion(int postion) {
        selectedPos = postion;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        NoteViewHolder holder;
        if (convertView == null) {
            convertView = new StripItem(mContext);
            holder = new NoteViewHolder();
            holder.mFloorTextTV = ((StripItem) convertView).getmText();
            convertView.setTag(holder);
        } else {
            holder = (NoteViewHolder) convertView.getTag();
        }

        String floor = mFloorList.get(position);
        if (floor != null) {
            // Log.i("indoor", "" + floor);
            holder.mFloorTextTV.setText(floor);
        }
        if (selectedPos == position) {
            refreshViewStyle(holder.mFloorTextTV, true);
        } else {
            refreshViewStyle(holder.mFloorTextTV, false);
        }
        return convertView;
    }

    private void refreshViewStyle(TextView view, boolean isSelected) {
        if (isSelected) {
            view.setBackgroundColor(StripItem.colorSelected);
        } else {
            view.setBackgroundColor(StripItem.color);
        }
        view.setSelected(isSelected);
    }

}

