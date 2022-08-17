package abhi.example.hp.stenobano.Dashboard;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;

import com.giphy.sdk.core.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import abhi.example.hp.stenobano.adapter.AdsBannerAdapter;
import abhi.example.hp.stenobano.adapter.CategoryAapter;
import abhi.example.hp.stenobano.auth.Login;
import abhi.example.hp.stenobano.chat.Inbox_F;
import abhi.example.hp.stenobano.config.BaseUrl;
import abhi.example.hp.stenobano.config.Base_Url;
import abhi.example.hp.stenobano.R;
import abhi.example.hp.stenobano.config.Constant;
import abhi.example.hp.stenobano.model.ModelAds;
import abhi.example.hp.stenobano.model.ModelCategory;
import abhi.example.hp.stenobano.notification.ResultNotification;
import abhi.example.hp.stenobano.other_class.ProcessingDialog;
import abhi.example.hp.stenobano.retrofit.APIClient;
import abhi.example.hp.stenobano.session.SesssionManager;
import abhi.example.hp.stenobano.sqlite_databse.DatabaseHandler;
import abhi.example.hp.stenobano.user.Get_Data;
import abhi.example.hp.stenobano.user.Get_Long_speed_Test;
import abhi.example.hp.stenobano.user.Help;
import abhi.example.hp.stenobano.user.Offline_File;
import abhi.example.hp.stenobano.user.Profile;
import abhi.example.hp.stenobano.user.Renewal_History;
import abhi.example.hp.stenobano.user.SendRequetToUpdate;
import abhi.example.hp.stenobano.user.Type_Searching;
import abhi.example.hp.stenobano.user.ViewSendRequest;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;

public class User_Home extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener, AdsBannerAdapter.BottomMenu {
    String dictonary_status;
    private ProcessingDialog processingDialog;
    TextView textname,name, mobile,news;
    EditText old, newpassword, confimpassword;
    private ImageView sideMenu,notification,close,imageView;
    List<ModelCategory.Result>modellist;
    List<ModelAds.Result>modelListAds;
    CardView savepass;
    String user_id;
    ProgressDialog progressDialog;
    CardView atoz, phrases, legal, contraction, short_form, steno_notification, long_speed_test, short_speed_test,
            legal_speed_test, kc_special_word, ne_dicitation, leaglword, wordofweek,card_serch,preyeardictation,
            card_kc_imp_word;
    RequestQueue requestQueue;
    ProgressBar progressBar;
    DrawerLayout drawer;
    String mobile2;
    AlertDialog alertDialog;
    int i = 0;
    private FragmentManager mFragmentManager;
    private String currentVersion;
    private SesssionManager sesssionManager;
    private HashMap<String,String>map;
    private RecyclerView recyclerView;
   private RecyclerView ads_recycler;
    private CategoryAapter adapter;

