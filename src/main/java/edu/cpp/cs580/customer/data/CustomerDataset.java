package edu.cpp.cs580.customer.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.data.annotation.Id;

import edu.cpp.cs580.util.Mappable;

public class CustomerDataset {

	@Id
	private String id;
	private String customerId;
	private String name;
	private Set<String> header;
	private List<Map<String, String>> dataset;
	
	public CustomerDataset() {
		dataset = new ArrayList<Map<String, String>>();
		header = null;
		name = null;
	}
	
	public CustomerDataset(List<Map<String,String>> dataset, Set<String> header) {
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

	public Set<String> getHeader() {
		return header;
	}

	public void setHeader(Set<String> header) {
		this.header = header;
	}
	
	public void insertRow(Map<String, String> row) {
		dataset.add(row);
	}
	
	public <T> List<Map<String, T>> map(Mappable<Map<String, String>, Map<String, T>> func) {
		List<Map<String, T>> mappingResult = new ArrayList<>();
		Iterator<Map<String, String>> data = dataset.iterator();
		Map<String, T> resultRow = null;
				
		while(data.hasNext()){
			resultRow = func.withArgument(data.next()).call();
			mappingResult.add(resultRow);
		}
		
		return mappingResult;
	}
	
	public CustomerDataset getColumns(String... columnNames) {
		Set<String> columnSet = new HashSet<>();
		
		for(String column : columnNames)
			if(header.contains(column))
				columnSet.add(column);
		
		return getColumns(columnSet);
	}
	
	public CustomerDataset getColumns(Set<String> columnNames) {
		Slice slice = new Slice(columnNames);
		List<Map<String, String>> dataSlice = map(slice);
		CustomerDataset result = new CustomerDataset();
		
		result.dataset = dataSlice;
		result.header = columnNames;
		result.id = id;
		result.name = name;
		result.customerId = customerId;
		
		return result;
	}
	
	public class Slice implements Mappable<Map<String, String>, Map<String, String>> {
		
		private Set<String> columns;
		private Map<String, String> arg;
		
		public Slice(Set<String> columns) {
			this.columns = columns;
		}
		
		public Slice withArgument(Map<String, String> arg) {
			this.arg = arg;
			return this;
		}
		
		public Map<String, String> call() {
			if(arg == null)
				return null;
			
			Map<String, String> result = new HashMap<>();
			for(String column : columns)
				result.put(column, arg.get(column));
			
			return result;
		}
	}
}
