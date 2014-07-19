http-rest-client
======================

This is an easy to use REST client written in Java. 
I was frustrated with how there is nothing out there in Java to simply execute REST http calls.
I looked at the Jersey Client and it still has too much configuration and boilerplate code.


Getting Started
======================

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
======================
