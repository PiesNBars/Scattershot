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