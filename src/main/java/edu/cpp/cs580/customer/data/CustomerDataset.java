package edu.cpp.cs580.customer.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.data.annotation.Id;

import edu.cpp.cs580.util.Mappable;
import edu.cpp.cs580.util.Reduceable;

public class CustomerDataset {

	@Id
	private String id;
	private String customerId;
	private String name;
	private Map<String, String> typeMap;
	private List<Map<String, ? extends Serializable>> dataset;
	
	public CustomerDataset(){
		dataset = new ArrayList<>();
	};
	
	public CustomerDataset(List<Map<String, ? extends Serializable>> dataset) {
		this.dataset = dataset;
		typeMap = createTypeMap(dataset);
	}
	
	public CustomerDataset(List<Map<String, ? extends Serializable>> dataset,
			Map<String, String> typeMap) {
		this.dataset = dataset;
		this.typeMap = typeMap;
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
	
	public List<Map<String, ? extends Serializable>> getDataset() {
		return dataset;
	}
	
	public void setDataset(List<Map<String, ? extends Serializable>> dataset) {
		this.dataset = dataset;
		typeMap = createTypeMap(dataset);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public Map<String, String> getTypeMap() {
		return typeMap;
	}
	
	public void setTypeMap(Map<String, String> typeMap) {
		this.typeMap = typeMap;
	}

	public Set<String> typeMap() {
		return typeMap.keySet();
	}
	
	public void insertRow(Map<String, String> row) {
		dataset.add(row);
	}

	public List<String> push(Map<String, ? extends Serializable> row) {
		Iterator<String> keys = row.keySet().iterator();
		List<String> wrongTypes = new ArrayList<String>();
		String key;
		
		if(typeMap == null) {
			typeMap = createTypeMap(row);
		}
		
		while(keys.hasNext()) {
			key = keys.next();
			Serializable value = row.get(key);
			try {
				Class<?> clazz = Class.forName(typeMap.get(key));
				clazz.cast(value);
			} catch (ClassCastException cce) {
				row.put(key, null);
				wrongTypes.add(key);
			} catch (ClassNotFoundException cnfe) {
				cnfe.printStackTrace();
			}
		}
		
		if(!(wrongTypes.size() > 0))
			dataset.add(row);
		
		return wrongTypes;
	}
	
	public List<Map<String, ? extends Serializable>> map(
			Mappable<Map<String, ? extends Serializable>, Map<String, ? extends Serializable>> func) {
		
		List<Map<String, ? extends Serializable>> mappingResult = new ArrayList<>();
		Iterator<Map<String, ? extends Serializable>> data = dataset.iterator();
		Map<String, ? extends Serializable> resultRow = null;
				
		while(data.hasNext()){
			resultRow = func.withArgument(data.next()).call();
			mappingResult.add(resultRow);
		}
		
		return mappingResult;
	}
	
	public <T> T reduce(Reduceable<CustomerDataset, T> reduceFunc,
			String... columns) {
		return reduceFunc.withArgument(this).call();
	}
	
	public CustomerDataset getColumns(String... columnNames) {

		Map<String, String> columns = new HashMap<>(typeMap);
		Set<String> subset = new HashSet<>(Arrays.asList(columnNames));
		Set<String> superset = columns.keySet();
		String[] supersetKeys = superset.toArray(new String[superset.size()]);
		
		for(int i = 0; i < supersetKeys.length; i++) {
			String column = supersetKeys[i];
			if(!(subset.contains(column)))
				columns.remove(column);
		}
		
		return getColumns(columns);
	}
	
	private CustomerDataset getColumns(Map<String, String> columnNames) {
		Slice slice = new Slice(columnNames.keySet());
		List<Map<String, ? extends Serializable>> dataSlice = map(slice);
		CustomerDataset result = new CustomerDataset(dataSlice);
		
		result.dataset = dataSlice;
		result.typeMap = columnNames;
		result.id = id;
		result.name = name;
		result.customerId = customerId;
		
		return result;
	}
	
	public class Slice implements Mappable<Map<String, ? extends Serializable>, Map<String, ? extends Serializable>> {
		
		private Set<String> columns;
		private Map<String, ? extends Serializable> arg;
		
		public Slice(Set<String> columns) {
			this.columns = columns;
		}
		
		public Slice withArgument(Map<String, ? extends Serializable> arg) {
			this.arg = arg;
			return this;
		}
		
		public Map<String, ? extends Serializable> call() {
			if(arg == null)
				return null;
			
			Map<String, ? extends Serializable> result = new HashMap<>(arg);
			Set<String> allColumns = typeMap.keySet();
			for(String column : allColumns)
				if(!(columns.contains(column)))
				result.remove(column);
			
			return result;
		}
	}
	
	private Map<String, String> createTypeMap(List<Map<String, ? extends Serializable>> data) {
		
		Map<String, String> newTypeMap = null;
		
		if(data != null && data.size() > 0) {
			Map<String, ? extends Serializable> firstRow = data.get(0);
			newTypeMap = createTypeMap(firstRow);
		}
		
		return newTypeMap;
	}
	
	private Map<String, String> createTypeMap(Map<String, ? extends Serializable> row) {
		Set<String> columns = row.keySet();
		Map<String, String> newTypeMap = new HashMap<>();
		
		for(String columnName : columns) {
			Serializable value = row.get(columnName);
			Class<?> clazz = value.getClass();
			String name = clazz.getName();
			newTypeMap.put(columnName, name);
		}
		
		return newTypeMap;
	}
}
