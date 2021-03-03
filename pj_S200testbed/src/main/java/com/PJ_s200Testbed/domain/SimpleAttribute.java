package com.PJ_s200Testbed.domain;

import java.util.List;

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
public class SimpleAttribute {
    public int catalogue_fk;
    public int simpleAttribute_pk;
    public String name;
    public String definition;
    public String code;
    public String alias;
    public String valueType;
    public String remarks;
    
	public int getCatalogue_fk() {
		return catalogue_fk;
	}
	public void setCatalogue_fk(int catalogue_fk) {
		this.catalogue_fk = catalogue_fk;
	}
	public int getSimpleAttribute_pk() {
		return simpleAttribute_pk;
	}
	public void setSimpleAttribute_pk(int simpleAttribute_pk) {
		this.simpleAttribute_pk = simpleAttribute_pk;
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
	public String getAlias() {
		return alias;
	}
	public void setAlias(String alias) {
		this.alias = alias;
	}
	public String getValueType() {
		return valueType;
	}
	public void setValueType(String valueType) {
		this.valueType = valueType;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
    
}
