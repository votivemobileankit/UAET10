package votive.com.appuaet10.Beans;


import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import votive.com.appuaet10.Utilities.Constant;

public class CategoryItemResponseBean {

    @SerializedName(Constant.JSON_RESPONSE_STATUS_KEY)
    private String mStatus;

    @SerializedName(Constant.JSON_RESPONSE_MSG_KEY)
    private String mMsg;

    @SerializedName(Constant.JSON_CATEGORY_ITEM_LIST_KEY)
    ArrayList<CategoryItemBean> mCategoryItemList;

    @SerializedName(Constant.JSON_RESPONSE_ERROR_CODE)
    private String mErrorCode;

    //====================getter=================

    public String getmStatus() {
        return mStatus;
    }

    public String getmMsg() {
        return mMsg;
    }

    public ArrayList<CategoryItemBean> getmCategoryItemList() {
        return mCategoryItemList;
    }

    public String getmErrorCode() {
        return mErrorCode;
    }
}
