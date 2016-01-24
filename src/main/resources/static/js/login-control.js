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
	var email = $('#RegisterEmail').val();
	var password = $('#RegisterPassword').val();

	var exist;
	if (email) {
		existCheck(function(result) {
			exist = result;
		});
alert("test: " + exist);
		$.ajax(
				{
					type: "POST",
					url: "/customers",
					contentType: "application/json",
					data: JSON.stringify({
						"email": email,
						"password": password
					}),
					complete: function(xhr) {
						if (xhr.readyState == 4) {
							if (xhr.status == 201) {
								alert("Created");
							}
						}
						else {
							alert(JSON.stringfy(xhr));
						}
					}
				});
	}
	else {
		alert("Please use a valid email");
	}
}

function existCheck(callback) {
	$.ajax(
			{
				type : "GET",
				url  : "/customer/checkExist",
				async: false,
				data : {
				},
				success : function(result) {
					$('#status').text(result);
					callback(result);
					
				},
				error: function (jqXHR, exception) {
					$('#status').text("Failed to get the status");
				}
			});
}