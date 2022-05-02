package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;

public class DataBaseHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "190376N_db";

    public DataBaseHelper(Context context) {
        super(context, DATABASE_NAME, null,  DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createAccount = "CREATE TABLE accounts(accountNo text primary key, bankName text, accountHolderName text, balance real);";
        db.execSQL(createAccount);

        String createTransation = "CREATE TABLE transactions(transactionId INTEGER PRIMARY KEY  AUTOINCREMENT, date text, accountNo text, type text, amount real);";
        db.execSQL(createTransation);


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS account");
        db.execSQL("DROP TABLE IF EXISTS transactions");
        // Create tables again
        onCreate(db);
    }
}
