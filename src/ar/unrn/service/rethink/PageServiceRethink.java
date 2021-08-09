package ar.unrn.service.rethink;

import java.util.Date;
import java.util.List;

import com.rethinkdb.RethinkDB;
import com.rethinkdb.model.MapObject;

import ar.unrn.model.Page;
import ar.unrn.service.PageService;

public class PageServiceRethink implements PageService {
	private final RethinkDB r = RethinkDB.r;

	private RethinkPersistanceService<Page> persistance;
	private String tableName;
	
	public PageServiceRethink(RethinkPersistanceService<Page> rethinkPersistanceService, String tableName) {
		this.persistance = rethinkPersistanceService;
		this.tableName = tableName;
	}

	@Override
	public void createPage(String title, String body, String authorName, Date creationDate) {
		this.persistance.openConnection();
		String uid = persistance.createUniqueId();
		MapObject object = r.hashMap("id", uid)
				.with("titulo", title)
				.with("texto", body)
				.with("autor", authorName)
				.with("fecha", creationDate.getTime());
		
		this.persistance.insert(object, tableName);
		this.persistance.closeConnection();
	}

	@Override
	public void updatePage(String pageId, String title, String body, String authorName, Date creationDate) {
		this.persistance.openConnection();
		MapObject object = r.hashMap("id", pageId)
				.with("titulo", title)
				.with("texto", body)
				.with("autor", authorName)
				.with("fecha", creationDate.getTime());
		
		this.persistance.update(object, pageId, tableName);
		this.persistance.closeConnection();
	}

	@Override
	public Page findPage(String id) {
		this.persistance.openConnection();
	
		Page page = this.persistance.getOne(tableName, id, Page.class);
		
		this.persistance.closeConnection();
		return page;
	}

	@Override
	public List<Page> findAll() {
		this.persistance.openConnection();
	
		List<Page> pages = this.persistance.findAll(tableName, Page.class);
		
		this.persistance.closeConnection();
		return pages;
	}	
}
