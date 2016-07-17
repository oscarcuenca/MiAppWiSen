package com.amg_eservices.miappwisen.RegistroyAcceso;

public class Config {
    //URI to our login.php file

    public static final String LOGIN_URL = "http://wi-sen.esy.es/wisen/Sensores/v1/usuarios/login";

    //Keys for email and password as defined in our $_POST['key'] in login.php

    public static final String KEY_EMAIL = "user_email";
    public static final String KEY_PASSWORD = "contrasena";
    // For login not need Api but for authentification to get object data will be need it
    public static final String KEY_API = "claveApi";

    //If server response is equal to this that means login is successful
    public static final String LOGIN_SUCCESS = "success";

    //Keys for Sharedpreferences
    //This would be the name of our shared preferences
    public static final String SHARED_PREF_NAME = "myloginapp";

    //This would be used to store the email and Api Key of current logged in user

    public static final String EMAIL_SHARED_PREF = "user_email";
    public static final String API_SHARED_PREF = "claveApi";

    //We will use this to store the boolean in sharedpreference to track user is loggedin or not
    public static final String LOGGEDIN_SHARED_PREF = "loggedin";
}
