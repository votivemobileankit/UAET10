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
import android.graphics.Color;
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
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
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
import com.bumptech.glide.Glide;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.HashMap;
import java.util.Map;

import votive.com.appuaet10.Beans.OfferDetail;
import votive.com.appuaet10.Beans.OfferVoucher;
//import votive.com.appuaet10.Beans.Offer_Voucher;
import votive.com.appuaet10.Beans.VoucherUsed;
import votive.com.appuaet10.R;
import votive.com.appuaet10.Utilities.AppController;
import votive.com.appuaet10.Utilities.AppData;
import votive.com.appuaet10.Utilities.Constant;
import votive.com.appuaet10.Utilities.PermissionUtil;
import votive.com.appuaet10.Utilities.StorageUtils;
import votive.com.appuaet10.Utilities.UIUtils;
import votive.com.appuaet10.Utilities.Utils;

import static android.content.ContentValues.TAG;

public class OfferDetailedActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private Context mContext;

    private TextView mTvCategoryTitleName;
    private TextView mTvCategoryDetailed;

    private SimpleDraweeView  iv_barcode_display;
    ImageView mSimpleDraweeView;
    private RelativeLayout mProgressBarLayout;
    private NestedScrollView mNestedScrollView;
    private LinearLayout mDataMainLayout;
    private LinearLayout mLlOfferInfoLayout;
//    private LinearLayout mLlNoOfferLayout;
//    private LinearLayout mLlApplyMoreLayout;
    private RelativeLayout mNoInternetConnectionLayout;

    private DrawerLayout mDrawer;
    private TextView textcompanyname, textcompanyconatact, textcompanyemail, textexpire, texttermsandcondition;

//    private TextView  mTvApplyMore, mTvNext;

    private BroadcastReceiver mRegistrationBroadcastReceiver;

    private int mScreenCallIndex;

    private boolean mProgressBarShowing = false;
    private int offerId;
    private String mTermsAndConditionWebLink = "";
    private TextView mNotificationCountTv;

    LinearLayout ll_get_offer;
    TextView get_offer;

    boolean offer_used_status=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offer_detailed_item);
        mContext = this;

        changeStatusBarColor();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        mBusiCategoryItemBeanArrayList = new ArrayList<>();
        setupNavigationDrawer();
        mProgressBarLayout = (RelativeLayout) findViewById(R.id.rl_progressBar);
        mNestedScrollView = (NestedScrollView) findViewById(R.id.nested_scroll_view);
        mTvCategoryTitleName = (TextView) findViewById(R.id.tv_category_title);

        mDataMainLayout = (LinearLayout) findViewById(R.id.ll_business_details_data);
        mNoInternetConnectionLayout = (RelativeLayout) findViewById(R.id.rl_business_details_no_internet);

        textcompanyname = (TextView) findViewById(R.id.textcompanyname);
        textcompanyconatact = (TextView) findViewById(R.id.textcompanyconatact);
        textcompanyemail= (TextView) findViewById(R.id.textcompanyemail);
        textexpire = (TextView) findViewById(R.id.textexpire);
        texttermsandcondition= (TextView) findViewById(R.id.texttermsandcondition);
//        mTvApplyMore = (TextView) findViewById(R.id.apply_more);
        mLlOfferInfoLayout = (LinearLayout) findViewById(R.id.ll_offer_info);
//        mLlNoOfferLayout = (LinearLayout) findViewById(R.id.ll_no_offer);
//        mLlApplyMoreLayout = (LinearLayout) findViewById(R.id.ll_apply_more);
//        mTvNext = (TextView) findViewById(R.id.tv_next_voucher);

        ll_get_offer = (LinearLayout) findViewById(R.id.ll_get_offer);
        get_offer = (TextView) findViewById(R.id.get_offer);

        Button retryBtn = (Button) findViewById(R.id.btn_no_internet_retry);
        retryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startHttpRequestForOfferDetailed(offerId);
            }
        });

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
            offerId = Integer.parseInt(getIntent().getStringExtra(Constant.INTENT_OFFER_ID));
            Log.e("offerId","offerId------>" + offerId);
            startHttpRequestForOfferDetailed(offerId);
            mScreenCallIndex = getIntent().getIntExtra(Constant.SCREEN_CALL_INDEX, -1);
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

        textcompanyconatact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleCallPermission(textcompanyconatact.getText().toString().trim());
            }
        });

        texttermsandcondition.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                openBrowserLink();
            }
        });

