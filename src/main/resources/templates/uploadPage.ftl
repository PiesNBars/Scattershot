<html>

<head>
    <title>${title}</title>
    <link rel="stylesheet" type="text/css" href="/css/style.css">
</head>

<body>
	<div> Customer id is: ${customerID}</div>
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
	</form>
</body>

</html>