package abhi.example.hp.stenobano.adapter;

import android.content.Context;
import android.content.Intent;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.RequestQueue;

import java.util.List;

import abhi.example.hp.stenobano.R;

import abhi.example.hp.stenobano.model.Category_Model;
import abhi.example.hp.stenobano.user.GetSearch_Result;
import abhi.example.hp.stenobano.user.Type_Searching;

public class Get_Search_Suggetion_Adapter extends RecyclerView.Adapter<Get_Search_Suggetion_Adapter.HolderItem> {

    private List<Category_Model> userModelArrayList;
    Context context;
    String pid,email;
    String p_id;

    RequestQueue requestQueue;
    private static final int DATABASE_VERSION = 1;

    // Database Name


    private static final String TABLE_CONTACTS = "search_history";

    // Contacts Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_BRAND = "brand";
    private static final String KEY_CATEGORY = "category";
    ImageView imageView,cancel;
    private TextView rank,name,cust_id,username,parent_alert,self_business,target;


    public Get_Search_Suggetion_Adapter(List<Category_Model> mlistItem, Context context) {
        this.userModelArrayList = mlistItem;
        this.context = context;
    }

    @Override
    public HolderItem onCreateViewHolder(ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.suggetion_list, parent, false);

        HolderItem holder = new HolderItem(layout);

        return holder;

    }

    @Override
    public void onBindViewHolder(final HolderItem holder, final int position) {
        final Category_Model model2 = userModelArrayList.get(position);

        //String image_name=model2.getImage_name();
        p_id=model2.getId();
                 holder.title.setText(model2.getName());

        holder.image_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i=0;i<userModelArrayList.size();i++) {
                    if (userModelArrayList.get(i).getId().equals(p_id)) {

                    Type_Searching.search.setText(model2.getName());

                }
                }

                }
            });
        holder.title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i=0;i<userModelArrayList.size();i++) {
                    if (userModelArrayList.get(i).getId().equals(p_id)) {

                Type_Searching.search.setText(model2.getName());
                Intent i2 = new Intent(context, GetSearch_Result.class);
                i2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                i2.putExtra("title", model2.getName());
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

    public class HolderItem extends RecyclerView.ViewHolder {


        private TextView price,title,description,discount;
       // ImageView cart;
       ImageView image_arrow,email;
        RelativeLayout relativeLayout;

        public HolderItem(View convertView) {
            super(convertView);

            title=convertView.findViewById(R.id.name);
            image_arrow=convertView.findViewById(R.id.image_arrow);
            email=convertView.findViewById(R.id.image_arrow);
            relativeLayout=convertView.findViewById(R.id.cardView);
        
        }
    }


}