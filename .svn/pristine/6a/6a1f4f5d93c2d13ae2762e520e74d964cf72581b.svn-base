package com.tyt.bbs.provider;

import java.io.File;

import com.tyt.bbs.provider.DataColums.PostData;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.util.Log;

public class DatabaseHelper{
	private String path;

	private static final String DATABASE_NAME = "collection";
	private static final int DATABASE_VERSION = 1;
	private static final String TABLE_NAME = "posts";
	private static final String TAG = "DatabaseHelper";
	private final Context mContext;
	private SQLiteDatabase mDatabase = null;
	private boolean mIsInitializing = false;

	/**
	 * Create a helper object to create, open, and/or manage a database.
	 * The database is not actually created or opened until one of
	 * {@link #getWritableDatabase} or {@link #getReadableDatabase} is called.
	 *
	 * @param context to use to open or create the database
	 * @param name of the database file, or null for an in-memory database
	 * @param factory to use for creating cursor objects, or null for the default
	 * @param version number of the database (starting at 1); if the database is older,
	 *     {@link #onUpgrade} will be used to upgrade the database
	 */
	public DatabaseHelper(Context context) {
		mContext = context;
		if(android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)){

			//获取SDCard目录
			java.io.File SDFile=android.os.Environment.getExternalStorageDirectory();
			//创建文件夹目录
			java.io.File desDir=new java.io.File(SDFile.getAbsolutePath()+java.io.File.separatorChar+"bbs");
			//文件夹不存在则创建新文件夹
			if(!desDir.exists()) desDir.mkdir();
			path =desDir.getAbsoluteFile()+"/"+DATABASE_NAME;
		};
	}

	private  String createTable(String tableName, String[] columns, String[] types) {
		StringBuilder stringBuilder = new StringBuilder("CREATE TABLE IF NOT EXISTS ");

		stringBuilder.append(tableName);
		stringBuilder.append(" (");
		for (int n = 0, i = columns.length; n < i; n++) {
			if (n > 0) {
				stringBuilder.append(", ");
			}
			stringBuilder.append(columns[n]).append(' ').append(types[n]);
		}
		return stringBuilder.append(");").toString();

	}

	/**
	 * Create and/or open a database that will be used for reading and writing.
	 * Once opened successfully, the database is cached, so you can call this
	 * method every time you need to write to the database.  Make sure to call
	 * {@link #close} when you no longer need it.
	 *
	 * <p>Errors such as bad permissions or a full disk may cause this operation
	 * to fail, but future attempts may succeed if the problem is fixed.</p>
	 *
	 * @throws SQLiteException if the database cannot be opened for writing
	 * @return a read/write database object valid until {@link #close} is called
	 */
	public synchronized SQLiteDatabase getWritableDatabase() {
		if (mDatabase != null && mDatabase.isOpen() && !mDatabase.isReadOnly()) {
			return mDatabase;  // The database is already open for business
		}

		if (mIsInitializing) {
			throw new IllegalStateException("getWritableDatabase called recursively");
		}

		// If we have a read-only database open, someone could be using it
		// (though they shouldn't), which would cause a lock to be held on
		// the file, and our attempts to open the database read-write would
		// fail waiting for the file lock.  To prevent that, we acquire the
		// lock on the read-only database, which shuts out other users.
		SQLiteDatabase db = null;
		try {
			mIsInitializing = true;
			db = SQLiteDatabase.openOrCreateDatabase(path,null);
			  int version = db.getVersion();
			  if(version==0){
				  db.execSQL(createTable(TABLE_NAME, PostData.COLUMNS, PostData.TYPES));
				  db.setVersion(DATABASE_VERSION);
			  }
				mDatabase = db;
			return mDatabase;
		} finally {
			mIsInitializing = false;
			if (db != null && db != mDatabase) db.close();
		}
	}

	public synchronized SQLiteDatabase getReadableDatabase() {
		if (mDatabase != null && mDatabase.isOpen()) {
			return mDatabase;  // The database is already open for business
		}

		if (mIsInitializing) 			throw new IllegalStateException("getReadableDatabase called recursively");
		
		try {
			return getWritableDatabase();
		} catch (SQLiteException e) {
			Log.e(TAG, "Couldn't open " +  " for writing (will try read-only):", e);
		}

		SQLiteDatabase db = null;
		try {
			mIsInitializing = true;
			db = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.OPEN_READONLY);
			mDatabase = db;
			return mDatabase;
		} finally {
			mIsInitializing = false;
			if (db != null && db != mDatabase) db.close();
		}
	}


	public synchronized void close() {
		if (mIsInitializing) throw new IllegalStateException("Closed during initialization");

		if (mDatabase != null && mDatabase.isOpen()) {
			mDatabase.close();
			mDatabase = null;
		}
	}

}
