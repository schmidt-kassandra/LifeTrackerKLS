package edu.rosehulman.schmidkl.lifetrackerkls;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class ListAdapter {
	// Becomes the filename of the database
	private static final String DATABASE_NAME = "lists.db";
	// Only one table in this database
	private static final String TABLE_NAME = "lists";
	// We increment this every time we change the database schema which will
	// kick off an automatic upgrade
	private static final int DATABASE_VERSION = 1;

	public static final String KEY_ID = "_id"; // Android naming convention for
												// IDs
	public static final String KEY_LIST_NAME = "name";
	private static final String DROP_STATEMENT = "DROP TABLE IF EXISTS "
			+ TABLE_NAME;
	private static final String CREATE_STATEMENT;
	static {
		StringBuilder sb = new StringBuilder();
		sb.append("CREATE TABLE " + TABLE_NAME + " (");
		sb.append(KEY_ID + " integer primary key autoincrement, ");
		sb.append(KEY_LIST_NAME + " text");
		sb.append(")");
		CREATE_STATEMENT = sb.toString();
	}

	private SQLiteOpenHelper mOpenHelper;
	private SQLiteDatabase mDatabase;

	public ListAdapter(Context context) {
		// Creates a SQliteHelper
		mOpenHelper = new ListDBHelper(context);
	}

	public void open() {
		// Opens the database
		mDatabase = mOpenHelper.getWritableDatabase();
	}

	public void close() {
		// Closes the database
		mDatabase.close();
	}

	public Cursor getListsCursor() {
		String[] projection = new String[] { KEY_ID, KEY_LIST_NAME };
		return mDatabase.query(TABLE_NAME, projection, null, null, null, null,
				KEY_LIST_NAME + " ASC");
	}

	public long addList(List list) {
		ContentValues row = getContentValuesFromList(list);
		long newID = mDatabase.insert(TABLE_NAME, null, row);
		list.setId(newID);
		return newID;
	}

	private ContentValues getContentValuesFromList(List list) {
		ContentValues row = new ContentValues();
		row.put(KEY_LIST_NAME, list.getName());
		return row;
	}

	public List getList(long id) {
		String[] projection = new String[] { KEY_ID, KEY_LIST_NAME };
		String selection = KEY_ID + "=" + id;
		Cursor cursor = mDatabase.query(TABLE_NAME, projection, selection,
				null, null, null, null);
		if (cursor != null && cursor.moveToFirst()) {
			return getListFromCursor(cursor);
		}
		return null;
	}

	private List getListFromCursor(Cursor cursor) {
		List list = new List();
		list.setId(cursor.getLong(cursor.getColumnIndexOrThrow(KEY_ID)));
		list.setName(cursor.getString(cursor
				.getColumnIndexOrThrow(KEY_LIST_NAME)));
		return list;
	}

	public void updateList(List list) {
		ContentValues row = getContentValuesFromList(list);
		String whereClause = KEY_ID + "=" + list.getId();
		mDatabase.update(TABLE_NAME, row, whereClause, null);
	}

	public boolean removeList(long id) {
		String whereClause = KEY_ID + "=" + id;
		return mDatabase.delete(TABLE_NAME, whereClause, null) > 0;
	}

	private static class ListDBHelper extends SQLiteOpenHelper {

		public ListDBHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL(CREATE_STATEMENT);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			Log.d(MainActivity.LT, "Upgrading the Database from Version"
					+ oldVersion + "to Version" + newVersion);
			db.execSQL(DROP_STATEMENT);
			onCreate(db);
		}
	}

}
