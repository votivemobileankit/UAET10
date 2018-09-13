package votive.com.appuaet10.Activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
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
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.HashMap;
import java.util.Map;

import votive.com.appuaet10.Beans.BlogBean;
import votive.com.appuaet10.Beans.BlogDetailBean;
import votive.com.appuaet10.R;
import votive.com.appuaet10.Utilities.AppController;
import votive.com.appuaet10.Utilities.AppData;
import votive.com.appuaet10.Utilities.Constant;
import votive.com.appuaet10.Utilities.StorageUtils;
import votive.com.appuaet10.Utilities.UIUtils;
import votive.com.appuaet10.Utilities.Utils;

import static android.content.ContentValues.TAG;

public class BlogDetailActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private Context mContext;

    private RelativeLayout mProgressBarLayout;
    private RelativeLayout mRlNoInterConnection;
    private RelativeLayout mDataLayout;
    private boolean mProgressBarshowing = false;
    private FloatingActionButton mGoToTopBtn;
    private BlogBean mSelectedBlogBean;
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    private DrawerLayout mDrawer;
    private NestedScrollView mScrollView;
    private TextView mTitleTv;
    private TextView mDescTv;
    private SimpleDraweeView mSiv;
    private BlogDetailBean mBlogDetailBean;
    private Button mShareLinkBtn;
    private Button mTryAgainBtn;

    private TextView mNotificationCountTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blog_detail);
        mGoToTopBtn = (FloatingActionButton) findViewById(R.id.fab_blog_details);
        mGoToTopBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mScrollView.smoothScrollTo(0, 0);
                mGoToTopBtn.hide();
            }
        });
        mContext = this;
        changeStatusBarColor();
        init();
        initBroadCastReceiver();
        startHttpRequestForBlogDetail();
    }

    private void init() {

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(true);
            UIUtils.setTitleWithFont(this, getSupportActionBar(), AppData.FONT_OPEN_SANS_BOLD, getString(R.string.app_name));
        }

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        if (getIntent() != null) {
            mSelectedBlogBean = getIntent().getParcelableExtra(Constant.INTENT_BLOG_DETAILS_BEAN_KEY);
        } else {
            finish();
        }
        mProgressBarLayout = (RelativeLayout) findViewById(R.id.rl_progressBar);
        mRlNoInterConnection = (RelativeLayout) findViewById(R.id.rl_blog_no_internet_main);
        mDataLayout = (RelativeLayout) findViewById(R.id.rl_blog_detail_list);
        mScrollView = (NestedScrollView) findViewById(R.id.sv_blog_details);
        mTitleTv = (TextView) findViewById(R.id.tv_blog_detail_title);
        mDescTv = (TextView) findViewById(R.id.tv_blog_detail_desc);
        mSiv = (SimpleDraweeView) findViewById(R.id.sdv_blog_details);
        mTryAgainBtn = (Button) findViewById(R.id.btn_no_internet_retry);
        mTryAgainBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startHttpRequestForBlogDetail();
            }
        });


        mGoToTopBtn.hide();
        mShareLinkBtn = (Button) findViewById(R.id.btn_blog_detail_share);


        mShareLinkBtn.setTypeface(AppData.getInstance().getTypeFaceForType(this, AppData.FONT_OPEN_SANS_BOLD));
        setupNavigationDrawer();

        hideProgressBar();
        mDataLayout.setVisibility(View.GONE);
        mRlNoInterConnection.setVisibility(View.GONE);
        startHttpRequestForBlogDetail();

        if (mScrollView != null) {
            mScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
                @Override
                public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {

                    System.out.println("dy = " + scrollY);
                    if (scrollY > 0 || scrollY < 0 && !mGoToTopBtn.isShown()) {
                        mGoToTopBtn.show();
                    } else {
                        mGoToTopBtn.show();
                    }

                    if (scrollY == 0) {
                        mGoToTopBtn.hide();
                    }
                }
            });
        }
    }

    private void handleShareClick() {
        Uri shareLink = Uri.parse(mSelectedBlogBean.getLink());
        final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
        String body = "UAE Top 10 \n" + shareLink + " \n" + "Explore the Gems, Download the app \n " + "http://play.google.com/store/apps/details?id=" + appPackageName;
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "UAE Top 10");
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, body);
        startActivity(Intent.createChooser(sharingIntent, "Share via"));
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

    public void startHttpRequestForBlogDetail() {
        if (Utils.isConnectingToInternet(mContext)) {
            showProgressBar();
            String baseUrl = Constant.API_BLOG_DETAILS;
            StringRequest strRequest = new StringRequest(Request.Method.POST, baseUrl,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            System.out.println("!!!!response = " + response);
                            try {
                                Gson gson = new GsonBuilder().create();
                                JsonParser jsonParser = new JsonParser();
                                JsonObject jsonResp = jsonParser.parse(response).getAsJsonObject();

                                BlogDetailBean blogResponse = gson.fromJson(jsonResp, BlogDetailBean.class);
                                hideProgressBar();
                                if (blogResponse != null && Utils.isStatusSuccess(blogResponse.getStatus())) {
                                    updateBlogDetailsList(blogResponse);
                                } else {
                                    hideProgressBar();
                                    UIUtils.showToast(mContext, getString(R.string.BlogListMsg));
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
                                mDataLayout.setVisibility(View.GONE);
                            } else {
                                UIUtils.showToast(mContext, getString(R.string.VolleyErrorMsg));
                            }
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    params.put(Constant.BLOG_DETAILS_ID_KEY, "" + mSelectedBlogBean.getmBlogId());
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
            hideProgressBar();
            mRlNoInterConnection.setVisibility(View.VISIBLE);
            mDataLayout.setVisibility(View.GONE);
        }
    }

    private void updateBlogDetailsList(BlogDetailBean aBlogResponse) {

        mBlogDetailBean = aBlogResponse;

        hideProgressBar();
        mRlNoInterConnection.setVisibility(View.GONE);
        mDataLayout.setVisibility(View.VISIBLE);

        if (mSelectedBlogBean.getmBlogName() != null && mSelectedBlogBean.getmBlogName().length() > 1) {
            mTitleTv.setText(mSelectedBlogBean.getmBlogName());
            mTitleTv.setVisibility(View.VISIBLE);
        } else {
            mTitleTv.setVisibility(View.GONE);
        }

        if (mSelectedBlogBean.getmBlogImageUrl() != null && mSelectedBlogBean.getmBlogImageUrl().length() > 1) {
            mSiv.setImageURI(Uri.parse(mSelectedBlogBean.getmBlogImageUrl()));
            mSiv.setVisibility(View.VISIBLE);
        } else {
            mSiv.setVisibility(View.GONE);
        }

        if (mBlogDetailBean.getDesc() != null && mBlogDetailBean.getDesc().length() > 1) {
            mDescTv.setText(mBlogDetailBean.getDesc());
            mDescTv.setVisibility(View.VISIBLE);
        } else {
            mDescTv.setVisibility(View.GONE);
        }

        if (mSelectedBlogBean.getLink() != null
                && !mSelectedBlogBean.getLink().isEmpty()) {

            mShareLinkBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    handleShareClick();
                }
            });
        } else {
            mShareLinkBtn.setVisibility(View.GONE);
        }
    }

    public void handleSideMenuClick(int aMenuIndex) {
        Intent intent = new Intent(mContext, HomeActivity.class);
        intent.putExtra(Constant.INTENT_MENU_INDEX_KEY, aMenuIndex);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
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

                    AppData.getInstance().showDialog(BlogDetailActivity.this, title, message, imageUrl, type, id);

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
}
