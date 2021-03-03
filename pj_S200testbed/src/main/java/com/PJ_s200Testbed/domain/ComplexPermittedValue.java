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
public class ComplexPermittedValue {
    public int complexBinding_fk;
    public String value;
    
	public int getComplexBinding_fk() {
		return complexBinding_fk;
	}
	public void setComplexBinding_fk(int complexBinding_fk) {
		this.complexBinding_fk = complexBinding_fk;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
}
