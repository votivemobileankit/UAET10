package votive.com.appuaet10.Activities;


import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import votive.com.appuaet10.Utilities.PermissionUtil;
import votive.com.appuaet10.Utilities.UIUtils;
import votive.com.appuaet10.Utilities.Utils;

import static android.content.ContentValues.TAG;

public class ContactUsFragment extends Fragment {

    private View mMainView;
    private Context mContext;
    private boolean mProgressBarshowing = false;
    private int mScreenCallIndex;
    private int mOfferId;
    private RelativeLayout mProgressBarLayout;

    private EditText mEtName;
    private EditText mEtEmail;
    private EditText mEtMessage;
    private EditText mEtContactNo;

    TextView contactus_number;

    public ContactUsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mContext = getActivity();
        mMainView = inflater.inflate(R.layout.fragment_contact_us, container, false);
        if (getArguments() != null){
            mOfferId = getArguments().getInt(Constant.INTENT_OFFER_ID);
            mScreenCallIndex = getArguments().getInt(Constant.SCREEN_CALL_INDEX);

            Log.e("mOfferId",""+mOfferId);
            Log.e("mScreenCallIndex",""+mScreenCallIndex);
        }

        init();
        return mMainView;
    }

    private void init() {
        mProgressBarLayout = (RelativeLayout) mMainView.findViewById(R.id.rl_progressBar);

        mEtName = (EditText) mMainView.findViewById(R.id.et_contact_name);
        mEtEmail = (EditText) mMainView.findViewById(R.id.et_contact_email);
        mEtMessage = (EditText) mMainView.findViewById(R.id.et_contact_message);
        Button buttonSend = (Button) mMainView.findViewById(R.id.b_contact_send);
        mEtContactNo = (EditText) mMainView.findViewById(R.id.et_contact_no);
        contactus_number = (TextView) mMainView.findViewById(R.id.contactus_number);
        buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateData();
            }
        });
        buttonSend.setTypeface(AppData.getInstance().getTypeFaceForType(getActivity(), AppData.FONT_OPEN_SANS_BOLD));
        contactus_number.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleCallPermission();
            }
        });

    }

    private void handleCallPermission() {
        if (PermissionUtil.isPermissionGranted(getActivity(), Manifest.permission.CALL_PHONE)) {
            dialNumber();
        } else {
            requestPermission();
        }
    }

    private void dialNumber() {
        String number = contactus_number.getText().toString();
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + number));
        startActivity(intent);
    }

    private void requestPermission() {
        // No explanation needed, we can request the permission.
        requestPermissions(new String[]{Manifest.permission.CALL_PHONE}, Constant.MY_PERMISSIONS_REQUEST_READ_PHONE_STATE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == Constant.MY_PERMISSIONS_REQUEST_READ_PHONE_STATE) {

            boolean permissionDenied = false;

            for (int i = 0; i < permissions.length; i++) {
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



    }

    private void showDialogForPermissionSetting() {
        new AlertDialog.Builder(getActivity())
                .setTitle(getString(R.string.PermissionGoSettingTitle))
                .setMessage(getString(R.string.PermissionGoSettingMsg))
                .setCancelable(true)
                .setPositiveButton(getString(R.string.PermissionGoSettingTxt), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        final Intent i = new Intent();
                        i.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        i.addCategory(Intent.CATEGORY_DEFAULT);
                        i.setData(Uri.parse("package:" + getActivity().getPackageName()));
                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        i.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                        i.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                        getActivity().startActivity(i);
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    private void showDialogForPermission() {
        new AlertDialog.Builder(getActivity())
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


    private void validateData() {

        String name = mEtName.getText().toString().trim();
        String email = mEtEmail.getText().toString().trim();
        String message = mEtMessage.getText().toString().trim();
        String contact = mEtContactNo.getText().toString().trim();

        if (name.isEmpty()) {
            UIUtils.showToast(mContext, getString(R.string.ContactUsNameEmptyMsg));
            mEtName.requestFocus();
        } else if (email.isEmpty()) {
            UIUtils.showToast(mContext, getString(R.string.ContactUsEmailEmptyMsg));
            mEtEmail.requestFocus();
            mEtName.clearFocus();
        } else if (!Utils.isValidEmail(email)) {
            UIUtils.showToast(mContext, getString(R.string.ContactUsInvalidEmailMsg));
            mEtEmail.requestFocus();
            mEtName.clearFocus();
        }
        else if (contact.isEmpty()) {
            UIUtils.showToast(mContext, getString(R.string.BrandCompanyConatct));
            mEtContactNo.requestFocus();
            mEtName.clearFocus();
            mEtEmail.clearFocus();

        }
        else if (message.isEmpty()) {
            UIUtils.showToast(mContext, getString(R.string.ContactUsMessageEmptyMsg));
            mEtMessage.requestFocus();
            mEtEmail.clearFocus();
            mEtName.clearFocus();
            mEtContactNo.clearFocus();
        } else {
            mEtMessage.clearFocus();
            mEtEmail.clearFocus();
            mEtName.clearFocus();
            mEtContactNo.clearFocus();

            if (mScreenCallIndex != -1 && mScreenCallIndex == Constant.INTENT_SCREEN_CALL_BY_MORE_OFFER && mOfferId != -1){
                startHttpRequestForMoreOffer();
            } else {
                startHttpRequestContact();
            }
        }
    }


    public void startHttpRequestContact() {

        if (Utils.isConnectingToInternet(mContext)) {
            String baseUrl = Constant.API_CONTACT_US;
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

                                ContactUsResponse contactResponse = gson.fromJson(jsonResp, ContactUsResponse.class);
                                hideProgressBar();
                                if (contactResponse != null) {
                                    if (Utils.isStatusSuccess(contactResponse.getmStatus())) {
                                        showSuccessPopUpDialog("");
                                    } else {
//                                        UIUtils.showToast(mContext, contactResponse.getmMsg());
                                        showSuccessPopUpDialog(contactResponse.getmMsg());
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
                    final String androidDeviceId = Settings.Secure.getString(mContext.getContentResolver(), Settings.Secure.ANDROID_ID);
                    Map<String, String> params = new HashMap<>();
                    params.put(Constant.JSON_CONTACT_US_NAME_KEY, mEtName.getText().toString().trim());
                    params.put(Constant.JSON_CONTACT_US_EMAIL_KEY, mEtEmail.getText().toString().trim());
                    params.put(Constant.JSON_CONTACT_US_MESSAGE_KEY, mEtMessage.getText().toString().trim());
                    params.put(Constant.JSON_CONTACT_US_CONTACT_KEY, ""+mEtContactNo.getText().toString().trim());
                    params.put(Constant.OFFER_DETAILED_ID_KEY, "" + mOfferId);
                    params.put(Constant.JSON_DEVICE_ID_KEY, ""+androidDeviceId);

                    Log.e("c params","-------->"+params.toString());
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


    private void showSuccessPopUpDialog(String aMsg) {
        final Dialog openDialog = new Dialog(mContext);
        openDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        openDialog.setContentView(R.layout.thanku_popup_dialog);
        openDialog.setCancelable(false);
        openDialog.setCanceledOnTouchOutside(false);
        openDialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
        openDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        TextView textView = (TextView) openDialog.findViewById(R.id.tv_notification_Message);
        if (!aMsg.isEmpty()) {
            textView.setText(aMsg);
        } else {
            textView.setText(getResources().getString(R.string.contactUsMsg));
        }

        Button dialogImage = (Button) openDialog.findViewById(R.id.imageView_custom_dialog_close);
        dialogImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog.dismiss();
                mEtName.setText("");
                mEtEmail.setText("");
                mEtMessage.setText("");
                mEtContactNo.setText("");
                if (mScreenCallIndex != -1 && mScreenCallIndex == Constant.INTENT_SCREEN_CALL_BY_MORE_OFFER && mOfferId != -1){
                    handleSideMenuClick(Constant.MENU_OPTION_OFFER_INDEX);
                }
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
                mEtMessage.setText("");
                if (mScreenCallIndex != -1 && mScreenCallIndex == Constant.INTENT_SCREEN_CALL_BY_MORE_OFFER && mOfferId != -1){
                    handleSideMenuClick(Constant.MENU_OPTION_OFFER_INDEX);
                }
            }
        });

        openDialog.setOnKeyListener(new Dialog.OnKeyListener() {

            @Override
            public boolean onKey(DialogInterface arg0, int keyCode,
                                 KeyEvent event) {
                // TODO Auto-generated method stub
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    openDialog.dismiss();
                    mEtName.setText("");
                    mEtContactNo.setText("");
                    mEtEmail.setText("");
                    mEtMessage.setText("");
                    if (mScreenCallIndex != -1 && mScreenCallIndex == Constant.INTENT_SCREEN_CALL_BY_MORE_OFFER && mOfferId != -1){
                        handleSideMenuClick(Constant.MENU_OPTION_OFFER_INDEX);
                    }
                }
                return true;
            }
        });

        openDialog.show();
    }

    public void startHttpRequestForMoreOffer() {

        final String androidDeviceId = Settings.Secure.getString(mContext.getContentResolver(), Settings.Secure.ANDROID_ID);

        if (Utils.isConnectingToInternet(mContext)) {
            String baseUrl = Constant.API_MORE_OFFER;
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

                                ContactUsResponse contactResponse = gson.fromJson(jsonResp, ContactUsResponse.class);
                                hideProgressBar();
                                if (contactResponse != null) {
                                    if (Utils.isStatusSuccess(contactResponse.getmStatus())) {
                                        showSuccessPopUpDialog(contactResponse.getmMsg());
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
                    params.put(Constant.JSON_CONTACT_US_NAME_KEY, mEtName.getText().toString().trim());
                    params.put(Constant.JSON_CONTACT_US_EMAIL_KEY, mEtEmail.getText().toString().trim());
                    params.put(Constant.JSON_CONTACT_US_MESSAGE_KEY, mEtMessage.getText().toString().trim());
                    params.put(Constant.JSON_CONTACT_US_CONTACT_KEY, "");
                    params.put(Constant.OFFER_DETAILED_ID_KEY, "" + mOfferId);
                    params.put(Constant.JSON_DEVICE_ID_KEY, ""+androidDeviceId);
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

    public void handleSideMenuClick(int aMenuIndex) {
        Intent intent = new Intent(mContext, HomeActivity.class);
        intent.putExtra(Constant.INTENT_MENU_INDEX_KEY, aMenuIndex);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

}
