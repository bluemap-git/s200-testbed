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
public class CatalogueAttribute {
    public int catalogue_fk;
    public int catalogueAttribute_pk;
    public String camecase;
    public String value;
    public String parents;
    
	public int getCatalogue_fk() {
		return catalogue_fk;
	}
	public void setCatalogue_fk(int catalogue_fk) {
		this.catalogue_fk = catalogue_fk;
	}
	public int getCatalogueAttribute_pk() {
		return catalogueAttribute_pk;
	}
	public void setCatalogueAttribute_pk(int catalogueAttribute_pk) {
		this.catalogueAttribute_pk = catalogueAttribute_pk;
	}
	public String getCamecase() {
		return camecase;
	}
	public void setCamecase(String camecase) {
		this.camecase = camecase;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getParents() {
		return parents;
	}
	public void setParents(String parents) {
		this.parents = parents;
	} 
}
