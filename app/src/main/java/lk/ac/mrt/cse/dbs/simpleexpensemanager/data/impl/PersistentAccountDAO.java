package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.AccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.exception.InvalidAccountException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;


public class PersistentAccountDAO extends SQLiteOpenHelper implements AccountDAO {
    public static final String ACCOUNT_TABLE = "account";
    public static final String BANK_NAME_COLUMN = "bankName";
    public static final String ACCOUNT_NO_COLUMN = "accountNo";
    public static final String NAME_COLUMN = "accountHolderName";
    public static final String BALANCE = "balance";

    public PersistentAccountDAO(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String createTableStatement = "CREATE TABLE " + ACCOUNT_TABLE + " (" +
                ACCOUNT_NO_COLUMN + " STRING PRIMARY KEY, " +
                BANK_NAME_COLUMN + " STRING, " +
                NAME_COLUMN + " STRING, " +
                BALANCE + " REAL)";
        sqLiteDatabase.execSQL(createTableStatement);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    @Override
    public List<String> getAccountNumbersList() {
        List<String> accountNoList= new ArrayList<>();
        String queryString = "SELECT "+ ACCOUNT_NO_COLUMN +" FROM " + ACCOUNT_TABLE;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor result = db.rawQuery(queryString, null);
        if(result.moveToFirst()) {
            do {
                String accountNo = result.getString(0);
                accountNoList.add(accountNo);

            } while ( result.moveToNext() );
        }
        result.close();
        db.close();

        return accountNoList;
    }

    @Override
    public List<Account> getAccountsList() {
        List<Account> accountsList= new ArrayList<>();
        String queryString = "SELECT "+ ACCOUNT_NO_COLUMN +" FROM " + ACCOUNT_TABLE;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor result = db.rawQuery(queryString, null);
        if(result.moveToFirst()) {
            do {
                String accountNo = result.getString(0);
                String bankName = result.getString(1);
                String accountHolderName = result.getString(2);
                double balance = result.getDouble(3);

                Account account = new Account(accountNo, bankName, accountHolderName, balance);
                accountsList.add(account);

            } while ( result.moveToNext() );
        }
        result.close();
        db.close();

        return accountsList;
    }
    

    @Override
    public Account getAccount(String accountNo) throws InvalidAccountException {
        String queryString = "SELECT * FROM " + ACCOUNT_TABLE + " WHERE " + ACCOUNT_NO_COLUMN + "=?;";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor result = db.rawQuery(queryString, new String[]{accountNo});
        if(result.moveToFirst()) {
            String bankName = result.getString(1);
            String accountHolderName = result.getString(2);
            double balance = result.getDouble(3);

            Account account = new Account(accountNo, bankName, accountHolderName, balance);

            result.close();
            db.close();

            return account;
        }else{
            String msg = "Account " + accountNo + " is invalid.";
            throw new InvalidAccountException(msg);
        }

    }

    @Override
    public void addAccount(Account account) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(ACCOUNT_NO_COLUMN, account.getAccountNo());
        cv.put(BANK_NAME_COLUMN, account.getBankName());
        cv.put(NAME_COLUMN, account.getAccountHolderName());
        cv.put(BALANCE, account.getBalance());

        db.insert(ACCOUNT_TABLE, null, cv);

    }

    @Override
    public void removeAccount(String accountNo) throws InvalidAccountException {
        SQLiteDatabase db=this.getWritableDatabase();
        String query ="DELETE FROM "+ ACCOUNT_TABLE +" WHERE "+ACCOUNT_NO_COLUMN+"=?;";

        Cursor result=db.rawQuery(query,new String[] {accountNo});

        if (!(result.moveToFirst())){
            String msg = "Account " + accountNo + " is invalid.";
            throw new InvalidAccountException(msg);
        }
        result.close();
        db.close();

    }

    @Override
    public void updateBalance(String accountNo, ExpenseType expenseType, double amount) throws InvalidAccountException {
        SQLiteDatabase db=this.getWritableDatabase();
        String operation= "+";
        if (expenseType == ExpenseType.EXPENSE) {
            operation="-";
        }
        String queryString = "UPDATE " + ACCOUNT_TABLE + " SET " + BALANCE + "="+ BALANCE +operation+" ? WHERE "+ACCOUNT_NO_COLUMN+"=?;";
        Cursor result = db.rawQuery(queryString, new String[]{String.valueOf(amount),accountNo});

        result.close();
        db.close();
    }
}
