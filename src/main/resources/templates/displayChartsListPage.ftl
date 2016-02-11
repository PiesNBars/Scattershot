<html>

<head>
    <title>${title}</title>
	<script src="//code.jquery.com/jquery-1.11.0.min.js"></script>
	<script src="/js/chart-form-control.js"></script>
    <link rel="stylesheet" type="text/css" href="/css/style.css">
</head>

<body>
	<div>Customer ID is: ${customerID} </div>
	<div>
		Is this data being aggregated?:
		<input type="radio" name="aggregate" value="true"/>Yes
		<input type="radio" name="aggregate" value="false" checked/>No<br/>
		What type of Chart Is this?
		<input type="radio" name="chartType" value="line"/>Line Chart
		<input type="radio" name="chartType" value="bar"/>Bar Chart
		<input type="radio" name="chartType" value="histogram"/>Histogram<br/>
		Bins: <input type="number" name="bins"/><br/>
		Column: <input type="text" name="columns"/><br/>
		Name: <input type="text" name="name"><br /> <br />
		<button onClick="submitForm()">Generate Chart</button>
	</div>
	<div>
		<table>
	<#list customerDataset as database>
		<tr>
			<td>ChartName:</td>
			<td>${database.name}</td>
		</tr>
	</#list>
	</div>
</body>
<script>
	function submitForm() {
		submit("${customerID}");
	};
</script>

</html>