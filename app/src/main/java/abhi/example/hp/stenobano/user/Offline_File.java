package abhi.example.hp.stenobano.user;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import abhi.example.hp.stenobano.Interface.IMethodCaller;
import abhi.example.hp.stenobano.R;
import abhi.example.hp.stenobano.adapter.Offline_Adapter;
import abhi.example.hp.stenobano.adapter.Offline_CustomAdapter;
import abhi.example.hp.stenobano.model.Helper_Model;
import abhi.example.hp.stenobano.sqlite_databse.DatabaseHandler;

public class Offline_File extends AppCompatActivity implements IMethodCaller {

    private RecyclerView listView;
    private List<Helper_Model> userModelArrayList;
    private Offline_CustomAdapter customAdapter;
    private DatabaseHandler databaseHelper;
    private static final String DATABASE_NAME = "steno";
public  static LinearLayout relativeLayout;
private LinearLayoutManager mManager;
private Offline_Adapter adapter;
    // Contacts table name
    private static final String TABLE_CONTACTS = "steno_file";

    // Contacts Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_IMAGE = "image";
    private static final String KEY_AUDIO = "audio";

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
        setContentView(R.layout.activity_offline__file);
        Toolbar toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setTitle("Notification");
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        relativeLayout=findViewById(R.id.rl);
        userModelArrayList = new ArrayList<>();
        listView = findViewById(R.id.listView);
        databaseHelper=new DatabaseHandler(this);
       userModelArrayList = databaseHelper.getAllContacts();
        //customAdapter = new Offline_CustomAdapter(this, (ArrayList<Helper_Model>) userModelArrayList,Offline_File.this);
        //listView.setAdapter(customAdapter);
        mManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        listView.setLayoutManager(mManager);
        adapter = new Offline_Adapter(this, (ArrayList<Helper_Model>) userModelArrayList,Offline_File.this);
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        record();


    }



    public void record() {
        DatabaseHandler dh = new DatabaseHandler(this);
        SQLiteDatabase db = dh.getWritableDatabase();
        String count = "SELECT count(*) FROM " + TABLE_CONTACTS;
        Cursor mcursor = db.rawQuery(count, null);
        mcursor.moveToFirst();
        int icount = mcursor.getInt(0);
        if (icount > 0) {
            //Toast.makeText(this, "found", Toast.LENGTH_SHORT).show();
        }
//leave
        else {

            //Toast.makeText(this, "not found", Toast.LENGTH_SHORT).show();
            LayoutInflater mInflater=(LayoutInflater)this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View  view=mInflater.inflate(R.layout.notfound, null);
             relativeLayout.addView(view);



//populate table
        }
    }


    @Override
    public void getOffline() {
        databaseHelper=new DatabaseHandler(this);
        userModelArrayList = databaseHelper.getAllContacts();
        //customAdapter = new Offline_CustomAdapter(this, (ArrayList<Helper_Model>) userModelArrayList,Offline_File.this);
        //listView.setAdapter(customAdapter);
        mManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        listView.setLayoutManager(mManager);
        adapter = new Offline_Adapter(this, (ArrayList<Helper_Model>) userModelArrayList,Offline_File.this);
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        
    }

}
