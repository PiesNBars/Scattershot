package edu.cpp.cs580.controller;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Stack;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;

import edu.cpp.cs580.customer.CustomerRepository;
import edu.cpp.cs580.customer.data.CustomerDataset;
import edu.cpp.cs580.customer.data.DatasetRepository;
import edu.cpp.cs580.util.CSVMapper;
import edu.cpp.cs580.util.Reduceable;

@RestController
public class DatasetController {
	
	@Autowired private DatasetRepository datasetRepository;
	@Autowired private CustomerRepository customerRepository;
	@Autowired private ObjectMapper objectMapper;
	
	@RequestMapping(value="/upload", method=RequestMethod.GET)
	public @ResponseBody String getDataset(@RequestParam("id") String customerId,
			@RequestParam("name") String datasetName,
			@RequestParam("columns") String columns,
			@RequestParam(required=false, value="bins") Integer bins,
			@RequestParam(value="aggregate") String aggregate) throws Exception {
		
		boolean agg = aggregate.compareTo("true") == 0 ? true : false;
		
		if(customerId.isEmpty())
			return "You didn't provide a customer Id!";
		if(datasetName.isEmpty())
			return "You didn't provide a dataset name!";

		String[] variables = columns.split(",");
		CustomerDataset dataset = datasetRepository
				.findByCustomerIdAndName(customerId, datasetName);
		CustomerDataset resultset = dataset.getColumns(variables);
		
		if(agg && bins != null) {
			ToHistogram toHistogram = new ToHistogram(variables[0], bins);
			resultset = dataset.reduce(toHistogram);
		}
		
		return objectMapper.writeValueAsString(resultset);
	}

    @RequestMapping(value="/upload", method=RequestMethod.POST)
    public @ResponseBody String handleFileUpload(@RequestParam("id") String customerId,
    		@RequestParam("name") String name,
    		@RequestParam("file") MultipartFile file,
    		@RequestParam(required=false, value="header") String header,
    		@RequestParam("hasOwnHeaders") String hasHeaderRow){
    	
    	String[] columnNames = null;
    	boolean hasHeader = hasHeaderRow.compareTo("true") == 0 ? true : false;
    	
    	if(header != null && header.length() > 0)
    		columnNames = header.split(",");
    	if(customerId == null || customerId.equals(""))
    		return "You didn't provide a customer id! This dataset can't be saved!";
    	if(name == null || name.isEmpty())
    		return "You didn't provide a name! This dataset can't be saved!";
    	if(customerRepository.findById(customerId) == null)
    		return "That customer doesn't exist! This dataset can't be saved!";
    	
        if (!file.isEmpty()) {
            try {
            	CustomerDataset data = (columnNames != null) ?
            			CSVMapper.mapCSV(file, columnNames) :
            			CSVMapper.mapCSV(file, hasHeader);
            	
            	data.setCustomerId(customerId);
            	data.setName(name);

            	datasetRepository.save(data);
            	       
                return "You successfully uploaded " + name + "!";
            } catch (Exception e) {
                e.printStackTrace();
                return "You failed to upload " + name + " => " + e.getMessage();
            }
        } else {
            return "You failed to upload " + name + " because the file was empty.";
        }
    }
    
    public class ToHistogram implements Reduceable<CustomerDataset, CustomerDataset> {
    	
    	private int bins;
    	private String column;
    	private CustomerDataset rawData;
    	
    	public ToHistogram(String column, int bins) {
    		this.bins = bins;
    		this.column = column;
    	}
    	
    	public ToHistogram withArgument(CustomerDataset rawData) {
    		this.rawData = rawData;
    		return this;
    	}
    	
    	public CustomerDataset call() {
    		List<Map<String, ? extends Serializable>> data = rawData.getDataset();
    		List<Map<String, ? extends Serializable>> aggregateData = new ArrayList<>();
    		Map<String, Serializable> finalRow = new HashMap<>();
    		PriorityQueue<Number> sortedValues = new PriorityQueue<>();
    		Number max = null;
    		Double prevBound = null;
    		Double nextBound = null;
    		Stack<Double> boundries = null;
    		int currentCount = 0;
    		
    		for(Map<String, ? extends Serializable> row : data) {
    			try {
    				Number value = Number.class.cast(row.get(column));
    				max = max == null ? value : Math.max(max.doubleValue(), value.doubleValue());
    				sortedValues.add(value);
    			} catch(ClassCastException cce) {}
    		}
    		
    		boundries = getBinBoundries(sortedValues.peek().doubleValue(), max.doubleValue());
    		
    		while(!boundries.empty()) {
    			if(sortedValues.peek().doubleValue() >= nextBound) {
    				Map<String, Serializable> newRow = new HashMap<>();
    				
    				nextBound = boundries.pop();
    				newRow.put("key", "[" + prevBound + "," + nextBound + ")");
    				newRow.put("value", currentCount);
    				currentCount = 0;
    				prevBound = nextBound;
    				aggregateData.add(newRow);
    			} else {
    				currentCount++;
    				sortedValues.poll();
    			}
    		}
    		
    		finalRow.put("key", "[" + prevBound + "," + max + "]");
    		finalRow.put("value", sortedValues.size());
    		
    		return new CustomerDataset(aggregateData);
    	}
    	
    	private Stack<Double> getBinBoundries(double min, double max) {
    		Stack<Double> boundries = new Stack<Double>();
    		Double delta = (max - min) / bins;
    		Double nextBound = max;
    		
    		for(int i = 0; i < bins - 1; i++) {
    			nextBound -= delta;
    			boundries.push(nextBound) ;
    		}
    		
    		return boundries;
    	}
    }

}
