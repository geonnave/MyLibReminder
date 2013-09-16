package com.geonnave.mylibreminder;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class PersistenceHelper extends SQLiteOpenHelper {
	 
    public static final String DB_NAME =  "MyLibReminder";
    public static final int VERSAO =  1;
     
    private static PersistenceHelper instance;
     
    private PersistenceHelper(Context context) {
        super(context, DB_NAME, null, VERSAO);
    }
     
    public static PersistenceHelper getInstance(Context context) {
        if(instance == null)
            instance = new PersistenceHelper(context);
         
        return instance;
    }
 
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(LoanDAO.SCRIPT_CREATE_TABLE_LOANS);
    }
 
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(LoanDAO.SCRIPT_DELETE_TABLE);
        onCreate(db);
    }
 
}