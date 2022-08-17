package abhi.example.hp.stenobano.adapter;

import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by hp on 25-Apr-18.
 */

class DownloadFile extends AsyncTask<String, Integer, String>
{

    @Override
    protected String doInBackground(String... Url) {
        try {
            URL url = new URL(Url[0]);
            URLConnection connection = url.openConnection();
            connection.connect();

            // Detect the file lenghth
            int fileLength = connection.getContentLength();

            // Locate storage location
            String filepath = Environment.getExternalStorageDirectory()
                    .getPath();

            // Download the file
            InputStream input = new BufferedInputStream(url.openStream());

            // Save the downloaded file
            OutputStream output = new FileOutputStream(filepath + "/"
                    +"download"+"/"+"steno_audio.mp3");

            byte data[] = new byte[1024];
            long total = 0;
            int count;
            while ((count = input.read(data)) != -1) {
                total += count;
                // Publish the progress
                publishProgress((int) (total * 100 / fileLength));
                output.write(data, 0, count);
            }

            // Close connection
            output.flush();
            output.close();
            input.close();
        } catch (Exception e) {
            // Error Log
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onProgressUpdate(Integer... progress) {
        super.onProgressUpdate(progress);
        // Update the progress dialog
        // Dismiss the progress dialog
        //mProgressDialog.dismiss();
    }
}



