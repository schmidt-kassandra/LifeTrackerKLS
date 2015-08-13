package edu.rosehulman.schmidkl.lifetrackerkls;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class ItemAdapter {
	//TODO: Make it so items are unique to their lists
	
	// Becomes the filename of the database
	private static final String DATABASE_NAME = "items.db";
	// Only one table in this database
	private static final String TABLE_NAME = "items";
	// We increment this every time we change the database schema which will
	// kick off an automatic upgrade
	private static final int DATABASE_VERSION = 1;

	public static final String KEY_ID = "_id"; // Android naming convention for
												// IDs
	public static final String KEY_ITEM = "item";
	private static final String DROP_STATEMENT = "DROP TABLE IF EXISTS "
			+ TABLE_NAME;
	private static final String CREATE_STATEMENT;
	static {
		StringBuilder sb = new StringBuilder();
		sb.append("CREATE TABLE " + TABLE_NAME + " (");
		sb.append(KEY_ID + " integer primary key autoincrement, ");
		sb.append(KEY_ITEM + " text");
		sb.append(")");
		CREATE_STATEMENT = sb.toString();
	}

	private SQLiteOpenHelper mOpenHelper;
	private SQLiteDatabase mDatabase;

	public ItemAdapter(Context context) {
		// Creates a SQliteHelper
		mOpenHelper = new ItemDBHelper(context);
	}

	public void open() {
		// Opens the database
		mDatabase = mOpenHelper.getWritableDatabase();
	}

	public void close() {
		// Closes the database
		mDatabase.close();
	}

	public Cursor getItemsCursor() {
		String[] projection = new String[] { KEY_ID, KEY_ITEM };
		return mDatabase.query(TABLE_NAME, projection, null, null, null, null,
				KEY_ITEM + " ASC");
	}

	public long addItem(Item item) {
		ContentValues row = getContentValuesFromItem(item);
		long newID = mDatabase.insert(TABLE_NAME, null, row);
		item.setID(newID);
		return newID;
	}

	private ContentValues getContentValuesFromItem(Item item) {
		ContentValues row = new ContentValues();
		row.put(KEY_ITEM, item.getName());
		return row;
	}

	public Item getItem(long ID) {
		String[] projection = new String[] { KEY_ID, KEY_ITEM };
		String selection = KEY_ID + "=" + ID;
		Cursor cursor = mDatabase.query(TABLE_NAME, projection, selection,
				null, null, null, null);
		if (cursor != null && cursor.moveToFirst()) {
			return getItemFromCursor(cursor);
		}
		return null;
	}

	private Item getItemFromCursor(Cursor cursor) {
		Item item = new Item();
		item.setID(cursor.getLong(cursor.getColumnIndexOrThrow(KEY_ID)));
		item.setName(cursor.getString(cursor
				.getColumnIndexOrThrow(KEY_ITEM)));
		return item;
	}

	public void updateItem(Item item) {
		ContentValues row = getContentValuesFromItem(item);
		String whereClause = KEY_ID + "=" + item.getID();
		mDatabase.update(TABLE_NAME, row, whereClause, null);
	}

	public boolean removeItem(long ID) {
		String whereClause = KEY_ID + "=" + ID;
		return mDatabase.delete(TABLE_NAME, whereClause, null) > 0;
	}

	private static class ItemDBHelper extends SQLiteOpenHelper {

		public ItemDBHelper(Context context) {
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
