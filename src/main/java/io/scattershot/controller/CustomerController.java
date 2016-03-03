/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package io.scattershot.controller;

import io.scattershot.customer.Customer;
import io.scattershot.customer.CustomerRepository;
import io.scattershot.customer.data.ChartSpec;
import io.scattershot.customer.data.ChartSpecRepository;
import io.scattershot.customer.data.CustomerDataset;
import io.scattershot.customer.data.DatasetRepository;

import java.sql.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author tom
 */

@RestController
public class CustomerController {
	@Autowired private CustomerRepository customerRepository;
	@Autowired private DatasetRepository datasetRepository;
	@Autowired private ChartSpecRepository chartSpecRepository;

	@RequestMapping(value = "/{customerID}/{chartID}/displayChartForm", method = RequestMethod.GET)
	ModelAndView displayChart(
		@PathVariable("chartID") String chartID,
		@PathVariable("customerID") String customerID) {

		ModelAndView modelAndView = new ModelAndView("displayChartFormPage");
		Map<String, String> cols = datasetRepository
				.findTypeMapById(chartID)
				.getTypeMap();
		
		try {
			Map<String, String> typeNames = getSimpleTypeNames(cols); 

			modelAndView.addObject("title", "Chart Display Page");
			modelAndView.addObject("chartID", chartID);
			modelAndView.addObject("customerID", customerID);
			modelAndView.addObject("columns", typeNames);

		} catch (ClassNotFoundException cnfe) {
			modelAndView = new ModelAndView("error");
		}
		
		return modelAndView;
	}
	
	@RequestMapping(value = "/{datasetId}/displayCharts", method = RequestMethod.GET)
	ModelAndView displayCharts(@PathVariable("datasetId") String datasetId) {
		
		List<ChartSpec> charts = chartSpecRepository.findAllByDatasetId(datasetId);
		ModelAndView chartPage = new ModelAndView("displayChartListPage");
		String customerId = datasetRepository.findCustomerIdById(datasetId).getCustomerId();
		
		chartPage.addObject("charts", charts);
		chartPage.addObject("datasetId", datasetId);
		chartPage.addObject("customerId", customerId);
		
		return chartPage;
	}
	
	private Map<String, String> getSimpleTypeNames(Map<String, String> typeMap) 
			throws ClassNotFoundException {
		
		Map<String, String> typeNames = new HashMap<>();
		
		for (String key : typeMap.keySet()) {
			Class<?> type = Class.forName(typeMap.get(key));
			String typeName = null;
			
			if (Number.class.isAssignableFrom(type)) {
				typeName = "number";
			} else if (Date.class.equals(type)) {
				typeName = "Date-time";
			} else {
				typeName = "category";
			}
			
			typeNames.put(key, typeName);
		}
		
		return typeNames;
	}

	@RequestMapping(value = "/{customerID}/displayDatasetList", method = RequestMethod.GET)
	ModelAndView displayChartsList(
		@PathVariable("customerID") String customerID) {

		ModelAndView modelAndView = new ModelAndView("displayDatasetListPage");

		modelAndView.addObject("title", "Charts List Display Page");
		modelAndView.addObject("customerID", customerID);

		List<CustomerDataset> customerDataset =
				datasetRepository.findAllByCustomerId(customerID);

		System.out.println("Chart names are: ");
		for (CustomerDataset test : customerDataset) {
			System.out.println(test.getName());
		}

		modelAndView.addObject("customerDataset", customerDataset);
		return modelAndView;
	}

// Not Used, serves as example for RequestParam
	@RequestMapping(value = "/customer/checkExist", method = RequestMethod.POST)
	String existCheck(@RequestParam("email") String email) {
		List<Customer> customerList = customerRepository.findAll();

		for (Customer customer : customerList) {
			String customerEmail = customer.getEmail();

			if (customerEmail.equals(email)) {
				return customer.getId();
			}
		}
		return "false";
	}

	@RequestMapping(value = "/", method = RequestMethod.GET)
	ModelAndView getLoginPage(Model m) {
		ModelAndView modelAndView = new ModelAndView("login");

		modelAndView.addObject("title", "Scattershot");

		m.addAttribute("customer", new Customer());

		return modelAndView;
	}

	@RequestMapping(value = "/{customerID}/upload", method = RequestMethod.GET)
	ModelAndView getUploadPage(@PathVariable("customerID") String customerID) {
		ModelAndView modelAndView = new ModelAndView("uploadPage");

		modelAndView.addObject("title", "upload page title");
		modelAndView.addObject("customerID", customerID);

		return modelAndView;
	}

	@RequestMapping(value = "/{customerID}/homepage", method = RequestMethod.GET)
	ModelAndView getHomepage(@PathVariable("customerID") String customerID) {
		Customer c = customerRepository.findById(customerID);

		ModelAndView modelAndView = new ModelAndView("userHomepage");

		modelAndView.addObject("title", "User Home Page");
		modelAndView.addObject("userFirstName", c.getFirstName());
		modelAndView.addObject("customer", c);

		return modelAndView;
	}

	@RequestMapping(value = "/login", method = RequestMethod.POST)
	ModelAndView loginAction(@ModelAttribute("customer") Customer customer) {
		System.out.println("customer email: " + customer.getEmail());

		Customer c = customerRepository.findByEmail(customer.getEmail());

		if (c == null) {
			ModelAndView modelAndView = new ModelAndView("userNotFoundPage");

			modelAndView.addObject("title", "User Not Found Page");
			modelAndView.addObject("userEmail", customer.getEmail());

			return modelAndView;
		}
		else {
			if (c.getPassword().equals(customer.getPassword())) {
				ModelAndView modelAndView = new ModelAndView("redirect:/" + c.getId() + "/homepage");

				modelAndView.addObject("title", "User Home Page");
				modelAndView.addObject("userFirstName", c.getFirstName());
				modelAndView.addObject("customer", c);

				return modelAndView;
			}
			else {
				ModelAndView modelAndView = new ModelAndView(
					"forgotPasswordPage");

				modelAndView.addObject("title", "Forgot Password");
				modelAndView.addObject(
					"message",
					c.getEmail() + " and password does not match, please retry"
						+ " or recovery your password.");

				return modelAndView;
			}
		}
	}

	@RequestMapping(value = "/forgotPassword", method = RequestMethod.GET)
	ModelAndView loginAction() {
		ModelAndView modelAndView = new ModelAndView(
			"forgotPasswordPage");

		modelAndView.addObject("title", "Forgot Password");
		modelAndView.addObject(
			"message",
			"This is the forgot password page");

		return modelAndView;
	}

	@RequestMapping(value = "/register", method = RequestMethod.POST)
	ModelAndView registerAction(@ModelAttribute("customer") Customer customer) {
		Customer c = customerRepository.findByEmail(customer.getEmail());

		if (c != null) {
			ModelAndView modelAndView = new ModelAndView("customerExist");

			return modelAndView;
		}

		customerRepository.save(customer);

		ModelAndView modelAndView = new ModelAndView("userHomepage");

		modelAndView.addObject("title", "User Home Page");
		modelAndView.addObject("userFirstName", customer.getFirstName());

		return modelAndView;
	}
}