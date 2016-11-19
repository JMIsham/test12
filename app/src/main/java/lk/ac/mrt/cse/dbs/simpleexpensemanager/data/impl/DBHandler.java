package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.R;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.exception.InvalidAccountException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Transaction;

import static lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType.EXPENSE;
import static lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType.INCOME;

/**
 * Created by Isham on 11/19/2016.
 */

public class DBHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION =1;
    private static final String DATABASE_NAME = "140236P.db";

    private static final String TABLE_ACCOUNT = "account";
    private static final String ID = "accountID";
    private static final String BANK = "bankName";
    private static final String NAME = "accountHolderName";
    private static final String BALANCE = "balance";

    private static final String TABLE_TRANSACTION = "transactiont";
    private static final String TYPE = "expenseType";
    private static final String AMOUNT = "amount";
    private static final String DATE = "date";

    private static DBHandler single=null;
    public static DBHandler getInstance(Context context){
        if (DBHandler.single==null){
            return new DBHandler(context);
        }
        else return DBHandler.single;
    }
    public DBHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query1 = "CREATE TABLE "+ TABLE_ACCOUNT +"("+
                        ID + " TEXT PRIMARY KEY, "+
                        BANK +" TEXT NOT NULL UNIQUE, " +
                        NAME + " TEXT NOT NULL, " +
                        BALANCE + " REAL "+
                        ");";

       String query3 = "CREATE TABLE transactiont (accountID TEXT , expenseType TEXT NOT NULL, amount REAL NOT NULL, date TEXT NOT NULL, " +
               "FOREIGN KEY (accountID) REFERENCES account(accountID));";
        db.execSQL(query3);
        db.execSQL(query1);


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {

    }


    public Cursor getAccountNumbersList(){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "select "+ ID +" from "+TABLE_ACCOUNT+";";
        Cursor result= db.rawQuery(query,null);
        return result;
    }

    public Cursor getAccountsList(){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "select * from "+TABLE_ACCOUNT+";";
        Cursor result= db.rawQuery(query,null);
        return result;
    }

    public Cursor getAccount(String accountNo) throws InvalidAccountException{
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "select * from "+TABLE_ACCOUNT+" where "+ID+" = '"+ accountNo +"' limit 1;";
        Cursor result= db.rawQuery(query,null);

        if (result.getCount()==0) throw new InvalidAccountException("Invalid Account");
        else return result;
    }

    public void addAccount(Account account){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(ID,account.getAccountNo());
        cv.put(NAME,account.getAccountHolderName());
        cv.put(BANK,account.getBankName());
        cv.put(BALANCE,account.getBalance());
        db.insert(TABLE_ACCOUNT,null,cv);


    }

    public void removeAccount(String accountNo) throws InvalidAccountException{
        SQLiteDatabase db = this.getWritableDatabase();
        int rs1=db.delete(TABLE_TRANSACTION,ID+" = ?",new String[]{accountNo});
        int rs2 = db.delete(TABLE_ACCOUNT,ID+" = ?",new String[]{accountNo});
        if (rs1<0 || rs2<0) {
            throw new InvalidAccountException("Account Number"+accountNo+"Not Available");
        }


    }

    public void updateBalance(String accountNo, ExpenseType expenseType, double amount) throws InvalidAccountException{
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "select "+BALANCE+" from "+TABLE_ACCOUNT+" where "+ ID +" = '"+ accountNo +"' limit 1;";
        Cursor result= db.rawQuery(query,null);

        if (result.getCount()==0) throw new InvalidAccountException("Invalid Account");
        else {
            double current=0;
            while (result.moveToNext()){
                current = result.getDouble(0);}
            if (expenseType==EXPENSE) amount=-amount;
            current+=amount;
            ContentValues cv = new ContentValues();
            cv.put(BALANCE,current);
            db.update(TABLE_ACCOUNT,cv,ID+" = ?",new String[] {accountNo});
        }
    }

    public void logTransaction(Date date, String accountNo, ExpenseType expenseType, double amount){

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        SimpleDateFormat f = new SimpleDateFormat("dd-mm-yyyy");
        String datef = f.format(date);
        cv.put(ID,accountNo);
        cv.put(DATE,datef);
        cv.put(TYPE,expenseType.toString());
        cv.put(AMOUNT,amount);
        db.insert(TABLE_TRANSACTION,null,cv);


    }

    public Cursor getAllTransactionLogs(){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "select * from "+TABLE_TRANSACTION+";";
        Cursor result= db.rawQuery(query,null);
        return result;
    }
    public Cursor getPaginatedTransactionLogs(int limit){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "select * from transactiont limit "+limit+";";
        Cursor result= db.rawQuery(query,null);
        return result;

    }

}
