package abhi.example.hp.stenobano.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import java.util.List;

import abhi.example.hp.stenobano.Interface.SetSpeed;
import abhi.example.hp.stenobano.R;
import abhi.example.hp.stenobano.model.Helper_Model;
import abhi.example.hp.stenobano.model.ModelCategoryDetail;

/**
 * Created by hp on 9/29/2017.
 */

public class OfflineAudioPlay_Adapter extends RecyclerView.Adapter<OfflineAudioPlay_Adapter.HolderItem> {

    private List<Helper_Model> userModelArrayList;
    Context context;
    SetSpeed speed;
    public OfflineAudioPlay_Adapter(List<Helper_Model> mlistItem, Context context, SetSpeed setSpeed) {
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
        final Helper_Model model = userModelArrayList.get(position);
        final OfflineViewPagerAdapter viewPagerAdapter = new OfflineViewPagerAdapter(context,model.getListImage());
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



