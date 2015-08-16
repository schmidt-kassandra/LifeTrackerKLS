package edu.rosehulman.schmidkl.lifetrackerkls;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ListActivity;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.TimePicker;

public class ItemActivity extends ListActivity {

	// TODO: maybe change item text color based on Priority

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

	private static final String mLowPriority = "Low";
	private static final String mMediumPriority = "Medium";
	private static final String mHighPriority = "High";
	public static final String KEY_NOTIFICATION = "KEY_NOTIFICATION";
	public static final String KEY_NOTIFICATION_ID = "KEY_NOTIFICATION_ID";
	public static final int KEY_PICK_FROM_GALLERY_REQUEST = 2;
	public static final String KEY_IMAGE_PATH = "KEY_IMAGE_PATH";
	public static final int KEY_VOICE_RECORDING = 7;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_item_list);

		Intent data = getIntent();

		mListID = data.getLongExtra(MainActivity.KEY_LIST_ID, 0);

		mItemDataAdapter = new ItemAdapter(this);
		mItemDataAdapter.open();

		Cursor cursor = mItemDataAdapter.getItemsCursor(mListID);
		String[] fromColumns = new String[] { ItemAdapter.KEY_ITEM };
		int[] toTextViews = new int[] { R.id.listTextView };
		mCursorAdapter = new SimpleCursorAdapter(this, R.layout.activity_items,
				cursor, fromColumns, toTextViews, 0);
		this.setListAdapter(mCursorAdapter);
		registerForContextMenu(getListView());
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		mSelectedID = id;
		itemAttributeSummaryDialog();
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
		menu.add(mGroupNumber, mEditItemID, 0, R.string.edit_item);
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

				TextView nameTextView = (TextView) view
						.findViewById(R.id.nameTextView);
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
								mSelectedID = item.getID();
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

				TextView nameTextView = (TextView) view
						.findViewById(R.id.nameTextView);
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
								String newName = nameEditText.getText()
										.toString();
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

	private void addItem(Item item) {
		mItemDataAdapter.addItem(item);
		mCursorAdapter.changeCursor(mItemDataAdapter.getItemsCursor(mListID));
	}

	private Item getItem(long ID) {
		return mItemDataAdapter.getItem(ID);
	}

	private void editItem(Item item) {
		if (mSelectedID == NO_ID_SELECTED) {
			Log.e(MainActivity.LT, "Attempt to update with no item selected.");
		}
		item.setID(mSelectedID);
		mItemDataAdapter.updateItem(item);
		mCursorAdapter.changeCursor(mItemDataAdapter.getItemsCursor(mListID));
	}

	private void removeItem(long ID) {
		mItemDataAdapter.removeItem(ID);
		mCursorAdapter.changeCursor(mItemDataAdapter.getItemsCursor(mListID));
	}

	private void attributesDialog() {			
		DialogFragment dialogFragment = new DialogFragment() {
			@Override
			public Dialog onCreateDialog(Bundle savedInstanceState) {
				AlertDialog.Builder builder = new AlertDialog.Builder(
						getActivity());
				// Inflate View
				LayoutInflater inflater = getActivity().getLayoutInflater();
				View view = inflater.inflate(R.layout.dialog_attributes, null);
				builder.setView(view);

				EditText descriptionEditText = (EditText) view
						.findViewById(R.id.descriptionEditText);
				EditText priceEditText = (EditText) view
						.findViewById(R.id.priceEditText);
				EditText quantityEditText = (EditText) view
						.findViewById(R.id.quantityEditText);
				EditText locationEditText = (EditText) view
						.findViewById(R.id.locationEditText);
				EditText weblinkEditText = (EditText) view
						.findViewById(R.id.weblinkEditText);

				final EditText[] editTextArray = new EditText[] {
						descriptionEditText, priceEditText, quantityEditText,
						locationEditText, weblinkEditText };
				
				final Item item = getItem(mSelectedID);


				String[] attributeArray = new String[] { item.getDescription(),
						item.getPrice(), item.getQuantity(),
						item.getLocation(), item.getWebLink() };

				for (int j = 0; j < editTextArray.length; j++) {
					if (attributeArray[j] != null) {
						editTextArray[j].setText(attributeArray[j]);
					}
				}

				Button priorityButton = (Button) view
						.findViewById(R.id.priorityButton);
				priorityButton.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						priorityDialog();
					}
				});

				Button reminderButton = (Button) view
						.findViewById(R.id.reminderButton);
				reminderButton.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						reminderDialog();
					}
				});

				Button imageButton = (Button) view
						.findViewById(R.id.imageButton);
				imageButton.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						imageSet();
					}
				});

				Button voiceButton = (Button) view
						.findViewById(R.id.voiceButton);
				voiceButton.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						voiceMessageDialog();
					}
				});

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
								Item item = getItem(mSelectedID);

								String attribute;
								String[] attributes = new String[5];

								for (int i = 0; i < editTextArray.length; i++) {
									if (editTextArray[i].getText().toString()
											.length() < 1) {
										attribute = null;
									} else {
										attribute = editTextArray[i].getText()
												.toString();
									}
									attributes[i] = attribute;
								}
								item.setDescription(attributes[0]);
								item.setPrice(attributes[1]);
								item.setQuantity(attributes[2]);
								item.setLocation(attributes[3]);
								item.setWebLink(attributes[4]);
								editItem(item);
								dismiss();
							}
						});
				return builder.create();
			}
		};
		dialogFragment.show(getFragmentManager(), null);
	}

	private void itemAttributeSummaryDialog() {
		// TODO: Voice

		DialogFragment dialogFragment = new DialogFragment() {
			@Override
			public Dialog onCreateDialog(Bundle savedInstanceState) {
				AlertDialog.Builder builder = new AlertDialog.Builder(
						getActivity());
				// Inflate View
				LayoutInflater inflater = getActivity().getLayoutInflater();
				View view = inflater.inflate(R.layout.dialog_attribute_summary,
						null);
				builder.setView(view);

				TextView descriptionTextView = (TextView) view
						.findViewById(R.id.summaryDescriptionTextView);
				TextView priceTextView = (TextView) view
						.findViewById(R.id.summaryPriceTextView);
				TextView quantityTextView = (TextView) view
						.findViewById(R.id.summaryQuantityTextView);
				TextView locationTextView = (TextView) view
						.findViewById(R.id.summaryLocationTextView);
				TextView weblinkTextView = (TextView) view
						.findViewById(R.id.summaryWebLinkTextView);
				TextView priorityTextView = (TextView) view
						.findViewById(R.id.summaryPriorityTextView);
				TextView reminderTimeTextView = (TextView) view
						.findViewById(R.id.summaryReminderTimeTextView);
				TextView reminderDateTextView = (TextView) view
						.findViewById(R.id.summaryReminderDateTextView);

				TextView[] textViewArray = new TextView[] {
						descriptionTextView, priceTextView, quantityTextView,
						locationTextView, weblinkTextView, priorityTextView };
				
				Item item = getItem(mSelectedID);

				String[] attributeArray = new String[] {
						item.getDescription(),
						item.getPrice(),
						item.getQuantity(),
						item.getLocation(),
						item.getWebLink(),
						item.getPriority() };

				for (int k = 0; k < textViewArray.length; k++) {
					if (attributeArray[k] != null) {
						textViewArray[k].setText(attributeArray[k]);
					}
				}

				if (item.getReminderBoolean()) {
					Date date = item.getReminder();

					SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm",
							Locale.getDefault());
					SimpleDateFormat dateFormat = new SimpleDateFormat(
							"MM dd, yyyy", Locale.getDefault());

					String timePart = timeFormat.format(date);
					String datePart = dateFormat.format(date);

					reminderTimeTextView.setText(timePart);
					reminderDateTextView.setText(datePart);
				}

				Button imageButton = (Button) view
						.findViewById(R.id.summaryImageButton);

				imageButton.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						Intent imageIntent = new Intent(getActivity(),
								ImageActivity.class);
						imageIntent.putExtra(KEY_IMAGE_PATH,
								getItem(mSelectedID).getImagePath());
						getActivity().startActivity(imageIntent);
					}
				});

				Button voiceButton = (Button) view
						.findViewById(R.id.summaryVoiceButton);

				voiceButton.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO
						// class PlayButton extends Button {
						// boolean mStartPlaying = true;
						//
						// OnClickListener clicker = new OnClickListener() {
						// public void onClick(View v) {
						// onPlay(mStartPlaying);
						// if (mStartPlaying) {
						// setText("Stop playing");
						// } else {
						// setText("Start playing");
						// }
						// mStartPlaying = !mStartPlaying;
						// }
						// };
						//
						// public PlayButton(Context ctx) {
						// super(ctx);
						// setText("Start playing");
						// setOnClickListener(clicker);
						// }
						// }
						//
						//
						//
						//
						//
						// private void onPlay(boolean start) {
						// if (start) {
						// startPlaying();
						// } else {
						// stopPlaying();
						// }
						// }
						//
						// private void startPlaying() {
						// mPlayer = new MediaPlayer();
						// try {
						// mPlayer.setDataSource(mFileName);
						// mPlayer.prepare();
						// mPlayer.start();
						// } catch (IOException e) {
						// Log.e(MainActivity.LT, "prepare() failed");
						// }
						// }
						//
						//
						// private void stopPlaying() {
						// mPlayer.release();
						// mPlayer = null;
						// }

					}
				});

				builder.setNeutralButton(R.string.edit,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								attributesDialog();
								dismiss();
							}
						});
				return builder.create();
			}
		};
		dialogFragment.show(getFragmentManager(), null);
	}

	private void priorityDialog() {
		DialogFragment dialogFragment = new DialogFragment() {
			@Override
			public Dialog onCreateDialog(Bundle savedInstanceState) {
				AlertDialog.Builder builder = new AlertDialog.Builder(
						getActivity());
				// Inflate View
				LayoutInflater inflater = getActivity().getLayoutInflater();
				View view = inflater.inflate(R.layout.dialog_priority, null);
				builder.setView(view);

				final RadioButton highPriorityButton = (RadioButton) view
						.findViewById(R.id.priorityHighRadioButton);
				final RadioButton mediumPriorityButton = (RadioButton) view
						.findViewById(R.id.priorityMediumRadioButton);
				final RadioButton lowPriorityButton = (RadioButton) view
						.findViewById(R.id.priorityLowRadioButton);
				
				

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
								Item item = getItem(mSelectedID);
								if (highPriorityButton.isChecked()) {
									item.setPriority(mHighPriority);
								} else if (mediumPriorityButton.isChecked()) {
									item.setPriority(mMediumPriority);
								} else if (lowPriorityButton.isChecked()) {
									item.setPriority(mLowPriority);
								}
								editItem(item);
								dismiss();
							}
						});
				return builder.create();
			}
		};
		dialogFragment.show(getFragmentManager(), null);
	}

	private void reminderDialog() {
		DialogFragment dialogFragment = new DialogFragment() {
			@Override
			public Dialog onCreateDialog(Bundle savedInstanceState) {
				AlertDialog.Builder builder = new AlertDialog.Builder(
						getActivity());
				// Inflate View
				LayoutInflater inflater = getActivity().getLayoutInflater();
				View view = inflater.inflate(R.layout.dialog_reminder, null);
				builder.setView(view);

				final TimePicker timePicker = (TimePicker) view
						.findViewById(R.id.timePicker);
				final DatePicker datePicker = (DatePicker) view
						.findViewById(R.id.datePicker);

				if (Build.VERSION.SDK_INT >= 11) {
					datePicker.setCalendarViewShown(false);
				}

				Calendar calendar = Calendar.getInstance();
				int minuteCalendar = calendar.get(Calendar.MINUTE);
				int hourCalendar = calendar.get(Calendar.HOUR_OF_DAY);

				timePicker.setCurrentMinute(minuteCalendar);
				timePicker.setCurrentHour(hourCalendar);


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
								Item item = getItem(mSelectedID);
								int minute = timePicker.getCurrentMinute();
								int hour = timePicker.getCurrentHour();
								int day = datePicker.getDayOfMonth();
								int month = datePicker.getMonth();
								int year = datePicker.getYear() - 1900;

								item.setReminder(minute, hour, day, month, year);
								item.setReminderBoolean(true);
								editItem(item);
								setReminder();
								dismiss();
							}
						});
				return builder.create();
			}
		};
		dialogFragment.show(getFragmentManager(), null);
	}

	private void setReminder() {
		Intent displayIntent = new Intent(this, MainActivity.class);

		Notification notification = getNotification(displayIntent);

		// Create an intent from this to send to the alarm manager. Add a
		// notification ID for the manager to use.
		Intent notificationIntent = new Intent(this,
				ReminderBroadcastReceiver.class);
		notificationIntent.putExtra(KEY_NOTIFICATION, notification);
		notificationIntent.putExtra(KEY_NOTIFICATION_ID, mSelectedID);
		int unusedRequestCode = 0;
		PendingIntent pendingIntent = PendingIntent.getBroadcast(this,
				unusedRequestCode, notificationIntent,
				PendingIntent.FLAG_UPDATE_CURRENT);

		Date date = getItem(mSelectedID).getReminder();
		// TODO: Does not go off when scheduled
		long time = SystemClock.elapsedRealtime() + date.getTime() + 15 * 1000;
		// long time = SystemClock.elapsedRealtime() + 15*1000;
		Log.d("LT", "Set Time " + time);
		// long time = date.getTime();
		// long time = SystemClock.
		AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
		alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, time,
				pendingIntent);
	}

	private Notification getNotification(Intent intent) {
		Notification.Builder builder = new Notification.Builder(this);
		builder.setContentTitle(getString(R.string.notification_title));
		builder.setContentText(getItem(mSelectedID).getName());
		builder.setSmallIcon(R.drawable.ic_launcher);

		int unusedRequestCode = 0; // arbitrary
		PendingIntent pendingIntent = PendingIntent.getActivity(this,
				unusedRequestCode, intent, PendingIntent.FLAG_UPDATE_CURRENT);
		builder.setContentIntent(pendingIntent);
		return builder.build();
	}

	private void imageSet() {
		Intent galleryIntent = new Intent(Intent.ACTION_PICK,
				MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

		this.startActivityForResult(galleryIntent,
				KEY_PICK_FROM_GALLERY_REQUEST);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode != RESULT_OK) {
			return;
		}
		if (requestCode == KEY_PICK_FROM_GALLERY_REQUEST) {
			Uri uri = data.getData();
			String realPath = getRealPathFromUri(uri);
			Log.d(MainActivity.LT, "Real URI on Device " + realPath);			
			Item item = getItem(mSelectedID);
			item.setImagePath(realPath);
			editItem(item);
		}
		
		if (requestCode == KEY_VOICE_RECORDING) {
			Uri uri = data.getData();
			String realPath = getRealPathFromUri(uri);
			Log.d(MainActivity.LT, "Real URI on Device" + realPath);
			// TODO: Won't save voicePath, maybe
			Item item = getItem(mSelectedID);
			item.setVoicePath(realPath);
			editItem(item);
		}
	}

	// From
	// http://android-er.blogspot.com/2013/08/convert-between-uri-and-file-path-and.html
	private String getRealPathFromUri(Uri contentUri) {
		String[] projection = { MediaStore.Images.Media.DATA };
		CursorLoader cursorLoader = new CursorLoader(this, contentUri,
				projection, null, null, null);
		Cursor cursor = cursorLoader.loadInBackground();
		cursor.moveToFirst();
		int columnIndex = cursor
				.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
		return cursor.getString(columnIndex);
	}

	private void voiceMessageDialog() {
		Intent recordIntent = new Intent(this, Recorder.class);
		startActivityForResult(recordIntent, KEY_VOICE_RECORDING );
	}

}
