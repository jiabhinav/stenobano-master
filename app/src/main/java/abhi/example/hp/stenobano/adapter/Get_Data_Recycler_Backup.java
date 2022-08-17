            package abhi.example.hp.stenobano.adapter;

            import android.annotation.TargetApi;
            import android.app.ProgressDialog;
            import android.content.Context;
            import android.content.Intent;
            import android.database.Cursor;
            import android.database.sqlite.SQLiteDatabase;
            import android.os.AsyncTask;
            import android.os.Build;
            import android.util.Log;
            import android.view.LayoutInflater;
            import android.view.View;
            import android.view.ViewGroup;
            import android.view.WindowManager;
            import android.widget.Filter;
            import android.widget.Filterable;
            import android.widget.ImageView;
            import android.widget.ProgressBar;
            import android.widget.RelativeLayout;
            import android.widget.TextView;
            import android.widget.Toast;

            import androidx.annotation.RequiresApi;
            import androidx.recyclerview.widget.RecyclerView;

            import com.android.volley.DefaultRetryPolicy;
            import com.android.volley.Request;
            import com.android.volley.RequestQueue;
            import com.android.volley.Response;
            import com.android.volley.VolleyError;
            import com.android.volley.toolbox.HurlStack;
            import com.android.volley.toolbox.Volley;
            import com.squareup.picasso.Picasso;

            import java.io.BufferedInputStream;
            import java.io.ByteArrayInputStream;
            import java.io.IOException;
            import java.io.InputStream;
            import java.io.OutputStream;
            import java.net.URL;
            import java.net.URLConnection;
            import java.util.ArrayList;
            import java.util.List;

            import abhi.example.hp.stenobano.Interface.AlertDialogInterFace;
            import abhi.example.hp.stenobano.R;
            import abhi.example.hp.stenobano.config.Base_Url;
            import abhi.example.hp.stenobano.model.Helper_Model;
            import abhi.example.hp.stenobano.model.Search_Model;
            import abhi.example.hp.stenobano.player.LongPlayAudioAndImage;
            import abhi.example.hp.stenobano.player.PlayAudioAndImageView;
            import abhi.example.hp.stenobano.sqlite_databse.DatabaseHandler;
            import abhi.example.hp.stenobano.user.Get_Data;
            import abhi.example.hp.stenobano.user.Get_Long_speed_Test;
            import abhi.example.hp.stenobano.user.InputStreamVolleyRequest;

            /**
             * Created by hp on 9/29/2017.
             */

             public class Get_Data_Recycler_Backup extends RecyclerView.Adapter<Get_Data_Recycler_Backup.HolderItem> implements Filterable {

                private List<Search_Model> userModelArrayList;
                Context context;
                String pid;
                Get_Data get_data;
                private ProgressDialog pDialog;
                public static final int progress_bar_type = 0;
                public  static  String type="";
                DatabaseHandler db;
                OutputStream output;
                AlertDialogInterFace alertDialogInterFace;
                ProgressDialog progressDialog;
                String image_name="",image_name2="",audio_name="",title="";


                private static final String DATABASE_NAME = "steno";
                private static final String TABLE_CONTACTS = "steno_file";

                private static final String KEY_ID = "id";
                private static final String KEY_IMAGE = "image";
                private static final String KEY_AUDIO = "audio";
               private ArrayList<Search_Model> arraylist;

                public Get_Data_Recycler_Backup(List<Search_Model> mlistItem, Context context, AlertDialogInterFace alertDialogInterFace) {
                    this.userModelArrayList = mlistItem;
                    this.context = context;
                    this.alertDialogInterFace=alertDialogInterFace;

                }

                @Override
                public HolderItem onCreateViewHolder(ViewGroup parent, int viewType) {
                    View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.get_data_list, parent, false);
                    HolderItem holder = new HolderItem(layout);
                    return holder;

                }

                @Override
                public void onBindViewHolder(HolderItem holder, final int position) {
                    final Search_Model model = userModelArrayList.get(position);

                    Picasso.with(context)
                            .load(model.getImage_name())
                            .placeholder(R.drawable.preview)
                            .resize(60, 60)
                            .centerCrop()
                            .into(holder.imageView);



                    holder.title.setText(model.getTitle());
                    holder.date.setText(model.getDate());
                    pid = model.getId();
                   holder.play_online.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            for (int i = 0; i < userModelArrayList.size(); i++) {
                                if (userModelArrayList.get(i).getId().equals(pid)) {
                                    //Toast.makeText(context, " ID is " + userModelArrayList.get(position).getDescription(), Toast.LENGTH_SHORT).show();
                                 int p=i;
                                  if (model.getType().equals("long_speed"))
                                  {

                                     // Intent intent=new Intent(context, Long_Play_View_Speed_Test.class);
                                      Intent intent=new Intent(context, LongPlayAudioAndImage.class);

                                      intent.putExtra("title",userModelArrayList.get(position).getTitle());
                                      intent.putExtra("audio",userModelArrayList.get(position).getAudio());
                                      intent.putExtra("image",userModelArrayList.get(position).getImage_name());
                                      intent.putExtra("image2",userModelArrayList.get(position).getImage_name_two());
                                      intent.putExtra("pos",i);
                                      intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                      context.startActivity(intent);
                                  }
                                  else if (model.getType().equals("short_speed"))
                                  {

                                    //Intent intent=new Intent(context, Play_and_Download.class);
                                      //Intent intent=new Intent(context, Play_Audio_Details.class);
                                      Intent intent=new Intent(context, PlayAudioAndImageView.class);
                                    intent.putExtra("title",userModelArrayList.get(position).getTitle());
                                    intent.putExtra("audio",userModelArrayList.get(position).getAudio());
                                    intent.putExtra("image",userModelArrayList.get(position).getImage_name());
                                    intent.putExtra("pos",i);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    context.startActivity(intent);

                                  }

                                }

                            }

                        }

                    });
                   holder.download.setOnClickListener(new View.OnClickListener() {
                       @Override
                       public void onClick(View v) {
                           for (int i = 0; i < userModelArrayList.size(); i++) {
                               if (userModelArrayList.get(i).getId().equals(pid)) {


                                   if (model.getType().equals("short_speed"))
                                   {
                                       type="short_speed";

                                       image_name=userModelArrayList.get(position).getImage();
                                       image_name2=userModelArrayList.get(position).getImage_two();
                                       audio_name=userModelArrayList.get(position).getAudio_name();
                                       title=userModelArrayList.get(position).getTitle();

                                       alertDialogInterFace.Alert();
                                       get( audio_name,image_name,image_name2);
                                       try{
                                           Get_Data.text3.setText("2");
                                       }
                                       catch ( NullPointerException e)
                                       {

                                       }

                                   }
                                   else
                                   {

                                       type="long_speed";
                                       image_name=userModelArrayList.get(position).getImage();
                                       image_name2=userModelArrayList.get(position).getImage_two();
                                       audio_name=userModelArrayList.get(position).getAudio_name();
                                       title=userModelArrayList.get(position).getTitle();
                                       //getLong_test(image_name);
                                       get( audio_name,image_name,image_name2);
                                      // Get_Data.text3.setText("3");
                                      // get_data.alertDialog();
                                       alertDialogInterFace.Alert();
                                   }



                                  // Toast.makeText(context, "title is "+title, Toast.LENGTH_SHORT).show();





                               }

                               }
                       }
                   });
                }


                @RequiresApi(api = Build.VERSION_CODES.CUPCAKE)
                public String getT(String str){
                    String[] columns = new String[]{KEY_IMAGE};
            DatabaseHandler db=new DatabaseHandler(context);
            SQLiteDatabase sq=db.getReadableDatabase();
                    Cursor c = sq.query(TABLE_CONTACTS, new String[] {KEY_IMAGE }, KEY_IMAGE + "=?",
                            new String[] { String.valueOf(str) }, null, null, null, null);

                    if(c != null){

                        if (c.moveToFirst()) {
                            Toast.makeText(context, "Record Already Exists", Toast.LENGTH_SHORT).show();
                        } else {
                            String MY_URL = Base_Url.url+"uploads_images/"+image_name;
                            String MY_URL2 = Base_Url.url+"uploads_images/"+audio_name;
                           // Toast.makeText(context, " ID is " + userModelArrayList.get(position).getTitle(), Toast.LENGTH_SHORT).show();
                            String [] ur={MY_URL,MY_URL2};
                            new DownloadFileFromURL().execute(MY_URL,MY_URL2);
                            //Toast.makeText(context, "No record  is exists", Toast.LENGTH_SHORT).show();
                        }

                       // c.moveToFirst();
                        //String date = c.getString(2);
                        //return date;

                    } else {
                        Toast.makeText(context, "No record exists", Toast.LENGTH_SHORT).show();
                        //Log.d("Error", "No record exists");
                    }


                    return null;
                }



                @TargetApi(Build.VERSION_CODES.CUPCAKE)
                public String getLong_test(String str){
                    String[] columns = new String[]{KEY_IMAGE};
                    DatabaseHandler db=new DatabaseHandler(context);
                    SQLiteDatabase sq=db.getReadableDatabase();
                    Cursor c = sq.query(TABLE_CONTACTS, new String[] {KEY_IMAGE }, KEY_IMAGE + "=?",
                            new String[] { String.valueOf(str) }, null, null, null, null);

                    if(c != null){

                        if (c.moveToFirst()) {
                            Toast.makeText(context, "Record Already Exists", Toast.LENGTH_SHORT).show();
                        } else {
                            String MY_URL = Base_Url.url+"uploads_images/"+image_name;
                            String MY_URL2 = Base_Url.url+"uploads_images/"+image_name2;
                            String MY_URL3 = Base_Url.url+"uploads_images/"+audio_name;
                            // Toast.makeText(context, " ID is " + userModelArrayList.get(position).getTitle(), Toast.LENGTH_SHORT).show();
                           // String [] ur={MY_URL,MY_URL2,MY_URL3};
                            new DownloadLongFileFromURL().execute(MY_URL,MY_URL2,MY_URL3);
                            // Toast.makeText(context, "No record  is exists", Toast.LENGTH_SHORT).show();
                        }

                        // c.moveToFirst();
                        //String date = c.getString(2);
                        //return date;

                    } else {
                        Toast.makeText(context, "No record exists", Toast.LENGTH_SHORT).show();
                        //Log.d("Error", "No record exists");
                    }


                    return null;
                }





                @Override
                public int getItemCount() {
                    return userModelArrayList.size();
                }

                @Override
                public Filter getFilter() {
                    return new Filter() {
                        @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
                        @Override
                        protected FilterResults performFiltering(CharSequence charSequence) {

                            String charString = charSequence.toString();

                            if (charString.isEmpty()) {

                                arraylist = (ArrayList<Search_Model>) userModelArrayList;
                            } else {

                                ArrayList<Search_Model> filteredList = new ArrayList<>();

                                for (Search_Model androidVersion : userModelArrayList) {

                                    if (androidVersion.getTitle().toLowerCase().contains(charString) || androidVersion.getTitle().toLowerCase().contains(charString) || androidVersion.getName().toLowerCase().contains(charString)) {

                                        filteredList.add(androidVersion);
                                    }
                                }

                                arraylist = filteredList;
                            }

                            FilterResults filterResults = new FilterResults();
                            filterResults.values = arraylist;
                            return filterResults;
                        }

                        @Override
                        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                            arraylist = (ArrayList<Search_Model>) filterResults.values;
                            notifyDataSetChanged();
                        }
                    };
                }


                public void updateList(List<Search_Model> list){
                    userModelArrayList = list;
                    notifyDataSetChanged();
                }




                public class HolderItem extends RecyclerView.ViewHolder {

                    // ImageView thumbnal;
                    private  ImageView imageView,download,play_online;
                    private TextView title, description, code, price,date,audio;
                RelativeLayout relativeLayout;
                ProgressBar progressBar2;


                    public HolderItem(View convertView) {
                        super(convertView);
                        // thumbnal=(ImageView)v.findViewById(R.id.image_cover);
                        imageView = convertView.findViewById(R.id.image);
                        title = convertView.findViewById(R.id.title);
                        date = convertView.findViewById(R.id.datetext);
                        audio = convertView.findViewById(R.id.audio);
                        relativeLayout=convertView.findViewById(R.id.rl);
                        play_online=convertView.findViewById(R.id.play_online);
                        download=convertView.findViewById(R.id.download);
                        progressBar2=convertView.findViewById(R.id.progressBar2);





                    }

                }
                protected void dialog(int id) {
                    switch (id) {
                        case progress_bar_type: // we set this to 0
                            pDialog = new ProgressDialog(context);
                            pDialog.setMessage("Downloading file. Please wait...");
                            pDialog.setIndeterminate(false);
                            pDialog.setMax(100);
                            pDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                            pDialog.setCancelable(false);
                            pDialog.show();

                        default:

                    }
                }
                @RequiresApi(api = Build.VERSION_CODES.CUPCAKE)
                class DownloadFileFromURL extends AsyncTask<String, String, String> {

                    /**
                     * Before starting background thread
                     * Show Progress Bar Dialog
                     * */
                    @Override
                    protected void onPreExecute() {
                        super.onPreExecute();
                        dialog(progress_bar_type);
                    }

                    /**
                     * Downloading file in background thread
                     * */
                    @Override
                    protected String doInBackground(String... f_url) {
                        int count;
                        try {
                            for (int i = 0; i < 2; i++) {
                                URL url = new URL(f_url[i]);
                                //Toast.makeText(context, "lenght is "+f_url.length, Toast.LENGTH_SHORT).show();
                                URLConnection conection = url.openConnection();
                                conection.connect();
                                // this will be useful so that you can show a tipical 0-100% progress bar
                                int lenghtOfFile = conection.getContentLength();


                                // download the file
                                //InputStream input = new BufferedInputStream(url.openStream(), 8192);
                                BufferedInputStream input = new BufferedInputStream(url.openStream(),16334);

                                if (i == 0) {


                                   // output = new FileOutputStream("/sdcard/download/" + image_name);
                                    output = context.openFileOutput(image_name, Context.MODE_PRIVATE);

                                } else {
                                   // output = new FileOutputStream("/sdcard/download/" + audio_name);
                                    output = context.openFileOutput(audio_name, Context.MODE_PRIVATE);

                                }


                                //byte data[] = new byte[1024];
                                byte data[] = new byte[8192];

                                long total = 0;

                                while ((count = input.read(data)) != -1) {
                                    total += count;
                                    // publishing the progress....
                                    // After this onProgressUpdate will be called
                                    publishProgress("" + (int) ((total * 100) / lenghtOfFile));


                                    // writing data to file
                                   // output.write(data, 0, count);
                                    output.write(data, 0, count);

                                }

                                output.flush();
                                output.close();
                                input.close();
                            }
                        } catch(Exception e){
                            Log.e("Error: ", e.getMessage());
                        }

                        return null;
                    }


                    /**
                     * Updating progress bar
                     * */
                    protected void onProgressUpdate(String... progress) {
                        // setting progress percentage
                        pDialog.setProgress(Integer.parseInt(progress[0]));
                    }

                    /**
                     * After completing background task
                     * Dismiss the progress dialog
                     * **/
                    @Override
                    protected void onPostExecute(String file_url) {
                        // dismiss the dialog after the file was downloaded
                        pDialog.dismiss();



                        // Displaying downloaded image into image view
                        // Reading image path from sdcard
                       // String imagePath = Environment.getExternalStorageDirectory().toString()+"/download"+"/downloadedfile.jpg";
                        // setting downloaded into image view
                       // my_image.setImageDrawable(Drawable.createFromPath(imagePath));
                       // Toast.makeText(context, ""+imagePath, Toast.LENGTH_SHORT).show();
                        db = new DatabaseHandler(context);
                        Helper_Model helper_model=new Helper_Model();
                        helper_model.setImage(image_name);
                        helper_model.setImage_two(image_name2);

                        helper_model.setAudio(audio_name);
                        helper_model.setTitle(title);
                        db.add_Long_Contact(helper_model);
                        Toast.makeText(context, "File downloaded,Go to 'Offline File' option  ", Toast.LENGTH_LONG).show();

                     /*   List<Helper_Model> contacts = db.getAllContacts();
                        for (Helper_Model cn : contacts) {

                            String image = cn.getImage();
                            String name = cn.getAudio();
                          //  Toast.makeText(context, ""+image+name, Toast.LENGTH_SHORT).show();

                        }
            */


                        }

                    }


                @RequiresApi(api = Build.VERSION_CODES.CUPCAKE)
                class DownloadLongFileFromURL extends AsyncTask<String, String, String> {

                    /**
                     * Before starting background thread
                     * Show Progress Bar Dialog
                     * */
                    @Override
                    protected void onPreExecute() {
                        super.onPreExecute();
                        dialog(progress_bar_type);
                    }

                    /**
                     * Downloading file in background thread
                     * */
                    @Override
                    protected String doInBackground(String... f_url) {
                        int count;
                        try {
                            for (int i = 0; i < 3; i++) {
                                URL url = new URL(f_url[i]);
                                //Toast.makeText(context, "lenght is "+f_url.length, Toast.LENGTH_SHORT).show();
                                URLConnection conection = url.openConnection();
                                conection.connect();
                                // this will be useful so that you can show a tipical 0-100% progress bar
                                int lenghtOfFile = conection.getContentLength();

                                InputStream is = conection.getInputStream();
                                //InputStream input = new BufferedInputStream(url.openStream(), 8192);
                                BufferedInputStream input = new BufferedInputStream(is);


                                if (i == 0) {


                                    // output = new FileOutputStream("/sdcard/download/" + image_name);
                                    output = context.openFileOutput(image_name, Context.MODE_PRIVATE);


                                } else if (i == 1){
                                    // output = new FileOutputStream("/sdcard/download/" + audio_name);
                                    output = context.openFileOutput(image_name2, Context.MODE_PRIVATE);

                                }
                                else
                                {
                                    output = context.openFileOutput(audio_name, Context.MODE_PRIVATE);
                                }


                                byte data[] = new byte[1024];

                                long total = 0;

                                while ((count = input.read(data)) != -1) {
                                    total += count;
                                    // publishing the progress....
                                    // After this onProgressUpdate will be called
                                    publishProgress("" + (int) ((total * 100) / lenghtOfFile));

                                    // writing data to file
                                    output.write(data, 0, count);
                                }

                                // flushing output
                                output.flush();

                                // closing streams
                                output.close();
                                input.close();
                            }
                        } catch(Exception e){
                            Log.e("Error: ", e.getMessage());
                        }

                        return null;
                    }


                    /**
                     * Updating progress bar
                     * */
                    protected void onProgressUpdate(String... progress) {
                        // setting progress percentage
                        pDialog.setProgress(Integer.parseInt(progress[0]));
                    }

                    /**
                     * After completing background task
                     * Dismiss the progress dialog
                     * **/
                    @Override
                    protected void onPostExecute(String file_url) {
                        // dismiss the dialog after the file was downloaded
                        pDialog.dismiss();



                        // Displaying downloaded image into image view
                        // Reading image path from sdcard
                        // String imagePath = Environment.getExternalStorageDirectory().toString()+"/download"+"/downloadedfile.jpg";
                        // setting downloaded into image view
                        // my_image.setImageDrawable(Drawable.createFromPath(imagePath));
                        // Toast.makeText(context, ""+imagePath, Toast.LENGTH_SHORT).show();
                        db = new DatabaseHandler(context);
                        Helper_Model helper_model=new Helper_Model();
                        helper_model.setImage(image_name);
                        helper_model.setImage_two(image_name2);
                        helper_model.setAudio(audio_name);
                        helper_model.setTitle(title);
                        db.add_Long_Contact(helper_model);
                        Toast.makeText(context, "File downloaded,Go to 'Offline File' option  ", Toast.LENGTH_LONG).show();

                     /*   List<Helper_Model> contacts = db.getAllContacts();
                        for (Helper_Model cn : contacts) {

                            String image = cn.getImage();
                            String name = cn.getAudio();
                          //  Toast.makeText(context, ""+image+name, Toast.LENGTH_SHORT).show();

                        }
            */


                    }

                }

