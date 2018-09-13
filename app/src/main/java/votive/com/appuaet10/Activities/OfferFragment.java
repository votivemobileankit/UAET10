package votive.com.appuaet10.Activities;


import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
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

import votive.com.appuaet10.Adapters.OfferItemAdapter;
import votive.com.appuaet10.Beans.OfferBean;
import votive.com.appuaet10.Beans.OfferResponseBean;
import votive.com.appuaet10.Interface.IOfferList;
import votive.com.appuaet10.R;
import votive.com.appuaet10.Utilities.AppController;
import votive.com.appuaet10.Utilities.Constant;
import votive.com.appuaet10.Utilities.UIUtils;
import votive.com.appuaet10.Utilities.Utils;

import static android.content.ContentValues.TAG;

public class OfferFragment extends Fragment implements IOfferList {

    private View mMainView;
    private Context mContext;
    private boolean mProgressBarshowing = false;
    private RecyclerView mRvOfferList;
    private OfferItemAdapter mOfferItemAdapter;
    private ArrayList<OfferBean> mOfferArrayList;

    private RelativeLayout mProgressBarLayout;
    private RelativeLayout mRlNoInterConnection;
    private Button mTryAgainBtn;
    private GridLayoutManager mLayoutManager;
    private LinearLayout mLLTermsNadConditions;
    private RelativeLayout mRlMainOfferList;
    private SwipeRefreshLayout mSwipeView;
    private boolean mHttpRequest = false;


    public OfferFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mContext = getActivity();
        mMainView = inflater.inflate(R.layout.fragment_offer, container, false);
        init();
        return mMainView;
    }


    private void init() {
        mSwipeView = (SwipeRefreshLayout) mMainView.findViewById(R.id.srl_offer_list);
        mProgressBarLayout = (RelativeLayout) mMainView.findViewById(R.id.rl_progressBar);
        mRvOfferList = (RecyclerView) mMainView.findViewById(R.id.rv_offer_list);
        mOfferArrayList = new ArrayList<>();
        mRlMainOfferList = (RelativeLayout) mMainView.findViewById(R.id.rl_offer_list_rv_main);

        mRlNoInterConnection = (RelativeLayout) mMainView.findViewById(R.id.rl_offers_no_internet_main);
        mTryAgainBtn = (Button) mMainView.findViewById(R.id.btn_no_internet_retry);
        mTryAgainBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startHttpRequestOfferList();
            }
        });

        startHttpRequestOfferList();

        mHttpRequest = false;
        mSwipeView.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mOfferArrayList.clear();
                mSwipeView.setRefreshing(true);
                mHttpRequest = true;
                startHttpRequestOfferList();
            }
        });


        final FloatingActionButton fabUp = (FloatingActionButton) mMainView.findViewById(R.id.fab_share);
        fabUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mRvOfferList.smoothScrollToPosition(0);
                fabUp.hide();
            }
        });
        fabUp.hide();

        mRvOfferList.addOnScrollListener(new RecyclerView.OnScrollListener() {
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
    }

    private void setCategoryList(ArrayList<OfferBean> aCategoryList) {

        if (aCategoryList.size() > 0) {
            mRlNoInterConnection.setVisibility(View.GONE);
            mRlMainOfferList.setVisibility(View.VISIBLE);
            mRvOfferList.setVisibility(View.VISIBLE);
            //mOfferArrayList = aCategoryList;
            for (int i = 0; i < aCategoryList.size(); i++) {

                mOfferArrayList.add(aCategoryList.get(i));
            }

            mOfferItemAdapter = new OfferItemAdapter(mContext, mOfferArrayList, this);
            mLayoutManager = new GridLayoutManager(mContext, 1);
            //mRvOfferList.addItemDecoration(new ItemDecorationAlbumColumns(5, 1));
            mRvOfferList.setLayoutManager(mLayoutManager);
            mRvOfferList.setAdapter(mOfferItemAdapter);
            mRvOfferList.setAdapter(mOfferItemAdapter);
            mSwipeView.setRefreshing(false);
        } else {
            UIUtils.showToast(mContext, getString(R.string.NoOffersErrorMsg));
        }
    }

    public void startHttpRequestOfferList() {

        if (Utils.isConnectingToInternet(mContext)) {
            String baseUrl = Constant.API_OFFER_LIST_DEV;

            if (!mHttpRequest) {
                showProgressBar();
            }
            StringRequest strRequest = new StringRequest(Request.Method.POST, baseUrl,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            System.out.println("!!!!API_OFFER_LIST_DEV = " + response);
                            try {
                                Gson gson = new GsonBuilder().create();
                                JsonParser jsonParser = new JsonParser();
                                JsonObject jsonResp = jsonParser.parse(response).getAsJsonObject();

                                OfferResponseBean offerResponseBean = gson.fromJson(jsonResp, OfferResponseBean.class);
                                hideProgressBar();
                                if (offerResponseBean != null && offerResponseBean.getmOfferList() != null && offerResponseBean.getmOfferList().size() > 0) {

                                    if (Utils.isStatusSuccess(offerResponseBean.getmStatus())) {
                                        mRlNoInterConnection.setVisibility(View.GONE);
                                        mRlMainOfferList.setVisibility(View.VISIBLE);
                                        setCategoryList(offerResponseBean.getmOfferList());
                                    } else {
                                        UIUtils.showToast(mContext, offerResponseBean.getmMsg());
                                    }
                                } else {
                                    UIUtils.showToast(mContext, getString(R.string.Offer_list_Msg));
                                    mRlNoInterConnection.setVisibility(View.GONE);
                                    mRvOfferList.setVisibility(View.GONE);
                                    mRlMainOfferList.setVisibility(View.GONE);

                                    hideProgressBar();
                                }
                            } catch (Exception e) {
                                UIUtils.showToast(mContext, getString(R.string.Offer_list_Msg));
                                mRlNoInterConnection.setVisibility(View.GONE);
                                mRvOfferList.setVisibility(View.GONE);
                                mRlMainOfferList.setVisibility(View.GONE);
                                hideProgressBar();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            hideProgressBar();

                            if (error instanceof NoConnectionError) {
                                mRlNoInterConnection.setVisibility(View.VISIBLE);
                                mRlMainOfferList.setVisibility(View.GONE);
                                mRvOfferList.setVisibility(View.GONE);
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
            mRlMainOfferList.setVisibility(View.GONE);
            mRvOfferList.setVisibility(View.GONE);

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


    public void handleOfferClick(int aMenuIndex, Context aContext, String myofferid) {
        Intent intent = new Intent(aContext, HomeActivity.class);
        intent.putExtra(Constant.INTENT_MENU_INDEX_KEY, aMenuIndex);
        intent.putExtra(Constant.INTENT_OFFER_ID, Integer.parseInt(myofferid));
        Log.e("myofferid",""+myofferid);
        intent.putExtra(Constant.SCREEN_CALL_INDEX, Constant.INTENT_SCREEN_CALL_BY_OFFER_FRAGMENT);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        aContext.startActivity(intent);
    }

    @Override
    public void clickOfferItem(OfferBean aOfferBean, int aId) {
        //showPopuDialog(aOfferBean);

        if (aOfferBean.getmStrOfferType().equalsIgnoreCase("1")){
            Intent intent;
            intent = new Intent(mContext, OfferDetailedActivity.class);
            intent.putExtra(Constant.INTENT_OFFER_ID, aOfferBean.getmStrOfferId());
            mContext.startActivity(intent);
        }else{
            handleOfferClick(Constant.MENU_OPTION_CONTACT_US_INDEX, mContext ,aOfferBean.getmStrOfferId());
        }

    }
}
