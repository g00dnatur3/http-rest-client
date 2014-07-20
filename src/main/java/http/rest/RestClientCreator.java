package http.rest;

public class RestClientCreator {

	public RestClient create(RestClientBuilder builder) {
		return new RestClient(builder);
	}
	
}
