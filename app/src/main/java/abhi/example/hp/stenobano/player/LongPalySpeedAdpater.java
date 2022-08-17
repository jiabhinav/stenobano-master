package abhi.example.hp.stenobano.player;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.toolbox.ImageLoader;

import java.util.List;

import abhi.example.hp.stenobano.R;

public class LongPalySpeedAdpater extends  RecyclerView.Adapter<LongPalySpeedAdpater.ViewHolder> {
    Context context;
    String sp;
    List<Float> getDataAdapter;

    ImageLoader imageLoader1;
    String subject;
    float speed;

    public LongPalySpeedAdpater(List<Float> getDataAdapter, Context context, float speed){

        super();
        this.getDataAdapter = getDataAdapter;
        this.context = context;
        this.speed=speed;


    }

    @Override
    public LongPalySpeedAdpater.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_speed, parent, false);

        LongPalySpeedAdpater.ViewHolder viewHolder = new LongPalySpeedAdpater.ViewHolder(v);




        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final LongPalySpeedAdpater.ViewHolder Viewholder, final int position) {


        Log.d("cscssdadaq",getDataAdapter.get(position)+"");
        if (getDataAdapter.get(position)==1f)
        {
            if (speed==getDataAdapter.get(position))
            {
                Viewholder.image.setVisibility(View.VISIBLE);
                Viewholder.title.setText("100 wpm(Normal)");
            }
            else
            {
                Viewholder.title.setText("100 wpm(Normal)");
            }

        }
        else
        {
            if (speed==getDataAdapter.get(position))
            {

                Viewholder.image.setVisibility(View.VISIBLE);;
               // Viewholder.title.setText(String.valueOf(getDataAdapter.get(position)));
                Viewholder.title.setText(getSpeed(String.valueOf(getDataAdapter.get(position))));

            }
            else
            {
               // Viewholder.title.setText(String.valueOf(getDataAdapter.get(position)));
                Viewholder.title.setText(getSpeed(String.valueOf(getDataAdapter.get(position))));
            }

        }


        Viewholder.title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (context instanceof LongPlayAudioAndImage) {
                    ((LongPlayAudioAndImage) context).setPlaySpeed(getDataAdapter.get(position));
                }
            }
        });
    }

    @Override
    public int getItemCount() {

        return getDataAdapter.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        public TextView title;
        private ImageView image;
        private LinearLayout linearLayout;
        public ViewHolder(View itemView) {

            super(itemView);
            title =  itemView.findViewById(R.id.f_25);
            linearLayout=itemView.findViewById(R.id.ll);
            image=itemView.findViewById(R.id.image);


        }

    }

    private String getSpeed(String speed)
    {

        switch (speed)
        {
            case "0.25":
            sp="25 wpm";
            break;

            case "0.5":
                sp="50 wpm";
                break;

            case "0.75":
                sp="75 wpm";
                break;


            case "1.0":
                sp="100 wpm";
                break;

            case "1.25":
                sp="125 wpm";
                break;

            case "1.5":
                sp="150 wpm";
                break;

            case "1.75":
                sp="175 wpm";
                break;

            case "2.0":
                sp="200 wpm";
                break;
        }
        return sp;
    }

}