package votive.com.appuaet10.Activities;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
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

import votive.com.appuaet10.Adapters.CategoryItemAdapter;
import votive.com.appuaet10.Beans.CategoryItemBean;
import votive.com.appuaet10.Beans.CategoryItemResponseBean;
import votive.com.appuaet10.Interface.ISubCategoryItemClickCallBack;
import votive.com.appuaet10.R;
import votive.com.appuaet10.Utilities.AppController;
import votive.com.appuaet10.Utilities.AppData;
import votive.com.appuaet10.Utilities.Constant;
import votive.com.appuaet10.Utilities.StorageUtils;
import votive.com.appuaet10.Utilities.UIUtils;
import votive.com.appuaet10.Utilities.Utils;

import static android.content.ContentValues.TAG;

public class CategoryItemListActivity extends AppCompatActivity implements ISubCategoryItemClickCallBack, NavigationView.OnNavigationItemSelectedListener {

    private boolean mIsRequestInProgress;
    private boolean mProgressBarshowing = false;

    private int strCategoryId;
    private int mScreenCallIndex;
    private String strCategoryName;

    private Context mContext;

    private RecyclerView mRvCategoryList;
    private RelativeLayout mProgressBarLayout;
    private RelativeLayout mRlNoInterConnection;
    private RelativeLayout mRlListItem;

    private ArrayList<CategoryItemBean> mCategortList;
    private SwipeRefreshLayout mSwipeView;
    private GridLayoutManager mLayoutManager;
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    private DrawerLayout mDrawer;

