package io.scattershot.customer.data;

import org.springframework.data.annotation.Id;

public class ChartSpec {

	public enum ChartType {HISTOGRAM, BAR, LINE}

	@Id
	private String id;
	String name;
	private String datasetId;
	private String[] columns;
	private ChartType chartType;
	private Integer bins;
	private String options;

	public ChartSpec() {}

	public ChartSpec(ChartType chartType) {
		this.chartType = chartType;
	}

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDatasetId() {
		return datasetId;
	}
	public void setDatasetId(String datasetId) {
		this.datasetId = datasetId;
	}
	public String[] getColumns() {
		return columns;
	}
	public void setColumns(String[] columns) {
		this.columns = columns;
	}
	public ChartType getChartType() {
		return chartType;
	}
	public void setChartType(ChartType chartType) {
		this.chartType = chartType;
	}
	public Integer getBins() {
		if(chartType != ChartType.HISTOGRAM)
			return null;
		return bins;
	}
	public void setBins(Integer bins) {
		this.bins = bins;
	}
	public String getOptions() {
		return options;
	}
	public void setOptions(String options) {
		this.options = options;
	}
}
