package com.jeeps;

import java.io.Serializable;

@SuppressWarnings("serial")
public class upassChange implements Serializable{
	private boolean chgPass;
	private String newpass;
	public boolean isChgPass() {
		return chgPass;
	}
	public void setChgPass(boolean chgPass) {
		this.chgPass = chgPass;
	}
	public String getNewpass() {
		return newpass;
	}
	public void setNewpass(String newpass) {
		this.newpass = newpass;
	}
	public upassChange() {
		super();
	}
	
	
}
