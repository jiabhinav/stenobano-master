package abhi.example.hp.stenobano.other_class;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import abhi.example.hp.stenobano.config.Base_Url;
import abhi.example.hp.stenobano.model.Helper_Model;
import abhi.example.hp.stenobano.model.ModelCategoryDetail;
import abhi.example.hp.stenobano.sqlite_databse.DatabaseHandler;

public class DownloadFile
{
    Activity context;
    private ProgressDialog pDialog;
    // Progress dialog type (0 - for Horizontal progress bar)
    public static final int progress_bar_type = 0;
    ModelCategoryDetail.Result model;
    public DownloadFile(Activity context)
    {
        this.context=context;
    }


    protected void onCreateDialog() {
                pDialog = new ProgressDialog(context);
                pDialog.setMessage("Downloading file. Please wait...");
                pDialog.setIndeterminate(false);
                pDialog.setMax(100);
                pDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                pDialog.setCancelable(false);
                //pDialog.show();

    }

    public  void download(ModelCategoryDetail.Result model)
    {
        this.model=model;
        int size=model.getAudio().size()+model.getImage().size();
        Log.d("sizeisi",model.getAudio().size()+"-"+model.getImage().size());

        String[] str = new String[size];
        for (int i=0;i<model.getAudio().size();i++)
        {
            str[i]= model.getAudio().get(i).getName();
        }

        for (int i=0;i<model.getImage().size();i++)
        {
            str[i+model.getAudio().size()]=model.getImage().get(i).getName();
        }

        onCreateDialog();

        /*String[] ur = {
                "http://www.funrocker.com/blog/wp-content/uploads/2010/04/Animals-Wild-Life-Jungle-FunRocker.Com-03.jpg",
                "http://2.bp.blogspot.com/-j56yaqpfjVE/TnzTjcqnCjI/AAAAAAAAGPM/MzqmczFkC30/s1600/natural-pictures.jpg",
                "http://www.fantasy-and-art.com/wp-content/gallery/nature-wallpapers/red-tree-wallpaper-hd.jpg" };*/
        new DownloadFileFromURL().execute(str);

    }

  public void showDialog()
    {
        pDialog.show();
    }



    class DownloadFileFromURL extends AsyncTask<String, Integer, String> {

        /**
         * Before starting background thread Show Progress Bar Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showDialog();
        }

        /**
         * Downloading file in background thread
         * */
        @Override
        protected String doInBackground(String... f_url) {
            int count;
            try {

                for (int i = 0; i < f_url.length; i++) {
                    URL url = new URL(Base_Url.url+"uploads_images/"+f_url[i]);
                    URLConnection conection = url.openConnection();
                    conection.connect();
                    // getting file length
                    int lenghtOfFile = conection.getContentLength();

                    // input stream to read file - with 8k buffer

                    Log.d("asaddadsd",f_url[i]);
                    InputStream input = new BufferedInputStream(
                            url.openStream(), 8192);

                    OutputStream output = context.openFileOutput(f_url[i], Context.MODE_PRIVATE);

                  //  OutputStream output = new FileOutputStream("/sdcard/downloaded" + i + ".jpg");

                    byte data[] = new byte[1024];

                    long total = 0;

                    while ((count = input.read(data)) != -1) {
                        total += count;
                        // publishing the progress....
                        // After this onProgressUpdate will be called
                        publishProgress((int) ((total * 100)/lenghtOfFile));

                        // writing data to file
                        output.write(data, 0, count);
                    }

                    // flushing output
                    output.flush();

                    // closing streams
                    output.close();
                    input.close();
                }
            } catch (Exception e) {
                Log.e("Error: ", e.getMessage());
            }
            return null;
        }

        /**
         * Updating progress bar
         * */
        protected void onProgressUpdate(Integer... progress) {
            // setting progress percentage
            pDialog.setProgress(progress[0]);
        }

        /**
         * After completing background task Dismiss the progress dialog
         * **/
        @Override
        protected void onPostExecute(String file_url) {
            // dismiss the dialog after the file was downloaded
            dismissDialog(progress_bar_type);

            Log.d("dwdwdwd",file_url+"");
            // Displaying downloaded image into image view
            // Reading image path from sdcard
           /* String imagePath = Environment.getExternalStorageDirectory()
                    .toString() + "/downloaded.jpg";
            Log.d("dsdddwqq",imagePath);*/
            // setting downloaded into image view
            // my_image.setImageDrawable(Drawable.createFromPath(imagePath));

            DatabaseHandler db = new DatabaseHandler(context);
            db.addContact(model);
            db.addAudio(model);
            db.addImage(model);

            Log.d("sizsisis",db.getAllContacts().size()+"");
            Toast.makeText(context, "File downloaded,Go to 'Offline File' option  ", Toast.LENGTH_LONG).show();


        }

        private void dismissDialog(int progress_bar_type) {
            pDialog.dismiss();
        }

    }

}
