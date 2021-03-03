package com.PJ_s200Testbed.domain;

import org.w3c.dom.Element;

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
public class ComplexAttribute {
    public int catalogue_fk;
    public int complex_pk;
    public String name;
    public String definition;
    public String code;
    public String remarks;
    
	public int getCatalogue_fk() {
		return catalogue_fk;
	}
	public void setCatalogue_fk(int catalogue_fk) {
		this.catalogue_fk = catalogue_fk;
	}
	public int getComplex_pk() {
		return complex_pk;
	}
	public void setComplex_pk(int complex_pk) {
		this.complex_pk = complex_pk;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDefinition() {
		return definition;
	}
	public void setDefinition(String definition) {
		this.definition = definition;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
}
