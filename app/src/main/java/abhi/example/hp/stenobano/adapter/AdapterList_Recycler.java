package abhi.example.hp.stenobano.adapter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.squareup.picasso.Picasso;


import java.util.ArrayList;
import java.util.List;


import abhi.example.hp.stenobano.R;
import abhi.example.hp.stenobano.user.ZoomLinearLayout;
import abhi.example.hp.stenobano.model.Search_Model;

/**
 * Created by hp on 9/29/2017.
 */

 public class AdapterList_Recycler extends RecyclerView.Adapter<AdapterList_Recycler.HolderItem> {



   private List<Search_Model> userModelArrayList;
    Context context;
    String pid;
    ImageView imageView,cancel;
    TextView textView;

   private ArrayList<Search_Model> arraylist;

    public AdapterList_Recycler(List<Search_Model> mlistItem, Context context) {
        this.userModelArrayList = mlistItem;
        this.context = context;
    }

    @Override
    public HolderItem onCreateViewHolder(ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.list, parent, false);
        HolderItem holder = new HolderItem(layout);

        return holder;

    }

    @Override
    public void onBindViewHolder(HolderItem holder, final int position) {
        Search_Model model = userModelArrayList.get(position);
        Glide.with(context).load(model.getImage()).thumbnail(0.5f).crossFade().diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.imageView);
        holder.title.setText(model.getTitle());
        //holder.description.setText(model.getDescription());
        pid = model.getId();
       holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public void onClick(View v) {
                for (int i = 0; i < userModelArrayList.size(); i++) {
                    if (userModelArrayList.get(i).getId().equals(pid)) {

                        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        LayoutInflater inflater = (LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );

                        builder.setCancelable(false);
                        final View view = inflater.inflate(R.layout.alert_view_details, null);
                        builder.setView(view);

                        textView=view.findViewById(R.id.title);
                        imageView=view.findViewById(R.id.alertimage);
                        textView.setText(userModelArrayList.get(position).getTitle());

                        Picasso.with(context)
                                .load(userModelArrayList.get(position).getImage())
                                .placeholder(R.drawable.steno_play_page)
                                .into(imageView);
                        final AlertDialog alertDialog = builder.create();
                       // alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        final ZoomLinearLayout zoomLinearLayout2 = view.findViewById(R.id.zoom);
                        zoomLinearLayout2.setOnTouchListener(new View.OnTouchListener()
                        {
                            @Override
                            public boolean onTouch(View v, MotionEvent event) {
                                zoomLinearLayout2.init(context);
                                return false;
                            }
                        });
                        builder.setNegativeButton("Back", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                               alertDialog.dismiss();
                            }
                        });
                        builder.show();






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

        // ImageView thumbnal;
        private ImageView imageView;
        private TextView title, description, code, price;
     RelativeLayout relativeLayout;


        public HolderItem(View convertView) {
            super(convertView);
            // thumbnal=(ImageView)v.findViewById(R.id.image_cover);
            imageView = convertView.findViewById(R.id.image);
            title = convertView.findViewById(R.id.title);
           // description = convertView.findViewById(R.id.discription);
          relativeLayout=convertView.findViewById(R.id.rl);





        }
    }
}