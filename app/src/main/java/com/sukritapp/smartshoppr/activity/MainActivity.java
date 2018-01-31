package com.sukritapp.smartshoppr.activity;

import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.sukritapp.smartshoppr.R;
import com.sukritapp.smartshoppr.database.LocalDataBase;
import com.sukritapp.smartshoppr.fragments.AboutUsFragment;
import com.sukritapp.smartshoppr.fragments.ContactUsFragment;
import com.sukritapp.smartshoppr.fragments.CountryFragment;
import com.sukritapp.smartshoppr.fragments.FAQFragment;
import com.sukritapp.smartshoppr.fragments.HomeFragment;
import com.sukritapp.smartshoppr.fragments.LanguageFragment;
import com.sukritapp.smartshoppr.fragments.SearchClassFragment;
import com.sukritapp.smartshoppr.fragments.SeeAllCategories;
import com.sukritapp.smartshoppr.fragments.ShowOnWebFragment;
import com.sukritapp.smartshoppr.listner.OnSukritResponseListner;
import com.sukritapp.smartshoppr.model.ResponseModel;
import com.sukritapp.smartshoppr.util.AppLog;
import com.sukritapp.smartshoppr.util.Constant;
import com.sukritapp.smartshoppr.util.SmartShopprUtils;
import com.sukritapp.smartshoppr.webservice.WebRetriveDataTask;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, OnSukritResponseListner {
    private final String TAG = AppLog.getClassName();
    private Toolbar toolbar;

    FragmentManager fragmentManager = getSupportFragmentManager();
    //    private GoogleApiClient client;
    private ProgressDialog progressDialog;
    NavigationView navigationView;
    private SearchView searchView = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppLog.enter(TAG, AppLog.getMethodName());
        super.onCreate(savedInstanceState);

        setContentView(R.layout.layout_main);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
//


        //  View headerView = navigationView.inflateHeaderView(R.layout.nav_header_main);
        //   TextView mTvHeaderTittle = (TextView) headerView.findViewById(R.id.txtVw_nav_header_titlle);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        applyFontForToolbarTitle(toolbar);
        setSupportActionBar(toolbar);
//        getUserMobile
        //   mTvHeaderTittle.setText(LocalDataBase.getInstance().getUserMobile());
        SmartShopprUtils.getInstance().setFragmentManager(fragmentManager);
//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
        final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        final ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        ////

        ///


//        new WebRetriveDataTask().retriveData(this, this, Constant.listtype.CHECKLANGUAGE, null, Constant.URL_BASE + "checkLanguage?lang=Hindi");
        String checkLanguage = Constant.URL_BASE + "checkLanguage?lang=" + LocalDataBase.getInstance().getLangugae();

        new WebRetriveDataTask().retriveData(this, this, Constant.listtype.CHECKLANGUAGE, null, checkLanguage);

        showProgressBar(this);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setItemIconTintList(null);
        navigationView.setNavigationItemSelectedListener(this);
        Menu menu = navigationView.getMenu();
        if (LocalDataBase.getInstance().getUserMobile().isEmpty()) {
            menu.findItem(R.id.logout).setVisible(false);
        }
        if (savedInstanceState == null) {
            selectItem(0);
        }
        getSupportFragmentManager().addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {
                if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
                    getSupportActionBar().setDisplayHomeAsUpEnabled(true); // show back button
                    toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            onBackPressed();
                        }
                    });
                } else {
                    //show hamburger
                    getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                    toggle.syncState();
                    toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            drawer.openDrawer(GravityCompat.START);
                        }
                    });
                }
            }
        });
    }

    private void selectItem(int i) {
        Fragment fragment = null;
        fragment = new HomeFragment();
        fragment.setRetainInstance(true);
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
        AppLog.enter(TAG, AppLog.getMethodName());
    }


    @Override
    public void onBackPressed() {
        AppLog.enter(TAG, AppLog.getMethodName());
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (getSupportFragmentManager().getFragments() instanceof ShowOnWebFragment && ShowOnWebFragment.canGoBack()) {
            AppLog.info(TAG, AppLog.getMethodName());

            ShowOnWebFragment.goBack();
            AppLog.info(TAG, AppLog.getMethodName());

        } else {
            FragmentManager fragmentManager = getSupportFragmentManager();
            if (fragmentManager.getBackStackEntryCount() == 0) {
                SmartShopprUtils.getInstance().showExitAlert(this);
            } else {
                super.onBackPressed();
            }
        }
        AppLog.exit(TAG, AppLog.getMethodName());

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        AppLog.enter(TAG, AppLog.getMethodName());
        menu.clear();
        MenuInflater inflater1 = this.getMenuInflater();
        inflater1.inflate(R.menu.smartshoppr_menu_main, menu);

        final MenuItem myActionMenuItem = menu.findItem(R.id.action_search);
        searchView = (SearchView) myActionMenuItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // Toast like print
                AppLog.info(TAG, "Search is : " + query);
                //UserFeedback.show( "SearchOnQueryTextSubmit: " + query);
                if (!searchView.isIconified()) {
                    searchView.setIconified(true);
                }
                myActionMenuItem.collapseActionView();
                new WebRetriveDataTask().retriveData(MainActivity.this, MainActivity.this, Constant.listtype.SEARCH, null, Constant.URL_SEARCH_BASE + "=" + query.toString() + "&country=" + LocalDataBase.getInstance().getCountry()
                        + "&language=" + LocalDataBase.getInstance().getLangugae() + "&mobile=" + LocalDataBase.getInstance().getUserMobile());

                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                // UserFeedback.show( "SearchOnQueryTextChanged: " + s);
                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_cleardata:
                clearAppCache();
                break;
            case R.id.action_notification:
                break;
            case R.id.action_rate_app:
                rateThisApp();
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    private void rateThisApp() {
        Uri uri = Uri.parse("market://details?id=" + getPackageName());
        Intent myAppLinkToMarket = new Intent(Intent.ACTION_VIEW, uri);
        try {
            startActivity(myAppLinkToMarket);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(this, " unable to find app on play store", Toast.LENGTH_LONG).show();
        }
    }

    private void clearAppCache() {
        AppLog.enter(TAG, AppLog.getMethodName());
        trimCache(this);
        AppLog.exit(TAG, AppLog.getMethodName());
    }

    public void trimCache(Context context) {
        try {
            File dir = context.getCacheDir();
            if (dir != null && dir.isDirectory()) {
                deleteDir(dir);
            }
        } catch (Exception e) {
            // TODO: handle exception
        }
    }

    public boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }

        // The directory is now empty so delete it
        return dir.delete();
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        AppLog.enter(TAG, AppLog.getMethodName());
        int id = item.getItemId();
        if (id == R.id.logout) {
            LocalDataBase.getInstance().clearData();
            startActivity(new Intent(this, LoginScreen.class));
            finish();
        } else {
            Fragment fragment = null;
            if (id == R.id.homefragment) {
                fragment = new HomeFragment();
            } else if (id == R.id.all_categories) {
                fragment = new SeeAllCategories();

            }
       /* else if (id == R.id.all_small_categories) {
            fragment = new ALLSmallListFragment();

        }*/
            else if (id == R.id.country) {
                fragment = new CountryFragment();

            } else if (id == R.id.share) {
                doShare();

            } else if (id == R.id.faqs) {
                fragment = new FAQFragment();

            } else if (id == R.id.about_us) {
                fragment = new AboutUsFragment();

            } else if (id == R.id.contact_us) {
                fragment = new ContactUsFragment();

            } else if (id == R.id.terms_and_conditions) {
                fragment = new ContactUsFragment();

            } else {
                fragment = new LanguageFragment();
            }


//
// else if (id == R.id.login) {
//
//        } else if (id == R.id.all_categories) {
//
//        } else if (id == R.id.all_small_lists) {
//
//        } else if (id == R.id.country) {
//
//        } else if (id == R.id.language) {
//
//        } else if (id == R.id.language) {
//
//        } else if (id == R.id.sync) {
//
//        } else if (id == R.id.share) {
//
//        } else if (id == R.id.contact_us) {
//
//        } else if (id == R.id.about_us) {
//
//        } else if (id == R.id.terms_and_conditions) {
//
//        }

            if (fragment != null) {
                fragment.setRetainInstance(true);
                fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
            }
        }
