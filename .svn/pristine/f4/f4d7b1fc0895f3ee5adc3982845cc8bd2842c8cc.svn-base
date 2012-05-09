package com.tyt.bbs.provider;

import java.io.File;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import com.tyt.bbs.provider.DataColums.PostData;

public class DataContentProvider extends ContentProvider {

	private static final String TAG = "DataContentProvider";

	private static final String DATABASE_NAME = "collection.db";
	private static final int DATABASE_VERSION = 2;
	private static final String TABLE_NAME = "posts";

	private static UriMatcher sUriMatcher=null;

	private static final int POSTS = 0;
	private static final int POST_ID = 1;

	static {
		sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
		sUriMatcher.addURI(DataColums.AUTHORITY, "posts", POSTS);
		sUriMatcher.addURI(DataColums.AUTHORITY, "posts/#", POST_ID);
	}

	private DatabaseHelper mOpenHelper;


	@Override
	public boolean onCreate() {
		// TODO Auto-generated method stub
		mOpenHelper = new DatabaseHelper(getContext());
		return false;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		// TODO Auto-generated method stub
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
		switch (sUriMatcher.match(uri)) {
		case POSTS:
			qb.setTables(TABLE_NAME);
			break;
		case POST_ID:
			qb.setTables(TABLE_NAME);
			qb.appendWhere(PostData._ID + "=" + uri.getPathSegments().get(1));
			break;

		default:
			throw new IllegalArgumentException("Unknown URI " + uri);
		}

		// If no sort order is specified use the default
		String orderBy;
		if (TextUtils.isEmpty(sortOrder)) {
			orderBy = PostData.DEFAULT_SORT_ORDER;
		} else {
			orderBy = sortOrder;
		}

		// Get the database and run the query
		SQLiteDatabase db = mOpenHelper.getWritableDatabase();
		Cursor c = qb.query(db, projection, selection, selectionArgs, null, null, orderBy);

		// Tell the cursor what uri to watch, so it knows when its source data changes
		c.setNotificationUri(getContext().getContentResolver(), uri);
		return c;
	}

	@Override
	public String getType(Uri uri) {
		// TODO Auto-generated method stub
		int option = sUriMatcher.match(uri);
		switch(option) {
		case POSTS : return PostData.CONTENT_TYPE;
		case POST_ID : return PostData.CONTENT_ITEM_TYPE;
		default : throw new IllegalArgumentException("Unknown URI: "+uri);
		}
	}

	@Override
	public Uri insert(Uri uri, ContentValues initialValues) {
		// TODO Auto-generated method stub
		if (sUriMatcher.match(uri) != POSTS) {
			throw new IllegalArgumentException("Unknown URI " + uri);
		}

		ContentValues values;
		if (initialValues != null) {
			values = new ContentValues(initialValues);
		} else {
			values = new ContentValues();
		}

		SQLiteDatabase db = mOpenHelper.getReadableDatabase();
		long rowId = db.insert(TABLE_NAME, "Пе", values);
		db.close();
		if (rowId > 0) {
			Uri noteUri = ContentUris.withAppendedId(PostData.CONTENT_URI, rowId);
			getContext().getContentResolver().notifyChange(noteUri, null);
			
			return noteUri;
		}

		throw new SQLException("Failed to insert row into " + uri);
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		// TODO Auto-generated method stub
		SQLiteDatabase db = mOpenHelper.getWritableDatabase();
		int count;
		switch (sUriMatcher.match(uri)) {
		case POSTS:
			count = db.delete(TABLE_NAME, selection, selectionArgs);
			break;

		case POST_ID:
			String noteId = uri.getPathSegments().get(1);
			count = db.delete(TABLE_NAME, PostData._ID + "=" + noteId
					+ (!TextUtils.isEmpty(selection) ? " AND (" + selection + ')' : ""), selectionArgs);
			break;

		default:
			throw new IllegalArgumentException("Unknown URI " + uri);
		}

		getContext().getContentResolver().notifyChange(uri, null);
		return count;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		// TODO Auto-generated method stub
		SQLiteDatabase db = mOpenHelper.getReadableDatabase();
		int count;
		switch (sUriMatcher.match(uri)) {
		case POSTS:
			count = db.update(TABLE_NAME, values, selection, selectionArgs);
			break;

		case POST_ID:
			String noteId = uri.getPathSegments().get(1);
			count = db.update(TABLE_NAME, values, PostData._ID + "=" + noteId
					+ (!TextUtils.isEmpty(selection) ? " AND (" + selection + ')' : ""), selectionArgs);
			break;

		default:
			throw new IllegalArgumentException("Unknown URI " + uri);
		}

		getContext().getContentResolver().notifyChange(uri, null);
		return count;
	}

}
