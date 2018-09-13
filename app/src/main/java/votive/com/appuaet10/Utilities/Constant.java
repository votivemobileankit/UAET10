package votive.com.appuaet10.Utilities;


import votive.com.appuaet10.R;

public class Constant {

    public static final boolean MULTI_TASK_BANNER = true;
    public static final int SPLASH_TIME_OUT = 3 * 1000;
    public static final int SEARCH_MIN_CHAR = 0;

    public static final int MY_PERMISSIONS_REQUEST_READ_PHONE_STATE = 2000;

    public static final int MAX_RECENT_SEARCH_COUNT = 5;
    public static final String PREF_USER_SEARCH_ITEMS = "user_search_item";

    public static final String PREF_SKIP_USER_ACCESS = "skipAccess";

    // broadcast receiver intent filters
    public static final String REGISTRATION_COMPLETE = "registrationComplete";
    public static final String PUSH_NOTIFICATION = "pushNotification";
    // global topic to receive app wide push notifications
    public static final String TOPIC_GLOBAL = "global";

    // id to handle the notification in the notification tray
    public static final int NOTIFICATION_ID = 100;
    public static final int NOTIFICATION_ID_BIG_IMAGE = 101;


    public static final int NOTIFICATION_BUSINESS_BY_HOME = 110;
    public static final int NOTIFICATION_CATEGORY_ID_FROM_HOME = 100;
    public static final int NOTIFICATION_OFFER_TYPE = 120;
    public static final int NOTIFICATION_APPDATA_OFFER_TYPE = 130;
    public static final int INTENT_SCREEN_CALL_BY_APPDATA_BUSINESS = 115;
    public static final int INTENT_SCREEN_CALL_BY_MORE_OFFER = 116;
    public static final int INTENT_SCREEN_CALL_BY_OFFER_FRAGMENT = 117;

    public static final String SHARED_PREF_POUP_FLAG = "Popupflag";
    public static final String SHARED_PREF_FIRST_LAUNCH_KEY = "skipPopup";
    public static final String SHARED_PREF_FIREBASE_KEY = "ah_firebase";
    public static final String SHARED_PREF_VIDEO_KEY = "video";
    public static final String SHARED_PREF_WELCOME_PUSH_KEY = "welcome_push";

    public static final String SCREEN_CALL_INDEX = "screen_call_index";

    public static final String NOTIFICATION_TYPE_FOR_BUSINESS_ENQUIRY = "1";
    public static final String NOTIFICATION_TYPE_FOR_CATEGORY_LIST = "2";
    public static final String NOTIFICATION_TYPE_FOR_OFFER = "3";
    public static final String NOTIFICATION_TYPE_FOR_DEFAULT = "4";

    public static final int MENU_OPTION_INVALID_INDEX = -1;
    public static final int MENU_OPTION_HOME_INDEX = 1;
    public static final int MENU_OPTION_OFFER_INDEX = 2;
    public static final int MENU_OPTION_BLOG_INDEX = 3;
    public static final int MENU_OPTION_CATEGORY_INDEX = 4;
    public static final int MENU_OPTION_CONTACT_US_INDEX = 5;
    public static final int MENU_OPTION_SUGGESTION_INDEX = 6;
    public static final int MENU_OPTION_PRIVACY_POLICY_INDEX = 7;
    public static final int MENU_OPTION_TERMS_AND_CONDITION_INDEX = 8;
    public static final int MENU_OPTION_COPY_RIGHT_INDEX = 9;
    public static final int MENU_OPTION_ABOUT_US_INDEX = 10;
    public static final int MENU_OPTION_SOCIAL_SHARE_INDEX = 11;
    public static final int MENU_OPTION_PLAY_VIDEO_INDEX = 12;

    public static  int FIX_BANNER_ID_INSURANSE_HOME = 0;
    public static final int SEARCH_TYPE_CATEGORY = 2;
    public static final int VALID_BUSINESS_INDEX = 0;
    public static final int COMINGSOON_BUSINESS_INDEX = 1;

