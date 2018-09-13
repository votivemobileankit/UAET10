package votive.com.appuaet10.Beans;


import com.google.gson.annotations.SerializedName;

import votive.com.appuaet10.Utilities.Constant;

public class NotificationResponseBean {

    @SerializedName(Constant.JSON_RESPONSE_STATUS_KEY)
    private String mStatus;

    @SerializedName(Constant.JSON_RESPONSE_MSG_KEY)
    private String mMsg;


    //====================getter=================

    public String getmStatus() {
        return mStatus;
    }

    public String getmMsg() {
        return mMsg;
    }

}
