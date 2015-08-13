package edu.rosehulman.schmidkl.lifetrackerkls;

public class Item {
	
	private long mListID;
	private String mName;
	private long mID;
	private String mDescription = null;
	private String mPrice = null;
	private String mQuantity = null;
	private String mLocation = null;
	private String mWebLink = null;
	
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
}