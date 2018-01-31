package com.sukritapp.smartshoppr.listner;

import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;

import com.sukritapp.smartshoppr.util.Constant;

/**
 * Created by abc on 2/19/2017.
 */

public interface RecycleViewItemClickListner {

     void onItemClick(String position, Drawable drawable, String s);

}
