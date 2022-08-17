package abhi.example.hp.stenobano.user;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

import abhi.example.hp.stenobano.auth.Login;
import abhi.example.hp.stenobano.config.Base_Url;
import abhi.example.hp.stenobano.R;

public class Forgot_Passord extends AppCompatActivity {
    ProgressDialog progressDialog;
    RequestQueue requestQueue;
    private EditText mobile,password,re_password,otpEdittxt;
    private Button forget;
    String user_id;
    int otp;
     AlertDialog alertDialog;
    String otp2;
    String o="0000";
    LinearLayout linearLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot__passord);
        mobile=findViewById(R.id.mobile);
        password=findViewById(R.id.password1);
        re_password=findViewById(R.id.re_password1);
        forget=findViewById(R.id.forgot);
        linearLayout=findViewById(R.id.ll);
        requestQueue= Volley.newRequestQueue(this);
        SharedPreferences sp = getSharedPreferences("steno_info", MODE_PRIVATE);
        String name2= sp.getString("name",null);
        String mobile2 =sp.getString("mobile",null);
        user_id=sp.getString("id",null);

        forget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            frogot_password();
            }
        });
    }
    public void frogot_password()
    {
if (!isOnline())
{
  alert_dialog();
}
       else if (mobile.getText().toString().equals("")) {
            Toast.makeText(Forgot_Passord.this, "Enter Register mobile number", Toast.LENGTH_SHORT).show();

        } else if (password.getText().toString().equals("")) {
            Toast.makeText(Forgot_Passord.this, "Enter new Password", Toast.LENGTH_SHORT).show();
        }
        else if (!re_password.getText().toString().equals(password.getText().toString())) {
            Toast.makeText(Forgot_Passord.this, "Confirm password dose not match", Toast.LENGTH_SHORT).show();
        }
        else
        {
            check_number();
        }


    }

    public void check_number() {
        if (!isOnline()) {
            alert_dialog();
        } else {
            progressDialog = ProgressDialog.show(Forgot_Passord.this, "Please wait...", "saving...", true);
            progressDialog.setCancelable(true);
            String url = Base_Url.url+"check_number.php";
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    //Toast.makeText(Forgot_Passord.this, "" + response, Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                    if (response.equals("succes")) {
                        send_otp();
                        alert_dialog();
                    } else if (response.equals("number not found")) {
                        Toast t = Toast.makeText(Forgot_Passord.this, "Email does not exist", Toast.LENGTH_SHORT);
                        t.setGravity(Gravity.CENTER, 0, 0);
                        t.show();
                    } else if (response.equals("account has been expired")) {
                        Toast t = Toast.makeText(Forgot_Passord.this, "Your Account has been expired, Please login again!!", Toast.LENGTH_SHORT);
                        t.setGravity(Gravity.CENTER, 0, 0);
                        t.show();
                    } else {
                        Toast.makeText(Forgot_Passord.this, "Try Again!!!!", Toast.LENGTH_SHORT).show();
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(Forgot_Passord.this, "" + error, Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();

                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {

                    Map<String, String> data = new HashMap<>();
                    //data.put("user_id", user_id);
                    data.put("mobile", mobile.getText().toString());

                    return data;
                }
            };
            //stringRequest.setRetryPolicy(new DefaultRetryPolicy(60000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            // requestQueue.add(stringRequest);
            requestQueue.add(stringRequest);

        }
    }

    public  void alert_dialog()
    {
        linearLayout.setVisibility(View.INVISIBLE);
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        builder.setCancelable(false);
        final View view = inflater.inflate(R.layout.alert_otp_forgot, null);
        builder.setView(view);
        otpEdittxt = view.findViewById(R.id.otp);
        Button button=view.findViewById(R.id.btn_dialog);
        TextView textView=view.findViewById(R.id.cancel);
        TextView resend=view.findViewById(R.id.re_send);
        alertDialog = builder.create();
       // alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.rgb(0,150,0)));
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (otpEdittxt.getText().toString().equals(otp2))
                {
                   new_create_password();
                   // alertDialog.dismiss();
                }
                else
                {
                    Toast.makeText(Forgot_Passord.this, "Incorrect OTP", Toast.LENGTH_SHORT).show();
                }
                //alertDialog.dismiss();
            }
        });
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        resend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                send_otp();
            }
        });
        alertDialog.show();

    }

    public void send_otp()
    {
        otp = (int) (Math.random() * 99999);
       otp2 = Integer.toString(otp);
       otp_on_email(otp);
      String url="http://sms4power.com/api/swsend.asp?username=t1steno&password=74620606&sender=STENOB&sendto="+mobile.getText().toString()+"&message="+"Your OTP is "+" "+otp2;
        //String url = "http://www.samveda.co.in/android/android_dictionary/check_number.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Forgot_Passord.this, "" + error, Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();

            }
        })

        ;
        //stringRequest.setRetryPolicy(new DefaultRetryPolicy(60000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // requestQueue.add(stringRequest);
        requestQueue.add(stringRequest);

    }




    public void new_create_password()
    {
        String url =  Base_Url.url+"forgot_password.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                           // Toast.makeText(Forgot_Passord.this, "respons is "+response, Toast.LENGTH_SHORT).show();
            if (response.equals("Password chnaged"))
            {
                Toast t=  Toast.makeText(Forgot_Passord.this, "Your new Password Updated", Toast.LENGTH_SHORT);
                t.setGravity(Gravity.CENTER,0,0);
                t.show();
                Intent intent=new Intent(Forgot_Passord.this, Login.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
                alertDialog.dismiss();
            }
            else
            {
                Toast t=  Toast.makeText(Forgot_Passord.this, "Faild", Toast.LENGTH_SHORT);
                t.setGravity(Gravity.CENTER,0,0);
                t.show();
            }

                        }
                    }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Forgot_Passord.this, "" + error, Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> data = new HashMap<>();
                data.put("mobile", mobile.getText().toString());
                 data.put("new_password",password.getText().toString());
                return data;

            }
        };
        //stringRequest.setRetryPolicy(new DefaultRetryPolicy(60000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // requestQueue.add(stringRequest);
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


    public void otp_on_email(int otp) {

        String url =  Base_Url.url+"otp.php";


        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Toast.makeText(getApplicationContext(), ""+response, Toast.LENGTH_SHORT).show();


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

                data.put("otp",String.valueOf(otp));
                data.put("email", mobile.getText().toString());
                // data.put("image_url",Integer.toString(image));
                return data;
            }
        };

        requestQueue.add(stringRequest);

    }

    }


