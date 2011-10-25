package com.silvermongoose.ecc_ff;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class Tech_DB_Handler extends SQLiteOpenHelper {
	
	private static final String DATABASE_NAME = "eccticket_db";
	private static final int DATABASE_VERSION = 1;
	
	private static final String DATABASE_CREATE = "create table tech (_id integer primary key autoincrement," 
													+ " techID integer not null,"
													+ " techFirstName text not null,"
													+ " techLastName text not null,"
													+ " techEmail text," 
													+ " techPhone text);";

	public Tech_DB_Handler(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(DATABASE_CREATE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.w(Tech_DB_Handler.class.getName(), "Upgrading database from version " + oldVersion + " to " + newVersion + ", which will destroy all old data.");
		db.execSQL("DROP TABLE IF EXIST tech");
		onCreate(db);
	}

}
