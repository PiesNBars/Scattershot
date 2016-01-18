package edu.cpp.cs580.customer;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface CustomerRepository extends MongoRepository<Customer, String> {
	public Customer findByFirstName(String firstName);
	public Customer findByLastName(String lastName);
	public Customer findById(String id);
	public List<Customer> findAllByFirstName(String firstName);
	public List<Customer> findAllByLastName(String lastName);
}
