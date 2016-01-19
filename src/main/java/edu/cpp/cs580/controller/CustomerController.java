/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.cpp.cs580.controller;

import edu.cpp.cs580.customer.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author tom
 */

@RestController
public class CustomerController {

	@Autowired
	private CustomerRepository customerRepository;

	@RequestMapping(value = "/cs580/login", method = RequestMethod.GET)
	ModelAndView getLoginPage() {
		ModelAndView modelAndView = new ModelAndView("login");
		modelAndView.addObject("title", "login page title");
		return modelAndView;
	}
}
