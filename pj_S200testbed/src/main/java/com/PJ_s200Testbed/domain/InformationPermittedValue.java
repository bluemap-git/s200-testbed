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
public class InformationPermittedValue {
    public int informationBinding_fk;
    public String value;
    
	public int getInformationBinding_fk() {
		return informationBinding_fk;
	}
	public void setInformationBinding_fk(int informationBinding_fk) {
		this.informationBinding_fk = informationBinding_fk;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
}
