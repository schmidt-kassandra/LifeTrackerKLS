package edu.rosehulman.schmidkl.lifetrackerkls;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * 
 * @author schmidkl
 * Item Model Class with getters and setters for the item Attributes
 *
 */

public class Item {
	
	private long mListID;
	private String mName;
	private long mID;
	private String mDescription = null;
	private String mPrice = null;
	private String mQuantity = null;
	private String mLocation = null;
	private String mWebLink = null;
	private String mPriority = "Low";
	private boolean mIsReminderSet = false;
	private Date mReminder = new Date();
	SimpleDateFormat mDateFormat = new SimpleDateFormat("HH:mm MM dd, yyyy", Locale.getDefault());
	private String mImagePath = null;
	private boolean mIsImageSet = false;
	private String mRecordingPath = null;
	private boolean mIsVoiceSet = false;
	
	public Item() {
		//Empty
	}
	
	public Item(long listID) {
		mListID = listID;
	}
	
	public long getListID() {
		return mListID;
	}
	
	public void setID(long ID) {
		this.mID = ID;
	}

	public long getID() {
		return mID;
	}
	
	public void setName(String name) {
		mName = name;
	}

	public String getName() {
		return mName;
	}
	
	public void setDescription(String description) {
		mDescription = description;
	}

	public String getDescription() {
		return mDescription;
	}
	
	public void setPrice(String price) {
		mPrice = price;
	}
	
	public String getPrice() {
		return mPrice;
	}
	
	public void setQuantity(String quantity) {
		mQuantity = quantity;
	}
	
	public String getQuantity() {
		return mQuantity;
	}
	
	public void setLocation(String location) {
		mLocation = location;
	}
	
	public String getLocation() {
		return mLocation;
	}
	
	public void setWebLink(String weblink) {
		mWebLink = weblink;
	}
	
	public String getWebLink() {
		return mWebLink;
	}
	
	public void setPriority(String priority) {
		mPriority = priority;
	}
	
	public String getPriority() {
		return mPriority;
	}
	
	public void setReminder(int minute, int hour, int day, int month, int year) {
		Date date = new Date(year, month, day, hour, minute);
		mReminder = date;
	}
	
	public void setReminder(String date) {
		try {
			mReminder = mDateFormat.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
	
	public String getReminderString() {
		return mDateFormat.format(mReminder);
	}
	
	public Date getReminder() {
		return mReminder;
	}
	
	public void setReminderBoolean(boolean reminder) {
		mIsReminderSet = reminder;
	}
	
	public boolean getReminderBoolean() {
		return mIsReminderSet;
	}
	
	public void setImagePath(String image) {
		mImagePath = image;
	}
	
	public String getImagePath() {
		return mImagePath;
	}
	
	public void setImageBoolean(Boolean image) {
		mIsImageSet = image;
	}
	
	public boolean getImageBoolean() {
		return mIsImageSet;
	}
	
	public void setVoicePath(String voice) {
		mRecordingPath = voice;
	}
	
	public String getVoicePath() {
		return mRecordingPath;
	}
	
	public void setVoiceBoolean(Boolean voice) {
		mIsVoiceSet = voice;
	}
	
	public boolean getVoiceBoolean() {
		return mIsVoiceSet;
	}
}