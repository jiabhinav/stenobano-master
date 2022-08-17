package abhi.example.hp.stenobano.user;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;

import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;

import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;


import java.util.ArrayList;

import java.util.List;
import java.util.Locale;

import abhi.example.hp.stenobano.Interface.AlertDialogInterFace;
import abhi.example.hp.stenobano.Interface.Download;
import abhi.example.hp.stenobano.R;

import abhi.example.hp.stenobano.adapter.CategoryDetailsAdapter;

import abhi.example.hp.stenobano.databinding.ActivityGetDataNewBinding;

import abhi.example.hp.stenobano.model.ModelCategoryDetail;

import abhi.example.hp.stenobano.other_class.DownloadFile;
import abhi.example.hp.stenobano.other_class.ProcessingDialog;
import abhi.example.hp.stenobano.retrofit.APIClient;
import abhi.example.hp.stenobano.sqlite_databse.DatabaseHandler;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;

public class GetDataNew extends AppCompatActivity  implements AlertDialogInterFace, Download {
    RequestQueue requestQueue;
    public static AlertDialog alertDialog;
    public static AlertDialog.Builder dialogBuilder;
    public static List<ModelCategoryDetail.Result> modellist;
   public static String type_id;
    ProcessingDialog processingDialog;
    LinearLayoutManager mManager;
    CategoryDetailsAdapter adapter2;
    public static TextView text1,text2,text3;
    private ActivityGetDataNewBinding binding;
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
       // setContentView(R.layout.activity_get_data_new);
        binding= DataBindingUtil.setContentView(this,R.layout.activity_get_data_new);
        processingDialog=new ProcessingDialog(this);
        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        requestQueue = Volley.newRequestQueue(this);
        EnableRuntimePermission();
        modellist = new ArrayList<>();
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);

        Intent intent = getIntent();
        type_id = intent.getStringExtra("type_id");
        LinearLayoutManager layoutManager=new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        binding.recyclerlist.setLayoutManager(layoutManager);
        if (!isOnline()) {
            checkConnection();
        } else {

            fetch();
        }


        binding.search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String text = binding.search.getText().toString().toLowerCase(Locale.getDefault());
                //adapter.getFilter().filter(s);
                filter(s.toString());


            }

        });

        DatabaseHandler db = new DatabaseHandler(this);
        Log.d("sizsisis",db.getAllContacts().size()+""+"-"+type_id);
    }


    void filter(String text){
        List<ModelCategoryDetail.Result> temp = new ArrayList();
        for(ModelCategoryDetail.Result d: modellist){
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

        if (ActivityCompat.shouldShowRequestPermissionRationale(GetDataNew.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

            Toast.makeText(GetDataNew.this, "CAMERA permission allows us to Access CAMERA app", Toast.LENGTH_LONG).show();

        } else {

            ActivityCompat.requestPermissions(GetDataNew.this, new String[]{
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

    }


    private void fetch()
    {
        processingDialog.show("");

        RequestBody key = RequestBody.create(MultipartBody.FORM, type_id);
        Call<ModelCategoryDetail> call = APIClient.getInstance().getDataAudio_Image(key);
        call.enqueue(new Callback<ModelCategoryDetail>() {
            @Override
            public void onResponse(Call<ModelCategoryDetail> call, retrofit2.Response<ModelCategoryDetail> response) {
                processingDialog.dismiss();
                Log.d("type122sdddd", "msg" + new Gson().toJson(response.body()));
                ModelCategoryDetail model=response.body();
                modellist=model.getResult();
                adapter2=new CategoryDetailsAdapter(modellist, GetDataNew.this,GetDataNew.this,GetDataNew.this);
                binding.recyclerlist.setAdapter(adapter2);
                adapter2.notifyDataSetChanged();

            }

            @Override
            public void onFailure(Call<ModelCategoryDetail> call, Throwable t) {
                Log.d("erere",t.toString());
                processingDialog.dismiss();
            }
        });
    }


    @Override
    public void downloadfile(ModelCategoryDetail.Result result) {
        new DownloadFile(GetDataNew.this).download(result);
    }
}
