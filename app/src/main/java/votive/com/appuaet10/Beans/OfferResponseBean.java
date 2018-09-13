package votive.com.appuaet10.Beans;


import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import votive.com.appuaet10.Utilities.Constant;

public class OfferResponseBean {

    @SerializedName(Constant.JSON_RESPONSE_STATUS_KEY)
    private String mStatus;

    @SerializedName(Constant.JSON_RESPONSE_MSG_KEY)
    private String mMsg;

    @SerializedName(Constant.JSON_OFFER_LIST_INFO_KEY)
    ArrayList<OfferBean> mOfferList;

    //====================getter=================

    public String getmStatus() {
        return mStatus;
    }

    public String getmMsg() {
        return mMsg;
    }

    public ArrayList<OfferBean> getmOfferList() {
        return mOfferList;
    }


}
