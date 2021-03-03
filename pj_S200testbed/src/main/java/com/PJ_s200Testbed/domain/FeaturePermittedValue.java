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
public class FeaturePermittedValue {
    public int attributeBinding_fk;
    public String value;
    
	public int getAttributeBinding_fk() {
		return attributeBinding_fk;
	}
	public void setAttributeBinding_fk(int attributeBinding_fk) {
		this.attributeBinding_fk = attributeBinding_fk;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
}
