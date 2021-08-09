package ar.unrn.service.rethink;

import java.util.List;

import com.rethinkdb.RethinkDB;
import com.rethinkdb.model.MapObject;

import ar.unrn.model.Tag;
import ar.unrn.service.TagService;

public class TagServiceRethink implements TagService {
	private final RethinkDB r = RethinkDB.r;

	private RethinkPersistanceService<Tag> persistance;
	private String tableName;
	
	public TagServiceRethink(RethinkPersistanceService<Tag> persistance, String tableName) {
		this.persistance = persistance;
		this.tableName = tableName;
	}

	@Override
	public void createTag(String name) {
		this.persistance.openConnection();
		String uid = persistance.createUniqueId();
		MapObject object = r.hashMap("id", uid).with("name", name);
		
		this.persistance.insert(object, tableName);
		this.persistance.closeConnection();
	}

	@Override
	public void updateTag(String tagId, String name) {
		this.persistance.openConnection();
		MapObject object = r.hashMap("name", name);
		
		this.persistance.update(object, tagId, tableName);
		this.persistance.closeConnection();
	}

	@Override
	public List<Tag> findAll() {
		this.persistance.openConnection();
		
		List<Tag> tags = this.persistance.findAll(tableName, Tag.class);
		
		this.persistance.closeConnection();
		return tags;
	}

	public List<Tag> findAllWithIds(List<String> ids) {
		this.persistance.openConnection();
		
		List<Tag> tags = this.persistance.getByIds(tableName, ids, Tag.class);
		
		this.persistance.closeConnection();
		return tags;
	}
	
	@Override
	public Tag findOne(String id) {
		this.persistance.openConnection();
		
		Tag tag = this.persistance.getOne(tableName, id, Tag.class);
		
		this.persistance.closeConnection();
		return tag;
	}
}
