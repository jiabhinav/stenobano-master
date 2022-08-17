package abhi.example.hp.stenobano.user;

import android.content.SharedPreferences;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
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
import java.util.Map;

import abhi.example.hp.stenobano.config.Base_Url;
import abhi.example.hp.stenobano.R;
import abhi.example.hp.stenobano.model.Search_Model;
import abhi.example.hp.stenobano.adapter.Renewal_Recycler;

public class Renewal_History extends AppCompatActivity {
private SharedPreferences sp;
private RecyclerView recyclerView;
ProgressBar progressBar;
 private  String mobile;
 RequestQueue requestQueue;
    private List<Search_Model> modellist;
    LinearLayoutManager mManager;
    Renewal_Recycler adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_renewal__history);
        Toolbar toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setTitle("Renewal History");
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));

         recyclerView=findViewById(R.id.recycler);
         requestQueue= Volley.newRequestQueue(this);
        sp=getSharedPreferences("steno_info",MODE_PRIVATE);
        mobile=sp.getString("mobile", null);
        modellist=new ArrayList<>();
        progressBar=findViewById(R.id.progress);
        mManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(mManager);
        adapter = new Renewal_Recycler(modellist, Renewal_History.this);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                mManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);
        recyclerView.setAdapter(adapter);
        get_renewal_history();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return false;
    }
    private void get_renewal_history()
    {
       // Toast.makeText(this, "mobile is "+mobile, Toast.LENGTH_SHORT).show();
        String url = Base_Url.url+"fetch_renewal_history.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.d("ressssss",response.toString());
                 //Toast.makeText(Renewal_History.this, "respons is " + response, Toast.LENGTH_SHORT).show();
                 progressBar.setVisibility(View.GONE);
                if (response.equals("empty"))
                {
                    Toast.makeText(Renewal_History.this, "Data empty", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                }
                else {

                    JSONArray js=null;
                    try {
                        js=new JSONArray(response);
                        for (int i = 0; i < js.length(); i++)
                        {
                            Search_Model model2=new Search_Model();
                            JSONObject jsonobj =js.getJSONObject(i);
                            model2.setId(jsonobj.getString("id"));
                            model2.setMobile(jsonobj.getString("mobile"));
                            model2.setEmail(jsonobj.getString("email"));
                            model2.setAmount(jsonobj.getString("amount"));
                            model2.setInvice(jsonobj.getString("invoice"));
                            model2.setFrom(jsonobj.getString("created_at"));
                            model2.setTo(jsonobj.getString("valid"));
                            int index=i+1;
                            model2.setIndex(Integer.toString(index));
                            modellist.add(model2);
                            adapter.notifyDataSetChanged();
                            progressBar.setVisibility(View.GONE);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressBar.setVisibility(View.GONE);


            }
        })  {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> data2 = new HashMap<>();


                data2.put("mobile",mobile);

                return data2;
            }
        };
        //stringRequest.setRetryPolicy(new DefaultRetryPolicy(60000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // requestQueue.add(stringRequest);
        requestQueue.add(stringRequest);

    }

}
