package abhi.example.hp.stenobano.auth;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Paint;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.security.ProviderInstaller;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import abhi.example.hp.stenobano.config.Base_Url;
import abhi.example.hp.stenobano.Interface.VolleyCallback;

import abhi.example.hp.stenobano.gatway.PaytmGatway;
import abhi.example.hp.stenobano.R;
import abhi.example.hp.stenobano.session.SesssionManager;
import abhi.example.hp.stenobano.user.Forgot_Passord;
import abhi.example.hp.stenobano.user.Help;
import abhi.example.hp.stenobano.Dashboard.User_Home;
import abhi.example.hp.stenobano.user.PlayAudioImage;

import static com.android.volley.DefaultRetryPolicy.DEFAULT_BACKOFF_MULT;


public class Login extends AppCompatActivity implements View.OnClickListener {
//=================================
    private static final String TAG = "Paynimo";
    private     int otp;
    private   String otp2;
    private   JSONObject json_to_be_sent;
    private Intent intent;
    private  String signed_msg;
    private  ProgressDialog progressDialog;
    private  RequestQueue requestQueue;
    private EditText mobile, email, password;
    private Button login, pay;
    private   TextView already, forgot_password, mesg,logout;
    int FONEPAISAPG_RET_CODE = 1;
    private  EditText mo, em, pa;
    private  String invoice="", mobile_no, email_id;
    private  String phoneTypes = "";
    private  String invoice_amt = "00.00";
    private String days="",otptext="", userDetails="";

