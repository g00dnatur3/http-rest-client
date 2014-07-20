package http.rest;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;

public class RestClientBuilder {

	protected HttpClient client;
	
	protected ObjectMapper mapper;
	
	protected RequestDecorator decorator;
	
	protected RestClientCreator creator;
	
	protected RestClientBuilder() {}
	
	public RestClientBuilder httpClient(HttpClient client) {
		this.client = client;
		return this;
	}

	public RestClientBuilder objectMapper(ObjectMapper mapper) {
		this.mapper = mapper;
		return this;
	}
	
	public RestClientBuilder requestDecorator(RequestDecorator decorator) {
		this.decorator = decorator;
		return this;
	}
	
	public RestClientBuilder restClientCreator(RestClientCreator creator) {
		this.creator = creator;
		return this;
	}
	
	public RestClient build() {
		if (creator == null) {
			creator = new RestClientCreator();
		}
		if (mapper == null) {
			mapper = new ObjectMapper();
			mapper.setSerializationInclusion(Include.NON_NULL);
		}
		if (client == null) {
			client = HttpClientBuilder.create().useSystemProperties().build();
		}
		return creator.create(this);
	}
	
}
