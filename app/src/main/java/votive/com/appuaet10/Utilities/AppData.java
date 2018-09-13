package votive.com.appuaet10.Utilities;

import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import votive.com.appuaet10.Activities.BusinessDetailedBySearchActivity;
import votive.com.appuaet10.Activities.CategoryItemListActivity;
import votive.com.appuaet10.Activities.HomeActivity;
import votive.com.appuaet10.Activities.SplashActivity;
import votive.com.appuaet10.Activities.VideoViewActivity;
import votive.com.appuaet10.Beans.BannerBean;
import votive.com.appuaet10.Beans.BlogBean;
import votive.com.appuaet10.Beans.CategoryBean;
import votive.com.appuaet10.R;


public class AppData {

    public static final String FONT_OPEN_SANS_REGULAR = "OpenSans_Regular.ttf";
    public static final String FONT_OPEN_SANS_BOLD = "OpenSans_Bold.ttf";
    public static final String FONT_ARIALIC = "Arialic_Hollow.ttf";
    public static final String FONT_EA_SPORTS_COVERS = "EA_Sports_Covers_SC_15.ttf";
    public static final String FONT_EA_SPORTS_15 = "EASPORTS15.ttf";


    public static final String[] FONT_NAMES = {FONT_OPEN_SANS_REGULAR, FONT_OPEN_SANS_BOLD, FONT_ARIALIC, FONT_EA_SPORTS_COVERS, FONT_EA_SPORTS_15};

    public static AppData smAppData;

    public static AppData getInstance() {
        if (smAppData == null) {
            smAppData = new AppData();
        }
        return smAppData;
    }

    private static ArrayList<CategoryBean> mCategoryList;
    private static ArrayList<CategoryBean> mCategoryItemList;
    private static ArrayList<CategoryBean> mCategoryHomeList;
    private static ArrayList<BlogBean> mBlogList;


    private static ArrayList<BannerBean> mBannerImageBeanArrayList;


    private Map<String, Typeface> mFontTypeList;

    public AppData() {
        mFontTypeList = new HashMap<>();
        mCategoryList = new ArrayList<>();
        mCategoryHomeList = new ArrayList<>();
        mCategoryItemList = new ArrayList<>();
        mBlogList = new ArrayList<>();
        mBannerImageBeanArrayList = new ArrayList<>();
    }

    private void initFontList() {
        if (mFontTypeList == null) {
            mFontTypeList = new HashMap<>();
        }
    }

    private void initCategoryList() {
        if (mCategoryList == null) {
            mCategoryList = new ArrayList<>();
        }
    }

    private void initCategoryItemList() {
        if (mCategoryItemList == null) {
            mCategoryItemList = new ArrayList<>();
        }
    }


    private void initBlogList() {
        if (mBlogList == null) {
            mBlogList = new ArrayList<>();
        }
    }


    private void iniBannerList() {
        if (mBannerImageBeanArrayList == null) {
            mBannerImageBeanArrayList = new ArrayList<>();
        }
    }


    public void loadAllFont(Context aContext) {
        initFontList();
        AssetManager am = aContext.getApplicationContext().getAssets();
        for (int i = 0; i < FONT_NAMES.length; i++) {
            Typeface curTypeFace = Typeface.createFromAsset(am,
                    String.format(Locale.US, "fonts/%s", FONT_NAMES[i]));
            if (curTypeFace != null) {
                mFontTypeList.put(FONT_NAMES[i], curTypeFace);
            }
        }
    }

    public Typeface getTypeFaceForType(Context aContext, String aFontName) {
        initFontList();
        Typeface typeFace = Typeface.DEFAULT;
        if (aFontName != null) {
            Typeface selectedTypeFace = mFontTypeList.get(aFontName);
            if (selectedTypeFace == null) {
                AssetManager am = aContext.getApplicationContext().getAssets();
                Typeface curTypeFace = Typeface.createFromAsset(am,
                        String.format(Locale.US, "fonts/%s", aFontName));
                if (curTypeFace != null) {
                    mFontTypeList.put(aFontName, curTypeFace);
                    typeFace = curTypeFace;
                }
            } else {
                typeFace = selectedTypeFace;
            }
        }
        return typeFace;
    }


    public void setCategoryList(Context aContext, ArrayList<CategoryBean> aCategoryList) {
        initCategoryList();
        mCategoryList = aCategoryList;
    }


    public ArrayList<CategoryBean> getCategoryList() {
        return mCategoryList;
    }


    public ArrayList<BlogBean> getBlogList() {

        if (mBlogList == null) {
            mBlogList = new ArrayList<>();
        }
        return mBlogList;

    }


    public ArrayList<CategoryBean> getHomeCategoryBusinessList() {
        if (mCategoryItemList == null) {
            mCategoryItemList = new ArrayList<>();
        }
        return mCategoryItemList;
    }


    public ArrayList<CategoryBean> getHomeCategoryList() {
        if (mCategoryHomeList == null) {
            mCategoryHomeList = new ArrayList<>();
        }
        return mCategoryHomeList;
    }

    public ArrayList<BannerBean> getHomeBannerList() {
        if (mBannerImageBeanArrayList == null) {
            mBannerImageBeanArrayList = new ArrayList<>();
        }
        return mBannerImageBeanArrayList;
    }

    public void setCategoryItemList(Context aContext, ArrayList<CategoryBean> aCategoryList) {
        initBlogList();
        mCategoryItemList = aCategoryList;
    }


    public void setBlogList(Context aContext, ArrayList<BlogBean> aBlogList) {
        initCategoryItemList();
        mBlogList = aBlogList;
    }


