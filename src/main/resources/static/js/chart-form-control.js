function submit(customerId) {
	var bins = parseInt($("[name=bins]").val());
	var request = {
		chartType: $("[name=chartType]").val(),
		bins: 11,
		customerId: customerId,
		datasetName: $("[name=name]").val(),
		options: $("[name=options]").val(),
		columns: $("[name=columns]").val()
	};
	
//	private String customerId;
//	private String datasetName;
//	private String chartType;
//	private String columns;
//	private Integer bins;
//	private String options;
	debugger;
	$.ajax({
		type : "POST",
		contentType : "application/json",
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