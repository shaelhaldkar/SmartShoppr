package com.sukritapp.smartshoppr.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.sukritapp.smartshoppr.R;
import com.sukritapp.smartshoppr.adapter.SeeAllCategoriesGridViewAdapter;
import com.sukritapp.smartshoppr.customview.DividerItemDecoration;
import com.sukritapp.smartshoppr.database.LocalDataBase;
import com.sukritapp.smartshoppr.listner.OnSukritResponseListner;
import com.sukritapp.smartshoppr.model.HomeTittle;
import com.sukritapp.smartshoppr.model.ResponseModel;
import com.sukritapp.smartshoppr.model.SeeAllCategoriesModel;
import com.sukritapp.smartshoppr.util.AppLog;
import com.sukritapp.smartshoppr.util.Constant;
import com.sukritapp.smartshoppr.util.SmartShopperServiceHelper;
import com.sukritapp.smartshoppr.util.SmartShopprApp;
import com.sukritapp.smartshoppr.util.SmartShopprUtils;
import com.sukritapp.smartshoppr.webservice.WebRetriveDataTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by abc on 1/8/2017.
 */

public class SeeAllCategories extends Fragment implements OnSukritResponseListner {
    private final String TAG = AppLog.getClassName();
    private ProgressBar mPrgBarCountry = null;
    private RecyclerView mReVwAllCategoriesList = null;
    private SeeAllCategoriesGridViewAdapter mSeeAllCategoriesGridViewAdapter;

    private ArrayList<HomeTittle> mCategoriesList;
    private ProgressDialog progressDialog =null;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        AppLog.enter(TAG, AppLog.getMethodName());
        View rootView = inflater.inflate(R.layout.lay_see_all_categories, container, false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("All Categories");

        initView(rootView);
        AppLog.exit(TAG, AppLog.getMethodName());
        return rootView;
    }

    private void initView(View rootView) {
        mPrgBarCountry = (ProgressBar) rootView.findViewById(R.id.progressBar_country);
        mReVwAllCategoriesList = (RecyclerView) rootView.findViewById((R.id.recyclerVw_See_All_Categories));
        mPrgBarCountry.setVisibility(View.VISIBLE);
        // GridLayoutManager   mGrlaySeeAllCategories = new GridLayoutManager(getActivity(), 3);
         GridLayoutManager   mGrlaySeeAllCategories = new GridLayoutManager(getActivity(), 3);
        mReVwAllCategoriesList.setHasFixedSize(true);

//        mReVwAllCategoriesList.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        mReVwAllCategoriesList.setLayoutManager(mGrlaySeeAllCategories);
//        RecyclerView.ItemDecoration itemDecoration = new
//                DividerItemDecoration(SmartShopprApp.getApplication(), DividerItemDecoration.VERTICAL_LIST);
//        mReVwAllCategoriesList.addItemDecoration(itemDecoration);
//        http://mgsmapi.semseosmo.com/smartshopper/AllCategory?country=france&language=english&mobile=9911254183
//        String URL_HOME_LIST=Constant.URL_BASE + Constant.URL_HOME_LIST +"?country="+ LocalDataBase.getInstance().getCountry()+"&language="+LocalDataBase.getInstance().getLangugae()+"&mobile="+LocalDataBase.getInstance().getUserMobile() ;
//        http://mgsmapi.semseosmo.com/smartshopper/AllCategory?country=france&language=english&mobile=9911254183
        String URL_ALL_CATEGORIES_LIST = Constant.URL_BASE + Constant.URL_ALL_CATEGORIES + "?country=" + LocalDataBase.getInstance().getCountry() + "&language=" + LocalDataBase.getInstance().getLangugae() + "&mobile=" + LocalDataBase.getInstance().getUserMobile();

        new WebRetriveDataTask().retriveData(getActivity(), this, Constant.listtype.ALLCATEGORIES, null, URL_ALL_CATEGORIES_LIST);

    }

    @Override
    public void onResponseReceived(String error,Constant.listtype tag, String response) {
        mPrgBarCountry.setVisibility(View.INVISIBLE);
        AppLog.info(TAG, "response---" + response);
//        String substring = response.substring(1);
        if(error==null) {
            mCategoriesList = SmartShopprUtils.getInstance().parseAllCategoryData(response);

            mSeeAllCategoriesGridViewAdapter = new SeeAllCategoriesGridViewAdapter(mCategoriesList, getActivity(), this);
            mReVwAllCategoriesList.setAdapter(mSeeAllCategoriesGridViewAdapter);
        }
        else {
            SmartShopprUtils.getInstance().showErrorDialog(getActivity(), error);
        }
    }
    public void showProgressBar(Context mContext) {
        //hide soft keyboard first
        progressDialog = ProgressDialog.show(mContext, "", "Please wait...", false);
        progressDialog.setCancelable(false);
    }

}
