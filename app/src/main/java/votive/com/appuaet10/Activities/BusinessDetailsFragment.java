package votive.com.appuaet10.Activities;


import android.Manifest;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import votive.com.appuaet10.Adapters.SimilarCategoryAdapter;
import votive.com.appuaet10.Beans.BusinessDetailedResponseBean;
import votive.com.appuaet10.Beans.CategoryItemBean;
import votive.com.appuaet10.Beans.StatusResponse;
import votive.com.appuaet10.Interface.ICategoryImageClickCallback;
import votive.com.appuaet10.R;
import votive.com.appuaet10.Utilities.AppController;
import votive.com.appuaet10.Utilities.Constant;
import votive.com.appuaet10.Utilities.PermissionUtil;
import votive.com.appuaet10.Utilities.UIUtils;
import votive.com.appuaet10.Utilities.Utils;

import static android.content.ContentValues.TAG;

public class BusinessDetailsFragment extends Fragment
        implements ICategoryImageClickCallback, View.OnClickListener {

    public static final String INTENT_CATEGORY_ITEM_KEY = "category_item_list";
    public static final String INTENT_SELECTED_ITEM_INDEX_KEY = "category_item_index";
    public static final String INTENT_SELECTED_CATEGORY_ID= "sel_category_id";
    public static final String INTENT_SELECTED_CATEGORY_NAME= "sel_category_name";


    private int[] mLayoutArr = {
            R.id.rl_bottom_menu_first_item,
            R.id.rl_bottom_menu_second_item,
            R.id.rl_bottom_menu_third_item,
            R.id.rl_bottom_menu_fourth_item,
            R.id.rl_bottom_menu_fifth_item};

    private int[] mTextViewArr = {
            R.id.tv_bottom_menu_first,
            R.id.tv_bottom_menu_second,
            R.id.tv_bottom_menu_third,
            R.id.tv_bottom_menu_fourth,
            R.id.tv_bottom_menu_five,
    };

    private int[] mImageViewArr = {
            R.id.iv_bottom_menu_first,
            R.id.iv_bottom_menu_second,
            R.id.iv_bottom_menu_third,
            R.id.iv_bottom_menu_fourth,
            R.id.iv_bottom_menu_five
    };


    public static final int[] BOTTOM_MENU_ICON_ID_ARR = {
            R.drawable.ic_home,
            R.drawable.ic_offers,
            R.drawable.ic_blogs,
            R.drawable.ic_category,
            R.drawable.ic_contact_us};


    private View mMainView;
    private Context mContext;
    private TextView mTvCategoryTitleName;
    private TextView mTvCategoryDetailed;
    private TextView mTvBusinessViews;

    private RecyclerView mRvCSimilarCategory;
    private SimilarCategoryAdapter mSimilarCategoryAdapter;
    private SimpleDraweeView mSimpleDraweeView;
    // private TextView mTvLink;
    private LinearLayoutManager layoutManagerl;
    private LinearLayout mLLBusinessViews;
    private LinearLayout mFadeLayout;

    private ImageView mImgFacebook;
    private ImageView mImgTwitter;
    private ImageView mImgLinkdin;
    private ImageView mImgInstagram;
    private ImageView mIvCall;
    private ImageView mIvWebLink;
    private ImageView mIvSendEmail;
    private ImageView mIvLike;
    private ImageView mIvShareLink;
    private TextView mTvLikeCount;

    private int mSelectedImageIndex;
    private ArrayList<CategoryItemBean> mCategoryList;
    private boolean isDescExpanded;
    private CategoryItemBean mCategoryItemBean;
    String mcategory_id;
    String mcategory_name;

    public BusinessDetailsFragment() {
    }

    public static BusinessDetailsFragment newInstance(int aSelectedIndex, ArrayList<CategoryItemBean> aCategoryItemList, String category_id, String category_name) {
        BusinessDetailsFragment fragmentFirst = new BusinessDetailsFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList(INTENT_CATEGORY_ITEM_KEY, aCategoryItemList);
        args.putInt(INTENT_SELECTED_ITEM_INDEX_KEY, aSelectedIndex);
        args.putString(INTENT_SELECTED_CATEGORY_ID, category_id);
        args.putString(INTENT_SELECTED_CATEGORY_NAME, category_name);

        fragmentFirst.setArguments(args);
        return fragmentFirst;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mMainView = inflater.inflate(R.layout.fragment_business_details, container, false);
        mContext = getActivity();
        return mMainView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initBottomMenu();
        init();
    }

    private void init() {
        mSelectedImageIndex = getArguments().getInt(INTENT_SELECTED_ITEM_INDEX_KEY);
        mCategoryList = getArguments().getParcelableArrayList(INTENT_CATEGORY_ITEM_KEY);
        mCategoryItemBean = mCategoryList.get(mSelectedImageIndex);
        mcategory_id = getArguments().getString(INTENT_SELECTED_CATEGORY_ID);
        mcategory_name = getArguments().getString(INTENT_SELECTED_CATEGORY_NAME);
        mTvCategoryTitleName = (TextView) mMainView.findViewById(R.id.tv_category_title);
        mTvBusinessViews = (TextView) mMainView.findViewById(R.id.tv_business_views);
        mLLBusinessViews = (LinearLayout) mMainView.findViewById(R.id.ll_business_views);

        mImgFacebook = (ImageView) mMainView.findViewById(R.id.img_fb);
        mImgInstagram = (ImageView) mMainView.findViewById(R.id.img_instagram);
        mImgTwitter = (ImageView) mMainView.findViewById(R.id.img_twitter);
        mImgLinkdin = (ImageView) mMainView.findViewById(R.id.img_linkdin);
        mIvCall = (ImageView) mMainView.findViewById(R.id.img_calling);
        mIvSendEmail = (ImageView) mMainView.findViewById(R.id.img_send_email);
        mIvWebLink = (ImageView) mMainView.findViewById(R.id.img_weblink);
        mTvLikeCount = (TextView) mMainView.findViewById(R.id.tv_like_count);
        mIvLike = (ImageView) mMainView.findViewById(R.id.img_like);
        mIvShareLink = (ImageView) mMainView.findViewById(R.id.img_share_link);

        mImgFacebook.setOnClickListener(this);
        mImgInstagram.setOnClickListener(this);
        mImgTwitter.setOnClickListener(this);
        mImgLinkdin.setOnClickListener(this);
        mIvCall.setOnClickListener(this);
        mIvSendEmail.setOnClickListener(this);
        mIvWebLink.setOnClickListener(this);
        mIvLike.setOnClickListener(this);
        mIvShareLink.setOnClickListener(this);

        mTvCategoryTitleName.setText(mCategoryItemBean.getCategoryItemName());
//        mTvLink = (TextView) mMainView.findViewById(R.id.tv_detailed_link);
        mSimpleDraweeView = (SimpleDraweeView) mMainView.findViewById(R.id.iv_category_display);
        mFadeLayout = (LinearLayout) mMainView.findViewById(R.id.ll_business_details_fade);
        mTvCategoryDetailed = (TextView) mMainView.findViewById(R.id.tv_category_detailed);
        mTvCategoryDetailed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isDescExpanded){
                    mTvCategoryDetailed.setMaxLines(Integer.MAX_VALUE);
                    isDescExpanded = true;
                    mFadeLayout.setVisibility(View.GONE);
                }else{
                    isDescExpanded = false;
                    mTvCategoryDetailed.setMaxLines(5);
                    mFadeLayout.setVisibility(View.VISIBLE);
                }
            }
        });


        mRvCSimilarCategory = (RecyclerView) mMainView.findViewById(R.id.rv_similar_product_Img);
        mRvCSimilarCategory.setNestedScrollingEnabled(false);
        loadDisplayImage(mSelectedImageIndex);


        if (mCategoryItemBean.getFbLinkStr() != null && !mCategoryItemBean.getFbLinkStr().isEmpty()) {
            mImgFacebook.setVisibility(View.VISIBLE);
        } else {
            mImgFacebook.setVisibility(View.GONE);
        }

        if (mCategoryItemBean.getInstaLinkStr() != null && !mCategoryItemBean.getInstaLinkStr().isEmpty()) {
            mImgInstagram.setVisibility(View.VISIBLE);
        } else {
            mImgInstagram.setVisibility(View.GONE);
        }
        if (mCategoryItemBean.getTwitterLinkStr() != null && !mCategoryItemBean.getTwitterLinkStr().isEmpty()) {
            mImgTwitter.setVisibility(View.VISIBLE);
        } else {
            mImgTwitter.setVisibility(View.GONE);
        }

        if (mCategoryItemBean.getLinkdinLinkStr() != null && !mCategoryItemBean.getLinkdinLinkStr().isEmpty()) {
            mImgLinkdin.setVisibility(View.VISIBLE);
        } else {
            mImgLinkdin.setVisibility(View.GONE);
        }

        if (mCategoryItemBean.getStrViews() != null && !mCategoryItemBean.getStrViews().isEmpty()) {
            String views = mCategoryItemBean.getStrViews() + " " + getResources().getString(R.string.ViewText);
            mTvBusinessViews.setText(views);
        } else {
            mLLBusinessViews.setVisibility(View.GONE);
        }

        if (mCategoryItemBean.getCategoryItemDesc() != null) {
            mTvCategoryDetailed.setText(mCategoryItemBean.getCategoryItemDesc());
        } else {
            mTvCategoryDetailed.setVisibility(View.GONE);
        }

        if (mCategoryItemBean.getPhoneNo() != null && !mCategoryItemBean.getPhoneNo().isEmpty()) {
            mIvCall.setVisibility(View.VISIBLE);
        } else {
            mIvCall.setVisibility(View.GONE);
        }

        if (mCategoryItemBean.getEmailStr() != null &&
                !mCategoryItemBean.getEmailStr().isEmpty()) {
            mIvSendEmail.setVisibility(View.VISIBLE);
        } else {
            mIvSendEmail.setVisibility(View.GONE);
        }

        if (mCategoryItemBean.getWebLinkStr() != null &&
                !mCategoryItemBean.getWebLinkStr().isEmpty()) {
            //mTvLink.setText(mCategoryItemBean.getWebNameStr());
            mIvWebLink.setVisibility(View.VISIBLE);
        } else {
            mIvWebLink.setVisibility(View.GONE);
        }

        mIvWebLink.setOnLongClickListener(new View.OnLongClickListener() {

            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(getActivity(), "Touch to Open Weblink", Toast.LENGTH_SHORT).show();
                return true;
            }

        });

        mIvSendEmail.setOnLongClickListener(new View.OnLongClickListener() {

            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(getActivity(), "Touch to Send Email", Toast.LENGTH_SHORT).show();
                return true;
            }

        });

        mImgInstagram.setOnLongClickListener(new View.OnLongClickListener() {

            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(getActivity(), "Touch to Open instagram Link", Toast.LENGTH_SHORT).show();
                return true;
            }
        });
        mImgFacebook.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(getActivity(), "Touch to Open Facebook Link", Toast.LENGTH_SHORT).show();
                return true;
            }
        });

        mImgLinkdin.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(getActivity(), "Touch to Open Linkdin Link", Toast.LENGTH_SHORT).show();
                return true;
            }
        });

        mImgTwitter.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(getActivity(), "Touch to Open twitter Link", Toast.LENGTH_SHORT).show();
                return true;
            }
        });

        if (mCategoryItemBean.getLikesCount() != -1) {
            String counts = ""+mCategoryItemBean.getLikesCount();
            mTvLikeCount.setText(counts);
        } else {
            mTvLikeCount.setText("0");
        }

        updateCategoryListView(mCategoryList);


    }

    private void initBottomMenu() {
        String[] mTitleArr;
        mTitleArr = getResources().getStringArray(R.array.bottom_menu);
        for (int i = 0; i < mTitleArr.length; i++) {
            ImageView curImageView = (ImageView) mMainView.findViewById(mImageViewArr[i]);
            curImageView.setImageResource(BOTTOM_MENU_ICON_ID_ARR[i]);

            TextView curTextView = (TextView) mMainView.findViewById(mTextViewArr[i]);
            curTextView.setText(mTitleArr[i]);

            RelativeLayout curLayout = (RelativeLayout) mMainView.findViewById(mLayoutArr[i]);
            final int finalI = i;
            curLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    handleBottomClick(finalI);
                }
            });
        }
    }

    private void handleBottomClick(int aPosition) {
            switch (aPosition) {

                case 0:
                    handleSideMenuClick(Constant.MENU_OPTION_HOME_INDEX);
                    break;

                case 1:
                    handleSideMenuClick(Constant.MENU_OPTION_OFFER_INDEX);
                    break;

                case 2:
                    handleSideMenuClick(Constant.MENU_OPTION_BLOG_INDEX);
                    break;

                case 3:
                    handleSideMenuClick(Constant.MENU_OPTION_CATEGORY_INDEX);
                    break;

                case 4:
                    handleSideMenuClick(Constant.MENU_OPTION_CONTACT_US_INDEX);
                    break;

            }

    }

    public void handleSideMenuClick(int aMenuIndex) {
        Intent intent = new Intent(mContext, HomeActivity.class);
        intent.putExtra(Constant.INTENT_MENU_INDEX_KEY, aMenuIndex);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        getActivity().finish();
    }


    private void requestPermission() {
        // No explanation needed, we can request the permission.
        requestPermissions(new String[]{Manifest.permission.CALL_PHONE}, Constant.MY_PERMISSIONS_REQUEST_READ_PHONE_STATE);
    }


    private void updateCategoryListView(ArrayList<CategoryItemBean> aCategoryList) {
        mCategoryList = aCategoryList;
        layoutManagerl = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        mRvCSimilarCategory.setLayoutManager(layoutManagerl);
        mSimilarCategoryAdapter = new SimilarCategoryAdapter(mContext, mCategoryList, this);
        mRvCSimilarCategory.setAdapter(mSimilarCategoryAdapter);
        layoutManagerl.scrollToPosition(mSelectedImageIndex);
        handleUpdate_Views(mCategoryItemBean.getCategoryListingId());

    }


    private void loadDisplayImage(int aSelectedIndex) {
        try {
            String selectedImageStr = mCategoryItemBean.getCategoryItemFullImgUrl();
            String imagePath = selectedImageStr.trim();
            String newPathStr = imagePath.replaceAll(" ", "%20");
            mSimpleDraweeView.setImageURI(Uri.parse(newPathStr));
        } catch (Exception e) {
            final int sdk = android.os.Build.VERSION.SDK_INT;
            if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {

                mSimpleDraweeView.setBackgroundDrawable(getResources().getDrawable(R.drawable.ic_placeholder));
            } else {
                mSimpleDraweeView.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.ic_placeholder));
            }
        }
    }

    @Override
    public void itemClicked(int aIndex, CategoryItemBean aCategoryItemBean) {
        if (mSelectedImageIndex != aIndex) {
            ((CategoryDetailedItemActivity) getActivity()).setSelectedItem(aIndex);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == Constant.MY_PERMISSIONS_REQUEST_READ_PHONE_STATE) {

            boolean permissionDenied = false;

            for (int i = 0; i < permissions.length; i++) {
                String permission = permissions[i];
                if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                    // user rejected the permission
                    permissionDenied = true;
                    boolean showRationale = false;
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                        showRationale = shouldShowRequestPermissionRationale(permission);
                    }
                    if (!showRationale) {
                        // user also CHECKED "never ask again"
                        // you can either enable some fall back,
                        // disable features of your app
                        // or open another dialog explaining
                        // again the permission and directing to
                        // the app setting
                        showDialogForPermissionSetting();
                    } else if (Manifest.permission.CALL_PHONE.equals(permission)) {
                        showDialogForPermission();
//                        showRationale(permission, R.string.permission_denied_contacts);
                        // user did NOT check "never ask again"
                        // this is a good place to explain the user
                        // why you need the permission and ask if he wants
                        // to accept it (the rationale)
                    }
                }
            }
            if (!permissionDenied) {
                dialNumber();
            }
        }

    }

    private void showDialogForPermissionSetting() {
        new AlertDialog.Builder(getActivity())
                .setTitle(getString(R.string.PermissionGoSettingTitle))
                .setMessage(getString(R.string.PermissionGoSettingMsg))
                .setCancelable(true)
                .setPositiveButton(getString(R.string.PermissionGoSettingTxt), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        final Intent i = new Intent();
                        i.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        i.addCategory(Intent.CATEGORY_DEFAULT);
                        i.setData(Uri.parse("package:" + getActivity().getPackageName()));
                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        i.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                        i.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                        getActivity().startActivity(i);
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    private void showDialogForPermission() {
        new AlertDialog.Builder(getActivity())
                .setTitle(getString(R.string.PermissionDeniedTitle))
                .setMessage(getString(R.string.PermissionDeniedMsg))
                .setCancelable(true)
                .setPositiveButton(getString(R.string.PermissionDeniedSureTxt), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setNegativeButton(getString(R.string.PermissionDeniedRetryTxt), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        requestPermission();
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    private void openBrowserLink() {

        try {
            String addHttp = mCategoryItemBean.getWebLinkStr();
            Uri uri = Uri.parse(addHttp);
            Intent myIntent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(myIntent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(mContext, "No application can handle this request." + " Please install a webbrowser", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }


    private void openBrowserFbLink() {

        try {
            Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(mCategoryItemBean.getFbLinkStr()));
            startActivity(myIntent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(getActivity(), "No application can handle this request."
                    + " Please install a webbrowser", Toast.LENGTH_LONG).show();
            e.printStackTrace();
            System.out.println("###e = " + e);
        }
    }


    private void openBrowserInstaLink() {

        try {
            Uri uri = Uri.parse(mCategoryItemBean.getInstaLinkStr());
            Intent myIntent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(myIntent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(getActivity(), "No application can handle this request."
                    + " Please install a webbrowser", Toast.LENGTH_LONG).show();
            e.printStackTrace();
            System.out.println("###e = " + e);
        }
    }

    private void openBrowserTwitterLink() {

        try {
            Uri uri = Uri.parse(mCategoryItemBean.getTwitterLinkStr());
            Intent myIntent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(myIntent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(getActivity(), "No application can handle this request."
                    + " Please install a webbrowser", Toast.LENGTH_LONG).show();
            e.printStackTrace();
            System.out.println("###e = " + e);
        }
    }


    private void openBrowserLinkdinLink() {

        try {
            Uri uri = Uri.parse(mCategoryItemBean.getLinkdinLinkStr());
            Intent myIntent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(myIntent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(getActivity(), "No application can handle this request."
                    + " Please install a webbrowser", Toast.LENGTH_LONG).show();
            e.printStackTrace();
            System.out.println("###e = " + e);
        }
    }


    private void sendEmail() {

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/html");
        intent.putExtra(Intent.EXTRA_EMAIL, mCategoryItemBean.getEmailStr());
        intent.putExtra(Intent.EXTRA_SUBJECT, "Subject");
        intent.putExtra(Intent.EXTRA_TEXT, "I'm email body.");
        startActivity(Intent.createChooser(intent, "Send Email"));
    }

    private void dialNumber() {
        String number = mCategoryItemBean.getPhoneNo();
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + number));
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {

        switch(v.getId()){
            case R.id.img_fb:
                openBrowserFbLink();
                break;

            case R.id.img_twitter:
                openBrowserTwitterLink();
                break;

            case R.id.img_instagram:
                openBrowserInstaLink();
                break;

            case R.id.img_linkdin:
                openBrowserLinkdinLink();
                break;

            case R.id.img_calling:
                handleCallPermission();

                break;

            case R.id.img_send_email:
                sendEmail();
                break;

            case R.id.img_weblink:
                openBrowserLink();
                break;

            case R.id.img_like:

                if (mCategoryItemBean.getCategoryListingId() != -1){
                    handleUpdateLike(mCategoryItemBean.getCategoryListingId());
                }

                break;

            case R.id.img_share_link:
                shareAppLink();
                break;

        }

    }

    private void handleCallPermission() {
            if (PermissionUtil.isPermissionGranted(getActivity(), Manifest.permission.CALL_PHONE)) {
                dialNumber();
            } else {
                requestPermission();
            }
    }

    private void shareAppLink() {

        try {
            /*Uri uri = Uri.parse(mCategoryItemBean.getInstaLinkStr());
            Intent myIntent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(myIntent);*/
            String shareBody = Constant.API_CATEGORY_ITEM_SHARE+"?";

//            Log.e("shareBody","------->"+ shareBody);
//            Log.e("catid","------->"+ mcategory_id);
//            Log.e("cat_name","------->"+ mcategory_name);
//            Log.e("position","------->"+ mSelectedImageIndex);

            shareBody= shareBody+"&catid="+mcategory_id+"&cat_name="+mcategory_name+"&position="+mSelectedImageIndex;

            shareBody= shareBody.replaceAll(" ","%20");
            Uri shareLink = Uri.parse(shareBody);
            final String appPackageName = getActivity().getPackageName(); // getPackageName() from Context or Activity object
            String body = "UAE Top 10 \n" +  "\n" + "Explore the Category, \n " + mcategory_name + "/"+ mCategoryItemBean.getCategoryItemName() +"\n\n" + shareLink;
            Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
            sharingIntent.setType("text/plain");
            sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "UAE Top 10");
            sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, body);
            startActivity(Intent.createChooser(sharingIntent, "Share via"));



        } catch (ActivityNotFoundException e) {
            Toast.makeText(getActivity(), "No application can handle this request."
                    + " Please install a web browser", Toast.LENGTH_LONG).show();
            e.printStackTrace();
            System.out.println("###e = " + e);
        }
    }





    private void handleUpdateLike(final int aCategoryId) {
        Log.e("F-----------------","!!!!CategoryId = " + aCategoryId);
        if (Utils.isConnectingToInternet(mContext)) {
            String baseUrl = Constant.API_ADD_LIKE;

            StringRequest strRequest = new StringRequest(Request.Method.POST, baseUrl,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            try {
                                Gson gson = new GsonBuilder().create();
                                JsonParser jsonParser = new JsonParser();
                                JsonObject jsonResp = jsonParser.parse(response).getAsJsonObject();

                                StatusResponse statusResponse = gson.fromJson(jsonResp, StatusResponse.class);
                                if (statusResponse != null) {
                                    if (Utils.isStatusSuccess(statusResponse.getStatus())) {
                                        int count = Integer.parseInt(mTvLikeCount.getText().toString().trim());
                                        count = count + 1;
                                        mTvLikeCount.setText(""+count);
                                    } else {
                                        UIUtils.showToast(mContext, statusResponse.getMsg());
                                    }
                                }
                            } catch (Exception e) {
                                UIUtils.showToast(mContext, getString(R.string.VolleyErrorMsg));
                                System.out.println("???e = " + e);
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                            if (error instanceof NoConnectionError) {
                                UIUtils.showToast(mContext, getString(R.string.InternetErrorMsg));
                            } else {
                                UIUtils.showToast(mContext, getString(R.string.VolleyErrorMsg));
                            }
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() {
                    final String androidDeviceId = Settings.Secure.getString(mContext.getContentResolver(), Settings.Secure.ANDROID_ID);
                    Map<String, String> params = new HashMap<>();
                    params.put(Constant.JSON_CATEGORY_LISTING_LIKE_ID, "" + aCategoryId);
                    params.put(Constant.JSON_DEVICE_ID_KEY, "" + androidDeviceId);
                    Log.e("F-----------------","!!!!params = " + params.toString());
                    return params;
                }
            };
            strRequest.setShouldCache(false);
            strRequest.setTag(TAG);
            AppController.getInstance().addToRequestQueue(strRequest);
            strRequest.setRetryPolicy(new DefaultRetryPolicy(
                    10000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        } else {
            UIUtils.showToast(mContext, getString(R.string.InternetErrorMsg));
        }
    }




    private void handleUpdate_Views(final int aCategoryId) {
        if (Utils.isConnectingToInternet(mContext)) {
            String baseUrl = Constant.API_ADD_VIEW;

            StringRequest strRequest = new StringRequest(Request.Method.POST, baseUrl,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            System.out.println("!!!!handleUpdate_Views response = " + response);
//                            try {
//                                Gson gson = new GsonBuilder().create();
//                                JsonParser jsonParser = new JsonParser();
//                                JsonObject jsonResp = jsonParser.parse(response).getAsJsonObject();
//
//                                StatusResponse statusResponse = gson.fromJson(jsonResp, StatusResponse.class);
//                                if (statusResponse != null) {
//                                    if (Utils.isStatusSuccess(statusResponse.getStatus())) {
//                                        int count = Integer.parseInt(mTvLikeCount.getText().toString().trim());
//                                        count = count + 1;
//                                        mTvLikeCount.setText(""+count);
//                                    } else {
//                                        UIUtils.showToast(mContext, statusResponse.getMsg());
//                                    }
//                                }
//                            } catch (Exception e) {
//                                UIUtils.showToast(mContext, getString(R.string.VolleyErrorMsg));
//                                System.out.println("???e = " + e);
//                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

//                            if (error instanceof NoConnectionError) {
//                                UIUtils.showToast(mContext, getString(R.string.InternetErrorMsg));
//                            } else {
//                                UIUtils.showToast(mContext, getString(R.string.VolleyErrorMsg));
//                            }
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() {
                    final String androidDeviceId = Settings.Secure.getString(mContext.getContentResolver(), Settings.Secure.ANDROID_ID);
                    Map<String, String> params = new HashMap<>();
                    params.put(Constant.JSON_CATEGORY_LISTING_LIKE_ID, "" + aCategoryId);
                    params.put(Constant.JSON_DEVICE_ID_KEY, "" + androidDeviceId);
                    System.out.println("!!!!handleUpdate_Views params = " + params);
                    return params;
                }
            };
            strRequest.setShouldCache(false);
            strRequest.setTag(TAG);
            AppController.getInstance().addToRequestQueue(strRequest);
            strRequest.setRetryPolicy(new DefaultRetryPolicy(
                    10000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        } else {
            UIUtils.showToast(mContext, getString(R.string.InternetErrorMsg));
        }
    }
}
