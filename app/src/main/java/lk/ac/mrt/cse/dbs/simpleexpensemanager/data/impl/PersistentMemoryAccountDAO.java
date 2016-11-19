package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.AccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.exception.InvalidAccountException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;

/**
 * Created by Isham on 11/19/2016.
 */

public class PersistentMemoryAccountDAO implements AccountDAO {

    private  DBHandler db;

    public PersistentMemoryAccountDAO(Context ct) {
        this.db = DBHandler.getInstance(ct);
    }

    @Override


    public List<String> getAccountNumbersList() {
        Cursor cs=db.getAccountNumbersList();
        List<String> lst = new ArrayList<String>();
        if (cs.getCount()>0){
            while (cs.moveToNext()){
                lst.add(cs.getString(0));
                System.out.println(cs.getString(0));
            }
        }
        return lst;

    }

    @Override
    public List<Account> getAccountsList() {
        Log.i("Message","inside calling3");
        Cursor cs=db.getAccountsList();
        List<Account> lst = new ArrayList<Account>();
        if (cs.getCount()>0){
            while(cs.moveToNext()){
                String accountNo=cs.getString(0);
                String bankName=cs.getString(1);
                String accountHolderName=cs.getString(2);
                double balance=cs.getDouble(3);
                Account ac = new Account(accountNo,bankName,accountHolderName,balance);
                lst.add(ac);
            }
        }
        return lst;


    }

    @Override
    public Account getAccount(String accountNo) throws InvalidAccountException {
        Cursor cs=db.getAccount(accountNo);
            if (cs.getCount()>0){
                while(cs.moveToNext()){
                    String accountno=cs.getString(0);
                    String bankName=cs.getString(1);
                    String accountHolderName=cs.getString(2);
                    double balance=cs.getDouble(3);
                    Account ac = new Account(accountNo,bankName,accountHolderName,balance);
                    return ac;
                }
            }
        return null;
    }

    @Override
    public void addAccount(Account account) {
        db.addAccount(account);

    }

    @Override
    public void removeAccount(String accountNo) throws InvalidAccountException {
        db.removeAccount(accountNo);
    }

    @Override
    public void updateBalance(String accountNo, ExpenseType expenseType, double amount) throws InvalidAccountException {
        db.updateBalance(accountNo,expenseType,amount);
    }
}
