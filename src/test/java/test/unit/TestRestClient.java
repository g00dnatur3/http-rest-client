package test.unit;

import junit.framework.Assert;
import http.rest.RestClient;

import org.apache.http.Header;
import org.junit.Before;
import org.junit.Test;

import test.Assertions;
import test.Settings;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class TestRestClient extends BaseTest {

    RestClient client;

    @Before
    public void before() throws Exception {
	client = RestClient.builder().restClientClass(MockRestClient.class).build();
    }

    @Test
    public void get() throws Exception {
	JsonNode node = client.get(Settings.geocoderUrl, null, JsonNode.class);
	Assertions.hasAddressComponents(node);
    }

    @Test
    public void post() throws Exception {
	Header header = client.create(Settings.geocoderUrl, new ObjectNode(JsonNodeFactory.instance));
	Assert.assertEquals(Settings.mockLocationHeader, header.getValue());
    }
    
    @Test
    public void put() throws Exception {
	client.update(Settings.geocoderUrl, new ObjectNode(JsonNodeFactory.instance));
    }
    
    @Test
    public void delete() throws Exception {
	client.delete(Settings.geocoderUrl);
    }
}
