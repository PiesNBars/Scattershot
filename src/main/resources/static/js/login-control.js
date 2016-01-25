function addCustomer() {
	var email = $('#RegisterEmail').val();
	var password = $('#RegisterPassword').val();
	var exist;

	if (email) {
		existCheck(email, function(result) {
			exist = result;
		});

		if (exist == "false") {
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
			alert("The email address you have entered is already registered");
		}
	}
	else {
		alert("Please use a valid email");
	}
}

function existCheck(email, callback) {
	data = { email: email };
	$.ajax(
		{
			type : "POST",
			url  : "/customer/checkExist",
			async: false,
			data : data,
			success : function(result) {
				alert("success");
				$('#status').text(result);
				callback(result);

			},
			error: function (jqXHR, exception) {
				alert("error");
				$('#status').text("Failed to get the status");
			}
		});
}