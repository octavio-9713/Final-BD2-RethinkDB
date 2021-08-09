package ar.unrn.api;

import static spark.Spark.get;

import java.util.List;
import java.util.stream.Collectors;

import org.json.simple.JSONArray;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import ar.unrn.model.Counter;
import ar.unrn.model.Page;
import ar.unrn.model.Post;
import ar.unrn.model.Tag;
import ar.unrn.service.PageService;
import ar.unrn.service.PostService;
import ar.unrn.service.TagService;
import ar.unrn.service.rethink.PageServiceRethink;
import ar.unrn.service.rethink.PostServiceRethink;
import ar.unrn.service.rethink.RethinkPersistanceService;
import ar.unrn.service.rethink.TagServiceRethink;
import spark.Spark;

public class BlogAPI {

	@SuppressWarnings("unchecked")
	public static void main(String[] args) {

		TagService tagService = new TagServiceRethink(
				new RethinkPersistanceService<Tag>("test", 28015, "localhost"), "tag");
		PageService pageService = new PageServiceRethink(
				new RethinkPersistanceService<Page>("test", 28015, "localhost"), "page");
		PostService postService = new PostServiceRethink(
				new RethinkPersistanceService<Post>("test", 28015, "localhost"), tagService, "post");
		
		ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();

		get("/byautor", (req, res) -> {
			res.header("Access-Control-Allow-Origin", "*");

			List<Counter> count = postService.findAuthorsWithPosts();
			return ow.writeValueAsString(count);
		});
		
		get("/ultimos4posts", (req, res) -> {
			res.header("Access-Control-Allow-Origin", "*");

			List<Post> posts = postService.findAllLimitedAndOrdered(4);			
			if (posts == null || posts.size() == 0)
				return "";
			
			JSONArray jsonPosts = new JSONArray();
			jsonPosts.addAll(posts.stream().map(Post::toJson).collect(Collectors.toList()));
			return ow.writeValueAsString(jsonPosts);
		});

		get("/posts-autor/:nombreautor", (req, res) -> {
			res.header("Access-Control-Allow-Origin", "*");

			// Recupero el nombre del autor que viene como parámetro
			String nombreAutor = req.params("nombreautor");
			
			List<Post> posts = postService.findPostsByAuthor(nombreAutor);
			if (posts == null || posts.size() == 0)
				return "";
			
			JSONArray jsonPosts = new JSONArray();
			jsonPosts.addAll(posts.stream().map(Post::toJson).collect(Collectors.toList()));
			return ow.writeValueAsString(jsonPosts);
		});

		get("/search/:text", (req, res) -> {
			res.header("Access-Control-Allow-Origin", "*");

			// Recupero la palabra/frase ingresada por el usuario
			String text = req.params("text");
			
			List<Post> posts = postService.findPostsByText(text);
			if (posts == null || posts.size() == 0)
				return "";

			JSONArray jsonPosts = new JSONArray();
			jsonPosts.addAll(posts.stream().map(Post::toJson).collect(Collectors.toList()));
			return ow.writeValueAsString(jsonPosts);
		});
		
		get("/post-id/:id", (req, res) ->
		{	
			res.header("Access-Control-Allow-Origin", "*");

			// Recupero el id del post que viene por parámetro
			String postId = req.params("id");
			if (postId == null)
				return "";
			
			Post post = postService.findPost(postId);
			
			if (post == null)
				return "";
			
			JSONArray jsonPosts = new JSONArray();
			jsonPosts.add(post.toJson());
			return ow.writeValueAsString(jsonPosts);
		});

		get("/pagina-id/:id", (req, res) ->
		{
			res.header("Access-Control-Allow-Origin", "*");
			// Recupero el id que viene por par�metro
			String paginaId = req.params("id");
			if (paginaId == null)
				return "";
			
			Page page = pageService.findPage(paginaId);
			if (page == null)
				return "";
			
			JSONArray jsonPages = new JSONArray();
			jsonPages.add(page.toJson());
			return ow.writeValueAsString(jsonPages);
		});

		Spark.exception(Exception.class, (exception, request, response) -> {
			exception.printStackTrace();
		});
	}
}