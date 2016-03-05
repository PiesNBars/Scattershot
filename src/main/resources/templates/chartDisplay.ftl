<html>
<head>
	<title>ChartDisplay</title>
    <script src="//code.jquery.com/jquery-1.11.0.min.js"></script>
    <script src="//d3js.org/d3.v3.min.js" charset="utf-8"></script>
    <script src="/js/d3-tip.js"></script>
    <script src="/js/bar-chart.js"></script>
    <script src="/js/line-chart.js"></script>
    <script src="/js/chart-control.js"></script>
    <link href="http://fonts.googleapis.com/css?family=Varela" rel="stylesheet" />
	<link href="/css/main/default.css" rel="stylesheet" type="text/css" media="all" />
	<link href="/css/main/fonts.css" rel="stylesheet" type="text/css" media="all" />
	<link href="/css/chart/chartDisplay.css" rel="stylesheet" type="text/css" />
	<link href="/css/chart/style.css" rel="stylesheet" type="text/css" />
</head>
<body>
	<div id="wrapper">
		<div id="header-wrapper">
			<div id="header" class="container">
				<div id="logo">
				<h1><a href="#">My Charts Page</a></h1>
				</div>
				<div id="menu">
					<ul>
						<li><a href="#" accesskey="1" title="">Homepage</a></li>
						<li class="current_page_item"><a href="#" onclick="goToDisplayPage()" accesskey="2" title="">Display Charts</a></li>
						<li><a href="#" onclick="goToUploadPage()" accesskey="3" title="">Upload</a></li>
						<li><a href="/about.html" accesskey="4" title="">About Us</a></li>
					</ul>
				</div>
			</div>
		</div>
	</div>
	<div id="banner">
		<h1>Look at your neat chart!</h1>
		<div class="chartContainer"></div>

		<div>Generate some sweet iframe code to embed this chart in your dumb web page! (And make it smarter ;)</div><br/>
		Width: <input type="number" name="width"/><br/>
		Height: <input type="number" name="height"/><br/>
		<button onClick="generateIframeCode()">Generate</button>
		<div class="codeContainer"></div>
	
	</div>
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