package abhi.example.hp.stenobano.user;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.provider.MediaStore;
import androidx.core.app.ActivityCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import abhi.example.hp.stenobano.config.Base_Url;
import abhi.example.hp.stenobano.R;

public class Job_Notification extends AppCompatActivity implements View.OnClickListener {

    private EditText type, title, description;
    ImageView choose, camera, imageView;
    Button save;
    TextView select;
    private Bitmap bitmap;
    Bitmap bitmap4;
    private Bitmap bitmap2;
    private int PICK_IMAGE_REQUEST = 1;
    private int PICK_IMAGE_REQUEST2 = 2;
    RequestQueue requestQueue;
    ProgressDialog progressDialog;
    String admin_id;
    String TYPE_ID = "101";
    String type_id;
    Uri uri;
    String u;
    final int CROP_PIC = 3;
    Uri imageUri;
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
        setContentView(R.layout.activity_job__notification);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        type = findViewById(R.id.type);
        title = findViewById(R.id.title);
        select = findViewById(R.id.select);
        choose = findViewById(R.id.choose);
        camera = findViewById(R.id.camera);

        imageView = findViewById(R.id.imageView23);
        choose.setOnClickListener(this);
        camera.setOnClickListener(this);
        save = findViewById(R.id.save);
        save.setOnClickListener(this);
        AllowRunTimePermission();
        EnableRuntimePermission();
        requestQueue = Volley.newRequestQueue(this);
        SharedPreferences sp = getSharedPreferences("steno_info", MODE_PRIVATE);
        admin_id = sp.getString("id", null);
        Intent intent = getIntent();
        type_id = intent.getStringExtra("type_id");

    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.choose) {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "pic"), PICK_IMAGE_REQUEST);

        }

        if (id == R.id.camera) {
 /*
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(intent, PICK_IMAGE_REQUEST2);
*/
            //=====================


            ContentValues values = new ContentValues();
            values.put(MediaStore.Images.Media.TITLE, "New Picture");
            values.put(MediaStore.Images.Media.DESCRIPTION, "From your Camera");
            imageUri = getContentResolver().insert(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
            startActivityForResult(intent, PICK_IMAGE_REQUEST2);


            //================================

        } else if (id == R.id.save) {
            save();

        }

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {

            Uri filePath = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);

                String name = filePath.getPath();
                //choose_image.setText("Selected");
                imageView.setImageBitmap(bitmap);
                //choose_image.setBackgroundColor(Color.rgb(0,150,0));
                select.setText("Seleted");
                select.setTextColor(Color.rgb(0, 150, 0));

            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (requestCode == PICK_IMAGE_REQUEST2) {
//================================
/*
           bitmap = (Bitmap) data.getExtras().get("data");
            imageView.setImageBitmap(bitmap);
            uri=getImageUri(getApplicationContext(),bitmap);
           //performCrop();
 */
//====================================

            Bitmap thumbnail = null;
            try {
                thumbnail = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                //imageView.setImageBitmap(thumbnail);
                //uri = getRealPathFromURI(imageUri);
                uri=getImageUri(getApplicationContext(),thumbnail);
                performCrop();
            } catch (IOException e) {
                e.printStackTrace();
            }



            //  ==================
        }
        else if (requestCode == CROP_PIC) {

            if(data.getData()==null){
                bitmap = (Bitmap)data.getExtras().get("data");
                imageView.setImageBitmap(bitmap);

            }else{
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(Job_Notification.this.getContentResolver(), data.getData());
                    imageView.setImageBitmap(bitmap);

                } catch (IOException e) {

                    e.printStackTrace();

                }
            }
        }



    }
    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 80, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }
    public Uri getImageUri2(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }


    public void EnableRuntimePermission() {

        if (ActivityCompat.shouldShowRequestPermissionRationale(Job_Notification.this,
                Manifest.permission.CAMERA)) {

            Toast.makeText(Job_Notification.this, "CAMERA permission allows us to Access CAMERA app", Toast.LENGTH_LONG).show();

        } else {

            ActivityCompat.requestPermissions(Job_Notification.this, new String[]{
                    Manifest.permission.CAMERA}, 1);

        }
    }

    @Override
    public void onRequestPermissionsResult(int RC, String per[], int[] PResult) {

        switch (RC) {

            case 1:

                if (PResult.length > 0 && PResult[0] == PackageManager.PERMISSION_GRANTED) {

                    //Toast.makeText(Add_Word.this, "Permission Granted, Now your application can access CAMERA.", Toast.LENGTH_LONG).show();

                } else {

                    // Toast.makeText(Add_Word.this, "Permission Canceled, Now your application cannot access CAMERA.", Toast.LENGTH_LONG).show();

                }
                break;
        }
    }


    public String getStringImage(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 30, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;

    }



    private void performCrop() {
        // take care of exceptions
        try {
            // call the standard crop action intent (the user device may not
            // support it)
            Intent cropIntent = new Intent("com.android.camera.action.CROP");
            // indicate image type and Uri
            cropIntent.setDataAndType(uri, "image/*");
            // set crop properties
            cropIntent.putExtra("crop", "true");
            // indicate aspect of desired crop
            cropIntent.putExtra("aspectX", .8);
            cropIntent.putExtra("aspectY", .6);
            // indicate output X and Y
            cropIntent.putExtra("outputX", 400);
            cropIntent.putExtra("outputY", 250);
            // retrieve data on return
            cropIntent.putExtra("return-data", true);
            // start the activity - we handle returning in onActivityResult
            startActivityForResult(cropIntent, CROP_PIC);
        }
        // respond to users whose devices do not support the crop action
        catch (ActivityNotFoundException anfe) {
            Toast toast = Toast
                    .makeText(this, "This device doesn't support the crop action!", Toast.LENGTH_SHORT);
            toast.show();
        }
    }


    private void save() {
        if (!isOnline())
        {
            AlertDialog.Builder builder =new AlertDialog.Builder(this);
            builder.setTitle("No internet Connection");
            builder.setMessage("Please turn on internet connection to continue");
            builder.setNegativeButton("close", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();

                }
            });
            AlertDialog alertDialog = builder.create();
            alertDialog.setCancelable(false);
            alertDialog.show();
        }
        else {

            //  Toast.makeText(this, ""+getStringImage(bitmap), Toast.LENGTH_SHORT).show();
            if (title.getText().toString().equals("")) {
                Toast.makeText(this, "Enter Title", Toast.LENGTH_SHORT).show();

            }  else {

                progressDialog = ProgressDialog.show(Job_Notification.this, "Please wait...", "saving...", true);
                progressDialog.setCancelable(true);
                String url =  Base_Url.url+"upload_job_notificatoin.php";
                StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Toast.makeText(Job_Notification.this, "" + response, Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                        title.setText("");
                        type.setText("");

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(Job_Notification.this, "" + error, Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();

                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {

                        Map<String, String> data = new HashMap<>();
                        String image = getStringImage(bitmap);
                        data.put("admin_id", admin_id);
                        data.put("type", type.getText().toString());
                        data.put("title", title.getText().toString());
                        data.put("image", image);
                        data.put("type_id", type_id);
                        return data;
                    }
                };
                //stringRequest.setRetryPolicy(new DefaultRetryPolicy(60000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                // requestQueue.add(stringRequest);
                requestQueue.add(stringRequest);

            }

        }

    }

    public void AllowRunTimePermission(){

        if (ActivityCompat.shouldShowRequestPermissionRationale(Job_Notification.this, Manifest.permission.READ_EXTERNAL_STORAGE))
        {

            Toast.makeText(Job_Notification.this,"READ_EXTERNAL_STORAGE permission Access Dialog", Toast.LENGTH_LONG).show();

        } else {

            ActivityCompat.requestPermissions(Job_Notification.this,new String[]{ Manifest.permission.READ_EXTERNAL_STORAGE}, 1);

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


    public String getRealPathFromURI(Uri contentUri) {
        String[] proj = { MediaStore.Images.Media.DATA };
        Cursor cursor = managedQuery(contentUri, proj, null, null, null);
        int column_index = cursor
                .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }
}