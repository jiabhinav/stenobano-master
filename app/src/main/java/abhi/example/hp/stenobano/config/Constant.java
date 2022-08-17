package abhi.example.hp.stenobano.config;

import android.content.Context;
import android.graphics.Paint;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;
import java.text.SimpleDateFormat;

import java.util.Locale;

public class Constant {
    public static String tmc="By logging in or registering, you agree to our Terms of Service and Privacy Policy.";
    public  static  int screen_width=0;
    public  static  int screen_height=0;
    public static int HomeFragment=1;
    public static int AllCategory=2;



    public static String gif_firstpart="https://media.giphy.com/media/";
    public static String gif_secondpart="/100w.gif";

    public static String gif_firstpart_chat="https://media.giphy.com/media/";
    public static String gif_secondpart_chat="/200w.gif";

    public static String gif_api_key1="giphy_api_key_here";

    public static SimpleDateFormat df =
            new SimpleDateFormat("dd-MM-yyyy HH:mm:ssZZ", Locale.ENGLISH);

    public static SimpleDateFormat df2 =
            new SimpleDateFormat("dd-MM-yyyy HH:mmZZ", Locale.ENGLISH);
    public static int permission_camera_code=786;
    public static int permission_write_data=788;
    public static int permission_Read_data=789;
    public static int permission_Recording_audio=790;




    public static void setImageWidthHeight(ImageView imageView, int width, int height)
    {
        imageView.getLayoutParams().width = Constant.screen_width*width/100;
        imageView.getLayoutParams().height = Constant.screen_width*height/100;
    }

    public static void setIconImageSize(ImageView imageView, int width, int height)
    {
        imageView.getLayoutParams().width = width;
        imageView.getLayoutParams().height = height;
    }

    public static void setImageTint(Context context, ImageView imageView, int color)
    {

        imageView.setColorFilter(ContextCompat.getColor(context,color));
    }


    public static void recyclerDiverLine(RecyclerView recyclerView)
    {
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));
    }

    public static void underline(TextView textView)
    {
        textView.setPaintFlags(textView.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
    }



}
