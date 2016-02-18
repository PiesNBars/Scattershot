/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package io.scattershot.customer;

import io.scattershot.customer.Customer;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @author tom
 */
public class CustomerTest {
	@BeforeClass
	public static void setUpClass() {
		_customer = new Customer(
			"email", "password", "firstName", "lastName");
	}

	@Test
	public void testGetFirstName() {
		Assert.assertEquals("firstName", _customer.getFirstName());
	}

	@Test
	public void testGetLastName() {
		Assert.assertEquals("lastName", _customer.getLastName());
	}

	private static Customer _customer;
}
