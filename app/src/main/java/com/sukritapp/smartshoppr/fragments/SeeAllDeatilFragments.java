package com.sukritapp.smartshoppr.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.sukritapp.smartshoppr.R;
import com.sukritapp.smartshoppr.adapter.SeeAllItemListAdaptrer;
import com.sukritapp.smartshoppr.adapter.SlidingImageAdapter;
import com.sukritapp.smartshoppr.customview.DividerItemDecoration;
import com.sukritapp.smartshoppr.database.LocalDataBase;
import com.sukritapp.smartshoppr.listner.OnSukritResponseListner;
import com.sukritapp.smartshoppr.model.ResponseModel;
import com.sukritapp.smartshoppr.util.AppLog;
import com.sukritapp.smartshoppr.util.Constant;
import com.sukritapp.smartshoppr.util.SmartShopprApp;
import com.sukritapp.smartshoppr.util.SmartShopprUtils;
import com.sukritapp.smartshoppr.webservice.WebRetriveDataTask;

import java.util.ArrayList;

import static android.view.View.GONE;

/**
 * Created by abc on 1/5/2017.
 */

public class SeeAllDeatilFragments extends Fragment implements ViewPager.OnPageChangeListener, OnSukritResponseListner {
    private final String TAG = AppLog.getClassName();
    protected View view;
    private ViewPager mViewPagerTop;

    private SlidingImageAdapter mAdapter;
    private LinearLayout pager_indicator;
    private int dotsCount;
    View rootView;
    ImageView[] dots;
    ArrayList<ResponseModel> mdatalist = new ArrayList<>();
    String titltName = null;
    private ProgressDialog progressDialog;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        AppLog.enter(TAG, AppLog.getMethodName());
        rootView = inflater.inflate(R.layout.lay_see_all, container, false);
        setHasOptionsMenu(true);


        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(SmartShopprUtils.getInstance().getFragmentName());
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            titltName = bundle.getString("tittle");
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
        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerVwSeeAll);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        RecyclerView.ItemDecoration itemDecoration = new
                DividerItemDecoration(SmartShopprApp.getApplication(), DividerItemDecoration.VERTICAL_LIST);
        recyclerView.addItemDecoration(itemDecoration);

        //setup view pager

        pager_indicator = (LinearLayout) rootView.findViewById(R.id.viewPagerCountDots);

        mViewPagerTop = (ViewPager) rootView.findViewById(R.id.viewpager);
//        http://mgsmapi.semseosmo.com/smartshopper/categoryBanners?category=Top Stores&country=India
        String url = Constant.URL_BASE + "categoryBanners?" + "category=" + titltName + "&country=" + LocalDataBase.getInstance().getCountry();
        AppLog.info("tag", "url : " + url);
        String tempUrl = url.replaceAll(" ", "%20");
        AppLog.info("tag", "tempUrl : " + url);
        new WebRetriveDataTask().retriveData(getActivity(), this, Constant.listtype.BANNNER, null, tempUrl);

       /* if (SmartShopprUtils.getInstance().getBannerList() != null && SmartShopprUtils.getInstance().getBannerList().size() > 0) {
            mAdapter = new SlidingImageAdapter(getActivity().getBaseContext(), SmartShopprUtils.getInstance().getBannerList());
            mViewPagerTop.setAdapter(mAdapter);
            setUiPageViewController();
        } else {
            mViewPagerTop.setVisibility(GONE);
        }*/

        SeeAllItemListAdaptrer rcAdapter = new SeeAllItemListAdaptrer(getActivity(), mdatalist);
        recyclerView.setAdapter(rcAdapter);
        showProgressBar(getActivity());
    }

    private void setUiPageViewController() {
        mViewPagerTop.addOnPageChangeListener(this);
        dotsCount = mAdapter.getCount();
        dots = new ImageView[dotsCount];

        for (int i = 0; i < dotsCount; i++) {
            dots[i] = new ImageView(getActivity().getApplicationContext());
            dots[i].setImageDrawable(getResources().getDrawable(R.drawable.non_selecteditem_dot));

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );

            params.setMargins(4, 0, 4, 0);

            pager_indicator.addView(dots[i], params);
        }

        dots[0].setImageDrawable(getResources().getDrawable(R.drawable.selected_item_dot));
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        AppLog.enter(TAG, AppLog.getMethodName());
        menu.clear();
        inflater.inflate(R.menu.menu_main, menu);

        AppLog.enter(TAG, AppLog.getMethodName());

        // inflater.inflate(R.menu.menu_main, menu);
    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        for (int i = 0; i < dotsCount; i++) {
            dots[i].setImageDrawable(getResources().getDrawable(R.drawable.non_selecteditem_dot));
        }

        dots[position].setImageDrawable(getResources().getDrawable(R.drawable.selected_item_dot));

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onResponseReceived(String error,Constant.listtype tag, String response) {
        AppLog.info("response : ", response);
        progressDialog.dismiss();
        if(error==null) {
            ResponseModel responseModel = new ResponseModel();
            responseModel.setLogo("R.drawable.banner4");
            responseModel.setWebURl("");
            ArrayList<ResponseModel> list;
            list = SmartShopprUtils.getInstance().parseJsonGetCategoryBannerlist(response, titltName);
            if (list.size() == 0)
                list.add(responseModel);
            mAdapter = new SlidingImageAdapter(getActivity().getBaseContext(), list);
            mViewPagerTop.setAdapter(mAdapter);
            setUiPageViewController();
        }  else {
            SmartShopprUtils.getInstance().showErrorDialog(getActivity(), error);
        }
    }

    public void showProgressBar(Context mContext) {
        progressDialog = ProgressDialog.show(mContext, "", Constant.MSG_PROGRESS_DIALOG, false);
        progressDialog.setCancelable(false);
    }

}
