package abhi.example.hp.stenobano.user;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Build;
import android.provider.Settings;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
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

import abhi.example.hp.stenobano.R;
import abhi.example.hp.stenobano.config.BaseUrl;
import abhi.example.hp.stenobano.session.SesssionManager;

public class SendRequetToUpdate extends AppCompatActivity implements View.OnClickListener {

    private Button send;
    private Spinner spinner;
    private SesssionManager sesssionManager;
    private RequestQueue requestQueue;
    private String reason="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_requet_to_update);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        send=findViewById(R.id.send);
        send.setOnClickListener(this);
        spinner=findViewById(R.id.spinner);
        sesssionManager=new SesssionManager(this);
        String[] list = getResources().getStringArray(R.array.reason);
        ArrayAdapter<String>adapter=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,list);
        spinner.setAdapter(adapter);
        Log.d("devicename",getDeviceName());
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                reason = parent.getAdapter().getItem(position).toString();
                Log.d("spinnertext",reason);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
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

        if (v.getId()==R.id.send)
        {
            AlertDialog.Builder builder=new AlertDialog.Builder(this);
            builder.setMessage("Do you want to do send request to admin?");
            builder.create();
            builder.setTitle("Steno Bano");
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    sendRequst();
                }
            }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            builder.show();
        }
    }





    private void sendRequst()
    {
        requestQueue= Volley.newRequestQueue(this);
        ProgressDialog progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("wait...");
        progressDialog.show();
     String url= BaseUrl.sendRequest;
        StringRequest stringRequest=new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("responsss",response.toString());
                progressDialog.dismiss();
                Toast.makeText(SendRequetToUpdate.this, ""+response, Toast.LENGTH_SHORT).show();
                onBackPressed();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                AlertDialog.Builder builder=new AlertDialog.Builder(SendRequetToUpdate.this);
                builder.create();
                builder.setTitle("Error!!!");
                builder.setMessage("Please check internet connection");
                builder.setCancelable(false);
                builder.setPositiveButton("Retry", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        sendRequst();
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
                Map<String,String>params=new HashMap<>();
                params.put("user_id",sesssionManager.userID());
                params.put("device_id",getDeviceUniqueID());
                params.put("device_name",getDeviceName());
                params.put("mobile",sesssionManager.userMobile());
                params.put("email",sesssionManager.userEmail());
                params.put("reason",reason);
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                60000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);

    }

    public String getDeviceUniqueID()
    {
        String device_unique_id= Settings.Secure.getString(this.getContentResolver(),
                Settings.Secure.ANDROID_ID);
        Log.d("dddweff",device_unique_id);
        return device_unique_id;

    }


    public static String getDeviceName() {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        if (model.startsWith(manufacturer)) {
            return capitalize(model);
        }
        return capitalize(manufacturer) + " " + model;
    }

    private static String capitalize(String str) {
        if (TextUtils.isEmpty(str)) {
            return str;
        }
        char[] arr = str.toCharArray();
        boolean capitalizeNext = true;
        String phrase = "";
        for (char c : arr) {
            if (capitalizeNext && Character.isLetter(c)) {
                phrase += Character.toUpperCase(c);
                capitalizeNext = false;
                continue;
            } else if (Character.isWhitespace(c)) {
                capitalizeNext = true;
            }
            phrase += c;
        }
        return phrase;
    }

}
