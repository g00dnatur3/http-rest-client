http-rest-client
======================

A simple & easy to use REST client written in Java and levarging the HttpClient 4.3 library.

Important Note:  This library only supports JSON payloads. You are more than welcome to add XML support and send me a pull request. I have no future plans to add XML support.

Cheers!

SBT / Maven Dependency
-------------------------

SBT

	resolvers += "java-utils" at "http://dl.bintray.com/g00dnatur3/java-utils/"
	
	libraryDependencies ++= Seq(
	  "g00dnatur3" %% "http-rest-client" % "1.0.21"
	)
	
Maven

	<repository>
		<id>java-utils</id>
		<url>http://dl.bintray.com/g00dnatur3/java-utils/</url>
	</repository>

	<dependency>
		<groupId>g00dnatur3</groupId>
		<artifactId>http-rest-client_2.10</artifactId>
		<version>1.0.21</version>
	</dependency>

Getting Started
-------------------------

This is the simplest way to construct a RestClient

	RestClient client = RestClient.builder().build();


This example executes a GET request to the google geocoder api:

  	RestClient client = RestClient.builder().build();
 
  	String geocoderUrl = "http://maps.googleapis.com/maps/api/geocode/json";

	Map<String, String> params = Maps.newHashMap();
	params.put("address", "1980 W. Bayshore Rd. 94303");
	params.put("sensor", "false");

  	JsonNode node = client.get(geocoderUrl, params, JsonNode.class);

Execute a GET Request to get a single object
------------------------------------------------------

	String url = ...
	Map<String, String> queryParams = ...
	
	Person person = client.get(url, queryParams, Person.class);

Execute a GET Request to get a list of objects
------------------------------------------------------

	String url = ...
	Map<String, String> queryParams = ...
	
	List<Person> people = client.get(url, queryParams, new TypeReference<List<Person>>() {});

Execute a POST Request on a single object
----------------------------------------------

	String url = ...
	Person person = ...
	
	Header header = client.create(url, person);
	
	if (header != null) {
		System.out.println("Location header is: " + header.value());
	}
	
Execute a POST Request on a list of objects
----------------------------------------------

	String url = ...
	List<Person> people = ...
	
	Header header = client.create(url, people);
	
	if (header != null) {
		System.out.println("Location header is: " + header.value());
	}
	
Execute a PUT Request on a single object
---------------------------------------------

	String url = ...
	Person person = ...
	
	client.update(url, person);
	
Execute a PUT Request on a list of objects
---------------------------------------------

	String url = ...
	List<Person> people = ...
	
	client.update(url, people);

Add Headers with RequestInterceptor
------------------------------------------------

In order to add headers on a request you need to use the RequestInterceptor.

This is an example of a RequestInterceptor that will "intercept" all requests sent from a RestClient and add an Authorization header to them.

	final String credentials = ...
	RequestInterceptor authorize = new RequestInterceptor() {
		@Override
		public void intercept(HttpRequestBase request) {
			request.addHeader("Authorization", credentials);
		}
	};
	RestClient client = RestClient.builder().requestInterceptor(authorize).build();
	
All the `get`,`create`,`update`,`delete` methods are overloaded with a RequestInterceptor.

This allows adding headers on a per reqeust basis instead of a per client basis as shown above.

This example of RequestInterceptor will "intercept" on a per request basis to add a header:

	public RequestInterceptor authorize(final String credentials) {
		return new RequestInterceptor() {
			@Override
			public void intercept(HttpUriRequest request) {
				request.addHeader("Authorization", credentials);
			}
		};
	}
	
	public Person getPerson(String credentials, url) {
	    return client.get(authorize(credentials), url, null, Person.class); //queryParams=null
	}

	public Header createPerson(String credentials, Person person, url) {
	    return client.create(authorize(credentials), url, person);
	}

Look at `test.integration.RequestInterceptorTest` to see working examples.

Configure the Request with RequestInterceptor
----------------------------------------------

