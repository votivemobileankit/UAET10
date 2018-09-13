package votive.com.appuaet10.Activities;

import android.app.Activity;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.github.javiersantos.appupdater.AppUpdaterUtils;
import com.github.javiersantos.appupdater.enums.AppUpdaterError;
import com.github.javiersantos.appupdater.enums.UpdateFrom;
import com.github.javiersantos.appupdater.objects.Update;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

import votive.com.appuaet10.Beans.CategoryResponse;
import votive.com.appuaet10.Beans.NotificationResponseBean;
import votive.com.appuaet10.Beans.Popup_Flag;
import votive.com.appuaet10.R;
import votive.com.appuaet10.Utilities.AppController;
import votive.com.appuaet10.Utilities.AppData;
import votive.com.appuaet10.Utilities.Constant;
import votive.com.appuaet10.Utilities.UIUtils;
import votive.com.appuaet10.Utilities.Utils;


public class SplashActivity extends AppCompatActivity {

    Context mContext;
    private static final String TAG = SplashActivity.class.getSimpleName();
    private LinearLayout mRlBottomLink;
    private Handler mHandler;
    private CustomRunnable mRunnable;
    private static boolean isActiveScreens = false;
    private BroadcastReceiver mRegistrationBroadcastReceiver;

    private boolean mTimerCompleted = false;
    private boolean mRequestCompleted = false;
    private String notifiType;
    private String notifyCategoryId;
    private LinearLayout mLLTopLeft;
    private LinearLayout mLLTopCenter;
    private LinearLayout mLLCenter;
    private LinearLayout mLLTopRight;
    private LinearLayout mLLRight;
    private boolean isNewUpdate = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        mContext = this;
        changeStatusBarColor();
        printKeyHash(this);

        //check version
        checkUpdate();
        startRequestForCategoryInfo();
        get_Flag_forPopup();

        mRlBottomLink = (LinearLayout) findViewById(R.id.ll_bottom);
        mLLTopLeft = (LinearLayout) findViewById(R.id.ll_topLeft);
        mLLTopCenter = (LinearLayout) findViewById(R.id.ll_topCenter);
        mLLCenter = (LinearLayout) findViewById(R.id.ll_middile);
        mLLRight = (LinearLayout) findViewById(R.id.ll_topRight);
        mLLTopRight = (LinearLayout) findViewById(R.id.right);
        mHandler = new Handler();


