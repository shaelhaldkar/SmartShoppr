package com.sukritapp.smartshoppr.fragments;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.sukritapp.smartshoppr.R;
import com.sukritapp.smartshoppr.activity.LoginScreen;
import com.sukritapp.smartshoppr.adapter.TitleNavigationAdapter;
import com.sukritapp.smartshoppr.database.LocalDataBase;
import com.sukritapp.smartshoppr.listner.OnSukritResponseListner;
import com.sukritapp.smartshoppr.model.ResponseModel;
import com.sukritapp.smartshoppr.util.AppLog;
import com.sukritapp.smartshoppr.util.Constant;
import com.sukritapp.smartshoppr.util.SmartShopprUtils;
import com.sukritapp.smartshoppr.webservice.WebRetriveDataTask;

import java.util.ArrayList;

/**
 * Created by eagers on 11/01/17.
 */

public class ShowOnWebFragment extends Fragment implements ActionBar.OnNavigationListener, OnSukritResponseListner {

    private final String TAG = AppLog.getClassName();
    public static WebView mWebView = null;
    private ProgressBar progressBar;
    private ArrayList<String> navSpinnerList;
    private TitleNavigationAdapter adapter;
    private ActionBar actionBar;
    private boolean isBookMarked;
    // private  Menu mMenu =null;
    private MenuItem mMenuItemBookMark = null;
    ResponseModel data = null;
    private ProgressDialog progressDialog;
    int count = 0;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        AppLog.enter(TAG, AppLog.getMethodName());
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(getString(R.string.app_name));
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        AppLog.exit(TAG, AppLog.getMethodName());
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        AppLog.enter(TAG, AppLog.getMethodName());
        // getActivity().getMenuInflater().inflate(R.menu.menu_main, menu);
        menu.clear();
        MenuInflater inflater1 = getActivity().getMenuInflater();
        inflater1.inflate(R.menu.menu_showonweb, menu);
        mMenuItemBookMark = menu.findItem(R.id.action_bookmark);
        if (isBookMarked) {
            mMenuItemBookMark.setIcon(R.drawable.bookmark);
        } else {
            mMenuItemBookMark.setIcon(R.drawable.ic_no_bookmark);
        }
        AppLog.exit(TAG, AppLog.getMethodName());
        super.onCreateOptionsMenu(menu, inflater1);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        AppLog.enter(TAG, AppLog.getMethodName());
        View rootView = inflater.inflate(R.layout.lay_web_view, container, false);
//        init(rootView);

        data = getArguments().getParcelable(Constant.BUNDLE_KEY);
        String URL = "http://" + data.getWebURl();
        isBookMarked = data.getIsBookmarked();
        navSpinnerList = data.getCouponList();
        mWebView = (WebView) rootView.findViewById(R.id.webVw_show_url);
        progressBar = (ProgressBar) rootView.findViewById(R.id.progressBar_web);
        mWebView.setWebViewClient(new MyWebView());
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        mWebView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        mWebView.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);

