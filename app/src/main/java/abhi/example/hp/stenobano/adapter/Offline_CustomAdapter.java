package abhi.example.hp.stenobano.adapter;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import androidx.cardview.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import java.io.File;
import java.util.ArrayList;

import abhi.example.hp.stenobano.R;
import abhi.example.hp.stenobano.Interface.IMethodCaller;
import abhi.example.hp.stenobano.model.Helper_Model;
import abhi.example.hp.stenobano.sqlite_databse.DatabaseHandler;
import abhi.example.hp.stenobano.user.Offline_File;
import abhi.example.hp.stenobano.user.Play_Offline_View;

/**
 * Created by hp on 06-Feb-18.
 */

public class Offline_CustomAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<Helper_Model> userModelArrayList;
    String id;
    int sum = 1;
    int sum2;
    int am = 0;
     int counter = 1;
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
    public Offline_CustomAdapter(Context context, ArrayList<Helper_Model> userModelArrayList,IMethodCaller methodCaller) {

        this.context = context;
        this.userModelArrayList = userModelArrayList;
        this.methodCaller=methodCaller;
    }


    @Override
    public int getCount() {
        return userModelArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return userModelArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;

        if (convertView == null) {
            holder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.offline_data_list, null, true);

            final Helper_Model model1 = userModelArrayList.get(position);
           holder.play_online=convertView.findViewById(R.id.play_online);
            holder.title =  convertView.findViewById(R.id.title);
            holder.imageView =  convertView.findViewById(R.id.image);
            holder.delete =  convertView.findViewById(R.id.delete);
          //  String imagePath = Environment.getExternalStorageDirectory().toString()+"/download"+"/"+userModelArrayList.get(position).getImage();
           file  = context.getFileStreamPath(model1.getImage());
            String imagePath = file.getAbsolutePath();
            holder.imageView.setImageDrawable(Drawable.createFromPath(imagePath));
            holder.imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            holder.title.setText(model1.getTitle());
            final String image=model1.getImage();
            convertView.setTag(holder);
            holder.play_online.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    for (int i = 0; i < userModelArrayList.size(); i++) {
                        if (userModelArrayList.get(i).getImage().equals(image)) {
                           // Toast.makeText(context, ""+userModelArrayList.get(position).getImage(), Toast.LENGTH_SHORT).show();
                            Intent intent=new Intent(context, Play_Offline_View.class);
                            intent.putExtra("id",model1.getId());
                            intent.putExtra("title",model1.getTitle());
                            intent.putExtra("audio",model1.getAudio());
                            intent.putExtra("image",model1.getImage());
                            intent.putExtra("image_two",model1.getImage_two());
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(intent);
                        }
                        }

                }});

            holder.delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    for (int i = 0; i < userModelArrayList.size(); i++) {
                        if (userModelArrayList.get(i).getImage().equals(image)) {
                            DatabaseHandler db = new DatabaseHandler(context);
                            SQLiteDatabase apdb = db.getReadableDatabase();
                            apdb.delete(TABLE_CONTACTS, KEY_IMAGE + "=?", new String[]{model1.getImage()});
                            apdb.close();
                          //  userModelArrayList.remove(position);
                            methodCaller.getOffline();
                            notifyDataSetChanged();
                            Toast.makeText(context, "Removed!!!", Toast.LENGTH_SHORT).show();
                            record();

                        }
                        }
                }
            });


        }else {
            // the getTag returns the viewHolder object set as a tag to the view
            holder = (ViewHolder)convertView.getTag();
        }



        return convertView;
    }
    private class ViewHolder {
        protected TextView audio,image,play_online,title,delete;
        CardView cardView;
        ImageView imageView,add,remove;
        String u;
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


}
