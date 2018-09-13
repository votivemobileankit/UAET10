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

/**
 * A simple {@link Fragment} subclass.
 */
public class AboutUsFragment extends Fragment {

    private View mMainView;
    private Context mContext;
    private boolean mProgressBarshowing = false;
    private RelativeLayout mProgressBarLayout;
    private RelativeLayout mRlMain;

    private RelativeLayout mRlNoInterConnection;
    private Button mTryAgainBtn;

    private WebView webViewAboutUs;

    public AboutUsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mContext = getActivity();
        mMainView = inflater.inflate(R.layout.fragment_about_us, container, false);
        init();
        return mMainView;
    }

    private void init() {

        mProgressBarLayout = (RelativeLayout) mMainView.findViewById(R.id.rl_progressBar);
        mRlMain = (RelativeLayout) mMainView.findViewById(R.id.rl_privacy_main);
        webViewAboutUs = (WebView) mMainView.findViewById(R.id.wv_about_us);
        mRlNoInterConnection = (RelativeLayout) mMainView.findViewById(R.id.rl_privacy_policy_no_internet_main);
        mTryAgainBtn = (Button) mMainView.findViewById(R.id.btn_no_internet_retry);
        webViewAboutUs.getSettings().setJavaScriptEnabled(true);

        mTryAgainBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startHttpRequestPrivacyPolicy();
            }
        });


        final Activity activity = (Activity) mContext;
        webViewAboutUs.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress) {
                // Activities and WebViews measure progress with different scales.
                // The progress meter will automatically disappear when we reach 100%
                showProgressBar();
            }
        });
        webViewAboutUs.setWebViewClient(new WebViewClient() {
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                hideProgressBar();
                mRlNoInterConnection.setVisibility(View.VISIBLE);
                webViewAboutUs.setVisibility(View.GONE);
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

        startHttpRequestPrivacyPolicy();
    }


    public void startHttpRequestPrivacyPolicy() {

        // showProgressBar();

        if (!Utils.isConnectToInternet(mContext)) {

            hideProgressBar();
            mRlNoInterConnection.setVisibility(View.VISIBLE);
            webViewAboutUs.setVisibility(View.GONE);
            mRlMain.setVisibility(View.GONE);
        } else {
            String baseUrl = Constant.API_ABOUT_US;
            hideProgressBar();
            webViewAboutUs.loadUrl(baseUrl);
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
