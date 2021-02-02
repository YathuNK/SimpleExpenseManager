package lk.ac.mrt.cse.dbs.simpleexpensemanager.control;

import android.content.Context;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.AccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.TransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.PersistentAccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.PersistentTransactionDAO;

public class PersistentExpenseManager extends ExpenseManager {
    private Context context;
    private String database="180722N";
    public PersistentExpenseManager(Context context) {
        this.context=context;
        setup();
    }

    @Override
    public void setup() {
        /*** Begin generating persistent manager implementation ***/

        TransactionDAO persistentTransactionDAO = new PersistentTransactionDAO(this.context,database,null,1);
        setTransactionsDAO(persistentTransactionDAO);

        AccountDAO persistentAccountDAO = new PersistentAccountDAO(this.context,"180722NN",null,1);
        setAccountsDAO(persistentAccountDAO);

        /*** End ***/
    }
}
