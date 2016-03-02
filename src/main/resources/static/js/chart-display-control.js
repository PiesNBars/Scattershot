function deleteChart(id) {
	window.location.replace("/delete/chart/" + id);
}

function newChart(customerId, datasetId) {
	window.location.replace("/" + customerId + "/" + datasetId + "/displayChartForm");
}