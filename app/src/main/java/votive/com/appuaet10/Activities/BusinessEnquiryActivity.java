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
import android.support.design.widget.NavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
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

import votive.com.appuaet10.Beans.BusinessEnquiryResponse;
import votive.com.appuaet10.Beans.CategoryBean;
import votive.com.appuaet10.Beans.CategoryResponse;
import votive.com.appuaet10.R;
import votive.com.appuaet10.Utilities.AppController;
import votive.com.appuaet10.Utilities.AppData;
import votive.com.appuaet10.Utilities.Constant;
import votive.com.appuaet10.Utilities.StorageUtils;
import votive.com.appuaet10.Utilities.UIUtils;
import votive.com.appuaet10.Utilities.Utils;

import static android.content.ContentValues.TAG;

public class BusinessEnquiryActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    public static final boolean SHOW_MENU_OPTION = true;
    private Context mContext;
    private int strCategoryId;
    private boolean mProgressBarshowing = false;

    private ScrollView mMainScrollView;

    private RelativeLayout mProgressBarLayout;
    private RelativeLayout mRlNoInterConnection;
    private String strCategoryName;
    private Button mTryAgainBtn;
    private EditText mEtName;
    private EditText mEtEmail;
    private EditText mEtContactNo;
    private EditText mEtCompanyName;
    private Spinner mSpinnerSelectCategory;

    private ArrayList<String> mCategoryBeanArrayList;
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    private DrawerLayout mDrawer;

    private TextView mNotificationCountTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top_brand);

        mContext = this;
        changeStatusBarColor();
        init();
        initBroadCastReceiver();

    }

    private void init() {
        if (getIntent() != null) {
            strCategoryId = getIntent().getIntExtra(Constant.INTENT_CATEGORY_ITEM_ID, -1);
            strCategoryName = getIntent().getStringExtra(Constant.INTENT_CATEGORY_NAME);
        }

        setupNavigationDrawer();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(true);
        }


        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }

        });


        String title = getString(R.string.app_name);
        if (strCategoryName != null) {
            title = strCategoryName;
        }
        UIUtils.setTitleWithFont(this, getSupportActionBar(), AppData.FONT_OPEN_SANS_BOLD, title);

        mMainScrollView = (ScrollView) findViewById(R.id.sv_contact);
        mProgressBarLayout = (RelativeLayout) findViewById(R.id.rl_progressBar);
        mRlNoInterConnection = (RelativeLayout) findViewById(R.id.rl_category_list_no_internet_main);
        mTryAgainBtn = (Button) findViewById(R.id.btn_no_internet_retry);
        mEtCompanyName = (EditText) findViewById(R.id.et_brand_company_name);
        mEtContactNo = (EditText) findViewById(R.id.et_brand_contact_no);
        mEtEmail = (EditText) findViewById(R.id.et_brand_email);
        mEtName = (EditText) findViewById(R.id.et_brand_your_name);

        mSpinnerSelectCategory = (Spinner) findViewById(R.id.spinner_category);
        mSpinnerSelectCategory.setEnabled(true);
        mSpinnerSelectCategory.setClickable(true);
        mCategoryBeanArrayList = new ArrayList<>();
        Button buttonSend = (Button) findViewById(R.id.b_brand_send);
        buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateData();
            }
        });
        buttonSend.setTypeface(AppData.getInstance().getTypeFaceForType(this, AppData.FONT_OPEN_SANS_BOLD));


        mTryAgainBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCategoryBeanArrayList.size() == 0) {
                    mMainScrollView.setVisibility(View.GONE);
                    mRlNoInterConnection.setVisibility(View.GONE);
                    mProgressBarLayout.setVisibility(View.VISIBLE);
                    startRequestForCategoryInfo();
                } else {
                    startHttpRequestForTopBrand();
                }
            }
        });

        ArrayList<CategoryBean> curCategoryList = AppData.getInstance().getCategoryList();
        if (curCategoryList != null && curCategoryList.size() > 0) {
            selectCategory();
        } else {
            mMainScrollView.setVisibility(View.GONE);
            mRlNoInterConnection.setVisibility(View.GONE);
            showProgressBar();
            startRequestForCategoryInfo();
        }


    }


    private void selectCategory() {

        ArrayList<CategoryBean> curCategoryList = AppData.getInstance().getCategoryList();
        if (curCategoryList.size() > 0) {
            for (int i = 0; i < curCategoryList.size(); i++) {
                CategoryBean categoryBean = curCategoryList.get(i);
                mCategoryBeanArrayList.add(categoryBean.getmCategoryName().toLowerCase());
            }
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, mCategoryBeanArrayList);
        mSpinnerSelectCategory.setAdapter(adapter);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinnerSelectCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                String item = parentView.getItemAtPosition(position).toString();
                // UIUtils.showToast(mContext, item);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {

            }
        });
        mMainScrollView.setVisibility(View.VISIBLE);
        mRlNoInterConnection.setVisibility(View.GONE);
        hideProgressBar();
    }


    private void validateData() {

        String name = mEtName.getText().toString().trim();
        String email = mEtEmail.getText().toString().trim();
        String compantName = mEtCompanyName.getText().toString().trim();
        String contactNo = mEtContactNo.getText().toString();
        String selectCategory = mSpinnerSelectCategory.getSelectedItem().toString();

        if (selectCategory.isEmpty()) {
            mSpinnerSelectCategory.requestFocus();
            UIUtils.showToast(mContext, getString(R.string.BrandSelectCategory));

        } else if (compantName.isEmpty()) {
            UIUtils.showToast(mContext, getString(R.string.BrandCompanyName));
            mEtCompanyName.requestFocus();

        } else if (name.isEmpty()) {
            UIUtils.showToast(mContext, getString(R.string.ContactUsNameEmptyMsg));
            mEtName.requestFocus();
            mEtCompanyName.clearFocus();
        } else if (contactNo.isEmpty()) {
            mEtCompanyName.clearFocus();
            mEtName.clearFocus();
            mEtEmail.clearFocus();
            mEtContactNo.requestFocus();
            UIUtils.showToast(mContext, getString(R.string.BrandCompanyConatct));

        } else if (email.isEmpty()) {
            UIUtils.showToast(mContext, getString(R.string.ContactUsEmailEmptyMsg));
            mEtEmail.requestFocus();
            mEtName.clearFocus();
            mEtContactNo.clearFocus();
            mEtCompanyName.clearFocus();

        } else if (!Utils.isValidEmail(email)) {
            UIUtils.showToast(mContext, getString(R.string.ContactUsInvalidEmailMsg));
            mEtEmail.requestFocus();
            mEtName.clearFocus();
            mEtContactNo.clearFocus();
            mEtCompanyName.clearFocus();

        } else {
            mEtCompanyName.clearFocus();
            mEtEmail.clearFocus();
            mEtName.clearFocus();
            mEtContactNo.clearFocus();
            mSpinnerSelectCategory.clearFocus();

            startHttpRequestForTopBrand();
        }
    }

    private void startHttpRequestForTopBrand() {
        if (Utils.isConnectingToInternet(mContext)) {
            String baseUrl = Constant.API_BUSINESS_ENQUIRY;
            showProgressBar();
            StringRequest strRequest = new StringRequest(Request.Method.POST, baseUrl,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            System.out.println("!!!!response = " + response);
                            try {
                                Gson gson = new GsonBuilder().create();
                                JsonParser jsonParser = new JsonParser();
                                JsonObject jsonResp = jsonParser.parse(response).getAsJsonObject();

                                BusinessEnquiryResponse businessEnquiryResponse = gson.fromJson(jsonResp, BusinessEnquiryResponse.class);
                                hideProgressBar();
                                if (businessEnquiryResponse != null) {
                                    if (Utils.isStatusSuccess(businessEnquiryResponse.getmStatus())) {
                                        successfulllyMessage(businessEnquiryResponse.getmMsg());
                                    } else {
                                        UIUtils.showToast(mContext, businessEnquiryResponse.getmMsg());
                                    }
                                } else {
                                    hideProgressBar();
                                    UIUtils.showToast(mContext, getString(R.string.ContactUsError));
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
                            UIUtils.showToast(mContext, getString(R.string.VolleyErrorMsg));
                            hideProgressBar();
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    params.put(Constant.JSON_BRAND_YOUR_NAME, mEtName.getText().toString().trim());
                    params.put(Constant.JSON_BRAND_EMAIL, mEtEmail.getText().toString().trim());
                    params.put(Constant.JSON_BRAND_CATEGORY, mSpinnerSelectCategory.getSelectedItem().toString());
                    params.put(Constant.JSON_BRAND_COMPANY_NAME, mEtCompanyName.getText().toString());
                    params.put(Constant.JSON_BRAND_CONTACT_NUMBER, mEtContactNo.getText().toString());
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
        }
    }

    private void successfulllyMessage(String s) {
        showPopuDialog();
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
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

    private void startRequestForCategoryInfo() {
        String baseUrl = Constant.API_CATEGORY_LIST;
        StringRequest strRequest = new StringRequest(Request.Method.POST, baseUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println("!!!!response = " + response);
                        hideProgressBar();
                        try {
                            Gson gson = new GsonBuilder().create();
                            JsonParser jsonParser = new JsonParser();
                            JsonObject jsonResp = jsonParser.parse(response).getAsJsonObject();
                            CategoryResponse categoryResponse = gson.fromJson(jsonResp, CategoryResponse.class);
                            if (categoryResponse != null && categoryResponse.getmCategoryList() != null && categoryResponse.getmCategoryList().size() > 0) {
                                if (Utils.isStatusSuccess(categoryResponse.getmStatus())
                                        && categoryResponse.getmCategoryList() != null && categoryResponse.getmCategoryList().size() > 0) {
                                    AppData.getInstance().setCategoryList(mContext, categoryResponse.getmCategoryList());
                                    mMainScrollView.setVisibility(View.VISIBLE);
                                    mRlNoInterConnection.setVisibility(View.GONE);
                                    selectCategory();
                                }
                            }
                        } catch (Exception e) {
                            mMainScrollView.setVisibility(View.GONE);
                            mRlNoInterConnection.setVisibility(View.VISIBLE);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        hideProgressBar();
                        mMainScrollView.setVisibility(View.GONE);
                        mRlNoInterConnection.setVisibility(View.VISIBLE);
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

                    AppData.getInstance().showDialog(BusinessEnquiryActivity.this, title, message, imageUrl, type, id);

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


    private void showPopuDialog() {
        final Dialog openDialog = new Dialog(mContext);
        openDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        openDialog.setContentView(R.layout.thanku_popup_dialog);
        openDialog.setCancelable(false);
        openDialog.setCanceledOnTouchOutside(false);
        openDialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
        openDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        Button dialogImage = (Button) openDialog.findViewById(R.id.imageView_custom_dialog_close);
        dialogImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog.dismiss();
                mEtName.setText("");
                mEtEmail.setText("");
                mEtCompanyName.setText("");
                mEtContactNo.setText("");
            }
        });

        Button okBtn = (Button) openDialog.findViewById(R.id.positive_button);
        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                openDialog.dismiss();
                mEtName.setText("");
                mEtContactNo.setText("");
                mEtEmail.setText("");
                mEtCompanyName.setText("");
            }
        });
        openDialog.show();
    }
}
