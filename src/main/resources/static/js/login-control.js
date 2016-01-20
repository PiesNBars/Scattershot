function addUser() {
	alert("in add user");
	alert("password: ");
	var userName = $('#Username').val();
	var password = $('#Password').val();

	alert("username: " + userName);
	alert("password: " + password);
	if (userName == userName) {
		alert("in if");
		$.ajax(
				{
					type: "POST",
					url: "/cs580/user/" + userName,
					data: {
						"name": userName,
						"major": password
					},
					success: function (result) {
						alert("success");
						location.reload();
					},
					error: function (jqXHR, exception) {
						alert("Failed to add the user. Please check the inputs.");
					}
				});
	} else {
		alert("Invalid user Id");
	}
}

function addCustomer() {
	var userName = $('#Username').val();
	var password = $('#Password').val();

	if (userName) {
		$.ajax(
				{
					type: "POST",
					url: "/customers",
					contentType: "application/json",
					data: JSON.stringify({
						"email": userName,
						"password": password
					}),
					success: function (result) {
						alert("success");
						location.reload();
					},
					error: function (jqXHR, exception) {
						alert("Failed.");
					}
				});
	}
	else {
		alert("Invalid user Id");
	}
}