    public void setCategoryHomeItemList(Context aContext, ArrayList<CategoryBean> aCategoryList) {
        initCategoryItemList();
        mCategoryHomeList = aCategoryList;
    }

    public void setCategoryHomeBannerList(Context aContext, ArrayList<BannerBean> aBannerList) {
        iniBannerList();
        mBannerImageBeanArrayList = aBannerList;
    }


    public void showDialog(final Context aContext, String title, String msg, String image, final String type, final String id) {
        final Dialog dialog;
        dialog = new Dialog(aContext);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        dialog.setContentView(R.layout.notification_popup_dialog);
        dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(false);

        TextView notititle = (TextView) dialog.findViewById(R.id.tv_notification_title);
        SimpleDraweeView imageurl = (SimpleDraweeView) dialog.findViewById(R.id.iv_notification_image);
        TextView messsage = (TextView) dialog.findViewById(R.id.tv_notification_Message);

        RelativeLayout rl_notification_image = (RelativeLayout) dialog.findViewById(R.id.rl_notification_image);


        if (title != null) {
            notititle.setText(title);
        } else {
            notititle.setVisibility(View.GONE);
        }
        if (image != null) {
            rl_notification_image.setVisibility(View.VISIBLE);
            imageurl.setImageURI(Uri.parse(image));

        } else {
            rl_notification_image.setVisibility(View.GONE);
        }

        if (msg != null) {
            messsage.setText(msg);
        } else {
            messsage.setVisibility(View.GONE);
        }

        LinearLayout dialogButtonCancel = (LinearLayout) dialog.findViewById(R.id.ll_cancel);
        dialogButtonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });


        LinearLayout dialogButtonCheck = (LinearLayout) dialog.findViewById(R.id.ll_check);
        dialogButtonCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();

                Intent intent;
                if (type.equals(Constant.NOTIFICATION_TYPE_FOR_BUSINESS_ENQUIRY)) {
                    intent = new Intent(aContext, BusinessDetailedBySearchActivity.class);
                    intent.putExtra(Constant.INTENT_BUSINESS_ID, id);
                    intent.putExtra(Constant.SCREEN_CALL_INDEX, Constant.INTENT_SCREEN_CALL_BY_APPDATA_BUSINESS);
                    aContext.startActivity(intent);

                } else if (type.equals(Constant.NOTIFICATION_TYPE_FOR_CATEGORY_LIST)) {

                    Intent intent1 = new Intent(aContext, CategoryItemListActivity.class);
                    intent1.putExtra(Constant.INTENT_CATEGORY_ITEM_ID, id);
                    intent1.putExtra(Constant.SCREEN_CALL_INDEX, Constant.NOTIFICATION_CATEGORY_ID_FROM_HOME);
                    aContext.startActivity(intent1);

                } else if (type.equals(Constant.NOTIFICATION_TYPE_FOR_OFFER)) {
                    handleOfferClick(Constant.MENU_OPTION_OFFER_INDEX, aContext);

                } else if (type.equals(Constant.NOTIFICATION_TYPE_FOR_DEFAULT)) {
                    intent = new Intent(aContext, SplashActivity.class);
                    aContext.startActivity(intent);
                } else {
                    intent = new Intent(aContext, SplashActivity.class);
                    aContext.startActivity(intent);
                }
            }
        });
        dialog.show();
    }

    public void handleOfferClick(int aMenuIndex, Context aContext) {
        Intent intent = new Intent(aContext, HomeActivity.class);
        intent.putExtra(Constant.INTENT_MENU_INDEX_KEY, aMenuIndex);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        aContext.startActivity(intent);
    }

    public void setVideoStr(Context aContext, String aVideoStr) {
        if (aVideoStr != null && aVideoStr.length() > 4) {
            StorageUtils.putPref(aContext, Constant.SHARED_PREF_VIDEO_KEY, aVideoStr);
        }
    }

    public String getVideoStr(Context aContext) {
        String str = null;
        str = StorageUtils.getPrefStr(aContext, Constant.SHARED_PREF_VIDEO_KEY);
        if (str == null) {
            str = aContext.getResources().getString(R.string.DefaultVideoLink);
        }
        return str;
    }

    public void handleVideoPlay(Context aContext) {
        String videoStr = AppData.getInstance().getVideoStr(aContext);
        if (videoStr.contains("youtube.com")) {
            try {
                Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(videoStr));
                aContext.startActivity(myIntent);
            } catch (ActivityNotFoundException e) {
                Toast.makeText(aContext, "No application can handle this request."
                        + " Please install a webbrowser", Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }
        } else {
            if (Utils.isConnectingToInternet(aContext)) {
                Intent intent = new Intent(aContext, VideoViewActivity.class);
                aContext.startActivity(intent);
            } else {
                Toast.makeText(aContext, aContext.getResources().getString(R.string.InternetErrorMsg), Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void setFirstLaunch(Context aContext){
        StorageUtils.putPref(aContext, Constant.SHARED_PREF_FIRST_LAUNCH_KEY, true);
    }

    public boolean getFirstLaunch(Context aContext){
        return StorageUtils.getPrefForBool(aContext, Constant.SHARED_PREF_FIRST_LAUNCH_KEY);
    }

    public void set_Popup_flag(Context aContext, boolean bolvalue){
        StorageUtils.putPref(aContext, Constant.SHARED_PREF_POUP_FLAG, bolvalue);
    }

    public boolean get_Popup_flag(Context aContext){
        return StorageUtils.getPrefForBool(aContext, Constant.SHARED_PREF_POUP_FLAG);
    }
}
