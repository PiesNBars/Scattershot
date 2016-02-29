package io.scattershot.exception;

public class FormProcessingException extends RuntimeException{

	private static final long serialVersionUID = -1344900741291719303L;
	private static final String MESSAGE =
			"It looks like we weren't able to process that request. Check your " +
			"input and try again. If you are trying to make a histogram, make " +
			"sure the attribute you select is numeric.";
	
	public FormProcessingException() {
		super(MESSAGE);
	}

}
