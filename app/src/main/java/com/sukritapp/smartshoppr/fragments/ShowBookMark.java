package com.sukritapp.smartshoppr.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.sukritapp.smartshoppr.R;
import com.sukritapp.smartshoppr.adapter.SeeAllItemListAdaptrer;
import com.sukritapp.smartshoppr.customview.DividerItemDecoration;
import com.sukritapp.smartshoppr.database.LocalDataBase;
import com.sukritapp.smartshoppr.listner.OnSukritResponseListner;
import com.sukritapp.smartshoppr.model.HomeTittle;
import com.sukritapp.smartshoppr.model.ResponseModel;
import com.sukritapp.smartshoppr.util.AppLog;
import com.sukritapp.smartshoppr.util.Constant;
import com.sukritapp.smartshoppr.util.SmartShopprApp;
import com.sukritapp.smartshoppr.util.SmartShopprUtils;
import com.sukritapp.smartshoppr.webservice.WebRetriveDataTask;

import java.util.ArrayList;

/**
 * Created by abc on 4/20/2017.
 */

public class ShowBookMark extends Fragment implements OnSukritResponseListner {
    private final String TAG = AppLog.getClassName();
    private ProgressDialog progressDialog;
    private RecyclerView mReCyVwBookMark= null;
    private ArrayList<HomeTittle> mdatalist;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.lay_bookmark, container, false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("All Bookmark");
        mReCyVwBookMark=(RecyclerView) rootView.findViewById(R.id.recyclerVwbookmark);
//        http://mgsmapi.semseosmo.com/smartshopper/AllBookmark?mobile=9911254183
        mReCyVwBookMark.setHasFixedSize(true);
        mReCyVwBookMark.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        RecyclerView.ItemDecoration itemDecoration = new
                DividerItemDecoration(SmartShopprApp.getApplication(), DividerItemDecoration.VERTICAL_LIST);
        mReCyVwBookMark.addItemDecoration(itemDecoration);

        new WebRetriveDataTask().retriveData(getActivity(), this, Constant.listtype.BOOKMARK, null,Constant.URL_ALL_BOOKMARK+ LocalDataBase.getInstance().getUserMobile());
        showProgressBar(getActivity());

        AppLog.exit(TAG, AppLog.getMethodName());
        return rootView;
    }

    public void showProgressBar(Context mContext) {
        progressDialog = ProgressDialog.show(mContext, "", Constant.MSG_PROGRESS_DIALOG, false);
        progressDialog.setCancelable(false);
    }

    @Override
    public void onResponseReceived(String error,Constant.listtype tag, String response) {
        AppLog.enter(TAG, AppLog.getMethodName());
        progressDialog.cancel();
        AppLog.info(TAG, "tag : " + tag);
        AppLog.info(TAG, "tag : " + response);
        if(error== null) {
            String msg = "";
            ArrayList<ResponseModel> listresponseModels = SmartShopprUtils.getInstance().parseBookmarkdata(response);
            if(listresponseModels!=null && listresponseModels.size()>0) {
                SeeAllItemListAdaptrer rcAdapter = new SeeAllItemListAdaptrer(getActivity(), listresponseModels);
                mReCyVwBookMark.setAdapter(rcAdapter);
            }else{

                SmartShopprUtils.getInstance().showErrorDialog(getActivity(), "No Bookmark Found");
            }
        }  else {
            SmartShopprUtils.getInstance().showErrorDialog(getActivity(), error);
        }
    }

}