//==============================================================================================
                private void get(String audio_name,String image1,String image2) {


                    DatabaseHandler db = new DatabaseHandler(context);
                    SQLiteDatabase sq = db.getReadableDatabase();
                    Cursor c = sq.query(TABLE_CONTACTS, new String[]{KEY_IMAGE}, KEY_IMAGE + "=?",
                            new String[]{String.valueOf(image1)}, null, null, null, null);

                    if (c != null) {

                        if (c.moveToFirst()) {
                            Toast.makeText(context, "Record Already Exists", Toast.LENGTH_SHORT).show();
                            Get_Data.alertDialog.dismiss();
                        } else {


                            String MY_URL3 =Base_Url.url+"uploads_images/" + audio_name;
                            // String mUrl = "http://www.samveda.co.in/android/android_dictionary/uploads_images/256.mp3";
                            InputStreamVolleyRequest request = new InputStreamVolleyRequest(Request.Method.GET, MY_URL3,
                                    new Response.Listener<byte[]>() {
                                        @Override
                                        public void onResponse(byte[] response) {
                                            // TODO handle the response
                                            try {
                                                if (response != null) {

                                                    try {
                                                        int count;
                                                        long lenghtOfFile = response.length;
                                                        Log.d("filelenthis",lenghtOfFile+"");

                                                        InputStream input = new ByteArrayInputStream(response);
                                                        OutputStream output = context.openFileOutput(audio_name, Context.MODE_PRIVATE);
                                                        //byte data[] = new byte[1024];
                                                        byte data[] = new byte[1024];

                                                        long total = 0;

                                                        while ((count = input.read(data)) != -1) {
                                                            total += count;

                                                            output.write(data, 0, count);
                                                           int progress = (int) ((int)total*100/lenghtOfFile);
                                                           Get_Data.progressDialog.setProgress(progress);


                                                        }

                                                        Get_Data.progressDialog.dismiss();
                                                        output.flush();
                                                        output.close();
                                                        input.close();
                                                        Get_Data.text1.setText("1");
                                                        if (type.equals("short_speed")) {
                                                            getImage(image1);
                                                        } else {
                                                            getAduioTwo(audio_name, image1, image2);
                                                        }


                                                    } catch (IOException e) {
                                                        e.printStackTrace();

                                                    }
                                                }
                                            } catch (Exception e) {
                                                // TODO Auto-generated catch block
                                                Log.d("KEY_ERROR", "UNABLE TO DOWNLOAD FILE");
                                                e.printStackTrace();
                                            }


                                        }


                                    }, new Response.ErrorListener() {

                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    // TODO handle the error
                                    error.printStackTrace();
                                }
                            }, null);

                            RequestQueue mRequestQueue = Volley.newRequestQueue(context, new HurlStack());
                            request.setRetryPolicy(new DefaultRetryPolicy(
                                    240000,
                                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                            mRequestQueue.add(request);

                        }
                    }

                }

                private void getAduioTwo(String audio_name,String image1,String image2)
                {

                    String MY_URL3 =Base_Url.url+"uploads_images/"+image1;
                    // String mUrl = "http://www.samveda.co.in/android/android_dictionary/uploads_images/256.mp3";
                    InputStreamVolleyRequest request = new InputStreamVolleyRequest(Request.Method.GET, MY_URL3,
                            new Response.Listener<byte[]>() {
                                @Override
                                public void onResponse(byte[] response) {
                                    // TODO handle the response
                                    try {
                                        if (response!=null) {

                                            try{
                                                int count;
                                                long lenghtOfFile = response.length;

                                                InputStream input = new ByteArrayInputStream(response);

                                                OutputStream  output = context.openFileOutput(image1, Context.MODE_PRIVATE);
                                                //byte data[] = new byte[1024];
                                                byte data[] = new byte[1024];

                                                long total = 0;

                                                while ((count = input.read(data)) != -1) {
                                                    total += count;

                                                    // publishProgress("" + (int) ((total * 100) / lenghtOfFile));
                                                    // int  progress = (int)total*100/lenghtOfFile;
                                                    // onProgressUpdate(""+(int)total*100/lenghtOfFile);
                                                  //  long progres=(int)total*100/lenghtOfFile;

                                                  //  progressDialog.setProgress((int)progres);
                                                    //text.setText(String.valueOf((int)count));
                                                    output.write(data, 0, count);

                                                }

                                                output.flush();
                                                output.close();
                                                input.close();
                                                Get_Data.text1.setText("2");
                                                getImage(image2);
                                               /* db = new DatabaseHandler(context);
                                                Helper_Model helper_model=new Helper_Model();
                                                helper_model.setImage(image_name);
                                                helper_model.setImage_two(image_name2);

                                                helper_model.setAudio(audio_name);
                                                helper_model.setTitle(title);
                                                db.add_Long_Contact(helper_model);


                                                Toast.makeText(context, "Done", Toast.LENGTH_SHORT).show();
                                                       */


                                                //imageView.setImageResource(R.drawable.ic_file_download_green_24dp);

                                            }
                                            catch(IOException e){
                                                e.printStackTrace();

                                            }
                                        }
                                    } catch (Exception e) {
                                        // TODO Auto-generated catch block
                                        Log.d("KEY_ERROR", "UNABLE TO DOWNLOAD FILE");
                                        e.printStackTrace();
                                    }


                                }
                                class InnerClass {
                                    void methodTwo() {
                                    }
                                }


                            } ,new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError error) {
                            // TODO handle the error
                            error.printStackTrace();
                        }
                    }, null);

                    RequestQueue mRequestQueue = Volley.newRequestQueue(context, new HurlStack());
                    request.setRetryPolicy(new DefaultRetryPolicy(
                            240000,
                            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                    mRequestQueue.add(request);


                }
                //==========================================
                private void getImage(String image)
                {


                    String MY_URL = Base_Url.url+"uploads_images/"+image;
                   // String MY_URL = "http://www.samveda.co.in/android/android_dictionary/uploads_images/256.jpg";
                    // String mUrl = "http://www.samveda.co.in/android/android_dictionary/uploads_images/256.mp3";
                    InputStreamVolleyRequest request = new InputStreamVolleyRequest(Request.Method.GET, MY_URL,
                            new Response.Listener<byte[]>() {
                                @Override
                                public void onResponse(byte[] response) {
                                    // TODO handle the response
                                    try {
                                        if (response!=null) {

                                            try{
                                                int count;
                                                long lenghtOfFile = response.length;

                                                InputStream input = new ByteArrayInputStream(response);

                                                OutputStream  output = context.openFileOutput(image, Context.MODE_PRIVATE);
                                                //byte data[] = new byte[1024];
                                                byte data[] = new byte[1024];

                                                long total = 0;

                                                while ((count = input.read(data)) != -1) {
                                                    total += count;

                                                    // publishProgress("" + (int) ((total * 100) / lenghtOfFile));
                                                    // int  progress = (int)total*100/lenghtOfFile;
                                                    // onProgressUpdate(""+(int)total*100/lenghtOfFile);
                                                   // long progres=(int)total*100/lenghtOfFile;

                                                   // progressDialog.setProgress((int)progres);
                                                    //text.setText(String.valueOf((int)count));
                                                    output.write(data, 0, count);

                                                }


                                                output.flush();
                                                output.close();
                                                input.close();


                                                db = new DatabaseHandler(context);
                                                Helper_Model helper_model=new Helper_Model();
                                                helper_model.setImage(image_name);
                                                helper_model.setImage_two(image_name2);

                                                helper_model.setAudio(audio_name);
                                                helper_model.setTitle(title);
                                                db.add_Long_Contact(helper_model);

                                                Toast.makeText(context, "Download completed,Go to menu and choose 'Offline file'", Toast.LENGTH_LONG).show();
                                               // dialog.dismiss();
                                                if (type.equals("short_speed"))
                                                {
                                                    Get_Data.alertDialog.dismiss();

                                                }
                                                else
                                                {
                                                    Get_Long_speed_Test.alertDialog.dismiss();
                                                }


                                                //imageView.setImageResource(R.drawable.ic_file_download_green_24dp);

                                            }
                                            catch(IOException e){
                                                e.printStackTrace();

                                            }
                                        }
                                    } catch (Exception e) {
                                        // TODO Auto-generated catch block
                                        Log.d("KEY_ERROR", "UNABLE TO DOWNLOAD FILE");
                                        e.printStackTrace();
                                    }


                                }


                            } ,new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError error) {
                            // TODO handle the error
                            error.printStackTrace();
                        }
                    }, null);

                    RequestQueue mRequestQueue = Volley.newRequestQueue(context, new HurlStack());
                    request.setRetryPolicy(new DefaultRetryPolicy(
                            240000,
                            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                    mRequestQueue.add(request);

                }


                public void progresUpdater()
                {
                    progressDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
                    progressDialog = new ProgressDialog(context);
                    progressDialog.setTitle("Any Title here");
                    progressDialog.setMessage("Downloading in Progress...");
                    progressDialog.setProgressStyle(progressDialog.STYLE_HORIZONTAL);
                    progressDialog.setCancelable(false);
                    progressDialog.setMax(100);
                    progressDialog.setProgress(0);
                }


}



