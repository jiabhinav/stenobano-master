package abhi.example.hp.stenobano.user;

import android.app.ProgressDialog;
import androidx.core.widget.NestedScrollView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
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
import abhi.example.hp.stenobano.adapter.Notification_Recycler;
import abhi.example.hp.stenobano.model.Search_Model;

public class Steno_Job_Notification extends AppCompatActivity implements View.OnClickListener{
Toolbar toolbar;
    //AutoCompleteTextView editText;
    ImageView clearSearch,back;
    String image;
    RequestQueue requestQueue;
    GridView gridView;
    ProgressDialog progressDialog;
ProgressBar progress;
NestedScrollView nested_scrol;
int currt=0;
int last=20;
Button more;
    ListView listView;
    private List<String> list_data;
    private List<Search_Model>model;
    RecyclerView mRecyclerView;
    //  RecyclerView.Adapter mAdapter;

    LinearLayoutManager mManager;

    RequestQueue mRequest;
    Notification_Recycler adapter2;
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
        setContentView(R.layout.activity_steno__job__notification);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
       // editText=findViewById(R.id.searchEditText);
        //clearSearch=findViewById(R.id.clear);
       // back=findViewById(R.id.back);
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        more=findViewById(R.id.more);
        more.setOnClickListener(this);
        more.setVisibility(View.GONE);
        mRecyclerView=findViewById(R.id.recyclerView);
        mRecyclerView.setNestedScrollingEnabled(false);
        requestQueue= Volley.newRequestQueue(this);
        // editText.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
        progress=findViewById(R.id.progress);
        progress.setVisibility(View.GONE);
        //save();
        fetch(currt,last);
       /* list_data =new ArrayList<>();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(Steno_Job_Notification.this,
                android.R.layout.simple_expandable_list_item_1, list_data);
        editText.setAdapter(adapter);
        */
        model=new ArrayList<>();
        mManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(mManager);
        adapter2 = new Notification_Recycler(model, Steno_Job_Notification.this);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mRecyclerView.getContext(),
                mManager.getOrientation());
        mRecyclerView.addItemDecoration(dividerItemDecoration);
        mRecyclerView.setAdapter(adapter2);
/*

        editText.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                search_items();
            }
        });
        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    //editText.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);

                    search_items();
                    return true;
                }
                return false;
            }
        });
        */




    }

   private void save()
    {
        String url = Base_Url.url+"search_hint.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                // adapter = new ArrayAdapter(Searching.this, android.R.layout.simple_list_item_1,list_data);
                //editText.setAdapter(adapter);


              if (!response.equals("[null]"))
              {
                  JSONArray js= null;
                  try {
                      js = new JSONArray(response);
                      for (int i = 0; i < response.length(); i++)
                      {
                          JSONObject jsonobj =js.getJSONObject(i);
                          list_data.add( jsonobj.getString("title"));
                      }

                  } catch (JSONException e) {
                      e.printStackTrace();
                  }
              }
              else {
                  Toast.makeText(Steno_Job_Notification.this, "Titile Empty", Toast.LENGTH_SHORT).show();
              }




            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {



            }
        })  {
                   /*  @Override
                     protected Map<String, String> getParams() throws AuthFailureError {

                         Map<String, String> data2 = new HashMap<>();


                         data2.put("type",editText.getText().toString());

                         return data2;
                     }*/
        };
        //stringRequest.setRetryPolicy(new DefaultRetryPolicy(60000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        //requestQueue.add(stringRequest);
        requestQueue.add(stringRequest);

    }





    private void search_items()
    {
        more.setVisibility(View.GONE);
        progress.setVisibility(View.VISIBLE);
        model.clear();
        Toast.makeText(this, "searching....", Toast.LENGTH_SHORT).show();

        String url =Base_Url.url+"search_notification_job.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {


               // Toast.makeText(Steno_Job_Notification.this, "reponse is " + response, Toast.LENGTH_SHORT).show();
                if (!response.equals("[null]"))
                {
                    JSONArray js= null;
                    try {
                        js = new JSONArray(response);
                        for (int i = 0; i < js.length(); i++)
                        {
                            Search_Model model2=new Search_Model();
                            JSONObject jsonobj =js.getJSONObject(i);
                            model2.setId(jsonobj.getString("id"));
                            model2.setTitle(jsonobj.getString("title"));
                            model2.setImage(Base_Url.url+"notification_image/"+jsonobj.getString("image"));
                            model2.setDate(jsonobj.getString("date"));
                            model.add(model2);
                            adapter2.notifyDataSetChanged();
                            progress.setVisibility(View.GONE);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                else
                {
                    Toast.makeText(Steno_Job_Notification.this, "Not Found", Toast.LENGTH_SHORT).show();

                }





            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                progress.setVisibility(View.GONE);

            }
        })  {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> data2 = new HashMap<>();


               // data2.put("title",editText.getText().toString());


                return data2;
            }
        };
        //stringRequest.setRetryPolicy(new DefaultRetryPolicy(60000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // requestQueue.add(stringRequest);
        requestQueue.add(stringRequest);

    }

    private void fetch(final int curr, final int last)
    {
        progress.setVisibility(View.VISIBLE);

        String url = Base_Url.url+"fetch_notification.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {


               //Toast.makeText(Steno_Job_Notification.this, "reponse is " + response, Toast.LENGTH_SHORT).show();
if(!response.equals("[null]"))
{
    JSONArray js=null;
    try {
        js=new JSONArray(response);
        for (int i = 0; i < js.length(); i++) {

            Search_Model model2=new Search_Model();
            JSONObject jsonobj =js.getJSONObject(i);
            model2.setId(jsonobj.getString("id"));
            model2.setTitle(jsonobj.getString("title"));
            model2.setImage(Base_Url.url+"notification_image/"+jsonobj.getString("image"));
            model2.setDate(jsonobj.getString("date"));
            model.add(model2);
            adapter2.notifyDataSetChanged();
            progress.setVisibility(View.GONE);
        }

        more.setVisibility(View.VISIBLE);


    } catch (JSONException e) {
        e.printStackTrace();
    }

    }
    else
{
    Toast.makeText(Steno_Job_Notification.this, "Not found", Toast.LENGTH_SHORT).show();
    progress.setVisibility(View.GONE);
}

}

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                progress.setVisibility(View.GONE);

            }
        })  {
           @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> data2 = new HashMap<>();


               data2.put("curr", String.valueOf(curr));
               data2.put("last", String.valueOf(last));

                return data2;
            }
        };
        //stringRequest.setRetryPolicy(new DefaultRetryPolicy(60000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // requestQueue.add(stringRequest);
        requestQueue.add(stringRequest);

    }





    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        if (v.getId()==R.id.more)
        {
           currt=currt+20;
           fetch(currt,last);
        }

    }
}