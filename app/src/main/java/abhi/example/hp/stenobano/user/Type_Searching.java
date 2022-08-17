package abhi.example.hp.stenobano.user;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.os.Looper;
import androidx.core.widget.NestedScrollView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
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

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;

import abhi.example.hp.stenobano.config.Base_Url;
import abhi.example.hp.stenobano.R;
import abhi.example.hp.stenobano.adapter.Get_Search_Suggetion_Adapter;
import abhi.example.hp.stenobano.model.Category_Model;
import abhi.example.hp.stenobano.sqlite_databse.Search_History;
import rx.subjects.PublishSubject;

public class Type_Searching extends AppCompatActivity implements View.OnClickListener {
    int curr_page=0;
    int last_page=20;
    Toolbar toolbar;
    ImageView cancel;
    private List<Category_Model> userModelArrayList;
    Search_History databaseHelper;
    int counter = 0;
    RequestQueue requestQueue;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private List<Category_Model>models;
    private PublishSubject<String> subject;
    LinearLayoutManager mLayoutManager;
    Timer timer;
    final int DELAY = 1000;
    Get_Search_Suggetion_Adapter my_all_product_recycler;
    public  static EditText search;
    String editable="";
    String editText="";
    public  static String brand="";
    public  static  String category="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_type__searching);
        toolbar=findViewById(R.id.toolbar);
        recyclerView=findViewById(R.id.search_recycler);
        requestQueue= Volley.newRequestQueue(this);
        setSupportActionBar(toolbar);
        userModelArrayList=new ArrayList<>();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        models=new ArrayList<>();
        cancel=findViewById(R.id.cancel);
        cancel.setOnClickListener(this);
        progressBar=findViewById(R.id.progress);
        search=findViewById(R.id.search);
        progressBar.setVisibility(View.GONE);
        databaseHelper = new Search_History(this);
        File dbFile = getApplicationContext().getDatabasePath("steno_search_history");

        if (!dbFile.exists()) {
            //Toast.makeText(this, "not found", Toast.LENGTH_SHORT).show();
        }
        else
        {
            //  Toast.makeText(this, "found"+" "+dbFile, Toast.LENGTH_SHORT).show();
            userModelArrayList = databaseHelper.getAllContacts();
            LinearLayoutManager layoutManager2 =new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
            recyclerView.setLayoutManager(layoutManager2);
            my_all_product_recycler = new Get_Search_Suggetion_Adapter (userModelArrayList, getApplicationContext());
            recyclerView.setAdapter(my_all_product_recycler);

        }









