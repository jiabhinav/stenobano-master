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

import java.io.File;
import java.util.List;

import abhi.example.hp.stenobano.R;
import abhi.example.hp.stenobano.model.Helper_Model;

/**
 * Created by hp on 27-Jan-18.
 */

public class BannerPagerAdapter extends PagerAdapter {

    private Context context;
    private LayoutInflater layoutInflater;
    private List<Helper_Model.Image> sliderImg;
    private ImageLoader imageLoader;
File file;

    public BannerPagerAdapter(List<Helper_Model.Image> sliderImg, Context context) {
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

        final Helper_Model.Image utils = sliderImg.get(position);

        final ImageView imageView = (ImageView) view.findViewById(R.id.imageView);

        if ( utils.getImage()!=null)
        {
            file  =context.getFileStreamPath(utils.getImage());
            String imagePath = file.getAbsolutePath();
            imageView.setImageDrawable(Drawable.createFromPath(imagePath));
        }
        else
        {

        }




        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(position == 0){
                    Toast.makeText(context, "Slide 1 Clicked", Toast.LENGTH_SHORT).show();
                } else if(position == 1){
                    Toast.makeText(context, "Slide 2 Clicked", Toast.LENGTH_SHORT).show();
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
