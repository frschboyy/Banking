package com.example.banking;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

public class DBSupport extends SQLiteOpenHelper {
    public static final String dbName = "bankUser.db";

    public DBSupport(Context context) {
        super(context, dbName, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase MyDB) {
        MyDB.execSQL("create table users (id TEXT primary key, name TEXT, phone TEXT, email TEXT, password TEXT, foreign key (email) references bankDetails(email))");
        MyDB.execSQL("create Table bankDetails(email TEXT primary key, balance DECIMAL (10,2) )");
    }

    @Override
    public void onUpgrade(SQLiteDatabase MyDB, int i, int i1) {
        MyDB.execSQL("drop Table if exists users");
    }

    public Boolean insertData(String id, String name, String phone, String mail, String pass){
        SQLiteDatabase MyDB = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("id", id);
        cv.put("name", name);
        cv.put("phone", phone);
        cv.put("email", mail);
        cv.put("password", pass);
        long result = MyDB.insert("users", null, cv);
        return result != -1;
    }

    public Boolean initializeAccount(String email){
        SQLiteDatabase MyDB = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("email", email);
        cv.put("balance", 0.00);
        long result = MyDB.insert("bankDetails", null, cv);
        return result != -1;
    }

    public Boolean checkID(String id){
        SQLiteDatabase MyDB = this.getWritableDatabase();
        Cursor cursor = MyDB.rawQuery("Select * from users where id = ?", new String[]{id});
        return cursor.getCount() > 0;
    }

    public Boolean checkEmail(String email){
        SQLiteDatabase MyDB = this.getWritableDatabase();
        Cursor cursor = MyDB.rawQuery("Select * from users where email = ?", new String[]{email});
        return cursor.getCount() > 0;
    }

    public Boolean checkEmailPassword(String email, String password){
        SQLiteDatabase MyDB = this.getWritableDatabase();
        Cursor cursor = MyDB.rawQuery("Select * from users where email = ? and password = ?", new String[]{email,password});
        return cursor.getCount() > 0;
    }

    public void deleteUser(String email){
        SQLiteDatabase MyDB = this.getWritableDatabase();
        int deletedRows = MyDB.delete("users", "email = ?", new String[]{email});
    }

    public Boolean deposit(double amount, String email){
        SQLiteDatabase MyDB = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        // update balance in bankDetails table
        Cursor cursor = MyDB.rawQuery("Select balance from bankDetails where email = ?", new String[]{email});
        double currentBalance = 0.00;
        if(cursor.moveToFirst()){
            currentBalance = cursor.getDouble(0);
        }
        cursor.close();

        // update balance value
        double newBalance = currentBalance + amount;
        cv.put("balance", newBalance);

        // update balance in table
        int updatedRows = MyDB.update("bankDetails", cv, "email = ?", new String[]{email});
        return updatedRows > 0;
    }

    public Boolean withdraw(double amount, String email){
        SQLiteDatabase MyDB = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        // Update the balance in bankDetails table
        Cursor cursor = MyDB.rawQuery("Select balance from bankDetails where email = ?", new String[]{email});
        double currentBalance = 0.00;
        if (cursor.moveToFirst()) {
            currentBalance = cursor.getDouble(0);
        }
        cursor.close();

        // check if withdrawal is possible
        if (currentBalance < amount) {
            return false; // Insufficient funds
        }

        // update balance value
        double newBalance = currentBalance - amount;
        cv.put("balance", newBalance);

        // update balance in table
        int updatedRows = MyDB.update("bankDetails", cv, "email = ?", new String[]{email});
        return updatedRows > 0;
    }

    public double checkBalance(String email) {
        SQLiteDatabase MyDB = this.getWritableDatabase();

        // retrieve balance from table
        Cursor cursor = MyDB.rawQuery("Select balance from bankDetails where email = ?", new String[]{email});
        double balance = 0.00;
        if(cursor.moveToFirst()){
            balance = cursor.getDouble(0);
        }
        cursor.close();

        return balance;
    }

    public Cursor getUserData(String email){
        SQLiteDatabase MyDB = this.getWritableDatabase();
        Cursor cursor = MyDB.rawQuery("select name, phone from users where email = ?", new String[]{email});
        return cursor;
    }
}