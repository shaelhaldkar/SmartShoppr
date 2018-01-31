package com.sukritapp.smartshoppr.adapter;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.sukritapp.smartshoppr.R;
import com.sukritapp.smartshoppr.listner.RecycleViewItemClickListner;
import com.sukritapp.smartshoppr.model.ResponseModel;
import com.sukritapp.smartshoppr.util.AppLog;
import com.sukritapp.smartshoppr.util.Constant;
import com.sukritapp.smartshoppr.util.SmartShopprUtils;

import java.util.ArrayList;

/**
 * Created by abc on 5/21/2017.
 */

public class ShowBookMarkViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    private final String TAG = AppLog.getClassName();
    public TextView countryName;
    public ImageView countryPhoto;

    ArrayList<ResponseModel> dataList;

    public ShowBookMarkViewHolder(View itemView, ArrayList<ResponseModel> dataList) {
        super(itemView);
        itemView.setOnClickListener(this);
        countryName = (TextView) itemView.findViewById(R.id.tvVwSeeAll);
        countryPhoto = (ImageView) itemView.findViewById(R.id.imgVwSeeAll);
        this.dataList = dataList;
    }

    @Override
    public void onClick(View view) {
        Bundle bundle= new Bundle();
        bundle.putParcelable(Constant.BUNDLE_KEY,dataList.get(getAdapterPosition()));
        SmartShopprUtils.getInstance().showWebFragment(bundle);
//        SmartShopprUtils.getInstance().showOnweb(dataList.get(getAdapterPosition()).getWebURl());
        AppLog.info(TAG, "inside  SeeAllItemViewHolder : ");

    }
}