//        mWebView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            AppLog.enter(TAG, " >= Build.VERSION_CODES.KITKAT");
            mWebView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        } else {
            AppLog.enter(TAG, " <= Build.VERSION_CODES.KITKAT");
            mWebView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }

       /* if (Build.VERSION.SDK_INT >= 11){
            mWebView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }*/
        mWebView.loadUrl(URL);


        AppLog.exit(TAG, AppLog.getMethodName());
        return rootView;
    }

    public void refreshWebView() {
        if (mWebView != null)
            mWebView.reload();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_refresh:
                refreshWebView();
                break;
            case R.id.action_bookmark:
                addToBookmark();
                break;
           /* case R.id.action_coupon:
                View vItem = getActivity().findViewById(R.id.action_coupon);
                PopupMenu popMenu = new PopupMenu(getActivity(), vItem);
                for (int i = 0; i < navSpinnerList.size(); i++) {
                    popMenu.getMenu().add(0, i, i, navSpinnerList.get(i));
                }

                popMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        return true;
                    }
                });
                popMenu.show();

                // showpopUpMenu();
                //showCoupon();
                break;*/
            case R.id.action_info:
                showInfoOfProduct();
                break;

            default:
                break;
        }

        return true;
    }

    public static boolean canGoBack() {
        return mWebView.canGoBack();
    }

    public static void goBack() {
        mWebView.goBack();
    }

    private void showInfoOfProduct() {
        SmartShopprUtils.getInstance().showInfoDialog(getActivity(), "information");
    }

    private void showCoupon() {
        final AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
        alertDialog.setCancelable(true);
        alertDialog.setMessage("Long press on to copy this coupon..");
        ImageView imgview = new ImageView(getActivity());
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        imgview.setAdjustViewBounds(true);
        imgview.setLayoutParams(layoutParams);
        imgview.setImageResource(R.drawable.coupon);
        alertDialog.setView(imgview);
        alertDialog.setCancelable(true);
        alertDialog.setButton(Dialog.BUTTON_POSITIVE, "Copy", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        alertDialog.setButton(Dialog.BUTTON_NEUTRAL, "Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        imgview.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                ClipboardManager clipboard = (ClipboardManager)
                        getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("Coupon Copied", "ZZZZ20");
                SmartShopprUtils.getInstance().showToastMSG("Coupon Copied : ZZZZ20");
                clipboard.setPrimaryClip(clip);
                alertDialog.dismiss();
                return true;
            }
        });
        alertDialog.show();
    }

    private void addToBookmark() {
        if (LocalDataBase.getInstance().getUserMobile().isEmpty()) {
            showAlertDialog(getActivity(), "Please Login with registered mobile number to add bookmark", "Alert !!");
            return;
        }
        if (isBookMarked) {
            isBookMarked = false;
            mMenuItemBookMark.setIcon(R.drawable.ic_no_bookmark);
            showProgressBar(getActivity());

        } else {
            isBookMarked = true;
            mMenuItemBookMark.setIcon(R.drawable.bookmark);
            showProgressBar(getActivity());
        }
//        http://www.mgsmapi.semseosmo.com/smartshopper/activeBookmark?store_name=shopclues&country=India&language=English&mobile=8802423589
        new WebRetriveDataTask().retriveData(getActivity(), this, Constant.listtype.ADD_DELETE_BOOKMARK, null, Constant.URL_ADD_BOOKMARK + data.getDiscription() + "&mobile=" + LocalDataBase.getInstance().getUserMobile() + "&country=" + LocalDataBase.getInstance().getCountry()
                + "&language=" + LocalDataBase.getInstance().getLangugae());
    }

    @Override
    public boolean onNavigationItemSelected(int itemPosition, long itemId) {
        return false;
    }

    @Override
    public void onResponseReceived(String error, Constant.listtype tag, String response) {
        AppLog.info(TAG, "response  : " + response);
        progressDialog.cancel();
        if (error == null) {

        } else {
            SmartShopprUtils.getInstance().showErrorDialog(getActivity(), error);
        }


    }


    private class MyWebView extends WebViewClient {

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
        }


        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            // TODO Auto-generated method stub
            AppLog.info(TAG, "shouldOverrideUrlLoading  : " + ++count);
            view.loadUrl(url);
            return false;

        }

        @Override
        public void onPageFinished(WebView view, String url) {
            // TODO Auto-generated method stub
            super.onPageFinished(view, url);

        }
    }

    public void showProgressBar(Context mContext) {
        progressDialog = ProgressDialog.show(mContext, "", Constant.MSG_PROGRESS_DIALOG, false);
        progressDialog.setCancelable(false);
    }

    public void showAlertDialog(Context mContext, String message, String title) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder
                .setTitle(title)
                .setMessage(message)
                .setCancelable(false)
                .setIcon(android.R.drawable.ic_dialog_info)
                .setCancelable(false)
                .setPositiveButton("Login", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        LocalDataBase.getInstance().setSignUp(false);
                        startActivity(new Intent(getActivity(), LoginScreen.class));
                        getActivity().finish();
                    }
                }).setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });


        AlertDialog alert = builder.create();
        alert.show();
    }
}