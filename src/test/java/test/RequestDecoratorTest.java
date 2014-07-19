package test;

import java.util.Map;

import http.rest.RequestDecorator;

import org.apache.http.client.methods.HttpUriRequest;
import org.junit.Test;

public class RequestDecoratorTest extends BaseTest {

	@Test
	public void useRequestDecoratorViaMethodCall() {
		
		/*
		String credentials = "";//
		String url = "";// ...
		Map<String, String> queryParams = null;//
		
		Person p = client.get(authorize(credentials), url, queryParams, Person.class);
		*/
		
	}
	
	public RequestDecorator authorize(final String credentials) {
		return new RequestDecorator() {
			@Override
			public void decorate(HttpUriRequest request) {
				request.addHeader("Authorization", credentials);
			}
		};
	}
}
