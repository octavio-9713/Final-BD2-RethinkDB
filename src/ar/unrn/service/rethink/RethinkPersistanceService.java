package ar.unrn.service.rethink;
import java.util.List;
import com.rethinkdb.RethinkDB;
import com.rethinkdb.gen.ast.Table;
import com.rethinkdb.model.MapObject;
import com.rethinkdb.net.Connection;
import com.rethinkdb.net.Connection.Builder;
import com.rethinkdb.net.Cursor;

import ar.unrn.model.Counter;

public class RethinkPersistanceService<T> {

	private final RethinkDB r = RethinkDB.r;
	private Connection baseConnection;
	private Builder baseBuilder;
	
	public RethinkPersistanceService(String dbName, int port, String host) {
		this.baseBuilder = r.connection().hostname(host).port(port).db(dbName);
	}
	
	public void openConnection() {
		this.closeConnection();
		this.baseConnection = this.baseBuilder.connect();
	}
	
	public String createUniqueId() {
		return r.uuid().run(baseConnection);
	}
	
	public void insert(MapObject object, String tableName) {
		Table table = r.table(tableName);		
		table.insert(object).run(baseConnection);
	}

	public void update(MapObject object, String objectId, String tableName) {
		Table table = r.table(tableName);	
		table.get(objectId).update(object).run(baseConnection);
	}
	
	public List<T> findByFilters(String tableName, Class<T> entityClass, int limit, MapObject filters, String orderIndex) {
		List<T> result;
		try {
			Table table = r.table(tableName);
			
			Cursor<T> cursor;
			if (limit > 0)
				cursor = table.orderBy().optArg("index", r.desc(orderIndex != null ? orderIndex : "id"))
					.filter(filters != null ? filters : "")	.limit(limit)
					.run(baseConnection, entityClass);
			
			else
				cursor = table.orderBy().optArg("index", r.desc(orderIndex != null ? orderIndex : "id"))
					.filter(filters).run(baseConnection, entityClass);
			
			result = cursor.toList();
		}
		catch (Exception e) {
			e.printStackTrace();
			result = null;
		}
		return result;
	}

	
	public List<T> findByText(String tableName, Class<T> entityClass, String search) {
		List<T> result;
		try {
			Table table = r.table(tableName);
			
			Cursor<T> cursor = table.filter(doc -> doc.coerceTo("string").match(search))
					.run(baseConnection, entityClass);
			
			result = cursor.toList();
		}
		catch (Exception e) {
			e.printStackTrace();
			result = null;
		}
		return result;
	}
	
	public List<Counter> countByElements(String tableName, String groupfield) {
		List<Counter> result;
		try {
			Table table = r.table(tableName);
			
			result = table.group(groupfield).count()
					.ungroup().map(doc -> 
						doc.merge(r.hashMap("_id", doc.g("group"))
								.with("count", doc.g("reduction")))
					).run(baseConnection, Counter.class);
			
		}
		catch (Exception e) {
			e.printStackTrace();
			result = null;
		}
		return result;
	}

	public List<T> findAll(String tableName, Class<T> entityClass) {
		List<T> result;
		try {
			Cursor<T> cursor = r.table(tableName).run(baseConnection, entityClass);
			result = cursor.toList();
		}
		catch (Exception e) {
			e.printStackTrace();
			result = null;
		}
		return result;
	}

	public T getOne(String tableName, String id, Class<T> entityClass) {
		T result;
		
		try {
			result = r.table(tableName).get(id).run(baseConnection, entityClass);
		}
		catch (Exception e) {
			e.printStackTrace();
			result = null;
		}
		return result;
	}
	
	public List<T> getByIds(String tableName, List<String> ids, Class<T> entityClass) {
		List<T> result = null;
		
		try {
			String idsSTtring = ids != null ? String.join(",", ids) : "";
			Cursor<T> cursor = r.table(tableName).getAll(idsSTtring).run(baseConnection, entityClass);
			result = cursor.toList();
		}
		catch (Exception e) {
			e.printStackTrace();
			result = null;
		}
		return result;
	}

	
	public void closeConnection() {
		if (this.baseConnection != null && this.baseConnection.isOpen())
			this.baseConnection.close();
	}
}
