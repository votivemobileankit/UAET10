package votive.com.appuaet10.Utilities;


import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PermissionUtil {
    public static final String[] ALL_APP_PERMISSIONS = {
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.CALL_PHONE,
            Manifest.permission.ACCESS_NETWORK_STATE
    };


    public static final int REQUEST_CODE_WRITE_EXTERNAL_STORAGE = 100;
    public static final int REQUEST_CODE_ALL_PERMISSIONS = 101;
    public static final int REQUEST_CODE_LOCATION = 102;
    public static final int REQUEST_CODE_CALL = 103;

    public static String getGroupIdFromHRDetailsStr(String aStr) {
        String groupId = null;
        String[] res = aStr.split("&#");
        if (res.length > 0) {
            groupId = res[res.length - 1];
        }
        return groupId;
    }

    public static List<String> getPolicyNumberList(String aStr) {
        List policyList = null;
        String[] res = aStr.split("&#");
        if (res.length > 0) {
            String[] policyArray = Arrays.copyOf(res, res.length - 1);
            policyList = Arrays.asList(policyArray);
        }
        if (policyList == null) {
            policyList = new ArrayList();
        }
        return policyList;
    }


    @TargetApi(Build.VERSION_CODES.M)
    public static boolean isPermissionGranted(Context aContext, String aPermissionStr) {
        boolean permissionGranted = false;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(aContext, aPermissionStr) == PackageManager.PERMISSION_GRANTED) {
                permissionGranted = true;
            }
        } else {
            permissionGranted = true;
        }
        return permissionGranted;
    }

    public static void requestAppForPermission(Activity aActivity, String[] aPermissionArr, int aRequestCode) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (aPermissionArr.length > 0) {
                ActivityCompat.requestPermissions(aActivity, aPermissionArr, aRequestCode);
            }
        }
    }




    public static boolean isAllPermissionGranted(Context aContext, String[] aPermissionArr) {
        boolean permissionGranted = true;
        for (String aStr : aPermissionArr) {
            if (ContextCompat.checkSelfPermission(aContext, aStr)
                    != PackageManager.PERMISSION_GRANTED) {
                permissionGranted = false;
            }
        }
        return permissionGranted;
    }
}
