package edu.cpp.cs580.customer.data;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface DatasetRepository extends MongoRepository<CustomerDataset, String> {
	public CustomerDataset findByCustomerIdAndName(String customerId, String name);
}
