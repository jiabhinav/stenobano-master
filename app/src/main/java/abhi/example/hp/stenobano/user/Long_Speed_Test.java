package abhi.example.hp.stenobano.user;

import android.Manifest;
import android.app.AlertDialog;
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
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import net.gotev.uploadservice.MultipartUploadRequest;
import net.gotev.uploadservice.UploadNotificationConfig;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.UUID;

import abhi.example.hp.stenobano.config.Base_Url;
import abhi.example.hp.stenobano.R;

public class Long_Speed_Test extends AppCompatActivity implements View.OnClickListener{
    Button UploadButton;
    EditText PdfNameEditText;
    ImageView choose, camera, imageView, select_audio,imageView2;
    TextView audioselect;
    private int PICK_IMAGE_REQUEST = 1;
    private int PICK_IMAGE_REQUEST2 = 2;
    Bitmap bitmap;
    String admin_id;
    TextView select1,select2;

    Uri uri;
    Uri uri2;
    Uri uri3;
    String type_id;
    int value =0;
    public static final String PDF_UPLOAD_HTTP_URL = Base_Url.url+"upload_long_speed_test.php";
   // public static final String PDF_UPLOAD_HTTP_URL = "http://www.samveda.co.in/android/android_dictionary/upload_long_image.php";
    //String url = "http://www.samveda.co.in/android/android_dictionary/upload.php";
    // public static final String PDF_UPLOAD_HTTP_URL = "http://yasheschool.com/android/upload_temp.php";
    public int PDF_REQ_CODE = 1;
    public int PDF_REQ_CODE2 = 2;
    public int SECOND_IMAGE = 4;
    public int PDF_REQ_CODE3 = 3;
    File finalFile;

    String PdfNameHolder, PdfPathHolder, PdfID, PdfPathHolder2,ImagePathHolder3;

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
        setContentView(R.layout.activity_long__speed__test);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        AllowRunTimePermission();
        EnableRuntimePermission();
        choose = findViewById(R.id.choose);
        camera = findViewById(R.id.camera);
        imageView = findViewById(R.id.imageView23);
        imageView2 = findViewById(R.id.imageView24);
        audioselect = findViewById(R.id.audioselect);
        select1 = findViewById(R.id.select);
        select2 = findViewById(R.id.select2);
        choose.setOnClickListener(this);
        camera.setOnClickListener(this);
        SharedPreferences sp = getSharedPreferences("steno_info", MODE_PRIVATE);
        admin_id = sp.getString("id", null);
        // Toast.makeText(this, ""+admin_id, Toast.LENGTH_SHORT).show();
        select_audio = findViewById(R.id.audio);
        UploadButton = findViewById(R.id.button_upload);
        PdfNameEditText = findViewById(R.id.title);
        Intent intent = getIntent();
        type_id = intent.getStringExtra("type_id");
        // Toast.makeText(this, ""+type_id, Toast.LENGTH_SHORT).show();

        select_audio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // PDF selection code start from here .

                Intent intent = new Intent();

                // intent.setType("application/pdf");
                intent.setType("audio/*");

                intent.setAction(Intent.ACTION_GET_CONTENT);

