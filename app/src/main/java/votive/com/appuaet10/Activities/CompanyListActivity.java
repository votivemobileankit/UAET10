package votive.com.appuaet10.Activities;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
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

import java.util.HashMap;
import java.util.Map;

import votive.com.appuaet10.Beans.ContactUsResponse;
import votive.com.appuaet10.R;
import votive.com.appuaet10.Utilities.AppController;
import votive.com.appuaet10.Utilities.AppData;
import votive.com.appuaet10.Utilities.Constant;
import votive.com.appuaet10.Utilities.StorageUtils;
import votive.com.appuaet10.Utilities.UIUtils;
import votive.com.appuaet10.Utilities.Utils;

import static android.content.ContentValues.TAG;


public class CompanyListActivity extends AppCompatActivity {

    private Context mContext;
    private EditText mETUserName;
    private EditText mETUserCompanyName;
    private EditText mETMobileNo;
    private RelativeLayout mProgressBarLayout;
    private boolean mProgressBarshowing = false;
    private BroadcastReceiver mRegistrationBroadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_list);
        mContext = this;
        changeStatusBarColor();
        UIUtils.hideKeyBoard(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(true);
            UIUtils.setTitleWithFont(this, getSupportActionBar(), AppData.FONT_OPEN_SANS_BOLD, getString(R.string.ListCompanyText));
        }

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        initBroadCastReceiver();
        init();
    }

    private void init() {
        mETUserName = (EditText) findViewById(R.id.et_user_name);
        mETUserCompanyName = (EditText) findViewById(R.id.et_compamy_name);
        mETMobileNo = (EditText) findViewById(R.id.et_mobile);
        mProgressBarLayout = (RelativeLayout) findViewById(R.id.rl_progressBar);
        Button buttonSend = (Button) findViewById(R.id.b_contact_send);

        buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateData();
            }
        });
        buttonSend.setTypeface(AppData.getInstance().getTypeFaceForType(mContext, AppData.FONT_OPEN_SANS_BOLD));



    }

    private void validateData() {

        String name = mETUserName.getText().toString().trim();
        String companyName = mETUserCompanyName.getText().toString().trim();
        String contact = mETMobileNo.getText().toString().trim();

        if (name.isEmpty()) {
            UIUtils.showToast(mContext, getString(R.string.CompanyUserNameEmptyMsg));
            mETUserName.requestFocus();
        } else if (companyName.isEmpty()) {
            UIUtils.showToast(mContext, getString(R.string.CompanyNameEmptyMsg));
            mETUserCompanyName.requestFocus();
            mETUserName.clearFocus();
            mETMobileNo.clearFocus();
        } else if (contact.isEmpty()) {
            UIUtils.showToast(mContext, getString(R.string.CompanyMobNoEmptyMsg));
            mETMobileNo.requestFocus();
            mETUserName.clearFocus();
            mETUserCompanyName.clearFocus();

        } else {

            mETUserCompanyName.clearFocus();
            mETUserName.clearFocus();
            mETMobileNo.clearFocus();

            startHttpRequestCompanyList();
        }
    }

    private void startHttpRequestCompanyList() {

        if (Utils.isConnectingToInternet(mContext)) {
            String baseUrl = Constant.API_COMPANY_INFO;
            showProgressBar();
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

                                ContactUsResponse contactResponse = gson.fromJson(jsonResp, ContactUsResponse.class);
                                hideProgressBar();
                                if (contactResponse != null) {
                                    if (Utils.isStatusSuccess(contactResponse.getmStatus())) {
                                        successfulllyMessage(contactResponse.getmMsg());
                                    } else {
                                        UIUtils.showToast(mContext, contactResponse.getmMsg());
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
                    params.put(Constant.JSON_COMPANY_USER_KEY, mETUserName.getText().toString().trim());
                    params.put(Constant.JSON_COMPANY_NAME_KEY, mETUserCompanyName.getText().toString().trim());
                    params.put(Constant.JSON_COMPANY_CONTACT_KEY, mETMobileNo.getText().toString().trim());
                    params.put(Constant.JSON_DEVICE_ID_KEY, androidDeviceId);
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
        showPopuDialog(s);
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


    private void showPopuDialog(String s) {
        final Dialog openDialog = new Dialog(mContext);
        openDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        openDialog.setContentView(R.layout.thanku_popup_dialog);
        openDialog.setCancelable(false);
        openDialog.setCanceledOnTouchOutside(false);
        openDialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
        openDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        TextView textView = (TextView) openDialog.findViewById(R.id.tv_notification_Message);

        if (s != null) {
            textView.setText(s);
        } else {
            textView.setText(getResources().getString(R.string.contactUsMsg));
        }
        Button dialogImage = (Button) openDialog.findViewById(R.id.imageView_custom_dialog_close);
        dialogImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog.dismiss();
                mETUserName.setText("");
                mETUserCompanyName.setText("");
                mETMobileNo.setText("");
                finish();
            }
        });

        Button okBtn = (Button) openDialog.findViewById(R.id.positive_button);
        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                openDialog.dismiss();
                mETUserName.setText("");
                mETUserCompanyName.setText("");
                mETMobileNo.setText("");
                AppData.getInstance().setFirstLaunch(mContext);
                finish();
            }
        });
        openDialog.show();
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

                    AppData.getInstance().showDialog(CompanyListActivity.this, title, message, imageUrl, type, id);

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

    }
}
