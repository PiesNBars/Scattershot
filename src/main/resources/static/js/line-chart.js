if(!$.scattershot)
	$.scattershot = {};

//Minor change comment
$.scattershot.lineChart = (function() {

	var getColorScheme = function(palette) {

		// constant
		var defaultPalette = ["steelblue", "darkred"];

		if(!palette || !Array.isArray(palette) || palette.length < 1)
			palette = defaultPalette;

		if(palette.length > 1)
			return {
			categories: palette.slice(0, palette.length - 1),
			highlight: palette[palette.length - 1]
		};
		else
			return {
			categories: palette,
			highlght: palette[0]
		};
	};
	
	var stringContainsSubstring = function(string, substring) {
		return string.indexOf(substring) >= 0;
	};
	
	var getType = function (dataType) {
		var lowerCaseType = dataType.toLowerCase();
		
		if(stringContainsSubstring(lowerCaseType, "date")) {
			return "date";
		} else if (stringContainsSubstring(lowerCaseType, "string")) {
			return "string"
		} else {
			return "number";
		}
	};
	
	var isDate = function(dataType) {
		return (dataType.toLowerCase().indexOf("date") >= 0);
	};
	
	var convertDates = function(data, field) {
		return data.map(function(d) {
			d[field] = new Date(d[field]);
			return d;
		});
	};
	
	var sortDates = function(data, field) {
		var comparator = function(a, b) {
			if(a[field].getTime() > b[field].getTime())
				return 1;
			if(b[field].getTime() > a[field].getTime())
				return -1;
			return 0;
		};
		
		return data.sort(comparator);
	};
	
	var createToolTipElement = function(getDisplayValueFromData) {
		return d3.tip()
			.attr("class", "d3-tip")
			.offset([-10, 0])
			.html(getDisplayValueFromData);
	};
	
	var sortNumeric = function(data, field) {
		var compareNumbers = function(a, b) {
			if(a[field] > b[field])
				return 1;
			if(b[field] > a[field])
				return -1;
			return 0;
		}
		
		return data.sort(compareNumbers);
	};

	var getScale = function(data, dataType, field) {

		var getValue = function(d) { return d[field]; };
		var scale = null;
		var domain = null;

		if(dataType.toLowerCase().indexOf("date") >= 0) {
			scale = d3.time.scale();
		} else if(dataType.toLowerCase().indexOf("string") >= 0) {
			scale = d3.scale.ordinal();
		} else {
			scale = d3.scale.linear();
		}

		domain = [d3.min(data, getValue), d3.max(data, getValue)];
		scale.domain(domain);

		return scale;
	};

	var that = {

			// This code taken largely from the example at
			// http://bl.ocks.org/mbostock/1667367
		create: function(dataset, width, height, xType, yType, color) {
			
			if(isDate(xType))
				dataset = sortDates(convertDates(dataset, "x"), "x");
			
			if(getType(xType) === "number")
				dataset = sortNumeric(dataset, "x");
			
			if(isDate(yType))
				dataset = convertDates(dataset, "y");

			var focusMarginRatio = {top: 0.01, right: 0.01, bottom: 0.2, left: 0.047};
			var contextMarginRatio = {top: 0.86, right: 0.01, bottom: 0.04, left: 0047};

			var focusMargin = {
					right: focusMarginRatio.right * width,
					left: focusMarginRatio.left * width,
					top: focusMarginRatio.top * height,
					bottom: focusMarginRatio.bottom * height
			};

			var contextMargin = {
					right: contextMarginRatio.right * width,
					left: contextMarginRatio.left * width,
					top: contextMarginRatio.top * height,
					bottom: contextMarginRatio.bottom * height
			};


			var plotWidth = width - focusMargin.right - focusMargin.left;
			var focusHeight = height - focusMargin.top - focusMargin.bottom;
			var contextHeight = height - contextMargin.top - contextMargin.bottom;

			var focusXScale = getScale(dataset, xType, "x").range([0, plotWidth]);
			var focusYScale = getScale(dataset, yType, "y").range([focusHeight, 0]);

			var contextXScale = getScale(dataset, xType, "x").range([0, plotWidth]);
			var contextYScale = getScale(dataset, yType, "y").range([contextHeight, 0]);

			var focusXAxis = d3.svg.axis()
				.orient("bottom")
				.scale(focusXScale);

			var focusYAxis = d3.svg.axis()
				.orient("left")
				.scale(focusYScale);

			var contextXAxis = d3.svg.axis()
				.orient("bottom")
				.scale(contextXScale);

			var focusLine = d3.svg.line()
				.interpolate("linear")
				.x(function(d) { return focusXScale(d.x); })
				.y(function(d) { return focusYScale(d.y); });

			var contextLine = d3.svg.line()
				.interpolate("linear")
				.x(function(d) { return contextXScale(d.x); })
				.y(function(d) { return contextYScale(d.y); });


			var container = d3.select(".chartContainer").empty() ? d3.select("body") :
																   d3.select(".chartContainer");

			var svg = container.append("svg")
				.attr("width", width)
				.attr("height", height);

			var brush = d3.svg.brush()
				.x(contextXScale)
				.on("brush", brushed);

			var focus = svg.append("g")
				.attr("class", "focus")
				.attr("transform", "translate(" + focusMargin.left + "," + focusMargin.top + ")");

			var context = svg.append("g")
				.attr("class", "context")
				.attr("transform", "translate(" + focusMargin.left + "," + contextMargin.top + ")");

			var getYValueFromDatum = function(d) {
				return "<strong>Value:</strong> <span style='color:red'>" + d.y + "</span>"
			};

			var tip = createToolTipElement(getYValueFromDatum);
			
			svg.call(tip);
			
			color = color || "steelblue";

			svg.append("defs").append("clipPath")
				.attr("id", "clip")
				.append("rect")
				.attr("width", plotWidth)
				.attr("height", focusHeight);

			focus.append("path")
				.datum(dataset)
				.attr("class", "line")
				.attr("d", focusLine)
				.style("stroke", color)
				.style("fill", "none");

			focus.append("g")
				.attr("class", "x axis")
				.attr("transform", "translate(0," + focusHeight + ")")
				.call(focusXAxis);

			focus.append("g")
				.attr("class", "y axis")
				.call(focusYAxis);
			
			focus.selectAll(".point")
				.data(dataset)
				.enter().append("circle")
				.attr("class", "point")
				.attr("cx", function(d) { return focusXScale(d.x); })
				.attr("cy", function(d) { return focusYScale(d.y); })
				.attr("r", 2)
				.style("fill", color)
				.on("mouseover", tip.show)
				.on("mouseout", tip.hide);

			context.append("path")
				.datum(dataset)
				.attr("class", "line")
				.attr("d", contextLine)
				.style("stroke", color)
				.style("fill", "none");

			context.append("g")
				.attr("class", "x axis")
				.attr("transform", "translate(0," + contextHeight + ")")
				.call(contextXAxis);

			context.append("g")
				.attr("class", "x brush")
				.call(brush)
				.selectAll("rect")
				.attr("y", -6)
				.attr("height", contextHeight + 7);
			
			context.selectAll(".point")
				.data(dataset)
				.enter().append("circle")
				.attr("class", "point")
				.attr("cx", function(d) { return contextXScale(d.x); })
				.attr("cy", function(d) { return contextYScale(d.y); })
				.attr("r", 2)
				.style("fill", color);

			function brushed() {
				focusXScale.domain(brush.empty() ? contextXScale.domain() : brush.extent());
				focus.select(".line").attr("d", focusLine);
				focus.selectAll(".point")
					.attr("cy", function(d) { return focusYScale(d.y); })
					.attr("cx", function(d) { return focusXScale(d.x); })
				focus.select(".x.axis").call(focusXAxis);
			}
		}
	};

	return that;

})();
