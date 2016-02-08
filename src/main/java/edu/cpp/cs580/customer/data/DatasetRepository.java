package edu.cpp.cs580.customer.data;

import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface DatasetRepository extends MongoRepository<CustomerDataset, String> {
	public CustomerDataset findByCustomerIdAndName(String customerId, String name);
	public List<CustomerDataset> findAllByCustomerId(String customerId);
}
