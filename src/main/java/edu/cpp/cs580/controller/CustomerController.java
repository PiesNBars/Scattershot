/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.cpp.cs580.controller;

import edu.cpp.cs580.customer.Customer;
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

	@RequestMapping(value = "/cs580/customer/{customerId}", method = RequestMethod.POST)
	Customer updateCustomer(
			@PathVariable("customerId") String id,
			@RequestParam("email") String email,
			@RequestParam("password") String password) {

		Customer customer = new Customer();

		customer.setEmail(email);
		customer.setPassword(password);

		return customer;
	}

	@RequestMapping(value = "/cs580/login", method = RequestMethod.GET)
	ModelAndView getLoginPage() {
		ModelAndView modelAndView = new ModelAndView("login");
		modelAndView.addObject("title", "login page title");
		return modelAndView;
	}
}
