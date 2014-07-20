package test.integration;

import http.rest.RestClient;

import java.net.URLEncoder;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.collect.Maps;

public class GoogleGeocodeTest {
	
	RestClient client;
	
	@Before
	public void before() {
		client = RestClient.builder().build();
	}
	
	@Test
	public void getGeocodeWithParamsAsMap() throws Exception {
		Map<String, String> params = Maps.newHashMap();
		params.put("address", "1980 W. Bayshore Rd. 94303");
		params.put("sensor", "false");
		doAssertions(client.get(Settings.geocoderUrl, params, JsonNode.class));
	}
	
	@Test
	public void getGeocodeWithParamsAsString() throws Exception{
		String address = "1980 W. Bayshore Rd. 94303";
		String url = Settings.geocoderUrl + "?address=" + URLEncoder.encode(address, "UTF-8") + "&sensor=false";
		doAssertions(client.get(url, null, JsonNode.class));
	}
	
	private void doAssertions(JsonNode node) {
		Settings.assertHasAddressComponents(node);
	}
	
}
