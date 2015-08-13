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

public class MainActivity extends ListActivity {

	public static final String LT = "LT";
	/**
	 * Constant to indicate that no row is selected for editing
	 */
	public static final long NO_ID_SELECTED = -1;

	/**
	 * Index of the row selected
	 */
	private long mSelectedID = NO_ID_SELECTED;
	
	public static final String KEY_LIST_ID = "KEY_LIST_ID";

	private ListAdapter mListDataAdapter;
	private SimpleCursorAdapter mCursorAdapter;

	private static final int mGroupNumber = 0;
	private static final int mEditListID = 1;
	private static final int mDeleteListID = 2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_list);

		mListDataAdapter = new ListAdapter(this);
		mListDataAdapter.open();

		Cursor cursor = mListDataAdapter.getListsCursor();
		String[] fromColumns = new String[] { ListAdapter.KEY_LIST_NAME };
		int[] toTextViews = new int[] { R.id.listTextView };
		mCursorAdapter = new SimpleCursorAdapter(this, R.layout.activity_lists,
				cursor, fromColumns, toTextViews, 0);
		this.setListAdapter(mCursorAdapter);
		registerForContextMenu(getListView());
	}
	
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		mSelectedID = id;		
		Intent itemIntent = new Intent(this, ItemActivity.class);
		itemIntent.putExtra(KEY_LIST_ID, mSelectedID);
		this.startActivity(itemIntent);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
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
		} else if (id == R.id.add_list) {
			newListDialog();
			return true;
		} else if (id == R.id.priceTextView) {
			Intent aboutIntent = new Intent(this, AboutActivity.class);
			this.startActivity(aboutIntent);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		menu.add(mGroupNumber, mEditListID, 0, R.string.edit_list);
		menu.add(mGroupNumber, mDeleteListID, 0, R.string.delete_list);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item
				.getMenuInfo();
		mSelectedID = info.id;

		int id = item.getItemId();
		if (id == mEditListID) {
			editListDialog();
			return true;
		} else if (id == mDeleteListID) {
			deleteListDialog();
			return true;
		}
		return super.onContextItemSelected(item);
	}

	private void newListDialog() {
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
				nameTextView.setText(R.string.new_list_message);

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
								String listName = nameEditText.getText()
										.toString();
								List list = new List();
								list.setName(listName);
								addList(list);
								dismiss();
							}
						});
				return builder.create();
			}
		};
		dialogFragment.show(getFragmentManager(), null);
	}

	private void editListDialog() {
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
				nameTextView.setText(R.string.edit_list_message);
				
				final List list = getList(mSelectedID);

				final EditText nameEditText = (EditText) view
						.findViewById(R.id.nameEditText);
				
				nameEditText.setText(list.getName());

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
								list.setName(newName);
								editList(list);
								dismiss();
							}
						});
				return builder.create();
			}
		};
		dialogFragment.show(getFragmentManager(), null);
	}

	private void deleteListDialog() {
		DialogFragment dialogFragment = new DialogFragment() {
			@Override
			public Dialog onCreateDialog(Bundle savedInstanceState) {
				AlertDialog.Builder builder = new AlertDialog.Builder(
						getActivity());
				builder.setTitle(R.string.delete_list);
				builder.setMessage(R.string.delete_list_certainty);

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
								removeList(mSelectedID);
								dismiss();
							}
						});
				return builder.create();
			}
		};
		dialogFragment.show(getFragmentManager(), null);
	}

	private void addList(List list) {
		mListDataAdapter.addList(list);
		mCursorAdapter.changeCursor(mListDataAdapter.getListsCursor());
	}

	private List getList(long ID) {
		return mListDataAdapter.getList(ID);
	}

	private void editList(List list) {
		if (mSelectedID == NO_ID_SELECTED) {
			Log.e(LT, "Attempt to update with no list selected.");
		}
		list.setID(mSelectedID);
		mListDataAdapter.updateList(list);
		mCursorAdapter.changeCursor(mListDataAdapter.getListsCursor());
	}

	private void removeList(long ID) {
		mListDataAdapter.removeList(ID);
		mCursorAdapter.changeCursor(mListDataAdapter.getListsCursor());
	}
}