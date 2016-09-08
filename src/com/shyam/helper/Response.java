package com.shyam.helper;

import java.io.InputStream;

public class Response {
	private InputStream body;

	public Response(InputStream body) {
		super();
		this.body = body;
	}

	public InputStream getBody() {
		return body;
	}
	
	
}
