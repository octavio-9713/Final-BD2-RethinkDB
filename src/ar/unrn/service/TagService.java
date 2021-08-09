package ar.unrn.service;

import java.util.List;

import ar.unrn.model.Tag;

public interface TagService {
	
	void createTag(String name);

	void updateTag(String tagId, String name);
	
	List<Tag> findAll();

	List<Tag> findAllWithIds(List<String> ids);
	
	Tag findOne(String id);
}
