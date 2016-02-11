<html>
<head>
	<script src="//code.jquery.com/jquery-1.11.0.min.js"></script>
	<script src="/js/chart-form-control.js"></script>
</head>
<body>
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
		<input type="submit" value="Upload"> Press here to upload the file!
	</div>
</body>
</html>