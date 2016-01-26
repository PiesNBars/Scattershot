/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.cpp.cs580.controller;

import edu.cpp.cs580.customer.Customer;
import edu.cpp.cs580.customer.CustomerRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
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

	@RequestMapping(value = "/cs580/login", method = RequestMethod.GET)
	ModelAndView getLoginPage(Model m) {
		ModelAndView modelAndView = new ModelAndView("login");
		modelAndView.addObject("title", "login page title");
		m.addAttribute("customer", new Customer());
		return modelAndView;
	}

	@RequestMapping(value = "/cs580/login", method = RequestMethod.POST)
	ModelAndView showExistPage(@ModelAttribute("customer") Customer customer, Model m) {
		List<Customer> customerList = customerRepository.findAll();

		System.out.println("spring customer email: " + customer.getEmail());
		System.out.println("spring customer password: " + customer.getPassword());

		for (Customer c : customerList) {
			String customerEmail = c.getEmail();

			if (customerEmail.equals(customer.getEmail())) {
				ModelAndView modelAndView = new ModelAndView("customerExist");
				return modelAndView;
			}
		}
		customerRepository.save(customer);
		ModelAndView modelAndView = new ModelAndView("userHomepage");
		modelAndView.addObject("title", "User Home Page");
		modelAndView.addObject("userFirstName", customer.getFirstName());
		return modelAndView;
	}
}
