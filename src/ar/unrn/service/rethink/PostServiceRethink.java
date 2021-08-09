package ar.unrn.service.rethink;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.rethinkdb.RethinkDB;
import com.rethinkdb.model.MapObject;

import ar.unrn.model.Counter;
import ar.unrn.model.Post;
import ar.unrn.model.Tag;
import ar.unrn.service.PostService;
import ar.unrn.service.TagService;

public class PostServiceRethink implements PostService{

	private final RethinkDB r = RethinkDB.r;

	private RethinkPersistanceService<Post> persistance;
	private String tableName;
	private TagService tagService;
	
	public PostServiceRethink(RethinkPersistanceService<Post> rethinkPersistanceService, TagService tagService, String tableName) {
		this.persistance = rethinkPersistanceService;
		this.tableName = tableName;
		this.tagService = tagService;
	}
	
	@Override
	public void createPost(String title, String body, String resume, String author, Date creationDate, List<Tag> tags, List<String> links) {

		this.persistance.openConnection();
		
		List<String> tagsIds = tags.stream().map(tag -> tag.getId()).collect(Collectors.toList());
		
		String uid = persistance.createUniqueId();
		MapObject object = r.hashMap("id", uid)
				.with("titulo", title)
				.with("texto", body)
				.with("resumen", resume)
				.with("autor", author)
				.with("fecha", creationDate.getTime())
				.with("tags", tagsIds)
				.with("linksRelacionados", links);
		
		this.persistance.insert(object, tableName);
		this.persistance.closeConnection();
	}

	@Override
	public void updatePost(String postId, String title, String body, String resume, String author, Date creationDate, List<Tag> tags, List<String> links) {
		
		this.persistance.openConnection();	

		List<String> tagsIds = tags.stream().map(tag -> tag.getId()).collect(Collectors.toList());
		
		MapObject object = r.hashMap("id", postId)
				.with("titulo", title)
				.with("texto", body)
				.with("resumen", resume)
				.with("autor", author)
				.with("fecha", creationDate.getTime())
				.with("tags", tagsIds)
				.with("linksRelacionados", links);
		
		this.persistance.update(object, postId, tableName);
		this.persistance.closeConnection();
	}
	
	@Override
	public List<Post> findAll() {
		this.persistance.openConnection();
		
		List<Post> posts = new ArrayList<>();
		posts = this.persistance.findAll(tableName, Post.class);
		//posts.forEach(post -> setUpTags(post));
		
		this.persistance.closeConnection();
		return posts;
	}

	@Override
	public List<Post> findAllLimitedAndOrdered(int limit) {
		this.persistance.openConnection();
		
		List<Post> posts = new ArrayList<>();
		
		posts = this.persistance.findByFilters(tableName, Post.class, limit, null, "fecha");
		//posts.forEach(post -> setUpTags(post));
		
		this.persistance.closeConnection();
		return posts;
	}
	
	@Override
	public Post findPost(String postId) {
		this.persistance.openConnection();
		
		Post post = this.persistance.getOne(tableName, postId, Post.class);
		setUpTags(post);
		
		this.persistance.closeConnection();
		return post;
	}

	
	public List<Post> findPostsByText(String text) {
		this.persistance.openConnection();
		
		List<Post> posts = new ArrayList<>();

		posts = this.persistance.findByText(tableName, Post.class, text);
		//posts.forEach(post -> setUpTags(post));
		
		this.persistance.closeConnection();
		return posts;
	}
	
	public List<Post> findPostsByAuthor(String authorName) {
		this.persistance.openConnection();
		
		List<Post> posts = new ArrayList<>();
		MapObject filter = r.hashMap("autor", authorName);
		
		posts = this.persistance.findByFilters(tableName, Post.class, 0, filter, "fecha");
		posts.forEach(post -> setUpTags(post));
		
		this.persistance.closeConnection();
		return posts;
	}
	
	public List<Counter> findAuthorsWithPosts() {
		List<Counter> authorsWithPosts = new ArrayList<Counter>();
		List<Post> posts = findAll();
		
		if (posts == null)
			return null;
		
		Map<String, List<Post>> res = posts.stream()
				.collect(Collectors.groupingBy(Post::getAutor));
		
		res.forEach((author, list) -> authorsWithPosts.add(new Counter(author, list.size())));
		return authorsWithPosts;
	}
	
	private void setUpTags(Post post) {
		if (post != null && post.getTags() != null && post.getTags().size() > 0) {
			List<String> ids = post.getTags().stream()
					.map(tag -> tag.getId()).collect(Collectors.toList());
			
			post.changeTags(tagService.findAllWithIds(ids));
		}
	}
	
}