    private TextView mNotificationCountTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_item_list);
        mContext = this;
        changeStatusBarColor();
        init();
    }

    private void init() {

        if (getIntent() != null) {
            strCategoryId = Integer.parseInt(getIntent().getStringExtra(Constant.INTENT_CATEGORY_ITEM_ID));
            strCategoryName = getIntent().getStringExtra(Constant.INTENT_CATEGORY_NAME);
            mScreenCallIndex = getIntent().getIntExtra(Constant.SCREEN_CALL_INDEX, -1);



        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(true);
        }

        setupNavigationDrawer();

        mProgressBarLayout = (RelativeLayout) findViewById(R.id.rl_progressBar);
        mRlNoInterConnection = (RelativeLayout) findViewById(R.id.rl_category_list_no_internet_main);
        Button mTryAgainBtn = (Button) findViewById(R.id.btn_no_internet_retry);

        mRvCategoryList = (RecyclerView) findViewById(R.id.rv_category_list);
        mRvCategoryList.addItemDecoration(new ItemDecorationAlbumColumns(3, 4));
        mLayoutManager = new GridLayoutManager(mContext, 2);
        mRvCategoryList.setLayoutManager(mLayoutManager);
        mSwipeView = (SwipeRefreshLayout) findViewById(R.id.srl_category);
        mRlListItem = (RelativeLayout) findViewById(R.id.rl_category_list_main);
        mCategortList = new ArrayList<>();

        final FloatingActionButton fabUp = (FloatingActionButton) findViewById(R.id.fab_share);
        fabUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mRvCategoryList.smoothScrollToPosition(0);
                fabUp.hide();
            }
        });
        fabUp.hide();


        mTryAgainBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startHttpRequestCategoryItemList(strCategoryId);
            }
        });


        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mScreenCallIndex != -1 && mScreenCallIndex == Constant.NOTIFICATION_CATEGORY_ID_FROM_HOME) {

                    gotohome();
                } else {
                    onBackPressed();
                }
            }
        });

        String title = getString(R.string.app_name);
        if (strCategoryName != null) {
            title = strCategoryName;
        }
        UIUtils.setTitleWithFont(this, getSupportActionBar(), AppData.FONT_OPEN_SANS_BOLD, title.toUpperCase());
        mSwipeView.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mSwipeView.setRefreshing(true);
                Log.d("Swipe", "Refreshing Category");
                mIsRequestInProgress = true;
                startHttpRequestCategoryItemList(strCategoryId);
            }
        });
        mRvCategoryList.addOnScrollListener(new RecyclerView.OnScrollListener() {
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
        initBroadCastReceiver();
        startHttpRequestCategoryItemList(strCategoryId);
    }

    private void setupNavigationDrawer() {
        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        Menu m = navigationView.getMenu();
        for (int i = 0; i < m.size(); i++) {
            MenuItem mi = m.getItem(i);
            UIUtils.setMenuTitleWithFont(this, mi,
                    AppData.FONT_OPEN_SANS_BOLD, "" + mi.getTitle());
        }
    }

    private void gotohome() {

        Intent intent = new Intent(CategoryItemListActivity.this, HomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();

    }


    public void handleSideMenuClick(int aMenuIndex) {
        Intent intent = new Intent(mContext, HomeActivity.class);
        intent.putExtra(Constant.INTENT_MENU_INDEX_KEY, aMenuIndex);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }


    private void setCategoryList(ArrayList<CategoryItemBean> aCategoryList) {

        if (aCategoryList.size() > 0) {

            mRlNoInterConnection.setVisibility(View.GONE);
            mRlListItem.setVisibility(View.VISIBLE);
            mRvCategoryList.setVisibility(View.VISIBLE);
            mCategortList = aCategoryList;
            CategoryItemAdapter mCategoryItemAdapter = new CategoryItemAdapter(mContext, mCategortList, this);
            mRvCategoryList.setAdapter(mCategoryItemAdapter);
            mSwipeView.setRefreshing(false);
        }
    }


    private void changeStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.StatusbarColor));
        }
    }

    @Override
    public void categoryItem(CategoryItemBean aCategoryItemBean, int aIndex) {
        if (aCategoryItemBean.getBusinessIndex() == Constant.VALID_BUSINESS_INDEX) {
            ArrayList<CategoryItemBean> validItemList = new ArrayList<>();
            for (int i = 0; i < mCategortList.size(); i++) {
                CategoryItemBean categoryItemBean = mCategortList.get(i);
                if (categoryItemBean.getBusinessIndex() == Constant.VALID_BUSINESS_INDEX) {
                    validItemList.add(categoryItemBean);
                }
            }


            if (validItemList.get(aIndex).getCategoryListingId()==891){

                Intent intent = new Intent(mContext, WEbpage.class);
                startActivity(intent);

            }else{
                Intent intent = new Intent(mContext, CategoryDetailedItemActivity.class);
                intent.putExtra(Constant.INTENT_SELECTED_INDEX_KEY, aIndex);
                intent.putExtra(Constant.INTENT_CATEGORY_LIST, validItemList);
                intent.putExtra(Constant.INTENT_CATEGORY_ID  , ""+strCategoryId);
                intent.putExtra(Constant.INTENT_CATEGORY_NAME, ""+strCategoryName);
                startActivity(intent);
            }



        } else {
            Intent intent = new Intent(mContext, BusinessEnquiryActivity.class);
            startActivity(intent);
        }
    }

    public void startHttpRequestCategoryItemList(final int strCategoryId) {
        Log.e("CategoryItemList = ","---------->" + strCategoryId);
        if (Utils.isConnectingToInternet(mContext)) {
            mIsRequestInProgress = true;
            String baseUrl = Constant.API_CATEGORY_ITEM_LIST;
            showProgressBar();


            StringRequest strRequest = new StringRequest(Request.Method.POST, baseUrl,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            System.out.println("!!!!response = " + response);
                            if (mIsRequestInProgress) {
                                try {
                                    Gson gson = new GsonBuilder().create();
                                    JsonParser jsonParser = new JsonParser();
                                    JsonObject jsonResp = jsonParser.parse(response).getAsJsonObject();

                                    CategoryItemResponseBean categoryResponse = gson.fromJson(jsonResp, CategoryItemResponseBean.class);
                                    hideProgressBar();
                                    if (categoryResponse != null && categoryResponse.getmCategoryItemList() != null && categoryResponse.getmCategoryItemList().size() > 0) {
                                        if (categoryResponse.getmErrorCode() != null) {
                                            if (categoryResponse.getmErrorCode().equalsIgnoreCase(Constant.JSON_CATEGORY_ITEM_ERROR_CODE)) {
                                                UIUtils.showToast(mContext, categoryResponse.getmMsg());
                                            } else {
                                                UIUtils.showToast(mContext, getResources().getString(R.string.VolleyErrorMsg));
                                            }
                                        } else {
                                            if (Utils.isStatusSuccess(categoryResponse.getmStatus())) {
                                                mRlNoInterConnection.setVisibility(View.GONE);
                                                mRlListItem.setVisibility(View.VISIBLE);
                                                setCategoryList(categoryResponse.getmCategoryItemList());
                                            } else {
                                                UIUtils.showToast(mContext, categoryResponse.getmMsg());
                                            }

                                            mIsRequestInProgress = false;
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
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            if (mIsRequestInProgress) {
                                hideProgressBar();
                                if (error instanceof NoConnectionError) {
                                    mRlNoInterConnection.setVisibility(View.VISIBLE);
                                    mRlListItem.setVisibility(View.GONE);
                                    mRvCategoryList.setVisibility(View.GONE);
                                    //UIUtils.showToast(mContext, getString(R.string.InternetErrorMsg));
                                } else {
                                    UIUtils.showToast(mContext, getResources().getString(R.string.VolleyErrorMsg));
                                }
                            }
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    params.put(Constant.CATEGORY_ITEM_ID_PARAMS, "" + strCategoryId);
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
            mRlListItem.setVisibility(View.GONE);
            mRvCategoryList.setVisibility(View.GONE);

        }
    }

    private void hideProgressBar() {
        mIsRequestInProgress = false;
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
    public void onBackPressed() {

        if (mDrawer.isDrawerOpen(GravityCompat.END)) {
            mDrawer.closeDrawer(GravityCompat.END);
        } else {
            finish();
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
                    AppData.getInstance().showDialog(CategoryItemListActivity.this, title, message, imageUrl, type, id);

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
        if (!TextUtils.isEmpty(regId)) {
            System.out.println("regId = " + regId);
        } else {
            System.out.println("regId =" + "Firebase Reg Id is not received yet!");
        }
    }


    @Override
    protected void onResume() {
        super.onResume();

        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Constant.REGISTRATION_COMPLETE));

        // register new push message receiver
        // by doing this, the activity will be notified each time a new message arrives
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Constant.PUSH_NOTIFICATION));

        if (mNotificationCountTv != null) {
            updateNotificationValue();
        }
    }

    private void updateNotificationValue() {
        if (mNotificationCountTv != null){
            int count = StorageUtils.getPrefForCount(mContext, Constant.NOTIFICATION_COUNTER_VALUE_KEY);
            if (count == 0) {
                mNotificationCountTv.setText(" " + count + " ");
                mNotificationCountTv.setVisibility(View.GONE);
            } else {
                mNotificationCountTv.setText(" " + count + " ");
                mNotificationCountTv.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        for (int i = 0; i < menu.size(); i++) {
            menu.getItem(i).setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        }

        final Menu m = menu;
        final MenuItem item = menu.findItem(R.id.action_notification);

        mNotificationCountTv = (TextView) item.getActionView().findViewById(R.id.tv_notification_count);
        if (mNotificationCountTv != null) {
            mNotificationCountTv.setVisibility(View.GONE);
        }

        item.getActionView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                m.performIdentifierAction(item.getItemId(), 0);
            }
        });

        updateNotificationValue();
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (id) {

            case R.id.action_toggle:
                if (mDrawer.isDrawerOpen(GravityCompat.END)) {
                    mDrawer.closeDrawer(GravityCompat.END);
                } else {
                    mDrawer.openDrawer(GravityCompat.END);
                }
                break;

            case R.id.action_notification:
                startActivity(new Intent(mContext, NotificationsActivity.class));
        }
        return true;
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        //TODO get selected fragment index for side menu
        int bottonMenuIndexPosition = -1;

        switch (id) {
            case R.id.nav_home:
                handleSideMenuClick(Constant.MENU_OPTION_HOME_INDEX);
                break;

            case R.id.nav_offers:

                handleSideMenuClick(Constant.MENU_OPTION_OFFER_INDEX);

                break;

            case R.id.nav_blog:
                handleSideMenuClick(Constant.MENU_OPTION_BLOG_INDEX);

                break;

            case R.id.nav_category:

                handleSideMenuClick(Constant.MENU_OPTION_CATEGORY_INDEX);

                break;

            case R.id.nav_contact:
                handleSideMenuClick(Constant.MENU_OPTION_CONTACT_US_INDEX);

                break;

            case R.id.nav_suggestion:

                handleSideMenuClick(Constant.MENU_OPTION_SUGGESTION_INDEX);
                break;

            case R.id.nav_privacy_policy:

                handleSideMenuClick(Constant.MENU_OPTION_PRIVACY_POLICY_INDEX);

                break;

            case R.id.nav_terms_conditions:

                handleSideMenuClick(Constant.MENU_OPTION_TERMS_AND_CONDITION_INDEX);

                break;

            case R.id.nav_copyright:
                handleSideMenuClick(Constant.MENU_OPTION_COPY_RIGHT_INDEX);

                break;

            case R.id.nav_about:

                handleSideMenuClick(Constant.MENU_OPTION_ABOUT_US_INDEX);

                break;

            case R.id.nav_share:
                handleSideMenuClick(Constant.MENU_OPTION_SOCIAL_SHARE_INDEX);
                break;

            case R.id.nav_play_video:
                AppData.getInstance().handleVideoPlay(mContext);
                break;

        }
        mDrawer.closeDrawer(GravityCompat.END);
        return true;
    }
}
