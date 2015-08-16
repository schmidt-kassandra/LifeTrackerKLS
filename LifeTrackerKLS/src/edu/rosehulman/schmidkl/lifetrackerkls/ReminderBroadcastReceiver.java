package edu.rosehulman.schmidkl.lifetrackerkls;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class ReminderBroadcastReceiver extends BroadcastReceiver {

	public ReminderBroadcastReceiver() {
		// Empty
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		Log.d(MainActivity.LT, "In reciever");

		NotificationManager manager = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);
		Notification notification = intent
				.getParcelableExtra(ItemActivity.KEY_NOTIFICATION);
		int id = intent.getIntExtra(ItemActivity.KEY_NOTIFICATION_ID, 0);
		long listID = intent.getLongExtra(ItemActivity.KEY_LIST_ALARM_ID, 0);
		manager.notify(id, notification);
		Item item = ItemActivity.getItem(listID);	
		item.setReminderBoolean(false);
		ItemActivity.editItem(item);
	}
}