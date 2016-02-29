package io.scattershot.exception;

public class DatasetNotFoundException extends RuntimeException {

	private static final long serialVersionUID = -1344900741291719303L;
	private static final String MESSAGE =
			"That dataset does not exist in our databases.";
	
	public DatasetNotFoundException() {
		super(MESSAGE);
	}

}
