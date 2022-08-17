package abhi.example.hp.stenobano.user;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.provider.Settings;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.Calendar;
import java.util.HashMap;
import java.util.*;

import abhi.example.hp.stenobano.Dashboard.User_Home;
import abhi.example.hp.stenobano.auth.Login;
import abhi.example.hp.stenobano.config.Base_Url;
import abhi.example.hp.stenobano.R;

   public class Splash_Screen extends AppCompatActivity {
    private Animation animation;
    private ImageView logo;
    Animation anim;
    Context context;
    SharedPreferences sp;
    Calendar calendar;
    int year, month, day;
    String date, valid;
    String type;
    int month_c;
    int day_c;
    int year_C;
    int month_v;
    int day_v;
    int year_v;
    String mobile="";
    AlertDialog alertDialog;
    RequestQueue requestQueue;

    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        Window window = getWindow();
        window.setFormat(PixelFormat.RGBA_8888);
    }

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash__screen);
        sp = getSharedPreferences("steno_info", MODE_PRIVATE);
        valid = sp.getString("valid", null);
        type = sp.getString("type", null);
        mobile = sp.getString("mobile", null);
        requestQueue = Volley.newRequestQueue(this);


        getDeviceUniqueID();
        if (sp.getString("type", null) != null) {
            Check_Expird();
        } else {
            //Toast.makeText(this, "3", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(Splash_Screen.this, Login.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        }

    }

private void Check_Expird()
{
    String url = Base_Url.url+"valid_user.php";
    StringRequest stringRequest=new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {
          //  Toast.makeText(Splash_Screen.this, "respons is "+response, Toast.LENGTH_SHORT).show();
                if (response.equals("valid"))
                {

                    Intent intent =new Intent(Splash_Screen.this, User_Home.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                    //alertDialog.dismiss();
                }
                else
                    {
                        Intent intent =new Intent(Splash_Screen.this, Login.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                        //alertDialog.dismiss();
                }


        }
    }, new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            Toast.makeText(Splash_Screen.this, "Please Re-Open app", Toast.LENGTH_SHORT).show();

        }
    }){
        @Override
        protected Map<String, String> getParams() throws AuthFailureError {
            Map<String,String>data=new HashMap<>();
            data.put("valid",valid);
            data.put("mobile",mobile);
            return data;
        }
    };
    stringRequest.setRetryPolicy(new DefaultRetryPolicy(60000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    requestQueue.add(stringRequest);
}

private  void AlertDialogView()
{
    final AlertDialog.Builder builder = new AlertDialog.Builder(this);
    LayoutInflater inflater = getLayoutInflater();
    builder.setCancelable(false);
    final View view = inflater.inflate(R.layout.activity_splash__screen, null);
    builder.setView(view);
    alertDialog=builder.create();
    alertDialog.show();
}


    public void getDeviceUniqueID()
    {
        String device_unique_id=Settings.Secure.getString(this.getContentResolver(),
                Settings.Secure.ANDROID_ID);
        Log.d("dddweff",device_unique_id);

    }






}


