package abhi.example.hp.stenobano.adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import abhi.example.hp.stenobano.R;
import abhi.example.hp.stenobano.model.Chat;
import abhi.example.hp.stenobano.model.Search_Model;

/**
 * Created by hp on 9/29/2017.
 */

 public class Notification_Adapter_Fcm extends RecyclerView.Adapter<Notification_Adapter_Fcm.HolderItem> {



   private List<Chat> userModelArrayList;
    Context context;
    String pid;

   private ArrayList<Search_Model> arraylist;

    public Notification_Adapter_Fcm(List<Chat> mlistItem, Context context) {
        this.userModelArrayList = mlistItem;
        this.context = context;
    }

    @Override
    public HolderItem onCreateViewHolder(ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.fcm_notification, parent, false);
        HolderItem holder = new HolderItem(layout);

        return holder;

    }

    @Override
    public void onBindViewHolder(HolderItem holder, final int position) {
        final Chat model = userModelArrayList.get(position);

        holder.title.setText("Title:"+model.getTitle());
        holder.description.setText("Description:"+model.getDescription());
        holder.date.setText(model.getDate());
    }

    @Override
    public int getItemCount() {
        return userModelArrayList.size();
    }



    public class HolderItem extends RecyclerView.ViewHolder {


        private TextView title,description,date;



        public HolderItem(View convertView) {
            super(convertView);
            // thumbnal=(ImageView)v.findViewById(R.id.image_cover);
            title = convertView.findViewById(R.id.title);
            description=convertView.findViewById(R.id.description);
            date=convertView.findViewById(R.id.date);




        }
    }
}