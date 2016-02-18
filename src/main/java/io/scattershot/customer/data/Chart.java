package io.scattershot.customer.data;

import java.util.List;

public class Chart {

	public enum ChartType {LINE, SCATTERPLOT, HISTOGRAM, BAR};

	private String chartId;
	private ChartType type;
	private List<String> colors;
	private String title;
	private boolean context;
	private boolean tooltip;
	private int bins;

	public Chart() {}

	public String getChartId() {
		return chartId;
	}
	public void setChartId(String chartId) {
		this.chartId = chartId;
	}
	public ChartType getType() {
		return type;
	}
	public void setType(ChartType type) {
		this.type = type;
	}
	public List<String> getColors() {
		return colors;
	}
	public void setColors(List<String> colors) {
		this.colors = colors;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public boolean isContext() {
		return context;
	}
	public void setContext(boolean context) {
		this.context = context;
	}
	public boolean isTooltip() {
		return tooltip;
	}
	public void setTooltip(boolean tooltip) {
		this.tooltip = tooltip;
	}
	public int getBins() {
		return bins;
	}
	public void setBins(int bins) {
		this.bins = bins;
	}

}
