<html>

<head>
    <title>${title}</title>
	<script src="//code.jquery.com/jquery-1.11.0.min.js"></script>
	<script src="/js/chart-form-control.js"></script>
    <link rel="stylesheet" type="text/css" href="/css/style.css">
</head>

<body>
	<script>
		var customerId = "${customerID}";
		var chartId = "${chartID}";
	</script>

	<div>Chart ID is: ${chartID} </div>

   <div>Customer ID is: ${customerID} </div>
	<div>
		<form method="get" action="/chart/add/${chartID}">
			What type of Chart Is this?
			<input type="radio" name="chartType" value="line"/>Line Chart
			<input type="radio" name="chartType" value="bar"/>Bar Chart
			<input type="radio" name="chartType" value="histogram"/>Histogram<br/>
			Chart Name: <input type="text" name="name"/><br/>
			Bins (only necessary for histogram): <input type="number" name="bins"/><br/>
			Which column should be represented on the x-axis? <br/>
			<#list columns?keys as column>
				<input type="checkbox" name="x" value="${column}">${column} (${columns[column]})
			</#list><br/>
			Which column should be represented on the y-axis?<br/>
			<#list columns?keys as column>
				<input type="checkbox" name="y" value="${column}">${column} (${columns[column]})
			</#list><br/>
			<input type="submit" value="drawChart"/>
		</form>
	</div>
</body>
<script>
	function submitForm() {
		submit("${customerID}");
	};
</script>

</html>