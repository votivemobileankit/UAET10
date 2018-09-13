package votive.com.appuaet10.Interface;


import votive.com.appuaet10.Beans.SearchBean;

public interface ISearchCategory {
    public void searchItemClicked(int aPosition, SearchBean aSearchBean);

    public void historyItemClicked(int aPosition, SearchBean aSearchBean);

}
