package edu.rosehulman.schmidkl.lifetrackerkls;

/**
 * 
 * @author schmidkl
 * List Model Class
 *
 */

public class List {
	
	private String mName;
	private long mID;
	
	public List() {
		//Empty
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