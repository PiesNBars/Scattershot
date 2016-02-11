package edu.cpp.cs580.controller;


import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Stack;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import edu.cpp.cs580.customer.CustomerRepository;
import edu.cpp.cs580.customer.data.ChartSpec;
import edu.cpp.cs580.customer.data.ChartSpecRepository;
import edu.cpp.cs580.customer.data.CustomerDataset;
import edu.cpp.cs580.customer.data.DatasetRepository;
import edu.cpp.cs580.util.CSVMapper;
import edu.cpp.cs580.util.Reduceable;

@Controller
public class DatasetController {
	
	@Autowired private DatasetRepository datasetRepository;
	@Autowired private ChartSpecRepository chartSpecRepository;
	@Autowired private CustomerRepository customerRepository;
	@Autowired private ObjectMapper objectMapper;
	
	@RequestMapping(value="chart/add", method=RequestMethod.POST)
	public ModelAndView newChart(@RequestBody String json) {
		
		try {
			ModelAndView ret = new ModelAndView("chart");
			ObjectNode node = objectMapper.readValue(json, ObjectNode.class);
			ChartSpec chart = new ChartSpec();
			NewChartRequest request = objectMapper.convertValue(node, NewChartRequest.class);
			ChartSpec.ChartType chartType = toEnum(request.getChartType());
			String datasetName = request.getDatasetName();
			String customerId = request.getCustomerId();
			Integer bins = request.getBins();
			String[] columns = request.getColumns();
			CustomerDataset dataset = datasetRepository
					.findByCustomerIdAndName(customerId, datasetName);
			
			chart.setChartType(chartType);
			chart.setDatasetId(dataset.getId());
			chart.setColumns(columns);
			chart.setOptions(request.getOptions());
			chart.setBins(bins);
			
			if(chartType == ChartSpec.ChartType.HISTOGRAM && bins != null && columns.length > 0) {
				ret.addObject("dataset", histogram(dataset, bins, columns[0]));
			} else if(chartType == ChartSpec.ChartType.BAR && columns.length > 0) {
				ret.addObject("dataset", barChart(dataset, columns[0]));
			} else if(chartType == ChartSpec.ChartType.LINE && columns.length > 2) {
				ret.addObject("dataset", dataset.getColumns(columns[0], columns[1]));
			}
			
			if(ret.getModel().isEmpty())
				return new ModelAndView("error");
			
			chartSpecRepository.save(chart);
			return ret;
			
		} catch(JsonMappingException jme) {
			return new ModelAndView("error");
		} catch(JsonParseException jpe) {
			jpe.printStackTrace();
			return new ModelAndView("error");
		} catch(IOException ioe) {
			return new ModelAndView("error");
		}	
	}
	
	private CustomerDataset histogram(CustomerDataset data, Integer bins, String feature) {
		ToHistogram toHistogram = new ToHistogram(feature, bins);
		return data.reduce(toHistogram);
	}
	
	private CustomerDataset barChart(CustomerDataset data, String feature) {
		ToBarChart toBarChart = new ToBarChart(feature);
		return data.reduce(toBarChart);
	}
	
	@RequestMapping(value="/upload", method=RequestMethod.GET)
	public ModelAndView getDataset(@RequestParam("id") String customerId,
			@RequestParam("name") String datasetName,
			@RequestParam("columns") String columns,
			@RequestParam(required=false, value="bins") Integer bins,
			@RequestParam(value="aggregate") String aggregate) throws Exception {
		
		if(customerId.isEmpty())
			return new ModelAndView("error");
		if(datasetName.isEmpty())
			return new ModelAndView("error");

		Boolean agg = aggregate.compareTo("true") == 0 ? true : false;
		String[] variables = columns.split(",");
		CustomerDataset resultset = null;
		CustomerDataset dataset = datasetRepository
				.findByCustomerIdAndName(customerId, datasetName);
		
		if(agg && bins != null) {
			ToHistogram toHistogram = new ToHistogram(variables[0], bins);
			resultset = dataset.reduce(toHistogram);
		} else if (agg) {
			ToBarChart toBarChart = new ToBarChart(variables[0]);
			resultset = dataset.reduce(toBarChart);
		} else {
			resultset = dataset.getColumns(variables);
		}
		
		String json = objectMapper.writeValueAsString(resultset.getDataset());
		ModelAndView chartPage = new ModelAndView("chart");
		chartPage.addObject("dataset", json);
		
		return chartPage;
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
    
    private ChartSpec.ChartType toEnum(String chartType) {
		
		if(chartType == "histogram")
			return ChartSpec.ChartType.HISTOGRAM;
		if(chartType == "bar")
			return ChartSpec.ChartType.BAR;
		return ChartSpec.ChartType.LINE;
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
	
	@JsonIgnoreProperties(ignoreUnknown=true)
	public class NewChartRequest {
		
		private String customerId;
		private String datasetName;
		private String chartType;
		private String[] columns;
		private Integer bins;
		private String options;
		
		public String getCustomerId() {
			return customerId;
		}
		public void setCustomerId(String customerId) {
			this.customerId = customerId;
		}
		public String getDatasetName() {
			return datasetName;
		}
		public void setDatasetName(String datasetName) {
			this.datasetName = datasetName;
		}
		public String getChartType() {
			return chartType;
		}
		public void setChartType(String chartType) {
			this.chartType = chartType;
		}
		public String[] getColumns() {
			return columns;
		}
		public void setColumns(String[] columns) {
			this.columns = columns;
		}
		public Integer getBins() {
			return bins;
		}
		public void setBins(Integer bins) {
			this.bins = bins;
		}
		public String getOptions() {
			return options;
		}
		public void setOptions(String options) {
			this.options = options;
		}
	}

}
