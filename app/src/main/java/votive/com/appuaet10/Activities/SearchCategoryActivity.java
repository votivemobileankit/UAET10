package votive.com.appuaet10.Activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
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

import votive.com.appuaet10.Adapters.SearchHistoryAdapter;
import votive.com.appuaet10.Adapters.SearchItemAdapter;
import votive.com.appuaet10.Beans.SearchBean;
import votive.com.appuaet10.Beans.SearchResponseBean;
import votive.com.appuaet10.Interface.ISearchCategory;
import votive.com.appuaet10.R;
import votive.com.appuaet10.Utilities.AppController;
import votive.com.appuaet10.Utilities.AppData;
import votive.com.appuaet10.Utilities.Constant;
import votive.com.appuaet10.Utilities.UIUtils;
import votive.com.appuaet10.Utilities.UserUtils;
import votive.com.appuaet10.Utilities.Utils;

import static android.content.ContentValues.TAG;

public class SearchCategoryActivity extends AppCompatActivity implements ISearchCategory {


    private RelativeLayout mAutoSearchRlMain;
    private RecyclerView mAutoSearchRv;
    private RelativeLayout mRecentSearchRl;
    private RecyclerView mRecentSearchRv;

    private SearchItemAdapter mAutoSearchAdapter;

    private SearchHistoryAdapter mHistoryAdapter;
    private ArrayList<SearchBean> mAutoSearchItemList;
    private ArrayList<SearchBean> mRecentSearchList;
    private RelativeLayout mRlNoInterConnection;
    private Button mTryAgainBtn;
    private Context mContext;
    private String strSearchText;
    private boolean mProgressBarshowing = false;
    private boolean mHttpRequest = false;
    private RelativeLayout mProgressBarLayout;
    private BroadcastReceiver mRegistrationBroadcastReceiver;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_category);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        changeStatusBarColor();
        initBroadCastReceiver();
        initCategorySearch();

    }

    private void initCategorySearch() {

        mContext = this;

        mAutoSearchRlMain = (RelativeLayout) findViewById(R.id.rl_search_auto_search_main);
        mAutoSearchRlMain.setVisibility(View.GONE);
        mAutoSearchRv = (RecyclerView) findViewById(R.id.rv_search_auto_search);
        mProgressBarLayout = (RelativeLayout) findViewById(R.id.rl_progressBar);

        mRecentSearchRl = (RelativeLayout) findViewById(R.id.rl_search_recent_search_main);
        mRecentSearchRl.setVisibility(View.VISIBLE);
        mRecentSearchRv = (RecyclerView) findViewById(R.id.rv_search_recent_search);

        setRecentSearchList();
        mAutoSearchItemList = new ArrayList<>();
        mRecentSearchList = new ArrayList<>();
        TextView mClearHistoryTv = (TextView) findViewById(R.id.tv_search_recent_search_clear_history);
        mClearHistoryTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleClearHistory();
            }
        });


        mRlNoInterConnection = (RelativeLayout) findViewById(R.id.rl_search_internet_main);
        mTryAgainBtn = (Button) findViewById(R.id.btn_no_internet_retry);
        mTryAgainBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                loadSuggestion(strSearchText);

            }
        });
    }

    private void setRecentSearchList() {
        mRecentSearchList = UserUtils.getInstance().getRecentSearchList(this);
        if (mRecentSearchList != null && mRecentSearchList.size() > 0) {
            mRecentSearchRl.setVisibility(View.VISIBLE);
            mHistoryAdapter = new SearchHistoryAdapter(mRecentSearchList, this);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
            mRecentSearchRv.setLayoutManager(mLayoutManager);
            mRecentSearchRv.setItemAnimator(new DefaultItemAnimator());
            mRecentSearchRv.setAdapter(mHistoryAdapter);
        } else {
            mRecentSearchRl.setVisibility(View.GONE);

        }
    }

    private void handleClearHistory() {
        UserUtils.getInstance().clearRecentSearch(this);
        mRecentSearchList.clear();
        mRecentSearchRl.setVisibility(View.GONE);
    }

    private void loadSuggestion(String aTextStr) {
        strSearchText = aTextStr;
        startHttpRequestForSearchList(strSearchText);
    }

    private void handleAutoSearchList(ArrayList<SearchBean> aItemList) {
        if (aItemList != null && aItemList.size() > 0) {
            mAutoSearchRlMain.setVisibility(View.VISIBLE);
            mAutoSearchItemList = aItemList;
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mContext);
            mAutoSearchRv.setLayoutManager(mLayoutManager);
            mAutoSearchRv.setItemAnimator(new DefaultItemAnimator());
            mAutoSearchAdapter = new SearchItemAdapter(mAutoSearchItemList, this);
            mAutoSearchRv.setAdapter(mAutoSearchAdapter);
        } else {
            mAutoSearchItemList.clear();
            mAutoSearchRlMain.setVisibility(View.GONE);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.search_menu, menu);
        MenuItem searchViewItem = menu.findItem(R.id.action_search);

        final SearchView searchViewAndroidActionBar = (SearchView) MenuItemCompat.getActionView(searchViewItem);
        searchViewAndroidActionBar.setIconified(false);
        searchViewAndroidActionBar.setMaxWidth(Integer.MAX_VALUE);
        searchViewAndroidActionBar.setQueryHint(getString(R.string.SearchHintText));

        EditText searchEditText = (EditText) searchViewAndroidActionBar.findViewById(android.support.v7.appcompat.R.id.search_src_text);

        if (searchEditText != null) {
            searchEditText.setTypeface(AppData.getInstance().getTypeFaceForType(this, AppData.FONT_OPEN_SANS_REGULAR));
        }

        searchViewAndroidActionBar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchViewAndroidActionBar.clearFocus();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.length() > Constant.SEARCH_MIN_CHAR) {
                    loadSuggestion(newText);
                } else {
                    mAutoSearchItemList.clear();
                    mAutoSearchRlMain.setVisibility(View.GONE);
                    mRecentSearchRl.setVisibility(View.GONE);
                    mRlNoInterConnection.setVisibility(View.GONE);
                }
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                handleBackButton();
                break;

        }

        return super.onOptionsItemSelected(item);
    }


    private void handleBackButton() {
        finish();
    }


    public void startHttpRequestForSearchList(final String aTextStr) {

        if (Utils.isConnectingToInternet(this)) {
            String baseUrl = Constant.API_SEARCH_LIST;

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

                                SearchResponseBean blogResponse = gson.fromJson(jsonResp, SearchResponseBean.class);
                                hideProgressBar();

                                if (blogResponse != null && blogResponse.getmSearchList() != null && blogResponse.getmSearchList().size() > 0) {
                                    handleAutoSearchList(blogResponse.getmSearchList());
                                } else {
                                    hideProgressBar();
                                    //UIUtils.showToast(mContext, getString(R.string.NosearchResultFoundMsg));
                                }
                            } catch (Exception e) {
                                hideProgressBar();

                                UIUtils.showToast(mContext, getString(R.string.VolleyErrorMsg));
                            }

                            hideProgressBar();
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            if (error instanceof NoConnectionError) {
                                mRlNoInterConnection.setVisibility(View.VISIBLE);
                                mAutoSearchRlMain.setVisibility(View.GONE);
                                mRecentSearchRl.setVisibility(View.GONE);
                            } else {
                                UIUtils.showToast(mContext, getString(R.string.VolleyErrorMsg));
                            }
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    params.put(Constant.JSON_SEARCH_KEY, aTextStr);
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
            mAutoSearchRlMain.setVisibility(View.GONE);
            mRecentSearchRl.setVisibility(View.GONE);
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

    @Override
    public void searchItemClicked(int aPosition, SearchBean aSearchBean) {
        UserUtils.getInstance().addRecentSearch(this, aSearchBean);
        Log.e("searchItemClicked","getmSearchType-->"+aSearchBean.getmSearchType());
        if (aSearchBean.getmSearchType() == Constant.SEARCH_TYPE_CATEGORY) {
            Intent intent = new Intent(mContext, CategoryItemListActivity.class);
            intent.putExtra(Constant.INTENT_CATEGORY_ITEM_ID, String.valueOf(aSearchBean.getStrSearchId()));
            intent.putExtra(Constant.INTENT_CATEGORY_NAME, aSearchBean.getmSearchLabel());
            startActivity(intent);
        } else {
            Intent intent = new Intent(this, BusinessDetailedBySearchActivity.class);
            intent.putExtra(Constant.INTENT_BUSINESS_ID, String.valueOf(aSearchBean.getStrSearchId()));
            startActivity(intent);
        }
    }

    @Override
    public void historyItemClicked(int aPosition, SearchBean aSearchBean) {
        UserUtils.getInstance().addRecentSearch(this, aSearchBean);
        if (aSearchBean.getmSearchType() == Constant.SEARCH_TYPE_CATEGORY) {
            Intent intent = new Intent(mContext, CategoryItemListActivity.class);
            intent.putExtra(Constant.INTENT_CATEGORY_ITEM_ID, String.valueOf(aSearchBean.getStrSearchId()));
            intent.putExtra(Constant.INTENT_CATEGORY_NAME, aSearchBean.getmSearchLabel());
            startActivity(intent);
        } else {
            Intent intent = new Intent(this, BusinessDetailedBySearchActivity.class);
            intent.putExtra(Constant.INTENT_BUSINESS_ID, String.valueOf(aSearchBean.getStrSearchId()));
            startActivity(intent);
        }
    }


    private void initBroadCastReceiver() {

        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                // checking for type intent filter
                if (intent.getAction().equals(Constant.REGISTRATION_COMPLETE)) {
                    // gcm successfully registered
                    // now subscribe to `global` topic to receive app wide notifications
                    displayFirebaseRegId();

                } else if (intent.getAction().equals(Constant.PUSH_NOTIFICATION)) {
                    // new push notification is received

                    String message = intent.getStringExtra(Constant.INTENT_NOTIFICATION_MSG);
                    String type = intent.getStringExtra(Constant.INTENT_NOTIFICATION_TYPE);
                    String id = intent.getStringExtra(Constant.INTENT_NOTIFICATION_ID);
                    String imageUrl = intent.getStringExtra(Constant.INTENT_NOTIFICATION_IMAGE_URL);
                    String title = intent.getStringExtra(Constant.INTENT_NOTIFICATION_TITLE);
                    System.out.println("???message = " + message);
                    // UIUtils.AlertMsgWithOk(HomeActivity.this, title, message, R.drawable.ic_home_logo);

                    AppData.getInstance().showDialog(SearchCategoryActivity.this, title, message, imageUrl, type, id);

                }
            }
        };
        displayFirebaseRegId();
    }


    @Override
    protected void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);

    }

    private void displayFirebaseRegId() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences(Constant.SHARED_PREF_FIREBASE_KEY, 0);
        String regId = pref.getString("regId", null);

        System.out.println("????regId = " + regId);

        if (!TextUtils.isEmpty(regId)) {

            System.out.println("regId = " + regId);
        } else {
            System.out.println("regId =" + "Firebase Reg Id is not received yet!");

        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        setRecentSearchList();

        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Constant.REGISTRATION_COMPLETE));

        // register new push message receiver
        // by doing this, the activity will be notified each time a new message arrives
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Constant.PUSH_NOTIFICATION));

    }

}
