package abhi.example.hp.stenobano.adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import abhi.example.hp.stenobano.R;
import abhi.example.hp.stenobano.model.Search_Model;

/**
 * Created by hp on 9/29/2017.
 */

 public class SendRequest_Adapter extends RecyclerView.Adapter<SendRequest_Adapter.HolderItem> {



   private List<Search_Model> userModelArrayList;
    Context context;
    String pid;
    ImageView imageView,cancel;
    TextView textView;

   private ArrayList<Search_Model> arraylist;

    public SendRequest_Adapter(List<Search_Model> mlistItem, Context context) {
        this.userModelArrayList = mlistItem;
        this.context = context;
    }

    @Override
    public HolderItem onCreateViewHolder(ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_request, parent, false);
        HolderItem holder = new HolderItem(layout);

        return holder;

    }

    @Override
    public void onBindViewHolder(HolderItem holder, final int position) {
        Search_Model model = userModelArrayList.get(position);

        holder.reason.setText(model.getReason());
        holder.date.setText(model.getDate());
        holder.status.setText(model.getStatus());
        pid = model.getId();

    }

    @Override
    public int getItemCount() {
        return userModelArrayList.size();
    }



    public class HolderItem extends RecyclerView.ViewHolder {

        // ImageView thumbnal;
        private ImageView imageView;
        private TextView reason, date, status;
     RelativeLayout relativeLayout;


        public HolderItem(View convertView) {
            super(convertView);
            // thumbnal=(ImageView)v.findViewById(R.id.image_cover);
            reason = convertView.findViewById(R.id.reason);
            date = convertView.findViewById(R.id.date);
            status=convertView.findViewById(R.id.status);





        }
    }
}