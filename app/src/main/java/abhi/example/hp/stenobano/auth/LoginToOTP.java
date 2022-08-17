package abhi.example.hp.stenobano.auth;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import abhi.example.hp.stenobano.R;
import abhi.example.hp.stenobano.config.Base_Url;
import abhi.example.hp.stenobano.session.SesssionManager;
import abhi.example.hp.stenobano.Dashboard.User_Home;

public class LoginToOTP extends AppCompatActivity implements View.OnClickListener {

    private EditText otpedit;
    private Button verify;
    private RequestQueue requestQueue;
    private ProgressDialog progressDialog;
   private SharedPreferences sp;
    private Intent intent;
    private String email,mobile,type;
   private String otptext="",userDetails="";
    private TextView resend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_to_otp);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        resend=findViewById(R.id.resend);
        resend.setOnClickListener(this);

        otpedit=findViewById(R.id.otp);
        verify=findViewById(R.id.verify);
        verify.setOnClickListener(this);
        intent=getIntent();
        otptext=intent.getStringExtra("otp");
        userDetails=intent.getStringExtra("userDetails");

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==android.R.id.home)
        {
            onBackPressed();
            return true;

        }
        return false;
    }

    @Override
    public void onClick(View v) {
        if (v.getId()==R.id.verify)
        {
            if (otptext.equals(otpedit.getText().toString()))
            {
                try {
                    JSONArray js = new JSONArray(userDetails);
                    if(js.length()>0)
                    {
                        JSONObject jb =js.getJSONObject(0);
                        SesssionManager sesssionManager=new SesssionManager(getApplicationContext());
                        sesssionManager.sessionLogin(jb.getString("id"),jb.getString("name"),jb.getString("email"),
                                jb.getString("mobile"), jb.getString("type"),jb.getString("valid"),jb.getString("device_id"),jb.getString("image")
                                ,jb.getString("token"));
                        if (jb.getString("type").equals("admin")) {

                        } else {
                            Intent intent = new Intent(LoginToOTP.this, User_Home.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                            finish();
                        }
                    }
                    else
                    {
                        Toast.makeText(this, "Failed,try again", Toast.LENGTH_SHORT).show();
                    }


                } catch (JSONException e) {

                    e.printStackTrace();
                }


            }
            else
            {
                Toast.makeText(LoginToOTP.this, "Incorrect OTP", Toast.LENGTH_SHORT).show();
            }
        }
        else if (v.getId()==R.id.resend)
        {
            getOTP(intent.getStringExtra("mobile"));
        }
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
               Toast.makeText(LoginToOTP.this, ""+OTP, Toast.LENGTH_SHORT).show();

           } catch (JSONException e) {
               e.printStackTrace();
           }
       }
       else
       {
           Toast.makeText(LoginToOTP.this, "You are not register!!", Toast.LENGTH_SHORT).show();
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

    private void getOTP(String mobile)
    {
      int  otp = (int) (Math.random() * 99999);
       String otp2 = Integer.toString(otp);
        otptext= Integer.toString(otp);
        otp(otp2);
        otp_on_email(otp2,mobile);

    }
}
