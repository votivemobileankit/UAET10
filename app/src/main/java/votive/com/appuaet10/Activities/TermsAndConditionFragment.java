package votive.com.appuaet10.Activities;


import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.RelativeLayout;

import votive.com.appuaet10.R;
import votive.com.appuaet10.Utilities.Constant;
import votive.com.appuaet10.Utilities.Utils;

public class TermsAndConditionFragment extends Fragment {

    private View mMainView;
    private Context mContext;
    private boolean mProgressBarshowing = false;
    private RelativeLayout mProgressBarLayout;
    private RelativeLayout mRlMain;

    private RelativeLayout mRlNoInterConnection;
    private Button mTryAgainBtn;

    private WebView webViewTermsAndConditions;

    public TermsAndConditionFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mContext = getActivity();
        mMainView = inflater.inflate(R.layout.fragment_terms_and_condition, container, false);
        init();
        return mMainView;
    }

    private void init() {
        mProgressBarLayout = (RelativeLayout) mMainView.findViewById(R.id.rl_progressBar);
        mRlMain = (RelativeLayout) mMainView.findViewById(R.id.rl_terms__main);
        mRlNoInterConnection = (RelativeLayout) mMainView.findViewById(R.id.rl_terms_condition_no_internet_main);
        mTryAgainBtn = (Button) mMainView.findViewById(R.id.btn_no_internet_retry);
        webViewTermsAndConditions = (WebView) mMainView.findViewById(R.id.tv_terms_and_condition);


        webViewTermsAndConditions.getSettings().setJavaScriptEnabled(true);

        mTryAgainBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startHttpRequestTermsAndCondition();
            }
        });


        final Activity activity = (Activity) mContext;
        webViewTermsAndConditions.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress) {
                // Activities and WebViews measure progress with different scales.
                // The progress meter will automatically disappear when we reach 100%
                showProgressBar();
            }
        });
        webViewTermsAndConditions.setWebViewClient(new WebViewClient() {
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                hideProgressBar();
                mRlNoInterConnection.setVisibility(View.VISIBLE);
                webViewTermsAndConditions.setVisibility(View.GONE);
                mRlMain.setVisibility(View.GONE);
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                showProgressBar();

                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                hideProgressBar();
                super.onPageFinished(view, url);
            }
        });

        startHttpRequestTermsAndCondition();
    }


    public void startHttpRequestTermsAndCondition() {
        //showProgressBar();

        if (Utils.isConnectingToInternet(mContext)) {
            String baseUrl = Constant.API_TERMS_AND_CONDITION;
            hideProgressBar();
            webViewTermsAndConditions.loadUrl(baseUrl);
        } else {
            hideProgressBar();
            mRlNoInterConnection.setVisibility(View.VISIBLE);
            webViewTermsAndConditions.setVisibility(View.GONE);
            mRlMain.setVisibility(View.GONE);
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

}
