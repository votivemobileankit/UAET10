package votive.com.appuaet10.Utilities;


import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import votive.com.appuaet10.Beans.SearchBean;


public class UserUtils {

    public static UserUtils smUserUtils;

    public static UserUtils getInstance() {
        if (smUserUtils == null) {
            smUserUtils = new UserUtils();
        }
        return smUserUtils;
    }


    public void addRecentSearch(Context aContext, SearchBean aSearchBean) {
        List<SearchBean> stringList = getRecentSearchList(aContext);

        if (stringList != null && stringList.size() > 0) {

            boolean alreadyAdded = false;
            int position = -1;
            for (int i = 0; i < stringList.size(); i++) {
                SearchBean currentBean = stringList.get(i);

                if (aSearchBean.getStrSearchId() == currentBean.getStrSearchId() &&
                        aSearchBean.getmSearchType() == currentBean.getmSearchType()) {
                    alreadyAdded = true;
                    position = i;
                    break;

                }
            }

            if (alreadyAdded) {
                stringList.remove(position);
            }
            stringList.add(0, aSearchBean);

            if (stringList.size() > Constant.MAX_RECENT_SEARCH_COUNT) {
                stringList.subList(0, Constant.MAX_RECENT_SEARCH_COUNT - 1).clear();
            }
            Gson gson = new Gson();
            String json = gson.toJson(stringList);
            StorageUtils.putPref(aContext, Constant.PREF_USER_SEARCH_ITEMS, json);

        } else {
            stringList.add(aSearchBean);
            Gson gson = new Gson();
            String json = gson.toJson(stringList);
            StorageUtils.putPref(aContext, Constant.PREF_USER_SEARCH_ITEMS, json);
        }
    }

    public ArrayList<SearchBean> getRecentSearchList(Context aContext) {
        ArrayList<SearchBean> list = new ArrayList<SearchBean>();
        String searchStr = StorageUtils.getPrefStr(aContext, Constant.PREF_USER_SEARCH_ITEMS);
        if (searchStr != null && searchStr.length() > 0) {
            Gson gson = new GsonBuilder().create();
            // Type listOfTestObject = new TypeToken<ArrayList<SearchBean>>() {
            // }.getType();
            list = gson.fromJson(searchStr, getTypeToken());
            if (list == null) {
                list = new ArrayList<SearchBean>();

            }
        }
        return list;
    }


    private Type getTypeToken() {
        return new TypeToken<ArrayList<SearchBean>>() {
            private static final long serialVersionUID = 1L;
        }.getType();
    }

    public void clearRecentSearch(Context aContext) {
        StorageUtils.putPref(aContext, Constant.PREF_USER_SEARCH_ITEMS, null);
    }


}



