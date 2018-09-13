package votive.com.appuaet10.Beans;


import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import votive.com.appuaet10.Utilities.Constant;

public class CategoryItemBean implements Parcelable {

    @SerializedName(Constant.JSON_CATEGORY_ITEM_NAME)
    private String mStrCategoryItemName;

    @SerializedName(Constant.JSON_CATEGORY_THUMB_IMAGE)
    private String mStrCategoryItemThumbUrl;

    @SerializedName(Constant.JSON_CATEGORY_FULL_IMAGE)
    private String mStrCategoryItemFullImgUrl;

    @SerializedName(Constant.JSON_CATEGORY_ITEMM_DESC)
    private String mStrCategoryItemDesc;

    @SerializedName(Constant.JSON_CATEGORY_ITEM_CONTACT_NO)
    private String mStrPhoneNo;

    @SerializedName(Constant.JSON_CATEGORY_ITEM_EMAIL)
    private String mStrSendEmail;

    @SerializedName(Constant.JSON_CATEGORY_ITEM_COMIN_SOON_STATE)
    private int mBusinessIndex;

    @SerializedName(Constant.JSON_CATEGORY_ITEM_WEB_LINK)
    private String mStrWebLinks;

    @SerializedName(Constant.JSON_CATEGORY_FB_LINK)
    private String mStrFblink;

    @SerializedName(Constant.JSON_CATEGORY_INSTA_LINK)

    private String mStrInstaLink;

    @SerializedName(Constant.JSON_CATEGORY_TWITTER_LINK)
    private String mStrTwitterLink;

    @SerializedName(Constant.JSON_CATEGORY_LINKDIN_LINK)
    private String mStrLinkdinLink;

    @SerializedName(Constant.JSON_CATEGORY_ITEM_WEB_NAME)
    private String mStrWebName;

    @SerializedName(Constant.JSON_CATEGORY_VIEWS)
    private String mStrViews;

    @SerializedName(Constant.JSON_CATEGORY_LIKE_COUNT)
    private int mLikesCount;

    @SerializedName(Constant.JSON_CATEGORY_LIST_ID)
    private int mCategoryListingId;

    public CategoryItemBean() {

    }


    protected CategoryItemBean(Parcel in) {
        mStrCategoryItemName = in.readString();
        mStrCategoryItemThumbUrl = in.readString();
        mStrCategoryItemFullImgUrl = in.readString();
        mStrCategoryItemDesc = in.readString();
        mStrPhoneNo = in.readString();
        mStrSendEmail = in.readString();
        mBusinessIndex = in.readInt();
        mStrWebLinks = in.readString();
        mStrFblink = in.readString();
        mStrInstaLink = in.readString();
        mStrTwitterLink = in.readString();
        mStrLinkdinLink = in.readString();
        mStrWebName = in.readString();
        mStrViews = in.readString();
        mLikesCount = in.readInt();
        mCategoryListingId = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mStrCategoryItemName);
        dest.writeString(mStrCategoryItemThumbUrl);
        dest.writeString(mStrCategoryItemFullImgUrl);
        dest.writeString(mStrCategoryItemDesc);
        dest.writeString(mStrPhoneNo);
        dest.writeString(mStrSendEmail);
        dest.writeInt(mBusinessIndex);
        dest.writeString(mStrWebLinks);
        dest.writeString(mStrFblink);
        dest.writeString(mStrInstaLink);
        dest.writeString(mStrTwitterLink);
        dest.writeString(mStrLinkdinLink);
        dest.writeString(mStrWebName);
        dest.writeString(mStrViews);
        dest.writeInt(mLikesCount);
        dest.writeInt(mCategoryListingId);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<CategoryItemBean> CREATOR = new Creator<CategoryItemBean>() {
        @Override
        public CategoryItemBean createFromParcel(Parcel in) {
            return new CategoryItemBean(in);
        }

        @Override
        public CategoryItemBean[] newArray(int size) {
            return new CategoryItemBean[size];
        }
    };


    public String getCategoryItemName() {
        return mStrCategoryItemName;
    }

    public String getPhoneNo() {
        return mStrPhoneNo;
    }


    public String getEmailStr() {
        return mStrSendEmail;
    }

    public String getWebLinkStr() {

        String str = mStrWebLinks;
        if (str != null && !str.isEmpty()) {
            if (!str.startsWith("http://") && !str.startsWith("https://")) {
                str = "http://" + str;
            } else {
                str = mStrWebLinks;
            }
        }
        return str;
    }


    public String getCategoryItemFullImgUrl() {
        return mStrCategoryItemFullImgUrl;
    }

    public String getCategoryItemDesc() {
        return mStrCategoryItemDesc;
    }

    public String getFbLinkStr() {

        String str = mStrFblink;
        if (str != null && !str.isEmpty()) {
            if (!str.startsWith("http://") && !str.startsWith("https://")) {
                str = "http://" + str;
            } else {
                str = mStrFblink;
            }
        }
        return str;

    }

    public String getInstaLinkStr() {

        String str = mStrInstaLink;
        if (str != null && !str.isEmpty()) {
            if (!str.startsWith("http://") && !str.startsWith("https://")) {
                str = "http://" + str;
            } else {
                str = mStrInstaLink;
            }
        }
        return str;
    }
    public String getTwitterLinkStr() {

        String str = mStrTwitterLink;
        if (str != null && !str.isEmpty()) {
            if (!str.startsWith("http://") && !str.startsWith("https://")) {
                str = "http://" + str;
            } else {
                str = mStrTwitterLink;
            }
        }
        return str;
    }

    public int getBusinessIndex() {
        return mBusinessIndex;
    }


    public String getWebNameStr() {

        return mStrWebName;
    }

    public String getLinkdinLinkStr() {

        String str = mStrLinkdinLink;
        if (str != null && !str.isEmpty()) {
            if (!str.startsWith("http://") && !str.startsWith("https://")) {
                str = "http://" + str;
            } else {
                str = mStrLinkdinLink;
            }
        }
        return str;
    }

    public String getStrViews() {
        return mStrViews;
    }

    public int getLikesCount() {
        return mLikesCount;
    }

    public int getCategoryListingId() {
        return mCategoryListingId;
    }
}
