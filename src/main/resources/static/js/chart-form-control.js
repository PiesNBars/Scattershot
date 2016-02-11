function submit(customerId) {
	var bins = parseInt($("[name=bins]").val());
	var request = {
		chartType: $("[name=chartType]").val(),
		aggregate: $("[name=aggregate]").val() == "true",
		bins: isNaN(bins) ? 0 : bins,
		customerId: customerId,
		name: $("[name=name]").val(),
		columns: $("[name=columns]").val().split(",").map( function(str) {
			return str.trim();
		})
	}
	
	$.ajax({
		type : "POST",
		contentType : "application/json; chartset=utf-8",
		url : "/chart/add",
		data : JSON.stringify(request),
		dataType : 'json',
		timeout : 100000,
		success : function(data) {
			console.log("SUCCESS: ", data);
		},
		error : function(e) {
			console.log("ERROR: ", e);
		},
		done : function(e) {
			console.log("DONE");
		}
	});
}