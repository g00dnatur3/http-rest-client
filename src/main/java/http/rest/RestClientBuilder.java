package http.rest;

import java.lang.reflect.Constructor;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;

public class RestClientBuilder {

    protected HttpClient client;

    protected ObjectMapper mapper;

    protected RequestInterceptor interceptor;

    protected Class<? extends RestClient> clazz;

    protected RestClientBuilder() {
    }

    public RestClientBuilder httpClient(HttpClient client) {
	this.client = client;
	return this;
    }

    public RestClientBuilder objectMapper(ObjectMapper mapper) {
	this.mapper = mapper;
	return this;
    }

    public RestClientBuilder requestInterceptor(RequestInterceptor interceptor) {
	this.interceptor = interceptor;
	return this;
    }

    public RestClientBuilder restClientClass(Class<? extends RestClient> clazz) {
	this.clazz = clazz;
	return this;
    }

    public RestClient build() {
	if (clazz == null) {
	    clazz = RestClient.class;
	}
	if (mapper == null) {
	    mapper = new ObjectMapper();
	    mapper.setSerializationInclusion(Include.NON_NULL);
	}
	if (client == null) {
	    client = HttpClientBuilder.create().useSystemProperties().build();
	}
	return createRestClient(this, clazz);
    }

    protected <T extends RestClient> T createRestClient(RestClientBuilder builder, Class<T> restClientClass) {
	try {
	    Constructor<T> constructor = restClientClass.getDeclaredConstructor(RestClientBuilder.class);
	    constructor.setAccessible(true);
	    return constructor.newInstance(builder);
	} catch (Exception e) {
	    throw new RuntimeException(e);
	}
    }

}
