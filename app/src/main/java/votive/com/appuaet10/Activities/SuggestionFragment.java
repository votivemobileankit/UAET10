package votive.com.appuaet10.Activities;


import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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

import votive.com.appuaet10.Beans.BusinessEnquiryResponse;
import votive.com.appuaet10.R;
import votive.com.appuaet10.Utilities.AppController;
import votive.com.appuaet10.Utilities.AppData;
import votive.com.appuaet10.Utilities.Constant;
import votive.com.appuaet10.Utilities.UIUtils;
import votive.com.appuaet10.Utilities.Utils;

import static android.content.ContentValues.TAG;

public class SuggestionFragment extends Fragment {

    private View mMainView;
    private boolean mProgressBarshowing = false;

    private RelativeLayout mProgressBarLayout;

    private EditText mEtName;
    private EditText mEtEmail;
    private EditText mEtSuggestion;
    private EditText mEtContact;
    private Context mContext;


    public SuggestionFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mContext = getActivity();
        mMainView = inflater.inflate(R.layout.fragment_suggestion, container, false);
        init();
        return mMainView;
    }

    private void init() {
        mProgressBarLayout = (RelativeLayout) mMainView.findViewById(R.id.rl_progressBar);

        mEtName = (EditText) mMainView.findViewById(R.id.et_suggestion_name);
        mEtEmail = (EditText) mMainView.findViewById(R.id.et_suggestion_email);
        mEtSuggestion = (EditText) mMainView.findViewById(R.id.et_suggestion_message);
        mEtContact = (EditText) mMainView.findViewById(R.id.et_suggestion_contact);
        Button buttonSend = (Button) mMainView.findViewById(R.id.b_suggestion_send);

        buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateData();
            }
        });
        buttonSend.setTypeface(AppData.getInstance().getTypeFaceForType(getActivity(), AppData.FONT_OPEN_SANS_BOLD));


    }

    private void validateData() {

        String name = mEtName.getText().toString().trim();
        String email = mEtEmail.getText().toString().trim();
        String contact = mEtContact.getText().toString().trim();
        String message = mEtSuggestion.getText().toString().trim();


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
        } else if (contact.isEmpty()) {
            UIUtils.showToast(mContext, getString(R.string.BrandCompanyConatct));
            mEtEmail.requestFocus();
            mEtName.requestFocus();
            mEtContact.requestFocus();
        } else if (message.isEmpty()) {
            UIUtils.showToast(mContext, getString(R.string.SuggestionListMsg));
            mEtSuggestion.requestFocus();
            mEtEmail.clearFocus();
            mEtName.clearFocus();
            mEtContact.clearFocus();
        } else {
            mEtSuggestion.clearFocus();
            mEtEmail.clearFocus();
            mEtName.clearFocus();
            mEtContact.clearFocus();
            startHttpRequestForSuggestion();
        }
    }

    private void startHttpRequestForSuggestion() {

        if (Utils.isConnectingToInternet(mContext)) {
            String baseUrl = Constant.API_SUGGESTION;
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
                    params.put(Constant.JSON_SUGGESTION_MESSAGE, mEtSuggestion.getText().toString());
                    params.put(Constant.JSON_BRAND_CONTACT_NUMBER, mEtContact.getText().toString());
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


    private void showPopuDialog() {
        final Dialog openDialog = new Dialog(mContext);
        openDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        openDialog.setContentView(R.layout.thanku_popup_dialog);
        openDialog.setCancelable(false);
        openDialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
        openDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        Button dialogImage = (Button) openDialog.findViewById(R.id.imageView_custom_dialog_close);
        Button okBtn = (Button) openDialog.findViewById(R.id.positive_button);

        TextView tv_notification_Message = (TextView) openDialog.findViewById(R.id.tv_notification_Message);
        tv_notification_Message.setText(R.string.SuggestionTxtMsg);
        dialogImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEtName.setText("");
                mEtEmail.setText("");
                mEtContact.setText("");
                mEtSuggestion.setText("");
                openDialog.dismiss();
            }
        });

        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog.dismiss();
                mEtName.setText("");
                mEtContact.setText("");
                mEtEmail.setText("");
                mEtSuggestion.setText("");
            }
        });

        openDialog.setOnKeyListener(new Dialog.OnKeyListener() {

            @Override
            public boolean onKey(DialogInterface arg0, int keyCode,
                                 KeyEvent event) {
                // TODO Auto-generated method stub
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    openDialog.dismiss();
                }
                return true;
            }
        });
        openDialog.show();
    }
}