//        SpannableString sSApplyMore = new SpannableString(getString(R.string.apply));
//        ClickableSpan span = new ClickableSpan() {
//            @Override
//            public void onClick(View textView) {
//                handleMoreVoucherClick(Constant.MENU_OPTION_CONTACT_US_INDEX);
//            }
//
//            @Override
//            public void updateDrawState(TextPaint ds) {
//                super.updateDrawState(ds);
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                    ds.setColor(getColor(R.color.colorPrimary));
//                }
//            }
//        };
//
//        sSApplyMore.setSpan(span, 37, getString(R.string.apply).length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//        mTvApplyMore.setText(sSApplyMore,TextView.BufferType.SPANNABLE);
//        mTvApplyMore.setMovementMethod(LinkMovementMethod.getInstance());
//
//        mTvNext.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startHttpRequestForOfferDetailed(offerId);
//            }
//        });



        get_offer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(offer_used_status){

//                    new AlertDialog.Builder(mContext)
//                            .setTitle(getString(R.string.app_name))
//                            .setMessage("Looks like you've already taken this offer")
//                            .setCancelable(false)
//                            .setPositiveButton("ok", new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialog, int which) {
//                                    // Whatever...
//                                    dialog.dismiss();
//                                }
//                            }).show();
                    openBrowserLink_butinah();

                }else {
                    startHttpRequestForoffer_voucher_used(offerId);
                }
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

        Intent intent = new Intent(OfferDetailedActivity.this, HomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();

    }

    private void startHttpRequestForOfferDetailed(final int offerId) {

        final String androidDeviceId = Settings.Secure.getString(mContext.getContentResolver(), Settings.Secure.ANDROID_ID);

//        https://www.uaet10.com/webservices/offer_voucher?device_id=1&offer_id=36
        if (Utils.isConnectingToInternet(mContext)) {
            String baseUrl = Constant.API_OFFER_DETAILED_DEV;
            Log.e(">>>baseUrl = ","------>" + baseUrl);
            showProgressBar();
            StringRequest strRequest = new StringRequest(Request.Method.POST, baseUrl,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.e("!!!!API_OFFER_DETAILED_DEV = " ,"------>"+ response);
                            try {
                                Gson gson = new GsonBuilder().create();
                                JsonParser jsonParser = new JsonParser();
                                JsonObject jsonResp = jsonParser.parse(response).getAsJsonObject();

                                OfferVoucher offer_Voucher = gson.fromJson(jsonResp, OfferVoucher.class);
                                hideProgressBar();
                                if (offer_Voucher != null) {

                                    if (Utils.isStatusSuccess(""+offer_Voucher.getOfferDetail().getStatus())) {
                                        setData(offer_Voucher);
                                    } else {
                                        UIUtils.showToast(mContext, offer_Voucher.getOfferDetail().getMessage());
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

                    params.put(Constant.OFFER_DETAILED_ID_KEY, "" + offerId);
                    params.put(Constant.JSON_DEVICE_ID_KEY, ""+androidDeviceId);
                    Log.e(">>>param = ","------>" + params.toString());
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


    private void setData(OfferVoucher offer_Voucher) {
        mNoInternetConnectionLayout.setVisibility(View.GONE);
        mDataMainLayout.setVisibility(View.VISIBLE);
        hideProgressBar();

//        if (offer_Voucher.getOfferDetail().getApplyMore() == 1){
//            mLlApplyMoreLayout.setVisibility(View.VISIBLE);
//            mTvApplyMore.setVisibility(View.VISIBLE);
//            mLlNoOfferLayout.setVisibility(View.VISIBLE);
//            mLlOfferInfoLayout.setVisibility(View.GONE);
//            mTvNext.setVisibility(View.GONE);
//        } else {
//            mTvNext.setVisibility(View.VISIBLE);
//            mLlOfferInfoLayout.setVisibility(View.VISIBLE);
//            mLlApplyMoreLayout.setVisibility(View.GONE);
//            mTvApplyMore.setVisibility(View.GONE);
//            mLlNoOfferLayout.setVisibility(View.GONE);
//        }

        if (offer_Voucher.getOfferDetail().getTitle() != null && !offer_Voucher.getOfferDetail().getTitle().equals("")) {
            mTvCategoryTitleName.setText(offer_Voucher.getOfferDetail().getTitle());
        } else {
            mTvCategoryTitleName.setVisibility(View.GONE);
        }


//        mSimpleDraweeView = (SimpleDraweeView) findViewById(R.id.iv_business_display);
        mSimpleDraweeView = (ImageView) findViewById(R.id.iv_business_display);

        iv_barcode_display  = (SimpleDraweeView) findViewById(R.id.iv_barcode_display);
        mTvCategoryDetailed = (TextView) findViewById(R.id.tv_business_detailed);

        loadDisplayImage(offer_Voucher.getOfferDetail());
        load_Barcode_Image(offer_Voucher.getOfferDetail().getVoucherImage());

        if (offer_Voucher.getOfferDetail().getDesc() != null) {
            mTvCategoryDetailed.setText(offer_Voucher.getOfferDetail().getDesc());
        } else {
            mTvCategoryDetailed.setVisibility(View.GONE);
        }

        if (offer_Voucher.getOfferDetail().getmUsed()!="1"){
            offer_used_status=true;
        }else{
            offer_used_status=false;
        }


        mTermsAndConditionWebLink = offer_Voucher.getOfferDetail().getTermAndCondition();

        textcompanyname.setText(offer_Voucher.getOfferDetail().getCompanyName());
                textcompanyconatact.setText(offer_Voucher.getOfferDetail().getCompanyContact());
                textcompanyemail.setText(offer_Voucher.getOfferDetail().getCompanyEmail());
                textexpire.setText(getString(R.string.expirydate)+ " "+ offer_Voucher.getOfferDetail().getExpiryDate()+ "*");

    }


    private void requestPermission() {
        // No explanation needed, we can request the permission.
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, Constant.MY_PERMISSIONS_REQUEST_READ_PHONE_STATE);
    }


    private void handleCallPermission(String aPhoneNumber) {

        if (PermissionUtil.isPermissionGranted(this, Manifest.permission.CALL_PHONE)) {
            dialNumber(aPhoneNumber);
        } else {
            requestPermission();
        }
    }

    private void openBrowserLink() {
        if (!mTermsAndConditionWebLink.isEmpty()){
            try {
                Uri uri = Uri.parse(mTermsAndConditionWebLink);
                Intent myIntent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(myIntent);
            } catch (ActivityNotFoundException e) {
                Toast.makeText(this, "No application can handle this request."
                        + " Please install a web browser", Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }
        }
    }


    private void openBrowserLink_butinah() {

            try {
                Uri uri = Uri.parse("http://www.butinahcharters.com/contact-us/");
                Intent myIntent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(myIntent);
            } catch (ActivityNotFoundException e) {
                Toast.makeText(this, "No application can handle this request."
                        + " Please install a web browser", Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }

    }

   private void dialNumber(String aPhoneNumber) {
       Intent intent = new Intent(Intent.ACTION_DIAL);
       intent.setData(Uri.parse("tel:" + aPhoneNumber));
       if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
           //    ActivityCompat#requestPermissions
           // here to request the missing permissions, and then overriding
           //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
           //   int[] grantResults)
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

    private void loadDisplayImage(OfferDetail offer_detail_local) {
        try {
            String selectedImageStr = offer_detail_local.getMimage_inner();
            String imagePath = selectedImageStr.trim();
            String newPathStr = imagePath.replaceAll(" ", "%20");
//            mSimpleDraweeView.setImageURI(Uri.parse(newPathStr));

            Glide.with(mContext)
                    .load(newPathStr).placeholder(R.drawable.ic_placeholder)
                    .into(mSimpleDraweeView);



        } catch (Exception e) {
            final int sdk = Build.VERSION.SDK_INT;
            if (sdk < Build.VERSION_CODES.JELLY_BEAN) {

                mSimpleDraweeView.setBackgroundDrawable(getResources().getDrawable(R.drawable.ic_placeholder));
            } else {
                mSimpleDraweeView.setBackground(ContextCompat.getDrawable(this, R.drawable.ic_placeholder));
            }
        }
    }


    private void load_Barcode_Image( String pathurl) {
        try {
//            String selectedImageStr = offer_detail_local.getImage();

            String imagePath = pathurl.trim();
            String newPathStr = imagePath.replaceAll(" ", "%20");
            Log.e("newPathStr","newPathStr------->"+newPathStr);
            iv_barcode_display.setImageURI(Uri.parse(newPathStr));
        } catch (Exception e) {
            final int sdk = Build.VERSION.SDK_INT;
            if (sdk < Build.VERSION_CODES.JELLY_BEAN) {

                iv_barcode_display.setBackgroundDrawable(getResources().getDrawable(R.drawable.ic_placeholder));
            } else {
                iv_barcode_display.setBackground(ContextCompat.getDrawable(this, R.drawable.ic_placeholder));
            }
        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    public void handleMoreVoucherClick(int aMenuIndex) {
        Intent intent = new Intent(mContext, HomeActivity.class);
        intent.putExtra(Constant.INTENT_MENU_INDEX_KEY, aMenuIndex);
        intent.putExtra(Constant.INTENT_OFFER_ID, offerId);
        intent.putExtra(Constant.SCREEN_CALL_INDEX, Constant.INTENT_SCREEN_CALL_BY_MORE_OFFER);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
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

    private void hideProgressBar() {
        if (mProgressBarShowing) {
            mProgressBarLayout.setVisibility(View.GONE);
            mProgressBarShowing = false;
        }
    }

    private void showProgressBar() {
        if (!mProgressBarShowing) {
            mProgressBarLayout.setVisibility(View.VISIBLE);
            mProgressBarShowing = true;
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

                } else if (intent.getAction().equals(Constant.PUSH_NOTIFICATION)) {
                    // new push notification is received

                    String message = intent.getStringExtra(Constant.INTENT_NOTIFICATION_MSG);
                    String type = intent.getStringExtra(Constant.INTENT_NOTIFICATION_TYPE);
                    String id = intent.getStringExtra(Constant.INTENT_NOTIFICATION_ID);
                    String imageUrl = intent.getStringExtra(Constant.INTENT_NOTIFICATION_IMAGE_URL);
                    String title = intent.getStringExtra(Constant.INTENT_NOTIFICATION_TITLE);
                    System.out.println("???message = " + message);
                    // UIUtils.AlertMsgWithOk(HomeActivity.this, title, message, R.drawable.ic_home_logo);

                    AppData.getInstance().showDialog(OfferDetailedActivity.this, title, message, imageUrl, type, id);

                }
            }
        };
    }


    @Override
    protected void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);

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
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
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
                dialNumber(textcompanyconatact.getText().toString().trim());
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

        }
        mDrawer.closeDrawer(GravityCompat.END);
        return true;
    }

    private void startHttpRequestForoffer_voucher_used(final int offerId) {
//        https://www.uaet10.com/webservices/offer_voucher_used?device_id=9999&offer_id=55
        final String androidDeviceId = Settings.Secure.getString(mContext.getContentResolver(), Settings.Secure.ANDROID_ID);

//        https://www.uaet10.com/webservices/offer_voucher?device_id=1&offer_id=36
        if (Utils.isConnectingToInternet(mContext)) {
            String baseUrl = Constant.API_OFFER_DETAILED_USED;
            Log.e(">>>baseUrl = ","------>" + baseUrl);
            showProgressBar();
            StringRequest strRequest = new StringRequest(Request.Method.POST, baseUrl,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.e("!!!!API_OFFER_DETAILED_USED = " ,"------>"+ response);
                            try {
                                Gson gson = new GsonBuilder().create();
                                JsonParser jsonParser = new JsonParser();
                                JsonObject jsonResp = jsonParser.parse(response).getAsJsonObject();

                                VoucherUsed voucherUsed = gson.fromJson(jsonResp, VoucherUsed.class);
                                hideProgressBar();
                                if (voucherUsed != null) {

                                    if (Utils.isStatusSuccess(""+voucherUsed.getStatus())) {
                                        UIUtils.showToast(mContext, voucherUsed.getMessage());
                                        offer_used_status=true;
                                        openBrowserLink_butinah();
                                    } else {
                                        UIUtils.showToast(mContext, voucherUsed.getMessage());
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

                    params.put(Constant.OFFER_DETAILED_ID_KEY, "" + offerId);
                    params.put(Constant.JSON_DEVICE_ID_KEY, ""+androidDeviceId);
                    Log.e(">>>param = ","------>" + params.toString());
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


}