    public static final int MAX_ITEM_IN_CATEGORY_GRID = 12;

    public static final int BANNER_TYPE_DEFAULT = 0;
    public static final int BANNER_TYPE_CATEGORY = 1;
    public static final int BANNER_TYPE_BUSINESS = 2;
    public static final int BANNER_TYPE_WEBLINK = 3;

    // Sequence must same order
    public static final int[] MENU_ARR = {
            MENU_OPTION_ABOUT_US_INDEX,
            MENU_OPTION_OFFER_INDEX,
            MENU_OPTION_BLOG_INDEX,
            MENU_OPTION_CATEGORY_INDEX,
            MENU_OPTION_CONTACT_US_INDEX,
            MENU_OPTION_SUGGESTION_INDEX,
            MENU_OPTION_PRIVACY_POLICY_INDEX,
            MENU_OPTION_TERMS_AND_CONDITION_INDEX,
            MENU_OPTION_COPY_RIGHT_INDEX,
            MENU_OPTION_ABOUT_US_INDEX,
            MENU_OPTION_SOCIAL_SHARE_INDEX
    };

    //INTENT_KEYS

    public static final String NOTIFICATION_COUNTER_VALUE_KEY = "notify_counter";

    public static final String INTENT_MENU_INDEX_KEY = "menu_selection_index";

    public static boolean HOME_FRAGMENT_CALL_BACK = false;

    public static final String CATEGORY_ITEM_ID_PARAMS = "category_id";


    public static final String JSON_CATEGORY_ID_KEY = "category_id";
    public static final String JSON_CATEGORY_NAME_KEY = "category_name";
    public static final String JSON_CATEGORY_DESCRIPTION_KEY = "category_description";
    public static final String JSON_CATEGORY_IMAGE_URL_KEY = "category_image";
    public static final String JSON_CATEGORY_IMAGE_RECT_URL_KEY = "category_rect_image";
    public static final String JSON_CATEGORY_THUMB_IMAGE_URL_KEY = "category_thumb_image";


    public static final String JSON_BLOG_POST_ID = "post_id";
    public static final String JSON_BLOG_TITLE_NAME = "post_title";
    public static final String JSON_BLOG_IMAGE = "image";
    public static final String JSON_BLOG_URL = "link";


    public static final String JSON_FEATURE_LISTING_NAME = "name";
    public static final String JSON_FEATURE_LISTING_FULL_IMAGE = "full_image";


    public static final String JSON_CATEGORY_ARRAY_LIST_KEY = "categoryInfo";
    public static final String JSON_FEATURE_CATEGORY_LIST_KEY = "feature_category";
    public static final String JSON_FEATURES_LISTING_KEY = "feature_listings";
    public static final String JSON_BANNER_LISTING_KEY = "banners";
    public static final String JSON_WATCH_VIDEO_LINK_KEY = "video";

    public static final String JSON_BLOG_LIST_KEY = "post_info";
    public static final String JSON_BLOG_TOTAL_PAGES = "pages";
    public static final String JSON_BLOG_PAGE_INDEX_KEY = "page_no";

    public static final String JSON_BANNER_TYPE_KEY = "type";
    public static final String JSON_BANNER_ID_KEY = "id";
    public static final String JSON_BANNER_NAME_KEY = "name";
    public static final String JSON_BANNER_URL_KEY = "url";
    public static final String JSON_WEBLINK_KEY = "web_url";


    public static final String INTENT_CATEGORY_ITEM_KEY = "category_bean_home";
    public static final String INTENT_CATEGORY_ITEM_ID = "id";

    public static final String INTENT_SCREEN_INDEX_KEY = "index";
    public static final String INTENT_BEAN_HOME = "fragment_listing_bean";

    public static final String INTENT_BLOG_DETAILS_BEAN_KEY = "blog_details";

    public static final String INTENT_IMAGE_SELECTED_INDEX_KEY = "index";

    public static final String INTENT_CATEGORY_LIST = "category_item_list";
    public static final String INTENT_SELECTED_INDEX_KEY = "selected_index";


