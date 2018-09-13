package votive.com.appuaet10.Activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;

import votive.com.appuaet10.R;

/**
 * Created by ADMIN on 5/29/2018.
 */

public class WEbpage extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.webpage);


        WebView mywebview = (WebView) findViewById(R.id.webView1);
        //mywebview.loadUrl("http://www.javatpoint.com/");

        /*String data = "<html><body><h1>Hello, Javatpoint!</h1></body></html>";
        mywebview.loadData(data, "text/html", "UTF-8"); */

        mywebview.loadUrl("http://www.insurancehouse.ae/insure-and-win/");


    }






}
