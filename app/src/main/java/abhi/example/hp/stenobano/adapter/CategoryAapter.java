package abhi.example.hp.stenobano.adapter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import abhi.example.hp.stenobano.Dashboard.User_Home;
import abhi.example.hp.stenobano.R;
import abhi.example.hp.stenobano.config.BaseUrl;
import abhi.example.hp.stenobano.model.ModelCategory;
import abhi.example.hp.stenobano.model.Search_Model;
import abhi.example.hp.stenobano.other_class.GetData;
import abhi.example.hp.stenobano.user.GetDataNew;
import abhi.example.hp.stenobano.user.PlayAudioImage;
import abhi.example.hp.stenobano.user.Type_Searching;
import abhi.example.hp.stenobano.user.ZoomLinearLayout;

/*
*
* Created by hp on 9/29/2017.
*/

 public class CategoryAapter extends RecyclerView.Adapter<CategoryAapter.HolderItem> {



   private List<ModelCategory.Result> userModelArrayList;
    Context context;
    String pid;
    ImageView imageView,cancel;
    TextView textView;

   private ArrayList<ModelCategory.Result> arraylist;

    public CategoryAapter(List<ModelCategory.Result> mlistItem, Context context) {
        this.userModelArrayList = mlistItem;
        this.context = context;
    }

    @Override
    public HolderItem onCreateViewHolder(ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_category, parent, false);
        HolderItem holder = new HolderItem(layout);

        return holder;

    }

    @Override
    public void onBindViewHolder(HolderItem holder, final int position) {
        ModelCategory.Result model = userModelArrayList.get(position);

        try
        {
            Picasso.with(context)
                    .load(BaseUrl.CATEGORY_URL +model.getImage())
                    .placeholder(R.drawable.preview)
                    .into(holder.image);
        }
        catch (Exception e)
        {

        }

        holder.name.setText(model.getName());
        pid = model.getId();
       holder.card.setOnClickListener(new View.OnClickListener()
       {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public void onClick(View v) {

                    if (model.getType().equals("Audio/Image"))
                    {
                        Intent intent=new Intent(context, GetDataNew.class);
                        intent.putExtra("type_id",model.getId());
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                    }
                    else if (model.getType().equals("Link"))
                    {
                       /* String url =model.getUrl();
                        Intent i = new Intent(Intent.ACTION_VIEW);
                        i.setData(Uri.parse(url));
                        context.startActivity(i);*/
                        String url = model.getUrl();
                        Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        String title = "Select a browser";
                        Intent chooser = Intent.createChooser(i, title);
                        i.setPackage("com.android.chrome");
                        try {
                            context.startActivity(chooser);
                        } catch (ActivityNotFoundException e) {
                            i.setData(Uri.parse(url));
                            context.startActivity(chooser);
                        }
                    }
                    else
                    {
                        Intent i = new Intent(context, Type_Searching.class);
                        i.putExtra("type_id", "101");
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(i);
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

    public class HolderItem extends RecyclerView.ViewHolder {

        // ImageView thumbnal;
        private ImageView image;
        private TextView name;
     CardView card;


        public HolderItem(View convertView) {
            super(convertView);
            image = convertView.findViewById(R.id.image);
            name = convertView.findViewById(R.id.name);
            card=convertView.findViewById(R.id.card);





        }
    }
}