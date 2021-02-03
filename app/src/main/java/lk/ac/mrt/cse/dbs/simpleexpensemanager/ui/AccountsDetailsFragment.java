package lk.ac.mrt.cse.dbs.simpleexpensemanager.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.R;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.control.ExpenseManager;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Transaction;

import static lk.ac.mrt.cse.dbs.simpleexpensemanager.Constants.EXPENSE_MANAGER;

public class AccountsDetailsFragment extends Fragment {
    private ExpenseManager currentExpenseManager;

    public static AccountsDetailsFragment newInstance(ExpenseManager expenseManager) {
        AccountsDetailsFragment accountsDetailsFragment = new AccountsDetailsFragment();
        Bundle args = new Bundle();
        args.putSerializable(EXPENSE_MANAGER, expenseManager);
        accountsDetailsFragment.setArguments(args);
        return accountsDetailsFragment;
    }

    public AccountsDetailsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_account_details, container, false);
        TableLayout accountTableLayout = (TableLayout) rootView.findViewById(R.id.account_table);
        TableRow tableRowHeader = (TableRow) rootView.findViewById(R.id.account_table_header);

        currentExpenseManager = (ExpenseManager) getArguments().get(EXPENSE_MANAGER);
        List<Account> accountList = new ArrayList<>();
        if (currentExpenseManager != null) {
            accountList = currentExpenseManager.getAccountsList();
        }
        generateTransactionsTable(rootView, accountTableLayout, accountList);
        return rootView;
    }

    private void generateTransactionsTable(View rootView, TableLayout accountTableLayout,
                                           List<Account> accountList) {
        for (Account account : accountList) {
            TableRow tr = new TableRow(rootView.getContext());

            TextView lAccountVal = new TextView(rootView.getContext());
            lAccountVal.setText(account.getAccountNo());
            tr.addView(lAccountVal);

            TextView lBankVal = new TextView(rootView.getContext());
            lBankVal.setText(account.getBankName());
            tr.addView(lBankVal);

            TextView lAccountHolderVal = new TextView(rootView.getContext());
            lAccountHolderVal.setText(account.getAccountHolderName());
            tr.addView(lAccountHolderVal);

            TextView lBalanceVal = new TextView(rootView.getContext());
            lBalanceVal.setText(String.valueOf(account.getBalance()));
            tr.addView(lBalanceVal);

            accountTableLayout.addView(tr);
        }
    }
}