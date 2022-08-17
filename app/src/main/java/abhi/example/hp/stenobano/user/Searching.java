 package abhi.example.hp.stenobano.user;

 import android.app.ProgressDialog;
 import android.os.Bundle;
 import androidx.appcompat.app.AppCompatActivity;
 import androidx.recyclerview.widget.LinearLayoutManager;
 import androidx.recyclerview.widget.RecyclerView;
 import android.view.KeyEvent;
 import android.view.View;
 import android.view.WindowManager;
 import android.view.inputmethod.EditorInfo;
 import android.widget.AdapterView;
 import android.widget.ArrayAdapter;
 import android.widget.AutoCompleteTextView;
 import android.widget.GridView;
 import android.widget.ImageView;
 import android.widget.ListView;
 import android.widget.TextView;
 import android.widget.Toast;
 import android.widget.Toolbar;

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

 public class Searching extends AppCompatActivity {
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
     AdapterList_Recycler adapter2;
     @Override
     protected void onCreate(Bundle savedInstanceState) {
         super.onCreate(savedInstanceState);
         setContentView(R.layout.activity_seraching);
         editText=findViewById(R.id.searchEditText);
         clearSearch=findViewById(R.id.clear);
         back=findViewById(R.id.back);
         //getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
         mRecyclerView=findViewById(R.id.recyclerView);
         requestQueue= Volley.newRequestQueue(this);
        // editText.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
         save();
         search_items();
            list_data =new ArrayList<>();
         ArrayAdapter<String> adapter = new ArrayAdapter<String>(Searching.this,
                 android.R.layout.simple_expandable_list_item_1, list_data);
         editText.setAdapter(adapter);
         model=new ArrayList<>();
         mManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
         mRecyclerView.setLayoutManager(mManager);
         adapter2 = new AdapterList_Recycler(model, Searching.this);
         mRecyclerView.setAdapter(adapter2);

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
                 String url = Base_Url.url+"fetch-all-fish.php";
                 StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                     @Override
                     public void onResponse(String response) {


                         for (int i = 0; i < response.length(); i++) {
                             try {
                                 JSONArray js=new JSONArray(response);

                                 JSONObject jsonobj =js.getJSONObject(i);
                                 list_data.add( jsonobj.getString("title"));
                                // Toast.makeText(Searching.this, "title is "+jsonobj.getString("title"), Toast.LENGTH_SHORT).show();
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

                 requestQueue.add(stringRequest);

             }


     private void search_items()
     {
         model.clear();
         //Toast.makeText(this, "click", Toast.LENGTH_SHORT).show();

         String url = Base_Url.url+"search_word.php";
         StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
             @Override
             public void onResponse(String response) {


                 Toast.makeText(Searching.this, "reponse is " + response, Toast.LENGTH_SHORT).show();

                 for (int i = 0; i < response.length(); i++) {
                     try {
                         Search_Model model2=new Search_Model();
                         JSONArray js=new JSONArray(response);
                         JSONObject jsonobj =js.getJSONObject(i);
                         model2.setId(jsonobj.getString("id"));
                         model2.setTitle(jsonobj.getString("title"));
                         String image=jsonobj.getString("image");
                         String url=Base_Url.url+"uploads_images"+"/"+image;
                         model2.setImage(url);
                        // model2.setDescription(jsonobj.getString("title"));
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
                       // data2.put("title","Animal");
                         return data2;
                     }
         };
         //stringRequest.setRetryPolicy(new DefaultRetryPolicy(60000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
         // requestQueue.add(stringRequest);
         requestQueue.add(stringRequest);

     }


 }





