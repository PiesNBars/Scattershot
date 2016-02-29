/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

function goToDisplayPage() {
	window.location.replace("/" + customerId +"/displayDatasetList");
}

function goToChartListPage(id) {
	window.location.replace("/" + id + "/displayCharts");
}

function goToChartFormPage() {
	window.location.replace("/" + customerId + "/" + chartId + "/displayChartForm");
}

function goToUploadPage() {
	window.location.replace("/" + customerId +"/upload");
}

function goToHomepage() {
	window.location.replace("/" + customerId +"/homepage");
}

function deleteDataset(id) {
	window.location.replace("/delete/dataset" + id);
}