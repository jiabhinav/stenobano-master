package abhi.example.hp.stenobano.adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import abhi.example.hp.stenobano.R;
import abhi.example.hp.stenobano.model.Search_Model;

/**
 * Created by hp on 9/29/2017.
 */

 public class Renewal_Recycler extends RecyclerView.Adapter<Renewal_Recycler.HolderItem> {

   private List<Search_Model> userModelArrayList;
    Context context;
    String pid;
    Search_Model model;
   private ArrayList<Search_Model> arraylist;

    public Renewal_Recycler(List<Search_Model> mlistItem, Context context) {
        this.userModelArrayList = mlistItem;
        this.context = context;
    }

    @Override
    public HolderItem onCreateViewHolder(ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.renewal_list, parent, false);
        HolderItem holder = new HolderItem(layout);

        return holder;

    }

    @Override
    public void onBindViewHolder(HolderItem holder, final int position) {
       model = userModelArrayList.get(position);
        //holder.index.setText(model.getIndex()+".");
        holder.mobile.setText(model.getMobile());
        holder.amount.setText(model.getAmount()+".00");
        holder.invoice.setText(model.getInvice());
        holder.email.setText(model.getEmail());
      String from=model.getFrom();
      String to=model.getTo();
      holder.valid.setText(from+" "+"to"+" "+to);
        pid = model.getId();
       holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < userModelArrayList.size(); i++) {
                    if (userModelArrayList.get(i).getId().equals(pid)) {
                       /* Intent intent=new Intent(context, Notification_details.class);
                        intent.putExtra("type",userModelArrayList.get(position).getJobb_type());
                        intent.putExtra("details",userModelArrayList.get(position).getDescription());
                        intent.putExtra("link",userModelArrayList.get(position).getJob_link());
                        intent.putExtra("date",userModelArrayList.get(position).getDate());

                        context.startActivity(intent);
                        */


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


        private TextView mobile,amount,valid,index,email,invoice;
     RelativeLayout relativeLayout;


        public HolderItem(View convertView) {
            super(convertView);
           // index=convertView.findViewById(R.id.index);
            mobile = convertView.findViewById(R.id.mobile);
            amount= convertView.findViewById(R.id.amount);
            valid = convertView.findViewById(R.id.valid);
            email = convertView.findViewById(R.id.email);
            invoice = convertView.findViewById(R.id.invoice);
            relativeLayout=convertView.findViewById(R.id.rl);



        }
    }
}