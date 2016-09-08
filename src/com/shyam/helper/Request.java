package com.shyam.helper;

import java.net.URL;
import java.util.concurrent.Callable;

public class Request implements Callable<Response> {

	private URL url;
	
	
	public Request(URL url) {
		super();
		this.url = url;
	}


	@Override
	public Response call() throws Exception {
		return new Response(url.openStream());
	}

}
