package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.TransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Transaction;

public class PersistentTransactionDAO extends SQLiteOpenHelper implements TransactionDAO {
    public static final String TRANSACTION_TABLE = "account_transaction";
    public static final String TRANSACTION_ID_COLUMN = "transactionId";
    public static final String DATE_COLUMN = "date";
    public static final String ACCOUNT_NO_COLUMN = "accountNo";
    public static final String EXPENSE_TYPE_COLUMN = "expenseType";
    public static final String AMOUNT_COLUMN = "amount";

    public PersistentTransactionDAO(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String createTableStatement = "CREATE TABLE " + TRANSACTION_TABLE + " ( " +
                TRANSACTION_ID_COLUMN + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                DATE_COLUMN + " TEXT," +
                ACCOUNT_NO_COLUMN + " STRING," +
                EXPENSE_TYPE_COLUMN + " STRING," +
                AMOUNT_COLUMN + " REAL);";
        sqLiteDatabase.execSQL(createTableStatement);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    @Override
    public void logTransaction(Date date, String accountNo, ExpenseType expenseType, double amount) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        SimpleDateFormat dateFormat = new SimpleDateFormat( "dd/MM/yyyy" );

        cv.put(DATE_COLUMN, dateFormat.format(date));
        cv.put(ACCOUNT_NO_COLUMN, accountNo);
        cv.put(EXPENSE_TYPE_COLUMN, expenseType.toString());
        cv.put(AMOUNT_COLUMN, amount);

        long insert = db.insert(TRANSACTION_TABLE, null, cv);
    }

    @Override
    public List<Transaction> getAllTransactionLogs() {
        List<Transaction> transactionsList=new ArrayList<>();
        String queryString = "SELECT * FROM " + TRANSACTION_TABLE;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor result = db.rawQuery(queryString, null);
        if(result.moveToFirst()) {
            SimpleDateFormat dateFormat = new SimpleDateFormat( "dd/MM/yyyy" );
            do {
                Date date = null;
                try {
                    date = dateFormat.parse( result.getString(1) );
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                String accountNo = result.getString(2);
                ExpenseType expenseType = ExpenseType.valueOf( result.getString(3).toUpperCase() );
                double amount = result.getDouble(4);

                Transaction transaction = new Transaction(date, accountNo, expenseType, amount);
                transactionsList.add(transaction);

            } while ( result.moveToNext() );
        }
        result.close();
        db.close();

        return transactionsList;
    }

    @Override
    public List<Transaction> getPaginatedTransactionLogs(int limit) {
        List<Transaction> transactionsList=new ArrayList<>();
        String queryString = "SELECT *  FROM  " + TRANSACTION_TABLE + " ORDER BY "+TRANSACTION_ID_COLUMN+" DESC LIMIT ?;";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor result = db.rawQuery(queryString, new String[]{String.valueOf(limit)});
        if(result.moveToFirst()) {
            SimpleDateFormat dateFormat = new SimpleDateFormat( "dd/MM/yyyy" );
            do {
                Date date = null;
                try {
                    date = dateFormat.parse( result.getString(1) );
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                String accountNo = result.getString(2);
                ExpenseType expenseType = ExpenseType.valueOf( result.getString(3).toUpperCase() );
                double amount = result.getDouble(4);

                Transaction transaction = new Transaction(date, accountNo, expenseType, amount);
                transactionsList.add(transaction);

            } while ( result.moveToNext() );
        }
        result.close();
        db.close();

        return transactionsList;
    }
}
