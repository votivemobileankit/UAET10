package votive.com.appuaet10.Activities;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import votive.com.appuaet10.Beans.CategoryBean;
import votive.com.appuaet10.Beans.CategoryResponse;
import votive.com.appuaet10.R;
import votive.com.appuaet10.Utilities.AppController;
import votive.com.appuaet10.Utilities.AppData;
import votive.com.appuaet10.Utilities.Constant;
import votive.com.appuaet10.Utilities.UIUtils;
import votive.com.appuaet10.Utilities.Utils;

import static android.content.ContentValues.TAG;

public class CategoryFragment extends Fragment {

    private View mMainView;
    private Context mContext;

    private RelativeLayout mProgressBarLayout;
    //private SwipeRefreshLayout mSwipeView;
    private RelativeLayout mRlNoInterConnection;
    private Button mTryAgainBtn;
    private RelativeLayout mRlMain;
    private ViewPager mViewPager;

    private int mTotalPages = 0;
    private boolean mProgressBarshowing = false;
    private boolean mHttpRequest = false;

    private LinearLayout[] mIndicatorArr;
    private LinearLayout mPageIndicatorLl;


    public CategoryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mContext = getActivity();
        mMainView = inflater.inflate(R.layout.fragment_category, container, false);
        init();
        return mMainView;
    }

    private void init() {
        mProgressBarLayout = (RelativeLayout) mMainView.findViewById(R.id.rl_progressBar);
        mRlMain = (RelativeLayout) mMainView.findViewById(R.id.rl_category_main);
        mRlNoInterConnection = (RelativeLayout) mMainView.findViewById(R.id.rl_category_no_internet_main);
        mTryAgainBtn = (Button) mMainView.findViewById(R.id.btn_no_internet_retry);
        mTryAgainBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startHttpRequestCategory();
            }
        });

        //mSwipeView = (SwipeRefreshLayout) mMainView.findViewById(R.id.srl_category);
        mPageIndicatorLl = (LinearLayout) mMainView.findViewById(R.id.ll_page_indicator);
        mViewPager = (ViewPager) mMainView.findViewById(R.id.vp_category);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                addBottomDots(position, mTotalPages);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        ArrayList<CategoryBean> curCategoryList = AppData.getInstance().getCategoryList();
        if (curCategoryList.size() > 0) {
            updateCategoryListView(curCategoryList);
        } else {
            startHttpRequestCategory();
        }

        mHttpRequest = false;

    }


    private void addBottomDots(int currentPage, int aTotalPages) {

        ImageView[] mDots = new ImageView[aTotalPages];
        mPageIndicatorLl.removeAllViews();
        for (int i = 0; i < mDots.length; i++) {
            mDots[i] = new ImageView(getActivity());
            LinearLayout.LayoutParams vp = new LinearLayout.LayoutParams(60, 18);
            mDots[i].setLayoutParams(vp);
            mDots[i].setBackgroundResource(R.drawable.slide_orange);
            mPageIndicatorLl.addView(mDots[i]);
            if (i != mDots.length - 1) {
                ImageView hiddenDotIv = new ImageView(getActivity());
                LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(25, 15);
                hiddenDotIv.setLayoutParams(param);
                //hiddenDotIv.setBackgroundResource(R.drawable.selected_activedots);
                hiddenDotIv.setVisibility(View.INVISIBLE);
                mPageIndicatorLl.addView(hiddenDotIv);
            }
        }
        if (mDots.length > 0) {
            mDots[currentPage].setBackgroundResource(R.drawable.slide_white);
        }
    }


    private void updateCategoryListView(ArrayList<CategoryBean> aCategoryList) {
        if (aCategoryList.size() > 0) {
            mRlNoInterConnection.setVisibility(View.GONE);
            mRlMain.setVisibility(View.VISIBLE);
            mTotalPages = getTotalPages(aCategoryList.size());
            GridPagerAdapter adapter = new GridPagerAdapter(getChildFragmentManager(),
                    mTotalPages, aCategoryList);
            mViewPager.setAdapter(adapter);
            addBottomDots(0, mTotalPages);
        }
        //mSwipeView.setRefreshing(false);
    }

    public void startHttpRequestCategory() {

        if (Utils.isConnectingToInternet(mContext)) {
            String baseUrl = Constant.API_CATEGORY_LIST;
            if (!mHttpRequest) {
                showProgressBar();
            }
            StringRequest strRequest = new StringRequest(Request.Method.POST, baseUrl,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            System.out.println("!!!!response = " + response);
                            try {
                                Gson gson = new GsonBuilder().create();
                                JsonParser jsonParser = new JsonParser();
                                JsonObject jsonResp = jsonParser.parse(response).getAsJsonObject();

                                CategoryResponse categoryResponse = gson.fromJson(jsonResp, CategoryResponse.class);
                                hideProgressBar();
                                if (categoryResponse != null && categoryResponse.getmCategoryList() != null && categoryResponse.getmCategoryList().size() > 0) {
                                    if (categoryResponse.getmErrorCode() != null) {
                                        if (categoryResponse.getmErrorCode().equalsIgnoreCase(Constant.JSON_CATEGORY_ERROR_CODE)) {
                                            UIUtils.showToast(mContext, categoryResponse.getmMsg());
                                        } else {
                                            UIUtils.showToast(mContext, getResources().getString(R.string.VolleyErrorMsg));
                                        }
                                    } else {
                                        if (Utils.isStatusSuccess(categoryResponse.getmStatus())) {
                                            AppData.getInstance().setCategoryList(mContext, categoryResponse.getmCategoryList());

                                            updateCategoryListView(categoryResponse.getmCategoryList());
                                        } else {
                                            UIUtils.showToast(mContext, categoryResponse.getmMsg());
                                        }
                                    }
                                } else {
                                    hideProgressBar();
                                    UIUtils.showToast(mContext, getResources().getString(R.string.Home_No_Category_List));
                                }
                            } catch (Exception e) {
                                hideProgressBar();
                                UIUtils.showToast(mContext, getResources().getString(R.string.VolleyErrorMsg));
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            hideProgressBar();
                            if (error instanceof NoConnectionError) {
                                mRlNoInterConnection.setVisibility(View.VISIBLE);
                                mRlMain.setVisibility(View.GONE);
                                //UIUtils.showToast(mContext, getString(R.string.InternetErrorMsg));
                            } else {
                                UIUtils.showToast(mContext, getResources().getString(R.string.VolleyErrorMsg));
                            }
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    return params;
                }
            };
            strRequest.setShouldCache(false);
            strRequest.setTag(TAG);
            AppController.getInstance().addToRequestQueue(strRequest);
            strRequest.setRetryPolicy(new DefaultRetryPolicy(
                    10000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        } else {
            mRlNoInterConnection.setVisibility(View.VISIBLE);
            mRlMain.setVisibility(View.GONE);
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

    public static class GridPagerAdapter extends FragmentPagerAdapter {
        private int mTotalPages;
        private ArrayList<CategoryBean> mCategoryBeanList;

        public GridPagerAdapter(FragmentManager fragmentManager,
                                int aTotalPages,
                                ArrayList<CategoryBean> aCategoryBean) {
            super(fragmentManager);
            mTotalPages = aTotalPages;
            mCategoryBeanList = aCategoryBean;
        }

        // Returns total number of pages
        @Override
        public int getCount() {
            return mTotalPages;
        }

        // Returns the fragment to display for that page
        @Override
        public Fragment getItem(int position) {
            return CategoryGridFragment.newInstance(position, getCategoryListForPage(position));
        }

        private ArrayList<CategoryBean> getCategoryListForPage(int aPage) {
            ArrayList<CategoryBean> list = new ArrayList<>();
            int startIndex = aPage * Constant.MAX_ITEM_IN_CATEGORY_GRID;
            int endIndex = (aPage + 1) * Constant.MAX_ITEM_IN_CATEGORY_GRID;
            if (endIndex > mCategoryBeanList.size()) {
                endIndex = mCategoryBeanList.size();
            }
            for (int i = startIndex; i < endIndex; i++) {
                CategoryBean curBean = mCategoryBeanList.get(i);
                list.add(curBean);
            }
            return list;
        }
    }


    private static int getTotalPages(int aListCount) {
        int pages = 1;
        if (aListCount > Constant.MAX_ITEM_IN_CATEGORY_GRID) {
            pages = aListCount / Constant.MAX_ITEM_IN_CATEGORY_GRID;
            if (aListCount % Constant.MAX_ITEM_IN_CATEGORY_GRID != 0) {
                pages = pages + 1;
            }
        }
        return pages;
    }


}
