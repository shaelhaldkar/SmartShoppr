package com.sukritapp.smartshoppr.util;

/**
 * Created by abc on 12/25/2016.
 */

public interface Constant {
    public static final String KEY_TITTLE_TAG = "tittle";
    String KEY_COUNTRY = "country";
    String KEY_IS_USER_LOGIN = "isuserLogin";
    String KEY_NAME = "name";
    String KEY_EMAIL = "email";
    String KEY_PHONE = "phone";
    String KEY_COUNTRY_IMAGE = "country_icon";
    String KEY_ISSKIPPED_SINGUP = "isSkippedSignup";
    String KEY_LANGUAGE = "language";

    String BUNDLE_KEY = "bundledata";

    //http://www.mgsmapi.semseosmo.com/smartshopper/HomeList?country=India&language=french
    String URL_BASE = "http://mgsmapi.semseosmo.com/smartshopper/";
    String URL_BANNER_LIST = "bannerListing";
    String URL_HOME_LIST = "HomeList";


    String URL_COUNTRY = "countryList";
    String URL_FORGET_PASSWORD = "forgetLoginPassword";
    String URL_ALL = "AllCategoryList";
    String URL_SIGNUP = "registerUsers";
    String URL_LOG_IN = "userLogin";
    String URL_ALL_CATEGORIES = "AllCategory";
    String URL_PARAMS_lANGUAGE = "&language=";
    String URL_PARAMS_COUNTRY = "?country=";
    String URL_LANGUAGE = "languageList";
    String URL_VALIDATE_OTP = "validateOtp";
    String URL_VALIDATE_FORGET_OTP = "validateForgototp";
    String URL_SEARCH_BASE = URL_BASE + "searchList?character";
    String URL_UPDATE_PASSWORD = URL_BASE + "updatePassword?";

    String URL_ALL_BOOKMARK = URL_BASE + "AllBookmark?mobile=";
    String URL_ADD_BOOKMARK = URL_BASE + "activeBookmark?store_name=";
    //    String OLA_LOGIN_URL = "https://sandbox-t1.olacabs.com/oauth2/authorize?response_type=token&client_id=ZWRhNWZlOTUtNjBlNC00NWU1LWI2YzYtOTllYzA0YjYzZDE2&redirect_uri=http://kotlinondroid.com/ola/tokens&scope=profile%20booking&state=state123";
    String OLA_LOGIN_URL = "https://sandbox-t1.olacabs.com/oauth2/authorize?response_type=token&client_id=ZWRhNWZlOTUtNjBlNC00NWU1LWI2YzYtOTllYzA0YjYzZDE2&redirect_uri=http://localhost/ola/tokens&scope=profile%20booking&state=state123";


    String URL_FAQ = "faqList";


    String TAG_BANNER = "banner_list";
    String TAG_TOPSTORE = "top_store";
    String TAG_LOGO = "logo";
    String TAG_WEB_URL = "web_url";
    String TAG_NAME = "name";
    String TAG_COUNTRY_LIST = "country_list";
    String TAG_COUNTRY_NAME = "country_name";
    String TAG_LANGUAGE = "language_name";
    String TAG_COUNTRY_ICON_URL = "logo";
    String TAG_COUNTRY_DEFAULT_LANGUAGE = "country_default_language";

    //uber ride
    String TAG_UBER_RIDE_ID = "rideID";

    String ERROR_MEG_INTERNET = "Something went wrong !!";
    int REQUEST_UNSUCESSFULL = 0;
    int REQUEST_SUCESSFULL = 1;


    String DEFAULT_USER_NAME = "smartshopper";
    String DEFAULT_USER_EMAIL = "Contact@smartshopper.com";
    String DEFAULT_USER_MOBILE = "";
    String DEFAULT_LANGUAGE = "english";
    int SOCKET_TIMEOUT_MS = 6000;//for webservice


    String RESPONSE_MSG_FORGET_PASSWORD = "";
    String RESPONSE_MSG_REGISTRATION_SUCESSFULL = "Success.";
    String RESPONSE_MSG_SUCCESS = "Success.";
    String MSG_PROGRESS_DIALOG = "Please wait..";
    String RESPONSE_MSG_OTP = "Your registration OTP";

    String URL_PLACES_API_BASE = "https://maps.googleapis.com/maps/api/place";
    String URL_PLACES_GEO_CODE_BASE = "http://maps.google.com/maps/api/geocode/json?address=";
    String TYPE_AUTOCOMPLETE = "/autocomplete";
    String OUT_JSON = "/json";
    String API_KEY = "AIzaSyC7oNB95O3kSluYsFxW76NsNOw_9D0-SJU";

     String UBER_CLIENT_ID = "gPgbAdXNcnS4xw2McaXyIEUd7zeury7d";
     String UBER_TOKEN = "kGFL7o8RcH73c3YFtbuFJk_fAHUq0JmqKXR491ds";
     String UBER_REDIRECT_URI = "sociallogin://oauthresponse";
    String UBER_test_access_token = "KA.eyJ2ZXJzaW9uIjoyLCJpZCI6IndjUlFaY2kvUnl5bGphU0tNbk5wbkE9PSIsImV4cGlyZXNfYXQiOjE1MDk3OTA1NDIsInBpcGVsaW5lX2tleV9pZCI6Ik1RPT0iLCJwaXBlbGluZV9pZCI6MX0.nl3CFpzD8UUiL9cxaxhqEp48VKhw7wdOHxvdt-y_HdE";
String OLA_BASE_URL="https://olawebcdn.com/assets/ola-universal-link.html?";
String OLA_PRICE_BASE_URL="http://sandbox-t.olacabs.com/v1/products?";
// manage single Activity for book cab option

    String TAG_ITEM_TYPE_PRICE_COMPARISION="Comapre Price";
    String TAG_ITEM_TYPE_UBER="Uber";
    String TAG_ITEM_TYPE_OLA ="Ola";
    String TAG_OLA_EST_PRICE ="Ola_Price";




//String OLA_BASE_URL="https://book.olacabs.com/?";

//    ola ride request sample
//http://book.olacabs.com/?url=lat=12.935&lng=77.614&category=compact&utm_source=12343&landing_page=bk&bk_act=rn&drop_lat=12.979&drop_lng=77.590&dsw=yes
// https://olawebcdn.com/assets/ola-universal-link.html?lat28.4520405&lng77.0561303&category=compact&utm_source=12343&landing_page=bk&bk_act=rn&drop_lat=28.4592693&drop_lng77.0724192&dsw=yes
    public enum listtype {
        SIGNUP, SIGNIN, GETPASSWORD, VALIDATEOTP, GETOTP, SENDOTP, ALL, ALLCATEGORIES, BANNNER, COUNTRY, LANGUAGE, HOMELIST, ABOUTUS, FAQ, CONTACTUS, CHECKLANGUAGE, SEARCH, BOOKMARK, ADD_DELETE_BOOKMARK, CHANGEPASSWORD
    }

}
