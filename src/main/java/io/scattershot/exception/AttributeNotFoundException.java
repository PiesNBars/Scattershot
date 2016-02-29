package io.scattershot.exception;

public class AttributeNotFoundException extends RuntimeException{

	private static final long serialVersionUID = 8367273017569061607L;
	private static final String MESSAGE =
			"Wow. Ok. So we can't seem to find that attribute (column) on this " +
			"dataset. This is totally on us. Sorry. We're working on it.";
	
	public AttributeNotFoundException() {
		super(MESSAGE);
	}
}
