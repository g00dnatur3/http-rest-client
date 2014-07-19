package http.rest;

import org.apache.http.HttpResponse;

public class RestClientException extends Exception {
	private static final long serialVersionUID = -785745983831601722L;
	
	private final HttpResponse response;

	public RestClientException(String message, HttpResponse response) {
		super(message);
		this.response = response;
	}
	
	public RestClientException(Exception e, HttpResponse response) {
		super(e);
		this.response = response;
	}

	public HttpResponse response() {
		return response;
	}
}