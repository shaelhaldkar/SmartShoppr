package com.sukritapp.smartshoppr.fragments;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.sukritapp.smartshoppr.R;
import com.sukritapp.smartshoppr.adapter.CountryAdapter;
import com.sukritapp.smartshoppr.customview.DividerItemDecoration;
import com.sukritapp.smartshoppr.database.LocalDataBase;
import com.sukritapp.smartshoppr.listner.OnSukritResponseListner;
import com.sukritapp.smartshoppr.listner.RecycleViewItemClickListner;
import com.sukritapp.smartshoppr.model.CountryModel;
import com.sukritapp.smartshoppr.util.AppLog;
import com.sukritapp.smartshoppr.util.Constant;
import com.sukritapp.smartshoppr.util.SmartShopprApp;
import com.sukritapp.smartshoppr.util.SmartShopprUtils;
import com.sukritapp.smartshoppr.webservice.WebRetriveDataTask;

import java.util.ArrayList;

/**
 * Created by abc on 1/8/2017.
 */

public class CountryFragment extends Fragment implements OnSukritResponseListner ,RecycleViewItemClickListner{
    private final String TAG = AppLog.getClassName();
    private ProgressBar mPrgBarCountry = null;
    private RecyclerView mReVwCountryList = null;

    private TextView mTvCountryName;
    private ImageView mImgVwCountry;
    private CountryAdapter countryAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        AppLog.enter(TAG, AppLog.getMethodName());
        View rootView = inflater.inflate(R.layout.lay_country, container, false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(R.string.app_name);
        initView(rootView);
        AppLog.exit(TAG, AppLog.getMethodName());
        return rootView;
    }

    private void initView(View rootView) {
        mPrgBarCountry = (ProgressBar) rootView.findViewById(R.id.progressBar_country);
        mTvCountryName = (TextView) rootView.findViewById(R.id.txtVw_selected_country);
        mImgVwCountry = (ImageView) rootView.findViewById(R.id.imgVw_selected_country);
        mReVwCountryList = (RecyclerView) rootView.findViewById((R.id.recyclerVwCountry));

        //
        mTvCountryName.setText(LocalDataBase.getInstance().getCountry());
        mImgVwCountry.setImageBitmap(LocalDataBase.getInstance().getBitmap());
        mPrgBarCountry.setVisibility(View.VISIBLE);

        mReVwCountryList.setHasFixedSize(true);
        mReVwCountryList.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        RecyclerView.ItemDecoration itemDecoration = new
                DividerItemDecoration(SmartShopprApp.getApplication(), DividerItemDecoration.VERTICAL_LIST);
        mReVwCountryList.addItemDecoration(itemDecoration);
        new WebRetriveDataTask().retriveData(getActivity(), this, Constant.listtype.COUNTRY, null, Constant.URL_BASE + Constant.URL_COUNTRY);

    }

    @Override
    public void onResponseReceived(String error,Constant.listtype tag, String response) {
        mPrgBarCountry.setVisibility(View.INVISIBLE);
        AppLog.info(TAG,"response---"+response);
        if(error==null) {
            ArrayList<CountryModel> countrylist = SmartShopprUtils.getInstance().parseJsonGetCountryList(response);
            countryAdapter = new CountryAdapter(countrylist, this, getActivity().getApplicationContext());
            mReVwCountryList.setAdapter(countryAdapter);
        }else {
            SmartShopprUtils.getInstance().showErrorDialog(getActivity(), error);
        }
    }

    @Override
    public void onItemClick(String countryName, Drawable drawable, String defaultLanguage) {
         mImgVwCountry.setImageDrawable(drawable);
        mTvCountryName.setText(countryName);
        LocalDataBase.getInstance().setCountry(countryName);
        LocalDataBase.getInstance().setLanguage(defaultLanguage);
        AppLog.info(TAG,"country_name : "+countryName);
        AppLog.info(TAG,"country_language : "+defaultLanguage);

        Bitmap bmp=((BitmapDrawable)mImgVwCountry.getDrawable()).getBitmap();
        LocalDataBase.getInstance().saveCountryImage(bmp);



    }
}
