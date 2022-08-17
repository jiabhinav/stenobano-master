package abhi.example.hp.stenobano.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;
import com.squareup.picasso.Picasso;

import java.util.List;

import abhi.example.hp.stenobano.R;
import abhi.example.hp.stenobano.model.Search_Model;

/**
 * Created by hp on 27-Jan-18.
 */

public class ImageSliderAdapter extends PagerAdapter {

    private Context context;
    public LayoutInflater layoutInflater;
    private List<Search_Model> sliderImg;
    private ImageLoader imageLoader;
    int rotationAngle=90;/*angle by which you want to rotate image*/
    int mCurrRotation = 0;
    public static ImageView imageView;
    public ImageSliderAdapter(List sliderImg, Context context) {
        this.sliderImg = sliderImg;
        this.context = context;
    }

    @Override
    public int getCount() {
        return sliderImg.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {

        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.banner_list_items, null);

        final Search_Model utils = sliderImg.get(position);

      imageView = view.findViewById(R.id.imageView);



            imageView.setImageDrawable(Drawable.createFromPath(utils.getImage()));


        Picasso.with(context).load(utils.getImage()).fit()
                .placeholder(R.drawable.steno_play_page)
                .error(R.drawable.ic_error_outline_black_24dp)
                .into(imageView);


        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(position == 0){
                    Toast.makeText(context, "Slide 1 ", Toast.LENGTH_SHORT).show();


                } else if(position == 1){
                    Toast.makeText(context, "Slide 2 ", Toast.LENGTH_SHORT).show();

                }

            }
        });

        ViewPager vp = (ViewPager) container;
        vp.addView(view, 0);
        return view;

    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {

        ViewPager vp = (ViewPager) container;
        View view = (View) object;
        vp.removeView(view);

    }




}
