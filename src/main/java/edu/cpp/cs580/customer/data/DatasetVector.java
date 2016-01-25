package edu.cpp.cs580.customer.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class DatasetVector <T extends Serializable> {
	
	public static enum DataType {DATE, NUMBER, CATAGORY};
	
	private DataType dataType;
	private String datasetId;
	private List<T> values;
	
	public DatasetVector() {
		values = new ArrayList<T>();
	}
	
	public DataType getDataType() {
		return dataType;
	}
	
	public void setDataType(DataType dataType) {
		this.dataType = dataType;
	}
	
	public String getDatasetId() {
		return datasetId;
	}
	
	public void setDatasetId(String datasetId) {
		this.datasetId = datasetId;
	}
	
	public List<T> getValues() {
		return values;
	}
	
	public void setValues(List<T> values) {
		this.values = values;
	}
	
	public void push(T value) {
		values.add(value);
	}
}
