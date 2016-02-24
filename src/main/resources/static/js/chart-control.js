function test (data, chartType, xType, yType, width, height) {
	var obj = JSON.parse(data);
	if(chartType == "line") {
		$.scattershot.lineChart.create(obj, width, height, xType, yType)
	} else {
		$.scattershot.barChart.create(obj, width, height, null);	
	}
}