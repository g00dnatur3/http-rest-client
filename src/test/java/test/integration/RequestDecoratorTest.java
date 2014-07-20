package test.integration;

import static org.junit.Assert.assertNotNull;
import http.rest.RequestDecorator;
import http.rest.RestClient;

import java.net.URLEncoder;

import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.databind.JsonNode;

public class RequestDecoratorTest {

    RestClient client;

    @Before
    public void before() {
	client = RestClient.builder().build();
    }

    @Test
    public void decorateRequestsThruTheClientBuilder() throws Exception {
	final String address = "1980 W. Bayshore Rd. 94303";
	final String url = Settings.geocoderUrl + "?address=" + URLEncoder.encode(address, "UTF-8") + "&sensor=false";

	client = RestClient.builder().requestDecorator(new RequestDecorator() {
	    @Override
	    public void decorate(HttpUriRequest request) {
		setParams(request, address, false);
	    }
	}).build();
	doAssertions(client.get(url, null, JsonNode.class));
    }

    @Test
    public void decorateReqeustsAsNeeded() throws Exception {
	final String address = "1980 W. Bayshore Rd. 94303";
	final String url = Settings.geocoderUrl + "?address=" + URLEncoder.encode(address, "UTF-8") + "&sensor=false";

	doAssertions(client.get(new RequestDecorator() {
	    @Override
	    public void decorate(HttpUriRequest request) {
		setParams(request, address, false);
	    }
	}, url, null, JsonNode.class));
    }

    private void setParams(HttpUriRequest request, String address, boolean sensor) {
	// i don't recommend to set parameters this way,
	// its only here so i can verify the decorator is working
	HttpParams params = new BasicHttpParams();
	params.setParameter("address", address);
	params.setBooleanParameter("sensor", false);
	request.setParams(params);
    }

    private void doAssertions(JsonNode node) {
	Settings.assertHasAddressComponents(node);
    }
}
