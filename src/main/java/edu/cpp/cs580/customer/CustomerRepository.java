package edu.cpp.cs580.customer;

import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;

public interface CustomerRepository extends MongoRepository<Customer, String> {
	public Customer findByFirstName(@Param("firstName") String firstName);
	public Customer findByLastName(@Param("lastName") String lastName);
	public Customer findById(@Param("id") String id);
	public List<Customer> findAllByFirstName(@Param("firstName") String firstName);
	public List<Customer> findAllByLastName(@Param("lastName") String lastName);

}
