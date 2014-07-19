package http.rest;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;

import com.fasterxml.jackson.core.type.TypeReference;

public class RestClient extends AbstractRestClient {

	protected RestClient(RestClientBuilder builder) {
		super(builder);
	}

    private String get(RequestDecorator decorator, String path, Map<String, String> queryParams, int expectedStatus) 
    		throws RestClientException, IOException {
    	
        HttpGet get = new HttpGet(appendParams(path, queryParams));
        HttpResponse response = execute(decorator, get, expectedStatus);
        String content = contentAsString(response);
        EntityUtils.consume(response.getEntity());
        return content;
    }

    public <T> T get(RequestDecorator decorator, String path, Map<String, String> queryParams, Class<T> entityClass, int expectedStatus)
            throws Exception {
        String content = get(decorator, path, queryParams, expectedStatus);
        if (content != null) {
            return bindObject(content, entityClass);
        }
        else {
            return null;
        }
    }
    
    public <T> List<T> get(RequestDecorator decorator, String path, Map<String, String> params, TypeReference<List<T>> type, int expectedStatus)
            throws Exception {
    	String content = get(decorator, path, params, expectedStatus);
        if (content != null) {
        	return bindJsonArray(content, type);
        }
        else {
            return null;
        }
    }
   
    public <T> T get(RequestDecorator decorator, String path, Map<String, String> params, Class<T> entityClass)
            throws Exception {
    	return get(decorator, path, params, entityClass, 200);
    }
    
    public <T> T get(String path, Map<String, String> params, Class<T> entityClass)
            throws Exception {
    	return get(null, path, params, entityClass, 200);
    }
    
    public <T> List<T> get(RequestDecorator decorator, String path, Map<String, String> params, TypeReference<List<T>> type)
            throws Exception {
    	return get(decorator, path, params, type, 200);
    }
    
    public <T> List<T> get(String path, Map<String, String> params, TypeReference<List<T>> type)
            throws Exception {
    	return get(null, path, params, type, 200);
    }
    
    public Header create(RequestDecorator decorator, String path, List<?> data) throws Exception {
        return create(decorator, path, data, 201);
    }
    
    public Header create(String path, List<?> data) throws Exception {
        return create(null, path, data, 201);
    }

    private Header create(RequestDecorator decorator, String path, Object object, int expectedStatus) throws RestClientException, IOException {
        HttpPost post = contentTypeJson(new HttpPost(path));
        HttpEntity entity = new StringEntity(toJson(object).toString(), "UTF-8");
        post.setEntity(entity);
        return execute(decorator, post, expectedStatus).getFirstHeader("Location");
    }

    public Header create(RequestDecorator decorator, String path, Object object)
            throws Exception {
        return create(decorator, path, object, 201);
    }
    
    public Header create(String path, Object object)
            throws Exception {
        return create(null, path, object, 201);
    }

    private Header create(RequestDecorator decorator, String path, List<?> data, int expectedStatus) throws RestClientException, IOException {
    	HttpPost post = contentTypeJson(new HttpPost(path));
        HttpEntity entity = new StringEntity(toJsonArray(data).toString(), "UTF-8");
        post.setEntity(entity);
        return execute(decorator, post, expectedStatus).getFirstHeader("Location");
    }
    
    public void delete(RequestDecorator decorator, String path) throws Exception {
        delete(decorator, path, 200);
    }

    public void delete(String path) throws Exception {
        delete(null, path, 200);
    }

    private void delete(RequestDecorator decorator, String path, int expectedStatus) throws RestClientException, IOException {
        HttpDelete delete = new HttpDelete(path);
        HttpResponse response = execute(decorator, delete, expectedStatus);
        EntityUtils.consume(response.getEntity());
    }

    public void update(RequestDecorator decorator, String path, Object object) throws Exception {
        update(decorator, path, object, 200);
    }
    
    public void update(String path, Object object) throws Exception {
        update(null, path, object, 200);
    }

    private void update(RequestDecorator decorator, String path, Object object, int expectedStatus) 
    		throws RestClientException, IOException {
    	
        HttpPut put = contentTypeJson(new HttpPut(path));
        HttpEntity entity = new StringEntity(toJson(object).toString(), "UTF-8");
        put.setEntity(entity);
        HttpResponse response = execute(decorator, put, expectedStatus);
        EntityUtils.consume(response.getEntity());
    }
    
    public void update(RequestDecorator decorator, String path, List<?> data) throws Exception {
        update(decorator, path, data, 200);
    }
    
    public void update(String path, List<?> data) throws Exception {
        update(null, path, data, 200);
    }
    
    private void update(RequestDecorator decorator, String path, List<?> data, int expectedStatus) 
    		throws RestClientException, IOException {
    	
        HttpPut put = contentTypeJson(new HttpPut(path));
        HttpEntity entity = new StringEntity(toJsonArray(data).toString(), "UTF-8");
        put.setEntity(entity);
        HttpResponse response = execute(decorator, put, expectedStatus);
        EntityUtils.consume(response.getEntity());
    }
}
