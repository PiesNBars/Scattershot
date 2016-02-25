<html>
<head>
	<title>Scattershot</title>
    <script src="//code.jquery.com/jquery-1.11.0.min.js"></script>
    <script src="//d3js.org/d3.v3.min.js" charset="utf-8"></script>
    <script src="/js/bar-chart.js"></script>
    <script src="/js/line-chart.js"></script>
    <script src="/js/chart-control.js"></script>
    <link rel="stylesheet" href="/css/chart/style.css">
</head>
<body>
	<h1>Look at your neat chart!</h1>
	<div class="chartContainer"></div>
	
	<div>Generate some sweet iframe code to embed this chart in your dumb web page! (And make it smarter ;)</div>
	<input type="number" name="width"/>
	<input type="number" name="height"/>
	<button onClick="generateIframeCode()">Click it or Ticket!</button>
	<div class="codeContainer"></div>
	
	
</body>
<script>
	<#if chartType?? && chartType?is_string && chartType == "line">
		test("${dataset?json_string}", "${chartType}", "${xType}", "${yType}", ${width?c}, ${height?c});
	<#else>
		test("${dataset?json_string}", "${chartType}", null, null, ${width?c}, ${height?c});
	</#if>
	
	var generateIframeCode = function() {
		var width = $("[name=width]").val();
		var height = $("[name=height]").val();
		var url = '"http://www.scattershot.tech/chart/show/${chartID}?height=' + height + '&width=' + width + '"';
		var widthString = '"' + width + '"';
		var heightString = '"' + height + '"';
		var embed = "<iframe src=" + url + " width=" + widthString + " height=" + heightString + " frameborder=\"0\" scrolling=\"no\"></iframe>";
		
		$(".codeContainer").text(embed);
	};
</script>
</html>