package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.control.DatabaseHelper;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.TransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Transaction;

public class PersistentTransactionDAO implements TransactionDAO {
    public static final String TRANSACTION_TABLE = "account_transaction";
    public static final String TRANSACTION_ID_COLUMN = "transactionId";
    public static final String DATE_COLUMN = "date";
    public static final String ACCOUNT_NO_COLUMN = "accountNo";
    public static final String EXPENSE_TYPE_COLUMN = "expenseType";
    public static final String AMOUNT_COLUMN = "amount";

    DatabaseHelper databaseHelper;
    public PersistentTransactionDAO(DatabaseHelper databaseHelper) {
        this.databaseHelper=databaseHelper;
    }

    @Override
    public void logTransaction(Date date, String accountNo, ExpenseType expenseType, double amount) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        SimpleDateFormat dateFormat = new SimpleDateFormat( "dd/MM/yyyy" );

        cv.put(DATE_COLUMN, dateFormat.format(date));
        cv.put(ACCOUNT_NO_COLUMN, accountNo);
        cv.put(EXPENSE_TYPE_COLUMN, expenseType.toString());
        cv.put(AMOUNT_COLUMN, amount);

        db.insert(TRANSACTION_TABLE, null, cv);
    }

    @Override
    public List<Transaction> getAllTransactionLogs() {
        List<Transaction> transactionsList=new ArrayList<>();
        String queryString = "SELECT * FROM " + TRANSACTION_TABLE;
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
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
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
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
