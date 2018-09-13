package votive.com.appuaet10.Beans;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import votive.com.appuaet10.Utilities.Constant;


public class BlogBean implements Parcelable {

    @SerializedName(Constant.JSON_BLOG_POST_ID)
    private int mBlogId;

    @SerializedName(Constant.JSON_BLOG_TITLE_NAME)
    private String mBlogName;

    @SerializedName(Constant.JSON_BLOG_IMAGE)
    private String mBlogImageUrl;

    @SerializedName(Constant.JSON_BLOG_URL)
    private String mLink;


    public BlogBean() {
    }



    //================setter=======================

    protected BlogBean(Parcel in) {
        mBlogId = in.readInt();
        mBlogName = in.readString();
        mBlogImageUrl = in.readString();
        mLink = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mBlogId);
        dest.writeString(mBlogName);
        dest.writeString(mBlogImageUrl);
        dest.writeString(mLink);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<BlogBean> CREATOR = new Creator<BlogBean>() {
        @Override
        public BlogBean createFromParcel(Parcel in) {
            return new BlogBean(in);
        }

        @Override
        public BlogBean[] newArray(int size) {
            return new BlogBean[size];
        }
    };

    public void setmBlogId(int mBlogId) {
        this.mBlogId = mBlogId;
    }

    public void setmBlogName(String mBlogName) {
        this.mBlogName = mBlogName;
    }

    public void setmBlogImageUrl(String mBlogImageUrl) {
        this.mBlogImageUrl = mBlogImageUrl;
    }


    //====================getter=================

    public int getmBlogId() {
        return mBlogId;
    }

    public String getmBlogName() {
        return mBlogName;
    }

    public String getmBlogImageUrl() {
        return mBlogImageUrl;
    }

    public String getLink() {
        String str = mLink;
        if (str != null) {
            if (!str.startsWith("http://") && !str.startsWith("https://")) {
                str = "http://" + str;
            } else {
                str = mLink;
            }
        }
        return str;
    }
}