    public static final String INTENT_NOTIFICATION_TYPE = "type";
    public static final String INTENT_NOTIFICATION_ID = "id";
    public static final String INTENT_NOTIFICATION_TITLE = "title";
    public static final String INTENT_NOTIFICATION_MSG = "message";
    public static final String INTENT_NOTIFICATION_IMAGE_URL = "image";
    public static final String INTENT_NOTIFICATION_BUSINESS_ID = "business_id";
    //public static final String INTENT_NOTIFICATION_CATEGORY_ID = "category_id";


    public static final String INTENT_BUSINESS_ID = "business_id";
    public static final String INTENT_OFFER_ID = "offer_id";


    public static final String INTENT_CATEGORY_ID = "category_id";
    public static final String INTENT_CATEGORY_NAME = "category_name";
    public static final String INTENT_COMING_FROM = "coming_from";

    public static final String JSON_CATEGORY_ITEM_NAME = "name";
    public static final String JSON_CATEGORY_THUMB_IMAGE = "thumb_image";
    public static final String JSON_CATEGORY_FULL_IMAGE = "full_image";
    public static final String JSON_CATEGORY_ITEMM_DESC = "description";
    public static final String JSON_CATEGORY_ITEM_CONTACT_NO = "contactno";
    public static final String JSON_CATEGORY_ITEM_EMAIL = "conatctemail";
    public static final String JSON_CATEGORY_ITEM_WEB_LINK = "weburl";
    public static final String JSON_CATEGORY_ITEM_WEB_NAME = "webname";
    public static final String JSON_CATEGORY_FB_LINK = "fb_url";
    public static final String JSON_CATEGORY_TWITTER_LINK = "tw_url";
    public static final String JSON_CATEGORY_LINKDIN_LINK = "ln_url";
    public static final String JSON_CATEGORY_INSTA_LINK = "insta_url";
    public static final String JSON_CATEGORY_VIEWS = "view";
    public static final String JSON_CATEGORY_ITEM_COMIN_SOON_STATE = "coming_soon";
    public static final String JSON_CATEGORY_ITEM_LIST_KEY = "categoryListingsInfo";
    public static final String JSON_CATEGORY_LIKE_COUNT = "like";
    public static final String JSON_CATEGORY_LIST_ID = "id";

    public static final String JSON_CATEGORY_LISTING_LIKE_ID = "cat_list_id";

    public static final String JSON_OFFER_ID = "id";
    public static final String JSON_OFFER_TITLE_NAME = "title";
    public static final String JSON_OFFER_TYPE = "type";
    public static final String JSON_OFFER_DESC = "description";
    public static final String JSON_OFFER_IMAGE = "image";
    public static final String JSON_OFFER_LIST_INFO_KEY = "offerInfo";

    public static final String JSON_CONTACT_US_NAME_KEY = "name";
    public static final String JSON_CONTACT_US_EMAIL_KEY = "email";
    public static final String JSON_CONTACT_US_MESSAGE_KEY = "message";
    public static final String JSON_CONTACT_US_CONTACT_KEY = "mobile_no";

    public static final String JSON_BRAND_CATEGORY = "category";
    public static final String JSON_BRAND_COMPANY_NAME = "brand";
    public static final String JSON_BRAND_YOUR_NAME = "name";
    public static final String JSON_BRAND_EMAIL = "email";
    public static final String JSON_BRAND_CONTACT_NUMBER = "contact";
    public static final String JSON_SUGGESTION_MESSAGE = "suggestion";


    public static final String JSON_CATEGORY_ERROR_CODE = "10001";
    public static final String JSON_CATEGORY_ITEM_ERROR_CODE = "20001";
    public static final String JSON_CONTACT_US_NAME_ERROR_CODE = "30001";
    public static final String JSON_CONTACT_US_EMAIL_ERROR_CODE = "30002";
    public static final String JSON_CONTACT_US_MESSAGE_ERROR_CODE = "30003";


    public static final String JSON_RESPONSE_STATUS_KEY = "status";
    public static final String JSON_RESPONSE_MSG_KEY = "message";
    public static final String JSON_RESPONSE_ERROR_CODE = "errorcode";


