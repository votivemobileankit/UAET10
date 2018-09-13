package votive.com.appuaet10.Utilities;

import android.app.Activity;
import android.content.Context;
import android.support.v7.app.ActionBar;
import android.text.Spannable;
import android.text.SpannableString;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import votive.com.appuaet10.CustomUI.TypefaceSpan;


public class UIUtils {

    public static void showToast(Context aContext, String aMag) {
        Toast.makeText(aContext, aMag, Toast.LENGTH_SHORT).show();
    }

    public static void setTitleWithFont(Context aContex, ActionBar aActionBar, String aFontName, String aTitleStr) {
        SpannableString s = new SpannableString(aTitleStr);
        s.setSpan(new TypefaceSpan(aContex, aFontName), 0, s.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        if (aActionBar != null) {
            aActionBar.setTitle(s);
        }
    }

    public static void setMenuTitleWithFont(Context aContex, MenuItem aMenuTItem, String aFontName, String aTitleStr) {
        SpannableString s = new SpannableString(aTitleStr);
        s.setSpan(new TypefaceSpan(aContex, aFontName), 0, s.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        if (aMenuTItem != null) {
            aMenuTItem.setTitle(s);
        }
    }

    public static void hideKeyBoard(Activity aActivity){
        View view = aActivity.getCurrentFocus();
        if (view != null){
            InputMethodManager imm = (InputMethodManager)aActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
    

}
