package abhi.example.hp.stenobano.adapter;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import abhi.example.hp.stenobano.Interface.Download;
import abhi.example.hp.stenobano.R;
import abhi.example.hp.stenobano.config.BaseUrl;
import abhi.example.hp.stenobano.config.Base_Url;
import abhi.example.hp.stenobano.model.Helper_Model;
import abhi.example.hp.stenobano.model.ModelCategoryDetail;
import abhi.example.hp.stenobano.model.Search_Model;
import abhi.example.hp.stenobano.other_class.DownloadFile;
import abhi.example.hp.stenobano.other_class.GetData;
import abhi.example.hp.stenobano.player.LongPlayAudioAndImage;
import abhi.example.hp.stenobano.player.PlayAudioAndImageView;
import abhi.example.hp.stenobano.sqlite_databse.DatabaseHandler;
import abhi.example.hp.stenobano.user.Get_Data;
import abhi.example.hp.stenobano.user.Get_Long_speed_Test;
import abhi.example.hp.stenobano.user.InputStreamVolleyRequest;
import abhi.example.hp.stenobano.user.PlayAudioImage;

import static abhi.example.hp.stenobano.sqlite_databse.DatabaseHandler.CAT_ID;

/**
 * Created by hp on 9/29/2017.
 */

public class CategoryDetailsAdapter extends RecyclerView.Adapter<CategoryDetailsAdapter.HolderItem> implements Filterable {

    private List<ModelCategoryDetail.Result> userModelArrayList;
    Context context;
    String pid;
    public  static  String type="";
    AlertDialogInterFace alertDialogInterFace;


    private static final String TABLE_CONTACTS = "steno_file";

    private ArrayList<ModelCategoryDetail.Result> arraylist;
    private int pStatus = 0;
    Download download;
    public CategoryDetailsAdapter(List<ModelCategoryDetail.Result> mlistItem, Context context, AlertDialogInterFace alertDialogInterFace, Download download) {
        this.userModelArrayList = mlistItem;
        this.context = context;
        this.alertDialogInterFace=alertDialogInterFace;
        this.download=download;
    }

    @Override
    public HolderItem onCreateViewHolder(ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.get_data_list, parent, false);
        HolderItem holder = new HolderItem(layout);
        return holder;

    }

    @Override
    public void onBindViewHolder(HolderItem holder, final int position) {
        final ModelCategoryDetail.Result model = userModelArrayList.get(position);

        try
        {
            Picasso.with(context)
                    .load(BaseUrl.IMAGE_URL+model.getImage().get(0).getName())
                    .placeholder(R.drawable.preview)
                    .resize(60, 60)
                    .centerCrop()
                    .into(holder.imageView);
        }
        catch (IndexOutOfBoundsException e)
        {

        }
        holder.title.setText(model.getTitle());
        holder.date.setText(model.getCreatedAt());
        pid = model.getId();
        if (checkExistValue(model.getId()))
        {
            holder.rl_download.setVisibility(View.VISIBLE);
            holder.progressBar2.setProgress(100);
            holder.text_download.setText("100%");
            holder.status.setText("Downloaded");
        }
        else
        {
            holder.progressBar2.setProgress(0);
            holder.rl_download.setVisibility(View.INVISIBLE);
            holder.text_download.setText("0%");
            holder.status.setText("Downloading...");
        }

        holder.play_online.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                        GetData.modelAllDetail=userModelArrayList;
                        Intent intent=new Intent(context, PlayAudioImage.class);
                        intent.putExtra("pos",position);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);

            }

        });
        holder.download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GetData.modelDetail = model;
                DatabaseHandler db = new DatabaseHandler(context);
                SQLiteDatabase sq = db.getReadableDatabase();
                Cursor c = sq.query(TABLE_CONTACTS, new String[]{CAT_ID}, CAT_ID + "=?",
                        new String[]{String.valueOf(model.getId())}, null, null, null, null);

                if (c != null) {
                    if (c.moveToFirst()) {
                        Toast.makeText(context, "Record Already Exists", Toast.LENGTH_SHORT).show();
                    } else {
                        download.downloadfile(model);
                    }

                }
            }
        });
        }



    @Override
    public int getItemCount() {
        return userModelArrayList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {

                String charString = charSequence.toString();

                if (charString.isEmpty()) {

                    arraylist = (ArrayList<ModelCategoryDetail.Result>) userModelArrayList;
                } else {

                    ArrayList<ModelCategoryDetail.Result> filteredList = new ArrayList<>();

                    for (ModelCategoryDetail.Result androidVersion : userModelArrayList) {

                        if (androidVersion.getTitle().toLowerCase().contains(charString) || androidVersion.getTitle().toLowerCase().contains(charString) || androidVersion.getTitle().toLowerCase().contains(charString)) {

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
                arraylist = (ArrayList<ModelCategoryDetail.Result>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }


    public void updateList(List<ModelCategoryDetail.Result> list){
        userModelArrayList = list;
        notifyDataSetChanged();
    }




    public class HolderItem extends RecyclerView.ViewHolder {

        // ImageView thumbnal;
        private  ImageView imageView,download,play_online;
        private TextView title, description, code, price,date,audio,text_download,status;
        RelativeLayout relativeLayout,rl_download;
        ProgressBar progressBar2;


        public HolderItem(View convertView) {
            super(convertView);
            imageView = convertView.findViewById(R.id.image);
            title = convertView.findViewById(R.id.title);
            date = convertView.findViewById(R.id.datetext);
            audio = convertView.findViewById(R.id.audio);
            relativeLayout=convertView.findViewById(R.id.rl2);
            play_online=convertView.findViewById(R.id.play_online);
            download=convertView.findViewById(R.id.download);
            progressBar2=convertView.findViewById(R.id.progressBar2);
            text_download=convertView.findViewById(R.id.text_download);
            rl_download=convertView.findViewById(R.id.rl_download);
            status=convertView.findViewById(R.id.status);

        }

    }




    private boolean checkExistValue(String image) {
        boolean exist=false;
        DatabaseHandler db = new DatabaseHandler(context);
        SQLiteDatabase sq = db.getReadableDatabase();
        Cursor c = sq.query(TABLE_CONTACTS, new String[]{CAT_ID}, CAT_ID + "=?",
                new String[]{String.valueOf(image)}, null, null, null, null);

        if (c != null) {
            if (c.moveToFirst()) {
                exist=true;
            } else {
                exist=false;
            }
        }
        return exist;
    }



}



