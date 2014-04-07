package com.leisure.core.exception;

public class CanotRemoveObjectException extends RuntimeException {

	private static final long serialVersionUID = -2054002318882604747L;

	@Override
	public void printStackTrace() {
		System.out.println("it's wrong to romove the object");
		super.printStackTrace();
	}
}
