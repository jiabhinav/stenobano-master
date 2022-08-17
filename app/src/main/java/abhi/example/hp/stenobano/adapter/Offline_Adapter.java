package abhi.example.hp.stenobano.adapter;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

import abhi.example.hp.stenobano.R;
import abhi.example.hp.stenobano.Interface.IMethodCaller;
import abhi.example.hp.stenobano.model.Helper_Model;
import abhi.example.hp.stenobano.other_class.GetData;
import abhi.example.hp.stenobano.sqlite_databse.DatabaseHandler;
import abhi.example.hp.stenobano.user.Offline_File;
import abhi.example.hp.stenobano.user.PlayOfflineAudioImage;
import abhi.example.hp.stenobano.user.Play_Offline_View;

import static abhi.example.hp.stenobano.sqlite_databse.DatabaseHandler.CAT_ID;
import static abhi.example.hp.stenobano.sqlite_databse.DatabaseHandler.TABLE_AUDIO;
import static abhi.example.hp.stenobano.sqlite_databse.DatabaseHandler.TABLE_IMAGE;

/**
 * Created by hp on 9/29/2017.
 */

 public class Offline_Adapter extends RecyclerView.Adapter<Offline_Adapter.HolderItem>
{
   private ArrayList<Helper_Model> userModelArrayList;
    Context context;
    String pid;
    ImageView imageView,cancel;
    TextView textView;
    File file;
    IMethodCaller methodCaller;
    private static final String DATABASE_NAME = "steno";

    // Contacts table name
    private static final String TABLE_CONTACTS = "steno_file";

    // Contacts Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_TITLE = "title";
    private static final String KEY_IMAGE = "image";
    private static final String KEY_AUDIO = "audio";
   private ArrayList<Helper_Model> arraylist;

    public Offline_Adapter(Context context, ArrayList<Helper_Model> userModelArrayList,IMethodCaller methodCaller) {
        this.userModelArrayList = userModelArrayList;
        this.context = context;
        this.methodCaller=methodCaller;
    }

    @Override
    public HolderItem onCreateViewHolder(ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.offline_data_list, parent, false);
        HolderItem holder = new HolderItem(layout);

        return holder;

    }

    @Override
    public void onBindViewHolder(HolderItem holder, final int position) {
        Helper_Model model1 = userModelArrayList.get(position);

        //=======================
        try
        {
            file  = context.getFileStreamPath(model1.getListImage().get(0).getImage());
            String imagePath = file.getAbsolutePath();
            Bitmap bitmapImage = BitmapFactory.decodeFile(imagePath);
            int nh = (int)(bitmapImage.getHeight() * (100.0 / bitmapImage.getWidth()) );
            Bitmap scaled = Bitmap.createScaledBitmap(bitmapImage, 100, nh, true);
            holder.imageView.setImageBitmap(scaled);

        }
        catch (Exception e)
        {

        }

        //=================================

       // holder.imageView.setImageDrawable(Drawable.createFromPath(imagePath));

        holder.title.setText(model1.getTitle());
        final String image=model1.getImage();
        holder.play_online.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                       //GetData.offlineMOdel=userModelArrayList.get(position);
                        GetData.offlinelist=userModelArrayList;
                        // Toast.makeText(context, ""+userModelArrayList.get(position).getImage(), Toast.LENGTH_SHORT).show();
                        //Intent intent=new Intent(context, Play_Offline_View.class);
                        Intent intent=new Intent(context, PlayOfflineAudioImage.class);
                       intent.putExtra("pos",position);
                      /*  intent.putExtra("id",model1.getId());
                        intent.putExtra("title",model1.getTitle());
                        intent.putExtra("audio",model1.getAudio());
                        intent.putExtra("image",model1.getImage());
                        intent.putExtra("image_two",model1.getImage_two());*/
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);


            }});

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                        DatabaseHandler db = new DatabaseHandler(context);
                        SQLiteDatabase apdb = db.getReadableDatabase();
                       apdb.delete(TABLE_CONTACTS, KEY_ID + "=?", new String[]{model1.getId()});
                        apdb.delete(TABLE_IMAGE, KEY_ID + "=?", new String[]{model1.getId()});
                         apdb.delete(TABLE_AUDIO, KEY_ID + "=?", new String[]{model1.getId()});
                         apdb.close();
                        //  userModelArrayList.remove(position);
                        methodCaller.getOffline();
                        notifyDataSetChanged();
                        Toast.makeText(context, "Removed!!!", Toast.LENGTH_SHORT).show();
                        record();

            }
        });


    }

    @Override
    public int getItemCount() {
        return userModelArrayList.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public class HolderItem extends RecyclerView.ViewHolder {

        protected TextView audio,image,play_online,title,delete;
        CardView cardView;
        ImageView imageView,add,remove;
        String u;


        public HolderItem(View convertView) {
            super(convertView);
           play_online=convertView.findViewById(R.id.play_online);
           title =  convertView.findViewById(R.id.title);
           imageView =  convertView.findViewById(R.id.image);
           delete =  convertView.findViewById(R.id.delete);
        }
    }

    public void record() {
        DatabaseHandler dh = new DatabaseHandler(context);
        SQLiteDatabase db = dh.getWritableDatabase();
        String count = "SELECT count(*) FROM " + TABLE_CONTACTS;
        Cursor mcursor = db.rawQuery(count, null);
        mcursor.moveToFirst();
        int icount = mcursor.getInt(0);
        if (icount > 0) {
            //Toast.makeText(context, "found", Toast.LENGTH_SHORT).show();
        }

        else {
            // Toast.makeText(context, "not found", Toast.LENGTH_SHORT).show();
            LayoutInflater  mInflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View  view=mInflater.inflate(R.layout.notfound, null, false);
            Offline_File.relativeLayout.addView(view);


        }

    }
    public Bitmap ShrinkBitmap(String file, int width, int height)
    {
        BitmapFactory.Options bmpFactoryOptions = new BitmapFactory.Options();
        bmpFactoryOptions.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeFile(file, bmpFactoryOptions);

        int heightRatio = (int) Math.ceil(bmpFactoryOptions.outHeight / (float) height);
        int widthRatio = (int) Math.ceil(bmpFactoryOptions.outWidth / (float) width);

        if(heightRatio > 1 || widthRatio > 1)
        {
            if(heightRatio > widthRatio)
            {
                bmpFactoryOptions.inSampleSize = heightRatio;
            }
            else
            {
                bmpFactoryOptions.inSampleSize = widthRatio;
            }
        }

        bmpFactoryOptions.inJustDecodeBounds = false;
        bitmap = BitmapFactory.decodeFile(file, bmpFactoryOptions);
        return bitmap;
    }


}