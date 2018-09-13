package votive.com.appuaet10.Activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
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
import java.util.List;
import java.util.Map;

import votive.com.appuaet10.Beans.CategoryItemBean;
import votive.com.appuaet10.Beans.CategoryItemResponseBean;
import votive.com.appuaet10.R;
import votive.com.appuaet10.Utilities.AppController;
import votive.com.appuaet10.Utilities.AppData;
import votive.com.appuaet10.Utilities.Constant;
import votive.com.appuaet10.Utilities.StorageUtils;
import votive.com.appuaet10.Utilities.UIUtils;
import votive.com.appuaet10.Utilities.Utils;

import static android.content.ContentValues.TAG;

public class CategoryDetailedItemActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    Context mContext;
    private int mSelectedImageIndex;
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    private ArrayList<CategoryItemBean> mCategoryList;
    private ViewPager mBusinessViewPager;
    private DrawerLayout mDrawer;
    public static String mCategoryid , mCategoryname;
    private boolean mIsRequestInProgress;
    private boolean mProgressBarshowing = false;
    private RelativeLayout mProgressBarLayout;

    public boolean from = false;
    private TextView mNotificationCountTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_detailed_item);
        mContext = this;
        changeStatusBarColor();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        String title = getString(R.string.app_name);
        UIUtils.setTitleWithFont(this, getSupportActionBar(), AppData.FONT_OPEN_SANS_BOLD, title.toUpperCase());
        setupNavigationDrawer();

        mProgressBarLayout = (RelativeLayout) findViewById(R.id.rl_progressBar);
        mBusinessViewPager = (ViewPager) findViewById(R.id.vp_business_details);

        mSelectedImageIndex = 0;
        if (getIntent().getAction() != null) {
            mCategoryList = new ArrayList<CategoryItemBean>();
            String data = getIntent().getDataString();
            Log.e("data","------->"+ data);
            ArrayList<String>  list1 = getQuery(data);
            mCategoryid= list1.get(0);
            mCategoryname = list1.get(1);
            mSelectedImageIndex= Integer.parseInt(list1.get(2));
            Log.e("mCategoryid","------->"+ mCategoryid);
            Log.e("mCategoryname","------->"+ mCategoryname);
            Log.e("mSelectedImageIndex","------->"+ mSelectedImageIndex);


            startHttpRequestCategoryItemList(Integer.parseInt(mCategoryid));
            from=true;
        }
       else if (getIntent() != null) {
            mSelectedImageIndex = getIntent().getIntExtra(Constant.INTENT_SELECTED_INDEX_KEY, 0);
            mCategoryList = getIntent().getParcelableArrayListExtra(Constant.INTENT_CATEGORY_LIST);
            mCategoryid = getIntent().getStringExtra(Constant.INTENT_CATEGORY_ID);
            mCategoryname = getIntent().getStringExtra(Constant.INTENT_CATEGORY_NAME);

            Log.e("mCategoryid","------->"+ mCategoryid);
            Log.e("mCategoryname","------->"+ mCategoryname);
            Log.e("getCategoryListingId","------->"+ mCategoryList.get(mSelectedImageIndex).getCategoryListingId());


            set_adapter_method();
            from=false;
        }


        mBusinessViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                //System.out.println("position = " + position);
            }

            @Override
            public void onPageSelected(int position) {
                mSelectedImageIndex = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        initBroadCastReceiver();
    }

    private void set_adapter_method() {
        Log.e("mCategoryList","----->"+mCategoryList.size());
        if (mCategoryList != null && mCategoryList.size() > 0) {
            SwipePagerAdapter adapter = new SwipePagerAdapter(getSupportFragmentManager(),mCategoryList);
            mBusinessViewPager.setAdapter(adapter);
            mBusinessViewPager.setCurrentItem(mSelectedImageIndex);
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
                    System.out.println("???message = " + message);
                    // UIUtils.AlertMsgWithOk(HomeActivity.this, title, message, R.drawable.ic_home_logo);

                    AppData.getInstance().showDialog(CategoryDetailedItemActivity.this, title, message, imageUrl, type, id);

                }
            }
        };
        displayFirebaseRegId();
    }

    private void changeStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.StatusbarColor));
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (from){
//            handleSideMenuClick(Constant.MENU_OPTION_HOME_INDEX);
            Intent intent = new Intent(mContext, CategoryItemListActivity.class);
            intent.putExtra(Constant.INTENT_CATEGORY_ITEM_ID, mCategoryid);
            intent.putExtra(Constant.INTENT_CATEGORY_NAME, mCategoryname);
            intent.putExtra(Constant.SCREEN_CALL_INDEX, Constant.NOTIFICATION_CATEGORY_ID_FROM_HOME);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }else{
          finish();
        }
    }

    public void handleSideMenuClick(int aMenuIndex) {
        Intent intent = new Intent(mContext, HomeActivity.class);
        intent.putExtra(Constant.INTENT_MENU_INDEX_KEY, aMenuIndex);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
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
    protected void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
    }

    public static class SwipePagerAdapter extends FragmentPagerAdapter {

        private ArrayList<CategoryItemBean> mItemList;

        public SwipePagerAdapter(FragmentManager fragmentManager,
                                 ArrayList<CategoryItemBean> aCategoryItemList) {

            super(fragmentManager);
            mItemList = aCategoryItemList;
        }

        // Returns total number of pages
        @Override
        public int getCount() {
            return mItemList.size();
        }

        // Returns the fragment to display for that page
        @Override
        public Fragment getItem(int position) {
            //System.gc();
            return BusinessDetailsFragment.newInstance(position, mItemList, mCategoryid , mCategoryname);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            super.destroyItem(container, position, object);
           /* Log.d("DESTROY", "destroying view at position " + position);
            View view = (View) object;
            ((ViewPager) container).removeView(view);
            view = null;*/
        }
    }

    public void setSelectedItem(int aIndex) {
        if (aIndex > 0 && aIndex <= mCategoryList.size()){
            mSelectedImageIndex = aIndex;
            mBusinessViewPager.setCurrentItem(mSelectedImageIndex);
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

        // get selected fragment index for side menu
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

    private ArrayList<String> getQuery(String query) {
        String[] params = query.split("&");
        Map<String, String> map = new HashMap<String, String>();
        ArrayList<String> list = new ArrayList<String>();
        for (String param : params)
        {
            try {
                String name = param.split("=")[0];
                String value = param.split("=")[1];
                Log.e("getQuery","name--->"+ name + " & value--->"+ value);
                map.put(name, value);
                list.add(value);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return list;
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
//                                                mRlNoInterConnection.setVisibility(View.GONE);
//                                                mRlListItem.setVisibility(View.VISIBLE);
//                                                setCategoryList(categoryResponse.getmCategoryItemList());

                                                    ArrayList<CategoryItemBean> validItemList = new ArrayList<>();
                                                    for (int i = 0; i < categoryResponse.getmCategoryItemList().size(); i++) {
                                                        CategoryItemBean categoryItemBean = categoryResponse.getmCategoryItemList().get(i);
                                                        if (categoryItemBean.getBusinessIndex() == Constant.VALID_BUSINESS_INDEX) {
                                                            validItemList.add(categoryItemBean);
                                                        }
                                                    }

                                                mCategoryList=validItemList;
                                                set_adapter_method();
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
//                                    mRlNoInterConnection.setVisibility(View.VISIBLE);
//                                    mRlListItem.setVisibility(View.GONE);
//                                    mRvCategoryList.setVisibility(View.GONE);
                                    UIUtils.showToast(mContext, getString(R.string.InternetErrorMsg));
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
//            mRlNoInterConnection.setVisibility(View.VISIBLE);
//            mRlListItem.setVisibility(View.GONE);
//            mRvCategoryList.setVisibility(View.GONE);
            UIUtils.showToast(mContext, getString(R.string.InternetErrorMsg));

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

}
