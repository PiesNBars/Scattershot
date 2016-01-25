package edu.cpp.cs580.customer.data;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

import org.springframework.data.annotation.Id;

public class CustomerDataset {

	@Id
	private String id;
	private String customerId;
	private String name;
	private Map<String, Integer> header;
	private List<Map<String, String>> dataset;
	
	public CustomerDataset() {
		dataset = new ArrayList<Map<String, String>>();
		header = null;
		name = null;
	}
	
	public CustomerDataset(List<Map<String,String>> dataset,
			Map<String, Integer> header){
		this.dataset = dataset;
		this.header = header;
	}
	
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public String getCustomerId() {
		return customerId;
	}
	
	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}
	
	public List<Map<String, String>> getDataset() {
		return dataset;
	}
	
	public void setDataset(List<Map<String, String>> dataset) {
		this.dataset = dataset;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Map<String, Integer> getHeader() {
		return header;
	}

	public void setHeader(Map<String, Integer> header) {
		this.header = header;
	}
	
	public void insertRow(Map<String, String> row) {
		dataset.add(row);
	}
	
	public List<String> getColumn(String columnName) {
		List<String> column = new ArrayList<>();
		Iterator<Map<String, String>> it = dataset.iterator();
		while(it.hasNext())
			column.add(it.next().get(columnName));
		
		return column;
	}
}
