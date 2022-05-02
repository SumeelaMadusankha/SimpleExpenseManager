package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.AccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.exception.InvalidAccountException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;

public class PersistentAccountDAO extends DataBaseHelper implements AccountDAO {
    private List<String> accountNoList;
    private List<Account> accountsList;

    public PersistentAccountDAO(Context context) {
        super(context);
        this.accountNoList = new ArrayList<String>();
        this.accountsList = new ArrayList<Account>();
    }

    @Override
    public List<String> getAccountNumbersList() {
        this.accountNoList = new ArrayList<String>();

        String sql = "SELECT accountNo FROM accounts";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(sql,null);

        if (cursor.moveToFirst()){

            do{
               this.accountNoList.add(cursor.getString(0));
            }while(cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return this.accountNoList;
    }

    @Override
    public List<Account> getAccountsList() {
        this.accountsList = new ArrayList<Account>();

        String sql = "SELECT * FROM accounts";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(sql,null);

        if (cursor.moveToFirst()){

            do{
                String accountNo = cursor.getString(0);
                String bankName = cursor.getString(1);
                String accountHolderName = cursor.getString(2);
                double balance = cursor.getInt(3);

                Account account = new Account(accountNo,bankName,accountHolderName,balance);
                this.accountsList.add(account);


            }while(cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return this.accountsList;
    }

    @Override
    public Account getAccount(String accountNo) throws InvalidAccountException {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM accounts WHERE accountNo = '"+ accountNo + "' ;";
        Cursor cursor = db.rawQuery(query,null);
        Account account = null;

        String bankName = cursor.getString(1);
        String accountHolderName = cursor.getString(2);
        double balance = cursor.getInt(3);

        account = new Account(accountNo,bankName,accountHolderName,balance);

        cursor.close();
        db.close();
        return account;
    }

    @Override
    public void addAccount(Account account) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put("accountNo", account.getAccountNo());
        cv.put("bankName", account.getBankName());
        cv.put("accountHolderName", account.getAccountHolderName());
        cv.put("balance", account.getBalance());

        db.insert("accounts", null, cv);
    }

    @Override
    public void removeAccount(String accountNo) throws InvalidAccountException {
        String query = "DELETE FROM accounts WHERE accountNo = '"+ accountNo + "' ;";
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(query);
    }

    @Override
    public void updateBalance(String accountNo, ExpenseType expenseType, double amount) throws InvalidAccountException {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT balance FROM accounts WHERE accountNo = '"+ accountNo +"' ;";
        Cursor cursor = db.rawQuery(query,null);
        cursor.moveToFirst();
        double balance = cursor.getDouble(0);
        switch(expenseType){
            case EXPENSE:
                balance  -= amount;
                break;
            case INCOME:
                balance  += amount;
                break;
        }

        String updateQuery = "UPDATE accounts SET balance = "+ balance +" WHERE accountNo = '"+accountNo+"' ;";
        db.execSQL(updateQuery);
        cursor.close();
        db.close();

    }
}
