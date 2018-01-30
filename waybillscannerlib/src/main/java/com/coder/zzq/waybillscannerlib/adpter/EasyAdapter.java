package com.coder.zzq.waybillscannerlib.adpter;

import android.support.annotation.LayoutRes;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pig on 2018/1/1.
 */

public class EasyAdapter<DATA> extends android.widget.BaseAdapter{

    private List<DATA> mData;
    @LayoutRes
    private int mLayoutRes;

    public EasyAdapter(@LayoutRes int layoutRes) {
        mData = new ArrayList<>();
        mLayoutRes = layoutRes;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public DATA getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null){
            convertView = LayoutInflater.from(parent.getContext()).inflate(mLayoutRes,null);
        }

        processView(convertView,getItem(position));
        return convertView;
    }

    protected void processView(View convertView, DATA data) {

    }

    public void clear() {
        mData.clear();
    }

    public void addAll(List<DATA> truckList) {
        mData.addAll(truckList);
    }
}
