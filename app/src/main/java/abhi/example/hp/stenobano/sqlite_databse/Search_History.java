package abhi.example.hp.stenobano.sqlite_databse;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import abhi.example.hp.stenobano.model.Category_Model;
import abhi.example.hp.stenobano.model.DB_Model;
import abhi.example.hp.stenobano.model.Product_Model;


/**
 * Created by hp on 05-Feb-18.
 */

public class Search_History extends SQLiteOpenHelper {

    // All Static variables
    // Database Version
    Context context;
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "steno_search_history";
    // Contacts table name
    private static final String TABLE_CONTACTS = "search_history";

    // Contacts Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";


    public  static String type="";
    public  static String type2="";

    public Search_History(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_CONTACTS + "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                 + KEY_NAME + " TEXT " + ")";
                db.execSQL(CREATE_CONTACTS_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTACTS);

        // Create tables again
        onCreate(db);
    }

    /**
     * All CRUD(Create, Read, Update, Delete) Operations
     */

    // Adding new contact
    public void addContact(Category_Model model) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, model.getName());
        db.insert(TABLE_CONTACTS, null, values);
        db.close();

        // Closing database connection
    }

    //Getting single contact
DB_Model getContact(int id) {
    SQLiteDatabase db = this.getReadableDatabase();

    Cursor cursor = db.query(TABLE_CONTACTS, new String[] { KEY_ID,KEY_NAME }, KEY_ID + "=?",
            new String[] { String.valueOf(id) }, null, null, null, null);
    if (cursor != null)
        cursor.moveToFirst();
             DB_Model contact = new DB_Model(Integer.parseInt(cursor.getString(0)),
             cursor.getString(1), cursor.getString(2));

    // return contact
    return contact;
    }





    // Getting All Contacts
    public List<Category_Model> getAllContacts() {
        List<Category_Model> contactList = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT   DISTINCT id, name FROM " + TABLE_CONTACTS ;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToLast()) {
            do {
                Category_Model contact = new Category_Model();
                contact.setId((cursor.getString(0)));
                contact.setName(cursor.getString(1));
                contactList.add(contact);
            } while (cursor.moveToPrevious());
        }

        // return contact list
        return contactList;
    }

    // Getting All Contacts
    public List<Product_Model> getSingleContacts(String nam) {
        List<Product_Model> contactList = new ArrayList<>();
        // Select All Query
       // String selectQuery = "SELECT  * FROM " + TABLE_CONTACTS  + " where "+ KEY_CATEGORY +"="+nam;
type="0";
        SQLiteDatabase db = this.getWritableDatabase();
        String selectQuery = "SELECT  DISTINCT id, name FROM category_product WHERE category=? ";
        Cursor cursor = db.rawQuery(selectQuery, new String[] { nam });

      //  Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if(cursor.getCount()>0) {
            if (cursor.moveToFirst()) {
                do {

                    Product_Model contact = new Product_Model();
                  contact.setId((cursor.getString(0)));
                  contact.setName(cursor.getString(1));


                    contactList.add(contact);
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
    public List<Product_Model> getUnderSubCategory(String nam) {
        List<Product_Model> contactList = new ArrayList<>();
        // Select All Query
        // String selectQuery = "SELECT  * FROM " + TABLE_CONTACTS  + " where "+ KEY_CATEGORY +"="+nam;
        type2="1";
        SQLiteDatabase db = this.getWritableDatabase();
        String selectQuery = "SELECT   name FROM category_product WHERE sub_category=? ";
        Cursor cursor = db.rawQuery(selectQuery, new String[] { nam });

        //  Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if(cursor.getCount()>0) {
            if (cursor.moveToFirst()) {
                do {

                    Product_Model contact = new Product_Model();
                    //  contact.setId((cursor.getString(0)));
                    //contact.setCat_id((cursor.getString(1)));
                    //contact.setName(cursor.getString(2));
                    //contact.setCategory(cursor.getString(3));
                    contact.setSub_category(cursor.getString(0));
                    contactList.add(contact);
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
    public void deleteContact(Product_Model contact) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_CONTACTS, KEY_ID + " = ?",
                new String[] { String.valueOf(contact.getProduct_id())});
        db.close();
    }
    public void delete(String pro_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_CONTACTS, KEY_ID + " = ?",
                new String[] { String.valueOf(pro_id)});
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

    /*
    public long sum_Of_Rs() {

        int Sum=0;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery(" select * from  " +TABLE_CONTACTS + " ORDER BY " + KEY_ID + " DESC " , null);
        ArrayList<Product_Model> list = new ArrayList<Product_Model>();
        try {

            res.moveToFirst();

            for (int i = 0; i < res.getCount(); i++) {

                Product_Model Prod_Details = new Product_Model();
                Prod_Details.setPrice(res.getString(res.getColumnIndex(KEY_PRICE)));
                Prod_Details.setP_id(res.getString(res.getColumnIndex(KEY_ID)));

                try {
                    int total = Integer.parseInt(res.getString(res.getColumnIndex(KEY_PRICE)));

                    Sum = Sum + total;
                    return Sum;
                } catch (Exception e) {
                    e.printStackTrace();
                }

                list.add(Prod_Details);
                res.moveToNext();
            }

        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return Sum;
    }*/
    public long sum_Of_Rs() {
        SQLiteDatabase db = this.getReadableDatabase();
        //Cursor cursor = db.rawQuery("SELECT SUM (" + KEY_TOTALAMOUNT + ") FROM " + TABLE_CONTACTS , null);
        Cursor cursor = db.rawQuery("SELECT SUM (" + KEY_ID + ") FROM " + TABLE_CONTACTS , null);
        cursor.moveToFirst();
        int sum = cursor.getInt(0);
        cursor.close();
        return sum;
    }
    public long sum_Of_Rs_pro(String pro_id) {
        SQLiteDatabase db = this.getReadableDatabase();
        //Cursor cursor = db.rawQuery("SELECT SUM (" + KEY_TOTALAMOUNT + ") FROM " + TABLE_CONTACTS , null);
        //  Cursor cursor = db.rawQuery("SELECT SUM (" + KEY_TOTALAMOUNT + ") FROM " + TABLE_CONTACTS , null);

        Cursor cursor = db.rawQuery("SELECT SUM(total_amount) FROM add_cart where pro_id="+pro_id, null);
        cursor.moveToFirst();
        int sum = cursor.getInt(0);
        cursor.close();
        return sum;
    }




}

