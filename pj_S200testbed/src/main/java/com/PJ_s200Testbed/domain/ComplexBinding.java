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
public class ComplexBinding {
    public int complex_fk;
    public int complexBinding_pk;
    public String lower;
    public String upper;
    public String attribute;
    public String sequential;
    public String nil;
    public String infinite;
    
	public int getComplex_fk() {
		return complex_fk;
	}
	public void setComplex_fk(int complex_fk) {
		this.complex_fk = complex_fk;
	}
	public int getComplexBinding_pk() {
		return complexBinding_pk;
	}
	public void setComplexBinding_pk(int complexBinding_pk) {
		this.complexBinding_pk = complexBinding_pk;
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
