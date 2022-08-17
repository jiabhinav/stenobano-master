package abhi.example.hp.stenobano.auth;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Settings;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
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

import java.util.HashMap;
import java.util.Map;

import abhi.example.hp.stenobano.config.Base_Url;
import abhi.example.hp.stenobano.R;

public class Registration extends AppCompatActivity {

    private EditText name, email_add, mobile, password, re_password, otpEdittxt;
    TextView already_member,member;

    RequestQueue requestQueue;
    ProgressDialog progressDialog;
    int otp;
    String otp2;
    String o="0000";
    LinearLayout linearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_page);
        showKeyboard();
        name = findViewById(R.id.name);
        email_add = findViewById(R.id.email);
        mobile = findViewById(R.id.contact_number);
        password = findViewById(R.id.password);
        re_password = findViewById(R.id.re_password);
        already_member = findViewById(R.id.already_member);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        already_member.setPaintFlags(already_member.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        member = findViewById(R.id.register);
        linearLayout=findViewById(R.id.ll);
        requestQueue = Volley.newRequestQueue(this);
        member.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register();
            }
        });
        already_member.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Registration.this, Login.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
            }
        });
    }

    private void register() {
        if (!isOnline())
        {
            alert_online();
        }
       else if (name.getText().toString().equals("")) {
            Toast.makeText(this, "Enter name", Toast.LENGTH_LONG).show();
        } else if (email_add.getText().toString().equals("")) {
            Toast.makeText(this, "Enter email", Toast.LENGTH_LONG).show();
        } else if (mobile.getText().toString().equals("")) {
            Toast.makeText(this, "Please Enter 10 Digit mobile  Number without country code", Toast.LENGTH_LONG).show();
        }
        else  if (mobile.getText().length()!=10)
        {
            Toast.makeText(this, "Please Enter 10 Digit mobile  Number without country code", Toast.LENGTH_LONG).show();
        }

        else if (password.getText().toString().equals("")) {
            Toast.makeText(this, "Enter password", Toast.LENGTH_LONG).show();
        } else if (!re_password.getText().toString().equals(password.getText().toString())) {
            Toast.makeText(this, "Re-password does not match", Toast.LENGTH_LONG).show();
        } else {
            check_email_number();

        }
    }

    public  void check_email_number()
    {
        progressDialog =ProgressDialog.show(Registration.this,"Please wait...","Registring...",true);
        progressDialog.setCancelable(false);
            String url = Base_Url.url+"after_registeration_check.php";
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                   // Toast.makeText(Registration.this, ""+response, Toast.LENGTH_SHORT).show();
                    if (response.equals("succes")) {

                       //alert_dialog();

                        progressDialog.dismiss();
                        int  otp = (int) (Math.random() * 99999);
                        String otp2 = Integer.toString(otp);
                        String otptext= String.valueOf(otp);
                        otp(otp2);
                        otp_on_email(otp2);

                        Intent intent=new Intent(Registration.this, OTP_Page.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.putExtra("name",name.getText().toString());
                        intent.putExtra("password",password.getText().toString());
                        intent.putExtra("mobile",mobile.getText().toString());
                        intent.putExtra("email",email_add.getText().toString());
                        intent.putExtra("otp",otp2);
                        startActivity(intent);


                    } else {
                        Toast.makeText(Registration.this, "User alreday Exist!!!", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(Registration.this, "Try again", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();


                }
            }) {

                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> data = new HashMap<>();
                    data.put("mobile", mobile.getText().toString());
                    data.put("email", email_add.getText().toString());
                    // data.put("image_url",Integer.toString(image));
                    return data;
                }
            };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                60000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            requestQueue.add(stringRequest);

        }


    public void register_user() {
        if (!isOnline()) {
            alert_online();
        } else {

            String url = Base_Url.url+"register.php";
            progressDialog = ProgressDialog.show(Registration.this, "Please wait...", "Registring...", true);
            progressDialog.setCancelable(false);
            //SharedPreferences sp = getSharedPreferences("samveda_data", Context.MODE_PRIVATE);
            //final String user_id = sp.getString("id", null);
            // final String email = sp.getString("email", null);
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                     //Toast.makeText(Registration.this, ""+response, Toast.LENGTH_SHORT).show();
                    if (response.equals("registeredsuccessful")) {
                        Toast.makeText(Registration.this, "Registered successful", Toast.LENGTH_SHORT).show();
                        name.setText("");
                        email_add.setText("");
                        password.setText("");
                        re_password.setText("");
                        mobile.setText("");
                        Intent intent = new Intent(Registration.this, Login.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                        progressDialog.dismiss();
                    } else {
                        Toast.makeText(Registration.this, "Registration Failed.!!!", Toast.LENGTH_SHORT).show();
                        Toast.makeText(Registration.this, "Mobile number or Email alreadt exist", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(Registration.this, ""+error, Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();

                }
            }) {

                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> data = new HashMap<>();

                    data.put("name", name.getText().toString());
                    data.put("password", password.getText().toString());
                    data.put("mobile", mobile.getText().toString());
                    data.put("email", email_add.getText().toString());
                    data.put("device_id", getDeviceUniqueID());
                    // data.put("image_url",Integer.toString(image));
                    return data;
                }
            };
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                    60000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            requestQueue.add(stringRequest);

        }
    }


    public String getDeviceUniqueID()
    {
        String device_unique_id= Settings.Secure.getString(this.getContentResolver(),
                Settings.Secure.ANDROID_ID);
        Log.d("dddweff",device_unique_id);
        return device_unique_id;

    }

    public  void otp(String otp2)
    {

        requestQueue=Volley.newRequestQueue(this);
        // Toast t=  Toast.makeText(this, "Please wait..OTP sending on your entered mobile And email ", Toast.LENGTH_SHORT);
        //  t.setGravity(Gravity.CENTER,0,0);
        // t.show();
        String url="http://sms4power.com/api/swsend.asp?username=t1steno&password=74620606&sender=STENOB&sendto="+mobile.getText().toString()+"&message="+"Your Registration  OTP is "+" "+otp2;
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

    protected boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        } else {
            return false;
        }
    }
    public void alert_online() {
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
        }




    public void otp_on_email(String otp2) {
        requestQueue= Volley.newRequestQueue(this);
        String url = Base_Url.url+"otp.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Toast.makeText(Registration.this, ""+response, Toast.LENGTH_SHORT).show();


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

                data.put("email",email_add.getText().toString());
                // data.put("image_url",Integer.toString(image));
                return data;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                60000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);

    }

    public void showKeyboard(){
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
    }

    }


