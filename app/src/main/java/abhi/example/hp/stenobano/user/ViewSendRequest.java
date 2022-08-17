package abhi.example.hp.stenobano.user;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
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
import java.util.Map;

import abhi.example.hp.stenobano.R;
import abhi.example.hp.stenobano.adapter.SendRequest_Adapter;
import abhi.example.hp.stenobano.config.BaseUrl;
import abhi.example.hp.stenobano.model.Search_Model;
import abhi.example.hp.stenobano.session.SesssionManager;

public class ViewSendRequest extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RequestQueue requestQueue;
    private List<Search_Model> modelList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_send_request);
        Toolbar toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setTitle("Your Requested");
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        recyclerView=findViewById(R.id.recyclerView);
        getRequestedList();
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


    private void getRequestedList()
    {
        modelList=new ArrayList<>();
        requestQueue= Volley.newRequestQueue(this);
        ProgressDialog progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("wait...");
        progressDialog.show();
        String url= BaseUrl.getUserRequest;
        StringRequest stringRequest=new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("responsss",response.toString());
                progressDialog.dismiss();
                try {
                    JSONArray jsonArray=new JSONArray(response);
                    if (jsonArray.length()>0)
                    {
                        for (int i=0;i<jsonArray.length();i++)
                        {
                            JSONObject jsonObject=jsonArray.getJSONObject(i);
                            Search_Model model=new Search_Model();
                            model.setDate(jsonObject.getString("created_at"));
                            model.setStatus(jsonObject.getString("status"));
                            model.setReason(jsonObject.getString("reason"));
                            modelList.add(model);

                        }

                        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(ViewSendRequest.this,LinearLayoutManager.VERTICAL,false);
                        recyclerView.setLayoutManager(linearLayoutManager);
                        SendRequest_Adapter adapter=new SendRequest_Adapter(modelList,getApplicationContext());
                        recyclerView.setAdapter(adapter);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                AlertDialog.Builder builder=new AlertDialog.Builder(ViewSendRequest.this);
                builder.create();
                builder.setTitle("Error!!!");
                builder.setMessage("Please check internet connection");
                builder.setCancelable(false);
                builder.setPositiveButton("Retry", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        getRequestedList();
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
                params.put("user_id",new SesssionManager(ViewSendRequest.this).userID());
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                60000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);

    }
}
