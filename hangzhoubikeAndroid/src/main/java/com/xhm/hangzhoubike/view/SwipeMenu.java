package com.xhm.hangzhoubike.view;

import java.util.List;
import java.util.ArrayList;

import android.content.Context;
/**
 * <P></P>
 * User: <a href="mailto:xhm.xuhm@alibaba-inc.com">苍旻</a>
 * Date: 14/9/4
 * Time: 下午3:09
 */
public class SwipeMenu {

    private Context mContext;
    private List<SwipeMenuItem> mItems;

    public SwipeMenu(Context context) {
        mContext = context;
        mItems = new ArrayList<SwipeMenuItem>();
    }

    public Context getContext() {
        return mContext;
    }

    public void addMenuItem(SwipeMenuItem item) {
        mItems.add(item);
    }

    public void removeMenuItem(SwipeMenuItem item) {
        mItems.remove(item);
    }

    public List<SwipeMenuItem> getMenuItems() {
        return mItems;
    }

    public SwipeMenuItem getMenuItem(int index){
        return mItems.get(index);
    }
}