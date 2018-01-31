package com.sukritapp.smartshoppr.model;

/**
 * Created by abc on 1/3/2017.
 */

public class ItemData {

    private  int imgId;
    private  String mTextMSG;

    public int getImgId() {
        return imgId;
    }

    public String getmTextMSG() {
        return mTextMSG;
    }

    public ItemData(int res, String s) {
        this.imgId=res;

        this.mTextMSG=s;
    }


}
