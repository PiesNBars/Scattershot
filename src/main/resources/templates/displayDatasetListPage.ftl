<html>

<head>
    <title>${title}</title>
	<script src="//code.jquery.com/jquery-1.11.0.min.js"></script>
	<script src="/js/chart-form-control.js"></script>
	<script src="/js/pageControl.js"></script>
	<link href="http://fonts.googleapis.com/css?family=Varela" rel="stylesheet" />
	<link href="/css/main/default.css" rel="stylesheet" type="text/css" media="all" />
	<link href="/css/main/fonts.css" rel="stylesheet" type="text/css" media="all" />
</head>

<body>
	<script>
		var customerId = "${customerID}";
	</script>
	<div id="wrapper">
		<div id="header-wrapper">
			<div id="header" class="container">
				<div id="logo">
				<h1><a href="/${customerID}/homepage">Dataset List Page</a></h1>
				</div>
				<div id="menu">
					<ul>
						<li><a href="/${customerID}/homepage" accesskey="1" title="">Homepage</a></li>
						<li class="current_page_item"><a href="#" onclick="goToDisplayPage()" accesskey="2" title="">Display Charts</a></li>
						<li><a href="#" onclick="goToUploadPage()" accesskey="3" title="">Upload</a></li>
						<li><a href="/about.html" accesskey="4" title="">About Us</a></li>
					</ul>
				</div>
			</div>
		</div>
	</div>

	<div id="banner">
		<table>
	<#list customerDataset as dataset>
		<tr>
			<td>ChartName:</td>
			<td><button onclick="goToChartListPage('${dataset.id}')">
				${dataset.name}</button></td>
			<td>
				<button onclick="deleteDataset('${dataset.id}')">Delete</button>
			</td>
		</tr>
	</#list>
	</div>
</body>
</html>