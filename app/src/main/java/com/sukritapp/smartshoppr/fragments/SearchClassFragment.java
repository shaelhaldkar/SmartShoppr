package com.sukritapp.smartshoppr.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.sukritapp.smartshoppr.R;
import com.sukritapp.smartshoppr.adapter.SeeAllItemListAdaptrer;
import com.sukritapp.smartshoppr.customview.DividerItemDecoration;
import com.sukritapp.smartshoppr.database.LocalDataBase;
import com.sukritapp.smartshoppr.model.ResponseModel;
import com.sukritapp.smartshoppr.util.AppLog;
import com.sukritapp.smartshoppr.util.Constant;
import com.sukritapp.smartshoppr.util.SmartShopprApp;
import com.sukritapp.smartshoppr.util.SmartShopprUtils;
import com.sukritapp.smartshoppr.webservice.WebRetriveDataTask;

import java.util.ArrayList;

import static android.R.attr.data;

/**
 * Created by abc on 5/23/2017.
 */

public class SearchClassFragment extends Fragment {

    private final String TAG = AppLog.getClassName();
    protected View view;

    View rootView;
    ImageView[] dots;
    ArrayList<ResponseModel> mdatalist = new ArrayList<>();



    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        AppLog.enter(TAG, AppLog.getMethodName());
        rootView = inflater.inflate(R.layout.lay_search_list, container, false);

        setHasOptionsMenu(true);


        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Search Result");
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(bundle.getString("tittle"));
            mdatalist = bundle.getParcelableArrayList("list");
        }
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        init(rootView);
        AppLog.exit(TAG, AppLog.getMethodName());
        return rootView;
    }

    private void init(View rootView) {
        AppLog.enter(TAG, AppLog.getMethodName());
        TextView Tv = (TextView) rootView.findViewById(R.id.txtVw_no_record_found);
        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.recyVw_serarchResult);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        RecyclerView.ItemDecoration itemDecoration = new
                DividerItemDecoration(SmartShopprApp.getApplication(), DividerItemDecoration.VERTICAL_LIST);
        recyclerView.addItemDecoration(itemDecoration);

        //setup view pager

//        http://mgsmapi.semseosmo.com/smartshopper/categoryBanners?category=Top Stores&country=India
       /* if (SmartShopprUtils.getInstance().getBannerList() != null && SmartShopprUtils.getInstance().getBannerList().size() > 0) {
            mAdapter = new SlidingImageAdapter(getActivity().getBaseContext(), SmartShopprUtils.getInstance().getBannerList());
            mViewPagerTop.setAdapter(mAdapter);
            setUiPageViewController();
        } else {
            mViewPagerTop.setVisibility(GONE);
        }*/
        if (mdatalist != null && !mdatalist.isEmpty()) {
            SeeAllItemListAdaptrer rcAdapter = new SeeAllItemListAdaptrer(getActivity(), mdatalist);
            recyclerView.setAdapter(rcAdapter);
        } else {
            recyclerView.setVisibility(View.GONE);
            Tv.setVisibility(View.VISIBLE);
        }
    }

        @Override
        public void onCreateOptionsMenu (Menu menu, MenuInflater inflater){
            AppLog.enter(TAG, AppLog.getMethodName());
            menu.clear();
           // inflater.inflate(R.menu.menu_main, menu);
           /* mMenuItemBookMark = menu.findItem(R.id.action_bookmark);
            if (isBookMarked) {
                mMenuItemBookMark.setIcon(R.drawable.bookmark);
            } else {
                mMenuItemBookMark.setIcon(R.drawable.ic_no_bookmark);
            }*/
            AppLog.exit(TAG, AppLog.getMethodName());

        }


}