        mLLTopCenter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openBrowserUKLink();
            }
        });
        mLLCenter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openBrowserLink();
            }
        });
        mLLRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openBrowserUSALink();
            }
        });

        mRlBottomLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openBrowserLink();
            }
        });
        if (getIntent().getExtras() != null) {
            notifiType = getIntent().getStringExtra(Constant.INTENT_NOTIFICATION_TYPE);
            notifyCategoryId = getIntent().getStringExtra((Constant.INTENT_NOTIFICATION_ID));
        }


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
                    AppData.getInstance().showDialog(SplashActivity.this, title, message, imageUrl, type, id);
                }
            }
        };
        displayFirebaseRegId();
    }

    private void displayFirebaseRegId() {

        SharedPreferences pref = getApplicationContext().getSharedPreferences(Constant.SHARED_PREF_FIREBASE_KEY, 0);
        String regId = pref.getString("regId", null);
        System.out.println("????regId = " + regId);

        if (!TextUtils.isEmpty(regId) || regId != null) {
            startHttpRequestForNotification(regId);
        } else {
            String refreshedToken = FirebaseInstanceId.getInstance().getToken();
            SharedPreferences.Editor editor = pref.edit();
            editor.putString("regId", refreshedToken);
            editor.commit();

            startHttpRequestForNotification(refreshedToken);
        }
    }

    private void startHttpRequestForNotification(final String refreshedToken) {
        if (Utils.isConnectingToInternet(mContext)) {
            String baseUrl = Constant.API_DEVICE_KEY;
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
                                    System.out.println("categoryResponse = " + categoryResponse.getmMsg());
                                } else {
                                    System.out.println("categoryResponse = " + categoryResponse.getmMsg());
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
            strRequest.setTag(TAG);
            AppController.getInstance().addToRequestQueue(strRequest);
        }
    }

    private void get_Flag_forPopup() {
        String baseUrl = Constant.API_POPUP_FLAG_CONTENT;
//        final String androidDeviceId = Settings.Secure.getString(mContext.getContentResolver(), Settings.Secure.ANDROID_ID);
        StringRequest strRequest = new StringRequest(Request.Method.POST, baseUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("get_Flag_forPopup", "get_Flag_forPopup response --> " + response);
                        try {
                            Gson gson = new GsonBuilder().create();
                            JsonParser jsonParser = new JsonParser();
                            JsonObject jsonResp = jsonParser.parse(response).getAsJsonObject();

                            Popup_Flag categoryResponse = gson.fromJson(jsonResp, Popup_Flag.class);
                            if (categoryResponse != null) {
                                System.out.println("categoryResponse = " + categoryResponse.getFlag());
                                if (categoryResponse.getFlag().equalsIgnoreCase("yes")) {
                                    AppData.getInstance().set_Popup_flag(mContext, true);
                                } else {
                                    AppData.getInstance().set_Popup_flag(mContext, false);
                                }
                            }
//                            else {
//                                System.out.println("categoryResponse = " + categoryResponse.getFlag());
//                            }
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
//                params.put("key", refreshedToken);
//                params.put("device_id", androidDeviceId);
                System.out.println(">>>params = " + params);
                return params;
            }
        };
        strRequest.setShouldCache(false);
        strRequest.setTag(TAG);
        AppController.getInstance().addToRequestQueue(strRequest);

    }


    public void handleOfferAndCategory(int aMenuIndex, Context aContext) {
        Intent intent = new Intent(aContext, HomeActivity.class);
        intent.putExtra(Constant.INTENT_MENU_INDEX_KEY, aMenuIndex);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        aContext.startActivity(intent);
        finish();
    }

    private void gotoMain() {
        if (mRequestCompleted && mTimerCompleted) {

            if (checkUpdate()){
                showPopUp();
            } else {
                Intent intent;
                if (notifiType != null) {
                    if (notifiType.equals(Constant.NOTIFICATION_TYPE_FOR_BUSINESS_ENQUIRY)) {
                        intent = new Intent(mContext, BusinessDetailedBySearchActivity.class);
                        intent.putExtra(Constant.SCREEN_CALL_INDEX, Constant.NOTIFICATION_BUSINESS_BY_HOME);
                        intent.putExtra(Constant.INTENT_BUSINESS_ID, notifyCategoryId);
                        startActivity(intent);

                    } else if (notifiType.equals(Constant.NOTIFICATION_TYPE_FOR_CATEGORY_LIST)) {

                        Intent intent1 = new Intent(mContext, CategoryItemListActivity.class);
                        intent1.putExtra(Constant.INTENT_CATEGORY_ITEM_ID, notifyCategoryId);
                        intent1.putExtra(Constant.SCREEN_CALL_INDEX, Constant.NOTIFICATION_CATEGORY_ID_FROM_HOME);
                        startActivity(intent1);
                        finish();

                        // handleOfferAndCategory(Constant.MENU_OPTION_CATEGORY_INDEX, mContext);

                    } else if (notifiType.equals(Constant.NOTIFICATION_TYPE_FOR_OFFER)) {
                        handleOfferAndCategory(Constant.MENU_OPTION_OFFER_INDEX, mContext);

                    } else if (notifiType.equals(Constant.NOTIFICATION_TYPE_FOR_DEFAULT)) {
                        intent = new Intent(SplashActivity.this, HomeActivity.class);
                        intent.putExtra(Constant.INTENT_MENU_INDEX_KEY, Constant.MENU_OPTION_HOME_INDEX);
                        startActivity(intent);
                        finish();
                    } else {
                        intent = new Intent(SplashActivity.this, HomeActivity.class);
                        intent.putExtra(Constant.INTENT_MENU_INDEX_KEY, Constant.MENU_OPTION_HOME_INDEX);
                        startActivity(intent);
                        finish();

                    }
                } else {
                    intent = new Intent(SplashActivity.this, HomeActivity.class);
                    intent.putExtra(Constant.INTENT_MENU_INDEX_KEY, Constant.MENU_OPTION_HOME_INDEX);
                    startActivity(intent);
                    finish();
                }
            }
        }
    }

    private void changeStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.colorBlack));
        }
    }

    private void openBrowserLink() {

        try {
            Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.app_links)));
            startActivity(myIntent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(this, "No application can handle this request."
                    + " Please install a web browser", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }


    private void openBrowserUSALink() {

        try {
            Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.usat10.com/"));
            startActivity(myIntent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(this, "No application can handle this request."
                    + " Please install a web browser", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }


    private void openBrowserUKLink() {

        try {
            Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.ukt10.com/"));
            startActivity(myIntent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(this, "No application can handle this request."
                    + " Please install a web browser", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        isActiveScreens = true;
        mRunnable = new CustomRunnable();
        new Handler().postDelayed(mRunnable, Constant.SPLASH_TIME_OUT);
    }

    @Override
    protected void onPause() {
        super.onPause();
        isActiveScreens = false;
        if (mHandler != null) {
            mHandler.removeCallbacks(mRunnable);
            mRunnable = null;
        }
    }

    private class CustomRunnable implements Runnable {

        @Override
        public void run() {
            if (isActiveScreens) {
                mTimerCompleted = true;
                gotoMain();
                isActiveScreens = false;
            }
        }
    }

    private void startRequestForCategoryInfo() {
        String baseUrl = Constant.API_CATEGORY_LIST;
        StringRequest strRequest = new StringRequest(Request.Method.POST, baseUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println("!!!!response = " + response);
                        try {
                            Gson gson = new GsonBuilder().create();
                            JsonParser jsonParser = new JsonParser();
                            JsonObject jsonResp = jsonParser.parse(response).getAsJsonObject();

                            CategoryResponse categoryResponse = gson.fromJson(jsonResp, CategoryResponse.class);
                            if (categoryResponse != null && categoryResponse.getmCategoryList() != null && categoryResponse.getmCategoryList().size() > 0) {
                                if (Utils.isStatusSuccess(categoryResponse.getmStatus())
                                        && categoryResponse.getmCategoryList() != null && categoryResponse.getmCategoryList().size() > 0) {
                                    AppData.getInstance().setCategoryList(mContext, categoryResponse.getmCategoryList());
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();

                        } finally {
                            mRequestCompleted = true;
                            gotoMain();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        mRequestCompleted = true;
                        gotoMain();
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


    public static String printKeyHash(Activity context) {
        PackageInfo packageInfo;
        String key = null;
        try {
            //getting application package name, as defined in manifest
            String packageName = context.getApplicationContext().getPackageName();

            //Retriving package info
            packageInfo = context.getPackageManager().getPackageInfo(packageName,
                    PackageManager.GET_SIGNATURES);

            System.out.println("Package Name=" + context.getApplicationContext().getPackageName());

            for (Signature signature : packageInfo.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                key = new String(Base64.encode(md.digest(), 0));

                // String key = new String(Base64.encodeBytes(md.digest()));
                System.out.println("Key Hash=" + key);
            }
        } catch (PackageManager.NameNotFoundException e1) {
            System.out.println("Name not found" + e1.toString());
        } catch (NoSuchAlgorithmException e) {
            System.out.println("No such an algorithm" + e.toString());
        } catch (Exception e) {
            System.out.println("Exception" + e.toString());
        }

        return key;
    }

    private boolean checkUpdate() {

        AppUpdaterUtils appUpdaterUtils = new AppUpdaterUtils(this)
                //.setUpdateFrom(UpdateFrom.JSON)
                .setUpdateFrom(UpdateFrom.GOOGLE_PLAY)
                //.setUpdateJSON("http://votivedesigner.com/image/update_from.json")
                .withListener(new AppUpdaterUtils.UpdateListener() {
                    @Override
                    public void onSuccess(Update update, Boolean isUpdateAvailable) {
                        Log.d("Latest Version", update.getLatestVersion());
                        Log.d("Latest Version Code", ""+update.getLatestVersionCode());
                        /* Log.d("LatestRelease notes", update.getReleaseNotes());
                        Log.d("LatestURL", ""+update.getUrlToDownload());*/
                        Log.d("Latest Is update available?", Boolean.toString(isUpdateAvailable));

                        isNewUpdate = isUpdateAvailable;

                    }

                    @Override
                    public void onFailed(final AppUpdaterError error) {
                        Log.d("AppUpdater Error", "Something went wrong");
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                UIUtils.showToast(mContext, "Something went wrong");
                            }
                        });
                    }
                });
        appUpdaterUtils.start();

        return isNewUpdate;
    }


    public void openGooglePlayStore() {

        try {
            Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id="+this.getPackageName()));
            startActivity(myIntent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(this, "No application can handle this request."
                    + " Please install a web browser", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }

        finish();
    }

    private void showPopUp() {
        final Dialog openDialog = new Dialog(mContext);
        openDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        openDialog.setContentView(R.layout.custom_update_apk_pop_up_layout);
        openDialog.setCancelable(false);
        openDialog.setCanceledOnTouchOutside(false);
        openDialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
        openDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        Button btnOk = (Button) openDialog.findViewById(R.id.positive_button);
        btnOk.setTypeface(AppData.getInstance().getTypeFaceForType(mContext, AppData.FONT_OPEN_SANS_BOLD));
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGooglePlayStore();
                if (openDialog != null){
                    openDialog.dismiss();
                }
            }
        });

        Button btnClose = (Button) openDialog.findViewById(R.id.close_button);
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (openDialog != null){
                    openDialog.dismiss();
                }
                finish();
            }
        });

        if (openDialog != null){
            openDialog.show();
        }
    }
}
