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
			<td><button onclick="goToChartFormPage()">
				<script>
					var chartID = "${dataset.id}";
				</script>
				${dataset.name}</button></td>
		</tr>
	</#list>
	</div>
</body>
</html>