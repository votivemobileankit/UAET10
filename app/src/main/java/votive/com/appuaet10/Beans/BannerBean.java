package votive.com.appuaet10.Beans;


import com.google.gson.annotations.SerializedName;

import votive.com.appuaet10.Utilities.Constant;

public class BannerBean {

    @SerializedName(Constant.JSON_BANNER_URL_KEY)
    private String mURl;

    @SerializedName(Constant.JSON_BANNER_TYPE_KEY)
    private int mBannerType;

    @SerializedName(Constant.JSON_BANNER_ID_KEY)
    private int mId = -1;

    @SerializedName(Constant.JSON_BANNER_NAME_KEY)
    private String mItemName;


    @SerializedName(Constant.JSON_WEBLINK_KEY)
    private String mStrWebLink;


    public String getURl() {
        return mURl;
    }

    public int getBannerType() {
        return mBannerType;
    }

    public int getId() {
        return mId;
    }

    public String getItemName() {
        return mItemName;
    }

    public String getmStrWebLink() {
        return mStrWebLink;
    }

    public void setmStrWebLink(String mStrWebLink) {
        this.mStrWebLink = mStrWebLink;
    }
}
