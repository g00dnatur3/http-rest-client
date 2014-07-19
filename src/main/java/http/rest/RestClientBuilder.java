package http.rest;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;

public class RestClientBuilder {

	protected HttpClient client;
	
	protected ObjectMapper mapper;
	
	protected RestClientBuilder() {
		// defaults
		client = HttpClientBuilder.create().useSystemProperties().build();
		mapper = new ObjectMapper();
		mapper.setSerializationInclusion(Include.NON_NULL);
	}
	
	public RestClientBuilder httpClient(HttpClient client) {
		this.client = client;
		return this;
	}

	public RestClientBuilder objectMapper(ObjectMapper mapper) {
		this.mapper = mapper;
		return this;
	}
	
	public RestClient build() {
		return new RestClient(this);
	}
	
}
