package ar.unrn.model;


public class Tag {

	private String _id;
	private String name;
	
	public Tag() {
		
	}
	
	public Tag(String id) {
		this._id = id;
	}
	
	public Tag(String id, String tag) {
		this._id = id;
		this.name = tag;
	}
	
	public String getId() {
		return _id;
	}

	public void setId(String id) {
		this._id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
