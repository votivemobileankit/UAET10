package votive.com.appuaet10.Activities;


import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.RelativeLayout;

import votive.com.appuaet10.R;
import votive.com.appuaet10.Utilities.Constant;
import votive.com.appuaet10.Utilities.Utils;

public class PrivacyPolicyFragment extends Fragment {

    private View mMainView;
    private Context mContext;
    private boolean mProgressBarshowing = false;
    private RelativeLayout mProgressBarLayout;
    private RelativeLayout mRlMain;

    private RelativeLayout mRlNoInterConnection;
    private Button mTryAgainBtn;

    private WebView webViewPrivacyPolicy;

    public PrivacyPolicyFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mContext = getActivity();
        mMainView = inflater.inflate(R.layout.fragment_privacy_policy, container, false);
        init();
        return mMainView;
    }

    private void init() {
        mProgressBarLayout = (RelativeLayout) mMainView.findViewById(R.id.rl_progressBar);
        mRlMain = (RelativeLayout) mMainView.findViewById(R.id.rl_privacy_main);
        webViewPrivacyPolicy = (WebView) mMainView.findViewById(R.id.tv_privacy_policy);
        mRlNoInterConnection = (RelativeLayout) mMainView.findViewById(R.id.rl_privacy_policy_no_internet_main);
        mTryAgainBtn = (Button) mMainView.findViewById(R.id.btn_no_internet_retry);
        webViewPrivacyPolicy.getSettings().setJavaScriptEnabled(true);
        mTryAgainBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startHttpRequestPrivacyPolicy();
            }
        });

        final Activity activity = (Activity) mContext;
        webViewPrivacyPolicy.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress) {
                // Activities and WebViews measure progress with different scales.
                // The progress meter will automatically disappear when we reach 100%
                showProgressBar();
            }
        });
        webViewPrivacyPolicy.setWebViewClient(new WebViewClient() {

            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                hideProgressBar();
                mRlNoInterConnection.setVisibility(View.VISIBLE);
                webViewPrivacyPolicy.setVisibility(View.GONE);
                mRlMain.setVisibility(View.GONE);
            }

            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                hideProgressBar();
                view.loadUrl(request.getUrl().toString());
                return true;
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

        startHttpRequestPrivacyPolicy();

    }


    public void startHttpRequestPrivacyPolicy() {

        if (Utils.isConnectToInternet(mContext)) {
            hideProgressBar();
            webViewPrivacyPolicy.reload();
            String baseUrl = Constant.API_PRIVACY_POLICY;
            webViewPrivacyPolicy.loadUrl(baseUrl);
        } else {
            hideProgressBar();
            mRlNoInterConnection.setVisibility(View.VISIBLE);
            webViewPrivacyPolicy.setVisibility(View.GONE);
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


    /*private class CustomWebViewClient extends WebViewClient {

        @TargetApi(Build.VERSION_CODES.N)
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {

            if (Utils.isConnectToInternet(mContext)) {
                mRlNoInterConnection.setVisibility(View.VISIBLE);
                webViewPrivacyPolicy.setVisibility(View.GONE);
                mRlMain.setVisibility(View.GONE);
            } else {
                view.loadUrl(String.valueOf(request));
            }

            return true;

        }

        @SuppressWarnings("deprecation")
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (Utils.isConnectToInternet(mContext)) {
                mRlNoInterConnection.setVisibility(View.VISIBLE);
                webViewPrivacyPolicy.setVisibility(View.GONE);
                mRlMain.setVisibility(View.GONE);
            } else {
                view.loadUrl(url);
            }
            return true;
        }
    }*/
}
