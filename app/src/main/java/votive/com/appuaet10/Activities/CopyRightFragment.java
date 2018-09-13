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

public class CopyRightFragment extends Fragment {

    private View mMainView;
    private Context mContext;
    private boolean mProgressBarshowing = false;
    private RelativeLayout mProgressBarLayout;
    private RelativeLayout mRlMain;

    private RelativeLayout mRlNoInterConnection;
    private Button mTryAgainBtn;

    private WebView webViewCopyRights;

    public CopyRightFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mContext = getActivity();
        mMainView = inflater.inflate(R.layout.fragment_copy_right, container, false);
        init();
        return mMainView;
    }

    private void init() {
        mProgressBarLayout = (RelativeLayout) mMainView.findViewById(R.id.rl_progressBar);
        mRlMain = (RelativeLayout) mMainView.findViewById(R.id.rl_copy_right_main);
        mRlNoInterConnection = (RelativeLayout) mMainView.findViewById(R.id.rl_copy_right_no_internet_main);
        mTryAgainBtn = (Button) mMainView.findViewById(R.id.btn_no_internet_retry);
        webViewCopyRights = (WebView) mMainView.findViewById(R.id.tv_copy_right);

        webViewCopyRights.getSettings().setJavaScriptEnabled(true);

        mTryAgainBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startHttpRequestCopyRights();
            }
        });


        final Activity activity = (Activity) mContext;
        webViewCopyRights.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress) {
                // Activities and WebViews measure progress with different scales.
                // The progress meter will automatically disappear when we reach 100%
                showProgressBar();
            }
        });
        webViewCopyRights.setWebViewClient(new WebViewClient() {
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                hideProgressBar();
                mRlNoInterConnection.setVisibility(View.VISIBLE);
                webViewCopyRights.setVisibility(View.GONE);
                mRlMain.setVisibility(View.GONE);            }

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

        startHttpRequestCopyRights();

    }


    public void startHttpRequestCopyRights() {
        //showProgressBar();
        String baseUrl = Constant.API_COPY_RIGHT;

        if (Utils.isConnectToInternet(mContext)) {
            hideProgressBar();
            webViewCopyRights.loadUrl(baseUrl);
        } else {
            hideProgressBar();
            mRlNoInterConnection.setVisibility(View.VISIBLE);
            webViewCopyRights.setVisibility(View.GONE);
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