                startActivityForResult(Intent.createChooser(intent, "Select audio"), PDF_REQ_CODE);

            }
        });


        UploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isOnline())
                {
                    alert_online();
                }
                else if (PdfNameEditText.getText().toString().equals("")) {
                    Toast.makeText(Long_Speed_Test.this, "Please Enter file name", Toast.LENGTH_SHORT).show();
                } else if (!select1.getText().toString().equals("selected(1)")) {
                    Toast.makeText(Long_Speed_Test.this, "PLease select  first image ", Toast.LENGTH_SHORT).show();
                }
                else if (!select2.getText().toString().equals("selected(2)")) {
                    Toast.makeText(Long_Speed_Test.this, "PLease select second image ", Toast.LENGTH_SHORT).show();
                }else if (audioselect.getText().toString().equals("File Selected")) {
                    PdfUploadFunction();
                } else {
                    Toast.makeText(Long_Speed_Test.this, "Please select file first", Toast.LENGTH_SHORT).show();
                }


            }
        });

    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.choose) {

            if (value==0) {

                Intent intent = new Intent();

                //intent.setType("application/pdf");
                intent.setType("image/*");

                intent.setAction(Intent.ACTION_GET_CONTENT);

                startActivityForResult(Intent.createChooser(intent, "Select image"), PDF_REQ_CODE2);
                value=1;
            }

            else if (value==1)
            {
                Intent intent = new Intent();

                //intent.setType("application/pdf");
                intent.setType("image/*");

                intent.setAction(Intent.ACTION_GET_CONTENT);

                startActivityForResult(Intent.createChooser(intent, "Select image"), SECOND_IMAGE);
                value=0;

            }

        }

        if (id == R.id.camera) {
           /*
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(intent, PDF_REQ_CODE3);
            */

            ContentValues values = new ContentValues();
            values.put(MediaStore.Images.Media.TITLE, "New Picture");
            values.put(MediaStore.Images.Media.DESCRIPTION, "From your Camera");
            uri2 = getContentResolver().insert(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, uri2);
            startActivityForResult(intent, PDF_REQ_CODE3);

        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PDF_REQ_CODE && resultCode == RESULT_OK && data != null && data.getData() != null) {

            uri = data.getData();

            audioselect.setText("File Selected");
            audioselect.setBackgroundColor(Color.rgb(0, 150, 0));
            //SelectButton.setTextColor(Color.rgb());

        } else if (requestCode == PDF_REQ_CODE2 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            uri2 = data.getData();

            // Toast.makeText(this, "choose uri is  "+uri2, Toast.LENGTH_SHORT).show();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri2);
                uri2 = getImageUri(getApplicationContext(), bitmap);
                imageView.setImageBitmap(bitmap);
                select1.setText("selected(1)");
                //select.setTextColor(Color.rgb(0, 150, 0));
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        else if (requestCode == SECOND_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            uri3 = data.getData();

            // Toast.makeText(this, "choose uri is  "+uri2, Toast.LENGTH_SHORT).show();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri3);
                uri3 = getImageUri(getApplicationContext(), bitmap);
                imageView2.setImageBitmap(bitmap);
                 select2.setText("selected(2)");
                //select.setTextColor(Color.rgb(0, 150, 0));
            } catch (IOException e) {
                e.printStackTrace();
            }

        }


        else if (requestCode == PDF_REQ_CODE3 && resultCode == RESULT_OK) {
        /*
            uri2 = data.getData();
            // Toast.makeText(this, "camera uri is "+uri2, Toast.LENGTH_SHORT).show();
            // bitmap = (Bitmap) data.getExtras().get("data");
            imageView.setImageBitmap(bitmap);
            select.setText("Seleted");
            select.setTextColor(Color.rgb(0, 150, 0));
            uri2 = getImageUri(getApplicationContext(), bitmap);
            Toast.makeText(this, "usri is " + uri2, Toast.LENGTH_SHORT).show();
*/
            bitmap = null;
            try {
                bitmap=  MediaStore.Images.Media.getBitmap(getContentResolver(), uri2);
                imageView.setImageBitmap(bitmap);
                //select.setText("Seleted");
                //select.setTextColor(Color.rgb(0, 150, 0));
                uri2=getImageUri2(getApplicationContext(),bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 30, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }
    public Uri getImageUri2(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 70, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }


    public String getRealPathFromURI(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);
    }


    public void PdfUploadFunction() {

        PdfNameHolder = PdfNameEditText.getText().toString();

        PdfPathHolder = FilePath.getPath(this, uri);
        PdfPathHolder2 = FilePath.getPath(this, uri2);
        ImagePathHolder3 = FilePath.getPath(this, uri3);

        if (PdfPathHolder == null && PdfPathHolder2 == null) {


            Toast.makeText(this, "Please move your audio file to internal storage & try again.", Toast.LENGTH_LONG).show();

        }

        else
        {

            try {
                // Toast.makeText(this, ""+PdfPathHolder+PdfPathHolder2+PdfNameHolder+type_id+admin_id, Toast.LENGTH_SHORT).show();
                PdfID = UUID.randomUUID().toString();

                new MultipartUploadRequest(this, PdfID, PDF_UPLOAD_HTTP_URL)
                        .addFileToUpload(PdfPathHolder, "pdf")
                        .addFileToUpload(PdfPathHolder2, "image")
                        .addFileToUpload(ImagePathHolder3, "image2")
                        .addParameter("name", PdfNameHolder)
                        .addParameter("type_id", type_id)
                        .addParameter("admin_id", admin_id)
                        .setNotificationConfig(new UploadNotificationConfig())
                        .setMaxRetries(5)
                        .setUtf8Charset()
                        .startUpload();

            } catch (Exception exception) {

                Toast.makeText(this, exception.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void AllowRunTimePermission() {

        if (ActivityCompat.shouldShowRequestPermissionRationale(Long_Speed_Test.this, Manifest.permission.READ_EXTERNAL_STORAGE)) {

            Toast.makeText(Long_Speed_Test.this, "READ_EXTERNAL_STORAGE permission Access Dialog", Toast.LENGTH_LONG).show();

        } else {

            ActivityCompat.requestPermissions(Long_Speed_Test.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);

        }
    }

    @Override
    public void onRequestPermissionsResult(int RC, String per[], int[] PResult) {

        switch (RC) {

            case 1:

                if (PResult.length > 0 && PResult[0] == PackageManager.PERMISSION_GRANTED) {

                    // Toast.makeText(Add_Word_Copy.this, "Permission Granted, Now your application can access CAMERA.", Toast.LENGTH_LONG).show();

                } else {

                    // Toast.makeText(Add_Word_Copy.this, "Permission Canceled, Now your application cannot access CAMERA.", Toast.LENGTH_LONG).show();

                }
                break;
        }
    }


    public void EnableRuntimePermission() {

        if (ActivityCompat.shouldShowRequestPermissionRationale(Long_Speed_Test.this,
                Manifest.permission.CAMERA)) {

            Toast.makeText(Long_Speed_Test.this, "CAMERA permission allows us to Access CAMERA app", Toast.LENGTH_LONG).show();

        } else {

            ActivityCompat.requestPermissions(Long_Speed_Test.this, new String[]{
                    Manifest.permission.CAMERA}, 1);

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
    public void alert_online() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
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

}

