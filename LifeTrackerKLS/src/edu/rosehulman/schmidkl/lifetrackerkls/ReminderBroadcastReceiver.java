package edu.rosehulman.schmidkl.lifetrackerkls;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * 
 * @author schmidkl
 *  Broadcast Receiver which sends a notification to the device when
 *  the Alarmmanager goes off
 *
 */

public class ReminderBroadcastReceiver extends BroadcastReceiver {

	public ReminderBroadcastReceiver() {
		// Empty
	}

	@Override
	public void onReceive(Context context, Intent intent) {
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