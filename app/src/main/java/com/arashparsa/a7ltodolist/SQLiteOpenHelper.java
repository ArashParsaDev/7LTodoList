package com.arashparsa.a7ltodolist;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;


public class SQLiteOpenHelper extends android.database.sqlite.SQLiteOpenHelper {

    private static final String TABLE_TASKS = "tbl_task";
    private static final String TAG = "SQLiteHelper";

    public SQLiteOpenHelper(@Nullable Context context) {
        super(context, "db_app", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        //int INTEGER
        //String TEXT
        //REAL ===> ashari
        //NUMERIC ===> numeric,decimal,boolean,date,datetime
        try {
            sqLiteDatabase.execSQL("CREATE TABLE " + TABLE_TASKS + " (id INTEGER PRIMARY KEY AUTOINCREMENT , nameTask TEXT , isCompleted BOOLEAN ); ");
        } catch (SQLiteException e) {
            Log.e(TAG, "onCreate: " + e.toString());
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }


    public long addTask(Task task) {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        //id will set automatically. no need to put!
        contentValues.put("nameTask", task.getNameTask());
        contentValues.put("isCompleted", task.isCompleted());
        long result = sqLiteDatabase.insert(TABLE_TASKS, null, contentValues);
        sqLiteDatabase.close();
        return result;


    }

    public List<Task> getTasks() {

        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM " + TABLE_TASKS, null);
        //moveToFirst ==> if it was true it will return the first row of selected table
        List<Task> taskList = new ArrayList<>();
        if (cursor.moveToFirst()) {
            //this is just add of one task
            do {
                Task task = new Task();
                task.setId(cursor.getLong(0));
                task.setNameTask(cursor.getString(1));
                task.setCompleted(cursor.getInt(2) == 1);
                taskList.add(task);
            } while (cursor.moveToNext());
            //do while until we have a row
        }
        sqLiteDatabase.close();
        return taskList;
    }

    public List<Task> searchTask(String query) {
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM "+TABLE_TASKS+" WHERE nameTask LIKE '%" +query + "%'",null);
        List<Task> taskList = new ArrayList<>();
        if (cursor.moveToFirst()) {
            //this is just add of one task
            do {
                Task task = new Task();
                task.setId(cursor.getLong(0));
                task.setNameTask(cursor.getString(1));
                task.setCompleted(cursor.getInt(2) == 1);
                taskList.add(task);
            } while (cursor.moveToNext());
            //do while until we have a row
        }
        sqLiteDatabase.close();
        return taskList;
    }


    public int deleteTask(Task task) {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        int result = sqLiteDatabase.delete(TABLE_TASKS, "id = ?", new String[]{String.valueOf(task.getId())});
        sqLiteDatabase.close();
        return result;

    }

    public void deleteTasks() {
        try {
            SQLiteDatabase sqLiteDatabase =getWritableDatabase();
            sqLiteDatabase.execSQL("DELETE FROM " + TABLE_TASKS);

        }catch (SQLiteException e){
            Log.i(TAG, "deleteTasks: " + e.toString());
        }

    }

    public int updateTask(Task task) {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("nameTask", task.getNameTask());
        contentValues.put("isCompleted", task.isCompleted());
        //where clause  ===> u should set which row should updated
        //where args = ?
        int result = sqLiteDatabase.update(TABLE_TASKS, contentValues, "id = ?", new String[]{String.valueOf(task.getId())});
        sqLiteDatabase.close();
        return result;

    }
}
