package http.rest;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.ContentType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public abstract class AbstractRestClient {
	
	protected final Logger logger = LoggerFactory.getLogger(getClass());

	protected final HttpClient client;
	
	protected final ObjectMapper mapper;

	protected AbstractRestClient(RestClientBuilder builder) {
		this.client = builder.client;
		this.mapper = builder.mapper;
	}
	
	public static RestClientBuilder builder() {
		return new RestClientBuilder();
	};
	
	protected <T> T bindObject(String source, Class<T> entityClass) throws IOException {
        return mapper.readValue(source, entityClass);
    }

	protected <T> List<T> bindJsonArray(String source, TypeReference<List<T>> type) throws IOException {
        return mapper.readValue(source, type);
    }
	
	protected JsonNode toJson(Object object) throws IOException {
        if(object instanceof JsonNode)
            return (JsonNode) object;
        return mapper.valueToTree(object);
    }
	
	protected <T> JsonNode toJsonArray(List<T> data) throws IOException {
        return mapper.convertValue(data, JsonNode.class);
    }
    
    protected String contentAsString(HttpResponse response) throws Exception {
        return IOUtils.toString(response.getEntity().getContent(), "UTF-8");
    }

    protected byte[] contentAsBytes(HttpResponse response) throws Exception {
        return IOUtils.toByteArray(response.getEntity().getContent());
    }
    
    protected <T extends HttpUriRequest> HttpResponse execute(T request, int expectedStatus) throws Exception {
    	String method = request.getMethod();
    	String path = request.getURI().toString();
    	logger.info("Send --> " + method + " " + path);
    	HttpResponse response = client.execute(request);
    	int status = response.getStatusLine().getStatusCode();
        if (expectedStatus != successStatus(method)) {
            if (status == successStatus(method)) {
                logger.error("[Not Expected] Successfully sent " + method + " " + path);
            }
            else {
                logger.info("[Expected] Failed to send " + method + " " + path);
            }
        }
        else {
            if (status == successStatus(method)) {
                logger.info("[Expected] Successfully sent " + method + " " + path);
            }
            else {
                logger.error("[Not Expected] Failed to send " + method + " " + path);
            }
        }
        if (expectedStatus != status) {
        	StringBuilder sb = new StringBuilder("Status of " + status);
        	sb.append(" not equal to expected value of ").append(expectedStatus);
        	throw new RestClientException(sb.toString(), response);
        }
        return response;
    }
    
    private int successStatus(String method) {
    	switch (method) {
    		case "POST": return 201;
    		default: return 200;
    	}
    }
    
    protected String appendParams(String path, Map<String, String> params) {
    	if (params != null) {
    		return path + queryString(params);
    	}
    	return path;
    }
    
    protected String queryString(Map<String, String> params) {
        if (params == null || params.isEmpty()) return "";
        StringBuilder sb = new StringBuilder("?");
        int i = 0;
        for (Map.Entry<String, String> entry : params.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            if (i > 0) {
                sb.append("&");
            }
            try {
                sb.append(key).append("=").append(URLEncoder.encode(value, "utf-8"));
            }
            catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            i++;
        }
        return sb.toString();
    }
    
    protected <T extends HttpUriRequest> T contentTypeJson(T request) {
    	request.addHeader("Content-Type", ContentType.APPLICATION_JSON.toString());
    	return request;
    }
}
