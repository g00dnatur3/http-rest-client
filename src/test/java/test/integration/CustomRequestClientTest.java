package test.integration;

import static org.junit.Assert.assertTrue;
import http.rest.RestClient;
import http.rest.RestClientBuilder;

import java.util.Map;

import org.apache.http.client.methods.HttpGet;
import org.junit.Test;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.collect.Maps;

public class CustomRequestClientTest {

    RestClient client;

    @Test
    public void injectClassWithBrokenHttpGet() throws Exception {
	client = RestClient.builder().restClientClass(MyRestClient.class).build();
	Map<String, String> params = Maps.newHashMap();
	params.put("address", "1980 W. Bayshore Rd. 94303");
	params.put("sensor", "false");
	try {
	    client.get(Settings.geocoderUrl, params, JsonNode.class);
	} catch (Exception e) {
	    assertTrue(e instanceof NullPointerException);
	}
    }

    public static class MyRestClient extends RestClient {
	protected MyRestClient(RestClientBuilder builder) {
	    super(builder);
	}

	@Override
	protected HttpGet newHttpGet(String url) {
	    return null;
	}
    }

}
