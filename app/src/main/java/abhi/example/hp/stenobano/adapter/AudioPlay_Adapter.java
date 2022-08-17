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
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

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
import abhi.example.hp.stenobano.Interface.SetSpeed;
import abhi.example.hp.stenobano.R;
import abhi.example.hp.stenobano.config.Base_Url;
import abhi.example.hp.stenobano.model.Helper_Model;
import abhi.example.hp.stenobano.model.ModelCategoryDetail;
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

public class AudioPlay_Adapter extends RecyclerView.Adapter<AudioPlay_Adapter.HolderItem> {

    private List<ModelCategoryDetail.Result> userModelArrayList;
    Context context;
    SetSpeed speed;
    public AudioPlay_Adapter(List<ModelCategoryDetail.Result> mlistItem, Context context,SetSpeed setSpeed) {
        this.userModelArrayList = mlistItem;
        this.context = context;
        this.speed=setSpeed;
    }

    @Override
    public HolderItem onCreateViewHolder(ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_play_audio, parent, false);
        HolderItem holder = new HolderItem(layout);
        return holder;

    }

    @Override
    public void onBindViewHolder(HolderItem holder, final int position) {
        final ModelCategoryDetail.Result model = userModelArrayList.get(position);
        final ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(context,model.getImage());
        holder.viewPager.setAdapter(viewPagerAdapter);

       /* LinearLayoutManager linearLayoutManager=new LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false);
        holder.recyclerView.setLayoutManager(linearLayoutManager);
        SlidingImageAdapter adapter=new SlidingImageAdapter(model.getImage(),context);
        holder.recyclerView.setAdapter(adapter);*/
        holder.speed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                speed.speed();
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



    public class HolderItem extends RecyclerView.ViewHolder {


        private ViewPager viewPager;
        private RecyclerView recyclerView;
        private Button speed;
        public HolderItem(View convertView) {
            super(convertView);
            viewPager = convertView.findViewById(R.id.viewpager);
            speed=convertView.findViewById(R.id.speed);
            recyclerView=convertView.findViewById(R.id.recyclerView);

        }

    }




}



