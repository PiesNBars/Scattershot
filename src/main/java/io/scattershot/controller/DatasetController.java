package io.scattershot.controller;


import java.io.Serializable;
import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.Stack;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.scattershot.customer.CustomerRepository;
import io.scattershot.customer.data.ChartSpec;
import io.scattershot.customer.data.ChartSpec.ChartType;
import io.scattershot.customer.data.ChartSpecRepository;
import io.scattershot.customer.data.CustomerDataset;
import io.scattershot.customer.data.DatasetRepository;
import io.scattershot.exception.AttributeNotFoundException;
import io.scattershot.exception.ChartNotFoundException;
import io.scattershot.exception.DatasetNotFoundException;
import io.scattershot.exception.FormProcessingException;
import io.scattershot.util.CSVMapper;
import io.scattershot.util.Mappable;
import io.scattershot.util.Reduceable;

@Controller
public class DatasetController {
	
	private static final String DEFAULT_WIDTH = "1020";
	private static final String DEFAULT_HEIGHT = "550";
	private static final String EMBED_TEMPLATE = "chart";
	private static final String DISPLAY_TEMPLATE = "chartDisplay";

	@Autowired private DatasetRepository datasetRepository;
	@Autowired private CustomerRepository customerRepository;
	@Autowired private ChartSpecRepository chartSpecRepository;
	@Autowired private ObjectMapper objectMapper;

	@RequestMapping(value="/chart/add/{datasetId}", method=RequestMethod.GET)
	public ModelAndView getDataset(@PathVariable String datasetId,
			@RequestParam("chartType") String chartType,
			@RequestParam(value="x", required=false) String x,
			@RequestParam("y") String y,
			@RequestParam(required=false, value="colors") String colors,
			@RequestParam(required=false, value="bins") Integer bins,
			@RequestParam("name") String name)
			throws Exception {

		CustomerDataset resultset = null;
		CustomerDataset dataset = datasetRepository.findOne(datasetId);
		String[] columns = {x, y};
		Set<String> columnsDifference = doesNotContain(dataset, columns);
		ChartSpec chart = null;

		if(!columnsDifference.isEmpty()) {
			throw new AttributeNotFoundException();
		}

		if(chartType.compareTo("histogram") == 0 && bins != null && bins > 0) {
			resultset = getHistogramData(dataset, y, bins);
			chart = new ChartSpec(ChartType.HISTOGRAM);
			chart.setBins(bins);
		} else if (chartType.compareTo("bar") == 0) {
			resultset = getBarChartData(dataset, y);
			chart = new ChartSpec(ChartType.BAR);
		} else if (chartType.compareTo("line") == 0 && columns.length == 2){
			resultset = getLineData(dataset, columns);
			chart = new ChartSpec(ChartType.LINE);
		}
		
		if(resultset == null) {
			throw new FormProcessingException();
		}
		
		String json = objectMapper.writeValueAsString(resultset.getDataset());
		ModelAndView chartPage = new ModelAndView("chartDisplay");
		String xType = resultset.getTypeMap().get("x");
		String yType = resultset.getTypeMap().get("y");

		chart.setColumns(columns);
		chart.setDatasetId(datasetId);
		chart.setName(name);

		// sets id field.
		chartSpecRepository.save(chart);

		chartPage.addObject("xType", xType);
		chartPage.addObject("yType", yType);
		chartPage.addObject("dataset", json);
		chartPage.addObject("width", 1020);
		chartPage.addObject("height", 550);
		chartPage.addObject("chartType", chartType);
		chartPage.addObject("chartID", chart.getId());

		return chartPage;
	}
	
	@RequestMapping(value="/chart/display/{chartSpecId}", method=RequestMethod.GET)
	public ModelAndView showChart(@PathVariable String chartSpecId) 
			throws JsonProcessingException {
		Integer width = Integer.parseInt(DEFAULT_WIDTH);
		Integer height = Integer.parseInt(DEFAULT_HEIGHT);
		ModelAndView chartDisplay = getChartView(chartSpecId, width, height,
				DISPLAY_TEMPLATE);
		
		if(chartDisplay == null)
			return null;
		
		chartDisplay.addObject("chartID", chartSpecId);
		
		return chartDisplay;
	}

	@RequestMapping(value="/chart/show/{chartSpecId}", method=RequestMethod.GET)
	public ModelAndView showDataset(@PathVariable String chartSpecId,
			@RequestParam(value="width", defaultValue=DEFAULT_WIDTH) Integer width,
			@RequestParam(value="height", defaultValue=DEFAULT_HEIGHT) Integer height)
			throws Exception{

		ModelAndView chartView = getChartView(chartSpecId, width, height, EMBED_TEMPLATE);
		
		if(chartView == null) {
			throw new ChartNotFoundException();
		}
		
		return chartView;
	}
	
	@RequestMapping(value="/delete/dataset/{datasetId}")
	public String deleteDataset(@PathVariable String datasetId) {
		List<CustomerDataset> data = datasetRepository.deleteById(datasetId);
		
		if(data.size() < 1 || data.get(0).getCustomerId() == null)
			throw new DatasetNotFoundException();
		
		chartSpecRepository.deleteAllByDatasetId(datasetId);
		String customerId = data.get(0).getCustomerId();
		
		return "redirect:/" + customerId + "/displayDatasetList";
	}
	
