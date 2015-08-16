package edu.rosehulman.schmidkl.lifetrackerkls;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class ItemAdapter {
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
	public static final String KEY_LIST_ID = "listID";
	public static final String KEY_DESCRIPTION = "description";
	public static final String KEY_PRICE = "price";
	public static final String KEY_QUANTITY = "quantity";
	public static final String KEY_LOCATION = "location";
	public static final String KEY_WEBLINK = "weblink";
	public static final String KEY_PRIORITY = "priority";
	public static final String KEY_REMINDER_BOOLEAN = "reminderboolean";
	public static final String KEY_REMINDER = "reminder";
	private static final int yesReminder = 1;
	private static final int noReminder = 0;
	public static final String KEY_IMAGE_BOOLEAN = "imageboolean";
	public static final String KEY_IMAGE_PATH = "image";
	public static final String KEY_VOICE_BOOLEAN = "voiceboolean";
	public static final String KEY_VOICE_PATH = "voice";
	private static final String DROP_STATEMENT = "DROP TABLE IF EXISTS "
			+ TABLE_NAME;
	private static final String CREATE_STATEMENT;
	static {
		StringBuilder sb = new StringBuilder();
		sb.append("CREATE TABLE " + TABLE_NAME + " (");
		sb.append(KEY_ID + " integer primary key autoincrement, ");
		sb.append(KEY_ITEM + " text, ");
		sb.append(KEY_LIST_ID + " integer, ");
		sb.append(KEY_DESCRIPTION + " text, ");
		sb.append(KEY_PRICE + " text, ");
		sb.append(KEY_QUANTITY + " text, ");
		sb.append(KEY_LOCATION + " text, ");
		sb.append(KEY_WEBLINK + " text, ");
		sb.append(KEY_PRIORITY + " text, ");
		sb.append(KEY_REMINDER_BOOLEAN + " integer, ");
		sb.append(KEY_REMINDER + " datetime, ");
		sb.append(KEY_IMAGE_BOOLEAN + " integer, ");
		sb.append(KEY_IMAGE_PATH + " text, ");
		sb.append(KEY_VOICE_BOOLEAN + " integer, ");
		sb.append(KEY_VOICE_PATH + " text");
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

	public Cursor getItemsCursor(long listID) {
		String[] projection = new String[] { KEY_ID, KEY_ITEM, KEY_LIST_ID,
				KEY_DESCRIPTION, KEY_PRICE, KEY_QUANTITY, KEY_LOCATION,
				KEY_WEBLINK, KEY_PRIORITY, KEY_REMINDER_BOOLEAN, KEY_REMINDER, KEY_IMAGE_BOOLEAN, KEY_IMAGE_PATH, KEY_VOICE_BOOLEAN, KEY_VOICE_PATH };
		String selection = KEY_LIST_ID + "=" + listID;
		return mDatabase.query(TABLE_NAME, projection, selection, null, null, null,
				KEY_PRIORITY + " DESC");
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
		row.put(KEY_LIST_ID, item.getListID());
		row.put(KEY_DESCRIPTION, item.getDescription());
		row.put(KEY_PRICE, item.getPrice());
		row.put(KEY_QUANTITY, item.getQuantity());
		row.put(KEY_LOCATION, item.getLocation());
		row.put(KEY_WEBLINK, item.getWebLink());
		row.put(KEY_PRIORITY, item.getPriority());	
		if(item.getReminderBoolean()) {
			row.put(KEY_REMINDER_BOOLEAN, yesReminder);
		} else {
			row.put(KEY_REMINDER_BOOLEAN, noReminder);
		}	
		row.put(KEY_REMINDER, item.getReminderString());
		if(item.getImageBoolean()) {
			row.put(KEY_IMAGE_BOOLEAN, yesReminder);
		} else {
			row.put(KEY_IMAGE_BOOLEAN, noReminder);
		}
		row.put(KEY_IMAGE_PATH, item.getImagePath());
		if(item.getVoiceBoolean()) {
			row.put(KEY_VOICE_BOOLEAN, yesReminder);
		} else {
			row.put(KEY_VOICE_BOOLEAN, noReminder);
		}
		row.put(KEY_VOICE_PATH, item.getVoicePath());
		Log.d(MainActivity.LT, "IA 108" + item.getVoicePath());
		return row;
	}

	public Item getItem(long ID) {
		String[] projection = new String[] { KEY_ID, KEY_ITEM, KEY_LIST_ID,
				KEY_DESCRIPTION, KEY_PRICE, KEY_QUANTITY, KEY_LOCATION,
				KEY_WEBLINK, KEY_PRIORITY, KEY_REMINDER_BOOLEAN, KEY_REMINDER, KEY_IMAGE_BOOLEAN, KEY_IMAGE_PATH, KEY_VOICE_BOOLEAN, KEY_VOICE_PATH };
		String selection = KEY_ID + "=" + ID;
		Cursor cursor = mDatabase.query(TABLE_NAME, projection, selection,
				null, null, null, null);
		if (cursor != null && cursor.moveToFirst()) {
			return getItemFromCursor(cursor);
		}
		return null;
	}

	private Item getItemFromCursor(Cursor cursor) {	
		Item item = new Item(cursor.getLong(cursor.getColumnIndexOrThrow(KEY_LIST_ID)));
		item.setID(cursor.getLong(cursor.getColumnIndexOrThrow(KEY_ID)));
		item.setName(cursor.getString(cursor.getColumnIndexOrThrow(KEY_ITEM)));
		item.setDescription(cursor.getString(cursor.getColumnIndexOrThrow(KEY_DESCRIPTION)));
		item.setPrice(cursor.getString(cursor.getColumnIndexOrThrow(KEY_PRICE)));
		item.setQuantity(cursor.getString(cursor.getColumnIndexOrThrow(KEY_QUANTITY)));
		item.setLocation(cursor.getString(cursor.getColumnIndexOrThrow(KEY_LOCATION)));
		item.setWebLink(cursor.getString(cursor.getColumnIndexOrThrow(KEY_WEBLINK)));
		item.setPriority(cursor.getString(cursor.getColumnIndexOrThrow(KEY_PRIORITY)));		
		if(cursor.getInt(cursor.getColumnIndexOrThrow(KEY_REMINDER_BOOLEAN)) == yesReminder) {
			item.setReminderBoolean(true);
		} else {
			item.setReminderBoolean(false);
		}
		item.setReminder(cursor.getString(cursor.getColumnIndexOrThrow(KEY_REMINDER)));
		if(cursor.getInt(cursor.getColumnIndexOrThrow(KEY_IMAGE_BOOLEAN)) == yesReminder) {
			item.setImageBoolean(true);
		} else {
			item.setImageBoolean(false);
		}
		item.setImagePath(cursor.getString(cursor.getColumnIndexOrThrow(KEY_IMAGE_PATH)));
		if(cursor.getInt(cursor.getColumnIndexOrThrow(KEY_VOICE_BOOLEAN)) == yesReminder) {
			item.setVoiceBoolean(true);
		} else {
			item.setVoiceBoolean(false);
		}
		item.setVoicePath(cursor.getString(cursor.getColumnIndexOrThrow(KEY_VOICE_PATH)));
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