package edu.cpp.cs580.customer.data;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface ChartSpecRepository extends MongoRepository<ChartSpec, String> {
	public List<ChartSpec> findAllByDatasetId(String datasetId);
}
