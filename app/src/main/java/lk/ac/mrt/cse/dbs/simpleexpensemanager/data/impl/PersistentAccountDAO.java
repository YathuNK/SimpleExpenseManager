package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.control.DatabaseHelper;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.AccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.exception.InvalidAccountException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;


public class PersistentAccountDAO implements AccountDAO {
    public static final String ACCOUNT_TABLE = "account";
    public static final String BANK_NAME_COLUMN = "bankName";
    public static final String ACCOUNT_NO_COLUMN = "accountNo";
    public static final String NAME_COLUMN = "accountHolderName";
    public static final String BALANCE = "balance";

    DatabaseHelper databaseHelper;

    public PersistentAccountDAO(DatabaseHelper databaseHelper) {
        this.databaseHelper=databaseHelper;
    }

    @Override
    public List<String> getAccountNumbersList() {
        List<String> accountNoList= new ArrayList<>();
        String queryString = "SELECT "+ ACCOUNT_NO_COLUMN +" FROM " + ACCOUNT_TABLE;
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
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
        String queryString = "SELECT * FROM " + ACCOUNT_TABLE;
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
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
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
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
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        DecimalFormat decimalFormat=new DecimalFormat("##.00");

        cv.put(ACCOUNT_NO_COLUMN, account.getAccountNo());
        cv.put(BANK_NAME_COLUMN, account.getBankName());
        cv.put(NAME_COLUMN, account.getAccountHolderName());
        cv.put(BALANCE, decimalFormat.format(account.getBalance()));

        db.insert(ACCOUNT_TABLE, null, cv);

    }

    @Override
    public void removeAccount(String accountNo) throws InvalidAccountException {
        SQLiteDatabase db=databaseHelper.getWritableDatabase();
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
        String queryString = "SELECT " + BALANCE + " FROM " + ACCOUNT_TABLE + " WHERE " + ACCOUNT_NO_COLUMN + "=?;";
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        Cursor result = db.rawQuery(queryString, new String[]{accountNo});
        if (result.moveToFirst()) {
            double balance = result.getDouble(0);

            result.close();
            db.close();

            db = databaseHelper.getWritableDatabase();
            ContentValues values = new ContentValues();

            switch (expenseType) {
                case EXPENSE:
                    balance = balance - amount;
                    break;
                case INCOME:
                    balance = balance + amount;
                    break;
            }
            DecimalFormat decimalFormat=new DecimalFormat("##.00");
            values.put(BALANCE, decimalFormat.format(balance));
            db.update(ACCOUNT_TABLE, values, ACCOUNT_NO_COLUMN + " = ?", new String[]{accountNo});

            db.close();

        } else {
            db.close();
            String msg = "Account " + accountNo + " is invalid.";
            throw new InvalidAccountException(msg);
        }
    }
}

   /* @Override
    public void updateBalance(String accountNo, ExpenseType expenseType, double amount) throws InvalidAccountException {
        SQLiteDatabase db=databaseHelper.getWritableDatabase();

        String operation= "+";
        if (expenseType == ExpenseType.EXPENSE) {
            operation="-";
        }
        String queryString = "UPDATE " + ACCOUNT_TABLE + " SET " + BALANCE + "="+ BALANCE +operation+" ? WHERE "+ACCOUNT_NO_COLUMN+"=?;";
        Cursor result = db.rawQuery(queryString, new String[]{String.valueOf(amount),accountNo});

        result.close();
        db.close();
    }
}*/