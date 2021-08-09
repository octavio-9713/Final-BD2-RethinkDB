package ar.unrn.model;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import org.json.simple.JSONObject;

public class Post {

	private String id;
	private String titulo;
	private String texto;
	private String resumen;
	private List<Tag> tags;
	private List<String> linksRelacionados;
	private String autor;
	private Long fecha;
	

	public Post() {
		this.fecha = new Date().getTime();
	}
	
	public Post(String id, String title, String body, String resume, String author, Date creationDate) {
		this.id = id;
		this.titulo = title;
		this.texto = body;
		this.resumen = resume;
		this.autor = author;
		this.fecha = creationDate.getTime();
	}
	
	public Post(String id, String title, String body, String resume, String author, Date creationDate, List<Tag> tags, List<String> links) {
		this.id = id;
		this.titulo = title;
		this.texto = body;
		this.resumen = resume;
		this.autor = author;
		this.fecha = creationDate.getTime();
		this.tags = tags;
		this.linksRelacionados = links;
	}

	public String getId() {
		return id;
	}

	public void setId(String _id) {
		this.id = _id;
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public String getTexto() {
		return texto;
	}

	public void setTexto(String texto) {
		this.texto = texto;
	}

	public String getResumen() {
		return resumen;
	}

	public void setResumen(String resumen) {
		this.resumen = resumen;
	}

	public List<Tag> getTags() {
		return tags;
	}

	public void setTags(List<String> tags) {
		if (tags != null && tags.size() > 0) {
			List<Tag> tagsObj = new ArrayList<>();
			tags.stream().forEach(tag -> tagsObj.add(new Tag(tag)));
			this.tags = tagsObj;
		}
	}
	
	public void changeTags(List<Tag> tags) {
		this.tags = tags;
	}

	public List<String> getLinksRelacionados() {
		return linksRelacionados != null ? linksRelacionados : new ArrayList<String>();
	}

	public void setLinksRelacionados(List<String> linksRelacionados) {
		this.linksRelacionados = linksRelacionados;
	}

	public String getAutor() {
		return autor;
	}

	public void setAutor(String autor) {
		this.autor = autor;
	}

	public Long getFecha() {
		return fecha;
	}

	public void setFecha(Long fecha) {
		this.fecha = fecha;
	}
	
	//TODO: Test with getters in protected/private
	@SuppressWarnings({ "unchecked"})
	public JSONObject toJson() {
		JSONObject id = new JSONObject();
		id.put("$oid", getId());
		
		JSONObject postJson = new JSONObject();
		postJson.put("_id", id);
		postJson.put("titulo", getTitulo());
		postJson.put("resumen", getResumen());
		postJson.put("texto", getTexto());

		postJson.put("links-relacionados", getLinksRelacionados());
		
		postJson.put("tags", createTagsJson());
		
		postJson.put("autor", getAutor());
		
		SimpleDateFormat formater = 
				new SimpleDateFormat("EEEE d 'de' MMMM 'de' yyyy - HH:mm:ss", new Locale("es", "ES"));
		JSONObject date = new JSONObject();
		date.put("$date", formater.format(new Date(this.fecha)));
		
		postJson.put("fecha", date);
		
		return postJson;
	}
	
	private List<String> createTagsJson() {
		if (getTags() != null && getTags().size() > 0)
			return tags.stream().map(Tag::getName).collect(Collectors.toList());
		
		else
			return new ArrayList<String>();
	}
}
