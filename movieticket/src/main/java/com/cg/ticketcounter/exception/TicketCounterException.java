package com.cg.ticketcounter.exception;

public class TicketCounterException extends Exception {

	/**
	* 
	*/
	private static final long serialVersionUID = 1L;

	private int code;
	private String status;

	public TicketCounterException(String status) {
		super(status);
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public TicketCounterException(int code, String status) {
		super();
		this.code = code;
		this.status = status;
	}
}
