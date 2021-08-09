package ar.unrn.service;

import java.util.Date;
import java.util.List;

import ar.unrn.model.Counter;
import ar.unrn.model.Post;
import ar.unrn.model.Tag;

public interface PostService {

	void createPost(String title, String body, String resume, String author, Date creationDate, List<Tag> tags, List<String> links);
	
	void updatePost(String postId, String title, String body, String resume, String author, Date creationDate, List<Tag> tags, List<String> links);
	
	Post findPost(String postId);
	
	List<Counter> findAuthorsWithPosts();
	
	List<Post> findPostsByText(String text);

	List<Post> findPostsByAuthor(String authorName);
	
	List<Post> findAll();
	
	List<Post> findAllLimitedAndOrdered(int limit);
	
}
