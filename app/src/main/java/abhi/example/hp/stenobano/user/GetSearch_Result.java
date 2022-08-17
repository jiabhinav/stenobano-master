package abhi.example.hp.stenobano.user;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
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
import abhi.example.hp.stenobano.adapter.AdapterList_Recycler;
import abhi.example.hp.stenobano.model.Search_Model;

public class GetSearch_Result extends AppCompatActivity {
RecyclerView recyclerView;
RequestQueue requestQueue;
List<Search_Model>models;
    String smilier;
    String title;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_search__result);
        recyclerView=findViewById(R.id.recyclerView);
        Toolbar toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
            toolbar.setTitle("Searched word");
            toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
      //  getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        requestQueue= Volley.newRequestQueue(this);
        Intent i=getIntent();
        progressBar=findViewById(R.id.progress);
        progressBar.setVisibility(View.GONE);
        title=i.getStringExtra("title");


        getSearched_Word(title,smilier);
    }
   public void getSearched_Word(String title,String smilier)
    {
progressBar.setVisibility(View.VISIBLE);
        models=new ArrayList<>();
        String url = Base_Url.url+"getSearch_Word.php";
        StringRequest stringRequest=new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                if (!response.equals("[null]"))
                {
                 LinearLayoutManager   mManager = new LinearLayoutManager(GetSearch_Result.this, LinearLayoutManager.VERTICAL, false);
                    recyclerView.setLayoutManager(mManager);
                    AdapterList_Recycler adapterList_recycler=new AdapterList_Recycler(models,GetSearch_Result.this);
                    recyclerView.setAdapter(adapterList_recycler);

                    JSONArray jsonArray=null;
                    try {
                        jsonArray=new JSONArray(response);

                        for (int i=0;i<jsonArray.length();i++)
                        {
                            JSONObject jsonObject=jsonArray.getJSONObject(i);
                            Search_Model model=new Search_Model();
                            model.setId(jsonObject.getString("id"));
                            model.setTitle(jsonObject.getString("title"));
                            model.setImage(Base_Url.url+"uploads_images/"+jsonObject.getString("image"));
                            models.add(model);


                        }
                        progressBar.setVisibility(View.GONE);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                else 
                {
                    Toast.makeText(GetSearch_Result.this, "Not found !!!", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressBar.setVisibility(View.GONE);

            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String>data=new HashMap<>();
                data.put("title",title);
                return data;
            }
        };
        requestQueue.add(stringRequest);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return false;
    }
}
