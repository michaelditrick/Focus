package com.example.focus;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class DBClass extends SQLiteOpenHelper { //SQLiteOpenHelper is an in-built class of android. database. sqlite. SQLiteDatabase package. It is a helper class to manage SQLite database creation and version.
    private static final String ID_COL = "id";
    private String TABLE_NAME="Info";
    // below variable is for our column names
    private static final String NAME_COL = "name";
    private static final String AGE_COL = "age";

    // below variable id for our course number column.
    private static final String GENDER_COL = "gender";

    // below variable id for year
    private static final String USERNAME_COL = "username";

    private static final String PASSWORD_COL = "password";


    public DBClass(Context context, String DATABASE_NAME){
        super(context,DATABASE_NAME,null,1);
    }
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_NAME + " ("
                + ID_COL + " INTEGER PRIMARY KEY AUTOINCREMENT, "   //uniquely identifies the table.
                + NAME_COL + " TEXT,"    //column name and type.
                + AGE_COL + " INTEGER,"
                + GENDER_COL + " TEXT,"
                + USERNAME_COL + " TEXT,"
                + PASSWORD_COL + " TEXT" +
                ")";

        db.execSQL(query);    //execute the SQL query. Cannot be used for SELECT/INSERT/UPDATE/DELETE.
    }
    /*  public void deletetable(String table_name){
          SQLiteDatabase db = this.getWritableDatabase();

          db.execSQL("DELETE * FROM "+table_name);
      }*/
    public void addInfo(String name, String age, String gender, String username, String password)//,  )
    {

        //This class is used to store a set of values that the ContentResolver (handles content provided to the app) can process.
        ContentValues values = new ContentValues();
        SQLiteDatabase db = this.getWritableDatabase();  //writing into the database.
        //values.put(ID_COL,1);    //add column name and values into the ContentValues object.
        values.put(NAME_COL, name);
        values.put(AGE_COL, Integer.parseInt(age));
        values.put(GENDER_COL, gender);
        values.put(USERNAME_COL, username);
        values.put(PASSWORD_COL, password);
        db.insert(TABLE_NAME, null, values); //insert into the DB table.
        db.close();
    }

    public boolean checkUsername(String username){
        String query="SELECT COUNT(*) FROM "+TABLE_NAME+" WHERE username = '"+username+"'";
        SQLiteDatabase db = this.getReadableDatabase();   //selection is a read command.

        //Cursor provides random read-write access to the result set returned by a database query.
        Cursor extract=db.rawQuery(query,null);  //rawQuery is used to build SQL query. Used only for read queries.
        extract.moveToFirst();  //move cursor to the last record.

        boolean usernameFound = false;
        if (extract != null) {
            extract.moveToFirst(); // Move to the first row of the results
            usernameFound = extract.getInt(0) > 0; // Check if the count is greater than 0
            extract.close(); // Close the cursor to release resources
        }

        db.close(); // Close the database instance
        return usernameFound; // Return true if a match was found, false otherwise
        //return name;
    }
    public boolean verifyPassword(String username, String password){
        String query="SELECT COUNT(*) FROM "+TABLE_NAME+" WHERE username = '"+username+"' AND password = '"+password+ "'";
        SQLiteDatabase db = this.getReadableDatabase();   //selection is a read command.

        //Cursor provides random read-write access to the result set returned by a database query.
        Cursor extract=db.rawQuery(query,null);  //rawQuery is used to build SQL query. Used only for read queries.
        extract.moveToFirst(); //moveToLast();  //move cursor to the last record.
        //extract.moveToNext();

        boolean matchFound = false;
        if (extract != null) {
            extract.moveToFirst(); // Move to the first row of the results
            matchFound = extract.getInt(0) > 0; // Check if the count is greater than 0
            extract.close(); // Close the cursor to release resources
        }

        db.close(); // Close the database instance
        return matchFound; // Return true if a match was found, false otherwise
    }

    public String getName(String username){
        String query="SELECT name FROM "+TABLE_NAME+" WHERE username = '"+username+"'";
        SQLiteDatabase db = this.getReadableDatabase();   //selection is a read command.

        //Cursor provides random read-write access to the result set returned by a database query.
        Cursor extract=db.rawQuery(query,null);  //rawQuery is used to build SQL query. Used only for read queries.
        extract.moveToFirst();  //move cursor to the last record.

        String name = "";
        if (extract != null) {
            name = extract.getString(0);
        }
        return name;
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // this method is called to check if the table exists already.
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);

    }}
