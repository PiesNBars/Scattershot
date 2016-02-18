package io.scattershot.customer;

import org.springframework.data.annotation.Id;

public class Customer {

	@Id
	private String id;
	private String firstName;
	private String lastName;
	private String email;
	private String password;

	public Customer() {}

	public Customer(Customer customer) {
		this.id = customer.id;
		this.email = customer.email;
		this.password = customer.password;
		this.firstName = customer.firstName;
		this.lastName = customer.lastName;
	}

	public Customer(String email, String password) {
		this();
		this.email = email;
		this.password = password;
	}

	public Customer(String email, String password, String firstName,
			String lastName) {
		this(email, password);
		this.firstName = firstName;
		this.lastName = lastName;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public String toString() {
		return String.format("Customer(%s, %s, %s, %s)", id, email, firstName,
				lastName);
	}

}