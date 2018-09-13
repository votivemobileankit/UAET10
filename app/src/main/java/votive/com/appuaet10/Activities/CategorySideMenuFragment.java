package votive.com.appuaet10.Activities;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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

import votive.com.appuaet10.Adapters.CategoryAdapter;
import votive.com.appuaet10.Beans.CategoryBean;
import votive.com.appuaet10.Beans.CategoryResponse;
import votive.com.appuaet10.Interface.ICategory;
import votive.com.appuaet10.R;
import votive.com.appuaet10.Utilities.AppController;
import votive.com.appuaet10.Utilities.AppData;
import votive.com.appuaet10.Utilities.Constant;
import votive.com.appuaet10.Utilities.UIUtils;
import votive.com.appuaet10.Utilities.Utils;

import static android.content.ContentValues.TAG;

/**
 * A simple {@link Fragment} subclass.
 */
public class CategorySideMenuFragment extends Fragment implements ICategory {

    private View mMainView;
    private Context mContext;
    private boolean mProgressBarshowing = false;
    private boolean mHttpRequest = false;

    private RecyclerView mRvCategory;
    private RelativeLayout mProgressBarLayout;
    private SwipeRefreshLayout mSwipeView;

    private CategoryAdapter mCategoryAdapter;
    private GridLayoutManager mLayoutManager;

    private RelativeLayout mRlNoInterConnection;
    private Button mTryAgainBtn;


    public CategorySideMenuFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mContext = getActivity();
        mMainView = inflater.inflate(R.layout.fragment_category_side_menu, container, false);
        init();
        return mMainView;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!Constant.HOME_FRAGMENT_CALL_BACK) {
            mRvCategory.smoothScrollToPosition(0);
            mLayoutManager.scrollToPosition(0);
        }
    }

    private void init() {
        mProgressBarLayout = (RelativeLayout) mMainView.findViewById(R.id.rl_progressBar);
        mRvCategory = (RecyclerView) mMainView.findViewById(R.id.rv_category);
        mSwipeView = (SwipeRefreshLayout) mMainView.findViewById(R.id.srl_home);

        mLayoutManager = new GridLayoutManager(mContext, 1);
        mRvCategory.addItemDecoration(new ItemDecorationAlbumColumns(5, 1));
        mRvCategory.setLayoutManager(mLayoutManager);

        mProgressBarLayout = (RelativeLayout) mMainView.findViewById(R.id.rl_progressBar);
        mRlNoInterConnection = (RelativeLayout) mMainView.findViewById(R.id.rl_category_side_menu_no_internet_main);
        mTryAgainBtn = (Button) mMainView.findViewById(R.id.btn_no_internet_retry);
        mTryAgainBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startHttpRequestCategory();
            }
        });

        final FloatingActionButton fabUp = (FloatingActionButton) mMainView.findViewById(R.id.fab_share);
        fabUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mRvCategory.smoothScrollToPosition(0);
                fabUp.hide();

            }
        });

        fabUp.hide();

        mRvCategory.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {

                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {

                System.out.println("dy = " + dy);
                if (dy > 0 || dy < 0 && !fabUp.isShown()) {
                    fabUp.show();
                } else {
                    fabUp.show();
                }

                if (dy == 0) {
                    fabUp.hide();
                }

                if (mLayoutManager.findFirstVisibleItemPosition() == 0 && fabUp.isShown()) {
                    fabUp.hide();
                }


            }
        });
        //updateCategoryListView(AppData.getCategoryList());

        startHttpRequestCategory();
        mHttpRequest = false;

        mSwipeView.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mSwipeView.setRefreshing(true);
                Log.d("Swipe", "Refreshing Category");
                mHttpRequest = true;
                startHttpRequestCategory();
            }
        });


    }

    private void updateCategoryListView(ArrayList<CategoryBean> aCategoryList) {
        if (aCategoryList.size() > 0) {
            //mRlEducationLayoutMsg.setVisibility(View.GONE);

            AppData.getInstance().setCategoryList(mContext, aCategoryList);
            mCategoryAdapter = new CategoryAdapter(mContext, aCategoryList, this);
            mRvCategory.setAdapter(mCategoryAdapter);
            mSwipeView.setRefreshing(false);
        } else {
            //mRlEducationLayoutMsg.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void clickCategoryItem(int aPosition, int aId, String aName) {

        Constant.HOME_FRAGMENT_CALL_BACK = true;
        Intent intent = new Intent(mContext, CategoryItemListActivity.class);
        intent.putExtra(Constant.INTENT_CATEGORY_ITEM_ID, String.valueOf(aId));
        intent.putExtra(Constant.INTENT_CATEGORY_NAME, aName);
        startActivity(intent);
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
                                            mRlNoInterConnection.setVisibility(View.GONE);
                                            mRvCategory.setVisibility(View.VISIBLE);
                                            updateCategoryListView(categoryResponse.getmCategoryList());
                                        } else {
                                            UIUtils.showToast(mContext, categoryResponse.getmMsg());
                                        }
                                    }
                                } else {
                                    hideProgressBar();
                                    UIUtils.showToast(mContext, getString(R.string.Home_No_Category_List));
                                }
                            } catch (Exception e) {
                                hideProgressBar();
                                UIUtils.showToast(mContext, getString(R.string.VolleyErrorMsg));
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            hideProgressBar();
                            if (error instanceof NoConnectionError) {
                                mRlNoInterConnection.setVisibility(View.VISIBLE);
                                mRvCategory.setVisibility(View.GONE);
                                mSwipeView.setVisibility(View.GONE);
                            } else {
                                UIUtils.showToast(mContext, getString(R.string.VolleyErrorMsg));
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
            mRvCategory.setVisibility(View.GONE);
            mSwipeView.setVisibility(View.GONE);
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
