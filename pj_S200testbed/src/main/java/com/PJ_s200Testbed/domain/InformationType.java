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
public class InformationType {
    public int catalogue_fk;
    public int informationType_pk;
    public String name;
    public String definition;
    public String code;
    
	public int getCatalogue_fk() {
		return catalogue_fk;
	}
	public void setCatalogue_fk(int catalogue_fk) {
		this.catalogue_fk = catalogue_fk;
	}
	public int getInformationType_pk() {
		return informationType_pk;
	}
	public void setInformationType_pk(int informationType_pk) {
		this.informationType_pk = informationType_pk;
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
}
