package io.scattershot.exception;

public class ChartNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 4658703062988734316L;
	private static final String MESSAGE =
			"We couldn't find the specified chart. If you reached this page " +
			"after embeding in a website, check that the chart still exists " +
			"in your user account on <a href=\"http://www.scattershot.tech\">scattershot</a>.";
	
	public ChartNotFoundException() {
		super(MESSAGE);
	}

}
