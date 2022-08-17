package abhi.example.hp.stenobano.other_class;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import abhi.example.hp.stenobano.model.Helper_Model;
import abhi.example.hp.stenobano.model.ModelCategoryDetail;

public class GetData {
    public static ModelCategoryDetail.Result modelDetail;
    public static List<ModelCategoryDetail.Result> modelAllDetail=new ArrayList<>();
    public static Helper_Model offlineMOdel;
    public static List<Helper_Model>offlinelist=new ArrayList<>();
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
}
