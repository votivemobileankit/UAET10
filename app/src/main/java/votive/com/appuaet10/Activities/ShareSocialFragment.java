package votive.com.appuaet10.Activities;


import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import votive.com.appuaet10.Adapters.SocialAdapter;
import votive.com.appuaet10.Interface.IRowClickListener;
import votive.com.appuaet10.R;
import votive.com.appuaet10.Utilities.Constant;

public class ShareSocialFragment extends Fragment implements IRowClickListener{


    private View mMainView;
    public ShareSocialFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mMainView = inflater.inflate(R.layout.fragment_share_social, container, false);
        initFragment();
        return mMainView;
    }

    private void initFragment() {
        ImageView shareApp = (ImageView) mMainView.findViewById(R.id.iv_share_frag_app_share);
        shareApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String appPackageName = getActivity().getPackageName(); // getPackageName() from Context or Activity object
                String body = "Explore the Gems " + "http://play.google.com/store/apps/details?id=" + appPackageName;
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "APP NAME (Open it in Google Play Store to Download the Application)");
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, body);
                startActivity(Intent.createChooser(sharingIntent, "Share via"));
            }
        });

        RecyclerView  shareRv = (RecyclerView) mMainView.findViewById(R.id.rv_share_farg);
        //shareRv.addItemDecoration(new ItemDecorationAlbumColumns(3, 4));
        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 3);
        shareRv.setLayoutManager(layoutManager);
        shareRv.setNestedScrollingEnabled(false);
        String[] titleArr = getActivity().getResources().getStringArray(R.array.SocialMediaShare);
        SocialAdapter adapter = new SocialAdapter(getActivity(), titleArr, Constant.SOCIAL_MEDIA_SHARE_ICON_ARR, this);
        shareRv.setAdapter(adapter);
    }

    @Override
    public void onRowClick(int aPosition, boolean aAutoSearch) {
        String url = Constant.SOCIAL_MEDIA_URL_ARR[aPosition];
        try {
            Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(myIntent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(getActivity(), "No application can handle this request."
                    + " Please install a webbrowser", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }
}
