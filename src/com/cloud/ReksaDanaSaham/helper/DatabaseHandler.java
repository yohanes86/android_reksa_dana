package com.cloud.ReksaDanaSaham.helper;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.cloud.ReksaDanaSaham.data.JenisVO;

public class DatabaseHandler extends SQLiteOpenHelper {
	// All Static variables
	// Database Version
	private static final int DATABASE_VERSION = 1;

	// Database Name
	private static final String DATABASE_NAME = "ReksaDanaSaham.db";

	// Contacts table name
	private static final String TABLE_REKSA_DANA = "table_reksa_dana";
	private static final String TABLE_SAHAM 	 = "table_saham";

	// Contacts Table Columns names
	private static final String KEY_ID = "id";
	private static final String KEY_LIST_REKSA_DANA = "list_reksa_dana";
	private static final String KEY_LIST_SAHAM = "list_saham";

	public DatabaseHandler(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	private final static String SQL_CREATE_REKSA_DANA_TABLE = "CREATE TABLE "
			+ TABLE_REKSA_DANA + "(" + KEY_ID
			+ " INTEGER PRIMARY KEY autoincrement," + KEY_LIST_REKSA_DANA
			+ " TEXT not null " + ")";
	
	private final static String SQL_CREATE_SAHAM_TABLE = "CREATE TABLE "
			+ TABLE_SAHAM + "(" + KEY_ID
			+ " INTEGER PRIMARY KEY autoincrement," + KEY_LIST_SAHAM + " TEXT not null" + ")";

	// Creating Tables
	@Override
	public void onCreate(SQLiteDatabase db) {
		// String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_JENIS + "("
		// + KEY_ID + " INTEGER PRIMARY KEY," + KEY_NAME + " TEXT,"
		// + KEY_PH_NO + " TEXT" + ")";
		db.execSQL(SQL_CREATE_REKSA_DANA_TABLE);
		db.execSQL(SQL_CREATE_SAHAM_TABLE);
	}

	// Upgrading database
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// Drop older table if existed
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_REKSA_DANA);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_SAHAM);

		// Create tables again
		onCreate(db);
	}

	/* add for table jenis */
	public void addReksaDana(JenisVO jenis) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_LIST_REKSA_DANA, jenis.getReksaDana()); // reksa dana

		// Inserting Row
		db.insert(TABLE_REKSA_DANA, null, values);
		db.close(); // Closing database connection
	}
	
	public void addSaham(JenisVO jenis) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_LIST_SAHAM, jenis.getSaham()); // saham

		// Inserting Row
		db.insert(TABLE_SAHAM, null, values);
		db.close(); // Closing database connection
	}

	// Getting All table jenis
	public List<String> getAllListReksaDana() {
		List<String> listJenis = new ArrayList<String>();
		// Select All Query
		String selectQuery = "SELECT  * FROM " + TABLE_REKSA_DANA;

		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
//				JenisVO jenis = new JenisVO();
//				jenis.setId(Integer.parseInt(cursor.getString(0)));
//				jenis.setReksaDana(cursor.getString(1));
				// Adding contact to list
				listJenis.add(cursor.getString(1));
			} while (cursor.moveToNext());
		}

		// return list jenis
		return listJenis;
	}
	
	// Getting All table jenis
		public List<String> getAllListSaham() {
			List<String> listJenis = new ArrayList<String>();
			// Select All Query
			String selectQuery = "SELECT  * FROM " + TABLE_SAHAM;

			SQLiteDatabase db = this.getWritableDatabase();
			Cursor cursor = db.rawQuery(selectQuery, null);

			// looping through all rows and adding to list
			if (cursor.moveToFirst()) {
				do {
//					JenisVO jenis = new JenisVO();
//					jenis.setId(Integer.parseInt(cursor.getString(0)));
//					jenis.setSaham(cursor.getString(1));
					// Adding contact to list
					listJenis.add(cursor.getString(1));
				} while (cursor.moveToNext());
			}

			// return list jenis
			return listJenis;
		}
}