    public static final String JSON_TERMS_AND_CONDITION_KEY = "terms_condition";
    public static final String JSON_TERMS_AND_CONDITION_ERROR_CODE = "1001";

    public static final String JSON_PRIVACY_POLICY_KEY = "privacy_policy";
    public static final String JSON_PRIVACY_POLICY_ERROR_CODE = "1001";

    public static final String JSON_COPY_RIGHTS_KEY = "copyrights";
    public static final String JSON_COPY_RIGHTS_ERROR_CODE = "1001";

    public static final String JSON_BLOG_DETAILS_DESC_KEY = "post_details";
    public static final String JSON_BLOG_DETAILS_URL_KEY = "link";


    public static final String JSON_SEARCH_LIST_KEY = "search_data";
    public static final String JSON_SEARCH_LEVEL = "label";
    public static final String JSON_SEARCH_TYPE = "type";
    public static final String JSON_SEARCH_KEY = "seacrh";


    public static final String JSON_BUSINESS_DETAILED = "buisness_detail";

    public static final String BUSINESS_DETAILED_ID_KEY = "buisness_id";
    public static final String OFFER_DETAILED_ID_KEY = "offer_id";

    public static final String NOTIFICATION_TYPE_KEY = "type";
    public static final String NOTIFICATION_ID_KEY = "id";
    public static final String NOTIFICATION_TITLE_KEY = "title";
    public static final String NOTIFICATION_MSG_KEY = "message";
    public static final String NOTIFICATION_IMAGE_URL_KEY = "image";

    public static final String JSON_COMPANY_NAME_KEY = "company_name";
    public static final String JSON_COMPANY_USER_KEY = "name";
    public static final String JSON_DEVICE_ID_KEY = "device_id";


    public static final String JSON_COMPANY_CONTACT_KEY = "mobile";


    public static final String[] images = {
            "http://7606-presscdn-0-74.pagely.netdna-cdn.com/wp-content/uploads/2015/09/Burj-Khalifa-Tower-Dubai-Photos-Images-Pictures-Videos-10-800x600.jpg",
            "http://7606-presscdn-0-74.pagely.netdna-cdn.com/wp-content/uploads/2015/09/Burj-Khalifa-Tower-Dubai-Photos-Images-Pictures-Videos-10-800x600.jpg",
            "http://kuae.kustar.ac.ae/wp-content/uploads/2016/01/010.jpg",
            "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQQY6EJENo1nvvCB1wXGh6MLSiz4hKxm8m8k1LaInoXlPaDzZ5S",
            "http://assets.inhabitat.com/wp-content/blogs.dir/1/files/2012/12/burj-khalifa.jpg",
            "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQJl196wVhEMzXjdv3Cu8cloHot8ygMkYIfkfgvlxBKb-8jJghE",
            "http://7606-presscdn-0-74.pagely.netdna-cdn.com/wp-content/uploads/2015/09/Burj-Khalifa-Tower-Dubai-Photos-Images-Pictures-Videos-10-800x600.jpg",
            "http://kuae.kustar.ac.ae/wp-content/uploads/2016/01/010.jpg", "http://7606-presscdn-0-74.pagely.netdna-cdn.com/wp-content/uploads/2015/09/Burj-Khalifa-Tower-Dubai-Photos-Images-Pictures-Videos-10-800x600.jpg",
            "http://7606-presscdn-0-74.pagely.netdna-cdn.com/wp-content/uploads/2015/09/Burj-Khalifa-Tower-Dubai-Photos-Images-Pictures-Videos-10-800x600.jpg",
            "http://kuae.kustar.ac.ae/wp-content/uploads/2016/01/010.jpg",
            "http://7606-presscdn-0-74.pagely.netdna-cdn.com/wp-content/uploads/2015/09/Burj-Khalifa-Tower-Dubai-Photos-Images-Pictures-Videos-10-800x600.jpg",
            "http://7606-presscdn-0-74.pagely.netdna-cdn.com/wp-content/uploads/2015/09/Burj-Khalifa-Tower-Dubai-Photos-Images-Pictures-Videos-10-800x600.jpg",
            "http://kuae.kustar.ac.ae/wp-content/uploads/2016/01/010.jpg"};

