package test.integration;

import static org.junit.Assert.assertNotNull;
import http.rest.RequestInterceptor;
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
    public void interceptAllRequestsThruTheClientBuilder() throws Exception {
	final String address = "1980 W. Bayshore Rd. 94303";
	final String url = Settings.geocoderUrl + "?address=" + URLEncoder.encode(address, "UTF-8") + "&sensor=false";

	client = RestClient.builder().requestInterceptor(new RequestInterceptor() {
	    @Override
	    public void intercept(HttpUriRequest request) {
		setParams(request, address, false);
	    }
	}).build();
	doAssertions(client.get(url, null, JsonNode.class));
    }

    @Test
    public void interceptReqeustsAsNeeded() throws Exception {
	final String address = "1980 W. Bayshore Rd. 94303";
	final String url = Settings.geocoderUrl + "?address=" + URLEncoder.encode(address, "UTF-8") + "&sensor=false";

	doAssertions(client.get(new RequestInterceptor() {
	    @Override
	    public void intercept(HttpUriRequest request) {
		setParams(request, address, false);
	    }
	}, url, null, JsonNode.class));
    }

    private void setParams(HttpUriRequest request, String address, boolean sensor) {
	// i don't recommend to set parameters this way,
	// its only here so i can verify the interceptor is working
	HttpParams params = new BasicHttpParams();
	params.setParameter("address", address);
	params.setBooleanParameter("sensor", false);
	request.setParams(params);
    }

    private void doAssertions(JsonNode node) {
	Settings.assertHasAddressComponents(node);
    }
}
