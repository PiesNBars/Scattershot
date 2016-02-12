/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.cpp.cs580.controller;

import edu.cpp.cs580.customer.Customer;
import edu.cpp.cs580.customer.CustomerRepository;
import edu.cpp.cs580.customer.data.CustomerDataset;
import edu.cpp.cs580.customer.data.DatasetRepository;
import java.util.List;
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

	@RequestMapping(value = "/cs580/main", method = RequestMethod.GET)
	ModelAndView getLoginPage(Model m) {
		ModelAndView modelAndView = new ModelAndView("login");

		modelAndView.addObject("title", "login page title");

		m.addAttribute("customer", new Customer());

		return modelAndView;
	}

	@RequestMapping(value = "/cs580/login", method = RequestMethod.POST)
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
				ModelAndView modelAndView = new ModelAndView("userHomepage");

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

	@RequestMapping(value = "/cs580/register", method = RequestMethod.POST)
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

	@RequestMapping(value = "/cs580/upload", method = RequestMethod.GET)
	ModelAndView getUploadPage(Model m) {
		ModelAndView modelAndView = new ModelAndView("uploadPage");

		modelAndView.addObject("title", "upload page title");

		return modelAndView;
	}

	@RequestMapping(value = "/{customerId}/displayChartsList", method = RequestMethod.GET)
	ModelAndView displayChartsList(
		@PathVariable("customerId") String customerID) {

		ModelAndView modelAndView = new ModelAndView("displayChartsListPage");

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

	@RequestMapping(value = "/{customerID}/{chartID}/displayChartForm", method = RequestMethod.GET)
	ModelAndView displayChart(
		@PathVariable("chartID") String chartID,
		@PathVariable("customerID") String customerID) {

		ModelAndView modelAndView = new ModelAndView("displayChartFormPage");

		modelAndView.addObject("title", "Chart Display Page");
		modelAndView.addObject("chartID", chartID);
		modelAndView.addObject("customerID", customerID);

		return modelAndView;
	}
}