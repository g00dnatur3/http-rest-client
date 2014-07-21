package test.integration;

import http.rest.RestClient;
import http.rest.RestClientBuilder;

import java.util.Map;

import org.apache.http.client.methods.HttpGet;
import org.junit.Test;

import test.Assertions;
import test.Settings;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.collect.Maps;

public class CustomRequestClientTest {

    RestClient client;

    @Test
    public void injectClassWithBrokenHttpGet() throws Exception {
	client = RestClient.builder().restClientClass(MyRestClient.class).build();
	JsonNode node = client.get(null, null, JsonNode.class);
	Assertions.hasAddressComponents(node);
    }

    public static class MyRestClient extends RestClient {
	protected MyRestClient(RestClientBuilder builder) {
	    super(builder);
	}

	@Override
	protected HttpGet newHttpGet(String url) {
	    Map<String, String> params = Maps.newHashMap();
	    params.put("address", "1980 W. Bayshore Rd. 94303");
	    params.put("sensor", "false");
	    url = appendParams(Settings.geocoderUrl, params);
	    return new HttpGet(url);
	}
    }

}
