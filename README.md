http-rest-client
======================

This is an easy to use REST client written in Java and levarging the HttpClient 4.3 library. 
I was frustrated with how there is nothing out there (in Java) to simply execute REST calls.
I looked at the Jersey Client and it still has too much configuration and boilerplate code.

Important Note:  This library only supports JSON payloads. You are more than welcome to add XML support and send me a pull request. I have no future plans to add XML support.

Cheers!

SBT / Maven Dependency
-------------------------

SBT

	resolvers += "java-utils" at "http://dl.bintray.com/g00dnatur3/java-utils/"
	
	libraryDependencies ++= Seq(
	  "g00dnatur3" %% "http-rest-client" % "1.0.1"
	)
	
Maven

	<repository>
		<id>java-utils</id>
		<url>http://dl.bintray.com/g00dnatur3/java-utils/</url>
	</repository>

	<dependency>
		<groupId>g00dnatur3</groupId>
		<artifactId>http-rest-client_2.10</artifactId>
		<version>1.0.1</version>
	</dependency>

Getting Started
-------------------------

This is the simplest way to construct a RestClient

	RestClient client = RestClient.builder().build();


This example executes a GET request to the google geocoder api:

  	RestClient client = RestClient.builder().build();
 
  	String geocoderUrl = "http://maps.googleapis.com/maps/api/geocode/json"

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

Setting headers & cookies with RequestDecorator (How to Authorize requests)
------------------------------------------------

In order to set headers/cookies on a request you need to use the RequestDecorator.

This is an example of a RequestDecorator that will "decorate" all requests sent from a RestClient.

	final String credentials = ...
	RequestDecorator authorize = new RequestDecorator() {
		@Override
		public void decorate(HttpUriRequest request) {
			request.addHeader("Authorization", credentials);
		}
	};
	RestClient client = RestClient.builder().requestDecorator(authorize).build();
	
All RestClient methods `get`,`create`,`update`,`delete` are overloaded with a RequestDecorator.

This allows setting headers/cookies on a per reqeust basis instead of a per client basis as show above.

This example of RequestDecorator will "decorate" on a per request basis:


	public RequestDecorator authorize(final String credentials) {
		return new RequestDecorator() {
			@Override
			public void decorate(HttpUriRequest request) {
				request.addHeader("Authorization", credentials);
			}
		};
	}
	
	public Person getPerson(String credentials) {
	    return client.get(authorize(credentials), url, null, Person.class); //queryParams=null
	}


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
If your request fails, have no fear, you can get your hands on the HttpResonse and handle it however your need.

All the RestClient methods `get` `post` `put` `create` throw a RestClientException when they fail to execute.

The RestClientException contains the actual "ready-to-consume" HttpResponse object.

I designed it so that handling errors is flexible and easy..


	try {
		Person p = client.get(url, queryParams, Person.class)	
	} catch (RestClientException e) {
	
		HttpResponse response = e.response();
		
		//if the payload contains error information you can get it:
		String errorInfo = client.contentAsString(response);
		
		client.consume(response); //closes the response
	}


Injecting your own HttpClient
------------------------------

Because I am using the builder pattern, it is quite easy to inject your own HttpClient instance:

HttpClient myClient = ...

RestClient.builder().httpClient(myClient).build()


Next Steps
------------------------------

Add more examples and tests.

Cheers!



