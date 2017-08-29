package com.example.user_dell.labtwitteroauth;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.util.Log;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import java.lang.String;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import static android.content.Intent.getIntent;

/**
 * Created by USER - DELL on 25-08-2017.
 */
public class DbHelper extends SQLiteOpenHelper {
    private static final String TAG=DbHelper.class.getSimpleName();

    private Resources mResources;
    private static final String DATABASE_NAME="twitterfeed.db";
    private static final int DATABASE_VERSION=1;
    Context context;
    SQLiteDatabase db;
    DbHelper dh;

    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        mResources = context.getResources();
        db=this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        final String SQL_CREATE_BUGS_TABLE="CREATE TABLE"+DbContract.MenuEntry.TABLE_NAME + "(" +
                DbContract.MenuEntry.Id + "TEXT UNIQUE NOT NULL ," +
                DbContract.MenuEntry.TIME + "TEXT NOT NULL ," +
                DbContract.MenuEntry.LINK + "TEXT NOT NULL ," + " );" ;
        db.execSQL(SQL_CREATE_BUGS_TABLE);
        Log.d(TAG, "Data base created successfully");

        try
        {
            readDataToDb(db);
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
        catch(JSONException e )
        {
            e.printStackTrace();
        }

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    private void readDataToDb(SQLiteDatabase db)throws IOException,JSONException {
        final String MNU_Id = "id";
        final String MNU_TIME = "time";
        final String MNU_LINK = "link";
        String jsonDatastring;
        try {
            jsonDatastring = readJsonData();
            JSONArray jsonArray = new JSONArray(jsonDatastring);

            for (int a = 0; a < jsonArray.length(); a++) {
                String id;
                String time;
                String link;

                JSONObject jsonobject = jsonArray.getJSONObject(a);
                time = jsonobject.getString(MNU_TIME);
                id = jsonobject.getString(MNU_Id);
                link = jsonobject.getString(MNU_LINK);

                ContentValues menuValues = new ContentValues();
                menuValues.put(DbContract.MenuEntry.TIME, time);
                menuValues.put(DbContract.MenuEntry.Id, id);
                menuValues.put(DbContract.MenuEntry.LINK, link);

                db.insert(DbContract.MenuEntry.TABLE_NAME, null, menuValues);


            }
        } catch (JSONException e) {
            Log.e(TAG, e.getMessage(), e);
            e.printStackTrace();
        }

        Pattern p=Pattern.compile("https?(.*)");
        SQLiteDatabase dob = this.getWritableDatabase();
        Cursor cursor = dob.rawQuery("select * from " + DbContract.MenuEntry.TABLE_NAME, null);
        if (cursor.getCount() == 0) {
            showmessage("Error", "Nothing found");
        } else {
            StringBuffer buffer = new StringBuffer();
            while (cursor.moveToNext()) {
                buffer.append(cursor.getString(0));
                buffer.append(cursor.getString(1));
                StringBuffer t=buffer.append(cursor.getString(2));
                 Matcher m=p.matcher(t);
                buffer.append(m.group());
                showmessage("Twitter Feed", buffer.toString());
            }

        }
    }


        public void showmessage(String title,String message)
    {
        AlertDialog.Builder builder=new AlertDialog.Builder(context);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setTitle(message);
        builder.show();
    }

        private String readJsonData() throws IOException
    {
        Intent intent=new Intent();
        String data=(String) intent.getExtras().get("json");
        intent.getExtras();
        StringBuilder builder = new StringBuilder(data);
        return new String(builder);
    }

    public Cursor getInfo(SQLiteDatabase dop) {
        String[] projection = {DbContract.MenuEntry.TIME, DbContract.MenuEntry.Id, DbContract.MenuEntry.LINK};
        Cursor cursor = dop.query(DbContract.MenuEntry.TABLE_NAME, projection, null, null, null, null, null);
        return cursor;
    }



}
