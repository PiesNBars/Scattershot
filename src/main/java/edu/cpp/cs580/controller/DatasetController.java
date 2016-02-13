package edu.cpp.cs580.controller;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.Stack;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.databind.ObjectMapper;

import edu.cpp.cs580.customer.CustomerRepository;
import edu.cpp.cs580.customer.data.ChartSpec;
import edu.cpp.cs580.customer.data.ChartSpec.ChartType;
import edu.cpp.cs580.customer.data.ChartSpecRepository;
import edu.cpp.cs580.customer.data.CustomerDataset;
import edu.cpp.cs580.customer.data.DatasetRepository;
import edu.cpp.cs580.util.CSVMapper;
import edu.cpp.cs580.util.Reduceable;

@Controller
public class DatasetController {
	
	@Autowired private DatasetRepository datasetRepository;
	@Autowired private CustomerRepository customerRepository;
	@Autowired private ChartSpecRepository chartSpecRepository;
	@Autowired private ObjectMapper objectMapper;
	
	@RequestMapping(value="/chart/add/{datasetId}", method=RequestMethod.GET)
	public ModelAndView getDataset(@PathVariable String datasetId,
			@RequestParam("chartType") String chartType,
			@RequestParam("columns") String columns,
			@RequestParam(required=false, value="colors") String colors,
			@RequestParam(required=false, value="bins") Integer bins)
			throws Exception {

		String[] variables = columns.split(",");
		CustomerDataset resultset = null;
		CustomerDataset dataset = datasetRepository.findOne(datasetId);
		Set<String> columnsDifference = doesNotContain(dataset, variables);
		ChartSpec chart = null;
		
		if(!columnsDifference.isEmpty()) {
			return new ModelAndView("error");
		}
		
		if(chartType.compareTo("histogram") == 0) {
			resultset = getHistogramData(dataset, variables[0], bins);
			chart = new ChartSpec(ChartType.HISTOGRAM);
			chart.setBins(bins);
		} else if (chartType.compareTo("bar") == 0) {
			resultset = getBarChartData(dataset, variables[0]);
			chart = new ChartSpec(ChartType.BAR);
		} else if (chartType.compareTo("line") == 0){
			resultset = getLineData(dataset, variables);
			chart = new ChartSpec(ChartType.LINE);
		}
		
		if(resultset != null) {
			String json = objectMapper.writeValueAsString(resultset.getDataset());
			ModelAndView chartPage = new ModelAndView("chart");
			
			chart.setColumns(variables);
			chart.setDatasetId(datasetId);
			
			chartSpecRepository.save(chart);
			
			chartPage.addObject("dataset", json);
			chartPage.addObject("chartType", chartType);
			
			return chartPage;
		}
		
		return new ModelAndView("error");
	}
	
	private CustomerDataset getHistogramData(CustomerDataset data, String column,
			Integer bins) {
		Map<String, String> typeMap = data.getTypeMap();
		
		if(bins == null || bins < 2 || typeMap.get(column) != Integer.class.getName())
			return null;
		
		ToHistogram toHistogram = new ToHistogram(column, bins);
		return data.reduce(toHistogram);
	}
	
	private CustomerDataset getBarChartData(CustomerDataset data, String column) {
		
		ToBarChart toBarChart = new ToBarChart(column);
		return data.reduce(toBarChart);
	}
	
	private CustomerDataset getLineData(CustomerDataset data, String[] columns) {
		
		if(columns.length != 2 || data.getTypeMap().keySet().size() < 2)
			return null;
		
		return data.getColumns(columns);
	}
	
	private Set<String> doesNotContain(CustomerDataset dataset, String[] variables) {
		Set<String> tableColumns = dataset.getTypeMap().keySet();
		Set<String> setDifference = new HashSet<>();
		
		for(int i = 0; i < variables.length; i++)
			if(!tableColumns.contains(variables[i]))
				setDifference.add(variables[i]);
		
		return setDifference;
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
    
    public class ToBarChart implements Reduceable<CustomerDataset, CustomerDataset> {
    	
    	private String column;
    	private CustomerDataset rawData;
    	
    	public ToBarChart(String column) {
    		this.column = column;
    	}
    	
    	public ToBarChart withArgument(CustomerDataset rawData) {
    		this.rawData = rawData;
    		return this;
    	}
    	
    	public CustomerDataset call() {
    		
    		List<Map<String, ? extends Serializable>> data = rawData.getDataset();
    		Map<String, Integer> barCounts = new HashMap<>();
    		String nextValue = null;
    		Integer currentCount = null;
    		
    		for(Map<String, ? extends Serializable> row : data) {
    			nextValue = String.class.cast(row.get(column));
    			currentCount = barCounts.get(nextValue);
    			
    			if(currentCount == null) {
    				barCounts.put(nextValue, 1);
    			} else {
    				barCounts.put(nextValue, currentCount + 1);
    			}
    		}
    		
    		return listify(barCounts);
    	}
    	
    	private CustomerDataset listify(Map<String, Integer> barCounts) {
    		Map<String, Serializable> nextRow = new HashMap<>();
    		List<Map<String, ? extends Serializable>> aggData = new ArrayList<>();
    		
    		for(String key : barCounts.keySet()) {
    			nextRow = new HashMap<>();
    			nextRow.put("key", key);
    			nextRow.put("value", barCounts.get(key));
    			
    			aggData.add(nextRow);
    		}
    		
    		return new CustomerDataset(aggData);
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
    		Double prevBound = Double.NEGATIVE_INFINITY ;
    		Double nextBound = null;
    		Stack<Double> boundries = null;
			Integer currentCount = null;
    		
    		for(Map<String, ? extends Serializable> row : data) {
    			try {
    				Number value = Number.class.cast(row.get(column));
    				max = max == null ? value : Math.max(max.doubleValue(), value.doubleValue());
    				sortedValues.add(value);
    			} catch(ClassCastException cce) {}
    		}
    		
    		boundries = getBinBoundries(sortedValues.peek().doubleValue(), max.doubleValue());
    		
    		while(!boundries.empty()) {
    			Map<String, Serializable> nextRow = new HashMap<>();
    			currentCount = 0;
    			nextBound = boundries.pop();
    			
    			while(sortedValues.peek().doubleValue() < nextBound) {
    				sortedValues.poll();
    				currentCount++;
    			}
    			
    			nextRow.put("key", "[" + prevBound + "," + currentCount + ")");
    			nextRow.put("value", currentCount);
    			aggregateData.add(nextRow);
    			prevBound = nextBound;
    		}
    		
    		finalRow.put("key", "[" + prevBound + "," + max + "]");
    		finalRow.put("value", sortedValues.size());
    		aggregateData.add(finalRow);
    		
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