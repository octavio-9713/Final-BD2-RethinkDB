package ar.unrn.service;

import java.util.Date;
import java.util.List;

import ar.unrn.model.Page;

public interface PageService {

	void createPage(String title, String body, String authorName, Date creationDate);

	void updatePage(String pageId, String title, String body, String authorName, Date creationDate);
	
	Page findPage(String id);
	
	List<Page> findAll();
}
