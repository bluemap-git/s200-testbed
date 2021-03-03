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
public class ListedValue {
    public int simpleAttribute_fk;
    public String label;
    public String definition;
    public String code;
    
	public int getSimpleAttribute_fk() {
		return simpleAttribute_fk;
	}
	public void setSimpleAttribute_fk(int simpleAttribute_fk) {
		this.simpleAttribute_fk = simpleAttribute_fk;
	}
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
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
}
