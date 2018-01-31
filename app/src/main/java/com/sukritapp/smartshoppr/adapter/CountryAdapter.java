package com.sukritapp.smartshoppr.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.sukritapp.smartshoppr.R;
import com.sukritapp.smartshoppr.listner.RecycleViewItemClickListner;
import com.sukritapp.smartshoppr.model.CountryModel;
import com.sukritapp.smartshoppr.model.ResponseModel;
import com.sukritapp.smartshoppr.util.AppLog;

import java.util.ArrayList;

/**
 * Created by abc on 2/19/2017.
 */

public class CountryAdapter extends RecyclerView.Adapter<CountryAdapter.MyAdapter> {
    private final String TAG = AppLog.getClassName();

    ArrayList<CountryModel> countryList = new ArrayList<>();
    private Context context;
    RecycleViewItemClickListner recycleViewItemClickListner;

    private DisplayImageOptions options;
    private String[] IMAGE_URLS;

    public CountryAdapter(ArrayList<CountryModel> countrylist, RecycleViewItemClickListner recycleViewItemClickListner, Context applicationContext) {
        this.countryList = countrylist;
        this.context = applicationContext;
        this.recycleViewItemClickListner=recycleViewItemClickListner;
        countrylist.toArray();
        options = new DisplayImageOptions.Builder()
                .showImageForEmptyUri(R.drawable.four)
                .showImageOnFail(R.drawable.one)
                .resetViewBeforeLoading(true)
                .cacheOnDisk(true)
                .imageScaleType(ImageScaleType.EXACTLY)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .considerExifParams(true)
                .displayer(new FadeInBitmapDisplayer(300))
                .build();
        initImagerArray(this.countryList);
    }

    @Override
    public MyAdapter onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.country_item, parent, false);

        return new CountryAdapter.MyAdapter(itemView,recycleViewItemClickListner);
    }

    @Override
    public void onBindViewHolder(MyAdapter holder, int position) {
        holder.txtviewCountry.setText(countryList.get(position).getCountryName());
        ImageLoader.getInstance().displayImage(IMAGE_URLS[position], holder.imageViewCountryIcon, options);

    }

    @Override
    public int getItemCount() {
        return countryList.size();
    }

    private void initImagerArray(ArrayList<CountryModel> responseModels) {
        AppLog.enter(TAG, AppLog.getMethodName());
        ArrayList<String> mCountryIconURLList = new ArrayList<>();
        for (int i = 0; i < responseModels.size(); i++) {
            mCountryIconURLList.add(responseModels.get(i).getCountryUrl());
        }
        IMAGE_URLS = mCountryIconURLList.toArray(new String[mCountryIconURLList.size()]);
        AppLog.enter(TAG, AppLog.getMethodName());
    }


    public class MyAdapter extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView imageViewCountryIcon;
        TextView txtviewCountry;

        public MyAdapter(View itemView,RecycleViewItemClickListner recycleViewItemClickListner) {
            super(itemView);
            itemView.setOnClickListener(this);
            imageViewCountryIcon = (ImageView) itemView.findViewById(R.id.imgVwCountryicon);
            txtviewCountry = (TextView) itemView.findViewById(R.id.txtVwCountryName);
        }

        @Override
        public void onClick(View view) {

            recycleViewItemClickListner.onItemClick(txtviewCountry.getText().toString(),imageViewCountryIcon.getDrawable(),countryList.get(getAdapterPosition()).getCountrydefaultLanguage().toString());
        }
    }


}
