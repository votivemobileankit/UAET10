package votive.com.appuaet10.Activities;

import android.content.Context;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
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
import votive.com.appuaet10.Adapters.NotificationAdapter;
import votive.com.appuaet10.Beans.BlogBean;
import votive.com.appuaet10.Beans.BlogResponse;
import votive.com.appuaet10.Beans.NotificationBean;
import votive.com.appuaet10.Beans.NotificationResponse;
import votive.com.appuaet10.R;
import votive.com.appuaet10.Utilities.AppController;
import votive.com.appuaet10.Utilities.AppData;
import votive.com.appuaet10.Utilities.Constant;
import votive.com.appuaet10.Utilities.StorageUtils;
import votive.com.appuaet10.Utilities.UIUtils;
import votive.com.appuaet10.Utilities.Utils;

import static android.content.ContentValues.TAG;


public class NotificationsActivity extends AppCompatActivity {

    private Context mContext;
    private boolean mHttpRequest = false;
    private boolean mFirstTime = true;

    private int mPageIndex = 1;
    private int mPageCount = 1;

    private ArrayList<NotificationBean> mNotificationList;
    private NotificationAdapter mNotificationAdapter;
    private GridLayoutManager mLayoutManager;
    private RelativeLayout mRlNoInterConnection;
    private RecyclerView mRvNotification;
    private RelativeLayout mProgressBarLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);

        mContext = this;
        changeStatusBarColor();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_notify);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(true);
            UIUtils.setTitleWithFont(this, getSupportActionBar(), AppData.FONT_OPEN_SANS_BOLD, getString(R.string.title_activity_notification));
        }

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        init();
    }

    private void init() {
        mProgressBarLayout = (RelativeLayout) findViewById(R.id.rl_notify_progressBar);
        mRlNoInterConnection = (RelativeLayout) findViewById(R.id.rl_notify_no_internet_main);
        Button tryAgainBtn = (Button) findViewById(R.id.btn_no_internet_retry);
        tryAgainBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startHttpRequestForNotificationList();
            }
        });

        mRvNotification = (RecyclerView) findViewById(R.id.rv_notification_list);
        mLayoutManager = new GridLayoutManager(mContext, 1);
        mRvNotification.setLayoutManager(mLayoutManager);
        mNotificationList = new ArrayList<>();

        EndlessRecyclerViewScrollListener mScrollListener = new EndlessRecyclerViewScrollListener(mLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to the bottom of the list

                // loadNextDataFromApi(page);
                System.out.println("page = " + page);

                if (Utils.isConnectingToInternet(mContext)) {
                    loadNextDataFromApi(page);
                } else {
                    Toast.makeText(mContext, getString(R.string.InternetErrorMsg), Toast.LENGTH_SHORT).show();
                }
            }
        };
        // Adds the scroll listener to RecyclerView
        mRvNotification.addOnScrollListener(mScrollListener);

        mHttpRequest = false;
        startHttpRequestForNotificationList();

    }

    private void loadNextDataFromApi(int page) {
        if (mPageIndex <= mPageCount) {
            startHttpRequestForNotificationList();
        }
    }

    private void updateNotificationListView(ArrayList<NotificationBean> aNotifyList, int aPageIndex) {

        // reset notification counter to zero
        StorageUtils.putPref(getApplicationContext(), Constant.NOTIFICATION_COUNTER_VALUE_KEY, 0);

        mNotificationList.addAll(aNotifyList);
        mPageIndex++;
        mPageCount = aPageIndex;

        if (mFirstTime) {
            mNotificationAdapter = new NotificationAdapter(mContext, mNotificationList);
            mRvNotification.setAdapter(mNotificationAdapter);
            mFirstTime = false;
        } else {
            mNotificationAdapter.notifyDataSetChanged();
        }

    }

    public void startHttpRequestForNotificationList() {

        if (Utils.isConnectingToInternet(mContext)) {
            String baseUrl = Constant.API_ALL_NOTIFICATION;
            if (!mHttpRequest) {
                showProgressBar();
            }
            final String androidDeviceId = Settings.Secure.getString(mContext.getContentResolver(), Settings.Secure.ANDROID_ID);

            StringRequest strRequest = new StringRequest(Request.Method.POST, baseUrl,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            System.out.println("!!!!response = " + response);
                            try {
                                Gson gson = new GsonBuilder().create();
                                JsonParser jsonParser = new JsonParser();
                                JsonObject jsonResp = jsonParser.parse(response).getAsJsonObject();

                                NotificationResponse notifyResponse = gson.fromJson(jsonResp, NotificationResponse.class);
                                hideProgressBar();
                                if (notifyResponse != null && notifyResponse.getNotificationList() != null && notifyResponse.getNotificationList().size() > 0) {
                                    updateNotificationListView(notifyResponse.getNotificationList(), notifyResponse.getPageNo());
                                } else {
                                    hideProgressBar();
                                    UIUtils.showToast(mContext,  getResources().getString(R.string.NosearchResultFoundMsg));
                                }
                            } catch (Exception e) {
                                hideProgressBar();
                                System.out.println("!!!!response"+e);
                                UIUtils.showToast(mContext,  getResources().getString(R.string.VolleyErrorMsg));
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            hideProgressBar();
                            System.out.println("!!!!response"+error.getMessage());
                            if (error instanceof NoConnectionError) {
                                mRlNoInterConnection.setVisibility(View.VISIBLE);
                                mRvNotification.setVisibility(View.GONE);
                            } else {
                                UIUtils.showToast(mContext,  getResources().getString(R.string.VolleyErrorMsg));
                            }
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    params.put("page_no", "" + mPageIndex);
                    params.put("device_id", "20dd17043f03685a");
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
            mRvNotification.setVisibility(View.GONE);
        }
    }


    private void changeStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.StatusbarColor));
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
}
