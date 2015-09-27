package helpers;

/**
 * Created by magupta on 9/26/15.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

import models.Task;
public class DBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "task.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TASK_TABLE = "tasks";
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";

    public DBHelper(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
                "CREATE TABLE " + TASK_TABLE + "" +
                        "( " +
                        KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        KEY_NAME + " TEXT" +
                        ")"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TASK_TABLE);
        onCreate(db);
    }


    public int createTask(Task task){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_NAME, task.getName());

        int id = (int) db.insert(TASK_TABLE, null, contentValues);
        db.close();

        return id;
    }

    public Task getData(int id){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor =  db.query(TASK_TABLE, new String[] {KEY_ID, KEY_NAME}, KEY_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null);
        if (cursor != null ){
            cursor.moveToFirst();
        }
        if (cursor == null)
            return null;

        System.out.print(cursor.toString());
        Task task =  new Task(Integer.valueOf(cursor.getString(0)), cursor.getString(1));
        db.close();
        cursor.close();
        return task;
    }

    public void deleteTask(Task task){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TASK_TABLE, KEY_ID + "=?", new String[]{String.valueOf(task.getId())});
        db.close();
    }

    public int getTaskCount(){
        SQLiteDatabase db = this.getReadableDatabase();
        return (int) DatabaseUtils.queryNumEntries(db, TASK_TABLE);
    }

    public boolean updateTask (Task task) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_NAME, task.getName());

        db.update(TASK_TABLE, contentValues, KEY_ID + "=?", new String[]{String.valueOf(task.getId())});
        db.close();
        return true;
    }

    public ArrayList<Task> getAllTasks(){
        ArrayList<Task> tasks = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM " + TASK_TABLE, null);

        if ( cursor.moveToFirst()){
            do {
                Task task = new Task(Integer.valueOf(cursor.getString(0)), cursor.getString(1));
                tasks.add(task);
            } while (cursor.moveToNext());
        }
        db.close();
        cursor.close();
        return tasks;
    }
}