    private ImageView chat;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user__home);
        screenwidth_and_Heights();
        sesssionManager=new SesssionManager(this);
        processingDialog=new ProcessingDialog(this);
        map=new HashMap<>();
        map=sesssionManager.getUserDetails();
        news=findViewById(R.id.news);
        String name2 = map.get("name");
        mobile2 =  map.get("mobile");
        user_id =  map.get("id");
        BaseUrl.SCHOOL_ID="01012021";
        try {
            //getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
            currentVersion = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e)
        {
            e.printStackTrace();
        }
        textname=findViewById(R.id.name);
        textname.setText("Hi, "+name2);
        sideMenu=findViewById(R.id.sideMenu);
        sideMenu.setOnClickListener(this);
        atoz = findViewById(R.id.atoz);
        phrases = findViewById(R.id.phreses);
        legal = findViewById(R.id.leagal);
        contraction = findViewById(R.id.contraction);
        short_form = findViewById(R.id.shortform);
        requestQueue = Volley.newRequestQueue(this);
        atoz.setOnClickListener(this);
        phrases.setOnClickListener(this);
        legal.setOnClickListener(this);
        contraction.setOnClickListener(this);
        short_form.setOnClickListener(this);
        long_speed_test = findViewById(R.id.long_speed_test);
        long_speed_test.setOnClickListener(this);
        short_speed_test = findViewById(R.id.sort_speed_test);
        short_speed_test.setOnClickListener(this);
        legal_speed_test = findViewById(R.id.legal_speed_test);
        legal_speed_test.setOnClickListener(this);
        kc_special_word = findViewById(R.id.kc_special_word);
        kc_special_word.setOnClickListener(this);
        ne_dicitation = findViewById(R.id.ne_dicitation);
        ne_dicitation.setOnClickListener(this);

        leaglword = findViewById(R.id.leaglword);
        leaglword.setOnClickListener(this);

        wordofweek = findViewById(R.id.wordofweek);
        wordofweek.setOnClickListener(this);
        notification=findViewById(R.id.notification);
        notification.setOnClickListener(this);
        chat=findViewById(R.id.chatclick);
        chat.setOnClickListener(this);
        card_serch=findViewById(R.id.card_serch);
        card_serch.setOnClickListener(this);
        card_kc_imp_word=findViewById(R.id.card_kc_imp_word);
        card_kc_imp_word.setOnClickListener(this);
        recyclerView=findViewById(R.id.recyclerView);
        ads_recycler=findViewById(R.id.recycler_ads);
        drawer =  findViewById(R.id.drawer_layout);


      /*  ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();*/

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setItemIconTintList(null);
        navigationView.setNavigationItemSelectedListener(this);
        name = navigationView.getHeaderView(0).findViewById(R.id.name);
        mobile = navigationView.getHeaderView(0).findViewById(R.id.mobile);
        imageView = navigationView.getHeaderView(0).findViewById(R.id.imageView);
        imageView.setOnClickListener(this);
        name.setText("Hi, "+name2);
        mobile.setText(mobile2);
        // comming_soon();

        checkUserStatus(mobile2);

       /* customHandler = new android.os.Handler();
        customHandler.postDelayed(updateTimerThread, 0);*/


        FirebaseMessaging.getInstance().subscribeToTopic("all");
        //==============================notoification=========
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel chanal = new NotificationChannel("All", "all", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(chanal);
        } else {

        }
        modellist=new ArrayList<>();
        LinearLayoutManager layoutManager=new GridLayoutManager(this,3,LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(layoutManager);


        getAdminDashboardData();
        getAds();
        setImageProfile();

       String data= new Gson().toJson(new DatabaseHandler(this).getAllContacts());
       Log.d("dataisso",data);
        mFragmentManager = ((FragmentActivity) this).getSupportFragmentManager();
        getToken();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (mFragmentManager.getBackStackEntryCount() > 0)
                mFragmentManager.popBackStackImmediate();
            else super.onBackPressed();
        }
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.user__home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        final int id = item.getItemId();

        if (id == R.id.change_password) {
            change_password();
            return true;
        } else if (id == R.id.search) {
            Intent i = new Intent(User_Home.this, ResultNotification.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);
            return true;
        }


        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();


      if (id == R.id.ofline_dictection) {
            Intent i = new Intent(User_Home.this, Offline_File.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);
        }
      else if (id == R.id.profile) {
          Intent i = new Intent(User_Home.this, Profile.class);
          i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
          startActivity(i);
      }

      else if (id == R.id.renewal_history) {
            Intent i = new Intent(User_Home.this, Renewal_History.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);
        } else if (id == R.id.view_notoficatoon) {
            Intent i = new Intent(User_Home.this, ResultNotification.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);
        } else if (id == R.id.request) {
            Intent i = new Intent(User_Home.this, ViewSendRequest.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);
        }

      else if (id == R.id.chat) {
          Inbox_F frag = new Inbox_F();
          FragmentManager manager = getSupportFragmentManager();
          FragmentTransaction transaction = manager.beginTransaction();
          transaction.replace(R.id.drawer_layout,frag,"Inbox");
          transaction.addToBackStack(null);
          transaction.commit();
      }

      else if (id == R.id.change_password) {
         change_password();
      }

      else if (id == R.id.shareapp) {
        /*  try {
              Intent shareIntent = new Intent(Intent.ACTION_SEND);
              shareIntent.setType("text/plain");
              shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Steno Bano");
              String shareMessage= "\nLet me recommend you this application\n\n";
              shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID +"\n\n";
              shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
              startActivity(Intent.createChooser(shareIntent, "choose one"));
          } catch(Exception e) {
              //e.toString();
          }*/
          invitefriend();
      }
      //============================
      else if (id == R.id.logout) {


            AlertDialog.Builder builder;
            builder = new AlertDialog.Builder(this);
            builder.setTitle("Logout");
            builder.setIcon(R.drawable.stano8);
            builder.setMessage("Do you want to sure logout?");
            builder.setCancelable(false);
            builder.create();
            final AlertDialog alertDialog = builder.create();
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    logout();

                }
            }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    alertDialog.dismiss();
                }
            });
            builder.show();


        } else if (id == R.id.email) {
            Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:" + "nirankarirajender@gmail.com"));
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Complaint/ any Query!!!");
            emailIntent.putExtra(Intent.EXTRA_TEXT, "Enter complaint Or query message here....");
            startActivity(Intent.createChooser(emailIntent, "Chooser Title"));
        } else if (id == R.id.call) {
            Intent i = new Intent(User_Home.this, Help.class);
            i.putExtra("type_id", "107");
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id==R.id.sideMenu)
        {
            if (drawer.isDrawerOpen(GravityCompat.START)) {
                drawer.closeDrawer(GravityCompat.START);
            } else {
                drawer.openDrawer(GravityCompat.START);
            }
        }
        else if (id==R.id.card_serch)
        {
            if (dictonary_status.equals("deactive")) {
                alertdialog();
            } else {
                Intent i = new Intent(User_Home.this, Type_Searching.class);
                i.putExtra("type_id", "101");
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
            }
        }

       else if (id == R.id.atoz) {

            if (dictonary_status.equals("deactive")) {
                alertdialog();
            } else {
                Intent i = new Intent(User_Home.this, Type_Searching.class);
                i.putExtra("type_id", "101");
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
            }
        } else if (id == R.id.phreses) {
            Intent i = new Intent(User_Home.this, Get_Data.class);
            i.putExtra("type_id", "102");
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);
        } else if (id == R.id.shortform) {
            Intent i = new Intent(User_Home.this, Get_Data.class);
            i.putExtra("type_id", "103");
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);
        } else if (id == R.id.contraction) {
            Intent i = new Intent(User_Home.this, Get_Data.class);
            i.putExtra("type_id", "104");
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);
        } else if (id == R.id.leagal) {
            Intent i = new Intent(User_Home.this, Get_Data.class);
            i.putExtra("type_id", "105");
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);
        } else if (id == R.id.long_speed_test) {

            Intent i = new Intent(User_Home.this, Get_Long_speed_Test.class);
            i.putExtra("type_id", "108");
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);
        } else if (id == R.id.sort_speed_test) {

            Intent i = new Intent(User_Home.this, Get_Data.class);
            i.putExtra("type_id", "109");
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);
        } else if (id == R.id.legal_speed_test) {

            Intent i = new Intent(User_Home.this, Get_Data.class);
            i.putExtra("type_id", "110");
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);
        } else if (id == R.id.kc_special_word) {

            Intent i = new Intent(User_Home.this, Get_Data.class);
            i.putExtra("type_id", "111");
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);
        } else if (id == R.id.ne_dicitation) {
            Intent i = new Intent(User_Home.this, Get_Data.class);
            i.putExtra("type_id", "112");
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);
        } else if (id == R.id.leaglword) {
            Intent i = new Intent(User_Home.this, Get_Data.class);
            i.putExtra("type_id", "113");
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);
        } else if (id == R.id.wordofweek) {
            Intent i = new Intent(User_Home.this, Get_Data.class);
            i.putExtra("type_id", "114");
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);
        }

        else if (id == R.id.card_kc_imp_word) {
            Intent i = new Intent(User_Home.this, Get_Data.class);
            i.putExtra("type_id", "115");
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);
        }

       else if (id==R.id.notification)
        {
                Intent i = new Intent(User_Home.this, ResultNotification.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);

        }
        else if (id==R.id.imageView)
        {
            Intent i = new Intent(User_Home.this, Profile.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);

        }
        else if (id == R.id.chatclick) {
            Inbox_F frag = new Inbox_F();
            FragmentManager manager = getSupportFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.replace(R.id.drawer_layout,frag,"Inbox");
            transaction.addToBackStack(null);
            transaction.commit();
        }



    }

    public void comming_soon() {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String url = Base_Url.url + "comming_soon.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("dswdfff", response.toString());
                // Toast.makeText(User_Home.this, "res is"+response, Toast.LENGTH_SHORT).show();
                JSONArray jsonArray = null;
                try {
                    jsonArray = new JSONArray(response);
                    JSONObject jsonObject = jsonArray.getJSONObject(0);
                    dictonary_status = jsonObject.getString("status");

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> check = new HashMap<>();

                return check;
            }
        };
        requestQueue.add(stringRequest);


    }

    public void alertdialog() {
        AlertDialog.Builder builder;
        builder = new AlertDialog.Builder(this);
        builder.setTitle("Comming Soon");
        builder.setIcon(R.drawable.stano8);
        builder.setMessage("Dctionary is comming soon ,So please wait for some time!!!!");
        builder.setCancelable(false);
        builder.create();
        final AlertDialog alertDialog = builder.create();
        builder.setNegativeButton("Close", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                alertDialog.dismiss();
            }
        });
        builder.show();

    }

    private void checkUserStatus(String mobile2) {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String url = Base_Url.url + "User_Status.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                // Toast.makeText(User_Home.this, "res is"+response, Toast.LENGTH_SHORT).show();
                JSONArray jsonArray = null;
                try {
                    jsonArray = new JSONArray(response);
                    JSONObject jsonObject = jsonArray.getJSONObject(0);
                    dictonary_status = jsonObject.getString("status");
                    if (dictonary_status.equals("deactive")) {
                        alertDeactive();
                    } else {
                        comming_soon();

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                chekNEt();

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> check = new HashMap<>();
                check.put("mobile", mobile2);

                return check;
            }
        };
        requestQueue.add(stringRequest);


    }

    public void alertDeactive() {
        AlertDialog.Builder builder;
        builder = new AlertDialog.Builder(this);
        builder.setTitle("Account has been Deactivated");
        builder.setIcon(R.drawable.stano8);
        builder.setMessage("Sorry!!,You can,t accese steno Docs Or file without activation!!!!");
        builder.setCancelable(false);
        builder.create();
        final AlertDialog alertDialog = builder.create();
        builder.setNegativeButton("Close", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                alertDialog.dismiss();
                finish();
            }
        });
        builder.show();

    }

    public void chekNEt() {
        AlertDialog.Builder builder;
        builder = new AlertDialog.Builder(this);
        builder.setTitle("Check Internet Connection");
        builder.setIcon(R.drawable.stano8);
        builder.setMessage("Please check first internet Connection!");
        builder.setCancelable(false);
        builder.create();
        final AlertDialog alertDialog = builder.create();
        builder.setNegativeButton("Close", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                alertDialog.dismiss();
                finish();
            }
        });
        builder.show();

    }


    private void logoutfromDB(String mobile2) {
        progressDialog = new ProgressDialog(User_Home.this);
        progressDialog.setCancelable(false);
        progressDialog.setTitle("Please wait");
        progressDialog.setMessage("logout.........");
        progressDialog.show();
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String url = Base_Url.url + "logout.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                // Toast.makeText(User_Home.this, "res is"+response, Toast.LENGTH_SHORT).show();
                if (response.equals("Logout")) {

                    //customHandler.removeCallbacks(updateTimerThread);
                    SharedPreferences sp = getSharedPreferences("steno_info", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sp.edit();
                    editor.remove("id");
                    editor.remove("name");
                    editor.remove("email");
                    editor.remove("mobile");
                    editor.remove("type");
                    editor.remove("valid");
                    editor.commit();
                    Intent intent = new Intent(User_Home.this, Login.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                    progressDialog.dismiss();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();


            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> check = new HashMap<>();
                check.put("mobile", mobile2);

                return check;
            }
        };
        requestQueue.add(stringRequest);


    }

    private void getDeviceID(String user_id) {
        progressDialog = new ProgressDialog(User_Home.this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Wait...");
       // progressDialog.show();
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String url = Base_Url.url + "getDeviceID.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                // Toast.makeText(User_Home.this, "res is"+response, Toast.LENGTH_SHORT).show();
                Log.d("dsddwwfw", response.toString());
                progressDialog.dismiss();
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    if (!response.equals("null")) {
                        JSONObject jsonObject = jsonArray.getJSONObject(0);
                        String device_id = jsonObject.getString("device_id");
                        if (getDeviceUniqueID().equals(device_id)) {

                        } else {
                            alertDiviceNotMatch();
                        }

                    } else {
                        alertUpdateDevice();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("wdwdwwf", error.toString());
                progressDialog.dismiss();

                AlertDialog.Builder builder = new AlertDialog.Builder(User_Home.this);
                builder.create();
                builder.setTitle("Error!!!");
                builder.setMessage("Please check internet connection");
                builder.setCancelable(false);
                builder.setPositiveButton("Retry", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        getDeviceID(user_id);
                        progressDialog.show();
                    }
                }).setNegativeButton("Close", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();

                    }
                });
                builder.show();


            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> check = new HashMap<>();
                check.put("user_id", user_id);

                return check;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                60000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);


    }

    @Override
    public void clickBanner(int position) {

        if (modelListAds.get(position).getClickable().equals("1"))
        {
            String url=modellist.get(position).getUrl();
            try {
                Intent i = new Intent(Intent.ACTION_VIEW);
                if (!url.startsWith("http://") && !url.startsWith("https://"))
                {
                    url = "http://" + url;
                }
                i.setData(Uri.parse(url));
                startActivity(i);
            }
            catch (Exception e)
            {

            }
        }

    }


    class GetVersionCode extends AsyncTask<Void, String, String> {

        @Override

        protected String doInBackground(Void... voids) {

            String newVersion = null;

            try {
                Document document = Jsoup.connect("https://play.google.com/store/apps/details?id=" + User_Home.this.getPackageName() + "&hl=en")
                        .timeout(60000)
                        .userAgent("Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6")
                        .referrer("https://www.google.com")
                        .get();
                if (document != null) {
                    Elements element = document.getElementsContainingOwnText("Current Version");
                    for (Element ele : element) {
                        if (ele.siblingElements() != null) {
                            Elements sibElemets = ele.siblingElements();
                            for (Element sibElemet : sibElemets) {
                                newVersion = sibElemet.text();
                            }
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return newVersion;

        }


        @Override
        protected void onPostExecute(String onlineVersion) {
            super.onPostExecute(onlineVersion);
           double onli= Double.parseDouble(onlineVersion);
            Log.d("current_version-",currentVersion+"-onlineversion-"+onli);
            if (onlineVersion != null && !onlineVersion.isEmpty()) {

                if (Double.parseDouble(currentVersion) < onli) {
                    alertUpdateDialog();
                    // Toast.makeText(MainActivity.this, ""+"Current version " + currentVersion + "playstore version " + onlineVersion, Toast.LENGTH_SHORT).show();
                } else {

                    //Toast.makeText(MainActivity.this, "Not available", Toast.LENGTH_SHORT).show();
                }

            }

           /* Log.d("update", "Current version " + currentVersion + "playstore version " + onlineVersion);
            Toast.makeText(MainActivity.this, ""+"Current version " + currentVersion + "playstore version " + onlineVersion, Toast.LENGTH_SHORT).show();
*/
        }
    }
    @Override
    protected void onResume() {
       // new GetVersionCode().execute();
            try
            {
                alertDialog.dismiss();
            }
            catch (Exception e)
            {
            }

        getDeviceID(sesssionManager.userID());
        super.onResume();
    }


    private void alertUpdateDialog() {
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(this);
        builder.setMessage("Letest Version is Available.Click on UPDATE to Update app");
        builder.setIcon(getResources().getDrawable(R.mipmap.ic_black_icon_steno));
        builder.setTitle("Steno Bano");
        builder.setCancelable(false);
        builder.setPositiveButton("UPDATE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
                try {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse("https://play.google.com/store/apps/details?id="+getPackageName()));
                    intent.setPackage(appPackageName);
                    startActivity(intent);
                } catch (android.content.ActivityNotFoundException anfe) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id="+getPackageName())));
                }
            }
        }).setNegativeButton("Exit App", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();

            }
        });
        builder.show();
    }

    private void alertUpdateDevice() {
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(this);
        builder.setMessage("Your device does not registered,please update device first!");
        builder.setIcon(getResources().getDrawable(R.mipmap.ic_black_icon_steno));
        builder.setTitle("Steno Bano");
        builder.setCancelable(false);
        builder.setPositiveButton("UPDATE Device", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {


            }
        }).setNegativeButton("Close", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();

            }
        });
        builder.show();
    }

    private void checkLoginDevice() {
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(this);
        builder.setMessage("Your registered device does not match,please register own device");
        builder.setIcon(getResources().getDrawable(R.mipmap.ic_black_icon_steno));
        builder.setTitle("Alert");
        builder.setCancelable(false);
        builder.setPositiveButton("UPDATE DEVICE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
                try {
                  /*  Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse("https://play.google.com/store/apps/details?id=abhi.example.hp.stenobano"));
                    intent.setPackage(appPackageName);
                    startActivity(intent);*/
                } catch (android.content.ActivityNotFoundException anfe) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=abhi.example.hp.stenobano")));
                }
            }
        }).setNegativeButton("Exit App", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();

            }
        });
        builder.show();
    }


    public String getDeviceUniqueID() {
        String device_unique_id = Settings.Secure.getString(this.getContentResolver(),
                Settings.Secure.ANDROID_ID);

      //  Log.d("dddweff", device_unique_id);
        Log.d("dddweff", getDeviceIMEI());
        return getDeviceIMEI();

    }


    public String getDeviceIMEI() {
        String deviceUniqueIdentifier = null;
        TelephonyManager tm = (TelephonyManager)this.getSystemService(Context.TELEPHONY_SERVICE);
      /*  if (null != tm) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE}, 1);
                deviceUniqueIdentifier = tm.getDeviceId();
                return deviceUniqueIdentifier;
            }
            deviceUniqueIdentifier = tm.getDeviceId();
        }*/
        if (null == deviceUniqueIdentifier || 0 == deviceUniqueIdentifier.length()) {
            deviceUniqueIdentifier = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);
        }

        return deviceUniqueIdentifier;
    }


    private  void  alertDiviceNotMatch()
    {
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        builder.setCancelable(false);
        final View view = inflater.inflate(R.layout.alert_check_device, null);
        TextView update=view.findViewById(R.id.update);
        TextView logout=view.findViewById(R.id.logout);

        TextView close=view.findViewById(R.id.close);
        builder.setView(view);
        alertDialog=builder.create();
        alertDialog.show();
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkUpdation();
            }
        });
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });
    }

    private void logout()
    {

        new SesssionManager(this).logoutSession();
        Intent intent = new Intent(User_Home.this, Login.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK| Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();}


        private void change_password()
        {
            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
            LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final View view = inflater.inflate(R.layout.activity_change__password, null);
            AlertDialog dialog=builder.create();
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.getWindow().getAttributes().windowAnimations = R.style.DialogTheme;
            dialog.setView(view);
            dialog.show();
            close=view.findViewById(R.id.close);
            close.setOnClickListener(this);
            old = view.findViewById(R.id.oldpaswword);
            newpassword = view.findViewById(R.id.newpassword);
            confimpassword = view.findViewById(R.id.confirmpassword);
            savepass = view.findViewById(R.id.savepassword);
            progressBar = view.findViewById(R.id.progress);

            progressBar.setVisibility(View.GONE);

            savepass.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (old.getText().toString().equals("")) {
                        Toast.makeText(User_Home.this, "Enter Old password", Toast.LENGTH_SHORT).show();

                    } else if (newpassword.getText().toString().equals("")) {
                        Toast.makeText(User_Home.this, "Enter description", Toast.LENGTH_SHORT).show();
                    } else if (confimpassword.getText().toString().equals("")) {
                        Toast.makeText(User_Home.this, "Enter description", Toast.LENGTH_SHORT).show();
                    } else if (!confimpassword.getText().toString().equals(newpassword.getText().toString())) {
                        Toast.makeText(User_Home.this, "Confirm password dose not match", Toast.LENGTH_SHORT).show();
                    } else {
                        progressBar.setVisibility(View.VISIBLE);
                        String url = Base_Url.url + "change_password.php";
                        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {

                                Toast.makeText(User_Home.this, "" + response, Toast.LENGTH_SHORT).show();
                                progressBar.setVisibility(View.GONE);
                                old.setText("");
                                newpassword.setText("");
                                confimpassword.setText("");
                                dialog.dismiss();

                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(User_Home.this, "" + error, Toast.LENGTH_SHORT).show();
                                progressBar.setVisibility(View.GONE);

                            }
                        }) {
                            @Override
                            protected Map<String, String> getParams() throws AuthFailureError {

                                Map<String, String> data = new HashMap<>();
                                data.put("user_id", user_id);
                                data.put("old", old.getText().toString());
                                data.put("new_password", newpassword.getText().toString());

                                return data;
                            }
                        };
                        //stringRequest.setRetryPolicy(new DefaultRetryPolicy(60000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                        // requestQueue.add(stringRequest);
                        requestQueue.add(stringRequest);

                    }
                }
            });

            close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
        }




    private void checkUpdation()
    {
        progressDialog = new ProgressDialog(User_Home.this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Checking,please wait...");
        progressDialog.show();
        RequestQueue requestQueue=Volley.newRequestQueue(this);
        String url =  Base_Url.url+"checkUpdation.php";
        StringRequest stringRequest=new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                // Toast.makeText(User_Home.this, "res is"+response, Toast.LENGTH_SHORT).show();
                Log.d("dsddwwfw",response.toString());
                progressDialog.dismiss();

                    if (response.equals("1"))
                    {
                        Toast.makeText(User_Home.this, "Device has been updated", Toast.LENGTH_SHORT).show();
                        alertDialog.dismiss();
                    }
                    else
                    {
                        Intent intent=new Intent(User_Home.this, SendRequetToUpdate.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        alertDialog.dismiss();
                    }
                }


        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("wdwdwwf",error.toString());
                progressDialog.dismiss();

                AlertDialog.Builder builder=new AlertDialog.Builder(User_Home.this);
                builder.create();
                builder.setTitle("Error!!!");
                builder.setMessage("Please check internet connection");
                builder.setCancelable(false);
                builder.setPositiveButton("Retry", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        getDeviceID(user_id);
                    }
                }).setNegativeButton("Close", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.show();



            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String>check=new HashMap<>();
                check.put("user_id",user_id);
                check.put("device_id",getDeviceUniqueID());
                check.put("mobile",mobile2);

                return check;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                60000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);


    }


    private void setImageProfile()
    {
        if (!new SesssionManager(this).getImage().equals("0"))
        {
            String url= BaseUrl.profile_image+new SesssionManager(this).getImage();
            Log.d("imageurl",url);
            Picasso.with(getApplicationContext()).load(url).fit().centerCrop()
                    .placeholder(R.drawable.ic_person_outline_black_24dp)
                    .error(R.drawable.ic_person_outline_black_24dp)
                    .into(imageView);
        }
        else
        {

        }
    }



    private void getAdminDashboardData()
    {
        processingDialog.show("");
        RequestBody key = RequestBody.create(MultipartBody.FORM, "stenobano");
        Call<ModelCategory> call = APIClient.getInstance().getCategory(key);
        call.enqueue(new Callback<ModelCategory>() {
            @Override
            public void onResponse(Call<ModelCategory> call, retrofit2.Response<ModelCategory> response) {
                processingDialog.dismiss();
                Log.d("type122sdddd", "msg" + new Gson().toJson(response.body()));
                ModelCategory model=response.body();
                modellist=model.getResult();
                adapter=new CategoryAapter(modellist,User_Home.this);
                recyclerView.setAdapter(adapter);
                if (model.getNews().size()>0)
                {
                    news.setText(model.getNews().get(0).getMessage());
                    news.setSelected(true);
                    if (model.getNews().get(0).getClickable().equals("1"))
                    {
                        news.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                String url =model.getNews().get(0).getUrl();

                                try {
                                    Intent i = new Intent(Intent.ACTION_VIEW);
                                    if (!url.startsWith("http://") && !url.startsWith("https://"))
                                    {
                                        url = "http://" + url;
                                    }
                                    i.setData(Uri.parse(url));
                                    startActivity(i);
                                }
                                catch (Exception e)
                                {

                                }

                            }
                        });
                    }
                    else
                    {
                        news.setEnabled(false);
                    }

                }

                Log.d("ADMINDATAUISU", "onResponse: "+new Gson().toJson(model.getAdmin()));
                if (model.getAdmin().size()>0)
                {
                    BaseUrl.STENO_NAME=model.getAdmin().get(0).getName();
                    BaseUrl.STENO_MOBILE=model.getAdmin().get(0).getMobile();
                    BaseUrl.STENO_IMAGE=model.getAdmin().get(0).getImage();
                    BaseUrl.STENO_TOKEN=model.getAdmin().get(0).getToken();
                }
                else
                {
                    BaseUrl.STENO_NAME="Steno Bano";
                    BaseUrl.STENO_MOBILE="7290000723";
                    BaseUrl.STENO_TOKEN="f6ffCXV1SryI2OoO69-03J:APA91bHGs91CiPetKPrXk4iC-bO0VcJ_3AqIYZeOAxxpeIxTnanVKoc3zJlNyVWwulWWRDi-uD20pF6jvmeMv6xDWnuC1ry6WxFQbxi2j5HR5S_-vIQYsWY9SNAbANdjg5rfTfbncubK";
                    BaseUrl.STENO_IMAGE="https://stenobano.com/android/android_dictionary/admin/support_image/customer-support.png";
                }

                adapter.notifyDataSetChanged();

            }

            @Override
            public void onFailure(Call<ModelCategory> call, Throwable t) {
                Log.d("erere",t.toString());
                processingDialog.dismiss();
            }
        });
    }

    private void getAds()
    {
        RequestBody key = RequestBody.create(MultipartBody.FORM, "stenobano");
        Call<ModelAds> call = APIClient.getInstance().get_ads(key);
        call.enqueue(new Callback<ModelAds>() {
            @Override
            public void onResponse(Call<ModelAds> call, retrofit2.Response<ModelAds> response) {
                processingDialog.dismiss();
                Log.d("type122sdddd", "msg" + new Gson().toJson(response.body()));
                ModelAds model=response.body();
                modelListAds=model.getResult();

                if (modelListAds.size()>0 && model.getStatus()==200)
                {
                    LinearLayoutManager linearLayoutManager=new LinearLayoutManager(User_Home.this,LinearLayoutManager.VERTICAL,false);
                    ads_recycler.setLayoutManager(linearLayoutManager);
                    AdsBannerAdapter  adapter=new AdsBannerAdapter(User_Home.this,User_Home.this,modelListAds);
                    ads_recycler.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                }

            }

            @Override
            public void onFailure(Call<ModelAds> call, Throwable t) {
                Log.d("erere",t.toString());

            }
        });
    }

    private void getToken()
    {

        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            // Log.w("sucesssss", "getInstanceId failed", task.getException());
                            return;
                        }
                        // Get new Instance ID token
                        String token2 = task.getResult().getToken();

                        // Log and toast
                        Log.d("tokenis2", token2);
                        new SesssionManager(User_Home.this).updateToken(token2);
                        updateToken(token2);
                    }
                });
    }

    private void updateToken(String token)
    {
        RequestBody user_id = RequestBody.create(MultipartBody.FORM, new SesssionManager(this).userID());
        RequestBody tokens = RequestBody.create(MultipartBody.FORM, token);
        RequestBody type = RequestBody.create(MultipartBody.FORM, "user");
        Call<JsonObject> call = APIClient.getInstance().update_token(user_id,tokens,type);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, retrofit2.Response<JsonObject> response) {
                Log.d("tokenuopdate", new Gson().toJson(response.body()));
                try {
                    JSONObject jsonObject=new JSONObject(new Gson().toJson(response.body()));
                    if (jsonObject.getInt("status")==200)
                    {
                        new SesssionManager(User_Home.this).updateToken(token);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

            }
        });


    }
    private void invitefriend()
    {
        try {
            Bitmap b = BitmapFactory.decodeResource(getResources(),R.drawable.stenobano_logo);
            Intent share = new Intent(Intent.ACTION_SEND);
            share.setType("image/jpeg");
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            b.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
            String path = MediaStore.Images.Media.insertImage(getContentResolver(),
                    b, "Share link", "https://play.google.com/store/apps/details?id="+getPackageName());
            Uri imageUri =  Uri.parse(path);
            StringBuilder msg = new StringBuilder();
            String msgs= "नमस्ते/Welcome,  यदि आप Success होना चाहते हैं तो अभी डाउनलोड करे Steno Bano App!\n" +
                    "\n" +
                    "https://play.google.com/store/apps/details?id="+getPackageName()+"\n" +
                    "\n" +
                    "1- PRACTICE ANYWHERE ANY TIME\n" +

                    "2- TIME SAVING\n" +

                    "3- OFFLINE FEATURES ADDED\n" +

                    "4- SPEED TO APP INCREASED AND DECREASED\n" +

                    "5- 3 days free trial\n" +

                    "6- 500 per year charges\n" +
                    "\n" +
                    " Help and support function available 24 hours by nirankarirajender@gmail.com\n" +
                    "\n" +
                    "https://play.google.com/store/apps/details?id="+getPackageName()+"\n";
            msg.append(msgs);
            msg.append("\n");
            //  msg.append("https://play.google.com/store/apps/details?id="+context.getPackageName()); //example :com.package.name

            share.putExtra(android.content.Intent.EXTRA_TEXT, msg.toString());
            share.putExtra(Intent.EXTRA_STREAM, imageUri);
            startActivity(Intent.createChooser(share, "Choose one"));
        } catch(Exception e) {
            //e.toString();
        }


    }

    private void screenwidth_and_Heights()
    {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        Constant.screen_width = displayMetrics.widthPixels;
        Constant.screen_height = displayMetrics.heightPixels;
    }

}
