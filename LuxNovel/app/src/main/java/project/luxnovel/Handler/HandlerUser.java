package project.luxnovel.Handler;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import project.luxnovel.Model.ModelNovel;
import project.luxnovel.Model.ModelUser;

public class HandlerUser extends SQLiteOpenHelper
{
    String DB_NAME, path;
    int DB_VERSION;
    Context context;

    public HandlerUser(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version)
    {
        super(context, name, factory, version);
        this.DB_NAME = name;
        this.context = context;
        this.DB_VERSION = version;
        assert context != null;
        this.path = context.getFilesDir() +"/db/"+DB_NAME;
    }

    @Override
    public void onCreate(SQLiteDatabase database)
    {

    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int old_version, int new_version)
    {

    }

    @SuppressLint("Range")
    public ModelUser loadOneUser(int id)
    {
        SQLiteDatabase database = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.CREATE_IF_NECESSARY);
        Cursor cursor = database.rawQuery("select * from User where id_User=?", new String[]{String.valueOf(id)});

        cursor.moveToFirst();
        ModelUser user = new ModelUser();

        user.setId(cursor.getInt(0));
        user.setUsername(cursor.getString(1));
        user.setPassword(cursor.getString(2));
        user.setEmail(cursor.getString(3));
        user.setName(cursor.getString(4));
        user.setDob(cursor.getString(5));
        user.setGender(cursor.getString(6));

        cursor.close();
        database.close();
        return user;
    }

    public Cursor getCursorUser()
    {
        SQLiteDatabase database;

        database = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.CREATE_IF_NECESSARY);

        return database.rawQuery("select * from User", null);
    }

    public boolean insertAccount(ModelUser insert_user)
    {
        SQLiteDatabase database = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.CREATE_IF_NECESSARY);
        ContentValues values = new ContentValues();

        values.put("name_Login", insert_user.getUsername());
        values.put("password", insert_user.getPassword());
        values.put("email", insert_user.getEmail());

        long result = database.insert("User", null, values);
        if (result == -1)
        {
            Log.e("UserHandler", "Insert Account Successfully!");
            database.close();
            return true;
        }
        else
        {
            Log.d("UserHandler", "Insert Account Unsuccessfully!");
            database.close();
            return false;
        }
    }
    public boolean updateAccount(ModelUser update_user) {
        SQLiteDatabase database = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.CREATE_IF_NECESSARY);
        ContentValues values = new ContentValues();

        //values.put("name_Login", update_user.getUsername());
        values.put("password", update_user.getPassword());
        values.put("email", update_user.getEmail());
        values.put("username", update_user.getName());
        values.put("dob", update_user.getDob());
        values.put("gender", update_user.getGender());

        // Assuming you have a unique ID or username to identify the user
        String whereClause = "name_Login = ?";
        String[] whereArgs = { String.valueOf(update_user.getUsername()) };

        long result = database.update("User", values, whereClause, whereArgs);

        database.close();

        if (result > 0) {
            Log.d("UserHandler", "Update Account Successfully!");
            return true;
        } else {
            Log.e("UserHandler", "Update Account Unsuccessfully!");
            return false;
        }
    }


    public ModelUser findUser(Integer find_id)
    {
        ModelUser user  = new ModelUser();
        SQLiteDatabase database = SQLiteDatabase.openDatabase(path,null,SQLiteDatabase.CREATE_IF_NECESSARY);
        Cursor cursor = database.rawQuery("select * from User where id_User = ?", new String[] {String.valueOf(find_id)});

        if (cursor != null && cursor.moveToFirst())
        {
            do
            {
                user.setId(cursor.getInt(0));
                user.setName(cursor.getString(1));
                user.setPassword(cursor.getString(2));
                user.setEmail(cursor.getString(3));
                user.setUsername(cursor.getString(4));
                user.setDob(cursor.getString(5));
                user.setGender(cursor.getString(6));
            }
            while (cursor.moveToNext());

            cursor.close();
        }

        assert cursor != null;
        cursor.close();
        database.close();
        return user;
    }
}
