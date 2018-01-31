package com.sukritapp.smartshoppr.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.sukritapp.smartshoppr.R;
import com.sukritapp.smartshoppr.activity.BookCabsActivity;
import com.sukritapp.smartshoppr.adapter.HomeCategoryAdapter;
import com.sukritapp.smartshoppr.database.LocalDataBase;
import com.sukritapp.smartshoppr.listner.OnSukritResponseListner;
import com.sukritapp.smartshoppr.model.HomeTittle;
import com.sukritapp.smartshoppr.model.ResponseModel;
import com.sukritapp.smartshoppr.util.AppLog;
import com.sukritapp.smartshoppr.util.Constant;
import com.sukritapp.smartshoppr.util.SmartShopprUtils;
import com.sukritapp.smartshoppr.webservice.WebRetriveDataTask;

import java.util.ArrayList;

import static android.content.Context.INPUT_METHOD_SERVICE;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment implements ViewPager.OnPageChangeListener, View.OnClickListener, OnSukritResponseListner {
    private final String TAG = AppLog.getClassName();
    protected View view;
    private int dotsCount;
    private ImageView[] dots;
    private RecyclerView mRecyVwHomeItem;
    private ProgressBar mProgressbarHome;
    //    private AutoCompleteTextView mAutoCompleteSearch;
//    private ImageView imgVWSearch;
    private HomeCategoryAdapter mhHomeCategoryAdapter = null;
    private String mTagRecycleView;
    private Button mBtnAllCategory = null;
    private Button mBtnBookmark = null;
    private Button mBtnBookCab = null;
    private static int pageCount = 0;
    private FloatingActionButton mTxtVwSOS = null;

//    Timer timerObj;
//    TimerTask timerTaskObj;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        AppLog.enter(TAG, AppLog.getMethodName());
        if (container != null) {
            container.clearDisappearingChildren();
        }
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        hideSoftKeyboard();
        init(rootView);
        initHoriZontalScrollLayout(rootView);
        //autoScrollViewPager();
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(getString(R.string.app_name));
        AppLog.exit(TAG, AppLog.getMethodName());
        return rootView;

    }


    public void hideSoftKeyboard() {
        if (getActivity().getCurrentFocus() != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
        }
    }

    private void initHoriZontalScrollLayout(View rootView) {
        AppLog.enter(TAG, AppLog.getMethodName());

        mProgressbarHome = (ProgressBar) rootView.findViewById(R.id.progressBar_home);
        mRecyVwHomeItem = (RecyclerView) rootView.findViewById(R.id.recyVwhometittle);

//mgsmapi.semseosmo.com/smartshopper/Homelist?language=english&country=India&mobile=9911254183
//        http://mgsmapi.semseosmo.com/smartshopper/Homelist?country=India&language=english&mobile=9911254183
        String URL_HOME_LIST = Constant.URL_BASE + Constant.URL_HOME_LIST + "?country=" + LocalDataBase.getInstance().getCountry().toLowerCase() + "&language=" + LocalDataBase.getInstance().getLangugae() + "&mobile=" + LocalDataBase.getInstance().getUserMobile();
        new WebRetriveDataTask().retriveData(getActivity(), this, Constant.listtype.HOMELIST, null, URL_HOME_LIST);

        mRecyVwHomeItem.setLayoutManager(getLayOutManager());
        mProgressbarHome.setVisibility(View.VISIBLE);
        new WebRetriveDataTask().retriveData(getActivity(), this, Constant.listtype.BANNNER, null, Constant.URL_BASE + Constant.URL_BANNER_LIST + Constant.URL_PARAMS_COUNTRY + LocalDataBase.getInstance().getCountry());
        AppLog.exit(TAG, AppLog.getMethodName());

    }


    private void init(View rootView) {
//        mAutoCompleteSearch = (AutoCompleteTextView) rootView.findViewById(R.id.AutoCompleteViewSearch);
//        imgVWSearch = (ImageView) rootView.findViewById(R.id.btn_search);
        mBtnAllCategory = (Button) rootView.findViewById(R.id.btn_all_category);
        mBtnBookmark = (Button) rootView.findViewById(R.id.btn_bookmark);
        mTxtVwSOS = (FloatingActionButton) rootView.findViewById(R.id.txtVw_sos);
        mBtnBookCab = (Button) rootView.findViewById(R.id.btn_book_cab);
//        imgVWSearch.setOnClickListener(this);
        mBtnAllCategory.setOnClickListener(this);
        mBtnBookmark.setOnClickListener(this);
        mBtnBookCab.setOnClickListener(this);
        mTxtVwSOS.setOnClickListener(this);
        mBtnBookCab.setVisibility(View.GONE);


    }


    // event on viewpager scroll
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        pageCount = position;
    }

    @Override
    public void onPageSelected(int position) {
        pageCount = position;
        movetoNextImageOnViewPager(pageCount);

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    private LinearLayoutManager getLayOutManager() {
        return new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
    }

    @Override
    public void onClick(View view) {

//        if (view.getId() == R.id.btn_search) {
//            hideSoftKeyboard();
//            new WebRetriveDataTask().retriveData(getActivity(), this, Constant.listtype.SEARCH, null, Constant.URL_SEARCH_BASE + "=" + mAutoCompleteSearch.getText().toString() + "&country=" + LocalDataBase.getInstance().getCountry()
//                    + "&language=" + LocalDataBase.getInstance().getLangugae() + "&mobile=" + LocalDataBase.getInstance().getUserMobile());
//
//        }
        if (view.getId() == R.id.btn_all_category) {
            Fragment nextFrag = new SeeAllCategories();
            this.getFragmentManager().beginTransaction()
                    .replace(R.id.content_frame, nextFrag, null)
                    .addToBackStack(null)
                    .commit();
        } else if (view.getId() == R.id.btn_bookmark) {
            Fragment nextFrag = new ShowBookMark();
            this.getFragmentManager().beginTransaction()
                    .replace(R.id.content_frame, nextFrag, null)
                    .addToBackStack(null)
                    .commit();
        } else if (view.getId() == R.id.txtVw_sos) {
            Fragment nextFrag = new SosFragment();
            this.getFragmentManager().beginTransaction()
                    .replace(R.id.content_frame, nextFrag, null)
                    .addToBackStack(null)
                    .commit();
        } else if (view.getId() == R.id.btn_book_cab) {
//            Fragment nextFrag = new BookCabsFragment();
//            this.getFragmentManager().beginTransaction()
//                    .replace(R.id.content_frame, nextFrag, null)
//                    .addToBackStack(null)
//                    .commit();
//
            startActivity(new Intent(getActivity(), BookCabsActivity.class));
        }

    }

    ;

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResponseReceived(String error, Constant.listtype tag, String response) {
        AppLog.enter(TAG, AppLog.getMethodName());
        AppLog.info(TAG, "tag : " + tag);
        AppLog.info(TAG, "response : " + response);
        mProgressbarHome.setVisibility(View.INVISIBLE);
        if (error == null) {

            ResponseModel responseModel = new ResponseModel();
            responseModel.setLogo("R.drawable.banner4");
            responseModel.setWebURl("");
            ArrayList<ResponseModel> list;
            switch (tag) {
                case HOMELIST:
                    ArrayList<HomeTittle> tiltlelist = SmartShopprUtils.getInstance().parseHomedata(response);
                    mhHomeCategoryAdapter = new HomeCategoryAdapter(tiltlelist, this);
                    mRecyVwHomeItem.setAdapter(mhHomeCategoryAdapter);
                    break;
//
                case BANNNER:
//                    list = SmartShopprUtils.getInstance().parseJsonGetBannerlist(response, Constant.TAG_BANNER);
//                    if (list.size() == 0)
//                        list.add(responseModel);
//                    SmartShopprUtils.getInstance().setBannerList(list);
                    break;
                case SEARCH:
                    AppLog.info(TAG, "inside SEARCH ");
                    Bundle bundle = new Bundle();
                    ArrayList<ResponseModel> listresponseModels = SmartShopprUtils.getInstance().getSearchList(response);
                    bundle.putParcelableArrayList("list", listresponseModels);
                    Fragment nextFrag = new SearchClassFragment();
                    nextFrag.setArguments(bundle);
                    this.getFragmentManager().beginTransaction()
                            .replace(R.id.content_frame, nextFrag, null)
                            .addToBackStack(null)
                            .commit();

                    break;

            }
        } else {

            SmartShopprUtils.getInstance().showErrorDialog(getActivity(), error);
        }
        AppLog.exit(TAG, AppLog.getMethodName());

    }
//
//    private void autoScrollViewPager() {
//        timerObj = new Timer();
//        timerTaskObj = new TimerTask() {
//            public void run() {
//                AppLog.enter(TAG, AppLog.getMethodName());
//                AppLog.info(TAG, "pageCount" + pageCount + "dot count " + dotsCount);
//                movetoNextImageOnViewPager(pageCount);
//                pageCount++;
//                if (pageCount >= dotsCount)
//                    pageCount = 0;
//                AppLog.enter(TAG, "" + pageCount);
//                AppLog.enter(TAG, AppLog.getMethodName());
//            }
//
//
//        };
//        timerObj.schedule(timerTaskObj, 0, 2000);
//    }


    private void movetoNextImageOnViewPager(final int position) {
        AppLog.enter(TAG, AppLog.getMethodName());
        if (dotsCount != 0) {
            if (getActivity() == null)
                return;
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    try {
//                        mViewPagerTop.setCurrentItem(position);
                        for (int i = 0; i < dotsCount; i++) {
                            dots[i].setImageDrawable(getResources().getDrawable(R.drawable.non_selecteditem_dot));
                        }

                        dots[position].setImageDrawable(getResources().getDrawable(R.drawable.selected_item_dot));
                    } catch (Exception e) {
                    }
                }

            });

        }

    }

    @Override
    public void onDetach() {
        AppLog.enter(TAG, AppLog.getMethodName());
        super.onDetach();

        AppLog.exit(TAG, AppLog.getMethodName());
    }
}