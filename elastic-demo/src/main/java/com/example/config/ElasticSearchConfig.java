package com.example.config;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.AbstractFactoryBean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ElasticSearchConfig extends AbstractFactoryBean<RestHighLevelClient> {

	@Value("${elasticsearch.node}")
	private String clusterNodes;

	@Value("${elasticsearch.clustername}")
	private String clusterName;

	@Value("${elasticsearch.host}")
	private String host;

	@Value("${elasticsearch.port}")
	private int portNumber;

	@Value("${elasticsearch.port1}")
	private int portNumber1;

	RestHighLevelClient restHighLevelClient;

	@Override
	public Class<RestHighLevelClient> getObjectType() {
		return RestHighLevelClient.class;
	}

	@Override
	protected RestHighLevelClient createInstance() throws Exception {
		return buildClient();
	}

	private RestHighLevelClient buildClient() {
		try {
			restHighLevelClient = new RestHighLevelClient(RestClient.builder(new HttpHost(host, portNumber, "http"),
					new HttpHost(host, portNumber1, "http")));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return restHighLevelClient;
	}

	@Override
	public void destroy() throws Exception {
		if (restHighLevelClient != null)
			restHighLevelClient.close();
	}

	@Override
	public boolean isSingleton() {
		return false;
	}
}
