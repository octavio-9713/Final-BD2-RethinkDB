package ar.unrn.model;

public class Counter {

	private String _id;
	
	private int count = 0;
	
	public Counter() {
	}
	
	public Counter(String id) {
		this._id = id;
		this.count = 0;
	}

	public Counter(String id, int quantity) {
		this._id = id != null ? id : "";
		this.count = quantity;
	}
	
	public String countReason() {
		return this._id;
	}
	
	public int quantity() {
		return count;
	}

	public String get_id() {
		return _id;
	}

	public void set_id(String id) {
		this._id = id;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int quantity) {
		this.count = quantity;
	}
	
	
}
