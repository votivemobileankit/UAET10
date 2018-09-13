package votive.com.appuaet10.Activities;


import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.DefaultSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import votive.com.appuaet10.Adapters.FeaturesCategoryAdapter;
import votive.com.appuaet10.Adapters.FeaturesListingAdapter;
import votive.com.appuaet10.Beans.BannerBean;
import votive.com.appuaet10.Beans.CategoryBean;
import votive.com.appuaet10.Beans.HomeContentResponseBean;
import votive.com.appuaet10.Interface.IFeatureCategoryList;
import votive.com.appuaet10.Interface.IFeatureListing;
import votive.com.appuaet10.R;
import votive.com.appuaet10.Utilities.AppController;
import votive.com.appuaet10.Utilities.AppData;
import votive.com.appuaet10.Utilities.Constant;
import votive.com.appuaet10.Utilities.UIUtils;
import votive.com.appuaet10.Utilities.Utils;

import static android.content.ContentValues.TAG;

public class HomeContentFragment extends Fragment implements BaseSliderView.OnSliderClickListener, ViewPagerEx.OnPageChangeListener, IFeatureListing, IFeatureCategoryList {

    private View mMainView;
    private Context mContext;
    private boolean mProgressBarshowing = false;
    private boolean mHttpRequest = false;
    private RecyclerView mRvFeatureCategory;
    private RelativeLayout mProgressBarLayout;
    private SwipeRefreshLayout mSwipeView;
    private FeaturesListingAdapter mFeaturesListingAdapter;
    private FeaturesCategoryAdapter mFeaturesCategoryAdapter;
    private GridLayoutManager mLayoutManager;
    private RecyclerView mRvFeatureListing;
    private SliderLayout mDemoSlider;
    private RelativeLayout mRlMainHomeContent;
    private ArrayList<BannerBean> mStrBanner;
    private RelativeLayout mRlNoInterConnection;
    private ArrayList<CategoryBean> mFeatureListing;
    private RelativeLayout mRlBottomImg;
    private Button mTryAgainBtn;
    private NestedScrollView mScroller;
    private FloatingActionButton mGoToTopFBtn;
    private ArrayList<BannerBean> mBannerItemList;


