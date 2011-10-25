package com.silvermongoose.ecc_ff;

import java.sql.SQLData;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class Tech_DB_Adapter {
	public static final String KEY_ROWID = "_id";
	public static final String KEY_TECHID = "techID";
	public static final String KEY_TECHFIRSTNAME = "techFirstName";
	public static final String KEY_TECHLASTNAME = "techLastName";
	public static final String KEY_TECHEMAIL = "techEmail";
	public static final String KEY_TECHPHONE = "techPhone";
	private static final String DATABASE_TABLE = "tech";

	private Context context;
	private SQLiteDatabase database;
	private Tech_DB_Handler techDBHandler;
	
	public Tech_DB_Adapter(Context context){
		this.context = context;
	}
	
	public Tech_DB_Adapter open() throws SQLException {
		techDBHandler = new Tech_DB_Handler(context);
		database = techDBHandler.getWritableDatabase();
		return this;
	}
	
	public void close() {
		techDBHandler.close();
	}
	
	public long createTech(int techID, String techFirstName, String techLastName, String techEmail, String techPhone) {
		ContentValues initialValues = createContentValues(techID, techFirstName, techLastName, techEmail, techPhone);
		
		return database.insert(DATABASE_TABLE, null, initialValues);
	}
	
	public boolean updateTech(long rowId, int techID, String techFirstName, String techLastName, String techEmail, String techPhone) {
		ContentValues updateValues = createContentValues(techID, techFirstName, techLastName, techEmail, techPhone);
		return database.update(DATABASE_TABLE, updateValues, KEY_ROWID + "=" + rowId, null) > 0;
	}
	
	public boolean deleteTech(long rowId) {
		return database.delete(DATABASE_TABLE, KEY_ROWID + "=" + rowId, null) > 0;
	}
	
	public Cursor fetchAllTodos() {
		return database.query(DATABASE_TABLE, new String[] {KEY_ROWID, KEY_TECHID, KEY_TECHFIRSTNAME, KEY_TECHLASTNAME, KEY_TECHEMAIL, KEY_TECHPHONE}, null, null, null, null, null);
	}
	
	public Cursor fetchTech(int techID) throws SQLException {
		Cursor mCursor = database.query(true, DATABASE_TABLE, 
										new String[] {KEY_ROWID, KEY_TECHID, KEY_TECHFIRSTNAME, KEY_TECHLASTNAME, KEY_TECHEMAIL, KEY_TECHPHONE}, 
										KEY_TECHID + "=" + techID, null, null, null, null, null);
		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		return mCursor;
	}
	
	private ContentValues createContentValues(int techID, String techFirstName, String techLastName, String techEmail, String techPhone) {
		ContentValues values = new ContentValues();
		values.put(KEY_TECHID, techID);
		values.put(KEY_TECHFIRSTNAME, techFirstName);
		values.put(KEY_TECHLASTNAME, techLastName);
		values.put(KEY_TECHEMAIL, techEmail);
		values.put(KEY_TECHPHONE, techPhone);
		return values;
	}
}
