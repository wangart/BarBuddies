package database;

import android.database.sqlite.SQLiteDatabase;

// The declaration for the table stored in the DB, that stores contact info
// DO NOT TOUCH
public class HoTable {

	public static final String TABLE_OF_HOES = "hoes";
	public static final String COLUMN_ID = "_id";
	public static final String COLUMN_NAME = "name";
	public static final String COLUMN_NUMBER = "number";
	public static final String COLUMN_ETHNICITY = "ethnicity";
	public static final String COLUMN_FUCKABLE = "fuckable";
	public static final String COLUMN_FRIENDABLE = "friendable";
	public static final String COLUMN_DATEABLE = "dateable";
	public static final String COLUMN_BODY_RATING = "body_rating";
	public static final String COLUMN_FACE_RATING = "face_rating";
	public static final String COLUMN_PERSONALITY_RATING = "personality_rating";
	public static final String COLUMN_HUMOR_RATING = "humor_rating";
	public static final String COLUMN_BITCHINESS_RATING = "bitchiness_rating";	
	public static final String COLUMN_CONTACTS_ID = "contacts_uri";
	public static final String COLUMN_CONTACTS_DISPLAY_ID = "contacts_display_name";
	public static final String COLUMN_CONTACTS_NUMBER_ID = "contacts_number";
	
	public static final String DATABASE_CREATE = "CREATE TABLE "
			+ TABLE_OF_HOES
			+"("
			+ COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
			+ COLUMN_NAME + " TEXT NOT NULL, "
			+ COLUMN_NUMBER + " TEXT NOT NULL, "
			+ COLUMN_ETHNICITY + " TEXT, "
			+ COLUMN_CONTACTS_ID + " TEXT, "
			+ COLUMN_CONTACTS_DISPLAY_ID + " TEXT, "
			+ COLUMN_CONTACTS_NUMBER_ID + " TEXT, "
			+ COLUMN_FUCKABLE + " INTEGER, "
			+ COLUMN_FRIENDABLE + " INTEGER, "
			+ COLUMN_DATEABLE + " INTEGER, "
			+ COLUMN_BODY_RATING + " REAL, "
			+ COLUMN_FACE_RATING + " REAL, "
			+ COLUMN_PERSONALITY_RATING + " REAL, "
			+ COLUMN_HUMOR_RATING + " REAL, "
			+ COLUMN_BITCHINESS_RATING + " REAL"
			+ ");";
	
	public static void onCreate(SQLiteDatabase database){
		database.execSQL(DATABASE_CREATE);
	}
	
	public static void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion){
		database.execSQL("DROP TABLE IF EXISTS " + TABLE_OF_HOES);
		onCreate(database);
	}
}