    public HomeContentFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mContext = getActivity();
        mMainView = inflater.inflate(R.layout.fragment_home_content, container, false);
        init();
        return mMainView;
    }

    private void init() {

        mStrBanner = new ArrayList<>();
        mRlBottomImg = (RelativeLayout) mMainView.findViewById(R.id.rl_view_all_category);
        mRlMainHomeContent = (RelativeLayout) mMainView.findViewById(R.id.rl_main_home_content);
        mRlMainHomeContent.setVisibility(View.GONE);
        mDemoSlider = (SliderLayout) mMainView.findViewById(R.id.slider);
        mRlNoInterConnection = (RelativeLayout) mMainView.findViewById(R.id.rl_category_no_internet_main);
        mProgressBarLayout = (RelativeLayout) mMainView.findViewById(R.id.rl_progressBar);
        mRvFeatureCategory = (RecyclerView) mMainView.findViewById(R.id.rv_features_category);
        mRvFeatureListing = (RecyclerView) mMainView.findViewById(R.id.rv_features_listing);
        mFeatureListing = new ArrayList<>();
        hideProgressBar();
        mSwipeView = (SwipeRefreshLayout) mMainView.findViewById(R.id.srl_home);
        mScroller = (NestedScrollView) mMainView.findViewById(R.id.nsv_home_content);

        mTryAgainBtn = (Button) mMainView.findViewById(R.id.btn_no_internet_retry);
        mTryAgainBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startHttpRequestForHomeContentData();
            }
        });

        setFeatureListingData();

        mRlBottomImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((HomeActivity) getActivity()).handleHomeAllCategoryClick();
            }
        });


        if (mScroller != null) {
            mScroller.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
                @Override
                public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {

                    System.out.println("dy = " + scrollY);
                    if (scrollY > 0 || scrollY < 0 && !mGoToTopFBtn.isShown()) {
                        mGoToTopFBtn.show();
                    } else {
                        mGoToTopFBtn.show();
                    }

                    if (scrollY == 0) {
                        mGoToTopFBtn.hide();
                    }

                    if (mLayoutManager.findFirstVisibleItemPosition() == 0 && mGoToTopFBtn.isShown()) {
                        //fabUp.hide();
                    }


                }
            });
        }

        mScroller.post(new Runnable() {
            @Override
            public void run() {
                mScroller.scrollTo(0, 0);
            }
        });

        mGoToTopFBtn = (FloatingActionButton) mMainView.findViewById(R.id.fab_share);
        mGoToTopFBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mScroller.fullScroll(ScrollView.FOCUS_UP);
                mGoToTopFBtn.hide();

            }
        });


        mGoToTopFBtn.hide();
        mHttpRequest = false;

        mSwipeView.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mSwipeView.setRefreshing(true);
                mHttpRequest = true;
                startHttpRequestForHomeContentData();
            }
        });


        ArrayList<CategoryBean> categoryList = AppData.getInstance().getHomeCategoryList();
        ArrayList<CategoryBean> categoryBusinessList = AppData.getInstance().getHomeCategoryBusinessList();
        ArrayList<BannerBean> bannerImageBeanList = AppData.getInstance().getHomeBannerList();

        if (categoryBusinessList.size() > 0
                && categoryBusinessList.size() > 0
                && bannerImageBeanList.size() > 0) {
            updateCategoryListView(categoryList);
            setCategotyListing(categoryBusinessList);
            setBannerImageList(bannerImageBeanList);
        } else {
            startHttpRequestForHomeContentData();
        }
    }

    private void setFeatureListingData() {
        mLayoutManager = new GridLayoutManager(mContext, 2);
        mRvFeatureCategory.addItemDecoration(new ItemDecorationAlbumColumns(0, 2));
        mRvFeatureCategory.setLayoutManager(mLayoutManager);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(mContext, 1);
        mRvFeatureListing.addItemDecoration(new ItemDecorationAlbumColumns(0, 1));
        mRvFeatureListing.setLayoutManager(mLayoutManager);

    }


    private void updateCategoryListView(ArrayList<CategoryBean> aCategoryList) {
        if (aCategoryList.size() > 0) {
            mRlNoInterConnection.setVisibility(View.GONE);
            mRlMainHomeContent.setVisibility(View.VISIBLE);
            mRvFeatureCategory.setVisibility(View.VISIBLE);
            mFeaturesCategoryAdapter = new FeaturesCategoryAdapter(mContext, aCategoryList, this);
            mRvFeatureCategory.setAdapter(mFeaturesCategoryAdapter);
            mRvFeatureCategory.setNestedScrollingEnabled(false);
            mSwipeView.setRefreshing(false);
        }
    }


    public void startHttpRequestForHomeContentData() {

        if (Utils.isConnectingToInternet(mContext)) {
            String baseUrl = Constant.API_HOME_CONTENT;
            if (Constant.MULTI_TASK_BANNER) {
                baseUrl = Constant.API_HOME_WITH_BANNER_TASK;
            }
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

                                HomeContentResponseBean categoryResponse = gson.fromJson(jsonResp, HomeContentResponseBean.class);
                                hideProgressBar();

                                mRlMainHomeContent.setVisibility(View.VISIBLE);

                                if (categoryResponse != null && categoryResponse.getmFeatureCategoryBeanArrayList() != null && categoryResponse.getmFeatureCategoryBeanArrayList().size() > 0
                                        && categoryResponse.getmFeatureListingList() != null && categoryResponse.getmFeatureListingList().size() > 0
                                        && categoryResponse.getmBannerImagelist() != null && categoryResponse.getmBannerImagelist().size() > 0) {

                                    if (Utils.isStatusSuccess(categoryResponse.getmStatus())) {

                                        AppData.getInstance().setCategoryHomeItemList(mContext, categoryResponse.getmFeatureCategoryBeanArrayList());
                                        AppData.getInstance().setCategoryItemList(mContext, categoryResponse.getmFeatureListingList());
                                        AppData.getInstance().setCategoryHomeBannerList(mContext, categoryResponse.getmBannerImagelist());
                                        AppData.getInstance().setVideoStr(mContext, categoryResponse.getVideoStr());
                                        setBannerImageList(categoryResponse.getmBannerImagelist());

                                        updateCategoryListView(categoryResponse.getmFeatureCategoryBeanArrayList());
                                        setCategotyListing(categoryResponse.getmFeatureListingList());
                                        hideProgressBar();
                                        mRlNoInterConnection.setVisibility(View.GONE);
                                    } else {
                                        UIUtils.showToast(mContext, categoryResponse.getmMsg());
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
                                mRlMainHomeContent.setVisibility(View.GONE);
                                mRvFeatureListing.setVisibility(View.GONE);
                                mRvFeatureCategory.setVisibility(View.GONE);

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
            mRlMainHomeContent.setVisibility(View.GONE);
            mRvFeatureListing.setVisibility(View.GONE);
            mRvFeatureCategory.setVisibility(View.GONE);
            mDemoSlider.setVisibility(View.GONE);

        }
    }

    private void setBannerImageList(ArrayList<BannerBean> bannerImageBeen) {

        mStrBanner = bannerImageBeen;

        HashMap<String, String> url_maps = new HashMap<String, String>();
        for (int i = 0; i < mStrBanner.size(); i++) {
            BannerBean curBean = mStrBanner.get(i);
            url_maps.put("slide_image_" + i, curBean.getURl());
        }

        mDemoSlider.removeAllSliders();
        for (String name : url_maps.keySet()) {
            DefaultSliderView textSliderView = new DefaultSliderView(getActivity());
            textSliderView
                    .image(url_maps.get(name))
                    .setScaleType(BaseSliderView.ScaleType.Fit)
                    .setOnSliderClickListener(this);
            textSliderView.error(R.drawable.ic_placeholder);
            mDemoSlider.addSlider(textSliderView);
        }

        mRlNoInterConnection.setVisibility(View.GONE);
        mRlMainHomeContent.setVisibility(View.VISIBLE);
        mDemoSlider.setVisibility(View.VISIBLE);
        mDemoSlider.setPresetTransformer(SliderLayout.Transformer.Accordion);
        mDemoSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        mDemoSlider.setCustomAnimation(new DescriptionAnimation());
        mDemoSlider.setDuration(2000);
        mDemoSlider.addOnPageChangeListener(this);
    }

    private void setCategotyListing(ArrayList<CategoryBean> aFeatureListingBeen) {
        if (aFeatureListingBeen.size() > 0) {

            mFeatureListing = aFeatureListingBeen;

            mRlNoInterConnection.setVisibility(View.GONE);
            mRlMainHomeContent.setVisibility(View.VISIBLE);
            mRvFeatureListing.setVisibility(View.VISIBLE);


            mFeaturesListingAdapter = new FeaturesListingAdapter(mContext, mFeatureListing, this);
            mRvFeatureListing.setAdapter(mFeaturesListingAdapter);
            mRvFeatureListing.setNestedScrollingEnabled(false);
            mSwipeView.setRefreshing(false);
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


    private BannerBean getSelectedBannerBean(String aUrl) {
        BannerBean bean = null;
        if (mStrBanner != null && aUrl != null) {
            for (int i = 0; i < mStrBanner.size(); i++) {
                BannerBean curBean = mStrBanner.get(i);
                if (aUrl.equals(curBean.getURl())) {
                    bean = curBean;
                    break;
                }
            }
        }
        return bean;
    }

    @Override
    public void onSliderClick(BaseSliderView slider) {
        BannerBean bean = getSelectedBannerBean(slider.getUrl());
        if (bean != null) {
            switch (bean.getBannerType()) {

                case Constant.BANNER_TYPE_DEFAULT:
                    break;

                case Constant.BANNER_TYPE_BUSINESS:

                        Intent intentB = new Intent(mContext, BusinessDetailedBySearchActivity.class);
                        intentB.putExtra(Constant.INTENT_BUSINESS_ID, String.valueOf(bean.getId()));

                     //   Constant.FIX_BANNER_ID_INSURANSE_HOME = bean.getId();
                        startActivity(intentB);

                    break;

                case Constant.BANNER_TYPE_CATEGORY:
                    Constant.HOME_FRAGMENT_CALL_BACK = true;
                    Intent intent = new Intent(mContext, CategoryItemListActivity.class);
                    intent.putExtra(Constant.INTENT_CATEGORY_ITEM_ID, String.valueOf(bean.getId()));
                    intent.putExtra(Constant.INTENT_CATEGORY_NAME, bean.getItemName());
                    startActivity(intent);
                    break;

                case Constant.BANNER_TYPE_WEBLINK:
                    try {
                        Intent intentWebsite = new Intent(Intent.ACTION_VIEW);
                        intentWebsite.setData(Uri.parse(bean.getmStrWebLink()));
                        startActivity(intentWebsite);
                    } catch (ActivityNotFoundException e) {
                        UIUtils.showToast(mContext, "No application can handle this request. Please install a web browser");
                    }
                    break;
            }
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }


    @Override
    public void onStop() {
        mDemoSlider.stopAutoCycle();
        super.onStop();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!Constant.HOME_FRAGMENT_CALL_BACK) {
            mRvFeatureCategory.smoothScrollToPosition(0);
            mLayoutManager.scrollToPosition(0);
        }

        if (mDemoSlider != null && mStrBanner != null && mStrBanner.size() > 0) {
            mDemoSlider.startAutoCycle();
        }
    }

    @Override
    public void clickFeatureListingItem(int aPosition, String strName) {
        Constant.HOME_FRAGMENT_CALL_BACK = true;
        Intent intent = new Intent(mContext, CategoryItemListActivity.class);
        intent.putExtra(Constant.INTENT_CATEGORY_ITEM_ID, String.valueOf(aPosition));
        intent.putExtra(Constant.INTENT_CATEGORY_NAME, strName);
        startActivity(intent);
    }

    @Override
    public void clickFeatureCategoryItem(int aPosition, String strName) {
        Constant.HOME_FRAGMENT_CALL_BACK = true;
        Intent intent = new Intent(mContext, CategoryItemListActivity.class);
        intent.putExtra(Constant.INTENT_CATEGORY_ITEM_ID, String.valueOf(aPosition));
        intent.putExtra(Constant.INTENT_CATEGORY_NAME, strName);
        startActivity(intent);
    }
}
