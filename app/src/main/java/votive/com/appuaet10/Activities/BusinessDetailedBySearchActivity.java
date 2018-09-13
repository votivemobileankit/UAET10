package votive.com.appuaet10.Activities;

import android.Manifest;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import votive.com.appuaet10.Beans.BusinessDetailedResponseBean;
import votive.com.appuaet10.Beans.CategoryItemBean;
import votive.com.appuaet10.Beans.StatusResponse;
import votive.com.appuaet10.R;
import votive.com.appuaet10.Utilities.AppController;
import votive.com.appuaet10.Utilities.AppData;
import votive.com.appuaet10.Utilities.Constant;
import votive.com.appuaet10.Utilities.PermissionUtil;
import votive.com.appuaet10.Utilities.StorageUtils;
import votive.com.appuaet10.Utilities.UIUtils;
import votive.com.appuaet10.Utilities.Utils;

import static android.content.ContentValues.TAG;

public class BusinessDetailedBySearchActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    private Context mContext;

    private TextView mTvCategoryTitleName;
    private TextView mTvCategoryDetailed;

    private SimpleDraweeView mSimpleDraweeView;
    private RelativeLayout mProgressBarLayout;
    private NestedScrollView mNestedScrollView;
    private LinearLayout mDataMainLayout;
    private RelativeLayout mNoInternetConnectionLayout;

    private DrawerLayout mDrawer;
    private TextView mTvBusinessViews;
    private LinearLayout mLLBusinessViews;

    private ImageView mIvCall;
    private ImageView mIvWebLink;
    private ImageView mIvSendEmail;
    private ImageView mImgLinkdin;
    private ImageView mImgFacebook;
    private ImageView mImgTwitter;
    private ImageView mImgInstagram;

    private ImageView mIvLike;
    private ImageView mIvShareLink;
    private TextView mTvLikeCount;

    private ArrayList<CategoryItemBean> mBusiCategoryItemBeanArrayList;
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    private CategoryItemBean mCategoryItemBean;

    private int mScreenCallIndex;
    private String strNotiCategoryId;
    //private String strAppDataBusinessId;
    private boolean mProgressBarshowing = false;
    private int businessId;

    private TextView mNotificationCountTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_detailed_item);
        mContext = this;

        changeStatusBarColor();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        mBusiCategoryItemBeanArrayList = new ArrayList<>();
        setupNavigationDrawer();
        mProgressBarLayout = (RelativeLayout) findViewById(R.id.rl_progressBar);
        mNestedScrollView = (NestedScrollView) findViewById(R.id.nested_scroll_view);
        mTvCategoryTitleName = (TextView) findViewById(R.id.tv_category_title);

        mDataMainLayout = (LinearLayout) findViewById(R.id.ll_business_details_data);
        mNoInternetConnectionLayout = (RelativeLayout) findViewById(R.id.rl_business_details_no_internet);
        mTvBusinessViews = (TextView) findViewById(R.id.tv_business_views);
        mLLBusinessViews = (LinearLayout) findViewById(R.id.ll_business_views);

        mImgLinkdin = (ImageView) findViewById(R.id.img_linkdin);
        mIvCall = (ImageView) findViewById(R.id.img_calling);
        mIvSendEmail = (ImageView) findViewById(R.id.img_send_email);
        mIvWebLink = (ImageView) findViewById(R.id.img_weblink);

        mTvLikeCount = (TextView) findViewById(R.id.tv_like_count);
        mIvLike = (ImageView) findViewById(R.id.img_like);
        mIvShareLink = (ImageView) findViewById(R.id.img_share_link);

        mImgFacebook = (ImageView) findViewById(R.id.img_fb);
        mImgInstagram = (ImageView) findViewById(R.id.img_instagram);
        mImgTwitter = (ImageView) findViewById(R.id.img_twitter);

        mImgFacebook.setOnClickListener(this);
        mImgLinkdin.setOnClickListener(this);
        mImgInstagram.setOnClickListener(this);
        mImgTwitter.setOnClickListener(this);
        mIvCall.setOnClickListener(this);
        mIvSendEmail.setOnClickListener(this);
        mIvWebLink.setOnClickListener(this);

        mIvShareLink.setOnClickListener(this);



        hideProgressBar();
        mDataMainLayout.setVisibility(View.GONE);
        mNoInternetConnectionLayout.setVisibility(View.GONE);

        mNestedScrollView.scrollTo(0, 0);
        //showProgressBar();
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
        initBroadCastReceiver();

        String title = getString(R.string.app_name);
        UIUtils.setTitleWithFont(this, getSupportActionBar(), AppData.FONT_OPEN_SANS_BOLD, title.toUpperCase());
        if (getIntent() != null) {
            businessId = Integer.parseInt(getIntent().getStringExtra(Constant.INTENT_BUSINESS_ID));
            mScreenCallIndex = getIntent().getIntExtra(Constant.SCREEN_CALL_INDEX, -1);
            strNotiCategoryId = getIntent().getStringExtra(Constant.INTENT_NOTIFICATION_BUSINESS_ID);
            starthttpRequestForBusinessDetailed(businessId);
            Log.e("BusinessDetailedBySearchActivity","businessId----------> "+ businessId);
            handleUpdate_Views(businessId);

        }


        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mScreenCallIndex != -1 && mScreenCallIndex == Constant.NOTIFICATION_BUSINESS_BY_HOME) {

                    gotohome();
                } else if (mScreenCallIndex != -1 && mScreenCallIndex == Constant.INTENT_SCREEN_CALL_BY_APPDATA_BUSINESS) {
                    gotohome();
                } else {
                    onBackPressed();
                }
            }
        });


        mIvLike.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                Log.e("-----------------","!!!!CategoryId = " + businessId);
