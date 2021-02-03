package lk.ac.mrt.cse.dbs.simpleexpensemanager.control;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {
    //  Account table name
    public static final String ACCOUNT_TABLE = "account";
    //  Account table columns name
    public static final String BANK_NAME_COLUMN = "bankName";
    public static final String ACCOUNT_NO_COLUMN = "accountNo";
    public static final String NAME_COLUMN = "accountHolderName";
    public static final String BALANCE = "balance";

    //  Transaction table name
    public static final String TRANSACTION_TABLE = "account_transaction";
    //  Transaction table columns name
    public static final String TRANSACTION_ID_COLUMN = "transactionId";
    public static final String DATE_COLUMN = "date";
    //public static final String ACCOUNT_NO_COLUMN = "accountNo";   :- this column exist in two tables
    public static final String EXPENSE_TYPE_COLUMN = "expenseType";
    public static final String AMOUNT_COLUMN = "amount";

    //  Account table create query
    public static final String createAccountTableStatement = "CREATE TABLE " + ACCOUNT_TABLE + " (" +
            ACCOUNT_NO_COLUMN + " TEXT PRIMARY KEY, " +
            BANK_NAME_COLUMN + " TEXT, " +
            NAME_COLUMN + " TEXT, " +
            BALANCE + " REAL)";

    //  Transaction table create query
    public static final String createTransactionTableStatement = "CREATE TABLE " + TRANSACTION_TABLE + " ( " +
            TRANSACTION_ID_COLUMN + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            DATE_COLUMN + " TEXT," +
            ACCOUNT_NO_COLUMN + " TEXT," +
            EXPENSE_TYPE_COLUMN + " TEXT," +
            AMOUNT_COLUMN + " REAL);";

    public DatabaseHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        // creating required tables
        sqLiteDatabase.execSQL(createAccountTableStatement);
        sqLiteDatabase.execSQL(createTransactionTableStatement);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        // on upgrade drop older tables
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + ACCOUNT_TABLE);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TRANSACTION_TABLE);

        // create new tables
        onCreate(sqLiteDatabase);
    }
}
