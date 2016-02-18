package io.scattershot.util;

import java.util.concurrent.Callable;

public interface Mappable<T, U> extends Callable<U> {
	public Mappable<T, U> withArgument(T arg);
	public U call();
}
