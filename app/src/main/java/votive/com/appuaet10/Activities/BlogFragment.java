package votive.com.appuaet10.Activities;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

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

import votive.com.appuaet10.Adapters.BlogAdapter;
import votive.com.appuaet10.Beans.BlogBean;
import votive.com.appuaet10.Beans.BlogResponse;
import votive.com.appuaet10.Interface.IBlogRowClick;
import votive.com.appuaet10.R;
import votive.com.appuaet10.Utilities.AppController;
import votive.com.appuaet10.Utilities.Constant;
import votive.com.appuaet10.Utilities.UIUtils;
import votive.com.appuaet10.Utilities.Utils;

import static android.content.ContentValues.TAG;

public class BlogFragment extends Fragment implements IBlogRowClick {

    private View mMainView;
    private Context mContext;
    private boolean mProgressBarshowing = false;
    private boolean mHttpRequest = false;
    private RecyclerView mRvBlogList;
    private RelativeLayout mProgressBarLayout;
    private SwipeRefreshLayout mSwipeView;
    private BlogAdapter mBlogAdapter;
    private GridLayoutManager mLayoutManager;
    private RelativeLayout mRlNoInterConnection;
    private Button mTryAgainBtn;
    private int mPageIndex = 1;
    private int mPageCount = 1;
    private ArrayList<BlogBean> mBlogList;
    private boolean mFirstTime = true;


    public BlogFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mContext = getActivity();
        mMainView = inflater.inflate(R.layout.fragment_blog, container, false);
        init();
        return mMainView;
    }

    private void init() {
        mProgressBarLayout = (RelativeLayout) mMainView.findViewById(R.id.rl_progressBar);
        mRlNoInterConnection = (RelativeLayout) mMainView.findViewById(R.id.rl_blog_no_internet_main);
        mTryAgainBtn = (Button) mMainView.findViewById(R.id.btn_no_internet_retry);
        mTryAgainBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startHttpRequestForBlogList();
            }
        });

        mRvBlogList = (RecyclerView) mMainView.findViewById(R.id.rv_blog_list);
        mSwipeView = (SwipeRefreshLayout) mMainView.findViewById(R.id.srl_blog);
        mLayoutManager = new GridLayoutManager(mContext, 1);
        mRvBlogList.setLayoutManager(mLayoutManager);
        mBlogList = new ArrayList<>();

        EndlessRecyclerViewScrollListener mScrollListener = new EndlessRecyclerViewScrollListener(mLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to the bottom of the list

                // loadNextDataFromApi(page);
                System.out.println("page = " + page);

                if(Utils.isConnectingToInternet(mContext)){
                    loadNextDataFromApi(page);
                }else{
                    Toast.makeText(mContext, getString(R.string.InternetErrorMsg), Toast.LENGTH_SHORT).show();
                }
            }
        };
        // Adds the scroll listener to RecyclerView
        mRvBlogList.addOnScrollListener(mScrollListener);


        startHttpRequestForBlogList();

        mHttpRequest = false;

        mSwipeView.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mSwipeView.setRefreshing(true);

                mHttpRequest = true;
                startHttpRequestForBlogList();
            }
        });


        final FloatingActionButton fabUp = (FloatingActionButton) mMainView.findViewById(R.id.fab_share);
        fabUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mRvBlogList.smoothScrollToPosition(0);
                //fabUp.hide();
            }
        });
        fabUp.hide();

        mRvBlogList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {

                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
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
    }

    private void loadNextDataFromApi(int page) {
        if (mPageIndex <= mPageCount) {
            startHttpRequestForBlogList();
        }
    }

    private void HandleBlogList(ArrayList<BlogBean> aBlogList, int aPageIndex) {
       // mBlogList = aBlogList;
        mBlogList.addAll(aBlogList);
        mPageIndex++;
        mPageCount = aPageIndex;


        if (mFirstTime) {
            mBlogAdapter = new BlogAdapter(mContext, mBlogList, this);
            mRvBlogList.setAdapter(mBlogAdapter);
            mFirstTime = false;
        } else {
            mBlogAdapter.notifyDataSetChanged();
        }
        mSwipeView.setRefreshing(false);

    }

    public void startHttpRequestForBlogList() {

        if (Utils.isConnectingToInternet(mContext)) {
            String baseUrl = Constant.API_BLOG;
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

                                BlogResponse blogResponse = gson.fromJson(jsonResp, BlogResponse.class);
                                hideProgressBar();
                                if (blogResponse != null && blogResponse.getmBlogList() != null && blogResponse.getmBlogList().size() > 0) {
                                    HandleBlogList(blogResponse.getmBlogList(), blogResponse.getmPageNo());
                                } else {
                                    hideProgressBar();
                                    UIUtils.showToast(mContext,  getResources().getString(R.string.BlogListMsg));
                                }
                            } catch (Exception e) {
                                hideProgressBar();
                                UIUtils.showToast(mContext,  getResources().getString(R.string.VolleyErrorMsg));
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            hideProgressBar();
                            if (error instanceof NoConnectionError) {
                                mRlNoInterConnection.setVisibility(View.VISIBLE);
                                mRvBlogList.setVisibility(View.GONE);
                            } else {
                                UIUtils.showToast(mContext,  getResources().getString(R.string.VolleyErrorMsg));
                            }
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    params.put(Constant.JSON_BLOG_PAGE_INDEX_KEY, "" + mPageIndex);
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
            mRvBlogList.setVisibility(View.GONE);
        }
    }


    private void hideProgressBar() {
        if (mFirstTime) {
            mProgressBarLayout.setVisibility(View.GONE);
        } else {
            mProgressBarLayout.setVisibility(View.GONE);
        }
    }

    private void showProgressBar() {
        if (mFirstTime) {
            mProgressBarLayout.setVisibility(View.VISIBLE);
        } else {
            hideProgressBar();
        }
    }


    @Override
    public void onBlogRowClick(int aPosition, BlogBean aSelectedBean) {
        if (aPosition != -1) {
            Intent intent = new Intent(mContext, BlogDetailActivity.class);
            intent.putExtra(Constant.INTENT_BLOG_DETAILS_BEAN_KEY, aSelectedBean);
            getActivity().startActivity(intent);
        }
    }
}