	@RequestMapping(value="/delete/chart/{chartId}")
	public String deleteChart(@PathVariable String chartId) {
		List<ChartSpec> charts = chartSpecRepository.deleteById(chartId);
		
		if(charts.size() < 1)
			throw new ChartNotFoundException();
		
		String datasetId = charts.get(0).getDatasetId();
		
		return "redirect:/" + datasetId + "/displayCharts";
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
    
    @ExceptionHandler(Exception.class)
    public ModelAndView ExceptionHandler(HttpServletRequest req, Exception ex) {
    	ModelAndView exceptionPage = new ModelAndView("error");
    	
    	exceptionPage.addObject("errorMessage", ex.getMessage());
    	
    	return exceptionPage;
    }
	
	private ModelAndView getChartView(String chartSpecId, int width, int height,
			String viewTemplate)
			throws JsonProcessingException {
		ChartSpec spec = chartSpecRepository.findOne(chartSpecId);
		CustomerDataset dataset = null;
		CustomerDataset resultset = null;
		String chartType = null;
		
		if(spec == null)
			throw new ChartNotFoundException();
		
		dataset = datasetRepository.findOne(spec.getDatasetId());

		switch(spec.getChartType()) {
			case HISTOGRAM: resultset = getHistogramData(dataset, spec.getColumns()[1], spec.getBins());
							chartType = "histogram";
							break;
			case BAR: 		resultset = getBarChartData(dataset, spec.getColumns()[1]);
							chartType = "bar";
							break;
			case LINE: 		resultset = getLineData(dataset, spec.getColumns());
							chartType = "line";
		}

		if(resultset != null) {
			ModelAndView chartPage = new ModelAndView(viewTemplate);
			String jsonData = objectMapper.writeValueAsString(resultset.getDataset());
			String xType = resultset.getTypeMap().get("x") == null ? resultset.getTypeMap().get("x") :
															 "none";
			String yType = resultset.getTypeMap().get("y");

			chartPage.addObject("xType", xType);
			chartPage.addObject("yType", yType);
			chartPage.addObject("dataset", jsonData);
			chartPage.addObject("chartType", chartType);
			chartPage.addObject("width", width);
			chartPage.addObject("height", height);

			return chartPage;
		}
		
		return null;
	}

	private CustomerDataset getHistogramData(CustomerDataset data, String column,
			Integer bins) {
		Map<String, String> typeMap = data.getTypeMap();
		try {
			Class<?> type = Class.forName(typeMap.get(column));

			if(bins == null || bins < 2 || !Number.class.isAssignableFrom(type))
				return null;
			
		} catch(ClassNotFoundException cnfe) {
			return null;
		}

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

		ToLineChart toLineChart = new ToLineChart(columns[0], columns[1]);
		return new CustomerDataset(data.map(toLineChart));
	}

	private Set<String> doesNotContain(CustomerDataset dataset, String[] variables) {
		Set<String> tableColumns = dataset.getTypeMap().keySet();
		Set<String> setDifference = new HashSet<>();

		for(int i = 0; i < variables.length; i++){
			String col = variables[i];
			if(col != null && !tableColumns.contains(col))
				setDifference.add(col);
		}

		return setDifference;
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
    		String typeName = rawData.getTypeMap().get(column);
    		Boolean isString = typeName.compareTo(String.class.getName()) == 0;
    		Map<String, Integer> barCounts = new HashMap<>();
    		String nextValue = null;
    		Integer currentCount = null;

    		for(Map<String, ? extends Serializable> row : data) {
    			nextValue = isString ? String.class.cast(row.get(column)) :
    								   row.get(column).toString();
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

    	private static final int SIGNIFICANT_FIGURES = 4;
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
    		String readableUpperBound = null;
    		String readableLowerBound = null; 		
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
    			readableUpperBound = getReadableValue(nextBound);
    			readableLowerBound = prevBound.isInfinite() ? prevBound.toString() : getReadableValue(prevBound);

    			while(sortedValues.peek().doubleValue() < nextBound) {
    				sortedValues.poll();
    				currentCount++;
    			}

    			nextRow.put("key", "[" + readableLowerBound + "," + readableUpperBound + ")");
    			nextRow.put("value", currentCount);
    			aggregateData.add(nextRow);
    			prevBound = nextBound;
    		}

    		finalRow.put("key", "[" + getReadableValue(prevBound) + "," + max + "]");
    		finalRow.put("value", sortedValues.size());
    		aggregateData.add(finalRow);

    		return new CustomerDataset(aggregateData);
    	}
    	
    	private String getReadableValue(double rawNumber) {
    		return roundToNSignificantFigures(rawNumber, SIGNIFICANT_FIGURES).toString();
    	}
    	
    	private Double roundToNSignificantFigures(double rawNumber, int significantFigures) {
    		if (significantFigures < 1)
    			return null;
    		
    		BigDecimal bd = new BigDecimal(rawNumber);
    		bd = bd.round(new MathContext(significantFigures));
    		double rounded = bd.doubleValue();
    		
    		return rounded;
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

    public class ToLineChart implements Mappable<Map<String, ? extends Serializable>, Map<String, ? extends Serializable>>{

    	private String x;
    	private String y;
    	private Map<String, ? extends Serializable> arg;

    	public ToLineChart(String x, String y) {
    		this.x = x;
    		this.y = y;
    	}

    	public ToLineChart withArgument(Map<String, ? extends Serializable> arg) {
    		this.arg = arg;
    		return this;
    	}

    	public Map<String, ? extends Serializable> call() {
    		Map<String, Serializable> nextPair = new HashMap<>();

    		nextPair.put("x", arg.get(x));
    		nextPair.put("y", arg.get(y));

    		return nextPair;
    	}
    }

}