package com.PJ_s200Testbed.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class InformationBinding {
    public int informationType_fk;
    public int informationBinding_pk;
    public String lower;
    public String upper;
    public String attribute = "null";
    public String sequential ="false";
    public String nil;
    public String infinite;
    
	public int getInformationType_fk() {
		return informationType_fk;
	}
	public void setInformationType_fk(int informationType_fk) {
		this.informationType_fk = informationType_fk;
	}
	public int getInformationBinding_pk() {
		return informationBinding_pk;
	}
	public void setInformationBinding_pk(int informationBinding_pk) {
		this.informationBinding_pk = informationBinding_pk;
	}
	public String getLower() {
		return lower;
	}
	public void setLower(String lower) {
		this.lower = lower;
	}
	public String getUpper() {
		return upper;
	}
	public void setUpper(String upper) {
		this.upper = upper;
	}
	public String getAttribute() {
		return attribute;
	}
	public void setAttribute(String attribute) {
		this.attribute = attribute;
	}
	public String getSequential() {
		return sequential;
	}
	public void setSequential(String sequential) {
		this.sequential = sequential;
	}
	public String getNil() {
		return nil;
	}
	public void setNil(String nil) {
		this.nil = nil;
	}
	public String getInfinite() {
		return infinite;
	}
	public void setInfinite(String infinite) {
		this.infinite = infinite;
	}
}
