package abhi.example.hp.stenobano.user;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import androidx.core.app.ActivityCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.security.ProviderInstaller;
import com.squareup.picasso.Picasso;

import net.gotev.uploadservice.MultipartUploadRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import abhi.example.hp.stenobano.R;
import abhi.example.hp.stenobano.config.BaseUrl;
import abhi.example.hp.stenobano.other_class.SingleUploadBroadcastReceiver;
import abhi.example.hp.stenobano.session.SesssionManager;

public class Profile extends AppCompatActivity implements View.OnClickListener, SingleUploadBroadcastReceiver.Delegate {
    private TextView  name,name2, email, phone,days,expired;
    private SesssionManager sesssionManager;
    private ImageView back,image;
    private RequestQueue requestQueue;
    private ProgressDialog progressDialog;
    private Uri uri;
    private final SingleUploadBroadcastReceiver uploadReceiver = new SingleUploadBroadcastReceiver();
    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        requestPermission();
        sesssionManager = new SesssionManager(this);
        HashMap<String, String> map = new HashMap<>();
        map = sesssionManager.getUserDetails();
        name = findViewById(R.id.name);
        name2 = findViewById(R.id.name2);
        email = findViewById(R.id.email);
        phone = findViewById(R.id.mobile);
        back=findViewById(R.id.back);
        back.setOnClickListener(this);

