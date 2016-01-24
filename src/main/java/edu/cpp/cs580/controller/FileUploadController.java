package edu.cpp.cs580.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import edu.cpp.cs580.customer.CustomerRepository;
import edu.cpp.cs580.customer.data.CustomerDataset;
import edu.cpp.cs580.customer.data.DatasetRepository;
import edu.cpp.cs580.util.CSVMapper;

@Controller
public class FileUploadController {
	
	@Autowired private DatasetRepository datasetRepository;
	@Autowired private CustomerRepository customerRepository;

    @RequestMapping(value="/upload", method=RequestMethod.POST)
    public @ResponseBody String handleFileUpload(@RequestParam("id") String customerId,
    		@RequestParam("name") String name,
    		@RequestParam("file") MultipartFile file){
    	
    	if(customerId == null || customerId.equals(""))
    		return "You didn't provide a customer id! This dataset can't be saved!";
    	if(name == null || name.equals(""))
    		return "You didn't provide a name! This dataset can't be saved!";
    	if(customerRepository.findById(customerId) == null)
    		return "That customer doesn't exist! This dataset can't be saved!";
    	
        if (!file.isEmpty()) {
            try {
            	CustomerDataset data = CSVMapper.mapCSV(file, false);
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
