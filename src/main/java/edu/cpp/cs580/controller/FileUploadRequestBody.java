package edu.cpp.cs580.controller;

import java.util.Map;

public class FileUploadRequestBody {
	
	private String customerId;
	private String name;
	private Boolean containsHeaderRow;
	private String[] header;
	
	public FileUploadRequestBody() {}
	
	public String getCustomerId() {
		return customerId;
	}
	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Boolean getContainsHeaderRow() {
		return containsHeaderRow;
	}
	public void setContainsHeaderRow(Boolean containsHeaderRow) {
		this.containsHeaderRow = containsHeaderRow;
	}
	public String[] getHeader() {
		return header;
	}
	public void setHeader(String[] header) {
		this.header = header;
	}

}
