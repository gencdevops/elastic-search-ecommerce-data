package com.example.repository.es;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import lombok.RequiredArgsConstructor;
import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.ClearScrollRequest;
import org.elasticsearch.action.search.ClearScrollResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.Request;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.search.Scroll;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@SuppressWarnings("deprecation")
@RequiredArgsConstructor
public abstract class GenericEsService<T> {

	private final String INDEX = "employee-data";
	private final String TYPE = "employees";


	private final RestHighLevelClient restHighLevelClient;


	private final ObjectMapper objectMapper;

	private Class<T> referenceClass;

	@SuppressWarnings("unchecked")
	public GenericEsService() {
		ParameterizedType parameterizedType = (ParameterizedType) getClass().getGenericSuperclass();
		referenceClass = (Class<T>) parameterizedType.getActualTypeArguments()[0];
	}

	public void createIndexIfNotExist() throws IOException {
		Response response = restHighLevelClient.getLowLevelClient().performRequest(new Request("HEAD", "/" + INDEX));
		int statusCode = response.getStatusLine().getStatusCode();

		if (statusCode == 404) {
			CreateIndexRequest request = new CreateIndexRequest(INDEX);
			restHighLevelClient.indices().create(request, RequestOptions.DEFAULT);
		}
	}

	public T insert(T obj, Long id) {
		Map<?, ?> dataMap = objectMapper.convertValue(obj, Map.class);
		IndexRequest indexRequest = new IndexRequest(INDEX, TYPE, id.toString()).source(dataMap).create(true);
		try {
			restHighLevelClient.index(indexRequest);
		} catch (ElasticsearchException e) {
			e.getDetailedMessage();
		} catch (IOException ex) {
			ex.getLocalizedMessage();
		} catch (Exception ex) {
			ex.getLocalizedMessage();
		}
		return obj;
	}

	public T update(T obj, Long id) {
		UpdateRequest updateRequest = new UpdateRequest(INDEX, TYPE, id.toString()).fetchSource(true);
		try {
			String employeeJson = objectMapper.writeValueAsString(obj);
			updateRequest.doc(employeeJson, XContentType.JSON);
			restHighLevelClient.update(updateRequest);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return obj;
	}

	public void delete(Long id) {
		DeleteRequest deleteRequest = new DeleteRequest(INDEX, TYPE, id.toString());
		try {
			restHighLevelClient.delete(deleteRequest);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public T searchById(Long id) {
		GetRequest getRequest = new GetRequest(INDEX, TYPE, id + "");
		String resp = "";
		try {
			createIndexIfNotExist();
			resp = restHighLevelClient.get(getRequest).getSourceAsString();
			if (resp != null)
				return (T) objectMapper.readValue(resp, referenceClass);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public List<T> searchAll() {
		return searchByQueryWithOrder(null, null);
	}

	public List<T> searchByQuery(BoolQueryBuilder query) {
		return searchByQueryWithOrder(query, null);
	}

	public List<T> searchByQueryWithOrder(BoolQueryBuilder query, SortOrder order) {
		List<T> returnObject = new ArrayList<>();

		try {
			SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
			sourceBuilder.timeout(new TimeValue(600, TimeUnit.SECONDS));
			sourceBuilder.size(100); // Size of result hits in scroll
			if (order != null)
				sourceBuilder.sort(order.name());
			if (query != null)
				sourceBuilder.query(query);

			SearchRequest searchRequest = new SearchRequest(INDEX);
			searchRequest.source(sourceBuilder);

			final Scroll scroll = new Scroll(TimeValue.timeValueMinutes(10L));
			searchRequest.scroll(scroll);// Keep scroll alive
			SearchResponse searchResponse = restHighLevelClient.search(searchRequest);

			String scrollId = searchResponse.getScrollId();
			SearchHit[] searchHits = searchResponse.getHits().getHits();
			for (SearchHit hit : searchHits) {
				String sourceAsString = hit.getSourceAsString();
				if (sourceAsString != null) {
					returnObject.add(objectMapper.readValue(sourceAsString, referenceClass));
				}
			}
			ClearScrollRequest clearScrollRequest = new ClearScrollRequest();
			clearScrollRequest.addScrollId(scrollId);
			ClearScrollResponse clearScrollResponse = restHighLevelClient.clearScroll(clearScrollRequest);
			if (!clearScrollResponse.isSucceeded()) {
				System.out.println("Could not close scroll with scroll id: " + scrollId);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return returnObject;
	}

}
