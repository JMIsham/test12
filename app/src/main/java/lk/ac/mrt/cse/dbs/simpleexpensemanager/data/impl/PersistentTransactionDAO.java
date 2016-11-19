package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.content.Context;
import android.database.Cursor;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.TransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Transaction;

/**
 * Created by Isham on 11/19/2016.
 */

public class PersistentTransactionDAO implements TransactionDAO {
    private DBHandler db ;
    public PersistentTransactionDAO(Context ctx) {
        this.db = new DBHandler(ctx);
    }

    @Override
    public void logTransaction(Date date, String accountNo, ExpenseType expenseType, double amount) {
        db.logTransaction (date,accountNo,expenseType,amount);
    }

    @Override
    public List<Transaction> getAllTransactionLogs() throws ParseException {
        List<Transaction> lst = new ArrayList<Transaction>();
        Cursor cs = db.getAllTransactionLogs();
//        if (cs.getCount()>0){
            while(cs.moveToNext()){
                System.out.println("@@@@@@@@@@@@@@@");
                String accountNo = cs.getString(0);
                ExpenseType expenseType=ExpenseType.valueOf(cs.getString(1));
                double amount = cs.getDouble(2);
                String dates = cs.getString(3);
                DateFormat sourceFormat = new SimpleDateFormat("dd-MM-yyyy");
                String dateAsString = dates;
                Date date = sourceFormat.parse(dateAsString);
                Transaction ts = new Transaction(date,accountNo,expenseType,amount);
                lst.add(ts);

            }
//        }

        return lst;
    }

    @Override
    public List<Transaction> getPaginatedTransactionLogs(int limit) throws ParseException {
        List<Transaction> lst = new ArrayList<Transaction>();
        Cursor cs = db.getPaginatedTransactionLogs(limit);
//        if (cs.getCount()>0){
            while(cs.moveToNext()){
                System.out.println("@@@@@@@@@@@@@@@");
                String accountNo = cs.getString(0);
                ExpenseType expenseType=ExpenseType.valueOf(cs.getString(1));
                double amount = cs.getDouble(2);
                String dates = cs.getString(3);
                DateFormat sourceFormat = new SimpleDateFormat("dd-MM-yyyy");
                String dateAsString = dates;
                Date date = sourceFormat.parse(dateAsString);
                Transaction ts = new Transaction(date,accountNo,expenseType,amount);
                lst.add(ts);
            }
//        }

        return lst;
    }
}


