package votive.com.appuaet10.Beans;


import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import votive.com.appuaet10.Utilities.Constant;

public class BusinessDetailedResponseBean {

    @SerializedName(Constant.JSON_RESPONSE_STATUS_KEY)
    private String mStatus;

    @SerializedName(Constant.JSON_RESPONSE_MSG_KEY)
    private String mMsg;

    @SerializedName(Constant.JSON_BUSINESS_DETAILED)
    ArrayList<CategoryItemBean> mBusinessDetailedList;


    //====================getter=================

    public String getmStatus() {
        return mStatus;
    }

    public String getmMsg() {
        return mMsg;
    }

    public ArrayList<CategoryItemBean> getmBusinessDetailedList() {
        return mBusinessDetailedList;
    }

}