    public static final String[] imageTitle = {
            "Burj Khalifa",
            "Sheikh Zayed Grand Mosque",
            "Burj Khalifa",
            "Burj Khalifa",
            "Burj Khalifa",
            "Burj Khalifa",
            "Burj Khalifa",
            "Burj Khalifa",
            "Burj Khalifa",
            "Burj Khalifa",
    };


    public static final String[] SOCIAL_MEDIA_URL_ARR = {
            "https://www.facebook.com/TOP10CLUBS",
            "https://twitter.com/TOP10CLUBS_",
            "https://plus.google.com/110394396834804084874",
            "https://www.linkedin.com/company/uae-top-10",
            "https://uaetopten.tumblr.com/",
            "https://del.icio.us/uaetop10",
            "http://www.stumbleupon.com/stumbler/UAETOP10",
            "http://uaetop10.deviantart.com/",
            "https://in.pinterest.com/TOP10CLUBS/",
            "https://www.slideshare.net/UAETOP10",
            "https://vimeo.com/user54290410",
            "https://foursquare.com/user/216287359",
            "https://en.wikipedia.org/wiki/User:UAETOP10",
            "https://www.youtube.com/channel/UCHDMl7TZSmnErW1EI8XMlWA",
            "https://www.instagram.com/top10clubs/"
    };
    public static final int[] SOCIAL_MEDIA_SHARE_ICON_ARR = {
            R.drawable.ic_social_facebook,
            R.drawable.ic_social_twitter,
            R.drawable.ic_social_google_plus,
            R.drawable.ic_social_linkedin,
            R.drawable.ic_social_tumblr,
            R.drawable.ic_social_delicious,
            R.drawable.ic_social_stumbleupon,
            R.drawable.ic_social_deviantart,
            R.drawable.ic_social_pinterest,
            R.drawable.ic_social_slideshare,
            R.drawable.ic_social_vimeo,
            R.drawable.ic_social_foursquare,
            R.drawable.ic_social_wikepedia,
            R.drawable.ic_social_youtube,
            R.drawable.ic_social_instagaram
    };
    //---------------API DESIGN-----------------------------

    public static final String API_BASE_URL_DEV = "https://www.uaet10.com";
    //public static final String API_BASE_URL_DEV = "http://www.uaet10.com";
    public static final String API_BASE_URL = API_BASE_URL_DEV;

    public static final String API_DOMAIN = "/webservices";

    public static final String HOME_CONTENT = "/homeContent";
    public static final String API_HOME_CONTENT = API_BASE_URL + API_DOMAIN + HOME_CONTENT;

    public static final String API_HOME_WITH_BANNER_TASK = API_BASE_URL + API_DOMAIN + "/homeContent1";

    public static final String CATEGORY_LIST = "/allCategory";
    public static final String API_CATEGORY_LIST = API_BASE_URL + API_DOMAIN + CATEGORY_LIST;

    public static final String CATEGORY_ITEM_LIST = "/categoryListing";
    public static final String API_CATEGORY_ITEM_LIST = API_BASE_URL + API_DOMAIN + CATEGORY_ITEM_LIST;

    public static final String CONTACT_US = "/contact_us";
    public static final String API_CONTACT_US = API_BASE_URL + API_DOMAIN + CONTACT_US;

    public static final String COMPANY_INFO = "/add_info";
    public static final String API_COMPANY_INFO = API_BASE_URL + API_DOMAIN + COMPANY_INFO;

    public static final String OFFER_LIST = "/offerListings";
    public static final String API_OFFER_LIST = API_BASE_URL + API_DOMAIN + OFFER_LIST;

    public static final String TERMS_AND_CONDITION = "/mobile_pages?page=21";
    public static final String API_TERMS_AND_CONDITION = API_BASE_URL + API_DOMAIN + TERMS_AND_CONDITION;

