package votive.com.appuaet10.Beans;


import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import votive.com.appuaet10.Utilities.Constant;

public class HomeContentResponseBean {

    @SerializedName(Constant.JSON_RESPONSE_STATUS_KEY)
    private String mStatus;

    @SerializedName(Constant.JSON_RESPONSE_MSG_KEY)
    private String mMsg;

    @SerializedName(Constant.JSON_FEATURE_CATEGORY_LIST_KEY)
    ArrayList<CategoryBean> mFeatureCategoryBeanArrayList;


    @SerializedName(Constant.JSON_FEATURES_LISTING_KEY)
    ArrayList<CategoryBean> mFeatureListingList;
    //ArrayList<FeatureListingBean> mFeatureListingList;

    @SerializedName(Constant.JSON_BANNER_LISTING_KEY)
    ArrayList<BannerBean> mBannerImagelist;


    @SerializedName(Constant.JSON_RESPONSE_ERROR_CODE)
    private String mErrorCode;

    @SerializedName(Constant.JSON_WATCH_VIDEO_LINK_KEY)
    private String mVideoStr;






    //====================getter=================

    public String getmStatus() {
        return mStatus;
    }

    public String getmMsg() {
        return mMsg;
    }

    public ArrayList<CategoryBean> getmFeatureCategoryBeanArrayList() {
        return mFeatureCategoryBeanArrayList;
    }

    public String getmErrorCode() {
        return mErrorCode;
    }

    public ArrayList<CategoryBean> getmFeatureListingList() {
        return mFeatureListingList;
    }

    public ArrayList<BannerBean> getmBannerImagelist() {
        return mBannerImagelist;
    }


    public String getVideoStr() {
        return mVideoStr;
    }
}
