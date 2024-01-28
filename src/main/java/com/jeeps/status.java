package com.jeeps;

import java.io.Serializable;

@SuppressWarnings("serial")
public class status implements Serializable {
	private int code;
	private String message;
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public status(int code, String message) {
		super();
		this.code = code;
		this.message = message;
	}
	
}
