package com.PJ_s200Testbed.model;

public class ComplexChild {
	public int cc_pk;
	public int c_fk;
	public int cc_fk;
	public String title;
	public String value;
	
	public int getCc_pk() {
		return cc_pk;
	}
	public void setCc_pk(int cc_pk) {
		this.cc_pk = cc_pk;
	}
	public int getC_fk() {
		return c_fk;
	}
	public void setC_fk(int c_fk) {
		this.c_fk = c_fk;
	}
	public int getCc_fk() {
		return cc_fk;
	}
	public void setCc_fk(int cc_fk) {
		this.cc_fk = cc_fk;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
}
