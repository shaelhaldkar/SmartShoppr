package com.sukritapp.smartshoppr.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v4.app.Fragment;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.sukritapp.smartshoppr.R;
import com.sukritapp.smartshoppr.model.HomeTittle;
import com.sukritapp.smartshoppr.model.ResponseModel;

import com.sukritapp.smartshoppr.fragments.SeeAllDeatilFragments;
import com.sukritapp.smartshoppr.util.AppLog;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by abc on 4/12/2017.
 */

public class SeeAllCategoriesGridViewAdapter extends RecyclerView.Adapter<SeeAllCategoriesGridViewAdapter.SeeAllCategoryViewHolder> {

    private ArrayList<HomeTittle> mList;
    private Context mContext;
    private DisplayImageOptions options;
    private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();
    Fragment fragment = null;

    public SeeAllCategoriesGridViewAdapter(ArrayList<HomeTittle> mList, Context mContext, Fragment activity) {
        this.mList = mList;
        this.mContext = mContext;
        this.fragment = activity;

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

    @Override
    public SeeAllCategoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.see_all_categories_item, parent, false);

        return new SeeAllCategoryViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(SeeAllCategoryViewHolder holder, int position) {
        try {
            String tittle = mList.get(position).getTittle();
            String output = tittle.substring(0, 1).toUpperCase() + tittle.substring(1);

            holder.tvVwCategoriesName.setText(output);
            ImageLoader.getInstance().displayImage(mList.get(position).getLogo(), holder.imgVwCategoriesIcon, options, animateFirstListener);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {

        return mList.size();
    }

    public class SeeAllCategoryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tvVwCategoriesName;
        ImageView imgVwCategoriesIcon;

        public SeeAllCategoryViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            imgVwCategoriesIcon = (ImageView) itemView.findViewById(R.id.imgVw_category_icon);
            tvVwCategoriesName = (TextView) itemView.findViewById(R.id.tvVw_category_name);

        }

        @Override
        public void onClick(View view) {
//            int position = Integer.parseInt(view.getTag().toString());
//            mList.get(position);


            Bundle bundle = new Bundle();
            ArrayList<ResponseModel> listresponseModels = mList.get(getAdapterPosition()).getMlist();
            String tittle = mList.get(getAdapterPosition()).getTittle();

            bundle.putParcelableArrayList("list", listresponseModels);
            bundle.putString("tittle", tittle);
            Fragment nextFrag = new SeeAllDeatilFragments();
            nextFrag.setArguments(bundle);
            fragment.getFragmentManager().beginTransaction().replace(R.id.content_frame, nextFrag, null).addToBackStack(null).commit();

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