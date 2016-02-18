package io.scattershot.util;

import java.io.Serializable;

public class ParseResult<T extends Serializable> {

	T value;

	public Class<? extends Serializable> getClazz() {
		return value.getClass();
	}

	public ParseResult(T value) {
		this.value = value;
	}

	public T getValue() {
		return value;
	}
}