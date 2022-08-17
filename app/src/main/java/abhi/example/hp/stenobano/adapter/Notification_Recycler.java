package abhi.example.hp.stenobano.adapter;

import android.content.Context;
import android.content.Intent;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;
import java.util.List;

import abhi.example.hp.stenobano.R;
import abhi.example.hp.stenobano.model.Search_Model;
import abhi.example.hp.stenobano.user.View_Notificaon_image;

/**
 * Created by hp on 9/29/2017.
 */

 public class Notification_Recycler extends RecyclerView.Adapter<Notification_Recycler.HolderItem> {



   private List<Search_Model> userModelArrayList;
    Context context;
    String pid;

   private ArrayList<Search_Model> arraylist;

    public Notification_Recycler(List<Search_Model> mlistItem, Context context) {
        this.userModelArrayList = mlistItem;
        this.context = context;
    }

    @Override
    public HolderItem onCreateViewHolder(ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.notification_list, parent, false);
        HolderItem holder = new HolderItem(layout);

        return holder;

    }

    @Override
    public void onBindViewHolder(HolderItem holder, final int position) {
        final Search_Model model = userModelArrayList.get(position);

        holder.title.setText(model.getTitle());
        holder.date.setText(model.getDate());
        Glide.with(context).load(model.getImage()).thumbnail(0.5f).crossFade().diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.drawable.ic_image_black_24dp).into(holder.imageView);
        pid = model.getId();
       holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < userModelArrayList.size(); i++) {
                    if (userModelArrayList.get(i).getId().equals(pid)) {
                        Intent i2=new Intent(context, View_Notificaon_image.class);
                        i2.putExtra("title",model.getTitle());
                        i2.putExtra("url", model.getImage());
                        i2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(i2);





                    }

                }

            }


        });
    }

    @Override
    public int getItemCount() {
        return userModelArrayList.size();
    }

    /**
     * <p>Returns a filter that can be used to constrain data with a filtering
     * pattern.</p>
     * <p>
     * <p>This method is usually implemented by {@link Adapter}
     * classes.</p>
     *
     * @return a filter used to constrain data
     */
// Filter Class


    public class HolderItem extends RecyclerView.ViewHolder {


        private TextView title,date;
     RelativeLayout relativeLayout;
ImageView imageView;

        public HolderItem(View convertView) {
            super(convertView);
            // thumbnal=(ImageView)v.findViewById(R.id.image_cover);
            title = convertView.findViewById(R.id.title);
            date= convertView.findViewById(R.id.date);
            relativeLayout=convertView.findViewById(R.id.rl);
            imageView=convertView.findViewById(R.id.imageView);



        }
    }
}