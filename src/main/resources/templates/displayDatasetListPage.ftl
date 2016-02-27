<html>

<head>
    <title>${title}</title>
	<script src="//code.jquery.com/jquery-1.11.0.min.js"></script>
	<script src="/js/chart-form-control.js"></script>
	<script src="/js/pageControl.js"></script>
    <link rel="stylesheet" type="text/css" href="/css/style.css">
</head>

<body>
	<script>
		var customerId = "${customerID}";
	</script>
	<button onclick="goToHomepage()" class="button">Homepage</button>
	<div>Customer ID is: ${customerID} </div>

	<div>
		<table>
	<#list customerDataset as dataset>
		<tr>
			<td>ChartName:</td>
			<td><button onclick="goToChartListPage('${dataset.id}')">
				${dataset.name}</button></td>
			<td>
				<button onclick="deleteDataset('${dataset.id}')">Delete</button>
			</td>
			<td>
				<button onclick="goToChartFormPage()">
					<script>
						var chartId = "${dataset.id}";
					</script>
					New Chart
				</button>
			</td>
		</tr>
	</#list>
	</div>
</body>
</html>