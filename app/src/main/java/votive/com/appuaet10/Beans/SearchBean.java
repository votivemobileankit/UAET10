package votive.com.appuaet10.Beans;


import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import votive.com.appuaet10.Utilities.Constant;

public class SearchBean implements Parcelable {


    @SerializedName(Constant.JSON_SEARCH_LEVEL)
    private String mSearchLabel;

    @SerializedName(Constant.JSON_SEARCH_TYPE)
    private int mSearchType;

    @SerializedName("id")
    private int strSearchId;


    protected SearchBean(Parcel in) {
        mSearchLabel = in.readString();
        mSearchType = in.readInt();
        strSearchId = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mSearchLabel);
        dest.writeInt(mSearchType);
        dest.writeInt(strSearchId);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<SearchBean> CREATOR = new Creator<SearchBean>() {
        @Override
        public SearchBean createFromParcel(Parcel in) {
            return new SearchBean(in);
        }

        @Override
        public SearchBean[] newArray(int size) {
            return new SearchBean[size];
        }
    };

    public String getmSearchLabel() {
        return mSearchLabel;
    }

    public void setmSearchLabel(String mSearchLabel) {
        this.mSearchLabel = mSearchLabel;
    }

    public int getmSearchType() {
        return mSearchType;
    }

    public void setmSearchType(int mSearchType) {
        this.mSearchType = mSearchType;
    }

    public int getStrSearchId() {
        return strSearchId;
    }

    public void setStrSearchId(int strSearchId) {
        this.strSearchId = strSearchId;
    }
}