    public static final String PRIVACY_POLICY = "/mobile_pages?page=19";
    public static final String API_PRIVACY_POLICY = API_BASE_URL + API_DOMAIN + PRIVACY_POLICY;

    public static final String COPY_RIGHT = "/mobile_pages?page=20";
    public static final String API_COPY_RIGHT = API_BASE_URL + API_DOMAIN + COPY_RIGHT;

    public static final String BUSINESS_ENQUIRY = "/listing_suggetion";
    public static final String API_BUSINESS_ENQUIRY = API_BASE_URL + API_DOMAIN + BUSINESS_ENQUIRY;

    public static final String ABOUT_US = "/mobile_pages?page=22";
    public static final String API_ABOUT_US = API_BASE_URL + API_DOMAIN + ABOUT_US;

    public static final String SUGGESTION = "/mobile_suggestion";
    public static final String API_SUGGESTION = API_BASE_URL + API_DOMAIN + SUGGESTION;

    public static final String BLOG = "/get_post";
    public static final String API_BLOG = API_BASE_URL + API_DOMAIN + BLOG;

    public static final String BLOG_DETAILS = "/get_post_details";
    public static final String API_BLOG_DETAILS = API_BASE_URL + API_DOMAIN + BLOG_DETAILS;

    public static final String SEARCH_LIST = "/search_suggetion";
    public static final String API_SEARCH_LIST = API_BASE_URL + API_DOMAIN + SEARCH_LIST;

    public static final String BUSINESS_DETAILED = "/buisness_detail";
    public static final String API_BUSINESS_DETAILED = API_BASE_URL + API_DOMAIN + BUSINESS_DETAILED;

    public static final String OFFER_DETAILED = "/offer_voucher";
    public static final String API_OFFER_DETAILED = API_BASE_URL + API_DOMAIN + OFFER_DETAILED;

    public static final String BLOG_DETAILS_ID_KEY = "post_id";

    public static final String DEVICE_KEY = "/device_registration";
    public static final String API_DEVICE_KEY = API_BASE_URL + API_DOMAIN + DEVICE_KEY;

    public static final String POPUP_FLAG = "/get_popup_setting";
    public static final String API_POPUP_FLAG_CONTENT = API_BASE_URL + API_DOMAIN + POPUP_FLAG;

    public static final String MORE_OFFER = "/voucher_request";
    public static final String API_MORE_OFFER = API_BASE_URL + API_DOMAIN + MORE_OFFER;

    public static final String ADD_LIKE = "/add_like";
    public static final String API_ADD_LIKE = API_BASE_URL + API_DOMAIN + ADD_LIKE;

    public static final String ADD_VIEW = "/add_views";
    public static final String API_ADD_VIEW = API_BASE_URL + API_DOMAIN + ADD_VIEW;

    public static final String API_CATEGORY_ITEM_SHARE = API_BASE_URL + "/Category_item_Share";

    public static final String WELCOME_NOTIFICATION = "/device_registration1";
    public static final String API_WELCOME_NOTIFICATION = API_BASE_URL + API_DOMAIN + WELCOME_NOTIFICATION;

    public static final String ALL_NOTIFICATION = "/get_all_notification";
    public static final String API_ALL_NOTIFICATION = API_BASE_URL + API_DOMAIN + ALL_NOTIFICATION;

    public static final String OFFER_LIST_DEV = "/offerListings_dev";
    public static final String API_OFFER_LIST_DEV = API_BASE_URL + API_DOMAIN + OFFER_LIST_DEV;

    public static final String OFFER_DETAILED_DEV = "/offer_voucher_dev";
    public static final String API_OFFER_DETAILED_DEV  = API_BASE_URL + API_DOMAIN + OFFER_DETAILED_DEV;

    public static final String OFFER_DETAILED_USED = "/offer_voucher_used";
    public static final String API_OFFER_DETAILED_USED  = API_BASE_URL + API_DOMAIN + OFFER_DETAILED_USED;

}




