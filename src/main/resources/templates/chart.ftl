<html>
<head>
	<title>Scattershot</title>
    <script src="//code.jquery.com/jquery-1.11.0.min.js"></script>
    <script src="//d3js.org/d3.v3.min.js" charset="utf-8"></script>
    <script src="/js/d3-tip.js"></script>
    <script src="/js/bar-chart.js"></script>
    <script src="/js/line-chart.js"></script>
    <script src="/js/chart-control.js"></script>
    <link rel="stylesheet" href="/css/chart/style.css">
</head>
<body>
	
</body>
<script>
	<#if chartType?? && chartType?is_string && chartType == "line">
		test("${dataset?json_string}", "${chartType}", "${xType}", "${yType}", ${width?c}, ${height?c});
	<#else>
		test("${dataset?json_string}", "${chartType}", null, null, ${width?c}, ${height?c});
	</#if>
</script>
</html>