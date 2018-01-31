package com.sukritapp.smartshoppr.util;

import com.sukritapp.smartshoppr.database.LocalDataBase;

/**
 * Created by abc on 4/14/2017.
 */

public class SmartShopperServiceHelper {

    public static String URL_ALL_CATEGORIES=Constant.URL_BASE + Constant.URL_ALL_CATEGORIES + Constant.URL_PARAMS_COUNTRY  ;
    public static String URL_HOME_LIST=Constant.URL_BASE + Constant.URL_HOME_LIST + Constant.URL_PARAMS_COUNTRY ;//+ LocalDataBase.getInstance().getCountry() + Constant.URL_PARAMS_lANGUAGE + LocalDataBase.getInstance().getLangugae();
    public static String URL_LANGUAGE_LIST=Constant.URL_BASE + Constant.URL_LANGUAGE + Constant.URL_PARAMS_COUNTRY ;
//    public static String URL_LANGUAGE_LIST="http://mgsmapi.semseosmo.com/smartshopper/languageList?country=in" ;

//public static String URL_LANGUAGE=
}
