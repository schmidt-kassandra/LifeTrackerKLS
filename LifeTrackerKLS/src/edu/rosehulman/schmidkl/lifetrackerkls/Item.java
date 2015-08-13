package edu.rosehulman.schmidkl.lifetrackerkls;

public class Item {
	
	private long mListID;
	private String mName;
	private long mID;
	
	public Item() {
		//Empty
	}
	
	public Item(long listID) {
		mListID = listID;
	}
	
	public long getListID() {
		return mListID;
	}

	public long getID() {
		return mID;
	}

	public void setID(long ID) {
		this.mID = ID;
	}

	public String getName() {
		return mName;
	}

	public void setName(String name) {
		mName = name;
	}
}