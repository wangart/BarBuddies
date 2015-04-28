package database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

// DO NOT TOUCH
public class HoDatabaseHelper extends SQLiteOpenHelper {
	
	private static final String DATABASE_NAME = "hoes.db";
	private static final int DATABASE_VERSION = 1;
	
	public HoDatabaseHelper(Context context){
		super(context,DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		HoTable.onCreate(db);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		HoTable.onUpgrade(db, oldVersion, newVersion);
	}
}
