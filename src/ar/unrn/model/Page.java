package ar.unrn.model;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.json.simple.JSONObject;

public class Page {

	private String id;
	private String titulo;
	private String texto;
	private String autor;
	private Long fecha;
	
	public Page() {
		this.fecha = new Date().getTime();
	}
	
	public Page(String title, String body, String authorName, Date creationDate) {
		this.titulo = title;
		this.texto = body;
		this.autor = authorName;
		this.fecha = creationDate.getTime();
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
	
	
	@SuppressWarnings({ "unchecked"})
	public JSONObject toJson() {
		JSONObject id = new JSONObject();
		id.put("$oid", getId());
		
		JSONObject postJson = new JSONObject();
		postJson.put("_id", id);
		postJson.put("titulo", getTitulo());
		postJson.put("texto", getTexto());
		
		postJson.put("autor", getAutor());
		
		SimpleDateFormat formater = 
				new SimpleDateFormat("EEEE d 'de' MMMM 'de' yyyy - HH:mm:ss", new Locale("es", "ES"));
		JSONObject date = new JSONObject();
		date.put("$date", formater.format(new Date(this.fecha)));
		
		postJson.put("fecha", date);
		
		return postJson;
	}
}
