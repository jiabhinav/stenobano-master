package abhi.example.hp.stenobano.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;

import java.util.ArrayList;
import java.util.List;

import abhi.example.hp.stenobano.R;
import abhi.example.hp.stenobano.model.Search_Model;
import abhi.example.hp.stenobano.user.Play_and_Download;

/**
 * Created by hp on 9/29/2017.
 */

 public class Search_Speet_Test_R_Audio_Recycler extends RecyclerView.Adapter<Search_Speet_Test_R_Audio_Recycler.HolderItem> {



   private List<Search_Model> userModelArrayList;
    Context context;
    String pid;
    RequestQueue requestQueue;
    ProgressDialog progressDialog;
    String job_id;

   private ArrayList<Search_Model> arraylist;

    public Search_Speet_Test_R_Audio_Recycler(List<Search_Model> mlistItem, Context context) {
        this.userModelArrayList = mlistItem;
        this.context = context;
    }

    @Override
    public HolderItem onCreateViewHolder(ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.speed_test_list, parent, false);
        HolderItem holder = new HolderItem(layout);

        return holder;

    }

    @Override
    public void onBindViewHolder(HolderItem holder, final int position) {
        Search_Model model = userModelArrayList.get(position);

        holder.type.setText(model.getName());
        holder.description.setText(model.getDescription());
        holder.date.setText(model.getDate());
        pid = model.getId();
        holder.update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < userModelArrayList.size(); i++) {
                    if (userModelArrayList.get(i).getId().equals(pid)) {
                        //Toast.makeText(context, " ID is " + userModelArrayList.get(position).getDescription(), Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(context, Play_and_Download.class);
                        intent.putExtra("id", userModelArrayList.get(position).getId());
                        intent.putExtra("file", userModelArrayList.get(position).getFile());
                        // Toast.makeText(context, ""+userModelArrayList.get(position).getFile(), Toast.LENGTH_SHORT).show();
                        // intent.putExtra("date",userModelArrayList.get(position).getDate());
                        context.startActivity(intent);


                    }

                }

            }


        });

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < userModelArrayList.size(); i++) {
                    if (userModelArrayList.get(i).getId().equals(pid)) {
                       // Toast.makeText(context, "" + userModelArrayList.get(position).getFile(), Toast.LENGTH_SHORT).show();
                         String url=userModelArrayList.get(position).getFile();
                        new DownloadFile().execute(url);
                        Toast.makeText(context, "File is downloading please wait....", Toast.LENGTH_LONG).show();
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


        private TextView type, description, code, price,date,update,delete;
     RelativeLayout relativeLayout;


        public HolderItem(View convertView) {
            super(convertView);
            // thumbnal=(ImageView)v.findViewById(R.id.image_cover);
            type = convertView.findViewById(R.id.job_type_text);
            date= convertView.findViewById(R.id.date);
            description = convertView.findViewById(R.id.discription);
            update = convertView.findViewById(R.id.updatetext);
            delete= convertView.findViewById(R.id.delete);
           // relativeLayout=convertView.findViewById(R.id.rl);



        }
    }
}