    private  String user_email="";
    private    ProgressBar logoutProgress;
    private AlertDialog alertDialog;
    private VolleyCallback callback;
 private  TextView help;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_page);
        requestQueue = Volley.newRequestQueue(this);
        email = findViewById(R.id.email);
        mobile = findViewById(R.id.mobile);
        password = findViewById(R.id.password);
        mesg = findViewById(R.id.mesg);
        login = findViewById(R.id.login);
        pay = findViewById(R.id.pay);
        pay.setVisibility(View.GONE);
       // mo = findViewById(R.id.mo);
        //em = findViewById(R.id.em);
        //pa = findViewById(R.id.pa);
        logout=findViewById(R.id.logout);
        logout.setOnClickListener(this);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        already = findViewById(R.id.already_member);
        forgot_password = findViewById(R.id.forget_password);
       // em.setVisibility(View.GONE);
        already.setPaintFlags(already.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        SharedPreferences sp = getSharedPreferences("steno_info", MODE_PRIVATE);
        email_id = sp.getString("email", null);
        mobile_no = sp.getString("mobile", null);
        getMyPhoneNumber();
        help=findViewById(R.id.help);


        if (ContextCompat.checkSelfPermission(Login.this, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(Login.this, new String[]{Manifest.permission.READ_SMS, Manifest.permission.RECEIVE_SMS}, 101);
        }


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
               // payment_gatway();

            }
        });

        help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
     Intent intent=new Intent(getApplicationContext(), Help.class);
     intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
     startActivity(intent);
            }
        });

        already.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Login.this, Registration.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
            }
        });

        forgot_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Login.this, Forgot_Passord.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
            }
        });
        pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (email.getText().toString().equals("")) {
                    Toast.makeText(Login.this, "Enter Email no.", Toast.LENGTH_SHORT).show();
                } else if (mobile.getText().toString().equals(" ")) {
                    Toast.makeText(Login.this, "Enter mobile id", Toast.LENGTH_SHORT).show();
                } else {
                    //payment_gatway();
                    getString();
                }
            }
        });
    }

    private void login() {

        if (!isOnline()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("No internet Connection");
            builder.setMessage("Please turn on internet connection to continue");
            builder.setNegativeButton("close", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();

                }
            });
            AlertDialog alertDialog = builder.create();
            alertDialog.setCancelable(false);
            alertDialog.show();
        } else {

            if (mobile.getText().toString().equals("")) {
                Toast.makeText(this, "Enter mobile no.", Toast.LENGTH_SHORT).show();
            } else if (password.getText().toString().equals(" ")) {
                Toast.makeText(this, "Enter password", Toast.LENGTH_SHORT).show();
            } else {
                String url = Base_Url.url+"login_check.php";
                progressDialog = ProgressDialog.show(Login.this, "Please wait...", "Login...", true);
                progressDialog.setCancelable(false);

                StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        Log.d("loginrespons",response.toString());
                       // Toast.makeText(Login.this, ""+response, Toast.LENGTH_SHORT).show();
                         if(response.equals("Incorrect login Id"))
                         {
                             Toast.makeText(Login.this, ""+response, Toast.LENGTH_SHORT).show();
                         }
                         else if (response.equals("expired"))
                         {
                             //alertExpireUser();
                             getRenewalAmount();
                         }
                         else if (response.equals("exist"))
                         {
                            Toast t= Toast.makeText(Login.this, "Incorrect login Id", Toast.LENGTH_LONG);
                            t.setGravity(Gravity.CENTER,0,0);
                            t.show();
                           /*  int  otp = (int) (Math.random() * 99999);
                             String otp2 = Integer.toString(otp);
                             otptext= Integer.toString(otp);
                             otp_on_email(otp2,mobile.getText().toString());
                             otp(otp2);*/

                         }
                         else
                         {
                             try {
                                 JSONArray js = new JSONArray(response);
                                 JSONObject jb = (JSONObject)js.get(0);
                                 SesssionManager sesssionManager=new SesssionManager(getApplicationContext());
                                 sesssionManager.sessionLogin(jb.getString("id"),jb.getString("name"),jb.getString("email"),
                                         jb.getString("mobile"), jb.getString("type"),jb.getString("valid"),jb.getString("device_id"),
                                         jb.getString("image"),jb.getString("token"));
                                       new SesssionManager(Login.this).setPlay("next");
                                     Intent intent = new Intent(Login.this, User_Home.class);
                                     intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                     startActivity(intent);
                                     finish();


                             } catch (JSONException e) {
                                 Log.d("ererere",e.toString());

                                 e.printStackTrace();
                             }
                         }


                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("loginerroeis", "onErrorResponse: "+error.getMessage());
                        progressDialog.dismiss();
                    }
                }) {

                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> data = new HashMap<>();
                        data.put("email", mobile.getText().toString());
                        data.put("password", password.getText().toString());
                        data.put("device_id",getDeviceUniqueID());
                        Log.d("mapisi",data.toString());
                        return data;
                    }
                };

                stringRequest.setRetryPolicy(new DefaultRetryPolicy(60000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DEFAULT_BACKOFF_MULT));
                requestQueue.add(stringRequest);


            }
        }
    }

    public String getDeviceUniqueID()
    {
        String device_unique_id= Settings.Secure.getString(this.getContentResolver(),
                Settings.Secure.ANDROID_ID);
        Log.d("dddweff",device_unique_id);
        return device_unique_id;

    }




    public void otp_on_email(String otp2,String mobile) {
        String url = Base_Url.url+"login_OTP.php";
        requestQueue=Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                //Toast.makeText(LoginToOTP.this, "respons is "+response, Toast.LENGTH_SHORT).show();
                if (!response.equals("[null]"))
                {
                    try {
                        JSONObject jsonObject=new JSONObject(response);
                        userDetails=jsonObject.getString("details");

                        String OTP=jsonObject.getString("OTP");
                        Toast.makeText(Login.this, ""+OTP, Toast.LENGTH_SHORT).show();

                        Intent intent=new Intent(Login.this, LoginToOTP.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.putExtra("mobile",mobile);
                        intent.putExtra("otp",otp2);
                        intent.putExtra("userDetails",userDetails);
                        startActivity(intent);


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                else
                {
                    Toast.makeText(Login.this, "You are not register!!", Toast.LENGTH_SHORT).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {



            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> data = new HashMap<>();

                data.put("otp",otp2);

                data.put("mobile",mobile);
                // data.put("image_url",Integer.toString(image));
                return data;
            }
        };

        requestQueue.add(stringRequest);
    }



    private void getRenewalAmount()
    {
        requestQueue=Volley.newRequestQueue(this);
        String url= Base_Url.url+"getRenewalAmount.php";
        ProgressDialog progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Please wait....");
        progressDialog.setCancelable(false);
        progressDialog.show();
       StringRequest stringRequest=new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
           @Override
           public void onResponse(String response) {
               if (!response.equals("[null]"))
               {
                   JSONArray jsonArray=null;
                   try {
                       jsonArray=new JSONArray(response);
                       JSONObject jsonObject=jsonArray.getJSONObject(0);
                       String amount=jsonObject.getString("amount");
                        days=jsonObject.getString("valid");

                       DecimalFormat format = new DecimalFormat("0.00");
                       invoice_amt=String.valueOf(format.format(Integer.parseInt(amount)));
                       alertExpireUser(days,amount);
                       progressDialog.dismiss();
                   } catch (JSONException e) {
                       e.printStackTrace();
                   }
               }
               else
               {
                   Toast.makeText(Login.this, "Failed,try again!!", Toast.LENGTH_SHORT).show();
                   progressDialog.dismiss();
               }
           }
       }, new Response.ErrorListener() {
           @Override
           public void onErrorResponse(VolleyError error) {
               Toast.makeText(Login.this, "Check Internet Connection or May be server error!!", Toast.LENGTH_SHORT).show();
               progressDialog.dismiss();
           }
       });
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(60000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);

    }

    private  void alertExpireUser(String days,String amount)
    {
        Toast.makeText(Login.this, "Your Account has been Expired", Toast.LENGTH_SHORT).show();

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Login.this, R.style.Theme_AppCompat_DayNight_Dialog_Alert);
        alertDialogBuilder.setTitle("Your account has been expired");
        alertDialogBuilder.setMessage("Do you want to renewal?");
        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setPositiveButton("Countinue", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //  Toast.makeText(Login.this, "OK", Toast.LENGTH_SHORT).show();
                pay.setVisibility(View.VISIBLE);
                mesg.setText("Rs"+amount+" "+"pay for"+" "+days+" "+"days");
              //  pa.setVisibility(View.GONE);
                password.setVisibility(View.GONE);
                login.setVisibility(View.GONE);
                email.setVisibility(View.VISIBLE);
               // em.setVisibility(View.VISIBLE);
            }
        });
        alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        alertDialogBuilder.show();

    }


    protected boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        } else {
            return false;
        }
    }



    private void payment_gatway()
    {


        String  invoice = "STB"+String.valueOf((Math.random() * 99999000));
        Intent intent = new Intent(Login.this, PaytmGatway.class);
        intent.putExtra("orderid", invoice);
        intent.putExtra("custid",mobile.getText().toString());
        intent.putExtra("day", days);
        intent.putExtra("email", email.getText().toString());
        intent.putExtra("amount", invoice_amt);
        startActivity(intent);
        overridePendingTransition(0,0);


    }






    private void update_status() {
        invoice = "STB"+String.valueOf((Math.random() * 99999000));
       // Toast.makeText(this, "" + invoice + " " + invoice_amt + " " + email_id + " " + mobile_no, Toast.LENGTH_SHORT).show();
        String url = Base_Url.url+"update_status.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(Login.this, "" + response, Toast.LENGTH_SHORT).show();
                mobile.setVisibility(View.VISIBLE);
                password.setVisibility(View.VISIBLE);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> data = new HashMap<>();
                data.put("invoice", invoice);
                data.put("amount", invoice_amt);
                data.put("email", email.getText().toString());
                data.put("mobile", mobile.getText().toString());
                data.put("days", days);
                return data;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(60000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }


    private void getMyPhoneNumber() {

        int permission_check = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE);
        if (permission_check == PackageManager.PERMISSION_GRANTED) {
            //myTeliphoneManager();
        } else {
            ActivityCompat.requestPermissions(Login.this, new String[]{Manifest.permission.READ_PHONE_STATE}, 1);
        }


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1: {
                if (grantResults.length >= 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                } else {
                   // Toast.makeText(this, "You dont have required permission to make action", Toast.LENGTH_SHORT).show();
                }

            }
        }
    }



    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onClick(View v) {
        if (v.getId()==R.id.logout)
        {
            alertDialog();

        }

    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void alertDialog()

    {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
// ...Irrelevant code for customizing the buttons and title
        dialogBuilder.setCancelable(false);
        dialogBuilder.setTitle("Logout");
        dialogBuilder.setMessage("NOTE:- OTP will we send on your registered Email ID.");
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.alert_logout, null);
        EditText useremail=dialogView.findViewById(R.id.phone);
   TextView ok=dialogView.findViewById(R.id.ok);
     TextView cancel=dialogView.findViewById(R.id.cancel);
     TextView verify=dialogView.findViewById(R.id.verify);
        TextView text=dialogView.findViewById(R.id.text);
        EditText otp=dialogView.findViewById(R.id.otp);
        logoutProgress=dialogView.findViewById(R.id.progress);

        dialogBuilder.setView(dialogView);
        alertDialog=dialogBuilder.create();
        //alertDialog.create();
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (useremail.getText().equals(""))
                {
                    Toast.makeText(Login.this, "Enter Register email id", Toast.LENGTH_SHORT).show();
                }
                else {

                    useremail.setVisibility(View.GONE);
                    otp.setVisibility(View.VISIBLE);
                    text.setVisibility(View.VISIBLE);
                    logout(useremail.getText().toString());
                    ok.setVisibility(View.GONE);
                    verify.setVisibility(View.VISIBLE);
                }

            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!otp.getText().toString().equals(String.valueOf(otp2)))
                {
                    Toast.makeText(Login.this, "OTP is Incorrect!!", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    finally_logout(useremail.getText().toString());
                }

            }
        });

               alertDialog.show();
            }





    private void  logout(String email)
    {
        logoutProgress.setVisibility(View.VISIBLE);
        //Toast.makeText(this, ""+email+otp2, Toast.LENGTH_SHORT).show();
       String url=Base_Url.url+"logout_user.php";
        StringRequest stringRequest=new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                if (!response.equals("not exist"))
                {
                    Toast.makeText(Login.this, ""+response, Toast.LENGTH_SHORT).show();
                    logoutProgress.setVisibility(View.GONE);
                }
                else
                {
                    Toast.makeText(Login.this, "User does not exist!!!!", Toast.LENGTH_SHORT).show();
                    logoutProgress.setVisibility(View.GONE);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                logoutProgress.setVisibility(View.GONE);

            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String>data=new HashMap<>();
                data.put("email",email);
                data.put("otp",String.valueOf(otp2));

                return data;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(60000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);


    }


    private void  finally_logout(String email)
    {
        logoutProgress.setVisibility(View.VISIBLE);
        String url=Base_Url.url+"finally_logout.php";
        StringRequest stringRequest=new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                if (response.equals("Logout"))
                {
                    logoutProgress.setVisibility(View.GONE);
                    Toast.makeText(Login.this, ""+response, Toast.LENGTH_SHORT).show();
                    alertDialog.dismiss();
                    logout.setVisibility(View.GONE);

                }
                else
                {
                    logoutProgress.setVisibility(View.GONE);
                    Toast.makeText(Login.this, ""+response, Toast.LENGTH_SHORT).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                logoutProgress.setVisibility(View.GONE);

            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String>data=new HashMap<>();
                data.put("email",email);
                return data;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(60000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);


    }


    public void getString()
    {
        invoice = "STB"+String.valueOf((Math.random() * 99999000));
        progressDialog = ProgressDialog.show(Login.this, "Please wait...", "Taking time...", true);
        progressDialog.setCancelable(false);
        String url=Base_Url.url+"sign_gatway.php";
        StringRequest stringRequest=new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d("responsi",response.toString());
               // Toast.makeText(Login.this, "respon is "+response, Toast.LENGTH_SHORT).show();
               // callback.onSuccess(response);
                if (!response.equals(""))
                {
                    signed_msg = response;
                    progressDialog.dismiss();
                    payment_gatway();
                }
                else
                {
                    progressDialog.dismiss();
                    Toast.makeText(Login.this, "Please check Internet Connection!!!!!", Toast.LENGTH_SHORT).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String>data=new HashMap<>();
                data.put("amt",invoice_amt);
                data.put("invoice",invoice);

                return data;
            }
        };
        requestQueue.add(stringRequest);

    }

    public  void otp(String otp2)
    {

        Toast t=  Toast.makeText(this, "Please wait..OTP sending on your entered mobile OR  email ", Toast.LENGTH_SHORT);
        t.setGravity(Gravity.CENTER,0,0);
        t.show();
        requestQueue=Volley.newRequestQueue(this);
        String url="http://sms4power.com/api/swsend.asp?username=t1steno&password=74620606&sender=STENOB&sendto="+mobile+"&message="+"Your Registration  OTP is "+" "+otp2;
        //  String url = "http://www.samveda.co.in/android/android_dictionary/otp.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {


            }


        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {



            }
        });

        requestQueue.add(stringRequest);
    }

    public void otp_on_email() {

        otp = (int) (Math.random() * 99999);
        otp2 = Integer.toString(otp);
        String url = Base_Url.url+"otps.php";

        SharedPreferences sp = getSharedPreferences("steno_info", MODE_PRIVATE);

       String email = sp.getString("email", null);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Toast.makeText(Login.this, ""+response, Toast.LENGTH_SHORT).show();


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();


            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> data = new HashMap<>();

                data.put("otp",otp2);

                data.put("email",  sp.getString("email", null));
                // data.put("image_url",Integer.toString(image));
                return data;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(60000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);

    }

    private void sendFCMOTP()
    {
         PhoneAuthProvider.getInstance().verifyPhoneNumber(
                "+917210309464",                     // Phone number to verify
                60,                           // Timeout duration
                TimeUnit.SECONDS,                // Unit of timeout
                Login.this,        // Activity (for callback binding)
                mCallback);
    };

    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallback = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(PhoneAuthCredential credential) {
            // This callback will be invoked in two situations:
            // 1 - Instant verification. In some cases the phone number can be instantly
            //     verified without needing to send or enter a verification code.
            // 2 - Auto-retrieval. On some devices Google Play services can automatically
            //     detect the incoming verification SMS and perform verification without
            //     user action.
            Log.d(TAG, "onVerificationCompleted:" + credential);

            //signInWithPhoneAuthCredential(credential);
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            // This callback is invoked in an invalid request for verification is made,
            // for instance if the the phone number format is not valid.
            Log.w(TAG, "onVerificationFailed", e);

            if (e instanceof FirebaseAuthInvalidCredentialsException) {
                // Invalid request
                // ...
            } else if (e instanceof FirebaseTooManyRequestsException) {
                // The SMS quota for the project has been exceeded
                // ...
            }

            //Show a message and update the UI
            //...
        }

        @Override
        public void onCodeSent(@NonNull String verificationId,
                @NonNull PhoneAuthProvider.ForceResendingToken token) {
            // The SMS verification code has been sent to the provided phone number, we
            // now need to ask the user to enter the code and then construct a credential
            // by combining the code with a verification ID.
            Log.d(TAG, "onCodeSent:" + verificationId);

            // Save verification ID and resending token so we can use them later
            //mVerificationId = verificationId;
            // mResendToken = token;
            //...
        }
    };

    private void updateAndroidSecurityProvider()
    {
        try
    {
        ProviderInstaller.installIfNeeded(this);
    }
    catch (Exception e)
    {
        e.getMessage();
    }
    }

}


