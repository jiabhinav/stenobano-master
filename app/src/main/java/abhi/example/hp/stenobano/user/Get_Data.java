package abhi.example.hp.stenobano.user;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import androidx.core.app.ActivityCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ProgressBar;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import abhi.example.hp.stenobano.config.Base_Url;
import abhi.example.hp.stenobano.Interface.AlertDialogInterFace;
import abhi.example.hp.stenobano.R;
import abhi.example.hp.stenobano.model.Search_Model;
import abhi.example.hp.stenobano.adapter.Get_Data_Recycler;

public class Get_Data extends AppCompatActivity implements AlertDialogInterFace {
    RequestQueue requestQueue;
   public static AlertDialog alertDialog;
    public static  ProgressDialog progressDialog;
   public static AlertDialog.Builder dialogBuilder;
    public static List<Search_Model> model;
    RecyclerView mRecyclerView;
       EditText search;
    String type_id;
    AlertDialogInterFace dialogInterFace;
    ProgressBar progressBar;
    public static TextView text1,text2,text3;
    LinearLayoutManager mManager;
    Get_Data_Recycler adapter2;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return false;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get__data);
        Toolbar toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        search=findViewById(R.id.search);
        mRecyclerView = findViewById(R.id.recyclerlist);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        requestQueue = Volley.newRequestQueue(this);
        progressBar=findViewById(R.id.progress);
        EnableRuntimePermission();
        model = new ArrayList<>();
        Intent intent = getIntent();
        type_id = intent.getStringExtra("type_id");
       // Toast.makeText(this, ""+type_id, Toast.LENGTH_SHORT).show();

        if (!isOnline()) {
            checkConnection();
        } else {

            fetch();
        }


        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String text = search.getText().toString().toLowerCase(Locale.getDefault());
                //adapter.getFilter().filter(s);
                filter(s.toString());


            }

        });
    }


    void filter(String text){
        List<Search_Model> temp = new ArrayList();
        for(Search_Model d: model){
            //or use .equal(text) with you want equal match
            //use .toLowerCase() for better matches
            if(d.getTitle().toUpperCase().contains(text)|| d.getTitle().toLowerCase().contains(text)){
                temp.add(d);
            }
        }
        //update recyclerview
        try
        {
            adapter2.updateList(temp);
        }
        catch(NullPointerException e)
        {

        }

    }


    private void fetch()
    {
        progressBar.setVisibility(View.VISIBLE);
        Toast.makeText(this, "Loading Please wait.......", Toast.LENGTH_SHORT).show();
        String url = Base_Url.url+"fetch_image_audio.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

               // Toast.makeText(Get_Data.this, ""+response, Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
                if (response.equals("[null]"))
                {
                    Toast.makeText(Get_Data.this, "Data empty", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                }
                else {
                    mManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
                    mRecyclerView.setLayoutManager(mManager);
                    adapter2 = new Get_Data_Recycler(model, getApplicationContext(),Get_Data.this);
                  /*  DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mRecyclerView.getContext(),
                            mManager.getOrientation());
                    mRecyclerView.addItemDecoration(dividerItemDecoration);*/
                    mRecyclerView.setAdapter(adapter2);

                    JSONArray js=null;
                    try {
                        js=new JSONArray(response);
                        for (int i = 0; i < js.length(); i++)
                        {
                            Search_Model model2=new Search_Model();
                            JSONObject jsonobj =js.getJSONObject(i);
                            model2.setId(jsonobj.getString("id"));
                            model2.setTitle(jsonobj.getString("title"));
                            String image=jsonobj.getString("image");
                            model2.setImage(image);
                            String audio=jsonobj.getString("audio");
                            model2.setAudio_name(audio);
                            model2.setImage_name(Base_Url.url+"uploads_images/"+image);
                            model2.setAudio(Base_Url.url+"uploads_images/"+audio);
                            model2.setDate(jsonobj.getString("created_at"));
                            model2.setType("short_speed");
                            model.add(model2);
                            adapter2.notifyDataSetChanged();
                            progressBar.setVisibility(View.GONE);
                        }
                        progressBar.setVisibility(View.GONE);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressBar.setVisibility(View.GONE);
                fetch();

            }
        })  {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> data2 = new HashMap<>();


                data2.put("type_id",type_id);

                return data2;
            }
        };
        //stringRequest.setRetryPolicy(new DefaultRetryPolicy(60000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
         //requestQueue.add(stringRequest);
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

    public void checkConnection() {

        AlertDialog.Builder builder =new AlertDialog.Builder(this);
        builder.setTitle("No internet Connection");
        builder.setMessage("Please turn on internet connection to continue");
        builder.setCancelable(false);
        builder.setPositiveButton("Retry!!", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (!isOnline())
                {
                    alert_online();
                }
                else
                {
               fetch();
                }


            }
        }).setNegativeButton("close", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.setCancelable(false);
        alertDialog.show();
    }

    public void alert_online() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("No internet Connection");
        builder.setCancelable(false);
        builder.setMessage("Please turn on internet connection to continue");
       builder.setPositiveButton("Retry!!", new DialogInterface.OnClickListener() {
           @Override
           public void onClick(DialogInterface dialog, int which) {
               if (!isOnline())
               {
                   alert_online();
               }
               else
               {
                   fetch();
               }
           }
       }).setNegativeButton("close", new DialogInterface.OnClickListener() {
           @Override
           public void onClick(DialogInterface dialog, int which) {

           }
       });
       builder.show();
    }

    public void EnableRuntimePermission() {

        if (ActivityCompat.shouldShowRequestPermissionRationale(Get_Data.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

            Toast.makeText(Get_Data.this, "CAMERA permission allows us to Access CAMERA app", Toast.LENGTH_LONG).show();

        } else {

            ActivityCompat.requestPermissions(Get_Data.this, new String[]{
                    Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);

        }
    }

    public void alertDialog()

    {
        dialogBuilder = new AlertDialog.Builder(this);
// ...Irrelevant code for customizing the buttons and title
        dialogBuilder.setCancelable(false);
        dialogBuilder.setTitle("Downloading");
        dialogBuilder.setMessage("File downloading,please wait....");
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.activity_dowloading__volly__request, null);
        text1=dialogView.findViewById(R.id.text1);
        text3=dialogView.findViewById(R.id.text3);
        TextView hide=dialogView.findViewById(R.id.hide);
        dialogBuilder.setView(dialogView);

        alertDialog = dialogBuilder.create();
        hide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

        alertDialog.show();
    }


    @Override
    public void Alert() {
        dialogBuilder = new AlertDialog.Builder(this);
// ...Irrelevant code for customizing the buttons and title
        dialogBuilder.setCancelable(false);
        dialogBuilder.setTitle("Downloading");
        dialogBuilder.setMessage("File downloading,please wait....");
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.activity_dowloading__volly__request, null);
        text1=dialogView.findViewById(R.id.text1);
        text3=dialogView.findViewById(R.id.text3);
        TextView hide=dialogView.findViewById(R.id.hide);
        dialogBuilder.setView(dialogView);

        alertDialog = dialogBuilder.create();
        hide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

       // alertDialog.show();



    }
}