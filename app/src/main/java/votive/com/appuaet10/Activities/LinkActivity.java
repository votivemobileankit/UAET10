package votive.com.appuaet10.Activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;


import votive.com.appuaet10.R;

/**
 * Created by ADMIN on 9/22/2017.
 */

public class LinkActivity extends AppCompatActivity{

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_link);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

//        Intent in = getIntent();
//        Uri data = in.getData();
//
//        Log.e("data","------->"+ data);


        Intent intent = getIntent();
        if (intent.getAction() != null) {
            String data = intent.getDataString();
            Log.e("data","------->"+ data);
            if (data != null) {

                String value = data.replace("http://", "").replace("example.in/hackString/", "");
//                if (!value.equals("")) {
//                    deepLinkUrl.setText(value);
//                }
            }
        }
    }
}
