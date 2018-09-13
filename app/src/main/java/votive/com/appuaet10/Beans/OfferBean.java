package votive.com.appuaet10.Beans;


import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import votive.com.appuaet10.Utilities.Constant;

public class OfferBean implements Parcelable {

    @SerializedName(Constant.JSON_OFFER_ID)
    private String mStrOfferId;

    @SerializedName(Constant.JSON_OFFER_TITLE_NAME)
    private String mStrOfferTitleName;

    @SerializedName(Constant.JSON_OFFER_TYPE)
    private String mStrOfferType;

    @SerializedName(Constant.JSON_OFFER_DESC)
    private String mStrOfferDesc;

    @SerializedName(Constant.JSON_OFFER_IMAGE)
    private String mStrOfferImg;


    public String getmStrOfferId() {
        return mStrOfferId;
    }

    public void setmStrOfferId(String mStrOfferId) {
        this.mStrOfferId = mStrOfferId;
    }

    public static Creator<OfferBean> getCREATOR() {
        return CREATOR;
    }

    public String getmStrOfferTitleName() {
        return mStrOfferTitleName;

    }

    public void setmStrOfferTitleName(String mStrOfferTitleName) {
        this.mStrOfferTitleName = mStrOfferTitleName;
    }


    public String getmStrOfferType() {
        return mStrOfferType;
    }

    public void setmStrOfferType(String mStrOfferType) {
        this.mStrOfferType = mStrOfferType;
    }

    public String getmStrOfferDesc() {
        return mStrOfferDesc;
    }

    public void setmStrOfferDesc(String mStrOfferDesc) {
        this.mStrOfferDesc = mStrOfferDesc;
    }

    public String getmStrOfferImg() {
        return mStrOfferImg;
    }

    public void setmStrOfferImg(String mStrOfferImg) {
        this.mStrOfferImg = mStrOfferImg;
    }

    protected OfferBean(Parcel in) {
        mStrOfferId = in.readString();
        mStrOfferTitleName = in.readString();
        mStrOfferDesc = in.readString();
        mStrOfferImg = in.readString();
        mStrOfferType = in.readString();
    }

    public static final Creator<OfferBean> CREATOR = new Creator<OfferBean>() {
        @Override
        public OfferBean createFromParcel(Parcel in) {
            return new OfferBean(in);
        }

        @Override
        public OfferBean[] newArray(int size) {
            return new OfferBean[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mStrOfferId);
        dest.writeString(mStrOfferTitleName);
        dest.writeString(mStrOfferDesc);
        dest.writeString(mStrOfferImg);
        dest.writeString(mStrOfferType);
    }
}
