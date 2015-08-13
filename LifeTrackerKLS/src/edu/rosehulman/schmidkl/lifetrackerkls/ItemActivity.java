package edu.rosehulman.schmidkl.lifetrackerkls;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

public class ItemActivity extends ListActivity{
	
	/**
	 * Constant to indicate that no row is selected for editing
	 */
	public static final long NO_ID_SELECTED = -1;

	/**
	 * Index of the row selected
	 */
	private long mSelectedID = NO_ID_SELECTED;
	
	public static final String KEY_LIST_ID = "KEY_LIST_ID";
	
	private static long mListID;

	private ItemAdapter mItemDataAdapter;
	private SimpleCursorAdapter mCursorAdapter;

	private static final int mGroupNumber = 0;
	
	private static final int mEditItemID = 1;
	private static final int mDeleteItemID = 2;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_item_list);
		
		Intent data = getIntent();
		
		mListID = data.getLongExtra(MainActivity.KEY_LIST_ID, 0);
		
		mItemDataAdapter = new ItemAdapter(this);
		mItemDataAdapter.open();

		Cursor cursor = mItemDataAdapter.getItemsCursor();
		String[] fromColumns = new String[] { ItemAdapter.KEY_ITEM };
		int[] toTextViews = new int[] { R.id.listTextView };
		mCursorAdapter = new SimpleCursorAdapter(this, R.layout.activity_items,
				cursor, fromColumns, toTextViews, 0);
		this.setListAdapter(mCursorAdapter);
		registerForContextMenu(getListView());
	}
	
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		// TODO: Dialog w/ data; 
		super.onListItemClick(l, v, position, id);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.item, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		} else if (id == R.id.add_item) {
			newItemDialog();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		menu.add(mGroupNumber , mEditItemID, 0, R.string.edit_item);
		menu.add(mGroupNumber, mDeleteItemID, 0, R.string.delete_item);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item
				.getMenuInfo();
		mSelectedID = info.id;

		int id = item.getItemId();
		if (id == mEditItemID) {
			editItemDialog();
			return true;
		} else if (id == mDeleteItemID) {
			deleteItemDialog();
			return true;
		}
		return super.onContextItemSelected(item);
	}
	
	private void newItemDialog() {
		DialogFragment dialogFragment = new DialogFragment() {
			@Override
			public Dialog onCreateDialog(Bundle savedInstanceState) {
				AlertDialog.Builder builder = new AlertDialog.Builder(
						getActivity());
				// Inflate View
				LayoutInflater inflater = getActivity().getLayoutInflater();
				View view = inflater.inflate(R.layout.dialog_name, null);
				builder.setView(view);
				
				TextView nameTextView = (TextView) view.findViewById(R.id.nameTextView);
				nameTextView.setText(R.string.new_item_message);

				final EditText nameEditText = (EditText) view
						.findViewById(R.id.nameEditText);

				builder.setNegativeButton(android.R.string.cancel,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								dismiss();
							}
						});
				builder.setPositiveButton(android.R.string.ok,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								String name = nameEditText.getText().toString();
								Item item = new Item(mListID);
								item.setName(name);
								addItem(item);
								
								//TODO: Properties Dialog
								attributesDialog();
								
								dismiss();
							}
						});
				return builder.create();
			}
		};
		dialogFragment.show(getFragmentManager(), null);
	}
	
	private void editItemDialog() {
		DialogFragment dialogFragment = new DialogFragment() {
			@Override
			public Dialog onCreateDialog(Bundle savedInstanceState) {
				AlertDialog.Builder builder = new AlertDialog.Builder(
						getActivity());
				// Inflate View
				LayoutInflater inflater = getActivity().getLayoutInflater();
				View view = inflater.inflate(R.layout.dialog_name, null);
				builder.setView(view);
				
				TextView nameTextView = (TextView) view.findViewById(R.id.nameTextView);
				nameTextView.setText(R.string.edit_item_message);
				
				final Item item = getItem(mSelectedID);

				final EditText nameEditText = (EditText) view
						.findViewById(R.id.nameEditText);
				
				nameEditText.setText(item.getName());

				builder.setNegativeButton(android.R.string.cancel,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								dismiss();
							}
						});
				builder.setPositiveButton(android.R.string.ok,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								String newName = nameEditText.getText().toString();
								item.setName(newName);
								editItem(item);
								dismiss();
							}
						});
				return builder.create();
			}
		};
		dialogFragment.show(getFragmentManager(), null);
	}
	
	private void deleteItemDialog() {
		DialogFragment dialogFragment = new DialogFragment() {
			@Override
			public Dialog onCreateDialog(Bundle savedInstanceState) {
				AlertDialog.Builder builder = new AlertDialog.Builder(
						getActivity());
				builder.setTitle(R.string.delete_item);
				builder.setMessage(R.string.delete_item_certainty);

				builder.setNegativeButton(android.R.string.cancel,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								dismiss();
							}
						});
				builder.setPositiveButton(android.R.string.ok,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								removeItem(mSelectedID);
								dismiss();
							}
						});
				return builder.create();
			}
		};
		dialogFragment.show(getFragmentManager(), null);
	}
	
	private void attributesDialog() {
		
	}

	private void addItem(Item item) {
		mItemDataAdapter.addItem(item);
		mCursorAdapter.changeCursor(mItemDataAdapter.getItemsCursor());
	}
	
	private Item getItem(long ID) {
		return mItemDataAdapter.getItem(ID);
	}
	
	private void editItem(Item item) {
		if (mSelectedID == NO_ID_SELECTED) {
			Log.e(MainActivity.LT, "Attempt to update with no list selected.");
		}
		item.setID(mSelectedID);
		mItemDataAdapter.updateItem(item);
		mCursorAdapter.changeCursor(mItemDataAdapter.getItemsCursor());
	}
	
	private void removeItem(long ID) {
		mItemDataAdapter.removeItem(ID);
		mCursorAdapter.changeCursor(mItemDataAdapter.getItemsCursor());
	}
}
