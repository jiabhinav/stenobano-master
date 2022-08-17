package abhi.example.hp.stenobano.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import abhi.example.hp.stenobano.R;
import abhi.example.hp.stenobano.config.BaseUrl;
import abhi.example.hp.stenobano.model.ModelCategory;
import abhi.example.hp.stenobano.model.ModelCategoryDetail;
import abhi.example.hp.stenobano.user.GetDataNew;
import abhi.example.hp.stenobano.user.Type_Searching;

/*
*
* Created by hp on 9/29/2017.
*/

public class SlidingImageAdapter extends RecyclerView.Adapter<SlidingImageAdapter.HolderItem> {

  private List<ModelCategoryDetail.Image> userModelArrayList;
   Context context;

  private ArrayList<ModelCategory.Result> arraylist;

   public SlidingImageAdapter(List<ModelCategoryDetail.Image> mlistItem, Context context) {
       this.userModelArrayList = mlistItem;
       this.context = context;
   }

   @Override
   public HolderItem onCreateViewHolder(ViewGroup parent, int viewType) {
       View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_layout, parent, false);
       HolderItem holder = new HolderItem(layout);

       return holder;

   }

   @Override
   public void onBindViewHolder(HolderItem holder, final int position) {
       ModelCategoryDetail.Image model = userModelArrayList.get(position);

       try
       {
           Picasso.with(context)
                   .load(BaseUrl.IMAGE_URL+model.getName())
                   .placeholder(R.drawable.gradient_play_wall)
                   .error(R.drawable.gradient_play_wall)
                   .into(holder.imageView);
       }
       catch (Exception e)
       {

       }

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

       // ImageView thumbnal;
       private ImageView imageView;

       public HolderItem(View convertView) {
           super(convertView);
           imageView = convertView.findViewById(R.id.imageView);





       }
   }
}