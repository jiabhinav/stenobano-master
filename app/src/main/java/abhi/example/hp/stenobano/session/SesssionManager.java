package abhi.example.hp.stenobano.session;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;


import java.util.HashMap;

import abhi.example.hp.stenobano.auth.Login;
import abhi.example.hp.stenobano.Dashboard.User_Home;

import static abhi.example.hp.stenobano.config.BaseUrl.SP;




public class SesssionManager {

    SharedPreferences sp_login;
    SharedPreferences.Editor sp_editor;
    Context context;
    int PRIVATE_MODE = 0;
    int bal=0;
    public SesssionManager(Context context) {
        this.context = context;
        sp_login = context.getSharedPreferences(SP, PRIVATE_MODE);
        sp_editor = sp_login.edit();

    }
    public static String ID="id";
    public static String NAME="name";
    public static String EMAIL="email";
    public static String MOBILE="mobile";
    public static String TYPE="type";
    public static String VALID="valid";
    public static String DEVICE_ID="device_id";
    public static String IMAGE="image";
    public static String DEFAULT_PLAY="default_play";
    public static String DEFAULT_SPEED="default_speed";
    public static String TOKEN="token";
    public void sessionLogin(String id, String name, String email, String mobile, String type,String valid,String device_id,String image,String token)
    {
        sp_editor.putString(ID, id);
        sp_editor.putString(EMAIL, email);
        sp_editor.putString(NAME, name);
        sp_editor.putString(MOBILE, mobile);
        sp_editor.putString(TYPE, type);
        sp_editor.putString(VALID, valid);
        sp_editor.putString(DEVICE_ID, device_id);
        sp_editor.putString(IMAGE, image);
        sp_editor.putString(TOKEN, token);
        sp_editor.commit();


    }

    public void setPlay(String value)
    {
        sp_editor.putString(DEFAULT_PLAY, value);
        sp_editor.commit();

    }
    public void setSpeed(String value)
    {
        sp_editor.putString(DEFAULT_SPEED, value);
        sp_editor.commit();

    }


    public void checkLogin() {

        if (isLoggedIn()==null) {
            Intent loginsucces = new Intent(context, Login.class);
            loginsucces.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP| Intent.FLAG_ACTIVITY_CLEAR_TASK);
            context.startActivity(loginsucces);
        }
        else
        {

                Intent loginsucces = new Intent(context, User_Home.class);
                loginsucces.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP| Intent.FLAG_ACTIVITY_CLEAR_TASK);
                context.startActivity(loginsucces);
            }



    }

    public String isLoggedIn() {
        return sp_login.getString(MOBILE, null);
    }

    public void logoutSession() {
        sp_editor.clear();
        sp_editor.commit();
        Intent logout = new Intent(context, Login.class);
        logout.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP| Intent.FLAG_ACTIVITY_CLEAR_TASK);;
        context.startActivity(logout);
    }


    public HashMap<String, String> getUserDetails() {
        HashMap<String, String> user = new HashMap<String, String>();
        user.put(ID, sp_login.getString(ID, null));
        user.put(NAME, sp_login.getString(NAME, null));
        user.put(EMAIL, sp_login.getString(EMAIL, null));
        user.put(MOBILE, sp_login.getString(MOBILE, null));
        user.put(TYPE, sp_login.getString(TYPE, null));
        user.put(VALID, sp_login.getString(VALID, null));
        user.put(DEVICE_ID, sp_login.getString(DEVICE_ID, null));
        user.put(IMAGE, sp_login.getString(IMAGE, "0"));
        return user;
    }


    public String userID()
    {
        return sp_login.getString(ID, null);
    }
    public String userMobile()
    {
        return sp_login.getString(MOBILE, null);
    }

    public String getName()
    {
        return sp_login.getString(NAME, null);
    }

    public String getSpeed()
    {
        return sp_login.getString(DEFAULT_SPEED, "1.");
    }
    public String getDefaultPlay()
    {
        return sp_login.getString(DEFAULT_PLAY, "next");
    }

    public String userEmail()
    {
        return sp_login.getString(EMAIL, null);
    }
    public String userDevice_ID()
    {
        return sp_login.getString(DEVICE_ID, null);
    }

    public String getImage()
    {
        return sp_login.getString(IMAGE, "0");
    }

    public String getValid()
    {
        return sp_login.getString(VALID, "0");
    }

    public String getToken()
    {
        return sp_login.getString(TOKEN, "0");
    }

    public void updateKYCStatus(String kyc) {
        //sp_editor.putString(KYC, kyc);
        sp_editor.apply();

    }
    public void updateImage(String image) {
        sp_editor.putString(IMAGE, image);
        sp_editor.apply();

    }

    public void updateToken(String token) {
        sp_editor.putString(TOKEN, token);
        sp_editor.apply();

    }

    public void updateValid(String date) {
        sp_editor.putString(VALID, date);
        sp_editor.apply();

    }
    public void updateUserId(String id, String status) {
        sp_editor.putString(ID, id);
      //  sp_editor.putString(STATUS, status);
        sp_editor.commit();
    }


}
