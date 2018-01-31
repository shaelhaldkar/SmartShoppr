package com.sukritapp.smartshoppr.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.sukritapp.smartshoppr.R;
import com.sukritapp.smartshoppr.model.ResponseModel;
import com.sukritapp.smartshoppr.util.AppLog;

import java.util.ArrayList;


/**
 * Created by abc on 12/11/2016.
 */

public class SlidingImageAdapter extends PagerAdapter {

    private final String TAG = AppLog.getClassName();


    private static  String[] IMAGE_URLS = null;

    private LayoutInflater inflater;
    private DisplayImageOptions options;
    public SlidingImageAdapter(Context context, ArrayList<ResponseModel> responseModels) {
        inflater = LayoutInflater.from(context);
        initImagerArray(responseModels);
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

    private void initImagerArray(ArrayList<ResponseModel> responseModels){
        AppLog.enter(TAG, AppLog.getMethodName());
        ArrayList<String> mBannerURLList=new ArrayList<>();
      for (int i=0;i<responseModels.size();i++){
         mBannerURLList.add(responseModels.get(i).getWebURl());
      }
        IMAGE_URLS= mBannerURLList.toArray(new String[mBannerURLList.size()]);
        AppLog.enter(TAG, AppLog.getMethodName());
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return IMAGE_URLS.length;
    }

    @Override
    public Object instantiateItem(ViewGroup view, int position) {
        View imageLayout = inflater.inflate(R.layout.pager_item, view, false);
        assert imageLayout != null;
        ImageView imageView = (ImageView) imageLayout.findViewById(R.id.image);
        final ProgressBar spinner = (ProgressBar) imageLayout.findViewById(R.id.progressBar_img);

        ImageLoader.getInstance().displayImage(IMAGE_URLS[position], imageView, options, new SimpleImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) {
                spinner.setVisibility(View.VISIBLE);
            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                String message = null;
                switch (failReason.getType()) {
                    case IO_ERROR:
                        message = "Input/Output error";
                        break;
                    case DECODING_ERROR:
                        message = "Image can't be decoded";
                        break;
                    case NETWORK_DENIED:
                        message = "Downloads are denied";
                        break;
                    case OUT_OF_MEMORY:
                        message = "Out Of Memory error";
                        break;
                    case UNKNOWN:
                        message = "Unknown error";
                        break;
                }
              //  Toast.makeText(view.getContext(), message, Toast.LENGTH_SHORT).show();

                spinner.setVisibility(View.GONE);
            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                spinner.setVisibility(View.GONE);
            }
        });

        view.addView(imageLayout, 0);
        return imageLayout;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }

    @Override
    public void restoreState(Parcelable state, ClassLoader loader) {
    }

    @Override
    public Parcelable saveState() {
        return null;
    }


//    private ArrayList<Integer> IMAGES;
//    //private LayoutInflater minflater;
//    private Context context;
//    private static final String[] IMAGE_URLS = Constant.IMAGES;
//
//    private LayoutInflater inflater;
//    private DisplayImageOptions options;
//    public SlidingImageAdapter(Context context, ArrayList<Integer> IMAGES) {
//        this.context = context;
//        this.IMAGES = IMAGES;
//        inflater = LayoutInflater.from(context);
//    }
//
//    @Override
//    public void destroyItem(ViewGroup container, int position, Object object) {
//        container.removeView((View) object);
//    }
//
//    @Override
//    public int getCount() {
//        return IMAGES.size();
//    }
//
//    @Override
//    public Object instantiateItem(ViewGroup view, int position) {
//        View imageLayout = inflater.inflate(R.layout.pager_item, view, false);
//
//        assert imageLayout != null;
//        final ImageView imageView = (ImageView) imageLayout
//                .findViewById(R.id.image);
//
//
//        imageView.setImageResource(IMAGES.get(position));
//
//        view.addView(imageLayout, 0);
//
//        return imageLayout;
//    }
//
//    @Override
//    public boolean isViewFromObject(View view, Object object) {
//        return view.equals(object);
//    }
//
//    @Override
//    public void restoreState(Parcelable state, ClassLoader loader) {
//    }
//
//    @Override
//    public Parcelable saveState() {
//        return null;
//    }
//

}
