package com.sukritapp.smartshoppr.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.sukritapp.smartshoppr.R;
import com.sukritapp.smartshoppr.model.ResponseModel;
import com.sukritapp.smartshoppr.util.AppLog;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by abc on 1/5/2017.
 */
public class SeeAllItemListAdaptrer  extends RecyclerView.Adapter<SeeAllItemViewHolder> {
    private final String TAG = AppLog.getClassName();

private DisplayImageOptions options;
    private String[] IMAGE_URLS;
    ArrayList<ResponseModel> dataList=new ArrayList<>();

    private ImageLoadingListener animateFirstListener = new SeeAllItemListAdaptrer.AnimateFirstDisplayListener();
    private Context context;

    public SeeAllItemListAdaptrer(Context context, ArrayList<ResponseModel> dataList ) {
        this.dataList = dataList;
        this.context = context;
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

    @Override
    public SeeAllItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        AppLog.info(TAG,"inside  SeeAllItemViewHolder  onCreateViewHolder ");

        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.lay_see_all_item, parent,false);

        SeeAllItemViewHolder rcv = new SeeAllItemViewHolder(layoutView,dataList);
        return rcv;
    }

    @Override
    public void onBindViewHolder(SeeAllItemViewHolder holder, int position) {
        holder.countryName.setText(dataList.get(position).getDiscription());

        ImageLoader.getInstance().displayImage(IMAGE_URLS[position], holder.countryPhoto, options, animateFirstListener);

//        holder.countryPhoto.setImageResource(mListLanguage.get(position).getImgId());

    }

    @Override
    public int getItemCount() {
        return this.dataList.size();
    }
    private void initImagerArray(ArrayList<ResponseModel> responseModels){
        AppLog.enter(TAG, AppLog.getMethodName());
        ArrayList<String> mBannerURLList=new ArrayList<>();
        if(responseModels !=null) {
            for (int i = 0; i < responseModels.size(); i++) {
                mBannerURLList.add(responseModels.get(i).getLogo());
            }
            IMAGE_URLS = mBannerURLList.toArray(new String[mBannerURLList.size()]);
        }
        AppLog.exit(TAG, AppLog.getMethodName());
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