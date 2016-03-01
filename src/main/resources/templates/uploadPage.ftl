<html>

<head>
    <title>${title}</title>
    <link href="http://fonts.googleapis.com/css?family=Varela" rel="stylesheet" />
	<link href="/css/main/default.css" rel="stylesheet" type="text/css" media="all" />
	<link href="/css/main/fonts.css" rel="stylesheet" type="text/css" media="all" />
	<script src="/js/pageControl.js"></script>
</head>

<body>
	<script>
		var customerId = "${customerID}";
	</script>

	<div id="wrapper">
		<div id="header-wrapper">
			<div id="header" class="container">
				<div id="logo">
				<h1><a href="/${customerID}/homepage">Upload Page</a></h1>
				</div>
				<div id="menu">
					<ul>
						<li><a href="/${customerID}/homepage" accesskey="1" title="">Homepage</a></li>
						<li><a href="#" onclick="goToDisplayPage()" accesskey="2" title="">Display Charts</a></li>
						<li class="current_page_item"><a href="#" onclick="goToUploadPage()" accesskey="3" title="">Upload</a></li>
						<li><a href="#" accesskey="4" title="">Contact Us</a></li>
					</ul>
				</div>
			</div>
		</div>
	</div>

	<div id="banner">
		<div class="form">
			<form method="POST" enctype="multipart/form-data" action="/upload">
				<h2>Please Use this form to upload your dataset
				<input type="hidden" name="id" value="${customerID}" />
				Column names: <input type="text" name="header">
				<br />

				Does this file contain a header row (column names)?:
				<input type="radio" name="hasOwnHeaders" value="true" checked/>Yes
				<input type="radio" name="hasOwnHeaders" value="false"/>No
				<br />

				File to upload: <input type="file" name="file">
				<br />

				Name: <input type="text" name="name">
				<br />
				<br />

				<input type="submit" value="Upload"> Press here to upload the file!
			</form>
		</div>
	</div>

	<#-- <div> Customer id is: ${customerID}</div>
	<button onclick="goToHomepage()" class="button">Homepage</button>
   	<form method="POST" enctype="multipart/form-data" action="/upload">
		<input type="hidden" name="id" value="${customerID}" />
		Column names: <input type="text" name="header">
		<br />

		Does this file contain a header row (column names)?:
		<input type="radio" name="hasOwnHeaders" value="true" checked/>Yes
		<input type="radio" name="hasOwnHeaders" value="false"/>No
		<br />

		File to upload: <input type="file" name="file">
		<br />

		Name: <input type="text" name="name">
		<br />
		<br />

		<input type="submit" value="Upload"> Press here to upload the file!
	</form> -->
</body>

</html>