        search.addTextChangedListener(new TextWatcher() {
            Handler handler = new Handler(Looper.getMainLooper() /*UI thread*/);
            Runnable workRunnable;
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(final CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().equals(""))
                {
                    //Toast.makeText(SearchResultsActivity.this, "clear", Toast.LENGTH_SHORT).show();
                    models.clear();
                }
                else
                {

                    int counter = 0;

                    if (s.toString().length()>1)
                    {
                       // gettext(s.toString());
                        handler.removeCallbacks(workRunnable);
                        workRunnable = () -> gettext(s.toString());
                        handler.postDelayed(workRunnable, 500);
                    }
                    else
                    {

                    }

                }


            }



        });

        NestedScrollView scroller = findViewById(R.id.nested);
        if (scroller != null) {
            scroller.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
                @Override
                public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {

                    if (scrollY > oldScrollY) {
                        // Log.i(TAG, "Scroll DOWN");

                    }
                    if (scrollY < oldScrollY) {
                        //  Log.i(TAG, "Scroll UP");

                    }

                    if (scrollY == 0) {
                        // Log.i(TAG, "TOP SCROLL");
                    }

                    if (scrollY == (v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight())) {
                        //  Log.i(TAG, "BOTTOM SCROLL");
                    }


                    if (scrollY == (v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight())) {
                        //  imageView.setVisibility(View.VISIBLE);

                        Toast toast = new Toast(getApplicationContext());
                        toast.setGravity(Gravity.BOTTOM, 0, 0);
                        //  toast.setText("Scroll Down");
                        ImageView view = new ImageView(Type_Searching.this);
                        view.setImageResource(R.drawable.ic_arrow_upward_black_24dp);
                        toast.setView(view);
                        toast.show();
                        curr_page=curr_page+20;
                        searchItems(editable,curr_page,last_page);



                    }
                }
            });

        }

        search.setOnEditorActionListener(new TextView.OnEditorActionListener(){
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_GO) {
                    if (search.getText().toString().equals(""))
                    {
                        Toast.makeText(Type_Searching.this, "Enter search word", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        Search_History db = new Search_History(Type_Searching.this);
                        Category_Model model = new Category_Model();
                        model.setName(search.getText().toString());
                        db.addContact(model);
                        Intent i=new Intent(Type_Searching.this,GetSearch_Result.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        i.putExtra("title",search.getText().toString());
                        startActivity(i);
                    }

                    return true;
                }
                return false;
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.history, menu);

        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        if (id == R.id.clear_history) {

            String TABLE_CONTACTS = "search_history";
            Search_History search_history=new Search_History(Type_Searching.this);
            SQLiteDatabase db = search_history.getWritableDatabase();
            db.delete(TABLE_CONTACTS, null,null);
            db.close();
            userModelArrayList.clear();
            Toast.makeText(this, "Your searched history has been cleared", Toast.LENGTH_SHORT).show();
            return true;
        }


        return false;
    }

    @Override
    public void onBackPressed() {
        finish();
        this.overridePendingTransition(0,0);

    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        int id=v.getId();
        if (id==R.id.cancel)
        {
            search.setText("");
        }
    }
    private final void gettext(String str) {
        editable=str;
        searchItems(str,curr_page,last_page);

        //  Toast.makeText(this, "str is "+str, Toast.LENGTH_SHORT).show();

        //
    }

    public  void searchItems(final String sear,final int curr,final int last)
    {
        models.clear();
        progressBar.setVisibility(View.VISIBLE);
        // Toast.makeText(this, ""+sear+" "+curr+" "+last, Toast.LENGTH_SHORT).show();
        String url = Base_Url.url+"search_word.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                 // Toast.makeText(Type_Searching .this, "response is "+response, Toast.LENGTH_SHORT).show();

                // mRecyclerView .setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.HORIZONTAL));
                if (!response.equals("[null]"))
                {
                    mLayoutManager = new GridLayoutManager(getApplicationContext(), 1);
                    recyclerView.setLayoutManager(mLayoutManager);
                    my_all_product_recycler = new Get_Search_Suggetion_Adapter(models, getApplicationContext());
                    recyclerView.setAdapter(my_all_product_recycler);
                    //recyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext(), DividerItemDecoration.VERTICAL));
                    my_all_product_recycler.notifyDataSetChanged();
                    //=======================================================
                    // Toast.makeText(View_Under_Subcategory.this, "respons is "+response, Toast.LENGTH_SHORT).show();
                    JSONArray jsonArray = null;
                    try {
                        jsonArray = new JSONArray(response);

                        for (int i = 0; i < jsonArray.length(); i++) {

                            Category_Model model = new Category_Model();
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            model.setId(jsonObject.getString("id"));
                            model.setName(jsonObject.getString("title"));
                            model.setType("word_search");
                            models.add(model);




                        }
                        progressBar.setVisibility(View.GONE);

                        //
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }



                }
                else
                {

                    Toast t=Toast.makeText(Type_Searching.this, search.getText().toString()+" "+"not Found,Msg on: 7290-0007-23", Toast.LENGTH_LONG);
                    t.setGravity(Gravity.TOP,0,0);
                    t.show();
                    progressBar.setVisibility(View.GONE);
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

                data.put("title", sear);
                data.put("curr_page", Integer.toString(curr));
                data.put("last_page",Integer.toString(last));

                return data;
            }
        };

        requestQueue.add(stringRequest);

    }



}
