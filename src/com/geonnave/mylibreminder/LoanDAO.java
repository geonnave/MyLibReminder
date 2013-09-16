package com.geonnave.mylibreminder;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class LoanDAO {
	public static final String TABLE_NAME = "Loans";
    public static final String ID_COLUMN = "id";
    public static final String BOOK_COLUMN = "book_name";
    public static final String LIBRARY_COLUMN = "library_name";
    public static final String DATE_COLUMN = "date_loan";
    
    public static final String SCRIPT_CREATE_TABLE_LOANS = "CREATE TABLE " + TABLE_NAME + "("
            + ID_COLUMN + " INTEGER PRIMARY KEY," + BOOK_COLUMN + " TEXT," + LIBRARY_COLUMN + " TEXT,"
            + DATE_COLUMN + " DATE" + ")";
    
    public static final String SCRIPT_DELETE_TABLE =  "DROP TABLE IF EXISTS " + TABLE_NAME;
    
    private SQLiteDatabase dataBase = null;
    
    private static LoanDAO instance;
    
    private int nIds = 0;
    
    public static LoanDAO getInstance(Context context) {
        if(instance == null)
            instance = new LoanDAO(context);
        return instance;
    }

    private LoanDAO(Context context) {
        PersistenceHelper persistenceHelper = PersistenceHelper.getInstance(context);
        dataBase = persistenceHelper.getWritableDatabase();
    }
    
    public void save(Loan loan) {
        ContentValues values = generateContentValuesLoan(loan);
        dataBase.insert(TABLE_NAME, null, values);
        nIds += 1;
    }
    
    public List<Loan> getAll() {
        String queryReturnAll = "SELECT * FROM " + TABLE_NAME;
        Cursor cursor = dataBase.rawQuery(queryReturnAll, null);
        List<Loan> veiculos = constructLoanByCursor(cursor);
 
        return veiculos;
    }
    
    public void delete(Loan loan) {
    	 
        String[] valoresParaSubstituir = {
                String.valueOf(loan.getId())
        };
 
        dataBase.delete(TABLE_NAME, ID_COLUMN + " =  ?", valoresParaSubstituir);
    }
    
    public void delete(int id) {
    	 
        String[] valoresParaSubstituir = {
                String.valueOf(id)
        };
 
        dataBase.delete(TABLE_NAME, ID_COLUMN + " =  ?", valoresParaSubstituir);
    }
    
    public void deleteAll() {
    	dataBase.delete(TABLE_NAME, null, null);
    }
    
    public void edit(Loan loan) {
        ContentValues valores = generateContentValuesLoan(loan);
 
        String[] valoresParaSubstituir = {
                String.valueOf(loan.getId())
        };
 
        dataBase.update(TABLE_NAME, valores, ID_COLUMN + " = ?", valoresParaSubstituir);
    }
    
    public int getNIds() {
    	return this.nIds;
    }
    
    public void fecharConexao() {
        if(dataBase != null && dataBase.isOpen())
            dataBase.close(); 
    }
    
    private List<Loan> constructLoanByCursor(Cursor cursor) {
        List<Loan> loans = new ArrayList<Loan>();
        if(cursor == null)
            return loans;
         
        try {
 
            if (cursor.moveToFirst()) {
                do {
 
                    int indexID = cursor.getColumnIndex(ID_COLUMN);
                    int indexBook = cursor.getColumnIndex(BOOK_COLUMN);
                    int indexLibrary = cursor.getColumnIndex(LIBRARY_COLUMN);
                    int indexDate = cursor.getColumnIndex(DATE_COLUMN);
 
                    int id = cursor.getInt(indexID);
                    String book = cursor.getString(indexBook);
                    String library = cursor.getString(indexLibrary);
                    
                    Date d = new Date(cursor.getLong(indexDate));
                    Calendar date = new GregorianCalendar();
                    date.setTime(d);
 
                    Loan loan = new Loan(id, book, library, date);
 
                    loans.add(loan);
 
                } while (cursor.moveToNext());
            }
             
        } finally {
            cursor.close();
        }
        return loans;
    }
    
    private ContentValues generateContentValuesLoan(Loan loan) {
        ContentValues values = new ContentValues();
        values.put(BOOK_COLUMN, loan.getBook());
        values.put(LIBRARY_COLUMN, loan.getLibrary());
//        values.put(DATE_COLUMN, loan.getFormatedDate());
        values.put(DATE_COLUMN, loan.getDate().getTimeInMillis());
 
        return values;
    }
 
}
