package votive.com.appuaet10.Utilities;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.widget.Toast;

import votive.com.appuaet10.R;

public class Utils {
    public final static boolean isValidEmail(CharSequence aTarget) {


        if (TextUtils.isEmpty(aTarget)) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(aTarget).matches();
        }
    }

    public static String getIMEINumber(Context aContext) {
        TelephonyManager mngr = (TelephonyManager) aContext.getSystemService(Context.TELEPHONY_SERVICE);
        return mngr.getDeviceId();
    }

    public static boolean isStatusSuccess(String aString) {
        boolean status = false;
        if (aString != null) {
            int statusInt = Integer.parseInt(aString);
            if (statusInt == 1) {
                status = true;
            }
        }
        return status;
    }

    public static boolean isConnectingToInternet(Context aContext) {
        ConnectivityManager connectivityManager = (ConnectivityManager) aContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Network[] networks = connectivityManager.getAllNetworks();
            NetworkInfo networkInfo;
            for (Network mNetwork : networks) {
                networkInfo = connectivityManager.getNetworkInfo(mNetwork);
                if (networkInfo.getState().equals(NetworkInfo.State.CONNECTED)) {
                    return true;
                }
            }
        } else {
            if (connectivityManager != null) {
                //noinspection deprecation
                NetworkInfo[] info = connectivityManager.getAllNetworkInfo();
                if (info != null) {
                    for (NetworkInfo anInfo : info) {
                        if (anInfo.getState() == NetworkInfo.State.CONNECTED) {
                            return true;
                        }
                    }
                }
            }
        }
        Toast.makeText(aContext, aContext.getString(R.string.InternetErrorMsg), Toast.LENGTH_SHORT).show();
        return false;
    }


    public static boolean isConnectToInternet(Context aContext) {
        ConnectivityManager cm = (ConnectivityManager) aContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        if (ni == null) {
            // There are no active networks.
            Toast.makeText(aContext, aContext.getString(R.string.InternetErrorMsg), Toast.LENGTH_SHORT).show();
            return false;
        } else
            return true;

    }

    public static String getFirstCapInEachWord(String aString){
        String[] strArray = aString.split(" ");
        StringBuilder builder = new StringBuilder();
        for (String s : strArray) {
            String cap = s.substring(0, 1).toUpperCase() + s.substring(1);
            builder.append(cap + " ");
        }
        return builder.toString();
    }
}
