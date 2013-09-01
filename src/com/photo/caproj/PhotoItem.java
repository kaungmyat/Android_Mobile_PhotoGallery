package com.photo.caproj;

import java.util.HashMap;

public class PhotoItem extends HashMap<String, String> {

	private static final long serialVersionUID = 5976828556440440492L;
	public PhotoItem(String caption, String location, String file, String id) {
		put("caption", caption);
		put("location", location);
		put("file", file);
		put("id", id);
	}

}
