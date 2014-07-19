package test;

import http.rest.RequestDecorator;

import java.net.URLEncoder;
import java.util.Map;

import org.apache.http.client.methods.HttpUriRequest;
import org.junit.Test;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.collect.Maps;

import static org.junit.Assert.*;

public class GoogleGeocodeTest extends BaseTest {

	private static final String geocoderUrl = "http://maps.googleapis.com/maps/api/geocode/json";
	
	@Test
	public void getGeocodeWithParamsAsMap() throws Exception {
		Map<String, String> params = Maps.newHashMap();
		params.put("address", "1980 W. Bayshore Rd. 94303");
		params.put("sensor", "false");
		doAssertions(client.get(geocoderUrl, params, JsonNode.class));
	}
	
	@Test
	public void getGeocodeWithParamsAsString() throws Exception{
		String address = "1980 W. Bayshore Rd. 94303";
		String url = geocoderUrl + "?address=" + URLEncoder.encode(address, "UTF-8") + "&sensor=false";
		doAssertions(client.get(url, null, JsonNode.class));
	}
	
	private void doAssertions(JsonNode node) {
		assertNotNull(node);
		assertNotNull(node.get("results"));
	}
	
}
