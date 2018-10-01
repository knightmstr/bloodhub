package com.dipak.dev.bloodapp;

public class Config {
    //MainActivity Config constants name
    //URL to login.php file
    public static String url_login ="http://10.0.2.2/BloodHub/login.php";
    public static String URL_FETCH_ID ="http://10.0.2.2/BloodHub/login_fetch_ID.php";
    //public static final String LOGIN_URL ="http://10.0.2.2/BloodHub/login.php";
    //public static final String LOGIN_URL2 ="http://10.0.2.2/BloodHub/login_fetch_ID.php";

    //Keys for email and password
    public static final String KEY_EMAIL = "email";
    public static final String KEY_PASSWORD = "password";

    //If server response is equal to this that means login is successful
    public static final String LOGIN_SUCCESS = "success";

    //Shared preference Keys
    //Name of SharedPreference
    public static final String SHARED_PREFERENCE_NAME = "BloodHub";

    //Store email of user that has logged in
    public static final String SHARED_PREFERENCE_EMAIL = "email";
    public static final String SHARED_PREFERENCE_UID = "UID";
   // public static final String SHARED_PREFERENCE_PID = "PID";

    //Store boolean value for tracking logged in user
    public static final String SHARED_PREFERENCE_LOGGED_IN = "logged_in";

    public static final String DATA_URL = "http://10.0.2.2/BloodHub/myprofile.php?id=";
    public static final String UPLOAD_DATA_URL = "http://10.0.2.2/BloodHub/update_profile.php";


    //JSON TAGS
    //public static final String TAG_IMAGE_URL = "image";
    public static final String TAG_USERNAME = "username";
    public static final String TAG_DOB = "dob";
    public static final String TAG_ADDRESS = "address";
    public static final String TAG_MOBILE = "mobile";
    public static final String TAG_BLOOD_GROUP = "bloodgroup";
    public static final String TAG_DONATION_DATE = "donationDate";
   // public static final String TAG_EMAIL = "email";
  //  public static final String TAG_POST_ID = "post_id";
}
