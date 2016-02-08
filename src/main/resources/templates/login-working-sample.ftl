<#import "/spring.ftl" as spring />
<@spring.bind "customer" />
<html>

<head>
	<title>${title}</title>

	<script src="/js/login-control.js"></script>

	<script src="//code.jquery.com/jquery-1.11.0.min.js"></script>
</head>

<body>
	<form action="login" method="post">
		<table>
			<tr>
				<td>Email: </td>
				<td><@spring.formInput "customer.email" /></td>
			</tr>
			<tr>
				<td>Password: </td>
				<td><@spring.formInput "customer.password" /></td>
			</tr>
			<tr>
				<td><input type="submit" onclick="addCustomer()" value="Register" /></td>
			</tr>
		</table>
	</form>
</body>
</html>