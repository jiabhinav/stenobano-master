package abhi.example.hp.stenobano.auth;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings;
import androidx.core.app.ActivityCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.apache.http.conn.ConnectTimeoutException;
import org.json.JSONException;
import org.xmlpull.v1.XmlPullParserException;

import java.net.ConnectException;
import java.net.MalformedURLException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.HashMap;
import java.util.Map;

import abhi.example.hp.stenobano.config.Base_Url;
import abhi.example.hp.stenobano.R;

public class OTP_Page extends AppCompatActivity implements View.OnClickListener {

    private static String otptext="";
private EditText otpedit;
private Button verify;
private RequestQueue requestQueue;
private ProgressDialog progressDialog;
private Intent intent;
private TextView resend;
private ImageView back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_otp_page);
        otpedit=findViewById(R.id.otp);
        verify=findViewById(R.id.verify);
        verify.setOnClickListener(this);
        resend=findViewById(R.id.resend);
        resend.setOnClickListener(this);
        intent=getIntent();
        otptext=intent.getStringExtra("otp");
        back=findViewById(R.id.back);
        back.setOnClickListener(this);
        //getOTP();

    }




    @Override
    public void onClick(View v) {
            if (v.getId()==R.id.verify)
            {
                if (otptext.equals(otpedit.getText().toString()))
                {
                    register_user();

                }
                else
                {
                    Toast.makeText(OTP_Page.this, "Incorrect OTP", Toast.LENGTH_SHORT).show();
                }
            }
            else if (v.getId()==R.id.back)
            {
                onBackPressed();
            }

            else if (v.getId()==R.id.resend)
            {
              getOTP();
            }
                }
                public  void otp(String otp2)
                {

                    requestQueue=Volley.newRequestQueue(this);
                   // Toast t=  Toast.makeText(this, "Please wait..OTP sending on your entered mobile And email ", Toast.LENGTH_SHORT);
                  //  t.setGravity(Gravity.CENTER,0,0);
                   // t.show();
                    String url="http://sms4power.com/api/swsend.asp?username=t1steno&password=74620606&sender=STENOB&sendto="+intent.getStringExtra("mobile")+"&message="+"Your Registration  OTP is "+" "+otp2;
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

    public void otp_on_email(String otp2) {
        requestQueue= Volley.newRequestQueue(this);
        String url = Base_Url.url+"otp.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Toast.makeText(OTP_Page.this, ""+response, Toast.LENGTH_SHORT).show();


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

                data.put("email", intent.getStringExtra("email"));
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
         requestQueue= Volley.newRequestQueue(this);
        String url = Base_Url.url+"register_user.php";
        progressDialog = ProgressDialog.show(OTP_Page.this, "Please wait...", "Registring...", true);
        progressDialog.setCancelable(false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

               // Toast.makeText(OTP_Page.this, ""+response, Toast.LENGTH_SHORT).show();
                if (response.equals("registeredsuccessful")) {
                    Toast.makeText(OTP_Page.this, "Registered successful", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(OTP_Page.this, Login.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    progressDialog.dismiss();
                    finish();

                } else {
                    //Toast.makeText(OTP_Page.this, "Registration Failed.!!!", Toast.LENGTH_SHORT).show();
                    Toast.makeText(OTP_Page.this, "Mobile number or Email alreadt exist", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(OTP_Page.this, "Failed,try again!!", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
                check_Error(error);

            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> data = new HashMap<>();
                data.put("name",intent.getStringExtra("name"));
                data.put("password", intent.getStringExtra("password"));
                data.put("mobile",intent.getStringExtra("mobile"));
                data.put("email", intent.getStringExtra("email"));
                data.put("device_id",getDeviceUniqueID());
                return data;
            }

        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(60000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);

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
        TelephonyManager tm = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
        /*if (null != tm) {
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


    private void getOTP()
    {
        int  otp = (int) (Math.random() * 99999);
        String otp2 = Integer.toString(otp);
        otptext=Integer.toString(otp);
        otp(otp2);
        otp_on_email(otp2);

    }


    private  void check_Error(VolleyError error) {
        try {

            if (error instanceof NoConnectionError) {
                ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo activeNetwork = null;
                if (cm != null) {
                    activeNetwork = cm.getActiveNetworkInfo();
                }
                if (activeNetwork != null && activeNetwork.isConnectedOrConnecting()) {
                    Toast.makeText(getApplicationContext(), "Server is not connected to internet.",
                            Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Your device is not connected to internet.",
                            Toast.LENGTH_SHORT).show();
                }
            } else if (error instanceof NetworkError || error.getCause() instanceof ConnectException
                    || (error.getCause().getMessage() != null
                    && error.getCause().getMessage().contains("connection"))) {
                Toast.makeText(getApplicationContext(), "Your device is not connected to internet.",
                        Toast.LENGTH_SHORT).show();
            } else if (error.getCause() instanceof MalformedURLException) {
                Toast.makeText(getApplicationContext(), "Bad Request.", Toast.LENGTH_SHORT).show();
            } else if (error instanceof ParseError || error.getCause() instanceof IllegalStateException
                    || error.getCause() instanceof JSONException
                    || error.getCause() instanceof XmlPullParserException) {
                Toast.makeText(getApplicationContext(), "Parse Error (because of invalid json or xml).",
                        Toast.LENGTH_SHORT).show();
            } else if (error.getCause() instanceof OutOfMemoryError) {
                Toast.makeText(getApplicationContext(), "Out Of Memory Error.", Toast.LENGTH_SHORT).show();
            } else if (error instanceof AuthFailureError) {
                Toast.makeText(getApplicationContext(), "server couldn't find the authenticated request.",
                        Toast.LENGTH_SHORT).show();
            } else if (error instanceof ServerError || error.getCause() instanceof ServerError) {
                Toast.makeText(getApplicationContext(), "Server is not responding.", Toast.LENGTH_SHORT).show();
            } else if (error instanceof TimeoutError || error.getCause() instanceof SocketTimeoutException
                    || error.getCause() instanceof ConnectTimeoutException
                    || error.getCause() instanceof SocketException
                    || (error.getCause().getMessage() != null
                    && error.getCause().getMessage().contains("Connection timed out"))) {
                Toast.makeText(getApplicationContext(), "Connection timeout error",
                        Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getApplicationContext(), "An unknown error occurred.",
                        Toast.LENGTH_SHORT).show();
            }
        }
        catch (NullPointerException e)
        {

        }
    }


}
