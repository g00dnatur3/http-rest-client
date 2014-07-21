package test.unit;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import http.rest.RequestInterceptor;
import http.rest.RestClient;
import http.rest.RestClientBuilder;

import java.io.IOException;

import org.apache.commons.io.IOUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;

import test.Settings;

public class BaseTest {

    RestClient client;

    public static class MockRestClient extends RestClient {
	
	protected MockRestClient(RestClientBuilder builder) {
	    super(builder);
	}

	@Override
	protected HttpResponse execute(RequestInterceptor interceptor, HttpUriRequest request)
	        throws ClientProtocolException, IOException {

	    return mockHttpResponse(request);
	}
    }

    public static HttpResponse mockHttpResponse(HttpUriRequest request) throws IOException {
	HttpResponse mockHttpResponse = mock(HttpResponse.class);
	StatusLine statusLine = mock(StatusLine.class);
	if (request instanceof HttpPost) {
	    when(statusLine.getStatusCode()).thenReturn(201);
	    when(mockHttpResponse.getStatusLine()).thenReturn(statusLine);
	    Header header = mock(Header.class);
	    when(header.getValue()).thenReturn(Settings.mockLocationHeader);
	    when(mockHttpResponse.getFirstHeader("Location")).thenReturn(header);
	} else {
	    when(statusLine.getStatusCode()).thenReturn(200);
	    when(mockHttpResponse.getStatusLine()).thenReturn(statusLine);
	}
	if (request instanceof HttpGet) {
	    HttpEntity entity = mock(HttpEntity.class);
	    when(entity.getContent()).thenReturn(IOUtils.toInputStream(Settings.geocodeJson));
	    when(mockHttpResponse.getEntity()).thenReturn(entity);
	}
	return mockHttpResponse;
    }

}
