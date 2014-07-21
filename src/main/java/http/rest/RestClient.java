package http.rest;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.Charsets;
import org.apache.commons.io.IOUtils;
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

    protected String get(RequestInterceptor interceptor, String path, Map<String, String> queryParams,
	    int expectedStatus) throws RestClientException, IOException {

	HttpGet get = newHttpGet(appendParams(path, queryParams));
	HttpResponse response = execute(interceptor, get, expectedStatus);
	String content = null;
	try {
	    content = contentAsString(response);
	} catch (IOException e) {
	    consume(response);
	}
	return content;
    }

    public <T> T get(RequestInterceptor interceptor, String path, Map<String, String> queryParams,
	    Class<T> entityClass, int expectedStatus) throws RestClientException, IOException {
	String content = get(interceptor, path, queryParams, expectedStatus);
	if (content != null) {
	    return bindObject(content, entityClass);
	} else {
	    return null;
	}
    }

    public <T> List<T> get(RequestInterceptor interceptor, String path, Map<String, String> params,
	    TypeReference<List<T>> type, int expectedStatus) throws RestClientException, IOException {
	String content = get(interceptor, path, params, expectedStatus);
	if (content != null) {
	    return bindJsonArray(content, type);
	} else {
	    return null;
	}
    }

    public <T> T get(RequestInterceptor interceptor, String path, Map<String, String> params, Class<T> entityClass)
	    throws RestClientException, IOException {
	return get(interceptor, path, params, entityClass, 200);
    }

    public <T> T get(String path, Map<String, String> params, Class<T> entityClass) throws RestClientException,
	    IOException {
	return get(null, path, params, entityClass, 200);
    }

    public <T> List<T> get(RequestInterceptor interceptor, String path, Map<String, String> params,
	    TypeReference<List<T>> type) throws RestClientException, IOException {
	return get(interceptor, path, params, type, 200);
    }

    public <T> List<T> get(String path, Map<String, String> params, TypeReference<List<T>> type)
	    throws RestClientException, IOException {
	return get(null, path, params, type, 200);
    }

    public Header create(RequestInterceptor interceptor, String path, List<?> data) throws RestClientException,
	    IOException {
	return create(interceptor, path, data, 201);
    }

    public Header create(String path, List<?> data) throws RestClientException, IOException {
	return create(null, path, data, 201);
    }

    public Header create(RequestInterceptor interceptor, String path, Object object, int expectedStatus)
	    throws RestClientException, IOException {
	HttpPost post = contentTypeJson(newHttpPost(path));
	HttpEntity entity = new StringEntity(toJson(object).toString(), Charsets.UTF_8);
	post.setEntity(entity);
	HttpResponse response = execute(interceptor, post, expectedStatus);
	consume(response);
	return response.getFirstHeader("Location");
    }

    public Header create(RequestInterceptor interceptor, String path, Object object) throws RestClientException,
	    IOException {
	return create(interceptor, path, object, 201);
    }

    public Header create(String path, Object object) throws RestClientException, IOException {
	return create(null, path, object, 201);
    }

    public Header create(RequestInterceptor interceptor, String path, List<?> data, int expectedStatus)
	    throws RestClientException, IOException {
	HttpPost post = contentTypeJson(newHttpPost(path));
	HttpEntity entity = new StringEntity(toJsonArray(data).toString(), Charsets.UTF_8);
	post.setEntity(entity);
	HttpResponse response = execute(interceptor, post, expectedStatus);
	consume(response);
	return response.getFirstHeader("Location");
    }

    public void delete(RequestInterceptor interceptor, String path) throws RestClientException, IOException {
	delete(interceptor, path, 200);
    }

    public void delete(String path) throws RestClientException, IOException {
	delete(null, path, 200);
    }

    public void delete(RequestInterceptor interceptor, String path, int expectedStatus) throws RestClientException,
	    IOException {
	HttpDelete delete = newHttpDelete(path);
	consume(execute(interceptor, delete, expectedStatus));
    }

    public void update(RequestInterceptor interceptor, String path, Object object) throws RestClientException,
	    IOException {
	update(interceptor, path, object, 200);
    }

    public void update(String path, Object object) throws RestClientException, IOException {
	update(null, path, object, 200);
    }

    private void update(RequestInterceptor interceptor, String path, Object object, int expectedStatus)
	    throws RestClientException, IOException {

	HttpPut put = contentTypeJson(newHttpPut(path));
	HttpEntity entity = new StringEntity(toJson(object).toString(), Charsets.UTF_8);
	put.setEntity(entity);
	consume(execute(interceptor, put, expectedStatus));
    }

    public void update(RequestInterceptor interceptor, String path, List<?> data) throws RestClientException,
	    IOException {
	update(interceptor, path, data, 200);
    }

    public void update(String path, List<?> data) throws RestClientException, IOException {
	update(null, path, data, 200);
    }

    public void update(RequestInterceptor interceptor, String path, List<?> data, int expectedStatus)
	    throws RestClientException, IOException {

	HttpPut put = contentTypeJson(newHttpPut(path));
	HttpEntity entity = new StringEntity(toJsonArray(data).toString(), Charsets.UTF_8);
	put.setEntity(entity);
	consume(execute(interceptor, put, expectedStatus));
    }

    public void consume(HttpResponse response) throws IOException {
	if (response != null && response.getEntity() != null) {
	    EntityUtils.consume(response.getEntity());
	}
    }

    public String contentAsString(HttpResponse response) throws IOException {
	if (response != null && response.getEntity() != null) {
	    return IOUtils.toString(response.getEntity().getContent(), Charsets.UTF_8);
	}
	return null;
    }
}
