<html>

<head>
    <title>${title}</title>
	<link href="http://fonts.googleapis.com/css?family=Varela" rel="stylesheet" />
	<link href="/css/main/default.css" rel="stylesheet" type="text/css" media="all" />
	<link href="/css/main/fonts.css" rel="stylesheet" type="text/css" media="all" />
	<link href="/css/animate.css" rel="stylesheet" type="text/css" media="all" />

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
				<h1 class="animated tada"><a href="/${customer.id}/homepage">Welcome ${userFirstName}</a></h1>
				</div>
				<div id="menu">
					<ul>
						<li class="current_page_item"><a href="/${customer.id}/homepage" accesskey="1" title="">Homepage</a></li>
						<li><a href="#" onclick="goToDisplayPage()" accesskey="2" title="">Display Charts</a></li>
						<li><a href="#" onclick="goToUploadPage()" accesskey="3" title="">Upload</a></li>
						<li><a href="#" accesskey="4" title="">Contact Us</a></li>
					</ul>
				</div>
			</div>
		</div>
	</div>

	<div id="banner">
		<div class="container animated bounceInUp">
			<div class="title">
				<h2>Display my charts</h2>
				<a href="#" onclick="goToDisplayPage()"><span class="byline">Go to list of charts</span></a>
			</div>
		</div>
	</div>

	<div id="banner">
		<div class="container animated bounceInUp">
			<div class="title">
				<h2>Upload a new chart</h2>
				<a href="#" onclick="goToUploadPage()"><span class="byline">go to upload page</span></a>
			</div>
		</div>
	</div>

</body>

</html>