package io.scattershot.customer.data;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface DatasetRepository extends MongoRepository<CustomerDataset, String> {
	public CustomerDataset findByCustomerIdAndName(String customerId, String name);
	public List<CustomerDataset> findAllByCustomerId(String customerId);
	
	@Query(fields="{'customerId': 1 }")
	public CustomerDataset findCustomerIdById(String id);

	@Query(fields="{'_id' : 1}")
	public CustomerDataset findIdByCustomerIdAndName(String customerId, String name);
	
	@Query(fields="{'typeMap' : 1 }")
	public CustomerDataset findTypeMapById(String id);
	
	@Query(fields="{'customerId' : 1 }")
	public List<CustomerDataset> deleteById(String id);
}
