package edu.rosehulman.schmidkl.lifetrackerkls;

public class List {
	
	private String mName;
	private long mId;
	
	public List() {
		//Empty
	}

	public long getId() {
		return mId;
	}

	public void setId(long id) {
		this.mId = id;
	}

	public String getName() {
		return mName;
	}

	public void setName(String name) {
		mName = name;
	}
}