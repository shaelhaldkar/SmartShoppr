package com.sukritapp.smartshoppr.adapter;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.sukritapp.smartshoppr.R;
import com.sukritapp.smartshoppr.activity.BookCabsActivity;
import com.sukritapp.smartshoppr.model.ResponseModel;
import com.sukritapp.smartshoppr.util.AppLog;
import com.sukritapp.smartshoppr.util.Constant;
import com.sukritapp.smartshoppr.util.SmartShopprUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by abc on 12/29/2016.
 */

public class CategoryHorizontalAdapter extends RecyclerView.Adapter<CategoryHorizontalAdapter.MyAdapter> {
    private final String TAG = AppLog.getClassName();


    private DisplayImageOptions options;
    private String[] IMAGE_URLS;
    ArrayList<ResponseModel> dataList = new ArrayList<>();
    private final Fragment activity;
    private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();

    public CategoryHorizontalAdapter(ArrayList<ResponseModel> dataList, Fragment fragment) {
        this.dataList = dataList;
        this.activity=fragment;
        initImagerArray(dataList);
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


    }


    // private List<Data> data;

    @Override
    public CategoryHorizontalAdapter.MyAdapter onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.lay_recyleview_item, parent, false);

//        return new MyAdapter(itemView,TAG);
        return new MyAdapter(itemView);
    }

    @Override
    public void onBindViewHolder(CategoryHorizontalAdapter.MyAdapter holder, int position) {
//        holder.imageView.setImageResource(horizontalList.get(position).getImgId());
        holder.txtviewdata.setText(dataList.get(position).getDiscription());
//        holder.tag=TAG;


        ImageLoader.getInstance().displayImage(IMAGE_URLS[position], holder.imageView, options, animateFirstListener);

        // for ola uber

        if (dataList.get(position).getDiscription().equalsIgnoreCase("Comapre Price")){
            holder.imageView.setImageResource(R.drawable.ic_comparison);

        }else if (dataList.get(position).getDiscription().equalsIgnoreCase("Uber")){
            holder.imageView.setImageResource(R.drawable.ic_uber);

        }else if (dataList.get(position).getDiscription().equalsIgnoreCase("Ola")) {
            holder.imageView.setImageResource(R.drawable.ic_ola);

        }



    }

    @Override
    public int getItemCount() {
        return IMAGE_URLS.length;
    }

    private void initImagerArray(ArrayList<ResponseModel> responseModels) {
        ArrayList<String> mBannerURLList = new ArrayList<>();
        for (int i = 0; i < responseModels.size(); i++) {
            mBannerURLList.add(responseModels.get(i).getLogo());
        }
        IMAGE_URLS = mBannerURLList.toArray(new String[mBannerURLList.size()]);
        AppLog.enter("TAG", AppLog.getMethodName());
    }


    public class MyAdapter extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView imageView;
        TextView txtviewdata;
        String tag;


        public MyAdapter(View view) {
            super(view);
            view.setOnClickListener(this);
            imageView = (ImageView) view.findViewById(R.id.mImVwRecyleVwItem);
            txtviewdata = (TextView) view.findViewById(R.id.mTvRecyleVwItem);
        }

        @Override
        public void onClick(View view) {
            //show on webview
            Bundle bundle= new Bundle();
            bundle.putParcelable(Constant.BUNDLE_KEY,dataList.get(getAdapterPosition()));
            String itemType=dataList.get(getAdapterPosition()).getDiscription();
            if(itemType.equalsIgnoreCase(Constant.TAG_ITEM_TYPE_PRICE_COMPARISION) ||
                    itemType.equalsIgnoreCase(Constant.TAG_ITEM_TYPE_OLA) ||
                    itemType.equalsIgnoreCase(Constant.TAG_ITEM_TYPE_UBER) ){
                activity.startActivity(new Intent(activity.getActivity(), BookCabsActivity.class).putExtra("itemType",itemType));

            }else {

                SmartShopprUtils.getInstance().showWebFragment(bundle);
                AppLog.info(TAG, "url : " + dataList.get(getAdapterPosition()).getWebURl());
            }
        }
    }


    private static class AnimateFirstDisplayListener extends SimpleImageLoadingListener {

        static final List<String> displayedImages = Collections.synchronizedList(new LinkedList<String>());

        @Override
        public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
            if (loadedImage != null) {
                ImageView imageView = (ImageView) view;
                boolean firstDisplay = !displayedImages.contains(imageUri);
                if (firstDisplay) {
                    FadeInBitmapDisplayer.animate(imageView, 500);
                    displayedImages.add(imageUri);
                }
            }
        }
    }
}
