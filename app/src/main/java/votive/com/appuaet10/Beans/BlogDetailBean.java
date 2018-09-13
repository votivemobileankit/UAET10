package votive.com.appuaet10.Beans;

import com.google.gson.annotations.SerializedName;

import votive.com.appuaet10.Utilities.Constant;

public class BlogDetailBean {


    @SerializedName(Constant.JSON_RESPONSE_STATUS_KEY)
    private String mStatus;

    @SerializedName(Constant.JSON_RESPONSE_MSG_KEY)
    private String mMsg;


    @SerializedName(Constant.JSON_BLOG_DETAILS_DESC_KEY)
    private String mDesc;


    public BlogDetailBean(String aURL, String aDesc) {
        this.mDesc = aDesc;
    }

    public String getDesc() {
        return mDesc;
    }

    public String getStatus() {
        return mStatus;
    }

    public String getMsg() {
        return mMsg;
    }
}
