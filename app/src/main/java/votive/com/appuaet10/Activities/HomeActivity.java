package votive.com.appuaet10.Activities;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.HashMap;
import java.util.Map;

import votive.com.appuaet10.Beans.NotificationResponseBean;
import votive.com.appuaet10.R;
import votive.com.appuaet10.Utilities.AppController;
import votive.com.appuaet10.Utilities.AppData;
import votive.com.appuaet10.Utilities.Constant;
import votive.com.appuaet10.Utilities.StorageUtils;
import votive.com.appuaet10.Utilities.UIUtils;
import votive.com.appuaet10.Utilities.Utils;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, SearchView.OnQueryTextListener {
    private int[] mLayoutArr = {
            R.id.rl_bottom_menu_first_item,
            R.id.rl_bottom_menu_second_item,
            R.id.rl_bottom_menu_third_item,
            R.id.rl_bottom_menu_fourth_item,
            R.id.rl_bottom_menu_fifth_item};

    private int[] mTextViewArr = {
            R.id.tv_bottom_menu_first,
            R.id.tv_bottom_menu_second,
            R.id.tv_bottom_menu_third,
            R.id.tv_bottom_menu_fourth,
            R.id.tv_bottom_menu_five,
    };

    private int[] mImageViewArr = {
            R.id.iv_bottom_menu_first,
            R.id.iv_bottom_menu_second,
            R.id.iv_bottom_menu_third,
            R.id.iv_bottom_menu_fourth,
            R.id.iv_bottom_menu_five
    };


    public static final int[] BOTTOM_MENU_ICON_ID_ARR = {
            R.drawable.ic_home,
            R.drawable.ic_offers,
            R.drawable.ic_blogs,
            R.drawable.ic_category,
            R.drawable.ic_contact_us};

    public static final int[] SELECTED_BOTTOM_MENU_ICON_ID_ARR = {
            R.drawable.ic_yellow_home,
            R.drawable.ic_yellow_offers,
            R.drawable.ic_yellow_blogs,
            R.drawable.ic_yellow_category,
            R.drawable.ic_yellow_contact_us};

    private Context mContext;
    private DrawerLayout mDrawer;

    private String[] mTitleArr;
    private int mSelectedFragmentMenuItemId;
    private HomeContentFragment mHomeMainFragment;
    private int mSelectedIndex;
    private int mSelectedBottomMenuIndex;
    private int BackCount = 0;
    // to change action bar search icon button with Title Name
    private boolean mHomeWithText = true;
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    private int offerId;
    private int mScreenCallIndex;

    private TextView mNotificationCountTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_home);
        mContext = this;
        changeStatusBarColor();
        init();

    }

    private void init() {

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        if (getIntent() != null) {
            mSelectedIndex = getIntent().getIntExtra(Constant.INTENT_MENU_INDEX_KEY, Constant.MENU_OPTION_HOME_INDEX);
            if (getIntent().hasExtra(Constant.INTENT_OFFER_ID)){
                offerId = getIntent().getIntExtra(Constant.INTENT_OFFER_ID, -1);
                mScreenCallIndex = getIntent().getIntExtra(Constant.SCREEN_CALL_INDEX, -1);
            }
        }

        if (mSelectedIndex==Constant.MENU_OPTION_HOME_INDEX){
            handleCompanyListPopup();
        }

        mSelectedBottomMenuIndex = -1;
        mTitleArr = getResources().getStringArray(R.array.bottom_menu);

        if (mHomeWithText) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            UIUtils.setTitleWithFont(this, getSupportActionBar(), AppData.FONT_OPEN_SANS_BOLD, "   " + getString(R.string.app_name));
        }

        initBroadCastReceiver();
        initBottomMenu();
        setupNavigationDrawer();

        if (!StorageUtils.getPrefForBool(mContext, Constant.SHARED_PREF_WELCOME_PUSH_KEY)){
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    SharedPreferences pref = getApplicationContext().getSharedPreferences(Constant.SHARED_PREF_FIREBASE_KEY, 0);
                    final String regId = pref.getString("regId", null);
                    startHttpRequestForWelcomeNotification(regId);
                }
            }, 5000);
        }
    }

    private void handleCompanyListPopup() {

        boolean server_popup_flag = AppData.getInstance().get_Popup_flag(mContext);

        if (server_popup_flag) {
            boolean sshowPopUp = AppData.getInstance().getFirstLaunch(mContext);
            if (!sshowPopUp) {
//                showCompanyListPopup();

//                    showcarwinpopup();

            }
        }
    }



    private void showcarwinpopup() {

        final Dialog openDialog = new Dialog(mContext);
        openDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        openDialog.setContentView(R.layout.win_car_popup_dialog);
        openDialog.setCancelable(false);
        openDialog.setCanceledOnTouchOutside(false);
        openDialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
        openDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        Button dialogImage = (Button) openDialog.findViewById(R.id.imageView_custom_dialog_close);
        dialogImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog.dismiss();
            }
        });


        RelativeLayout btnShowVideos1 = (RelativeLayout) openDialog.findViewById(R.id.carbackground);
        btnShowVideos1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                openDialog.dismiss();

                Intent intentB = new Intent(mContext, BusinessDetailedBySearchActivity.class);
                //  Win A car category id is harcoded here
                intentB.putExtra(Constant.INTENT_BUSINESS_ID, "891");
                startActivity(intentB);

            }
        });
        openDialog.show();
    }



    private void showCompanyListPopup() {

        final Dialog openDialog = new Dialog(mContext);
        openDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        openDialog.setContentView(R.layout.first_launch_popup_dialog);
        openDialog.setCancelable(false);
        openDialog.setCanceledOnTouchOutside(false);
        openDialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
        openDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        Button dialogImage = (Button) openDialog.findViewById(R.id.imageView_custom_dialog_close);
        dialogImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog.dismiss();
            }
        });

        RelativeLayout btnShowVideos = (RelativeLayout) openDialog.findViewById(R.id.rl_click);
        btnShowVideos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                openDialog.dismiss();

                Intent intent = new Intent(mContext, CompanyListActivity.class);
                startActivity(intent);
            }
        });
        RelativeLayout btnShowVideos1 = (RelativeLayout) openDialog.findViewById(R.id.rl_later);
        btnShowVideos1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                openDialog.dismiss();
            }
        });
        openDialog.show();
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

                    AppData.getInstance().showDialog(HomeActivity.this, title, message, imageUrl, type, id);

                }
            }
        };
        displayFirebaseRegId();
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

        loadHomeMenu(); // default loading fragment
    }

    @Override
    public void onBackPressed() {

        if (mDrawer.isDrawerOpen(GravityCompat.END)) {
            mDrawer.closeDrawer(GravityCompat.END);
        } else {
            showExitAlertDialog();
        }
    }

    public void loadHomeMenu() {

        if (mSelectedIndex > 6) {
            mSelectedBottomMenuIndex = -1;
        }
        handleFragmentChanges(mSelectedIndex);
        setSelectedButtomMenuItem(mSelectedIndex - 1);
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
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (mHomeWithText) {
            MenuItem item = menu.findItem(R.id.action_search_b);
            item.setVisible(true);
        }

        return super.onPrepareOptionsMenu(menu);
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

            case R.id.action_search_b:
                startActivity(new Intent(mContext, SearchCategoryActivity.class));

                break;

            case R.id.action_notification:
                startActivity(new Intent(mContext, NotificationsActivity.class));

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
        boolean changeFragment = true;
        switch (id) {
            case R.id.nav_home:
                mSelectedIndex = Constant.MENU_OPTION_HOME_INDEX;
                bottonMenuIndexPosition = 0;
                changeFragment = true;
                break;

            case R.id.nav_offers:
                mSelectedIndex = Constant.MENU_OPTION_OFFER_INDEX;
                bottonMenuIndexPosition = 1;
                changeFragment = true;
                break;

            case R.id.nav_blog:
                mSelectedIndex = Constant.MENU_OPTION_BLOG_INDEX;
                bottonMenuIndexPosition = 2;
                changeFragment = true;
                break;

            case R.id.nav_category:
                mSelectedIndex = Constant.MENU_OPTION_CATEGORY_INDEX;
                bottonMenuIndexPosition = 3;
                changeFragment = true;
                break;

            case R.id.nav_contact:
                mSelectedIndex = Constant.MENU_OPTION_CONTACT_US_INDEX;
                bottonMenuIndexPosition = 4;
                changeFragment = true;
                break;

            case R.id.nav_suggestion:
                mSelectedIndex = Constant.MENU_OPTION_SUGGESTION_INDEX;
                changeFragment = true;
                break;

            case R.id.nav_privacy_policy:
                mSelectedIndex = Constant.MENU_OPTION_PRIVACY_POLICY_INDEX;
                changeFragment = true;
                break;

            case R.id.nav_terms_conditions:
                changeFragment = true;
                mSelectedIndex = Constant.MENU_OPTION_TERMS_AND_CONDITION_INDEX;
                break;

            case R.id.nav_copyright:
                changeFragment = true;
                mSelectedIndex = Constant.MENU_OPTION_COPY_RIGHT_INDEX;
                break;

            case R.id.nav_about:
                changeFragment = true;
                mSelectedIndex = Constant.MENU_OPTION_ABOUT_US_INDEX;
                break;


            case R.id.nav_share:
                changeFragment = true;
                mSelectedIndex = Constant.MENU_OPTION_SOCIAL_SHARE_INDEX;
                break;

            case R.id.nav_play_video:
                changeFragment = false;
                break;

        }
        // Handle button menu item
        if (changeFragment) {
            handleFragmentChanges(mSelectedIndex);
            final int finalBottonMenuIndexPosition = bottonMenuIndexPosition;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    setSelectedButtomMenuItem(finalBottonMenuIndexPosition);
                }
            }, 100);
        } else {
            AppData.getInstance().handleVideoPlay(mContext);
        }


        mDrawer.closeDrawer(GravityCompat.END);

        return true;
    }


    private void handleFragmentChanges(int aSelectedIndex) {

        Fragment curFragment = null;

        switch (aSelectedIndex) {
            case Constant.MENU_OPTION_HOME_INDEX:
                if (mHomeMainFragment == null) {
                    mHomeMainFragment = new HomeContentFragment();
                }
                curFragment = mHomeMainFragment;
                break;

            case Constant.MENU_OPTION_OFFER_INDEX:
                curFragment = new OfferFragment();
                break;

            case Constant.MENU_OPTION_BLOG_INDEX:
                curFragment = new BlogFragment();
                break;

            case Constant.MENU_OPTION_CATEGORY_INDEX:
                curFragment = new CategoryFragment();
                break;

            case Constant.MENU_OPTION_CONTACT_US_INDEX:
                curFragment = new ContactUsFragment();
                Log.e("h mScreenCallIndex",""+mScreenCallIndex);
                Log.e("1 mOfferId",""+offerId);
                if (mScreenCallIndex == Constant.INTENT_SCREEN_CALL_BY_MORE_OFFER){
                    if (offerId != -1){
                        Bundle bundle = new Bundle();
                        bundle.putInt(Constant.INTENT_OFFER_ID, offerId);
                        bundle.putInt(Constant.SCREEN_CALL_INDEX, mScreenCallIndex);
                        curFragment.setArguments(bundle);
                    }
                }

                else if (mScreenCallIndex == Constant.INTENT_SCREEN_CALL_BY_OFFER_FRAGMENT){
                    if (offerId != -1){
                        Bundle bundle = new Bundle();
                        bundle.putInt(Constant.INTENT_OFFER_ID, offerId);
                        Log.e("2 mOfferId",""+offerId);
                        bundle.putInt(Constant.SCREEN_CALL_INDEX, mScreenCallIndex);
                        curFragment.setArguments(bundle);
                    }
                }

                break;

            case Constant.MENU_OPTION_SUGGESTION_INDEX:
                curFragment = new SuggestionFragment();
                break;

            case Constant.MENU_OPTION_PRIVACY_POLICY_INDEX:
                curFragment = new PrivacyPolicyFragment();
                break;

            case Constant.MENU_OPTION_TERMS_AND_CONDITION_INDEX:
                curFragment = new TermsAndConditionFragment();
                break;

            case Constant.MENU_OPTION_COPY_RIGHT_INDEX:
                curFragment = new CopyRightFragment();
                break;

            case Constant.MENU_OPTION_ABOUT_US_INDEX:
                curFragment = new AboutUsFragment();
                break;

            case Constant.MENU_OPTION_PLAY_VIDEO_INDEX:
                Intent intent = new Intent(mContext, VideoViewActivity.class);
                startActivity(intent);
                // curFragment = new VideoViewActivity();
                break;

            case Constant.MENU_OPTION_SOCIAL_SHARE_INDEX:
                curFragment = new ShareSocialFragment();
                break;
        }

        if (curFragment != null) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fl_main_container, curFragment);
            transaction.addToBackStack(null);
            transaction.commit();
        }
    }

    private void showExitAlertDialog() {

        if (BackCount == 1) {
            BackCount = 0;
            finish();
        } else {
            Toast.makeText(getApplicationContext(), "Press Back again to exit.", Toast.LENGTH_SHORT).show();
            BackCount++;
        }
    }

    private void changeStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.StatusbarColor));
        }
    }

    private void initBottomMenu() {
        for (int i = 0; i < mTitleArr.length; i++) {
            ImageView curImageView = (ImageView) findViewById(mImageViewArr[i]);
            curImageView.setImageResource(BOTTOM_MENU_ICON_ID_ARR[i]);

            TextView curTextView = (TextView) findViewById(mTextViewArr[i]);
            curTextView.setText(mTitleArr[i]);

            RelativeLayout curLayout = (RelativeLayout) findViewById(mLayoutArr[i]);
            final int finalI = i;
            curLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    handleBottomClick(finalI);
                }
            });
        }
    }

    private void handleBottomClick(int aPosition) {
        if (mSelectedBottomMenuIndex != aPosition) {
            switch (aPosition) {

                case 0:
                    mSelectedIndex = Constant.MENU_OPTION_HOME_INDEX;
                    break;

                case 1:
                    mSelectedIndex = Constant.MENU_OPTION_OFFER_INDEX;
                    break;

                case 2:
                    mSelectedIndex = Constant.MENU_OPTION_BLOG_INDEX;
                    break;

                case 3:
                    mSelectedIndex = Constant.MENU_OPTION_CATEGORY_INDEX;
                    break;

                case 4:
                    mSelectedIndex = Constant.MENU_OPTION_CONTACT_US_INDEX;
                    break;

                default:
                    mSelectedBottomMenuIndex = -1;
            }

            handleFragmentChanges(mSelectedIndex);
            setSelectedButtomMenuItem(aPosition);
            Constant.HOME_FRAGMENT_CALL_BACK = false;//set value for Cattegory list load from top
        }
    }

    private void setSelectedButtomMenuItem(int aPosition) {
        if (mSelectedBottomMenuIndex != -1) {
            setBottomMenuItemUnChecked(mSelectedBottomMenuIndex);
        }
        if (aPosition != -1 && aPosition < 5) {
            setBottomMenuItemChecked(aPosition);
            mSelectedBottomMenuIndex = aPosition;
        }
    }

    private void setBottomMenuItemChecked(int aPosition) {
        if (aPosition != -1) {
            TextView textView = (TextView) findViewById(mTextViewArr[aPosition]);
            textView.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary));

            ImageView imageView = (ImageView) findViewById(mImageViewArr[aPosition]);
            imageView.setImageResource(SELECTED_BOTTOM_MENU_ICON_ID_ARR[aPosition]);
        }
    }

    private void setBottomMenuItemUnChecked(int aPosition) {
        TextView textView = (TextView) findViewById(mTextViewArr[aPosition]);
        textView.setTextColor(ContextCompat.getColor(this, R.color.colorBlack));

        ImageView imageView = (ImageView) findViewById(mImageViewArr[aPosition]);
        imageView.setImageResource(BOTTOM_MENU_ICON_ID_ARR[aPosition]);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
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

        if (getIntent() != null) {
            int requestChangeIndex = getIntent().getIntExtra(
                    Constant.INTENT_MENU_INDEX_KEY, Constant.MENU_OPTION_INVALID_INDEX);
            handleMenuChange(requestChangeIndex);
        }

        if (mNotificationCountTv != null) {
            updateNotificationValue();
        }
    }

    private void handleMenuChange(int aMenuIndex) {
        if (aMenuIndex != Constant.MENU_OPTION_INVALID_INDEX) {
        }
    }

    public void handleHomeAllCategoryClick() {
        handleBottomClick(Constant.MENU_OPTION_CATEGORY_INDEX - 1);
    }

    private void displayFirebaseRegId() {

        SharedPreferences pref = getApplicationContext().getSharedPreferences(Constant.SHARED_PREF_FIREBASE_KEY, 0);
        final String regId = pref.getString("regId", null);

        if (!TextUtils.isEmpty(regId)) {
            System.out.println("regId = " + regId);
        } else {
            System.out.println("regId =" + "Firebase Reg Id is not received yet!");
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
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

    private void startHttpRequestForWelcomeNotification(final String refreshedToken) {
        if (Utils.isConnectingToInternet(mContext)){
            StorageUtils.putPref(mContext, Constant.SHARED_PREF_WELCOME_PUSH_KEY, true);
            System.out.println("!!!!welcome = ");
            String baseUrl = Constant.API_WELCOME_NOTIFICATION;
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

                                NotificationResponseBean categoryResponse = gson.fromJson(jsonResp, NotificationResponseBean.class);
                                if (categoryResponse != null) {

                                }
                            } catch (Exception e) {
                                System.out.println("e = " + e);
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            System.out.println("error = " + error);
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    params.put("key", refreshedToken);
                    params.put("device_id", androidDeviceId);
                    params.put("phone_type", "0");
                    Log.e(">>>androidDeviceId = ","------>" + androidDeviceId);
                    return params;
                }
            };
            strRequest.setShouldCache(false);
            strRequest.setTag("PUSH_WElCOME");
            AppController.getInstance().addToRequestQueue(strRequest);
        }
    }
}