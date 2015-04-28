package sosis.AndroidApps.Provider;

import database.HoDatabaseHelper;
import database.HoTable;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;

// Provider used from application to interact with contact info DB 
// DO NOT TOUCH
public class HoProvider extends ContentProvider {

	private HoDatabaseHelper mDatabase;
	
	private static final String AUTHORITY = "sosis.AndroidApps.Provider";
	
	private static final String BASE_PATH =  "hoes";
	public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY
			+ "/" + BASE_PATH);

	@Override 
	public boolean onCreate(){
		mDatabase = new HoDatabaseHelper(getContext());
		return true;
	}
	
	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
		queryBuilder.setTables(HoTable.TABLE_OF_HOES);
		
		SQLiteDatabase db = mDatabase.getReadableDatabase();
		Cursor c = queryBuilder.query(db, projection, selection, selectionArgs, 
				null, null, sortOrder);
		c.setNotificationUri(getContext().getContentResolver(), uri);
		return c;		
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		SQLiteDatabase db = mDatabase.getWritableDatabase();
		
		int rowsDeleted = db.delete(HoTable.TABLE_OF_HOES, HoTable.COLUMN_ID + "=" 
	            + uri.getLastPathSegment(), null);
		getContext().getContentResolver().notifyChange(uri, null);
		return rowsDeleted;
	}

	@Override
	public String getType(Uri uri) {
		return null;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		SQLiteDatabase db = mDatabase.getWritableDatabase();
		long id = db.insert(HoTable.TABLE_OF_HOES, null, values);
		
		getContext().getContentResolver().notifyChange(uri, null);
		return Uri.withAppendedPath(CONTENT_URI,
				String.valueOf(id));
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		SQLiteDatabase db = mDatabase.getWritableDatabase();
		int rowsUpdated = 0;
		String id = uri.getLastPathSegment();
		if (TextUtils.isEmpty(selection)) {
			rowsUpdated = db.update(HoTable.TABLE_OF_HOES, values,
					HoTable.COLUMN_ID + "=" + uri.getLastPathSegment(),
					null);
		
		} else {
			rowsUpdated = db.update(HoTable.TABLE_OF_HOES, values
					, HoTable.TABLE_OF_HOES + "=" + id + " AND " + selection,
					selectionArgs);
		}
		getContext().getContentResolver().notifyChange(uri, null);
		return rowsUpdated;
	}
}