//                Toast.makeText(mContext, "getCategoryListingId--> "+mCategoryItemBean.getCategoryListingId()
//                        , Toast.LENGTH_LONG).show();
                if (businessId != -1){
                    handleUpdateLike(businessId);
                }
            }
        });

        Button retryBtn = (Button) findViewById(R.id.btn_no_internet_retry);
        retryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                starthttpRequestForBusinessDetailed(businessId);
            }
        });
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

        Intent intent = new Intent(BusinessDetailedBySearchActivity.this, HomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();

    }

    private void starthttpRequestForBusinessDetailed(final int businessId) {

        if (Utils.isConnectingToInternet(mContext)) {
            String baseUrl = Constant.API_BUSINESS_DETAILED;
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

                                BusinessDetailedResponseBean businessDetailedResponseBean = gson.fromJson(jsonResp, BusinessDetailedResponseBean.class);
                                hideProgressBar();
                                if (businessDetailedResponseBean != null && businessDetailedResponseBean.getmBusinessDetailedList() != null && businessDetailedResponseBean.getmBusinessDetailedList().size() > 0) {

                                    if (Utils.isStatusSuccess(businessDetailedResponseBean.getmStatus())) {
                                        setData(businessDetailedResponseBean.getmBusinessDetailedList());
                                    } else {
                                        UIUtils.showToast(mContext, businessDetailedResponseBean.getmMsg());
                                    }
                                } else {
                                    UIUtils.showToast(mContext, getString(R.string.NoDataFoundTextErrorMsg));
                                    hideProgressBar();
                                }
                            } catch (Exception e) {
                                UIUtils.showToast(mContext, getString(R.string.VolleyErrorMsg));
                                hideProgressBar();
                                System.out.println("???e = " + e);
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            hideProgressBar();

                            if (error instanceof NoConnectionError) {
                                UIUtils.showToast(mContext, getString(R.string.InternetErrorMsg));
                            } else {
                                UIUtils.showToast(mContext, getString(R.string.VolleyErrorMsg));
                            }
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    params.put(Constant.BUSINESS_DETAILED_ID_KEY, "" + businessId);
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
            mDataMainLayout.setVisibility(View.GONE);
            mNoInternetConnectionLayout.setVisibility(View.VISIBLE);
        }

    }


    private void setData(ArrayList<CategoryItemBean> categoryItemBeen) {
        mNoInternetConnectionLayout.setVisibility(View.GONE);
        mDataMainLayout.setVisibility(View.VISIBLE);
        hideProgressBar();
        mBusiCategoryItemBeanArrayList = categoryItemBeen;
        for (int i = 0; i < mBusiCategoryItemBeanArrayList.size(); i++) {
            mCategoryItemBean = mBusiCategoryItemBeanArrayList.get(i);
        }

        if (mCategoryItemBean.getCategoryItemName() != null && !mCategoryItemBean.getCategoryItemName().equals("")) {
            mTvCategoryTitleName.setText(mCategoryItemBean.getCategoryItemName());

        } else {
            mTvCategoryTitleName.setVisibility(View.GONE);
        }

        if (mCategoryItemBean.getStrViews() != null && !mCategoryItemBean.getStrViews().equals("")) {
            String views = mCategoryItemBean.getStrViews() + " " + getResources().getString(R.string.ViewText);
            mTvBusinessViews.setText(views);
        } else {
            mLLBusinessViews.setVisibility(View.GONE);
        }


        mSimpleDraweeView = (SimpleDraweeView) findViewById(R.id.iv_business_display);
        mTvCategoryDetailed = (TextView) findViewById(R.id.tv_business_detailed);
        loadDisplayImage();

        if (mCategoryItemBean.getCategoryItemDesc() != null) {
            mTvCategoryDetailed.setText(mCategoryItemBean.getCategoryItemDesc());
        } else {
            mTvCategoryDetailed.setVisibility(View.GONE);
        }


        if (mCategoryItemBean.getFbLinkStr() != null
                && !mCategoryItemBean.getFbLinkStr().isEmpty()) {
            mImgFacebook.setVisibility(View.VISIBLE);
        } else {
            mImgFacebook.setVisibility(View.GONE);
        }

        if (mCategoryItemBean.getTwitterLinkStr() != null
                && !mCategoryItemBean.getTwitterLinkStr().isEmpty()) {
            mImgTwitter.setVisibility(View.VISIBLE);
        } else {
            mImgTwitter.setVisibility(View.GONE);
        }

        if (mCategoryItemBean.getLinkdinLinkStr() != null
                && !mCategoryItemBean.getLinkdinLinkStr().isEmpty()) {
            mImgLinkdin.setVisibility(View.VISIBLE);
        } else {
            mImgLinkdin.setVisibility(View.GONE);
        }

        if (mCategoryItemBean.getInstaLinkStr() != null
                && !mCategoryItemBean.getInstaLinkStr().isEmpty()) {
            mImgInstagram.setVisibility(View.VISIBLE);
        } else {
            mImgInstagram.setVisibility(View.GONE);
        }


        if (mCategoryItemBean.getPhoneNo() != null
                && !mCategoryItemBean.getPhoneNo().isEmpty()) {
            mIvCall.setVisibility(View.VISIBLE);
        } else {
            mIvCall.setVisibility(View.GONE);
        }

        if (mCategoryItemBean.getEmailStr() != null
                && !mCategoryItemBean.getEmailStr().isEmpty()) {
            mIvSendEmail.setVisibility(View.VISIBLE);
        } else {
            mIvSendEmail.setVisibility(View.GONE);
        }


        if (mCategoryItemBean.getWebLinkStr() != null &&
                !mCategoryItemBean.getWebLinkStr().isEmpty()) {
            mIvWebLink.setVisibility(View.VISIBLE);
        } else {
            mIvWebLink.setVisibility(View.GONE);
        }


        mTvLikeCount.setText(""+mCategoryItemBean.getLikesCount());


        mIvWebLink.setOnLongClickListener(new View.OnLongClickListener() {

            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(BusinessDetailedBySearchActivity.this, "Touch to Open Weblink", Toast.LENGTH_SHORT).show();
                return true;
            }

        });

        mIvSendEmail.setOnLongClickListener(new View.OnLongClickListener() {

            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(BusinessDetailedBySearchActivity.this, "Touch to Send Email", Toast.LENGTH_SHORT).show();
                return true;
            }

        });


        mImgInstagram.setOnLongClickListener(new View.OnLongClickListener() {

            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(BusinessDetailedBySearchActivity.this, "Touch to Open instagram Link", Toast.LENGTH_SHORT).show();
                return true;
            }
        });
        mImgFacebook.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(BusinessDetailedBySearchActivity.this, "Touch to Open Facebook Link", Toast.LENGTH_SHORT).show();
                return true;
            }
        });

        mImgLinkdin.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(BusinessDetailedBySearchActivity.this, "Touch to Open Linkdin Link", Toast.LENGTH_SHORT).show();
                return true;
            }
        });

        mImgTwitter.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(BusinessDetailedBySearchActivity.this, "Touch to Open twitter Link", Toast.LENGTH_SHORT).show();
                return true;
            }
        });
    }


    private void requestPermission() {
        // No explanation needed, we can request the permission.
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, Constant.MY_PERMISSIONS_REQUEST_READ_PHONE_STATE);
    }


    private void handleCallPermission() {

        if (PermissionUtil.isPermissionGranted(this, Manifest.permission.CALL_PHONE)) {
            dialNumber();
        } else {
            requestPermission();
        }
    }

    private void openBrowserLink() {

        try {
            String addHttp = mCategoryItemBean.getWebLinkStr();
            Uri uri = Uri.parse(addHttp);
            Intent myIntent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(myIntent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(this, "No application can handle this request."
                    + " Please install a webbrowser", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }


    private void openBrowserFbLink() {

        try {
            Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(mCategoryItemBean.getFbLinkStr()));
            startActivity(myIntent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(this, "No application can handle this request."
                    + " Please install a webbrowser", Toast.LENGTH_LONG).show();
            e.printStackTrace();
            System.out.println("###e = " + e);
        }
    }


    private void openBrowserInstaLink() {

        try {
            //mCategoryItemBean.getWebLinkStr()
            Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(mCategoryItemBean.getInstaLinkStr()));
            startActivity(myIntent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(this, "No application can handle this request."
                    + " Please install a webbrowser", Toast.LENGTH_LONG).show();
            e.printStackTrace();
            System.out.println("###e = " + e);
        }
    }

    private void openBrowserTwitterLink() {

        try {
            //mCategoryItemBean.getWebLinkStr()
            Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(mCategoryItemBean.getTwitterLinkStr()));
            startActivity(myIntent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(this, "No application can handle this request."
                    + " Please install a webbrowser", Toast.LENGTH_LONG).show();
            e.printStackTrace();
            System.out.println("###e = " + e);
        }
    }


    private void openBrowserLinkdinLink() {

        try {
            //mCategoryItemBean.getWebLinkStr()
            Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(mCategoryItemBean.getLinkdinLinkStr()));
            startActivity(myIntent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(mContext, "No application can handle this request."
                    + " Please install a webbrowser", Toast.LENGTH_LONG).show();
            e.printStackTrace();
            System.out.println("###e = " + e);
        }
    }


    private void sendEmail() {

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/html");
        intent.putExtra(Intent.EXTRA_EMAIL, mCategoryItemBean.getEmailStr());
        intent.putExtra(Intent.EXTRA_SUBJECT, "Subject");
        intent.putExtra(Intent.EXTRA_TEXT, "I'm email body.");
        startActivity(Intent.createChooser(intent, "Send Email"));
    }

    private void dialNumber() {
        String number = mCategoryItemBean.getPhoneNo();
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + number));
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        startActivity(intent);
    }


    private void changeStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.StatusbarColor));
        }
    }

    private void loadDisplayImage() {
        try {
            String selectedImageStr = mCategoryItemBean.getCategoryItemFullImgUrl();

            String imagePath = selectedImageStr.trim();
            String newPathStr = imagePath.replaceAll(" ", "%20");
            mSimpleDraweeView.setImageURI(Uri.parse(newPathStr));

            mSimpleDraweeView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
             //   int idid = mCategoryItemBean.getCategoryListingId();
                    if (businessId ==891){

                        Intent intent = new Intent(mContext, WEbpage.class);
                        startActivity(intent);

                    }
                   /* Intent intent = new Intent(mContext, WEbpage.class);
                    startActivity(intent);*/

                }
            });

        } catch (Exception e) {
            final int sdk = Build.VERSION.SDK_INT;
            if (sdk < Build.VERSION_CODES.JELLY_BEAN) {

                mSimpleDraweeView.setBackgroundDrawable(getResources().getDrawable(R.drawable.ic_placeholder));
            } else {
                mSimpleDraweeView.setBackground(ContextCompat.getDrawable(this, R.drawable.ic_placeholder));
            }
        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    public void handleSideMenuClick(int aMenuIndex) {
        Intent intent = new Intent(mContext, HomeActivity.class);
        intent.putExtra(Constant.INTENT_MENU_INDEX_KEY, aMenuIndex);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
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

                    AppData.getInstance().showDialog(BusinessDetailedBySearchActivity.this, title, message, imageUrl, type, id);

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
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == Constant.MY_PERMISSIONS_REQUEST_READ_PHONE_STATE) {
            // for each permission check if the user granted/denied them
            // you may want to group the rationale in a single dialog,
            // this is just an example
            boolean permissionDenied = false;

            for (int i = 0, len = permissions.length; i < len; i++) {
                String permission = permissions[i];
                if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                    // user rejected the permission
                    permissionDenied = true;
                    boolean showRationale = false;
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                        showRationale = shouldShowRequestPermissionRationale(permission);
                    }
                    if (!showRationale) {
                        // user also CHECKED "never ask again"
                        // you can either enable some fall back,
                        // disable features of your app
                        // or open another dialog explaining
                        // again the permission and directing to
                        // the app setting
                        showDialogForPermissionSetting();
                    } else if (Manifest.permission.CALL_PHONE.equals(permission)) {
                        showDialogForPermission();
//                        showRationale(permission, R.string.permission_denied_contacts);
                        // user did NOT check "never ask again"
                        // this is a good place to explain the user
                        // why you need the permission and ask if he wants
                        // to accept it (the rationale)
                    }
                }
            }
            if (!permissionDenied) {
                dialNumber();
            }
        }

        if (requestCode == Constant.SPLASH_TIME_OUT) {
            requestPermission();
        }
    }

    private void showDialogForPermissionSetting() {
        //final Context context = this;
        new AlertDialog.Builder(mContext)
                .setTitle(getString(R.string.PermissionGoSettingTitle))
                .setMessage(getString(R.string.PermissionGoSettingMsg))
                .setCancelable(true)
                .setPositiveButton(getString(R.string.PermissionGoSettingTxt), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        final Intent i = new Intent();
                        i.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        i.addCategory(Intent.CATEGORY_DEFAULT);
                        i.setData(Uri.parse("package:" + mContext.getPackageName()));
                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        i.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                        i.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                        mContext.startActivity(i);
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    private void showDialogForPermission() {
        new AlertDialog.Builder(mContext)
                .setTitle(getString(R.string.PermissionDeniedTitle))
                .setMessage(getString(R.string.PermissionDeniedMsg))
                .setCancelable(true)
                .setPositiveButton(getString(R.string.PermissionDeniedSureTxt), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setNegativeButton(getString(R.string.PermissionDeniedRetryTxt), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        requestPermission();
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
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


            case R.id.img_share_link:
//                shareAppLink();
                break;

        }
        mDrawer.closeDrawer(GravityCompat.END);
        return true;
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.img_fb:
                openBrowserFbLink();
                break;

            case R.id.img_twitter:
                openBrowserTwitterLink();
                break;

            case R.id.img_instagram:
                openBrowserInstaLink();
                break;

            case R.id.img_linkdin:
                openBrowserLinkdinLink();
                break;

            case R.id.img_calling:
                handleCallPermission();

                break;

            case R.id.img_send_email:
                sendEmail();
                break;

            case R.id.img_weblink:
                openBrowserLink();
                break;

        }

    }


    private void handleUpdateLike(final int aCategoryId) {
        Log.e("-------handleUpdateLike----------","!!!!aCategoryId = " + aCategoryId);
        if (Utils.isConnectingToInternet(mContext)) {
            String baseUrl = Constant.API_ADD_LIKE;

            StringRequest strRequest = new StringRequest(Request.Method.POST, baseUrl,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
//                            System.out.println("!!!!response = " + response);
                            Log.e("--------handleUpdateLike---------","!!!!response = " + response);
                            try {
                                Gson gson = new GsonBuilder().create();
                                JsonParser jsonParser = new JsonParser();
                                JsonObject jsonResp = jsonParser.parse(response).getAsJsonObject();

                                StatusResponse statusResponse = gson.fromJson(jsonResp, StatusResponse.class);
                                if (statusResponse != null) {
                                    if (Utils.isStatusSuccess(statusResponse.getStatus())) {
                                        int count = Integer.parseInt(mTvLikeCount.getText().toString().trim());
                                        count = count + 1;
                                        mTvLikeCount.setText(""+count);
                                    } else {
                                        UIUtils.showToast(mContext, statusResponse.getMsg());
                                    }
                                }
                            } catch (Exception e) {
                                UIUtils.showToast(mContext, getString(R.string.VolleyErrorMsg));
                                System.out.println("???e = " + e);
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                            if (error instanceof NoConnectionError) {
                                UIUtils.showToast(mContext, getString(R.string.InternetErrorMsg));
                            } else {
                                UIUtils.showToast(mContext, getString(R.string.VolleyErrorMsg));
                            }
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() {
                    final String androidDeviceId = Settings.Secure.getString(mContext.getContentResolver(), Settings.Secure.ANDROID_ID);
                    Map<String, String> params = new HashMap<>();
                    params.put(Constant.JSON_CATEGORY_LISTING_LIKE_ID, "" + aCategoryId);
                    params.put(Constant.JSON_DEVICE_ID_KEY, "" + androidDeviceId);
                    Log.e("F-----------------","!!!!params = " + params.toString());
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
            UIUtils.showToast(mContext, getString(R.string.InternetErrorMsg));
        }
    }




    private void handleUpdate_Views(final int aCategoryId) {
        if (Utils.isConnectingToInternet(mContext)) {
            String baseUrl = Constant.API_ADD_VIEW;

            StringRequest strRequest = new StringRequest(Request.Method.POST, baseUrl,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            System.out.println("!!!!handleUpdate_Views response = " + response);

                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                        }
                    }) {
                @Override
                protected Map<String, String> getParams() {
                    final String androidDeviceId = Settings.Secure.getString(mContext.getContentResolver(), Settings.Secure.ANDROID_ID);
                    Map<String, String> params = new HashMap<>();
                    params.put(Constant.JSON_CATEGORY_LISTING_LIKE_ID, "" + aCategoryId);
                    params.put(Constant.JSON_DEVICE_ID_KEY, "" + androidDeviceId);
                    System.out.println("!!!!handleUpdate_Views params = " + params);
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
            UIUtils.showToast(mContext, getString(R.string.InternetErrorMsg));
        }
    }




}
