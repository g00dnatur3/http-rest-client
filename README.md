http-rest-client
======================

This is an easy to use REST client written in Java and levarging the HttpClient 4.3 library. 
I was frustrated with how there is nothing out there (in Java) to simply execute REST http calls.
I looked at the Jersey Client and it still has too much configuration and boilerplate code.


Getting Started
-------------------------

This is the simplest wat to construct a RestClient

	RestClient client = RestClient.builder().build();


This example executes a GET request to the google geocoder api:

  	RestClient client = RestClient.builder().build();
 
  	String geocoderUrl = "http://maps.googleapis.com/maps/api/geocode/json"

	Map<String, String> params = Maps.newHashMap();
	params.put("address", "1980 W. Bayshore Rd. 94303");
	params.put("sensor", "false");

  	JsonNode node = client.get(geocoderUrl, params, JsonNode.class);

Executing a GET Request to get a single object
------------------------------------------------------
Example:

	String url = ...
	Map<String, String> queryParams = ...
	
	Person person = client.get(url, queryParams, Person.class);

Execute a GET Request to get list of objects
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
	
Execute a POST Request on a list of object
----------------------------------------------

	String url = ...
	List<Person> people = ...
	
	Header header = client.create(url, people);
	
	if (header != null) {
	    System.out.println("Location header is: " + header.value());
	}
	
Execute a PUT Request
----------------------------

	String url = ...
	Person person = ...
	
	client.update(url, person);

Configuration
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

So, for example, if you wanted to configure the proxy you could do:

	System.setProperty("http.proxyHost","somehost.com")
	System.setProperty("http.proxyPort","8080")


Injecting your own HttpClient
------------------------------

Because I am using the builder pattern, it is quite easy to inject your own HttpClient instance:

HttpClient myClient = ...

RestClient.builder().httpClient(myClient).build()


Next Steps
------------------------------

I will be updating the code soon to add a RequestDecorator to the builder, this will allow for the "decoration" of the request before it is sent... a good example of this would be to set the Authorization header.

RequestDecorator myDecorator = ... //probably sets required headers (Authorization) etc.

RestClient.builder().httpClient(myClient).requestDecorator(myDecorator).build();

I will also add more examples and tests...


Cheers!



