<html>
<head>
	<script src="//code.jquery.com/jquery-1.11.0.min.js"></script>
</head>
<body>

<#list charts as chart>
	<div width="100", height="120">
		<h3>${chart.name}</h3>
		<iframe src="http://localhost:8080/chart/show/${chart.id}?height=100&width=100" width="100" height="100" frameborder="0" scrolling="no"></iframe>
	</div>
</#list>
</body>
</html>