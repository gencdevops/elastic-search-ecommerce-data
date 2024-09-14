package com.example.repository.es;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.stereotype.Service;

import com.example.model.Employee;

@Service
public class EmployeeESRepo extends GenericEsService<Employee> {

	public List<Employee> searchByName(String name) {
		BoolQueryBuilder query = new BoolQueryBuilder();
		//query.must(new MatchQueryBuilder("firstName", name));// Exact Match
		query.must(QueryBuilders.queryStringQuery("*" + name + "*").field("firstName"));// Match like in MySql
		query.mustNot(QueryBuilders.termQuery("id", 8)); // Exclude Specific field with value

		// for multiple value ignore for specific field
		Collection<Long> collection = new ArrayList<>();
		collection.add(8l);
		org.elasticsearch.index.query.QueryBuilder fb = QueryBuilders.termsQuery("firstName", collection);
		query.mustNot(fb);
		
		return searchByQuery(query);
	}

}
