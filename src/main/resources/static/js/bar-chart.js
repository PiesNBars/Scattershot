if(!$.scattershot)
	$.scattershot = {};

$.scattershot.barChart = (function() {
	
	var getColorScheme = function(palette) {
		
		// contstant
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
				categorides: palette,
				highlght: palette[0]
			}
		
	};
	
	var determineXAxisLayoutFromBarCount = function(barCount) {
		var layout = {
			chartBottomMargin: determineYAxisMarginFromBarCount(barCount),
			dx: determineLabelXShiftFromBarCount(barCount),
			dy: determineLabelYShiftFromBarCount(barCount),
			textAnchor: determineTextAnchorFromBarCount(barCount),
			rotation: getRotateAttributeValue(determineRotationAngleFromBarCount(barCount))
		};
		return layout;
	};
	
	var determineRotationAngleFromBarCount = function(barCount) {
		if (barCount > 30)
			return -90;
		if (barCount > 12)
			return -45;
		return 0;
	};
	
	var determineYAxisMarginFromBarCount = function(barCount) {
		if (barCount > 30)
			return .2;
		if (barCount > 12)
			return 0.11;
		return .065;
	};
	
	var determineLabelXShiftFromBarCount = function(barCount) {
		if (barCount > 12)
			return -8;
		return 0;
	};
	
	var determineLabelYShiftFromBarCount = function(barCount) {
		if (barCount > 30)
			return -5;
		if (barCount > 12)
			return 3;
		return 0;
	};
	
	var determineTextAnchorFromBarCount = function(barCount) {
		if (barCount > 12)
			return "end";
		return "middle";
	};
	

	var createToolTipElement = function(getDisplayValueFromData) {
		return d3.tip()
			.attr("class", "d3-tip")
			.offset([-10, 0])
			.html(getDisplayValueFromData);
	};
	
	var getRotateAttributeValue = function(number) {
		if (!(typeof number === "number"))
			return "rotate(0)";
		return "rotate(" + number + ")";
	};
	
	var that = {
		create: function(data, width, height, palette) {
			// This code taken largely from the example at
			// https://bl.ocks.org/mbostock/3885304.
			
			var xAxisLayout = determineXAxisLayoutFromBarCount(data.length);
			var bottomMargin = xAxisLayout.chartBottomMargin;
			
			var marginRatio = {
					top: 0.036,
					right: 0.02,
					bottom: bottomMargin,
					left: 0.039};
			
			var margin = {
				right: marginRatio.right * width,
				left: marginRatio.left * width,
				top: marginRatio.top * height,
				bottom: marginRatio.bottom * height
			};
			var chartHeight = height - margin.top - margin.bottom;
			var chartWidth = width - margin.left - margin.right;
			
			var color = getColorScheme(palette);
			var barColor = d3.scale.ordinal().range(color.categories);

			var x = d3.scale.ordinal()
			    .rangeRoundBands([0, chartWidth], .1);
			
			var y = d3.scale.linear()
			    .range([chartHeight, 0]);
			
			var xAxis = d3.svg.axis()
			    .scale(x)
			    .orient("bottom");
			
			var yAxis = d3.svg.axis()
			    .scale(y)
			    .orient("left");
			
			var container = d3.select(".chartContainer").empty() ? d3.select("body") :
																   d3.select(".chartContainer");
			
			var svg = container.append("svg")
			    .attr("width", width)
			    .attr("height", height)
			  .append("g")
			    .attr("transform", "translate(" + margin.left + "," + margin.top + ")");
			
			var getYValueFromDatum = function(d) {
				return "<strong>Value:</strong> <span style='color:red'>" + d.value + "</span>"
			};
			
			var tip = createToolTipElement(getYValueFromDatum);
			
			svg.call(tip);
			
			x.domain(data.map(function(d) { return d.key; }));
			y.domain([Math.min(d3.min(data, function(d) { return d.value; }), 0),
			          d3.max(data, function(d) { return d.value; })]);
			
			svg.append("g")
			    .attr("class", "x axis")
			    .attr("transform", "translate(0," + chartHeight + ")")
			    .call(xAxis)
			    .selectAll("text")
			    	.style("text-anchor", xAxisLayout.textAnchor)
			    	.attr("dx", xAxisLayout.dx)
			    	.attr("dy", xAxisLayout.dy)
			    	.attr("transform", xAxisLayout.rotation);
			
			svg.append("g")
			    .attr("class", "y axis")
			    .call(yAxis);
			
			svg.selectAll(".bar")
			    .data(data)
			    .enter().append("rect")
			    .attr("class", "bar")
			    .attr("x", function(d) { return x(d.key); })
			    .attr("width", x.rangeBand())
			    .attr("y", function(d) { return y(d.value); })
			    .attr("height", function(d) { return chartHeight - y(d.value); })
			    .style("fill", function(d) { return barColor(d.key); })
			    .on("mouseover", function() { d3.select(this).style("fill", color.highlight); })
			    .on("mouseout", function(d) { d3.select(this).style("fill", barColor(d.key)); })
			    .on("mouseover.tip", tip.show)
			    .on("mouseout.tip", tip.hide);

		}	
	};
	
	return that;
})();