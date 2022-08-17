package com.example.hp.dictionaryproject.admin_panel;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toolbar;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.hp.dictionaryproject.R;
import com.example.hp.dictionaryproject.adapter.Update_Word_Recycler;
import com.example.hp.dictionaryproject.model.Search_Model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Update_Delete_Word extends AppCompatActivity {
    Toolbar toolbar;
    AutoCompleteTextView editText;
    ImageView clearSearch,back;
    String image;
    RequestQueue requestQueue;
    GridView gridView;
    ProgressDialog progressDialog;

    ListView listView;
    private List<String> list_data;
    private List<Search_Model> model;
    RecyclerView mRecyclerView;
    //  RecyclerView.Adapter mAdapter;

    LinearLayoutManager mManager;

    RequestQueue mRequest;
    Update_Word_Recycler adapter2;
    String admin_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update__word);
        editText=findViewById(R.id.searchEditText);
        clearSearch=findViewById(R.id.clear);
        back=findViewById(R.id.back);
        mRecyclerView=findViewById(R.id.recyclerView);
        requestQueue= Volley.newRequestQueue(this);
        // editText.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
        TextView recent=findViewById(R.id.recent);
        recent.setPaintFlags(recent.getPaintFlags()| Paint.UNDERLINE_TEXT_FLAG);
        save();
        recently_update();
        list_data =new ArrayList<>();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(com.example.hp.dictionaryproject.admin_panel.Update_Delete_Word.this,
                android.R.layout.simple_expandable_list_item_1, list_data);
        editText.setAdapter(adapter);
        model=new ArrayList<>();
        mManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(mManager);
        adapter2 = new Update_Word_Recycler(model, com.example.hp.dictionaryproject.admin_panel.Update_Delete_Word.this);
        mRecyclerView.setAdapter(adapter2);
        SharedPreferences sp = getSharedPreferences("steno_info", MODE_PRIVATE);
         admin_id= sp.getString("id",null);

        clearSearch.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                editText.setText("");
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View v) {
                //  Intent intent=new Intent(Searching.this,MainActivity.class);
                //  startActivity(intent);
                //  overridePendingTransition(R.anim.push_left_in,R.anim.push_up_out);
                finish();
            }
        });

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
    }

    private void save()
    {
        String url = "http://www.samveda.co.in/android/android_dictionary/fetch-all-fish.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                // adapter = new ArrayAdapter(Searching.this, android.R.layout.simple_list_item_1,list_data);
                //editText.setAdapter(adapter);

                //  Toast.makeText(Searching.this, "reponse is " + response, Toast.LENGTH_SHORT).show();

                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONArray js=new JSONArray(response);

                        JSONObject jsonobj =js.getJSONObject(i);
                        list_data.add( jsonobj.getString("title"));


                        //  Toast.makeText(SearchData.this, ""+jsonobj.getString("name")+"\n "+jsonobj.getString("contact"), Toast.LENGTH_LONG).show();progressBar.setVisibility(View.GONE);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
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
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(60000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);
        requestQueue.add(stringRequest);

    }


    private void search_items()
    {
        model.clear();
        // Toast.makeText(this, "click", Toast.LENGTH_SHORT).show();

        String url = "http://www.samveda.co.in/android/android_dictionary/search_word.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {


                // Toast.makeText(Searching.this, "reponse is " + response, Toast.LENGTH_SHORT).show();

                for (int i = 0; i < response.length(); i++) {
                    try {
                        Search_Model model2=new Search_Model();
                        JSONArray js=new JSONArray(response);
                        JSONObject jsonobj =js.getJSONObject(i);
                        model2.setId(jsonobj.getString("id"));
                        model2.setTitle(jsonobj.getString("title"));
                        String image=jsonobj.getString("image");
                        model2.setImage_name(image);
                        String url="http://www.samveda.co.in/android/android_dictionary/uploads_images"+"/"+image;
                        model2.setImage(url);
                        model2.setDescription(jsonobj.getString("description"));
                        model.add(model2);
                        adapter2.notifyDataSetChanged();

                        // Toast.makeText(SearchData.this, ""+jsonobj.getString("name")+"\n "+jsonobj.getString("contact"), Toast.LENGTH_LONG).show();progressBar.setVisibility(View.GONE);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {



            }
        })  {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> data2 = new HashMap<>();


                data2.put("title",editText.getText().toString());

                return data2;
            }
        };
        //stringRequest.setRetryPolicy(new DefaultRetryPolicy(60000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // requestQueue.add(stringRequest);
        requestQueue.add(stringRequest);

    }
    private void recently_update()
    {
        // Toast.makeText(this, "click", Toast.LENGTH_SHORT).show();

        String url = "http://www.samveda.co.in/android/android_dictionary/fetch_recent_update.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {


               // Toast.makeText(Update_Delete_Word.this, "reponse is " + response, Toast.LENGTH_SHORT).show();

                for (int i = 0; i < response.length(); i++) {
                    try {
                        Search_Model model2=new Search_Model();
                        JSONArray js=new JSONArray(response);
                        JSONObject jsonobj =js.getJSONObject(i);
                        model2.setId(jsonobj.getString("id"));
                        model2.setTitle(jsonobj.getString("title"));
                        String image=jsonobj.getString("image");
                        model2.setImage_name(image);
                        String url="http://www.samveda.co.in/android/android_dictionary/uploads_images"+"/"+image;
                        model2.setImage(url);
                        model2.setDate(jsonobj.getString("updated_at"));
                        model2.setDescription(jsonobj.getString("description"));
                        model.add(model2);
                        adapter2.notifyDataSetChanged();

                        // Toast.makeText(SearchData.this, ""+jsonobj.getString("name")+"\n "+jsonobj.getString("contact"), Toast.LENGTH_LONG).show();progressBar.setVisibility(View.GONE);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {



            }
        })  {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> data2 = new HashMap<>();


                data2.put("admin_id",admin_id);

                return data2;
            }
        };
        //stringRequest.setRetryPolicy(new DefaultRetryPolicy(60000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // requestQueue.add(stringRequest);
        requestQueue.add(stringRequest);

    }

}



