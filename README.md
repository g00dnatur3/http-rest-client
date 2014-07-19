http-rest-client
======================

This is an easy to use REST client written in Java. 
I was frustrated with how there is nothing out there in Java to simply execute REST http calls.
I looked at the Jersey Client and it still has too much configuration and boilerplate code.


Getting Started
-------------------------

This example code executes a GET request to the google geocoder api:

  RestClient client = RestClient.builder().build();
 
  String geocoderUrl = "http://maps.googleapis.com/maps/api/geocode/json"

		Map<String, String> params = Maps.newHashMap();
		params.put("address", "1980 W. Bayshore Rd. 94303");
		params.put("sensor", "false");

  JsonNode node = client.get(geocoderUrl, params, JsonNode.class);
  
And thats all there is to it.

The library also has delete(), update(), and create() methods...


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

So, for example, if you wanted to configure the proxy you could do

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



