package edu.cpp.cs580.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
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
import edu.cpp.cs580.customer.data.DatasetVector;
import edu.cpp.cs580.util.CSVMapper;

@RestController
public class DatasetController {
	
	@Autowired private DatasetRepository datasetRepository;
	@Autowired private CustomerRepository customerRepository;
	@Autowired private ObjectMapper objectMapper;
	
	@RequestMapping(value="/upload", method=RequestMethod.GET)
	public @ResponseBody String getDataset(@RequestParam("id") String customerId,
			@RequestParam("name") String datasetName,
			@RequestParam("columns") String columns) throws Exception {
		
		if(customerId.isEmpty())
			return "You didn't provide a customer Id!";
		if(datasetName.isEmpty())
			return "You didn't provide a dataset name!";
		
		DatasetVector.DataType type = DatasetVector.DataType.CATAGORY;
		String response = null;
		String[] variables = columns.split(",");
		List<DatasetVector<String>> resultset = new ArrayList<>();
		CustomerDataset dataset = datasetRepository
				.findByCustomerIdAndName(customerId, datasetName);
		String datasetId = dataset.getId();
		
		for(String columnName : variables){
			DatasetVector<String> column = new DatasetVector<>();
			column.setDatasetId(datasetId);
			column.setDataType(type);
			column.setValues(dataset.getColumn(columnName));
			resultset.add(column);
		}
		
		return objectMapper.writeValueAsString(resultset);
	}

    @RequestMapping(value="/upload", method=RequestMethod.POST)
    public @ResponseBody String handleFileUpload(@RequestParam("id") String customerId,
    		@RequestParam("name") String name,
    		@RequestParam("file") MultipartFile file,
    		@RequestParam("header") String header,
    		@RequestParam(required=false, value="hasHeaderRow") Boolean hasHeaderRow){
    	
    	String[] columnNames = null;
    	boolean hasHeader = (hasHeaderRow != null) ?true : false;
    	
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
            	CustomerDataset data = (columnNames == null) ?
            			CSVMapper.mapCSV(file, columnNames) :
            			CSVMapper.mapCSV(file, hasHeader);
            	
            	data.setCustomerId(customerId);
            	data.setName(name);

            	datasetRepository.save(data);
            	       
                return "You successfully uploaded " + name + "!";
            } catch (Exception e) {
                return "You failed to upload " + name + " => " + e.getMessage();
            }
        } else {
            return "You failed to upload " + name + " because the file was empty.";
        }
    }

}
