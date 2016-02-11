<html>

<head>
    <title>${title}</title>
    <link rel="stylesheet" type="text/css" href="/css/style.css">
</head>

<body>
	<div>Customer ID is: ${customerID} </div>

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

</html>