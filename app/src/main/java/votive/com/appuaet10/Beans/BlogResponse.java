package votive.com.appuaet10.Beans;


import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import votive.com.appuaet10.Utilities.Constant;

public class BlogResponse {


    @SerializedName(Constant.JSON_BLOG_LIST_KEY)
    ArrayList<BlogBean> mBlogList;

    @SerializedName(Constant.JSON_BLOG_TOTAL_PAGES)
    private int mPageNo;


    //====================getter=================


    public ArrayList<BlogBean> getmBlogList() {
        return mBlogList;
    }

    public int getmPageNo() {
        return mPageNo;
    }

}