Similar with how we can add headers on a per request basis, we can also do some config on a per request basis.

	public RequestInterceptor connectTimeout(final int timeout) {
		return new RequestInterceptor() {
			@Override
			public void intercept(HttpUriRequest request) {
				request.setConfig(
					RequestConfig.custom()
					.setConnectTimeout(timeout).build());
			}
		};
	}
	
	client.create(connectTimeout(1000), url, person);
	

Configuration (Proxy, SSL...)
-------------------------

The following system properties are taken into account by this library:

	ssl.TrustManagerFactory.algorithm
	javax.net.ssl.trustStoreType
	javax.net.ssl.trustStore
	javax.net.ssl.trustStoreProvider
	javax.net.ssl.trustStorePassword
	java.home
	ssl.KeyManagerFactory.algorithm
	javax.net.ssl.keyStoreType
	javax.net.ssl.keyStore
	javax.net.ssl.keyStoreProvider
	javax.net.ssl.keyStorePassword
	http.proxyHost
	http.proxyPort
	http.nonProxyHosts
	http.keepAlive
	http.maxConnections

For example, if you wanted to configure the proxy you could do:

	System.setProperty("http.proxyHost","somehost.com")
	System.setProperty("http.proxyPort","8080")

Handling failed Requests
------------------------------
If your request fails, have no fear, you can get your hands on the HttpResonse and handle it however you need.

The `get`,`create`,`update`,`delete` methods throw a RestClientException when they fail to execute.

The RestClientException contains the actual "ready-to-consume" HttpResponse object.

	try {
		Person p = client.get(url, queryParams, Person.class)	
	} catch (RestClientException e) {
	
		HttpResponse response = e.response();
		if (response != null) {
		
		    // if the payload contains error information you can get it
		    String errorInfo = client.contentAsString(response);
	
		    client.consume(response); //closes the response
		}
	}

Expected Response Status
------------------------------

The default expected response status for `get`,`update`,`delete` is 200 and for `create` it's 201.

The RestClient will throw a RestClientException if the actual response status is not equal to the expected status.

All the methods in the RestClient are overloaded with `int expectedStatus` so you can specify the expected response status if it deviates from the default.

For example, the `create` has a default expected status of 201. However, if the server sends back a 200 response instead, you can do the following:

	String url = ...
	Person person = ...
	
	try {
		Header header = client.create(url, person, 200);
	} catch (RestClientException e) {
		// here if the response status is not 200 or the request failed to execute.
	}


Injecting your own HttpClient
------------------------------

Because I am using the builder pattern, it is quite easy to inject your own HttpClient instance:

	HttpClient myHttpClient = ...

	RestClient.builder().httpClient(myHttpClient).build();

Set Cookies with your own HttpClient
-------------------------------------

In order to set cookies, you need create your own HttpClient like so:

	BasicCookieStore cookieStore = new BasicCookieStore();
	BasicClientCookie cookie = new BasicClientCookie("JSESSIONID", "1234");
    	cookie.setDomain(".github.com");
    	cookie.setPath("/");
    	cookieStore.addCookie(cookie);
    	
	//notice the useSystemPoperties() -> enables configuration (Proxy, SSL...) via system properties
	HttpClient myHttpClient = HttpClientBuilder.create().setDefaultCookieStore(cookieStore).useSystemProperties().build();

	RestClient client = RestClient.builder().httpClient(myHttpClient).build();

Injecting your own RestClient!
--------------------------------

Yup, your read right, you can even inject your own RestClient implementation thru the builder.

	// you can override any of the RestClient methods you desire
	
	public class MyRestClient extends RestClient {
		protected MyRestClient(RestClientBuilder builder) {
			super(builder);
		}
		
		@Override
		protected HttpGet newHttpGet(String url) {
			...
		}
	}
	
	RestClient client = RestClient.builder().restClientClass(MyRestClient.class).build();
	
Look at `test.integration.CustomRestClientTest` to a see working example.


Next Steps
------------------------------

Add more tests & a plugin to get code coverage.

Currently there are 9 tests, 5 integration, and 4 unit.


Cheers!



