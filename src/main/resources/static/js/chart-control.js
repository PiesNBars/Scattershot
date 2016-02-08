function test (data) {
	var obj = JSON.parse(data);
	$.scattershot.barChart.create(obj, 1020, 550, null);
}