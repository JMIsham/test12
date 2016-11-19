package lk.ac.mrt.cse.dbs.simpleexpensemanager.control;

import android.content.Context;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.control.exception.ExpenseManagerException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.AccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.TransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.InMemoryAccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.InMemoryTransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.PersistentMemoryAccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.PersistentTransactionDAO;

/**
 * Created by Isham on 11/19/2016.
 */

public class PersistentExpenseManager extends ExpenseManager {
    private Context ctx;
    public PersistentExpenseManager(Context ct) throws ExpenseManagerException {
        this.ctx = ct;
        System.out.println("inside expenxe manaeer");
        setup();
    }


    @Override
    public void setup() throws ExpenseManagerException {
        /*** Begin generating dummy data for In-Memory implementation ***/

        TransactionDAO perMemoryTransactionDAO = new PersistentTransactionDAO(ctx);
        setTransactionsDAO(perMemoryTransactionDAO);

        AccountDAO perMemoryAccountDAO = new PersistentMemoryAccountDAO(ctx);
        setAccountsDAO(perMemoryAccountDAO);

    }
}