//        // update selected item and title, then close the drawer
//        mDrawerList.setItemChecked(position, true);
//        setTitle(mPlanetTitles[position]);
//        mDrawerLayout.closeDrawer(mDrawerList);
///
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        AppLog.exit(TAG, AppLog.getMethodName());
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
//        mTvHeaderTittle.setText(LocalDataBase.getInstance().getUserName());
//        mTvHeaderSubTittle.setText(LocalDataBase.getInstance().getUserEmail());
    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
//    public Action getIndexApiAction() {
//        Thing object = new Thing.Builder()
//                .setName("Main Page") // TODO: Define a title for the content shown.
//                // TODO: Make sure this auto-generated URL is correct.
//                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
//                .build();
//        return new Action.Builder(Action.TYPE_VIEW)
//                .setObject(object)
//                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
//                .build();
//    }
    public void doShare() {
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, "I found best web developer:");
        sharingIntent.putExtra(Intent.EXTRA_TEXT, "http://www.sukritinfotech.com");
        startActivity(Intent.createChooser(sharingIntent, "Share via :"));

    }

    public void applyFontForToolbarTitle(Toolbar a) {
        AppLog.enter(TAG, AppLog.getMethodName());
        for (int i = 0; i < a.getChildCount(); i++) {
            View view = a.getChildAt(i);
            if (view instanceof TextView) {
                AppLog.info(TAG, AppLog.getMethodName());
                TextView tv = (TextView) view;
                if (tv.getText().equals(a.getTitle())) {
                    tv.setGravity(Gravity.CENTER);
                    break;
                }
            }
            AppLog.exit(TAG, AppLog.getMethodName());
        }
    }

   /* @Override
    public void onResponseReceived(Constant.listtype tag, String response) {
        progressDialog.cancel();
        AppLog.info(TAG, response);
        String country = "";
        String language = "";
        try {
            if (response != null && response.length() >= 0) {
                JSONObject jsonObject = new JSONObject(response);
                JSONArray jsonArray = jsonObject.getJSONArray("country_language");
                if (jsonArray.length() > 0) {
                    JSONObject jsonObject1 = new JSONObject(jsonArray.get(0).toString());
                    country = jsonObject1.getString("country");
                    language = jsonObject1.getString("language");
                    Menu menu = navigationView.getMenu();
                    menu.findItem(R.id.country).setTitle("Country / " + country);
                    menu.findItem(R.id.language).setTitle("Language / " + language);

                }
            }
        } catch (Exception e) {
            AppLog.error(TAG, "error : " + e.toString());
        }

    }*/

    public void showProgressBar(Context mContext) {
        progressDialog = ProgressDialog.show(mContext, "", Constant.MSG_PROGRESS_DIALOG, false);
        progressDialog.setCancelable(false);
    }


    @Override
    public void onResponseReceived(String error, Constant.listtype tag, String response) {
        progressDialog.cancel();
        AppLog.info(TAG, response);
        String country = "";
        String language = "";
        if (error == null) {
            try {
                if (tag == Constant.listtype.SEARCH) {
                    AppLog.info(TAG, "inside SEARCH ");
                    Bundle bundle = new Bundle();
                    ArrayList<ResponseModel> listresponseModels = SmartShopprUtils.getInstance().getSearchList(response);
                    bundle.putParcelableArrayList("list", listresponseModels);
                    Fragment nextFrag = new SearchClassFragment();
                    nextFrag.setArguments(bundle);

                    nextFrag.setRetainInstance(true);
                    fragmentManager.beginTransaction().replace(R.id.content_frame, nextFrag).addToBackStack(null).commit();
//                  this.getFragmentManager().beginTransaction()
//                          .replace(R.id.content_frame, nextFrag, null)
//                          .addToBackStack(null)
//                          .commit();

                } else if (response != null && response.length() >= 0) {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("country_language");
                    if (jsonArray.length() > 0) {
                        JSONObject jsonObject1 = new JSONObject(jsonArray.get(0).toString());
                        country = jsonObject1.getString("country");
                        language = jsonObject1.getString("language");
                        Menu menu = navigationView.getMenu();
                        menu.findItem(R.id.country).setTitle("Country / " + country);
                        menu.findItem(R.id.language).setTitle("Language / " + language);

                    }
                }
            } catch (Exception e) {
                AppLog.error(TAG, "error : " + e.toString());
            }
        } else {
            SmartShopprUtils.getInstance().showErrorDialog(this, error);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        AppLog.info(TAG, "super...........");
    }
}
