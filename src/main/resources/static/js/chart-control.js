function test (data, chartType, xType, yType) {
	debugger;
	var obj = JSON.parse(data);
	debugger;
	if(chartType == "line") {
		$.scattershot.lineChart.create(obj, 1020, 550, xType, yType)
	} else {
		$.scattershot.barChart.create(obj, 1020, 550, null);	
	}
}