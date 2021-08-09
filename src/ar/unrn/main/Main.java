package ar.unrn.main;

import ar.unrn.model.Tag;
import ar.unrn.service.TagService;
import ar.unrn.service.rethink.RethinkPersistanceService;
import ar.unrn.service.rethink.TagServiceRethink;

public class Main {

	public static void main(String[] args) {
		RethinkPersistanceService<Tag> tagPersistance = new RethinkPersistanceService<Tag>("test", 28015, "localhost");
		TagService tagService = new TagServiceRethink(tagPersistance, "tag");
		tagService.findOne("7ef2ca07-43e6-404e-96d2-64660df6dc13");
	}

}