        name.setText(map.get("name"));
        name2.setText(map.get("name"));
        email.setText(map.get("email"));
        phone.setText(map.get("mobile"));
        days=findViewById(R.id.days);
        image=findViewById(R.id.image);
        image.setOnClickListener(this);
        expired=findViewById(R.id.expired);
        expired.setText(map.get("valid"));
        sesssionManager=new SesssionManager(this);
        updateAndroidSecurityProvider();
       // getBalance();
        setImageProfile();
        getDays();

    }

    private void setImageProfile()
    {
        if (!new SesssionManager(this).getImage().equals("0"))
        {
            String url= BaseUrl.profile_image+new SesssionManager(this).getImage();
            Log.d("imageurl",url);
            Picasso.with(getApplicationContext()).load(url).fit().centerCrop()
                    .placeholder(R.drawable.ic_person_outline_black_24dp)
                    .error(R.drawable.ic_person_outline_black_24dp)
                    .into(image);
        }
        else
        {

        }
    }




    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        switch (item.getItemId()) {

        }
        return false;
    }

    @Override
    public void onClick(View view) {
        if (view.getId()==R.id.back)
        {
            onBackPressed();
        }
        else if (view.getId()==R.id.image)
        {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select Image"), 1);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null) {

            uri = data.getData();

            PdfUploadFunction();
        }
    }

    private void getBalance()
    {

        String url = BaseUrl.balance_android;
        requestQueue= Volley.newRequestQueue(getApplicationContext());
        progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Wait...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        StringRequest stringRequest=new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //Toast.makeText(Login.this, ""+response, Toast.LENGTH_SHORT).show();
                Log.i("withdraaaaa", "onResponse: "+response);
                try {
                    JSONObject jsonObject=new JSONObject(response);

                } catch (JSONException e) {
                    e.printStackTrace();
                }


                progressDialog.dismiss();


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Log.d("eeee",error.getMessage());

                AlertDialog.Builder builder=new AlertDialog.Builder(Profile.this);
                builder.setMessage("Please check internet conncetion!!");
                builder.create();
                builder.setPositiveButton("Retry", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        getBalance();
                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String,String>data=new HashMap<>();
                data.put("username",sesssionManager.userID());
                return data;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(60000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);
    }

    private void updateAndroidSecurityProvider() {
        try {
            ProviderInstaller.installIfNeeded(getApplicationContext());
        } catch (Exception e) {
            e.getMessage();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        uploadReceiver.register(this);

    }




    @Override
    protected void onPause() {
        super.onPause();
        uploadReceiver.unregister(this);
    }


    public void PdfUploadFunction() {

        dialog = new ProgressDialog(this);
        dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        dialog.setMessage("Uploading file, please wait.");
        dialog.setMax(100);
        dialog.setCancelable(true);
        dialog.show();

        //

        String File_Aadhar = FilePath.getPath(this, uri);
        if (File_Aadhar == null)
        {

            Toast.makeText(this, "Please Selecte All file  & try again.", Toast.LENGTH_LONG).show();

        }

        else
        {


            try {
                //  Toast.makeText(this, "hello", Toast.LENGTH_SHORT).show();
                String File_Id = UUID.randomUUID().toString();
                uploadReceiver.setDelegate(this);
                uploadReceiver.setUploadID(File_Id);
                new MultipartUploadRequest(this, File_Id, BaseUrl.upload_profile_image)
                        .addFileToUpload(File_Aadhar, "image")
                        .addParameter("id", sesssionManager.userID())
                        .addParameter("image_name",new SesssionManager(this).getImage())
                        .setMaxRetries(8)
                        .setUtf8Charset()
                        .startUpload();

            } catch (Exception exception) {

                Toast.makeText(this, exception.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onProgress(int progress) {
        dialog.setProgress(progress);
    }

    @Override
    public void onProgress(long uploadedBytes, long totalBytes) {

    }

    @Override
    public void onError(Exception exception) {

    }

    @Override
    public void onCompleted(int serverResponseCode, byte[] serverResponseBody) {
        dialog.dismiss();


        Log.d("wqw",new String(serverResponseBody));
        try {
            JSONArray jsonArray=new JSONArray(new String(serverResponseBody));
            JSONObject jsonObject=jsonArray.getJSONObject(0);
            if (jsonObject.getString("status").equals("Success"))
            {
                SesssionManager sesssionManager=new SesssionManager(this);
                sesssionManager.updateImage(jsonObject.getString("image"));
                Toast.makeText(this, "Profie Image has been Updated!!", Toast.LENGTH_LONG).show();
                String url=BaseUrl.profile_image+jsonObject.getString("image");
                Log.d("imageurl",url);
                Picasso.with(getApplicationContext()).load(url).fit().centerCrop()
                        .placeholder(R.drawable.ic_person_outline_black_24dp)
                        .error(R.drawable.ic_person_outline_black_24dp)
                        .into(image);
            }
            else
            {
                Toast.makeText(this, "Faild,try again!!", Toast.LENGTH_LONG).show();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    @Override
    public void onCancelled() {

    }





    private void requestPermission() {

        if (ActivityCompat.shouldShowRequestPermissionRationale(Profile.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            Toast.makeText(Profile.this, "Write External Storage permission allows us to do store images. Please allow this permission in App Settings.", Toast.LENGTH_LONG).show();
        } else {
            ActivityCompat.requestPermissions(Profile.this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 5);

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 5:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                } else {

                }
                break;
        }
    }


    private void getDays()
    {
        Calendar cal1 = new GregorianCalendar();
        Calendar cal2 = new GregorianCalendar();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sdfdate = new SimpleDateFormat("dd MMM yy");
        Date date2 = new Date();
        String curr_date=sdf.format(date2);
        Date date = null;
        try {
            date = sdf.parse(curr_date);
            cal1.setTime(date);
            date = sdf.parse(new SesssionManager(this).getValid());
            cal2.setTime(date);


            Date dDate = sdf.parse(new SesssionManager(this).getValid());
            expired.setText(sdfdate.format(dDate));
        } catch (ParseException e) {
            e.printStackTrace();
        }


        //cal1.set(2008, 8, 1);
        //cal2.set(2008, 9, 31);
        days.setText(String.valueOf(daysBetween(cal1.getTime(),cal2.getTime()))+" "+"Days");
        Log.d("total_days", String.valueOf(daysBetween(cal1.getTime(),cal2.getTime())));

    }

    public int daysBetween(Date d1, Date d2){
        return (int)( (d2.getTime() - d1.getTime()) / (1000 * 60 * 60 * 24));
    }


}
