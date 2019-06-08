package com.rsi.kino.client.exception;

import java.util.function.Function;

public final class LambdaExceptionWrapper {

	@FunctionalInterface
	public interface Function_WithExceptions<T, R, E extends Exception> {
		R apply(T t) throws E;
	}


	public static <T, R, E extends Exception> Function<T, R> rethrowFunction(Function_WithExceptions<T, R, E> function) throws E  {
		return t -> {
			try {
				return function.apply(t);
			} catch (Exception exception) {
				throwActualException(exception);
				return null;
			}
		};
	}

	@SuppressWarnings("unchecked")
	private static <E extends Exception> void throwActualException(Exception exception) throws E {
		throw (E) exception;
	}
}
