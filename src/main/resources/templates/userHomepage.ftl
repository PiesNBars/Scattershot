<html>

<head>
    <title>${title}</title>
	<link href="http://fonts.googleapis.com/css?family=Varela" rel="stylesheet" />
	<link href="/css/main/default.css" rel="stylesheet" type="text/css" media="all" />
	<link href="/css/main/fonts.css" rel="stylesheet" type="text/css" media="all" />

	<script src="/js/login-control.js"></script>
	<script src="/js/pageControl.js"></script>

	<script src="//code.jquery.com/jquery-1.11.0.min.js"></script>
</head>

<body>
	<script>
		var customerId = "${customer.id}";
	</script>

	<div id="wrapper">
		<div id="header-wrapper">
			<div id="header" class="container">
				<div id="logo">
					<h1><a href="#">User page, Welcome ${userFirstName} and ${customer.lastName}</a></h1>
				</div>
			</div>
		</div>
	</div>

	<div id="banner">
		<div class="container">
			<div class="title">
				<h2>Display my charts</h2>
				<span class="byline">Go to list of charts</span>
			</div>
			<ul class="actions">
				<li><button onclick="goToDisplayPage()">Add</button></li>
			</ul>
		</div>
	</div>

</body>

</html>