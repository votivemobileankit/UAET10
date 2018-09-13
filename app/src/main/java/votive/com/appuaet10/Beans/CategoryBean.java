package votive.com.appuaet10.Beans;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import votive.com.appuaet10.Utilities.Constant;


public class CategoryBean implements Parcelable {

    @SerializedName(Constant.JSON_CATEGORY_ID_KEY)
    private int mCategoryId;

    @SerializedName(Constant.JSON_CATEGORY_DESCRIPTION_KEY)
    private String mCategoryDescription;

    @SerializedName(Constant.JSON_CATEGORY_NAME_KEY)
    private String mCategoryName;


    @SerializedName(Constant.JSON_CATEGORY_IMAGE_URL_KEY)
    private String mCategoryImageUrl;

    @SerializedName(Constant.JSON_CATEGORY_THUMB_IMAGE_URL_KEY)
    private String mCategoryThumbUrl;

    @SerializedName(Constant.JSON_CATEGORY_IMAGE_RECT_URL_KEY)
    private String mCategoryRectImageUrl;

    //================setter=======================

    protected CategoryBean(Parcel in) {
        mCategoryId = in.readInt();
        mCategoryDescription = in.readString();
        mCategoryName = in.readString();
        mCategoryImageUrl = in.readString();
        mCategoryThumbUrl = in.readString();
        mCategoryRectImageUrl = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mCategoryId);
        dest.writeString(mCategoryDescription);
        dest.writeString(mCategoryName);
        dest.writeString(mCategoryImageUrl);
        dest.writeString(mCategoryThumbUrl);
        dest.writeString(mCategoryRectImageUrl);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<CategoryBean> CREATOR = new Creator<CategoryBean>() {
        @Override
        public CategoryBean createFromParcel(Parcel in) {
            return new CategoryBean(in);
        }

        @Override
        public CategoryBean[] newArray(int size) {
            return new CategoryBean[size];
        }
    };

    public void setmCategoryId(int mCategoryId) {
        this.mCategoryId = mCategoryId;
    }

    public void setmCategoryName(String mCategoryName) {
        this.mCategoryName = mCategoryName;
    }

    public void setmCategoryImageUrl(String mCategoryImageUrl) {
        this.mCategoryImageUrl = mCategoryImageUrl;
    }

    public void setmCategoryDescription(String mCategoryDescription) {
        this.mCategoryDescription = mCategoryDescription;
    }

    //====================getter=================

    public int getmCategoryId() {
        return mCategoryId;
    }

    public String getmCategoryName() {
        return mCategoryName;
    }

    public String getmCategoryImageUrl() {
        return mCategoryImageUrl;
    }

    public String getmCategoryDescription() {
        return mCategoryDescription;
    }

    public String getmCategoryThumbUrl() {
        return mCategoryThumbUrl;
    }

    public void setmCategoryThumbUrl(String mCategoryThumbUrl) {
        this.mCategoryThumbUrl = mCategoryThumbUrl;
    }

    public String getmCategoryRectImageUrl() {
        return mCategoryRectImageUrl;
    }

    public void setmCategoryRectImageUrl(String mCategoryRectImageUrl) {
        this.mCategoryRectImageUrl = mCategoryRectImageUrl;
    }

}
