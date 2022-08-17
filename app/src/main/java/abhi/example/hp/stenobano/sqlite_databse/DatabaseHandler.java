package abhi.example.hp.stenobano.sqlite_databse;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


import java.util.ArrayList;
import java.util.List;

import abhi.example.hp.stenobano.model.Helper_Model;
import abhi.example.hp.stenobano.model.ModelCategoryDetail;

/**
 * Created by hp on 05-Feb-18.
 */

public class DatabaseHandler extends SQLiteOpenHelper {

    // All Static variables
    // Database Version
    Context context;
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "steno";

    // Contacts table name
    private static final String TABLE_CONTACTS = "steno_file";

    public static final String TABLE_IMAGE = "image";
    public static final String TABLE_AUDIO = "audio";

    // Contacts Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_TITLE = "title";
    private static final String KEY_IMAGE = "image";
    private static final String KEY_IMAGE_TWO = "image_two";
    private static final String KEY_AUDIO = "audio";

    public static final String CAT_ID = "cat_id";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_CONTACTS + "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
       + KEY_TITLE + " TEXT," + CAT_ID + " TEXT " + ")";
                db.execSQL(CREATE_CONTACTS_TABLE);

        String CREATE_CONTACTS_IMAGE = "CREATE TABLE " + TABLE_IMAGE + "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + CAT_ID + " TEXT," + KEY_IMAGE + " TEXT " + ")";
        db.execSQL(CREATE_CONTACTS_IMAGE);

        String CREATE_CONTACTS_AUDIO = "CREATE TABLE " + TABLE_AUDIO + "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + CAT_ID + " TEXT," + KEY_IMAGE + " TEXT " + ")";
        db.execSQL(CREATE_CONTACTS_AUDIO);

    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTACTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_IMAGE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_AUDIO);
        // Create tables again
        onCreate(db);
    }

    /**
     * All CRUD(Create, Read, Update, Delete) Operations
     */

    // Adding new contact
    public void addContact(ModelCategoryDetail.Result model) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_TITLE, model.getTitle());
        values.put(CAT_ID, model.getId());
        db.insert(TABLE_CONTACTS, null, values);
        // long rowInserted = db.insert(TABLE_CONTACTS, null, values);
    }


        public void addImage(ModelCategoryDetail.Result model) {

        for (int i=0;i<model.getImage().size();i++)
        {
            Log.d("audiourl",model.getImage().get(i).getName()+"-"+model.getId());
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(KEY_IMAGE, model.getImage().get(i).getName());
            values.put(CAT_ID, model.getId());
            db.insert(TABLE_IMAGE, null, values);
        }

    }


        public void addAudio(ModelCategoryDetail.Result model)
        {
            for (int i=0;i<model.getAudio().size();i++)
            {
                Log.d("audiourl",model.getAudio().get(i).getName()+"-"+model.getId());
                SQLiteDatabase db = this.getWritableDatabase();
                ContentValues values = new ContentValues();
                values.put(KEY_IMAGE, model.getAudio().get(i).getName());
                values.put(CAT_ID, model.getId());
                db.insert(TABLE_AUDIO, null, values);
            }

        }




    public void add_Long_Contact(Helper_Model model) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_TITLE, model.getTitle());
        values.put(KEY_IMAGE, model.getImage());
        values.put(KEY_IMAGE_TWO, model.getImage_two());
        values.put(KEY_AUDIO, model.getAudio());

        db.insert(TABLE_CONTACTS, null, values);
        // long rowInserted = db.insert(TABLE_CONTACTS, null, values);


        // db.close();


        // Closing database connection
    }







    // Getting All Contacts
    public List<Helper_Model> getAllContacts() {
        List<Helper_Model> contactList = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_CONTACTS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Helper_Model contact = new Helper_Model();
                contact.setId((cursor.getString(0)));
                contact.setTitle((cursor.getString(1)));
                contact.setCat_id((cursor.getString(2)));
                contact.setListAudio(getAudio(cursor.getString(2)));
                contact.setListImage(getImage(cursor.getString(2)));
                contactList.add(contact);
            //;



            } while (cursor.moveToNext());
        }

        // return contact list
        return contactList;
    }



    public List<Helper_Model.Audio> getAudio(String id) {
        List<Helper_Model.Audio> contactList = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT  * FROM "+TABLE_AUDIO +" WHERE cat_id=? ";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[] { id });

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Helper_Model.Audio contact = new Helper_Model.Audio();
                contact.setAudio((cursor.getString(2)));
                contactList.add(contact);

            } while (cursor.moveToNext());
        }

        // return contact list
        return contactList;
    }



    public List<Helper_Model.Image> getImage(String id) {
        List<Helper_Model.Image> contactList = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT  * FROM "+TABLE_IMAGE +" WHERE cat_id=? ";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[] { id });

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Helper_Model.Image contact = new Helper_Model.Image();
                contact.setImage((cursor.getString(2)));
                contactList.add(contact);

            } while (cursor.moveToNext());
        }

        // return contact list
        return contactList;
    }




    public List<Helper_Model> getSingleData(String id) {
        List<Helper_Model> contactList = new ArrayList<>();
        // Select All Query
        // String selectQuery = "SELECT  * FROM " + TABLE_CONTACTS  + " where "+ KEY_CATEGORY +"="+nam;
        SQLiteDatabase db = this.getWritableDatabase();
        String selectQuery = "SELECT  * FROM steno_file WHERE id=? ";
        Cursor cursor = db.rawQuery(selectQuery, new String[] { id });

        //  Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if(cursor.getCount()>0) {
            if (cursor.moveToFirst()) {
                do {

                    for (int i=0;i<2;i++)
                    {
                        if (i==0)
                        {
                            Helper_Model contact = new Helper_Model();
                            contact.setId((cursor.getString(0)));
                            contact.setTitle((cursor.getString(1)));
                            contact.setImage((cursor.getString(2)));
                            //contact.setImage_two((cursor.getString(3)));
                            contact.setAudio(cursor.getString(4));
                            contactList.add(contact);
                        }
                        else
                        {
                            Helper_Model contact = new Helper_Model();
                            contact.setId((cursor.getString(0)));
                            contact.setTitle((cursor.getString(1)));
                           // contact.setImage((cursor.getString(2)));
                            contact.setImage((cursor.getString(3)));
                          //  contact.setAudio(cursor.getString(4));
                            contactList.add(contact);
                        }

                    }

                }
                while (cursor.moveToNext());
            }

        }
        else {
            // Toast.makeText(context, "Empty", Toast.LENGTH_SHORT).show();
        }


        // return contact list
        return contactList;
    }


    // Deleting single contact
    public void deleteContact(Helper_Model contact) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_CONTACTS, KEY_ID + " = ?",
                new String[] { String.valueOf(contact.getId()) });
        db.close();
    }


    // Getting contacts Count
    public int getContactsCount() {
        String countQuery = "SELECT  * FROM " + TABLE_CONTACTS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();
        // return count
        return cursor.getCount();
    }

    public Integer deleteData(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int i =db.delete(TABLE_CONTACTS,"image=?",new String[]{id});
        db.close();
        return i;

    }
    public Cursor getData(String img)
    {
        SQLiteDatabase dp=this.getWritableDatabase();
        Cursor res=dp.rawQuery("Select * from "+ TABLE_CONTACTS + " WHERE image=" + img,null);
        return res;
    }


}

