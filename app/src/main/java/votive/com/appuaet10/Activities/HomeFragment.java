package votive.com.appuaet10.Activities;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import java.util.ArrayList;

import votive.com.appuaet10.Adapters.CategoryAdapter;
import votive.com.appuaet10.Beans.CategoryBean;
import votive.com.appuaet10.Interface.ICategory;
import votive.com.appuaet10.R;
import votive.com.appuaet10.Utilities.Utils;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment implements ICategory {

    private View mMainView;
    private Context mContext;
    private boolean mProgressBarshowing = false;
    private boolean mHttpRequest = false;

    private RecyclerView mRvCategory;
    private RelativeLayout mProgressBarLayout;
    private SwipeRefreshLayout mSwipeView;

    private CategoryAdapter mCategoryAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        mContext = getActivity();
        mMainView = inflater.inflate(R.layout.fragment_home, container, false);
        init();
        return mMainView;
    }

    @Override
    public void onResume() {
        super.onResume();
        /*if (!Constant.HOME_FRAGMENT_CALL_BACK){
            mRvCategory.smoothScrollToPosition(0);
            mLayoutManager.scrollToPosition(0);
        }*/
    }

    private void init() {
        mProgressBarLayout = (RelativeLayout) mMainView.findViewById(R.id.rl_progressBar);

    }

    private void updateCategoryListView(ArrayList<CategoryBean> aCategoryList) {
        if (aCategoryList.size() > 0) {
            //mRlEducationLayoutMsg.setVisibility(View.GONE);

        } else {
            //mRlEducationLayoutMsg.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void clickCategoryItem(int aPosition, int aId, String aName) {

        /*Constant.HOME_FRAGMENT_CALL_BACK = true;
        Intent intent = new Intent(mContext, CategoryItemListActivity.class);
        intent.putExtra(Constant.INTENT_CATEGORY_ITEM_ID, aId);
        intent.putExtra(Constant.INTENT_CATEGORY_NAME, aName);
        startActivity(intent);*/
    }


    public void startHttpRequestCategory() {

        if (Utils.isConnectingToInternet(mContext)) {

        }
    }

    private void hideProgressBar() {
        if (mProgressBarshowing) {
            mProgressBarLayout.setVisibility(View.GONE);
            mProgressBarshowing = false;
        }
    }

    private void showProgressBar() {

        if (!mProgressBarshowing) {
            mProgressBarLayout.setVisibility(View.VISIBLE);
            mProgressBarshowing = true;
        }
    }

}
