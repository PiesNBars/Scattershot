package io.scattershot.customer.data;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface ChartSpecRepository extends MongoRepository<ChartSpec, String> {
	public List<ChartSpec> findAllByDatasetId(String datasetId);
	public Long deleteAllByDatasetId(String datasetId);
	public List<ChartSpec> deleteById(String id);
}
