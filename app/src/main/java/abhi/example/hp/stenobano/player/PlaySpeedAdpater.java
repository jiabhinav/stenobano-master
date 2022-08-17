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
import abhi.example.hp.stenobano.user.PlayAudioImage;
import abhi.example.hp.stenobano.user.PlayOfflineAudioImage;

public class PlaySpeedAdpater extends  RecyclerView.Adapter<PlaySpeedAdpater.ViewHolder> {
    Context context;
    String sp;
    List<Float> getDataAdapter;

    ImageLoader imageLoader1;
    String subject;
    float speed;
    boolean id=false;

    public PlaySpeedAdpater(List<Float> getDataAdapter, Context context,float speed,boolean id){
        super();
        this.getDataAdapter = getDataAdapter;
        this.context = context;
        this.speed=speed;
        this.id=id;
    }
    public PlaySpeedAdpater(List<Float> getDataAdapter, Context context,float speed){
        super();
        this.getDataAdapter = getDataAdapter;
        this.context = context;
        this.speed=speed;

    }

    @Override
    public PlaySpeedAdpater.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_speed, parent, false);

        PlaySpeedAdpater.ViewHolder viewHolder = new PlaySpeedAdpater.ViewHolder(v);




        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final PlaySpeedAdpater.ViewHolder Viewholder, final int position) {


        Log.d("cscssdadaq",getDataAdapter.get(position)+"");
        if (getDataAdapter.get(position)==1f)
        {
            if (speed==getDataAdapter.get(position))
            {
                Viewholder.image.setVisibility(View.VISIBLE);
                if (id)
                {
                    Viewholder.title.setText("10(Normal)");
                }
                else
                {
                    Viewholder.title.setText("100 wpm(Normal)");
                }

            }
            else
            {
                if (id)
                {
                    Viewholder.title.setText("10(Normal)");
                }
                else
                {
                    Viewholder.title.setText("100 wpm(Normal)");
                }
            }

        }
        else
        {
            if (speed==getDataAdapter.get(position))
            {

                Viewholder.image.setVisibility(View.VISIBLE);;
               // Viewholder.title.setText(String.valueOf(getDataAdapter.get(position)));

                if (id)
                {
                    Viewholder.title.setText(getSpeed2(String.valueOf(getDataAdapter.get(position))));
                }
                else
                {
                    Viewholder.title.setText(getSpeed(String.valueOf(getDataAdapter.get(position))));
                }

            }
            else {
                // Viewholder.title.setText(String.valueOf(getDataAdapter.get(position)));
                if (id)
                {
                    Viewholder.title.setText(getSpeed2(String.valueOf(getDataAdapter.get(position))));
                }
                else
                {
                    Viewholder.title.setText(getSpeed(String.valueOf(getDataAdapter.get(position))));
                }
            }

        }


        Viewholder.title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (context instanceof PlayAudioImage) {
                   // ((PlayAudioAndImageView) context).setPlaySpeed(getDataAdapter.get(position));
                    ((PlayAudioImage)context).setPlaySpeed(getDataAdapter.get(position));

                }
                else if (context instanceof PlayOfflineAudioImage)
                {
                    ((PlayOfflineAudioImage)context).setPlaySpeed(getDataAdapter.get(position));
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
            case "0.2":
            sp="20 wpm";
            break;
            case "0.3":
                sp="30 wpm";
                break;
            case "0.4":
                sp="40 wpm";
                break;
            case "0.5":
                sp="50 wpm";
                break;
            case "0.6":
                sp="60 wpm";
                break;
            case "0.7":
                sp="70 wpm";
                break;
            case "0.8":
                sp="80 wpm";
                break;
            case "0.9":
                sp="90 wpm";
                break;

            case "1.0":
                sp="100 wpm";
                break;

            case "1.1":
                sp="110 wpm";
                break;
            case "1.2":
                sp="120 wpm";
                break;

            case "1.3":
                sp="130 wpm";
                break;
            case "1.4":
                sp="140 wpm";
                break;
            case "1.5":
                sp="150 wpm";
                break;
            case "1.6":
                sp="160 wpm";
                break;
            case "1.7":
                sp="170 wpm";
                break;

            case "1.8":
                sp="180 wpm";
                break;

            case "1.9":
                sp="190 wpm";
                break;

            case "2.0":
                sp="200 wpm";
                break;
        }
        return sp;
    }
    private String getSpeed2(String speed)
    {

        switch (speed)
        {
            case "0.2":
                sp="2";
                break;
            case "0.3":
                sp="3";
                break;
            case "0.4":
                sp="4";
                break;
            case "0.5":
                sp="5";
                break;
            case "0.6":
                sp="6";
                break;
            case "0.7":
                sp="7";
                break;
            case "0.8":
                sp="8";
                break;
            case "0.9":
                sp="9";
                break;

            case "1.0":
                sp="10";
                break;

            case "1.1":
                sp="11";
                break;

            case "1.2":
                sp="12";
                break;

            case "1.3":
                sp="13";
                break;
            case "1.4":
                sp="14";
                break;
            case "1.5":
                sp="15";
                break;
            case "1.6":
                sp="16";
                break;
            case "1.7":
                sp="17";
                break;

            case "1.8":
                sp="18";
                break;

            case "1.9":
                sp="19";
                break;

            case "2.0":
                sp="20";
                break;
        }
        return sp;
    }

}